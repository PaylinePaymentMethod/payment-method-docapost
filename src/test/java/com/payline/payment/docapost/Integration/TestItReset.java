package com.payline.payment.docapost.Integration;

import com.payline.payment.docapost.service.impl.PaymentServiceImpl;
import com.payline.payment.docapost.service.impl.ResetServiceImpl;
import com.payline.pmapi.bean.common.Amount;
import com.payline.pmapi.bean.common.Buyer;
import com.payline.pmapi.bean.configuration.PartnerConfiguration;
import com.payline.pmapi.bean.payment.*;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFailure;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFormUpdated;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseSuccess;
import com.payline.pmapi.bean.reset.request.ResetRequest;
import com.payline.pmapi.bean.reset.response.ResetResponse;
import com.payline.pmapi.bean.reset.response.impl.ResetResponseFailure;
import com.payline.pmapi.bean.reset.response.impl.ResetResponseSuccess;
import com.payline.pmapi.integration.AbstractPaymentIntegration;
import org.junit.jupiter.api.Assertions;

import java.math.BigInteger;
import java.util.*;

import static com.payline.payment.docapost.utils.DocapostConstants.*;

public class TestItReset extends AbstractPaymentIntegration {

    private static final String GOOD_CREDITOR_ID = "MARCHAND1";
    private static final String GOOD_LOGIN = "payline@docapost.fr";
    private static final String GOOD_PWD = "J:[ef8dccma";

    private static final String NOTIFICATION_URL = "http://google.com/";
    private static final String SUCCESS_URL = "https://successurl.com/";
    private static final String CANCEL_URL = "http://cancelurl.com/";

    private static final BigInteger AMOUNT_VALUE = BigInteger.TEN;
    private static final String AMOUNT_CURRENCY = "EUR";

    private static final String ORDER_REF = "orderRef";

    private static final String SOFT_DESCRIPTOR = "softDescriptor";

    private static final String IBAN = "FR7630076020821234567890186";

    private static final String SDD_ORDER_CANCEL_STATUS__CANCELLED = "CANCELLED";

    private static String TRANSACTION_ID = "transactionId";

    private static String PHONE_NUMBER_TEST = "0601020304";
    private static String OTP_TEST = "123456";

    private static Scanner KEYBOARD_USER;

    protected static String RESET_ADDITIONAL_DATA;
    protected static String RESET_PARTNER_TRANSACTION_ID;


    private PaymentServiceImpl paymentService = new PaymentServiceImpl();
    private ResetServiceImpl resetService = new ResetServiceImpl();

    protected static TestItReset TEST_IT = new TestItReset();

    @Override
    protected Map<String, ContractProperty> generateParameterContract() {
        Map<String, ContractProperty> parameterContract = new HashMap<>();
        parameterContract.put(CONTRACT_CONFIG_CREDITOR_ID, new ContractProperty(GOOD_CREDITOR_ID));
        return parameterContract;
    }

    @Override
    protected PaymentFormContext generatePaymentFormContext() {
        return null;
    }

    @Override
    protected String payOnPartnerWebsite(String s) {
        return null;
    }

    @Override
    protected String cancelOnPartnerWebsite(String s) {
        return null;
    }

    public static void main(String[] args) {

        RequestContext requestContext;

        // Console input needed (for phone number and OTP) => init console
        initKeyboardUser();

        //**************************************************************************************************************
        // SDD ORDER CREATE for payment

        //################################################
        // PaymentRequest step 1 not needed in integration test but need to initialize the RequestContext
        PaymentRequest paymentRequestStep1 = createPaymentRequestStep1();
        PaymentResponse paymentResponseFromPaymentRequestStep1 = TEST_IT.paymentService.paymentRequest(paymentRequestStep1);

        // ... check the response (should be a PaymentResponseFormUpdated)
        TEST_IT.checkPaymentResponseIsNotFailure(paymentResponseFromPaymentRequestStep1);
        TEST_IT.checkPaymentResponseIsRightClass("paymentRequest", paymentResponseFromPaymentRequestStep1, PaymentResponseFormUpdated.class);
        PaymentResponseFormUpdated paymentResponseStep1 = (PaymentResponseFormUpdated) paymentResponseFromPaymentRequestStep1;

        // ... consume the response if needed
        requestContext = paymentResponseStep1.getRequestContext();


        // Get input phone number
        PHONE_NUMBER_TEST = inputPhoneNumber();

        //################################################
        // PaymentRequest step 2 :
        // => API MandateWS /api/mandate/create
        // => API SignatureWS /api/initiateSignature
        // => API SignatureWS /api/sendOTP

        // Create the PaymentRequest step 2, execute the request and get the response result
        PaymentRequest paymentRequestStep2 = createPaymentRequestStep2(requestContext, PHONE_NUMBER_TEST, IBAN);
        PaymentResponse paymentResponseFromPaymentRequestStep2 = TEST_IT.paymentService.paymentRequest(paymentRequestStep2);

        // ... check the response (should be a PaymentResponseFormUpdated)
        TEST_IT.checkPaymentResponseIsNotFailure(paymentResponseFromPaymentRequestStep2);
        TEST_IT.checkPaymentResponseIsRightClass("paymentRequest", paymentResponseFromPaymentRequestStep2, PaymentResponseFormUpdated.class);
        PaymentResponseFormUpdated paymentResponseStep2 = (PaymentResponseFormUpdated) paymentResponseFromPaymentRequestStep2;

        // ... consume the response if needed
        requestContext = paymentResponseStep2.getRequestContext();


        // Get input OTP
        OTP_TEST = inputOtp();

        //################################################
        // PaymentRequest step 3 :
        // => API SignatureWS /api/setCode
        // => API SignatureWS /api/terminateSignature
        // => API MandateWS /api/order/create

        // Create the PaymentRequest step 3, execute the request and get the response result
        PaymentRequest paymentRequestStep3 = createPaymentRequestStep3(requestContext, OTP_TEST);
        PaymentResponse paymentResponseFromPaymentRequestStep3 = TEST_IT.paymentService.paymentRequest(paymentRequestStep3);

        // ... check the response (should be a PaymentResponseSuccess)
        TEST_IT.checkPaymentResponseIsNotFailure(paymentResponseFromPaymentRequestStep3);
        TEST_IT.checkPaymentResponseIsRightClass("paymentRequest", paymentResponseFromPaymentRequestStep3, PaymentResponseSuccess.class);
        PaymentResponseSuccess paymentResponseStep3 = (PaymentResponseSuccess) paymentResponseFromPaymentRequestStep3;

        // ... consume the response if needed
        RESET_ADDITIONAL_DATA = paymentResponseStep3.getTransactionAdditionalData();
        RESET_PARTNER_TRANSACTION_ID = paymentResponseStep3.getPartnerTransactionId();


        // Console input no more needed => close it !
        closeKeyboardUser();

        //**************************************************************************************************************
        // ORDER CANCEL for reset

        // Reset Request :
        // => API MandateWS /api/order/cancel/{creditorId}/{rum}/{e2eId}

        // Create the ResetRequest, execute the request and get the response result
        ResetRequest resetRequest = createResetRequest(RESET_ADDITIONAL_DATA, RESET_PARTNER_TRANSACTION_ID);
        ResetResponse resetResponseFromResetRequest = TEST_IT.resetService.resetRequest(resetRequest);

        // ... check the response (should be a RefundResponseSuccess)
        checkResetResponse(resetResponseFromResetRequest);
        
    }

    /**
     * Check the reset request response
     *
     * @param ResetResponse
     */
    private static void checkResetResponse(ResetResponse ResetResponse) {

        TEST_IT.checkResetResponseIsNotFailure(ResetResponse);
        TEST_IT.checkResetResponseIsRightClass("resetRequest", ResetResponse, ResetResponseSuccess.class);

        ResetResponseSuccess resetResponseSuccess = (ResetResponseSuccess) ResetResponse;

        Assertions.assertEquals(TRANSACTION_ID, resetResponseSuccess.getPartnerTransactionId());
        Assertions.assertTrue(SDD_ORDER_CANCEL_STATUS__CANCELLED.equalsIgnoreCase(resetResponseSuccess.getStatusCode()));

    }

    private static PaymentRequest createPaymentRequestStep1() {

        // PaymentRequest mandatory by checkIntegrity method :
        // - Amount
        // - Order
        // - Buyer
        // - ContractConfiguration
        // - Browser
        // - Environment
        // - TransactionId
        // - PartnerConfiguration

        final Amount amount = createAmount();
        final Order order = createOrder();
        final Buyer buyer = createBuyer();
        final ContractConfiguration contractConfiguration = createContractConfiguration();
        final Browser browser = createBrowser();
        final Environment environment = createEnvironment();
        final String transactionId = TRANSACTION_ID;
        final PartnerConfiguration partnerConfiguration = createPartnerConfiguration();

        final String softDescriptor = SOFT_DESCRIPTOR;

        // Init main PaymentRequest
        return PaymentRequest.builder()
                // Mandatory fields
                .withLocale(Locale.FRANCE)
                .withAmount(amount)
                .withOrder(order)
                .withBuyer(buyer)
                .withContractConfiguration(contractConfiguration)
                .withBrowser(browser)
                .withEnvironment(environment)
                .withTransactionId(transactionId)
                .withPartnerConfiguration(partnerConfiguration)
                // Optional fields
                .withSoftDescriptor(softDescriptor)
                .build();

    }

    /**
     * Create a PaymentRequest for the step 2
     *
     * @param requestContext
     * @param phoneNumber
     * @param iban
     * @return
     */
    private static PaymentRequest createPaymentRequestStep2(RequestContext requestContext, String phoneNumber, String iban) {

        // PaymentRequest mandatory by checkIntegrity method :
        // - Amount
        // - Order
        // - Buyer
        // - ContractConfiguration
        // - Browser
        // - Environment
        // - TransactionId
        // - PartnerConfiguration

        final Amount amount = createAmount();
        final Order order = createOrder();
        final Buyer buyer = createBuyer();
        final ContractConfiguration contractConfiguration = createContractConfiguration();
        final Browser browser = createBrowser();
        final Environment environment = createEnvironment();
        final String transactionId = TRANSACTION_ID;
        final PartnerConfiguration partnerConfiguration = createPartnerConfiguration();

        final String softDescriptor = SOFT_DESCRIPTOR;

        // Init PaymentFormContext
        Map<String, String> paymentFormParameter = new HashMap<>();
        paymentFormParameter.put(FORM_FIELD_PHONE, phoneNumber);

        Map<String, String> sensitivePaymentFormParameter = new HashMap<>();
        sensitivePaymentFormParameter.put(FORM_FIELD_IBAN, iban);

        final PaymentFormContext paymentFormContext = PaymentFormContext.PaymentFormContextBuilder
                .aPaymentFormContext()
                .withPaymentFormParameter(paymentFormParameter)
                .withSensitivePaymentFormParameter(sensitivePaymentFormParameter)
                .build();

        // Init main PaymentRequest
        return PaymentRequest.builder()
                // Mandatory fields
                .withLocale(Locale.FRANCE)
                .withAmount(amount)
                .withOrder(order)
                .withBuyer(buyer)
                .withContractConfiguration(contractConfiguration)
                .withBrowser(browser)
                .withEnvironment(environment)
                .withTransactionId(transactionId)
                .withPartnerConfiguration(partnerConfiguration)
                // Optional fields
                .withSoftDescriptor(softDescriptor)
                .withRequestContext(requestContext)
                .withPaymentFormContext(paymentFormContext)
                .build();

    }

    /**
     * Create a PaymentRequest for the step 3
     *
     * @param requestContext
     * @param otp
     * @return
     */
    private static PaymentRequest createPaymentRequestStep3(RequestContext requestContext, String otp) {

        // PaymentRequest mandatory by checkIntegrity method :
        // - Amount
        // - Order
        // - Buyer
        // - ContractConfiguration
        // - Browser
        // - Environment
        // - TransactionId
        // - PartnerConfiguration

        final Amount amount = createAmount();
        final Order order = createOrder();
        final Buyer buyer = createBuyer();
        final ContractConfiguration contractConfiguration = createContractConfiguration();
        final Browser browser = createBrowser();
        final Environment environment = createEnvironment();
        final String transactionId = TRANSACTION_ID;
        final PartnerConfiguration partnerConfiguration = createPartnerConfiguration();

        final String softDescriptor = SOFT_DESCRIPTOR;

        // Init PaymentFormContext
        Map<String, String> paymentFormParameter = new HashMap<>();
        paymentFormParameter.put(FORM_FIELD_OTP, otp);

        Map<String, String> sensitivePaymentFormParameter = new HashMap<>();

        final PaymentFormContext paymentFormContext = PaymentFormContext.PaymentFormContextBuilder
                .aPaymentFormContext()
                .withPaymentFormParameter(paymentFormParameter)
                .withSensitivePaymentFormParameter(sensitivePaymentFormParameter)
                .build();

        // Init main PaymentRequest
        return PaymentRequest.builder()
                // Mandatory fields
                .withLocale(Locale.FRANCE)
                .withAmount(amount)
                .withOrder(order)
                .withBuyer(buyer)
                .withContractConfiguration(contractConfiguration)
                .withBrowser(browser)
                .withEnvironment(environment)
                .withTransactionId(transactionId)
                .withPartnerConfiguration(partnerConfiguration)
                // Optional fields
                .withSoftDescriptor(softDescriptor)
                .withRequestContext(requestContext)
                .withPaymentFormContext(paymentFormContext)
                .build();

    }

    /**
     *
     *
     * @param paymentResponse
     */
    private void checkPaymentResponseIsNotFailure(PaymentResponse paymentResponse) {
        Assertions.assertFalse(paymentResponse instanceof PaymentResponseFailure, () -> {
            return "paymentRequest returned PaymentResponseFailure (Failure cause = " + ((PaymentResponseFailure) paymentResponse).getFailureCause() + ", errorCode = " + ((PaymentResponseFailure) paymentResponse).getErrorCode();
        });
    }

    /**
     *
     *
     * @param requestName
     * @param paymentResponse
     * @param clazz
     */
    private void checkPaymentResponseIsRightClass(String requestName, PaymentResponse paymentResponse, Class clazz) {
        Assertions.assertTrue(paymentResponse.getClass().isAssignableFrom(clazz), () -> {
            return requestName + " did not return a " + clazz.getSimpleName() + " (" + paymentResponse.toString() + ")";
        });
    }

    /**
     *
     *
     * @param ResetResponse
     */
    protected void checkResetResponseIsNotFailure(ResetResponse ResetResponse) {
        Assertions.assertFalse(ResetResponse instanceof ResetResponseFailure, () -> {
            return "ResetResponse returned ResetResponseFailure (Failure cause = " + ((ResetResponseFailure) ResetResponse).getFailureCause() + ", errorCode = " + ((ResetResponseFailure) ResetResponse).getErrorCode();
        });
    }

    /**
     *
     *
     * @param ResetResponse
     */
    protected void checkResetResonseIsFailure(ResetResponse ResetResponse) {
        Assertions.assertTrue(ResetResponse instanceof ResetResponseFailure, () -> {
            return "ResetResponse didn't return ResetResponseFailure (Failure cause = " + ((ResetResponseFailure) ResetResponse).getFailureCause() + ", errorCode = " + ((ResetResponseFailure) ResetResponse).getErrorCode();
        });
    }

    /**
     *
     *
     * @param requestName
     * @param ResetResponse
     * @param clazz
     */
    protected void checkResetResponseIsRightClass(String requestName, ResetResponse ResetResponse, Class clazz) {
        Assertions.assertTrue(ResetResponse.getClass().isAssignableFrom(clazz), () -> {
            return requestName + " did not return a " + clazz.getSimpleName() + " (" + ResetResponse.toString() + ")";
        });
    }

    /**
     * Create a ResetRequest
     *
     * @param transactionAdditionalData
     * @param transactionAdditionalData
     * @return
     */
    protected static ResetRequest createResetRequest(String transactionAdditionalData, String partnerTransactionId) {

        // PaymentRequest mandatory by checkIntegrity method :
        // - Amount
        // - Order
        // - Buyer
        // - ContractConfiguration
        // - Environment
        // - TransactionId
        // - PartnerConfiguration

        final Amount amount = createAmount();
        final Order order = createOrder();
        final Buyer buyer = createBuyer();
        final ContractConfiguration contractConfiguration = createContractConfiguration();
        final Environment environment = createEnvironment();
        final String transactionId = TRANSACTION_ID;
        final PartnerConfiguration partnerConfiguration = createPartnerConfiguration();

        final String softDescriptor = SOFT_DESCRIPTOR;

        return ResetRequest.ResetRequestBuilder.aResetRequest()
                // Mandatory fields
                .withAmount(amount)
                .withOrder(order)
                .withBuyer(buyer)
                .withContractConfiguration(contractConfiguration)
                .withEnvironment(environment)
                .withTransactionId(transactionId)
                .withPartnerConfiguration(partnerConfiguration)
                // Optional fields
                .withTransactionAdditionalData(transactionAdditionalData)
                .withSoftDescriptor(softDescriptor)
                .withPartnerTransactionId(partnerTransactionId)
                .build();

    }

    /**
     * Initialize Amount parameters
     *
     * @return
     */
    private static Amount createAmount() {
        return new Amount(
                AMOUNT_VALUE,
                Currency.getInstance(AMOUNT_CURRENCY)
        );
    }

    /**
     * Initialize Environment parameters
     *
     * @return
     */
    private static Environment createEnvironment() {
        return new Environment(
                NOTIFICATION_URL,
                SUCCESS_URL,
                CANCEL_URL,
                true
        );
    }

    /**
     * Initialize Order parameters
     *
     * @return
     */
    private static Order createOrder() {
        return Order.OrderBuilder.anOrder()
                .withReference(ORDER_REF)
                .withAmount(createAmount())
                .build();
    }

    /**
     *
     *
     * @return
     */
    private static Buyer createBuyer() {

        final String email = "testdocapost@yopmail.com";
        final Buyer.FullName fullName = createFullName();
        final Map<Buyer.AddressType, Buyer.Address> addresses = createDefaultAddresses();

        return Buyer.BuyerBuilder.aBuyer()
                .withEmail(email)
                .withFullName(fullName)
                .withAddresses(addresses)
                .build();

    }

    /**
     *
     *
     * @return
     */
    public static Buyer.FullName createFullName() {
        return new Buyer.FullName(
                "Albert",
                "Dupont",
                "Mr"
        );
    }

    /**
     *
     *
     * @return
     */
    private static Map<Buyer.AddressType, Buyer.Address> createDefaultAddresses() {
        Buyer.Address address = createDefaultCompleteAddress();
        return createAddresses(address);
    }

    /**
     *
     *
     * @param address
     * @return
     */
    private static Map<Buyer.AddressType, Buyer.Address> createAddresses(Buyer.Address address) {
        Map<Buyer.AddressType, Buyer.Address> addresses = new HashMap<>();
        addresses.put(Buyer.AddressType.DELIVERY, address);
        addresses.put(Buyer.AddressType.BILLING, address);
        return addresses;
    }

    /**
     *
     *
     * @return
     */
    private static Buyer.Address createDefaultCompleteAddress() {
        return createCompleteAddress(
                "12 rue Paradis",
                "residence azerty",
                "Paris",
                "75001",
                "FR"
        );
    }

    /**
     *
     *
     * @param street
     * @param street2
     * @param city
     * @param zip
     * @param country
     * @return
     */
    private static Buyer.Address createCompleteAddress(String street, String street2, String city, String zip, String country) {
        return Buyer.Address.AddressBuilder.anAddress()
                .withStreet1(street)
                .withStreet2(street2)
                .withCity(city)
                .withZipCode(zip)
                .withCountry(country)
                .build();
    }

    /**
     *
     *
     * @return
     */
    private static Browser createBrowser() {
        return new Browser(
                "",
                Locale.FRANCE
        );
    }

    /**
     * Initialize ContractConfiguration map parameters
     *
     * @return
     */
    private static ContractConfiguration createContractConfiguration() {

        final HashMap<String, ContractProperty> contractConfigurationMap = new HashMap<>();
        contractConfigurationMap.put(CONTRACT_CONFIG_CREDITOR_ID, new ContractProperty(GOOD_CREDITOR_ID));

        final ContractConfiguration contractConfiguration = new ContractConfiguration("Docapost", contractConfigurationMap);

        return contractConfiguration;

    }

    /**
     * Initialize PartnerConfiguration map parameters
     *
     * @return
     */
    public static PartnerConfiguration createPartnerConfiguration() {

        HashMap<String, String> partnerConfigurationMap = new HashMap<>();
        partnerConfigurationMap.put(PARTNER_CONFIG_AUTH_LOGIN, GOOD_LOGIN);

        HashMap<String, String> sensitivePartnerConfigurationMap = new HashMap<>();
        sensitivePartnerConfigurationMap.put(PARTNER_CONFIG_AUTH_PASS, GOOD_PWD);

        final PartnerConfiguration partnerConfiguration = new PartnerConfiguration(partnerConfigurationMap, sensitivePartnerConfigurationMap);

        return partnerConfiguration;

    }

    /**
     *
     */
    private static void initKeyboardUser() {
        KEYBOARD_USER  = new Scanner(System.in);
    }

    /**
     *
     */
    private static void resetKeyboardUser() {
        KEYBOARD_USER.reset();
    }

    /**
     *
     */
    private static void closeKeyboardUser() {
        KEYBOARD_USER.close();
    }

    /**
     * Defini le numero de téléphone utilisé pour l'envoi d'OTP
     *
     * @return
     */
    private static String inputPhoneNumber() {

        resetKeyboardUser();

        System.out.println("Enter your phone number : ");
        String input = KEYBOARD_USER.nextLine();
        System.out.print("You entered : ");
        System.out.println(input);

        PHONE_NUMBER_TEST = input;

        return input;

    }

    /**
     * Defini l'OTP reçu au numéro de téléphone
     *
     * @return
     */
    private static String inputOtp() {

        resetKeyboardUser();

        System.out.println("Enter your OTP : ");
        String input = KEYBOARD_USER.nextLine();
        System.out.print("You entered : ");
        System.out.println(input);
        resetKeyboardUser();

        OTP_TEST = input;

        return input;

    }

}