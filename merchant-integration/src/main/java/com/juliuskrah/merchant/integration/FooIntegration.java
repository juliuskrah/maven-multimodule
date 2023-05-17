package com.juliuskrah.merchant.integration;

import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import com.juliuskrah.camel.consumer.common.api.MerchantIntegration;
import com.juliuskrah.camel.consumer.common.wrappers.MerchantPayment;
import com.juliuskrah.camel.consumer.common.wrappers.Response;
import com.juliuskrah.merchant.integration.async.ResponseBlock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Julius Krah
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FooIntegration implements MerchantIntegration {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    @Value("${merchant.uri}")
    private String uri = "http://localhost:8050";

    @SneakyThrows
    public CompletableFuture<ResponseBlock> post(String uri, Map<String, ?> payload) {
        String requestBody = objectMapper //
                .writerWithDefaultPrettyPrinter() //
                .writeValueAsString(payload);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).POST(BodyPublishers.ofString(requestBody))
                .build();

        return httpClient.sendAsync(request, BodyHandlers.ofString()) //
                .thenApply(HttpResponse::body) //
                .thenApply(this::readValue);
    }

    private ResponseBlock readValue(String body) {
        try {
            return objectMapper.readValue(body, ResponseBlock.class);
        } catch (JsonProcessingException e) {
            throw new CompletionException(e);
        }
    }

    public ResponseBlock callAPI(Map<String, ?> payload) {
        ResponseBlock responseBlock = new ResponseBlock();

        responseBlock.setSuccess(false);
        // for sending request
        return post(uri, payload).thenApply(block -> {
            log.info("recieved {}", block);
            block.setSuccess(true);
            return block;
        }).exceptionally(cause -> {
            log.error("Encountered an error", cause.getCause());
            responseBlock.setSuccess(false);
            responseBlock.setCause(cause);
            return responseBlock;
        }).join();
    }

    @Override
    @SneakyThrows
    public Response invoke(MerchantPayment merchantPayment) {
        // Formulation of the payload...
        Map<String, Object> merchantpayload = new LinkedHashMap<>();

        merchantpayload.put("amt", merchantPayment.getMerchantPaymentAmount());
        merchantpayload.put("phonenumber", merchantPayment.getMerchantPaymentMSISDN());
        merchantpayload.put("service", merchantPayment.getServiceCode());

        MDC.put("phonenumber", merchantPayment.getMerchantPaymentMSISDN());

        ResponseBlock responseBlock = callAPI(merchantpayload);

        Response responseTemplate = new Response();

        if (!responseBlock.isSuccess()) {
            // Failure acknowledgement and call for escalation...
            if (responseBlock.getCause() instanceof ConnectException) {
                responseTemplate.setStatusCode(141);
                responseTemplate.setMessage("Connection time out exception");
            }

        } else {
            if ("Success".equalsIgnoreCase(responseBlock.getStatus())) {
                responseTemplate.setStatusCode(200);
                responseTemplate.setMessage(responseBlock.getMessage());
            } else if ("Error".equalsIgnoreCase(responseBlock.getStatus())) {
                responseTemplate.setStatusCode(400);
                responseTemplate.setMessage("Error no transaction");
            }
        }
        return responseTemplate;
    }
}
