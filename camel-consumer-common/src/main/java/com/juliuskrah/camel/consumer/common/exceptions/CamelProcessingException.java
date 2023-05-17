package com.juliuskrah.camel.consumer.common.exceptions;

public class CamelProcessingException extends Exception {
    private static final long serialVersionUID = 4199959119477774409L;

    public CamelProcessingException(String message) {
        super(message);
    }

    public CamelProcessingException(Throwable cause) {
        super(cause);
    }

    public CamelProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
