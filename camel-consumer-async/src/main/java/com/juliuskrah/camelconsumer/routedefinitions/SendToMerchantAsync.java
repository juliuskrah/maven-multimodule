package com.juliuskrah.camelconsumer.routedefinitions;

import com.juliuskrah.camel.consumer.common.CamelConsumerProperties;
import com.juliuskrah.camel.consumer.common.processors.MerchantPaymentProcessor;
import com.juliuskrah.camel.consumer.common.routebuilders.AbstractConsumerRouteBuilder;
import com.juliuskrah.camel.consumer.common.wrappers.MerchantPayment;

import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.opentracing.SetBaggageProcessor;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * @author Julius Krah
 */
@Component
@RequiredArgsConstructor
public class SendToMerchantAsync extends AbstractConsumerRouteBuilder {
        private final MerchantPaymentProcessor processor;
        private final CamelConsumerProperties properties;

        @Override
        protected void route() {
                var retry = properties.getRabbit().getRetry();
                var request = properties.getRabbit().getRequest();
                fromF("rabbitmq:%s?queue=%s&exchangeType=%s&concurrentConsumers=%s", //
                                retry.getExchangeName(), //
                                retry.getQueueName(), //
                                retry.getExhangeType(), //
                                retry.getConcurrentConsumers() //
                ) //
                                .routeId("retry-worker") //
                                .to("direct:messageProcessing");

                fromF("rabbitmq:%s?queue=%s&exchangeType=%s&concurrentConsumers=%s", //
                                request.getExchangeName(), //
                                request.getQueueName(), //
                                request.getExhangeType(), //
                                request.getConcurrentConsumers() //
                ) //
                                .routeId("request-worker") //
                                .to("direct:messageProcessing");

                var acknowledgement = properties.getRabbit().getAcknowledgement();
                from("direct:messageProcessing") //
                                .routeId("message-processing") //
                                .unmarshal().json(JsonLibrary.Jackson, MerchantPayment.class) //
                                .to("bean-validator:consumervalidator") //
                                .process(new SetBaggageProcessor("merchantPaymentID",
                                                simple("${body?.merchantPaymentID}")))
                                .process(processor) //
                                .toF("rabbitmq:%s?queue=%s", //
                                                acknowledgement.getExchangeName(), //
                                                acknowledgement.getQueueName() //
                                ) //
                                .end();
        }
}
