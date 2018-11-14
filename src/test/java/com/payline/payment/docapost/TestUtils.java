package com.payline.payment.docapost;

import com.payline.payment.docapost.bean.PaymentResponseSuccessAdditionalData;
import com.payline.pmapi.bean.common.Amount;
import com.payline.pmapi.bean.common.Buyer;
import com.payline.pmapi.bean.common.Buyer.Address;
import com.payline.pmapi.bean.configuration.PartnerConfiguration;
import com.payline.pmapi.bean.configuration.request.ContractParametersCheckRequest;
import com.payline.pmapi.bean.payment.*;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;
import com.payline.pmapi.bean.paymentform.request.PaymentFormConfigurationRequest;
import com.payline.pmapi.bean.refund.request.RefundRequest;
import com.payline.pmapi.bean.reset.request.ResetRequest;

import java.math.BigInteger;
import java.util.*;

import static com.payline.payment.docapost.utils.DocapostConstants.*;

/**
 * Class with method to generate mock easier
 */
public class TestUtils {

    public static final String SUCCESS_URL = "https://succesurl.com/";
    public static final String CANCEL_URL = "http://localhost/cancelurl.com/";
    public static final String NOTIFICATION_URL = "http://google.com/";
    public static final String GOOD_CREDITOR_ID = "MARCHAND1"; //testItSlimpay //democreditor01
    public static final String GOOD_LOGIN = "payline@docapost.fr"; //
    public static final String GOOD_PWD = "J:[ef8dccma";
    public static String PHONE_NUMBER_TEST = "06060606";
    public HashMap<String, String> extendedData;


    /**
     * Create a paymentRequest with default parameters.
     *
     * @return paymentRequest created
     */
    public static PaymentRequest createDefaultPaymentRequest() {
        final Amount amount = createAmount("EUR");
        final ContractConfiguration contractConfiguration = createContractConfiguration();
        final Environment paylineEnvironment = new Environment(NOTIFICATION_URL, SUCCESS_URL, CANCEL_URL, true);
        final String transactionID = "transactionID";
        final Order order = createOrder(transactionID);
        final String softDescriptor = "softDescriptor";


        return PaymentRequest.builder()
                .withAmount(amount)
                .withBrowser(new Browser("", Locale.FRANCE))
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(contractConfiguration)
                .withEnvironment(paylineEnvironment)
                .withOrder(order)
                .withBuyer(createDefaultBuyer())
                .withTransactionId(transactionID)
                .withSoftDescriptor(softDescriptor)
                .withEnvironment(createDefaultEnvironment())
                .withPartnerConfiguration(createDefaultPartnerConfiguration())
                .withSoftDescriptor(softDescriptor)
                .build();
    }

    public static PaymentRequest createDefaultPaymentRequestStep2(String phoneNumberTest) {
        final Amount amount = createAmount("EUR");
        final ContractConfiguration contractConfiguration = createContractConfiguration();
        final Environment environment = new Environment(NOTIFICATION_URL, SUCCESS_URL, CANCEL_URL, true);
        final String transactionID = "transactionID";
        final Order order = createOrder(transactionID);
        final String softDescriptor = "softDescriptor";
        //TODO find a better way to do it
        Map<String, String> requestData = new HashMap<>();
        requestData.put(CONTEXT_DATA_STEP, CONTEXT_DATA_STEP_IBAN_PHONE);
        requestData.put(CONTEXT_DATA_MANDATE_RUM, createRUM());
        requestData.put(CONTEXT_DATA_TRANSACTION_ID, transactionID);
        requestData.put(CONTEXT_DATA_SIGNATURE_ID, createsignatureId());

        final RequestContext requestContext = RequestContext.RequestContextBuilder
                .aRequestContext()
                .withRequestData(requestData)
                .build();


        return PaymentRequest.builder()
                .withAmount(amount)
                .withBrowser(new Browser("", Locale.FRANCE))
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(contractConfiguration)
                .withEnvironment(environment)
                .withOrder(order)
                .withBuyer(createDefaultBuyer())
                .withTransactionId(transactionID)
                .withSoftDescriptor(softDescriptor)
                .withEnvironment(createDefaultEnvironment())
                .withPartnerConfiguration(createDefaultPartnerConfiguration())
                .withSoftDescriptor(softDescriptor)
                .withRequestContext(requestContext)
                .withPaymentFormContext(createDefaultPaymentFormContext(phoneNumberTest))
                .build();
    }


    public static PaymentRequest createCustomPaymentRequestStep3(Map<String, String> customRequestData, String otp, String phoneNumberTest) {
        final Amount amount = createAmount("EUR");
        final ContractConfiguration contractConfiguration = createContractConfiguration();
        final Environment environment = new Environment(NOTIFICATION_URL, SUCCESS_URL, CANCEL_URL, true);
        final String transactionID = customRequestData.get("transactionId");
        //  final String transactionID = "transactionID";
        final Order order = createOrder(transactionID);
        final String softDescriptor = "softDescriptor";
        //TODO find a better way to do it
        Map<String, String> requestData = customRequestData;
        final RequestContext requestContext = RequestContext.RequestContextBuilder
                .aRequestContext()
                .withRequestData(requestData)
                .build();

        Map<String, String> paymentFormParameter = new HashMap<>();
        paymentFormParameter.put(FORM_FIELD_PHONE, phoneNumberTest);
        paymentFormParameter.put(FORM_FIELD_OTP, otp);

        Map<String, String> sensitivePaymentFormParameter = new HashMap<>();
        sensitivePaymentFormParameter.put(FORM_FIELD_IBAN, "FR7630076020821234567890186");
        PaymentFormContext paymentFormContext = createCustomPaymentFormContext(paymentFormParameter, sensitivePaymentFormParameter);

        return PaymentRequest.builder()
                .withAmount(amount)
                .withBrowser(new Browser("", Locale.FRANCE))
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(contractConfiguration)
                .withEnvironment(environment)
                .withOrder(order)
                .withBuyer(createDefaultBuyer())
                .withTransactionId(transactionID)
                .withSoftDescriptor(softDescriptor)
                .withEnvironment(createDefaultEnvironment())
                .withPartnerConfiguration(createDefaultPartnerConfiguration())
                .withSoftDescriptor(softDescriptor)
                .withRequestContext(requestContext)
//                .withPaymentFormContext(createDefaultPaymentFormContext())
                .withPaymentFormContext(paymentFormContext)
                .build();
    }

    /**
     * Create a default form context for Unit Test and IT Test
     *
     * @return PaymentFormContext which contain a mobile phone number and a iban
     */
    public static PaymentFormContext createDefaultPaymentFormContext(String phoneNumber) {
        Map<String, String> paymentFormParameter = new HashMap<>();
        paymentFormParameter.put(FORM_FIELD_PHONE, phoneNumber);
        paymentFormParameter.put(FORM_FIELD_OTP, "1234");

        Map<String, String> sensitivePaymentFormParameter = new HashMap<>();
        sensitivePaymentFormParameter.put(FORM_FIELD_IBAN, "FR7630076020821234567890186");
//        sensitivePaymentFormParameter.put(FORM_FIELD_OTP,"1234");

        //Ajout du numero de telephone et IBAN a la requête
        return PaymentFormContext.PaymentFormContextBuilder
                .aPaymentFormContext()
                .withPaymentFormParameter(paymentFormParameter)
                .withSensitivePaymentFormParameter(sensitivePaymentFormParameter)
                .build();

    }


    public static PaymentFormContext createCustomPaymentFormContext(Map<String, String> customPaymentFormParameter, Map<String, String> customSensitivePaymentFormParameter) {
        //Ajout du numero de telephone et IBAN a la requête
        return PaymentFormContext.PaymentFormContextBuilder
                .aPaymentFormContext()
                .withPaymentFormParameter(customPaymentFormParameter)
                .withSensitivePaymentFormParameter(customSensitivePaymentFormParameter)
                .build();

    }

    public static PaymentRequest createCompletePaymentRequest() {
        return createCompletePaymentBuilder().build();

    }

//    public static RefundRequest createRefundRequest(String transactionId) {
//        final PaylineEnvironment paylineEnvironment = new PaylineEnvironment(NOTIFICATION_URL, SUCCESS_URL, CANCEL_URL, true);
////       final String transactionID = createTransactionId();
//        final Amount amount = createAmount("EUR");
//        final Map<String, String> partnerConfiguration = new HashMap<>();
//        return RefundRequest.RefundRequestBuilder.aRefundRequest()
//                .withAmount(amount)
//                .withOrder(createOrder(transactionId, amount))
//                .withBuyer(createDefaultBuyer())
//                .withContractConfiguration(createContractConfiguration())
//                .withPaylineEnvironment(paylineEnvironment)
//                .withTransactionId(transactionId)
//                .withPartnerTransactionId("toto")
//                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration))
//                .build();
//    }

    public static RedirectionPaymentRequest createRedirectionPaymentRequest() {
        RedirectionPaymentRequest request = RedirectionPaymentRequest.builder().build();


        return request;
    }

    /**
     * Create a complete payment request used for Integration Tests
     *
     * @return PaymentRequest.Builder
     */

    public static PaymentRequest.Builder createCompletePaymentBuilder() {
        final Amount amount = createAmount("EUR");
        final ContractConfiguration contractConfiguration = createContractConfiguration();

        final Environment paylineEnvironment = new Environment(NOTIFICATION_URL, SUCCESS_URL, CANCEL_URL, true);
        final String transactionID = createTransactionId();
        final Order order = createOrder(transactionID);
        final String softDescriptor = "softDescriptor";
        final Locale locale = new Locale("FR");

        return PaymentRequest.builder()
                .withAmount(amount)
                .withBrowser(new Browser("", Locale.FRANCE))
                .withContractConfiguration(contractConfiguration)
                .withEnvironment(paylineEnvironment)
                .withOrder(order)
                .withLocale(locale)
                .withTransactionId(transactionID)
                .withSoftDescriptor(softDescriptor)
                .withPaymentFormContext(createDefaultPaymentFormContext(PHONE_NUMBER_TEST))
                .withPartnerConfiguration(createDefaultPartnerConfiguration())
                .withLocale(Locale.FRANCE)
                .withBuyer(createDefaultBuyer());
    }

    public static ResetRequest createResetRequest() {
        final ContractConfiguration contractConfiguration = createContractConfiguration();
        String rum = createRUM();
        final String transactionID = createTransactionId();
        final Environment environment = new Environment(NOTIFICATION_URL, SUCCESS_URL, CANCEL_URL, true);
        PaymentResponseSuccessAdditionalData additionalData = new PaymentResponseSuccessAdditionalData().mandateRum(rum).transactionId(transactionID).signatureId("signature1");
        final Order order = createOrder(transactionID);
        final String softDescriptor = "softDescriptor";
        //TODO find a better way to do it
        Map<String, String> requestData = new HashMap<>();
        requestData.put(CONTEXT_DATA_STEP, CONTEXT_DATA_STEP_IBAN_PHONE);
        requestData.put(CONTEXT_DATA_MANDATE_RUM, rum);


        return ResetRequest.ResetRequestBuilder
                .aResetRequest()
                .withAmount(createAmount("EUR"))
                .withOrder(createOrder(transactionID))
                .withBuyer(createDefaultBuyer())
                .withTransactionId(transactionID)
                .withContractConfiguration(contractConfiguration)
                .withPartnerConfiguration(createDefaultPartnerConfiguration())
                .withTransactionAdditionalData(additionalData.toJson())
                .withEnvironment(createDefaultEnvironment())
                .withPartnerTransactionId(createTransactionId())
                .build();
    }

    public static RefundRequest createRefundRequest() {
        final ContractConfiguration contractConfiguration = createContractConfiguration();
        String rum = createRUM();
        final String transactionID = createTransactionId();
//        final Environment environment = new Environment(NOTIFICATION_URL, SUCCESS_URL, CANCEL_URL, true);
        PaymentResponseSuccessAdditionalData additionalData = new PaymentResponseSuccessAdditionalData().mandateRum(rum).transactionId(transactionID).signatureId("signature1");
        final Order order = createOrder(transactionID);
        final String softDescriptor = "softDescriptor";
        //TODO find a better way to do it
        Map<String, String> requestData = new HashMap<>();
        requestData.put(CONTEXT_DATA_STEP, CONTEXT_DATA_STEP_IBAN_PHONE);
        requestData.put(CONTEXT_DATA_MANDATE_RUM, rum);


        return RefundRequest.RefundRequestBuilder
                .aRefundRequest()
                .withAmount(createAmount("EUR"))
                .withOrder(order)
                .withBuyer(createDefaultBuyer())
                .withTransactionId(transactionID)
                .withSoftDescriptor(softDescriptor)
                .withContractConfiguration(contractConfiguration)
                .withPartnerConfiguration(createDefaultPartnerConfiguration())
                .withTransactionAdditionalData(additionalData.toJson())
                .withEnvironment(createDefaultEnvironment())
                .withPartnerTransactionId(createTransactionId())
                .build();
    }


    public static String createTransactionId() {
        return "transactionID" + Calendar.getInstance().getTimeInMillis();
    }

    public static String createsignatureId() {
        return "signatureID" + Calendar.getInstance().getTimeInMillis();
    }

    public static String createRUM() {
        return "RUM" + Calendar.getInstance().getTimeInMillis();
    }

    public static Map<Buyer.AddressType, Buyer.Address> createAddresses(Buyer.Address address) {
        Map<Buyer.AddressType, Buyer.Address> addresses = new HashMap<>();
        addresses.put(Buyer.AddressType.DELIVERY, address);
        addresses.put(Buyer.AddressType.BILLING, address);

        return addresses;
    }

    public static Map<Buyer.AddressType, Buyer.Address> createDefaultAddresses() {
        Address address = createDefaultCompleteAddress();
        return createAddresses(address);
    }

    public static Amount createAmount(String currency) {
        return new Amount(BigInteger.TEN, Currency.getInstance(currency));
    }

    public static Order createOrder(String transactionID) {
        return Order.OrderBuilder.anOrder().withReference(transactionID).withAmount(createAmount("EUR")).build();
    }

    public static Order createOrder(String transactionID, Amount amount) {
        return Order.OrderBuilder.anOrder().withReference(transactionID).withAmount(amount).build();
    }


    public static Buyer.FullName createFullName() {
        return new Buyer.FullName("foo", "bar", Buyer.Civility.MR);
    }

    public static Map<Buyer.PhoneNumberType, String> createDefaultPhoneNumbers() {
        Map<Buyer.PhoneNumberType, String> phoneNumbers = new HashMap<>();
        phoneNumbers.put(Buyer.PhoneNumberType.BILLING, "+33606060606");
        phoneNumbers.put(Buyer.PhoneNumberType.CELLULAR, "+33707070707");

        return phoneNumbers;
    }

    public static ContractConfiguration createContractConfiguration() {
        final ContractConfiguration contractConfiguration = new ContractConfiguration("DocaPost", new HashMap<>());
        contractConfiguration.getContractProperties().put(CONTRACT_CONFIG_CREDITOR_ID, new ContractProperty(GOOD_CREDITOR_ID));
        contractConfiguration.getContractProperties().put(CONFIG_HOST, new ContractProperty("https://espaceclient.sepalia.fr/rcte"));
        contractConfiguration.getContractProperties().put(CONFIG_PATH_WSMANDATE_MANDATE_CREATE, new ContractProperty("mandate"));
        return contractConfiguration;
    }

    public static Map<String, String> createAccountInfo() {
        Map<String, String> accountInfo = new HashMap<>();
        accountInfo.put(CONTRACT_CONFIG_CREDITOR_ID, GOOD_CREDITOR_ID);
        return accountInfo;
    }

    public static ContractParametersCheckRequest createContractParametersCheckRequest() {

        final ContractParametersCheckRequest contractParametersCR = ContractParametersCheckRequest
                .CheckRequestBuilder
                .aCheckRequest()
                .withContractConfiguration(createContractConfiguration())
                .withAccountInfo(createAccountInfo())
                .withEnvironment(createDefaultEnvironment())
                .withLocale(Locale.FRANCE)
                .withPartnerConfiguration(createDefaultPartnerConfiguration())
                .build();

        return contractParametersCR;
    }


    public static Buyer.Address createAddress(String street, String city, String zip) {
        return Buyer.Address.AddressBuilder.anAddress()
                .withStreet1(street)
                .withCity(city)
                .withZipCode(zip)
                .withCountry("FR")
                .build();
    }


    public static Buyer.Address createCompleteAddress(String street, String street2, String city, String zip, String country) {
        return Buyer.Address.AddressBuilder.anAddress()
                .withStreet1(street)
                .withStreet2(street2)
                .withCity(city)
                .withZipCode(zip)
                .withCountry(country)
                .build();
    }

    public static Buyer.Address createDefaultAddress() {
        return createAddress("12 a street", "Paris", "75000");
    }

    public static Buyer.Address createDefaultCompleteAddress() {
        return createCompleteAddress("12 rue Paradis", "residence azerty", "Paris", "75001", "FR");
    }

    public static Buyer createBuyer(Map<Buyer.PhoneNumberType, String> phoneNumbers, Map<Buyer.AddressType, Buyer.Address> addresses, Buyer.FullName fullName) {
        return Buyer.BuyerBuilder.aBuyer()
                .withEmail("testdocapost@yopmail.com")
                .withPhoneNumbers(phoneNumbers)
                .withAddresses(addresses)
                .withFullName(fullName)
                .withCustomerIdentifier("subscriber1")
                .withExtendedData(createDefaultExtendedData())
                .build();
    }


    public static Map<String, String> createDefaultExtendedData() {

        HashMap<String, String> extData = new HashMap<>();
        extData.put("bic", "TESTFRP1");
        extData.put("iban", "FR7630076020821234567890186");
        extData.put("country", "FR");
        extData.put("institutionName", "Bank Test");
        return extData;

    }

    public static Buyer createDefaultBuyer() {
        return createBuyer(createDefaultPhoneNumbers(), createDefaultAddresses(), createFullName());
    }


    public static Environment createDefaultEnvironment() {
        return new Environment("http://notificationURL.com", "http://redirectionURL.com", "http://redirectionCancelURL.com", true);
    }

    public static PartnerConfiguration createDefaultPartnerConfiguration() {
        Map<String, String> partnerConfiguration = new HashMap<>();
        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();
        sensitivePartnerConfiguration.put(PARTNER_CONFIG_AUTH_LOGIN, GOOD_LOGIN);
        sensitivePartnerConfiguration.put(PARTNER_CONFIG_AUTH_PASS, GOOD_PWD);

        return new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration);
    }

    public static PaymentFormConfigurationRequest createDefaultPaymentFormConfigurationRequest() {
        return PaymentFormConfigurationRequest.PaymentFormConfigurationRequestBuilder.aPaymentFormConfigurationRequest()
                .withLocale(Locale.FRANCE)
                .withBuyer(createDefaultBuyer())
                .withAmount(new Amount(null, Currency.getInstance("EUR")))
                .withContractConfiguration(createContractConfiguration())
                .withOrder(createOrder("007"))
                .withEnvironment(createDefaultEnvironment())
                .withPartnerConfiguration(createDefaultPartnerConfiguration())

                .build();
    }

    /**
     * Defini le numero de téléphone utilisé pour l'envoi d'OTP
     *
     * @return
     */
    public static String setupNumber() {

        Scanner keyboardUser = new Scanner(System.in);
        System.out.println("Enter your  phone number : ");
        String phoneNumber = keyboardUser.nextLine();
        keyboardUser.reset();
        PHONE_NUMBER_TEST = phoneNumber;
        return phoneNumber;
    }


}
