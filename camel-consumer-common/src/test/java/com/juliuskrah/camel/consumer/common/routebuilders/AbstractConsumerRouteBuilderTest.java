package com.juliuskrah.camel.consumer.common.routebuilders;

import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import lombok.SneakyThrows;

/**
 * @author Julius Krah
 */
public class AbstractConsumerRouteBuilderTest extends CamelTestSupport {

    @Test
    @SneakyThrows
    public void sampleMockTest() throws InterruptedException {
        AdviceWith.adviceWith(context, "error-logger", advice -> advice.mockEndpoints("direct:*"));
        MockEndpoint errorsEndpoint = getMockEndpoint("mock:" + AbstractConsumerRouteBuilder.REPORT_TO_MONITORING);
        errorsEndpoint.expectedMessageCount(1);
        template.sendBody("direct:start", "");
        errorsEndpoint.expectedBodyReceived().body(Exception.class).isEqualTo(new Exception("Intentionally fails"));
        errorsEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new AbstractConsumerRouteBuilder() {
            protected void route() {
                from("direct:start").throwException(Exception.class, "Intentionally fails").end();
            }
        };
    }

}
