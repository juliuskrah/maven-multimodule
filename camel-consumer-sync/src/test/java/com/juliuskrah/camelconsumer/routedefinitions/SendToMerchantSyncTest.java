package com.juliuskrah.camelconsumer.routedefinitions;

import java.util.ArrayList;

import com.juliuskrah.camel.consumer.common.api.MerchantIntegration;
import com.juliuskrah.camel.consumer.common.routebuilders.AbstractConsumerRouteBuilder;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author Julius Krah
 */
@SpringBootTest
@MockEndpoints
public class SendToMerchantSyncTest {
    @Autowired
    private CamelContext context;
    @MockBean
    private MerchantIntegration integration;
    @Produce("direct:springcontroller")
    private ProducerTemplate producer;
    @EndpointInject("mock:" + AbstractConsumerRouteBuilder.REPORT_TO_MONITORING)
    MockEndpoint validationMock;

    @Test
    void testSendToMerchantValidationFails() throws Exception {
        AdviceWith.adviceWith(context, "error-logger", advice -> advice.mockEndpoints("direct:*"));
        var violation = new ViolationResponse();
        var errors = new ArrayList<Violation>();
        errors.add(new Violation("protocol", "must not be blank"));
        errors.add(new Violation("merchantPaymentMSISDN", "MSISDN invalid length"));
        violation.getViolations().addAll(errors);
        var body = new MerchantPayment();
        body.setIsTokenService(2);
        body.setInvoiceNumber("1298521");
        body.setMerchantPaymentID(123L);
        body.setServiceUrl("https://192.168.251.100");
        body.setMerchantPaymentMSISDN("07023325335");
        
        producer.sendBody(body);
        validationMock.expectedBodiesReceived(violation);
        MockEndpoint.assertIsSatisfied(context);
    }

}
