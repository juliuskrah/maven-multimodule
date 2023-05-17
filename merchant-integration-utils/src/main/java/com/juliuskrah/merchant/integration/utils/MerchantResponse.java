package com.juliuskrah.merchant.integration.utils;

import lombok.Data;

@Data
public class MerchantResponse {
    private boolean success;
    private String response;
    private String status;
    private String message;
    private Throwable cause;

}
