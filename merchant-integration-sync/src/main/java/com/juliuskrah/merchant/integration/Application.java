package com.juliuskrah.merchant.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        var application = new SpringApplication(Application.class);
        BufferingApplicationStartup applicationStartup = new BufferingApplicationStartup(2048);
        application.setApplicationStartup(applicationStartup);
        application.run(args);
    }

    @Bean
    RestTemplate httpClient(RestTemplateBuilder builder) {
        return builder.build();
    }
}