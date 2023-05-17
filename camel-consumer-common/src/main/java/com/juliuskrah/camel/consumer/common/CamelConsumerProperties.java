package com.juliuskrah.camel.consumer.common;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

/**
 * @author Julius Krah
 */
@Data
@ConfigurationProperties(prefix = "consumer")
public class CamelConsumerProperties {
    @NestedConfigurationProperty
    private final @Valid Tracer tracer = new Tracer();
    private final @Valid Rabbit rabbit = new Rabbit();

    @Data
    public static class Rabbit {
        @Getter(value = AccessLevel.NONE)
        private static final String EXCHANGE_NAME = "IN.GH.JPG.CHARGE.HANDLER.EXCHANGE";
        @Getter(value = AccessLevel.NONE)
        private static final String EXCHANGE_TYPE = "topic";
        private final @Valid Acknowledgement acknowledgement = new Acknowledgement();
        private final @Valid Request request = new Request();
        private final @Valid Retry retry = new Retry();

        @Data
        public static class Acknowledgement {
            private String exchangeName = EXCHANGE_NAME;
            private String queueName = "IN.GH.JPG.CHARGE.HANDLER.ACK";
            private String exhangeType = EXCHANGE_TYPE;
        }

        @Data
        public static class Retry {
            private String exchangeName = EXCHANGE_NAME;
            private String queueName = "IN.GH.JPG.CHARGE.HANDLER.RETRY";
            private String exhangeType = EXCHANGE_TYPE;
            private Integer concurrentConsumers = 1;
            private Integer maxRetryCount = 3;
            private Integer retryTTL = 15000;
        }

        @Data
        public static class Request {
            private String exchangeName = EXCHANGE_NAME;
            private String queueName = "IN.GH.JPG.CHARGE.HANDLER.REQ";
            private String exhangeType = EXCHANGE_TYPE;
            private Integer concurrentConsumers = 2;
        }

    }

    @Data
    public static class Tracer {
        @NotBlank
        private String serviceName = "camel-consumer";
        private String agentHost = "localhost";
        private String scheme = "http";
        private String path = "/api/v2/spans";
        private Integer agentPort = 9411;
        private Integer agentMaxPacketSize = 0;
        private Integer maxQueueSize;
        private boolean enabled;
        /**
         * probability a request will result in a new trace. 0 means never sample, 1
         * means always sample. Minimum probability is 0.01, or 1% of traces
         */
        private float probability = 0.2f;
    }
}
