package com.juliuskrah.camelconsumer.routedefinitions;

import com.juliuskrah.camel.consumer.common.processors.MerchantPaymentProcessor;
import com.juliuskrah.camel.consumer.common.routebuilders.AbstractConsumerRouteBuilder;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * @author Julius Krah
 */
@Component
@RequiredArgsConstructor
public class SendToMerchantSync extends AbstractConsumerRouteBuilder {
    private final MerchantPaymentProcessor processor;
    @Override
    protected void route() {
        from("direct:springcontroller").routeId("spring-controller")
                // Add tracer bag attributes
                .to("bean-validator:consumervalidator") //
                .process(processor).end();
    }

}
