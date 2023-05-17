package com.juliuskrah.camel.consumer.common.wrappers;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Acknowledgement extends Response implements Serializable {
    private static final long serialVersionUID = 645263321393881024L;
    /**
     * Type of service
     */
    private int isTokenService;

    /**
     * Receipt number returned by merchant
     */
    private String receiptNumber = "";

    /**
     * Transaction status description
     */
    @JsonAlias("message")
    private String statusDescription = "";

    /**
     * Amount transacted
     */
    private Double amountExpected = 0.0;

    /**
     * Payment uniqueID
     */
    private long beepTransactionID;

    /**
     * UniqueID from client
     */
    private String payerTransactionID = "";

    /**
     * Payment Last Send
     */
    private String paymentLastSend;

    /**
     * Payment First Send
     */
    private String paymentFirstSend;

    /**
     * Date Payment Pushed
     */
    private String datePaymentPushed;

    /**
     * Date merchant response received
     */
    private String merchantResponseDate;

    /**
     * Payer client acknowledgement ID
     */
    private String payerClientACKID;
}
