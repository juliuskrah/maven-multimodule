package com.juliuskrah.camel.consumer.common.processors;

import com.juliuskrah.camel.consumer.common.api.MerchantIntegration;
import com.juliuskrah.camel.consumer.common.routebuilders.AbstractConsumerRouteBuilder;
import com.juliuskrah.camel.consumer.common.wrappers.MerchantPayment;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Julius Krah
 */
@Slf4j
@Component
public class MerchantPaymentProcessor implements Processor {
    private final MerchantIntegration integration;

    public MerchantPaymentProcessor(MerchantIntegration integration) {
        this.integration = integration;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        MerchantPayment merchantPayment = exchange.getIn().getBody(MerchantPayment.class);
        try {
            var acknowledgement = integration.invoke(merchantPayment);
            exchange.getIn().setBody(acknowledgement);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            try (var producerTemplate = exchange.getContext().createProducerTemplate()) {
                // Report to APM
                producerTemplate.sendBody(AbstractConsumerRouteBuilder.REPORT_TO_MONITORING, ex);
            }
        }
    }
}
