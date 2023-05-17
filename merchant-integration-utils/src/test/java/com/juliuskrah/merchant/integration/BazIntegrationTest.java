package com.juliuskrah.merchant.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.net.ConnectException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.juliuskrah.camel.consumer.common.wrappers.MerchantPayment;
import com.juliuskrah.merchant.integration.utils.MerchantResponse;

import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BazIntegrationTest {
    @InjectMocks
    private BazIntegration integration;
    @Mock
    private ProducerTemplate producerTemplate;

    @Test
    void testCallApi() {
        var response = new MerchantResponse();
        response.setStatus("Executed successfully");
        when(producerTemplate.asyncRequestBody(anyString(), anyMap(), //
                ArgumentMatchers.<Class<MerchantResponse>>any())) //
                        .thenReturn(CompletableFuture.supplyAsync(() -> response));
        var responseBlock = integration.callAPI(Map.of());
        assertThat(responseBlock).hasFieldOrPropertyWithValue("status", "Executed successfully");
        assertThat(responseBlock).hasFieldOrPropertyWithValue("success", true);
    }

    @Test
    void testInvoke() {
        var futureExplosion = new CompletableFuture<MerchantResponse>();
        futureExplosion.completeExceptionally(new ConnectException("boom !"));
        when(producerTemplate.asyncRequestBody(anyString(), anyMap(), //
                ArgumentMatchers.<Class<MerchantResponse>>any())) //
                        .thenReturn(futureExplosion);
        var ack = integration.invoke(new MerchantPayment());
        assertThat(ack).isNotNull();
        assertThat(ack).hasFieldOrPropertyWithValue("statusCode", 141);
        assertThat(ack).hasFieldOrPropertyWithValue("statusDescription", "Connection time out exception");
    }
}
