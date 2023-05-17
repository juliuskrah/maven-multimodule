package com.juliuskrah.merchant.integration.sync;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MerchantCharge {
    @JsonProperty("MerchantRequestID")
    String merchantRequestID;
    @JsonProperty("CheckoutRequestID")
    String checkoutRequestID;
    @JsonProperty("ResponseCode")
    String responseCode;
    @JsonProperty("ResponseDescription")
    String responseDescription;
    @JsonProperty("CustomerMessage")
    String customerMessage;
}
