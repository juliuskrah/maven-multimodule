package com.juliuskrah.merchant.integration;

import java.net.http.HttpClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.context.annotation.Bean;
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        var application = new SpringApplication(Application.class);
        BufferingApplicationStartup applicationStartup = new BufferingApplicationStartup(2048);
        application.setApplicationStartup(applicationStartup);
        application.run(args);
    }

    @Bean
    HttpClient restTemplate() {
        return HttpClient.newBuilder().build();
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper().findAndRegisterModules();
    }
}