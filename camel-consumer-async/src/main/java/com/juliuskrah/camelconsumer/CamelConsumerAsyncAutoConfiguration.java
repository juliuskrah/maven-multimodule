package com.juliuskrah.camelconsumer;

import com.juliuskrah.camel.consumer.common.CamelConsumerProperties;
import com.juliuskrah.camel.consumer.common.processors.MerchantPaymentProcessor;
import com.juliuskrah.camelconsumer.routedefinitions.SendToMerchantAsync;

import org.apache.camel.CamelContext;
import org.apache.camel.component.jackson.JacksonConstants;
import org.apache.camel.component.rabbitmq.RabbitMQComponent;
import org.apache.camel.opentracing.starter.CamelOpenTracing;
import org.apache.camel.spi.ComponentCustomizer;
import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import brave.Tracing;
import brave.handler.SpanHandler;
import brave.opentracing.BraveTracer;
import brave.sampler.CountingSampler;
import brave.sampler.Sampler;
import io.opentracing.Tracer;
import io.opentracing.noop.NoopTracerFactory;
import zipkin2.Span;
import zipkin2.codec.SpanBytesEncoder;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Reporter;
import zipkin2.reporter.brave.AsyncZipkinSpanHandler;
import zipkin2.reporter.brave.ZipkinSpanHandler;
import zipkin2.reporter.okhttp3.OkHttpSender;

/**
 * @author Julius Krah
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CamelConsumerProperties.class)
@AutoConfigureAfter(value = { WebMvcAutoConfiguration.class })
@AutoConfigureBefore(value = { CamelAutoConfiguration.class })
@Import({ SendToMerchantAsync.class, MerchantPaymentProcessor.class })
@CamelOpenTracing()
public class CamelConsumerAsyncAutoConfiguration {
    @Autowired
    private CamelConsumerProperties properties;

    /**
     * Any customization of the Rabbit Component is done here
     */
    @Bean
    ComponentCustomizer rabbitMQCustomizer() {
        return (name, rabbitComponent) -> {
            if (rabbitComponent instanceof RabbitMQComponent) {
                // Add your customization code here
            }
        };
    }

    /**
     * Any additions to make on the camel context is done here
     */
    @Bean
    CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext context) {
                context.getGlobalOptions().put(JacksonConstants.ENABLE_TYPE_CONVERTER, "true");
                // allow Jackson json to convert to pojo types also (by default jackson only
                // converts to String and other simple types)
                context.getGlobalOptions().put(JacksonConstants.TYPE_CONVERTER_TO_POJO, "true");
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {
                // ignore
            }
        };
    }

    // OpenTelemetry has come to replace OpenTracing
    // however there are no stable APIs for OpenTelemetry yet
    // we will continue monitoring and update once there is
    @Bean
    @ConditionalOnProperty(name = "consumer.tracer.enabled", havingValue = "true")
    Tracer tracer(SpanHandler spanHandler) {
        final Tracing.Builder builder = Tracing.newBuilder() //
                .sampler(sampler()) //
                .localServiceName(properties.getTracer().getServiceName()) //
                .addSpanHandler(spanHandler);

        return BraveTracer.create(builder.build());
    }

    @Bean
    @ConditionalOnProperty(name = "consumer.tracer.enabled", havingValue = "false", matchIfMissing = true)
    Tracer noopTracer() {
        return NoopTracerFactory.create();
    }

    Reporter<Span> reporter() {
        var tracer = properties.getTracer();
        UriComponents uriComponents = UriComponentsBuilder.fromPath(tracer.getPath()) //
                .host(tracer.getAgentHost()) //
                .port(tracer.getAgentPort()) //
                .scheme(tracer.getScheme()) //
                .build();
        String url = uriComponents.toUriString();

        OkHttpSender sender = OkHttpSender.newBuilder().endpoint(url) //
                .encoding(SpanBytesEncoder.JSON_V2.encoding()).build();

        return AsyncReporter.builder(sender).build(SpanBytesEncoder.JSON_V2);
    }

    @Bean(destroyMethod = "close")
    ZipkinSpanHandler spanHandler() {
        var tracer = properties.getTracer();
        UriComponents uriComponents = UriComponentsBuilder.fromPath(tracer.getPath()) //
                .host(tracer.getAgentHost()) //
                .port(tracer.getAgentPort()) //
                .scheme(tracer.getScheme()) //
                .build();
        String url = uriComponents.toUriString();

        OkHttpSender sender = OkHttpSender.newBuilder().endpoint(url) //
                .encoding(SpanBytesEncoder.JSON_V2.encoding()).build();

        return AsyncZipkinSpanHandler.create(sender);
    }

    public Sampler sampler() {
        return CountingSampler.create(properties.getTracer().getProbability());
    }
}
