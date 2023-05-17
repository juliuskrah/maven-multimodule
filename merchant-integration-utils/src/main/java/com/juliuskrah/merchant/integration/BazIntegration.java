package com.juliuskrah.merchant.integration;

import java.net.ConnectException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.juliuskrah.camel.consumer.common.api.MerchantIntegration;
import com.juliuskrah.camel.consumer.common.wrappers.Acknowledgement;
import com.juliuskrah.camel.consumer.common.wrappers.MerchantPayment;
import com.juliuskrah.merchant.integration.utils.MerchantResponse;

import org.apache.camel.ProducerTemplate;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Julius Krah
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BazIntegration implements MerchantIntegration {
    private final ProducerTemplate producerTemplate;

    public MerchantResponse callAPI(Map<String, ?> payload) {
        MerchantResponse apiResponse = new MerchantResponse();

        apiResponse.setSuccess(false);
        // for sending request
        var response = producerTemplate.asyncRequestBody("direct:sendrequest", payload, MerchantResponse.class);
        return response.thenApply(block -> {
            log.info("recieved {}", block);
            block.setSuccess(true);
            return block;
        })
        .exceptionally(cause -> {
            apiResponse.setSuccess(false);
            apiResponse.setCause(cause);
            return apiResponse;
        })
        .join();
    }

    @Override
    public Acknowledgement invoke(MerchantPayment merchantPayment) {
        // Formulation of the payload...
        Map<String, Object> merchantpayload = new LinkedHashMap<>();

        merchantpayload.put("amt", merchantPayment.getMerchantPaymentAmount());
        merchantpayload.put("phonenumber", merchantPayment.getMerchantPaymentMSISDN());
        merchantpayload.put("service", merchantPayment.getServiceCode());

        MDC.put("phonenumber", merchantPayment.getMerchantPaymentMSISDN());
        MerchantResponse apiResponse = callAPI(merchantpayload);

        Acknowledgement acknowledgement = new Acknowledgement();

        if (!apiResponse.isSuccess()) {
            // Failure acknowledgement and call for escalation...
            if (apiResponse.getCause() != null && apiResponse.getCause().getCause() instanceof ConnectException) {
                acknowledgement.setStatusCode(141);
                acknowledgement.setStatusDescription("Connection time out exception");
            }

        } else {
            if ("Success".equalsIgnoreCase(apiResponse.getStatus())) {
                acknowledgement.setStatusCode(200);
                acknowledgement.setStatusDescription(apiResponse.getMessage());
            } else if ("Error".equalsIgnoreCase(apiResponse.getStatus())) {
                acknowledgement.setStatusCode(400);
                acknowledgement.setStatusDescription("Error no transaction");
            }
        }
        return acknowledgement;
    }

    

}
