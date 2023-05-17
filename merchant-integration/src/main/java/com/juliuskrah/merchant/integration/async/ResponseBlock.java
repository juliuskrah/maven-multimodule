package com.juliuskrah.merchant.integration.async;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseBlock {

    private boolean success;
    private String response;
    private String status;
    private String message;
    private Throwable cause;

}
