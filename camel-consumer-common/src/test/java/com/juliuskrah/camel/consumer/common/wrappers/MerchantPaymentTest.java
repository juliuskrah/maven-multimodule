package com.juliuskrah.camel.consumer.common.wrappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.camel.util.json.JsonArray;
import org.apache.camel.util.json.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MerchantPaymentTest {

    private MerchantPayment merchantPayment = new MerchantPayment();

    private JsonObject jsonObject, populatedJsonObject;

    @BeforeEach
    void setUp() throws Exception {

        jsonObject = new JsonObject();
        merchantPayment.setIsTokenService(1);
        merchantPayment.setInvoiceNumber("WE42342");
        merchantPayment.setMerchantPaymentAmount(2000.00);
        merchantPayment.setMerchantPaymentMSISDN("0723325335");
        merchantPayment.setServiceUrl("http://localhost:8000");
        merchantPayment.setProtocol("JSON");
        merchantPayment.setServiceID(123);
        merchantPayment.setMerchantPaymentID(147852369L);
        merchantPayment.setMerchantPaymentCustomerName("SILVESTER WANJIRU");
        merchantPayment.setMerchantPaymentDate("2018-12-23 12:10:10");
        merchantPayment.setMerchantPaymentCurrencyCode("KES");
        merchantPayment.setAutoAcknowledgePayment(0);
        merchantPayment.setApiFunctionName("WE42342");
        merchantPayment.setApiPortNumber(8080);
        merchantPayment.setSslEnabled(0);
        merchantPayment.setWsdlFile("");
        merchantPayment.setApiUserName("username");
        merchantPayment.setApiPassword("password");
        merchantPayment.setSslCertificatePath("");
        merchantPayment.setServiceCode("DSTVKE");
        merchantPayment.setNarration("Paid");
        merchantPayment.setHubCreationDate("2018-11-08 09:52:03");
        merchantPayment.setNumberOfSends(0);
        merchantPayment.setLastSend("");
        merchantPayment.setMerchantPaymentStatus(139);
        merchantPayment.setNextSend("");
        merchantPayment.setFirstSend("");
        merchantPayment.setMerchantPaymentAccountNumber("ACC");

        JsonArray payerTransactionID = new JsonArray();
        payerTransactionID.add("PAYER-TRX1");
        payerTransactionID.add("PAYER-TRX2");

        merchantPayment.setPayerTransactionID(payerTransactionID);

        JsonArray requestOriginIDs = new JsonArray();
        requestOriginIDs.add(1);
        requestOriginIDs.add(2);

        merchantPayment.setRequestOriginIDs(requestOriginIDs);

        JsonArray payerClientCode = new JsonArray();
        payerClientCode.add("SAFKE");
        payerClientCode.add("DTB");

        merchantPayment.setPayerClientCode(payerClientCode);

        // Empty JSON Object
        jsonObject = new JsonObject();

        jsonObject.put("isTokenService", 1);
        jsonObject.put("invoiceNumber", "WE42342");
        jsonObject.put("merchantPaymentID", 147852369L);
        jsonObject.put("merchantPaymentAmount", "");
        jsonObject.put("merchantPaymentMSISDN", "0723325335");
        jsonObject.put("merchantPaymentAccountNumber", "ACC");
        jsonObject.put("serviceUrl", "http://localhost:8000");
        jsonObject.put("protocol", "JSON");
        jsonObject.put("serviceID", "");
        jsonObject.put("merchantPaymentCustomerName", "SILVESTER WANJIRU");
        jsonObject.put("merchantPaymentDate", "2018-12-23 12:10:10");
        jsonObject.put("merchantPaymentCurrencyCode", "KES");
        jsonObject.put("autoAcknowledgePayment", 0);
        jsonObject.put("apiFunctionName", "WE42342");
        jsonObject.put("apiPortNumber", 8080);
        jsonObject.put("sslEnabled", 0);
        jsonObject.put("wsdlFile", "");
        jsonObject.put("apiUserName", "username");
        jsonObject.put("apiPassword", "password");
        jsonObject.put("sslCertificatePath", "");
        jsonObject.put("serviceCode", "DSTVKE");
        jsonObject.put("narration", "Paid");
        jsonObject.put("hubCreationDate", "2018-11-08 09:52:03");
        jsonObject.put("numberOfSends", "");
        jsonObject.put("maxNumberOfSends", 0);
        jsonObject.put("lastSend", "");
        jsonObject.put("merchantPaymentStatus", 139);
        jsonObject.put("nextSend", "");
        jsonObject.put("firstSend", "");
        jsonObject.put("paymentPusherProcessorClass", "");
        jsonObject.put("extraData", new JsonObject());
        jsonObject.put("payerTransactionID", new JsonArray());
        jsonObject.put("requestOriginIDs", new JsonArray());
        jsonObject.put("paymentMode", new JsonArray());
        jsonObject.put("payerClientCode", new JsonArray());

        // PopulatedJSON
        populatedJsonObject = new JsonObject();

        populatedJsonObject.put("isTokenService", 1);
        populatedJsonObject.put("invoiceNumber", "WE42342");
        populatedJsonObject.put("merchantPaymentID", 123);
        populatedJsonObject.put("merchantPaymentAmount", "456");
        populatedJsonObject.put("merchantPaymentMSISDN", "254700210076");
        populatedJsonObject.put("merchantPaymentAccountNumber", "ACC");
        populatedJsonObject.put("serviceUrl", "http://localhost:8000");
        populatedJsonObject.put("protocol", "JSON");
        populatedJsonObject.put("serviceID", 123);
        populatedJsonObject.put("merchantPaymentCustomerName", "George");
        populatedJsonObject.put("merchantPaymentDate", "2018-12-23 12:10:10");
        populatedJsonObject.put("merchantPaymentCurrencyCode", "KES");
        populatedJsonObject.put("autoAcknowledgePayment", 0);
        populatedJsonObject.put("apiFunctionName", "dstv.pay");
        populatedJsonObject.put("apiPortNumber", 8080);
        populatedJsonObject.put("sslEnabled", 0);
        populatedJsonObject.put("wsdlFile", "");
        populatedJsonObject.put("apiUserName", "username");
        populatedJsonObject.put("apiPassword", "password");
        populatedJsonObject.put("sslCertificatePath", "");
        populatedJsonObject.put("serviceCode", "DSTVKE");
        populatedJsonObject.put("narration", "Paid");
        populatedJsonObject.put("hubCreationDate", "2018-11-08 09:52:03");
        populatedJsonObject.put("numberOfSends", 0);
        populatedJsonObject.put("maxNumberOfSends", 0);
        populatedJsonObject.put("lastSend", "");
        populatedJsonObject.put("merchantPaymentStatus", 139);
        populatedJsonObject.put("nextSend", "");
        populatedJsonObject.put("firstSend", "");
        populatedJsonObject.put("paymentPusherProcessorClass", "AIRTEL.php");

        JsonObject popalatedextraData = new JsonObject();
        popalatedextraData.put("key", "value");

        JsonArray popaulatePayerTransactionID = new JsonArray();
        popaulatePayerTransactionID.add("PAYER-TRX1");
        popaulatePayerTransactionID.add("PAYER-TRX2");

        populatedJsonObject.put("payerTransactionID", payerTransactionID);

        JsonArray populatedRequestOriginIDs = new JsonArray();
        populatedRequestOriginIDs.add(1);
        populatedRequestOriginIDs.add(2);

        populatedJsonObject.put("requestOriginIDs", requestOriginIDs);

        JsonArray populatePaymentMode = new JsonArray();
        populatePaymentMode.add("MOMO");
        populatePaymentMode.add("BANK");

        populatedJsonObject.put("paymentMode", populatePaymentMode);

        JsonArray populatedPayerClientCode = new JsonArray();
        populatedPayerClientCode.add("SAFKE");
        populatedPayerClientCode.add("DTB");

        populatedJsonObject.put("payerClientCode", populatedPayerClientCode);

    }

    @Test
    void testGetApiFunctionName() {
        assertEquals(merchantPayment.getApiFunctionName(), jsonObject.get("apiFunctionName").toString());
    }

    @Test
    void testGetApiPassword() {
        assertEquals(merchantPayment.getApiPassword(), jsonObject.get("apiPassword").toString());
    }

    @Test
    void testGetApiPortNumber() {
        assertEquals(merchantPayment.getApiPortNumber(), jsonObject.get("apiPortNumber"));
    }

    @Test
    void testGetApiUserName() {
        assertEquals(merchantPayment.getApiUserName(), jsonObject.get("apiUserName"));
    }

    @Test
    void testGetAutoAcknowledgePayment() {
        assertEquals(merchantPayment.getAutoAcknowledgePayment(), jsonObject.get("autoAcknowledgePayment"));
    }

    @Test
    void testGetFirstSend() {
        assertEquals(merchantPayment.getFirstSend(), jsonObject.get("firstSend"));
    }

    @Test
    void testGetHubCreationDate() {
        assertEquals(merchantPayment.getHubCreationDate(), jsonObject.get("hubCreationDate"));
    }

    @Test
    void testGetInvoiceNumber() {
        assertEquals(merchantPayment.getInvoiceNumber(), jsonObject.get("invoiceNumber"));
    }

    @Test
    void testGetIsTokenService() {
        assertEquals(merchantPayment.getIsTokenService(), jsonObject.get("isTokenService"));
    }

    @Test
    void testGetLastSend() {
        assertEquals(merchantPayment.getLastSend(), jsonObject.get("lastSend"));
    }

    @Test
    void testGetMerchantPaymentAccountNumber() {
        assertEquals(merchantPayment.getMerchantPaymentAccountNumber(), jsonObject.get("merchantPaymentAccountNumber"));
    }

    @Test
    void testGetMerchantPaymentCurrencyCode() {
        assertEquals(merchantPayment.getMerchantPaymentCurrencyCode(), jsonObject.get("merchantPaymentCurrencyCode"));
    }

    @Test
    void testGetMerchantPaymentCustomerName() {
        assertEquals(merchantPayment.getMerchantPaymentCustomerName(), jsonObject.get("merchantPaymentCustomerName"));
    }

    @Test
    void testGetMerchantPaymentDate() {
        assertEquals(merchantPayment.getMerchantPaymentDate(), jsonObject.get("merchantPaymentDate"));
    }

    @Test
    void testGetMerchantPaymentID() {
        assertEquals(merchantPayment.getMerchantPaymentID(), jsonObject.get("merchantPaymentID"));
    }

    @Test
    void testGetMerchantPaymentMSISDN() {
        assertEquals(merchantPayment.getMerchantPaymentMSISDN(), jsonObject.get("merchantPaymentMSISDN"));
    }

    @Test
    void testGetMerchantPaymentStatus() {
        assertEquals(merchantPayment.getMerchantPaymentStatus(), jsonObject.get("merchantPaymentStatus"));
    }

    @Test
    void testGetNarration() {
        assertEquals(merchantPayment.getNarration(), jsonObject.get("narration"));
    }

    @Test
    void testGetNextSend() {
        assertEquals(merchantPayment.getNextSend(), jsonObject.get("nextSend"));
    }

    @Test
    void testGetNumberOfSends() {
        merchantPayment.setNumberOfSends(50);
        assertEquals(merchantPayment.getNumberOfSends(), 50);
    }

    @Test
    void testGetPayerClientCode() {
        assertTrue(merchantPayment.getPayerClientCode().size() > 0);
    }

    @Test
    void testGetPayerTransactionID() {
        assertTrue(merchantPayment.getPayerTransactionID().size() > 0);
    }

    @Test
    void testGetProtocol() {
        assertEquals(merchantPayment.getProtocol(), jsonObject.get("protocol"));
    }

    @Test
    void testGetServiceCode() {
        assertEquals(merchantPayment.getServiceCode(), jsonObject.get("serviceCode"));
    }

    @Test
    void testGetServiceUrl() {
        assertEquals(merchantPayment.getServiceUrl(), jsonObject.get("serviceUrl"));
    }

    @Test
    void testGetSslEnabled() {
        assertEquals(merchantPayment.getSslEnabled(), jsonObject.get("sslEnabled"));
    }

    @Test
    void testGetSslCertificatePath() {
        assertEquals(merchantPayment.getSslCertificatePath(), jsonObject.get("sslCertificatePath"));
    }

    @Test
    void testGetWsdlFile() {
        assertEquals(merchantPayment.getWsdlFile(), jsonObject.get("wsdlFile"));
    }

    @Test
    void testGetMaxNumberOfSends() {

        assertEquals(merchantPayment.getMaxNumberOfSends(), jsonObject.get("maxNumberOfSends"));
    }

}
