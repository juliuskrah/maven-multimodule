package com.juliuskrah.merchant.integration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.juliuskrah.camel.consumer.common.api.MerchantIntegration;
import com.juliuskrah.camel.consumer.common.wrappers.Acknowledgement;
import com.juliuskrah.camel.consumer.common.wrappers.MerchantPayment;
import com.juliuskrah.merchant.integration.sync.MerchantCharge;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Julius Krah
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BarIntegration implements MerchantIntegration {
    private final RestTemplate httpClient;
    @Value("${merchant.uri}")
    private String uri = "http://localhost:5050";

    @SneakyThrows
    public CompletableFuture<MerchantCharge> post(String uri, Map<String, ?> payload) {
        return CompletableFuture.supplyAsync(() -> httpClient.postForObject(uri, payload, MerchantCharge.class));
    }

    public MerchantCharge callAPI(Map<String, ?> payload) {
        MerchantCharge responseBlock = new MerchantCharge();

        responseBlock.setResponseCode("404.001.03");
        // for sending request
        return post(uri, payload).thenApply(block -> {
            log.info("recieved {}", block);
            responseBlock.setResponseCode("0");
            return block;
        }).exceptionally(cause -> {
            responseBlock.setResponseCode("500.002.1001");
            responseBlock.setResponseDescription("Service under maintenance");
            return responseBlock;
        }).join();
    }

    @Override
    @SneakyThrows
    public Acknowledgement invoke(MerchantPayment merchantPayment) {
        // Formulation of the payload...
        Map<String, Object> merchantpayload = new LinkedHashMap<>();
        Base64.Encoder base64 = Base64.getEncoder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddhhmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String password = String.format("%s:%s:%s", merchantPayment.getPaybill(), //
                merchantPayment.getApiPasskey(), //
                timestamp);

        merchantpayload.put("BusinessShortCode", merchantPayment.getPaybill());
        merchantpayload.put("Password", base64.encodeToString(password.getBytes()));
        merchantpayload.put("Timestamp", timestamp);
        merchantpayload.put("TransactionType", "CustomerPayBillOnline");
        merchantpayload.put("Amount", merchantPayment.getMerchantPaymentAmount());
        merchantpayload.put("PartyA", merchantPayment.getMerchantPaymentMSISDN());
        merchantpayload.put("PartyB", merchantPayment.getPaybill());
        merchantpayload.put("PhoneNumber", merchantPayment.getMerchantPaymentMSISDN());
        merchantpayload.put("CallBackURL", "https://exampe.com/callback");
        merchantpayload.put("AccountReference", merchantPayment.getRequestReferenceID());
        merchantpayload.put("TransactionDesc", "You have agreed to pay GHS100 for airtime");

        MDC.put("phonenumber", merchantPayment.getMerchantPaymentMSISDN());

        MerchantCharge apiResponse = callAPI(merchantpayload);

        Acknowledgement acknowledgement = new Acknowledgement();

        acknowledgement.setStatusDescription(apiResponse.getResponseDescription());
        acknowledgement.setPayerClientACKID(apiResponse.getCheckoutRequestID());
        if ("0".equalsIgnoreCase(apiResponse.getResponseCode())) {
            // Successful acknowledgement
            acknowledgement.setStatusCode(340);
        } else {
            acknowledgement.setStatusCode(346);
        }
        return acknowledgement;
    }

}
