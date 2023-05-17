package com.juliuskrah.camelconsumer;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.Data;
import lombok.SneakyThrows;

/**
 * @author Julius Krah
 */
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@MockEndpoints
public class CamelConsumerUtilsTest {
    WireMockServer wireMockServer = null;
    @Autowired
    private CamelContext context;
    @Autowired
    private CamelConsumerUtilsProperties properties;
    @Produce("direct:sendrequest")
    private ProducerTemplate producer;
    @EndpointInject("mock:rest:post:{{consumer.rest.uri-template}}")
    MockEndpoint restMock;

    @BeforeAll
    void setUpMockServer() {
        wireMockServer = new WireMockServer(wireMockConfig());
        wireMockServer.start();
    }

    @Test
    @SneakyThrows
    void testSendPostRoute() {
        stubFor(post(urlPathEqualTo(properties.getRest().getUriTemplate())) //
                .willReturn(aResponse() //
                        .withStatus(200) //
                        .withHeader("Content-Type", "application/json") //
                        .withBody("{\"statusDescription\": \"The request processing was succesful\"}")));
        AdviceWith.adviceWith(context, "sendByRest", advice -> advice.mockEndpoints("rest:*"));

        var body = new POJO();
        body.setInput("input");
        body.setOutput("output");

        producer.sendBody(body);
        restMock.expectedBodiesReceived("{\"statusDescription\": \"The request processing was succesful\"}");
        MockEndpoint.assertIsSatisfied(context);
        WireMock.reset();
    }

    @AfterAll
    void tearDownMockServer() {
        wireMockServer.stop();
    }

    @Data
    static class POJO {
        private String input;
        private String output;
    }
}
