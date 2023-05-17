package com.juliuskrah.camel.consumer.common.wrappers;

import java.util.Map;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.apache.camel.util.json.JsonArray;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MerchantPayment {
    /**
     * Flag to check if the merchantPayment is of a token service
     */
    private int isTokenService;

    /**
     * The invoiceNumber of the merchantPayment
     */
    private String invoiceNumber;

    /**
     * The ID of the merchantPayment
     */
    @NotNull
    private Long merchantPaymentID;

    /**
     * The paybill number
     */
    private String paybill;

    /**
     * The UUID of the merchantPayment
     */
    private long merchantPaymentUUID;

    /**
     * The amount of the merchantPayment
     */
    @Min(value = 1, message = "Invalid amount")
    private Double merchantPaymentAmount;

    /**
     * The MSISDN used to make the merchantPayment
     */

    @Size(min = 10, max = 10, message = "MSISDN invalid length")
    private String merchantPaymentMSISDN;

    /**
     * The account number the merchantPayment was made for
     */
    private String merchantPaymentAccountNumber;

    /**
     * The URL of the service this merchantPayment was made for
     */
    @NotBlank
    private String serviceUrl;

    /**
     * The protocol this service uses to hit the merchant endpoint
     */
    @NotBlank
    private String protocol;

    /**
     * The ID that identifies this service the merchantPayment is made for
     */
    private int serviceID;

    /**
     * The name of the customer
     */
    private String merchantPaymentCustomerName;

    /**
     * The date the merchantPayment was made/received/logged
     */
    private String merchantPaymentDate;

    /**
     * The currency code of the merchantPayment
     */
    private String merchantPaymentCurrencyCode;

    /**
     * Flag to check if the payment should be auto acknowledged
     */
    private int autoAcknowledgePayment;

    /**
     * The API function to be invoked
     */
    private String apiFunctionName = "";

    /**
     * The API port to be used
     */
    private int apiPortNumber = 0;

    /**
     * Flag to check if client uses SSL Certificates for security
     */
    private int sslEnabled = 0;

    /**
     * The path to the merchant's WSDL file
     */
    private String wsdlFile;

    /**
     * The username of the merchant API
     */
    private String apiUserName = "";

    /**
     * The password of the merchant API
     */
    private String apiPassword = "";

    /**
     * The passkey if applicable
     */
    private String apiPasskey;

    /**
     * The path to the SSL certificate
     */
    private String sslCertificatePath;

    /**
     * The service code of this merchantPayment
     */
    private String serviceCode;

    /**
     * The merchantPayment narration
     */
    private String narration;

    /**
     * The mode of payment used
     */
    private transient String[] parsedPaymentMode;

    /**
     * The raw mode of payment used
     */
    private JsonArray paymentMode;

    /**
     * The parsed payer client code
     */
    private transient String[] parsedPayerClientCode;

    /**
     * The raw payer client code
     */
    private JsonArray payerClientCode;

    /**
     * The date the merchantPayment was made
     */
    private String hubCreationDate;

    /**
     * The number of times this merchantPayment was pushed to the merchant
     */
    private int numberOfSends;

    /**
     * The maximum number of times this merchantPayment is to be pushed to the
     * merchant
     */
    private int maxNumberOfSends;

    /**
     * The last time the merchantPayment was attempted to be sent to the merchant
     */
    private String lastSend = "";

    /**
     * The current merchantPaymentStatus of the merchantPayment
     */
    private int merchantPaymentStatus;

    /**
     * The parsed unique ID of the trx on the payer's end
     */
    private transient String[] parsedPayerTransactionID;

    /**
     * The raw unique ID of the trx on the payer's end
     */
    private JsonArray payerTransactionID;
    /**
     * Request reference ID
     */
    private String requestReferenceID;

    /**
     * The parsed IDs for the request origins of the merchantPayment
     */
    private transient Integer[] parsedRequestOriginIDs;

    /**
     * The raw IDs for the request origins of the merchantPayment
     */
    private JsonArray requestOriginIDs;

    /**
     * The payment raw extraData
     */
    private Map<@NotBlank String, @NotNull Object> extraData;

    /**
     * The next time we will attempt to send the merchantPayment
     */
    private String nextSend;

    /**
     * The first time we attempted to send the merchantPayment
     */
    private String firstSend;

    /**
     * The class to be invoked in the wrapper
     */
    private String processorClass = "";

    /**
     * JsonElement instance for parsing the message from the queue
     */
    private transient Map<String, Object> jsonElement;

}
