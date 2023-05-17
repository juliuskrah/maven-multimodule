package com.juliuskrah.camelconsumer.routedefinitions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import com.juliuskrah.camel.consumer.common.api.MerchantIntegration;
import com.juliuskrah.camel.consumer.common.routebuilders.AbstractConsumerRouteBuilder;
import com.juliuskrah.camel.consumer.common.wrappers.Acknowledgement;
import com.juliuskrah.camel.consumer.common.wrappers.MerchantPayment;
import com.juliuskrah.camel.consumer.common.wrappers.ViolationResponse;
import com.juliuskrah.camel.consumer.common.wrappers.ViolationResponse.Violation;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.apache.camel.test.testcontainers.junit5.Wait;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author Julius Krah
 */
@SpringBootTest
@Testcontainers
@MockEndpoints
public class SendToMerchantAsyncTest {
    @Autowired
    private CamelContext context;
    @MockBean
    private MerchantIntegration integration;
    @Produce("rabbitmq:{{consumer.rabbit.request.exchange-name}}" //
            + "?queue={{consumer.rabbit.request.queue-name}}" //
            + "&exchangeType={{consumer.rabbit.request.exhange-type}}")
    private ProducerTemplate producer;
    @EndpointInject("mock:" + AbstractConsumerRouteBuilder.REPORT_TO_MONITORING)
    MockEndpoint validationMock;

    @EndpointInject("mock:rabbitmq:{{consumer.rabbit.acknowledgement.exchange-name}}")
    MockEndpoint rabbitMQMock;

    @Container
    private static final GenericContainer<?> rabbitMQContainer = new GenericContainer<>(
            "rabbitmq:3.8") //
                    .withExposedPorts(5672) //
                    .withEnv("RABBITMQ_DEFAULT_USER", "guest") //
                    .withEnv("RABBITMQ_DEFAULT_PASS", "guest") //
                    .waitingFor(Wait.forListeningPort());

    @Test
    void testSendToMerchantValidationFails() throws Exception {
        AdviceWith.adviceWith(context, "error-logger", advice -> advice.mockEndpoints("direct:*"));
        var violation = new ViolationResponse();
        var errors = new ArrayList<Violation>();
        errors.add(new Violation("protocol", "must not be blank"));
        violation.getViolations().addAll(errors);
        var body = "{\"isTokenService\":1, \"invoiceNumber \":\"1298521\", \"merchantPaymentID\":123, \"serviceUrl\": \"https://192.168.251.100\",\"merchantPaymentMSISDN\":\"0723325335\"}";
        producer.sendBody(body);
        validationMock.expectedBodiesReceived(violation);
        MockEndpoint.assertIsSatisfied(context);
    }

    @Test
    void testSendToMerchantValidationPass() throws Exception {
        AdviceWith.adviceWith(context, "message-processing", advice -> advice.mockEndpoints("rabbitmq:*"));
        var acknowledgement = new Acknowledgement();
        acknowledgement.setAmountExpected(1000.00);
        acknowledgement.setPayerTransactionID("123");
        when(integration.invoke(any(MerchantPayment.class))).thenReturn(acknowledgement);
        var body = "{\"isTokenService\":1, \"invoiceNumber \":\"1298521\", \"merchantPaymentID\":123, \"serviceUrl\": \"https://192.168.251.100\",\"merchantPaymentMSISDN\":\"0723325335\",\"protocol\":\"JSON\"}";
        producer.sendBody(body);
        rabbitMQMock.expectedBodiesReceived(acknowledgement);
        MockEndpoint.assertIsSatisfied(context);
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("camel.component.rabbitmq.port-number", rabbitMQContainer::getFirstMappedPort);
        registry.add("camel.component.rabbitmq.hostname", rabbitMQContainer::getContainerIpAddress);
    }
}
