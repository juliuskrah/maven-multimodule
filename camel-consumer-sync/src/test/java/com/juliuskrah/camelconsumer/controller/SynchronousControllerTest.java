package com.juliuskrah.camelconsumer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.CompletableFuture;

import com.juliuskrah.camel.consumer.common.wrappers.Acknowledgement;
import com.juliuskrah.camel.consumer.common.wrappers.MerchantPayment;

import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import lombok.SneakyThrows;

@WebMvcTest(controllers = SynchronousController.class)
public class SynchronousControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProducerTemplate template;

    @Test
    @SneakyThrows
    void testSynchronousRoute() {
        var ack = new Acknowledgement();
        ack.setStatusCode(189);
        ack.setAmountExpected(100.00);
        when(template.asyncSendBody(anyString(), any(MerchantPayment.class))) //
                .thenReturn(CompletableFuture.supplyAsync(() -> ack));
        var content = "{\"isTokenService\":1, \"invoiceNumber \":\"1298521\", \"merchantPaymentID\":123, \"serviceUrl\": \"https://192.168.251.100\",\"merchantPaymentMSISDN\":\"0723325335\"}"
                .getBytes();
        var mvcResult = this.mvc.perform(post("/camel-consumer/merchant").content(content) //
                .contentType(MediaType.APPLICATION_JSON)) //
                .andExpect(request().asyncStarted()) //
                .andDo(log()).andReturn();
        this.mvc.perform(asyncDispatch(mvcResult)) //
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$.amountExpected").value(100.00));
    }
}
