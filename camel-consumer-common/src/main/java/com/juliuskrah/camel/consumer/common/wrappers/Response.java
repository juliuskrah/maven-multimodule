package com.juliuskrah.camel.consumer.common.wrappers;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    @JsonAlias("status")
    private int statusCode;
    private String message;

}
