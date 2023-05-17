package com.juliuskrah.merchant.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        var application = new SpringApplication(Application.class);
        BufferingApplicationStartup applicationStartup = new BufferingApplicationStartup(2048);
        application.setApplicationStartup(applicationStartup);
        application.run(args);
    }
}