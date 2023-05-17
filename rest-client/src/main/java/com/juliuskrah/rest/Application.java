package com.juliuskrah.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/merchant")
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostMapping("/service")
    public MpesaCharge getService(@RequestBody String payload) {
        log.info("received payload {}", payload);
        return new MpesaCharge("00000000-0000-0000-0000-000000000000", //
                "1QW34RTY5FVG5783", 0, "The charge request was successful", //
                "You have been debitted GHS100.00");
    }

    @Value
    static class MpesaCharge {
        @JsonProperty("MerchantRequestID")
        String merchantRequestID;
        @JsonProperty("CheckoutRequestID")
        String checkoutRequestID;
        @JsonProperty("ResponseCode")
        Integer responseCode;
        @JsonProperty("ResponseDescription")
        String responseDescription;
        @JsonProperty("CustomerMessage")
        String customerMessage;
    }
}
