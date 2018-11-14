package com.payline.payment.docapost.Integration;

import com.google.gson.Gson;
import com.payline.payment.docapost.TestUtils;
import com.payline.payment.docapost.bean.PaymentResponseSuccessAdditionalData;
import com.payline.payment.docapost.service.impl.PaymentServiceImpl;
import com.payline.payment.docapost.service.impl.PaymentWithRedirectionServiceImpl;
import com.payline.payment.docapost.utils.config.ConfigEnvironment;
import com.payline.payment.docapost.utils.config.ConfigProperties;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.payment.PaymentFormContext;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFailure;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFormUpdated;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseSuccess;
import com.payline.pmapi.integration.AbstractPaymentIntegration;
import com.payline.pmapi.service.PaymentService;
import com.payline.pmapi.service.PaymentWithRedirectionService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static com.payline.payment.docapost.TestUtils.*;
import static com.payline.payment.docapost.utils.DocapostConstants.*;


public class TestIt extends AbstractPaymentIntegration {
    private PaymentServiceImpl paymentService = new PaymentServiceImpl();
    private PaymentWithRedirectionServiceImpl paymentWithRedirectionService = new PaymentWithRedirectionServiceImpl();
    private static final String GOOD_CREDITOR_ID = "MARCHAND1";

    @Override
    protected Map<String, ContractProperty> generateParameterContract() {
        Map<String, ContractProperty> parameterContract = new HashMap<>();
        parameterContract.put(CONTRACT_CONFIG_CREDITOR_ID, new ContractProperty(GOOD_CREDITOR_ID));
        return parameterContract;
    }

    @Override
    protected PaymentFormContext generatePaymentFormContext() {
        //Saisir numero de telephone sur lequel sera envoyé l'
        //Open a prompt which allows user to enter his phone number
        //Iban is set automatically
        String telephone = setupNumber();
        return createDefaultPaymentFormContext(telephone);
    }


    //todo find a better way to get a prompt
    public static void main(String[] args) {

        TestIt testIt = new TestIt();

        //Saisie du numéro de téléphone
        String phoneNumber = setupNumber();
        System.out.println("Wait 5-10 seconds... ");

        //step 2 Create a mandate
        //Saisir OTP
        PaymentRequest paymentRequestStep2 = createDefaultPaymentRequestStep2(phoneNumber);
        PaymentResponseFormUpdated paymentResponseStep2 = (PaymentResponseFormUpdated) testIt.paymentService.paymentRequest(paymentRequestStep2);

        Map<String, String> requestContextMain = paymentResponseStep2.getRequestContext().getRequestData();
        // Create a Payment request from payment request step 2 result

        //TODO REQUEST WITH PAYMENT RESPONSE STEP2
        Scanner keyboardOTP = new Scanner(System.in);
        keyboardOTP.reset();
        System.out.println("Enter your  OTP : ");
        String otp = keyboardOTP.nextLine();
        System.out.print("You entered : ");
        System.out.println(otp);
        keyboardOTP.close();
        //Step 3 confirm  transaction with the OTP inputed
        // !!! PaymentSuccess renvoyé
        PaymentRequest paymentRequestStep3 = createCustomPaymentRequestStep3(requestContextMain, otp, phoneNumber);

        testIt.checkPaymentResponse(paymentRequestStep3, testIt.paymentService, testIt.paymentWithRedirectionService);

        /*Below test method from parent Class
        /Dont work now because payment method return a PaymentSuccessResponse vs PaymentRedirectReponse is expected
        */
//        testIt.fullRedirectionPayment(paymentRequestStep3, testIt.paymentService, testIt.paymentWithRedirectionService);

    }


    @Test
    public void fullPaymentTest() {

    }

    @Override
    protected String payOnPartnerWebsite(String partnerUrl) {
        //we are not redirected to Docapost website
        return SUCCESS_URL;
    }

    @Override
    public PaymentRequest createDefaultPaymentRequest() {
        return TestUtils.createCompletePaymentBuilder().build();

    }


    @Override
    protected String cancelOnPartnerWebsite(String s) {
        return null;
    }


    /*
     * Test below inspired from parent Class method
     * Check we have a good PaymentResponseSuccess
     *
     */
    public void checkPaymentResponse(PaymentRequest paymentRequest, PaymentService paymentService, PaymentWithRedirectionService paymentWithRedirectionService) {
        PaymentResponse paymentResponseFromPaymentRequest = paymentService.paymentRequest(paymentRequest);
        this.checkPaymentResponseIsNotFailure(paymentResponseFromPaymentRequest);
        this.checkPaymentResponseIsRightClass("paymentRequest", paymentResponseFromPaymentRequest, PaymentResponseSuccess.class);
        PaymentResponseSuccess paymentResponseSuccess = (PaymentResponseSuccess) paymentResponseFromPaymentRequest;
        Assertions.assertNotNull(paymentResponseSuccess.getTransactionDetails());
        String partnerTransactionId = paymentResponseSuccess.getPartnerTransactionId();
        Assertions.assertEquals(partnerTransactionId, paymentResponseSuccess.getPartnerTransactionId());
        //Url to send ??
        //Mock
        String partnerUrl = paymentResponseSuccess.getStatusCode();
        String redirectionUrl = this.payOnPartnerWebsite(partnerUrl);
        Assertions.assertEquals("https://succesurl.com/", redirectionUrl);

        //Check mandate
        String mandateLink = this.getMandateUrl(paymentResponseSuccess);
        Assertions.assertNotNull(mandateLink);
        System.out.println("link to mandate below ");
        System.out.println(mandateLink);

    }

    //Test inte KO PaymentresponseSuccess returned vs PaymentResponseRedirect
    private void checkPaymentResponseIsNotFailure(PaymentResponse paymentResponse) {
        Assertions.assertFalse(paymentResponse instanceof PaymentResponseFailure, () -> {
            return "paymentRequest returned PaymentResponseFailure (Failure cause = " + ((PaymentResponseFailure) paymentResponse).getFailureCause() + ", errorCode = " + ((PaymentResponseFailure) paymentResponse).getErrorCode();
        });
    }

    private void checkPaymentResponseIsRightClass(String requestName, PaymentResponse paymentResponse, Class clazz) {
        Assertions.assertTrue(paymentResponse.getClass().isAssignableFrom(clazz), () -> {
            return requestName + " did not return a " + clazz.getSimpleName() + " (" + paymentResponse.toString() + ")";
        });
    }


    private String getMandateUrl(PaymentResponseSuccess paymentResponseStep3) {
        String additionalDataJson = paymentResponseStep3.getTransactionAdditionalData();

        Gson gson = new Gson();
        PaymentResponseSuccessAdditionalData paymentResponseSuccessAdditionalData = gson.fromJson(additionalDataJson, PaymentResponseSuccessAdditionalData.class);

        //Next step : download mandate
        String mandateRum = paymentResponseSuccessAdditionalData.getMandateRum();


        return ConfigProperties.get(CONFIG_SCHEME, ConfigEnvironment.DEV)
                + "://"
                + ConfigProperties.get(CONFIG_HOST, ConfigEnvironment.DEV)
                + "/"
                + ConfigProperties.get(CONFIG_PATH_WSMANDATE_MANDATE_PDFTPL)
                + "/"
                + GOOD_CREDITOR_ID
                + "/"
                + mandateRum;
    }
}