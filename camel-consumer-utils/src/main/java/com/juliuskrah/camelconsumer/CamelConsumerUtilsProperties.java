package com.juliuskrah.camelconsumer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import lombok.Data;

/**
 * @author Julius Krah
 */
@Data
@ConfigurationProperties(prefix = "consumer")
public class CamelConsumerUtilsProperties {
    @NestedConfigurationProperty
    private final Rest rest = new Rest();

    @Data
    public static class Rest {
        /**
         * Host and port of external API
         */
        private String hostPort = "localhost:8080";
        /**
         * The path on the external API
         */
        private String uriTemplate = "/";
    }
}
