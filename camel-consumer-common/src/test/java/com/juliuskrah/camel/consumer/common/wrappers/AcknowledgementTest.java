package com.juliuskrah.camel.consumer.common.wrappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AcknowledgementTest {

    public Acknowledgement acknowledgement;

    private String statusDescription;
    private String payerTransactionID;
    private String receiptNumber;
    private int isTokenService;
    private int statusCode;
    private double amount;
    private long beepTransactionID;

    @BeforeEach
    public void setUp() {
        statusDescription = "Successfully Processed";
        payerTransactionID = "PAYER-TRX";
        receiptNumber = "RCPT";
        isTokenService = 1;
        statusCode = 140;
        amount = 20.00;
        beepTransactionID = 1;

        acknowledgement = new Acknowledgement();

        acknowledgement.setStatusDescription(statusDescription);
        acknowledgement.setPayerTransactionID(payerTransactionID);
        acknowledgement.setReceiptNumber(receiptNumber);
        acknowledgement.setIsTokenService(isTokenService);
        acknowledgement.setAmountExpected(amount);
        acknowledgement.setBeepTransactionID(beepTransactionID);
    }

    @AfterEach
    public void tearDown() {
        acknowledgement = null;
    }

    @Test
    public void testStatusDescription() {
        // Differs cross-board
        String statusDescription = "Successfully processed";
        if (acknowledgement == null) {
            System.out.println("Acknnowledgement is blank");
        } else {
            acknowledgement.setStatusDescription(statusDescription);
            assertEquals(acknowledgement.getStatusDescription(), statusDescription);
        }
    }

    @Test
    public void testPayerTransactionID() {
        String payerTransactionID = "";
        acknowledgement.setPayerTransactionID(payerTransactionID);
        assertEquals(acknowledgement.getPayerTransactionID(), payerTransactionID);
    }

    @Test
    public void testReceiptNumber() {
        String receiptNumber = "";
        acknowledgement.setReceiptNumber(receiptNumber);
        assertEquals(acknowledgement.getReceiptNumber(), receiptNumber);
    }

    @Test
    public void testIsTokenService() {

        acknowledgement.setIsTokenService(isTokenService);
        assertEquals(acknowledgement.getIsTokenService(), isTokenService);
    }

    @Test
    public void testStatusCode() {

        acknowledgement.setStatusCode(statusCode);
        assertEquals(acknowledgement.getStatusCode(), statusCode);
    }

    @Test
    public void testAmountExpected() {

        acknowledgement.setAmountExpected(amount);
        assertEquals(acknowledgement.getAmountExpected(), amount, 0.0);
    }

    @Test
    public void testBeepTransactionID() {
        acknowledgement.setBeepTransactionID(beepTransactionID);
        assertEquals(acknowledgement.getBeepTransactionID(), beepTransactionID);
    }

    @Test
    public void testToString() {
        assertTrue(acknowledgement.toString().contains("receiptNumber="));
    }
}
