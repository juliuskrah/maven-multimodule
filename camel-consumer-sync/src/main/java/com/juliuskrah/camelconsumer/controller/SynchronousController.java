package com.juliuskrah.camelconsumer.controller;

import com.juliuskrah.camel.consumer.common.wrappers.MerchantPayment;
import java.util.concurrent.CompletableFuture;

import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Julius Krah
 */
@Slf4j
@RestController
@RequestMapping("/camel-consumer/")
@RequiredArgsConstructor
public class SynchronousController {
    private final ProducerTemplate template;

    @PostMapping("/merchant")
    public CompletableFuture<Object> merchant(@RequestBody MerchantPayment payment) {
        log.debug("received payload: {}", payment);
        return template.asyncSendBody("direct:springcontroller", payment);
    }
}
