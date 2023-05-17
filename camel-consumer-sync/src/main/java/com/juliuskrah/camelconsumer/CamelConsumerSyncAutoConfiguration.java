package com.juliuskrah.camelconsumer;

import com.juliuskrah.camel.consumer.common.CamelConsumerProperties;
import com.juliuskrah.camel.consumer.common.processors.MerchantPaymentProcessor;
import com.juliuskrah.camelconsumer.controller.SynchronousController;
import com.juliuskrah.camelconsumer.routedefinitions.SendToMerchantSync;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Julius Krah
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CamelConsumerProperties.class)
@AutoConfigureAfter(value = {WebMvcAutoConfiguration.class})
@AutoConfigureBefore(name = {"org.apache.camel.spring.boot.CamelAutoConfiguration"})
@Import({SendToMerchantSync.class, SynchronousController.class, MerchantPaymentProcessor.class })
public class CamelConsumerSyncAutoConfiguration {

}
