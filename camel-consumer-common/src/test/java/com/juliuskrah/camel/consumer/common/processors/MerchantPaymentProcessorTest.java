package com.juliuskrah.camel.consumer.common.processors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.juliuskrah.camel.consumer.common.api.MerchantIntegration;
import com.juliuskrah.camel.consumer.common.wrappers.MerchantPayment;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.support.DefaultMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

/**
 * @author Julius Krah
 */
public class MerchantPaymentProcessorTest {

    @Test
    @DisplayName("when payload is received, process")
    void whenPayloadReceivedProcess() throws Exception {
        var payment = new MerchantPayment();
        payment.setApiFunctionName("BEEP.chargeAPI");

        var exchange = mock(Exchange.class);
        // Create message object for testing
        Message message = new DefaultMessage((Exchange) null);
        message.setBody(payment, MerchantPayment.class);

        // Let us now set expectations
        when(exchange.getIn()).thenReturn(message);
        var integration = mock(MerchantIntegration.class);
        var processor = new MerchantPaymentProcessor(integration);

        // Invoke the processor
        processor.process(exchange);
        var merchantArgumentCaptor = ArgumentCaptor.forClass(MerchantPayment.class);
        verify(integration, times(1)).invoke(merchantArgumentCaptor.capture());
        var captured = merchantArgumentCaptor.getAllValues();
        assertThat(captured).hasSize(1);
        // Capture the argument passed to
        // IntegrationInterface#processRequest(MerchantPayment)
        assertThat(captured.get(0).getApiFunctionName()).isEqualTo("BEEP.chargeAPI");
        verify(exchange, times(2)).getIn();
    }
}
