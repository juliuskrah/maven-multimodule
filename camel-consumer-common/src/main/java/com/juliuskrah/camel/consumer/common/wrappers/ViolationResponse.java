package com.juliuskrah.camel.consumer.common.wrappers;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Value;

/**
 * @author Julius Krah
 */
@Data
public class ViolationResponse {
    private List<Violation> violations = new ArrayList<>();

    @Value
    public static class Violation {
        private final String fieldName;
        private final String message;
    }
}
