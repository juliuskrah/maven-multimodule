package com.juliuskrah.merchant.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.juliuskrah.camel.consumer.common.wrappers.MerchantPayment;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.SneakyThrows;

@ExtendWith(MockitoExtension.class)
public class FooIntegrationTest {
    @InjectMocks
    private FooIntegration integration;
    @Mock
    private HttpClient httpClient;
    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private HttpResponse<String> response;

    @Test
    @SneakyThrows
    void testPost() {
        var futureExplosion = new CompletableFuture<HttpResponse<String>>();
        futureExplosion.completeExceptionally(new IllegalArgumentException("KABOOM !"));
        when(httpClient.sendAsync(any(HttpRequest.class), ArgumentMatchers.<BodyHandler<String>>any())) //
                .thenReturn(futureExplosion);
        var completionStage = integration.post("http://valentine.com/path/to/merchant", Map.of());
        Assertions.assertThat(completionStage).isCompletedExceptionally() //
                .isNotCancelled() //
                .failsWithin(10, TimeUnit.MILLISECONDS) //
                .withThrowableOfType(ExecutionException.class) //
                .withCauseInstanceOf(IllegalArgumentException.class) //
                .withMessageContaining("KABOOM !");
    }

    @Test
    @SneakyThrows
    void testCallApi() {
        when(response.body()).thenReturn("{\"success\": \"true\", \"response\": \"It was a success\"}");
        when(httpClient.sendAsync(any(HttpRequest.class), ArgumentMatchers.<BodyHandler<String>>any())) //
                .thenReturn(CompletableFuture.supplyAsync(() -> response));
        var response = integration.callAPI(Map.of());
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getResponse()).contains("It was a success");
    }

    @Test
    @SneakyThrows
    void testInvoke() {
        when(response.body()).thenReturn("{\"success\": \"true\", \"status\": \"Success\"}");
        when(httpClient.sendAsync(any(HttpRequest.class), ArgumentMatchers.<BodyHandler<String>>any())) //
                .thenReturn(CompletableFuture.supplyAsync(() -> response));
        var response = integration.invoke(new MerchantPayment());
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(200);
    }
}
