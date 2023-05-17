package com.juliuskrah.camel.consumer.common.routebuilders;

import java.util.Map;

import javax.validation.ConstraintViolation;

import com.juliuskrah.camel.consumer.common.wrappers.ViolationResponse;
import com.juliuskrah.camel.consumer.common.wrappers.ViolationResponse.Violation;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.springframework.beans.factory.annotation.Autowired;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.log.Fields;
import io.opentracing.tag.Tags;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Julius Krah
 */
@Slf4j
public abstract class AbstractConsumerRouteBuilder extends RouteBuilder {
    public static final String REPORT_TO_MONITORING = "direct:reportToMonitoring";
    @Autowired(required = false)
    io.opentracing.Tracer tracer;

    @Override
    public void configure() throws Exception {
        onException(BeanValidationException.class).process(new ExceptionHandlerProcessor()) //
                .handled(true).end();
        onException(Exception.class).process(new ExceptionHandlerProcessor()) //
                .handled(true).end();
        from(REPORT_TO_MONITORING).routeId("error-logger") //
                .process(this::processTrace) //
                .to("log:com.juliuskrah.camel.consumer.common.routebuilders?level=INFO").end();
        route();
    }

    /**
     * Report the error using APM
     * 
     * @param exchange exchange
     */
    private void processTrace(Exchange exchange) {
        if (tracer != null) {
            var obj = exchange.getIn().getBody();
            final Span span = tracer.buildSpan(exchange.getFromRouteId()).start();
            try (Scope scope = tracer.scopeManager().activate(span)) {
                Tags.ERROR.set(span, true);
                span.log(Map.of(Fields.EVENT, "error", Fields.ERROR_OBJECT, obj));
            } finally {
                span.finish();
            }
        }
    }
    
    protected abstract void route();

    public static class ExceptionHandlerProcessor implements Processor {

        @Override
        public void process(Exchange exchange) throws Exception {
            Throwable cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
            if (cause instanceof BeanValidationException) {
                ViolationResponse error = new ViolationResponse();
                var constraintViolations = ((BeanValidationException) cause).getConstraintViolations();
                for (ConstraintViolation<?> violation : constraintViolations) {
                    error.getViolations()
                            .add(new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
                }
                log.info("Validation failures: {}", error);
                exchange.getIn().setBody(error);
                try (var template = exchange.getContext().createProducerTemplate()) {
                    template.send(REPORT_TO_MONITORING, exchange);
                }
            } else {
                log.error("A generic exception has been observed", cause);
                exchange.getIn().setBody(cause);
                try (var template = exchange.getContext().createProducerTemplate()) {
                    template.send(REPORT_TO_MONITORING, exchange);
                }
            }
        }

    }

}
