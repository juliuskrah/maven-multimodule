package com.juliuskrah.merchant.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Map;

import com.juliuskrah.camel.consumer.common.wrappers.MerchantPayment;
import com.juliuskrah.merchant.integration.sync.MerchantCharge;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import lombok.SneakyThrows;

@ExtendWith(MockitoExtension.class)
public class BarIntegrationTest {
    @InjectMocks
    private BarIntegration integration;
    @Mock
    private RestTemplate httpClient;

    @Test
    @SneakyThrows
    void testPost() {
        var mpesa = new MerchantCharge();
        mpesa.setResponseCode("0");
        mpesa.setResponseDescription("It was a success");
        when(httpClient.postForObject(anyString(), anyMap(), ArgumentMatchers.<Class<MerchantCharge>>any())) //
                .thenReturn(mpesa);
        var completionStage = integration.post("http://dummy.com/path/to/merchant", Map.of());
        Assertions.assertThat(completionStage).isCompleted() //
                .isCompletedWithValueMatching(response -> "It was a success".equals(response.getResponseDescription())) //
                .isCompletedWithValueMatching(response -> "0".equals(response.getResponseCode()));
    }

    @Test
    @SneakyThrows
    void testCallApi() {
        when(httpClient.postForObject(anyString(), anyMap(), ArgumentMatchers.<Class<MerchantCharge>>any())) //
                .thenThrow(new IllegalArgumentException("boom !"));
        var response = integration.callAPI(Map.of());
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response).hasFieldOrPropertyWithValue("responseDescription", "Service under maintenance");
        Assertions.assertThat(response.getResponseCode()).isEqualTo("500.002.1001");
    }

    @Test
    @SneakyThrows
    void testInvoke() {
        var mpesa = new MerchantCharge();
        mpesa.setResponseCode("404.001.03");
        mpesa.setResponseDescription("Authentication failed");
        when(httpClient.postForObject(anyString(), anyMap(), ArgumentMatchers.<Class<MerchantCharge>>any())) //
                .thenReturn(mpesa);
        var response = integration.invoke(new MerchantPayment());
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(346);
        assertThat(response).hasFieldOrPropertyWithValue("statusDescription", "Authentication failed");
    }
}
