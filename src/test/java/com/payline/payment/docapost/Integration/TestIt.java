package com.payline.payment.docapost.Integration;

import com.payline.payment.docapost.TestUtils;
import com.payline.payment.docapost.service.impl.PaymentServiceImpl;
import com.payline.payment.docapost.service.impl.PaymentWithRedirectionServiceImpl;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.payment.PaymentFormContext;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFormUpdated;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseSuccess;
import com.payline.pmapi.bean.paymentform.response.configuration.PaymentFormConfigurationResponse;
import com.payline.pmapi.integration.AbstractPaymentIntegration;
import com.payline.pmapi.service.PaymentService;
import com.payline.pmapi.service.PaymentWithRedirectionService;
import org.junit.Assert;
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
    public static final String GOOD_CREDITOR_ID = "MARCHAND1";
    //Todo  find a better way to do it
    public Map<String, String> requestContext;
    private PaymentFormConfigurationResponse formConfigutationResponse;

    private String transactionID = "";
    private PaymentRequest request;

    @Override
    protected Map<String, ContractProperty> generateParameterContract() {
        Map<String, ContractProperty> parameterContract = new HashMap<>();
        parameterContract.put(CONTRACT_CONFIG__CREDITOR_ID, new ContractProperty(GOOD_CREDITOR_ID));
        return parameterContract;
    }

    @Override
    protected PaymentFormContext generatePaymentFormContext() {
        return createDefaultPaymentFormContext();
    }


    //todo find a better way to get a prompt
    public static void main(String[] args) {
        PaymentServiceImpl paymentServiceMain = new PaymentServiceImpl();
        //step 2 Create a mandate
        PaymentRequest paymentRequestStep2 = createDefaultPaymentRequestStep2();
        PaymentResponseFormUpdated paymentResponseStep2 = (PaymentResponseFormUpdated) paymentServiceMain.paymentRequest(paymentRequestStep2);

        Map<String, String> requestContextMain = paymentResponseStep2.getRequestContext().getRequestData();
//        PaymentFormConfigurationResponse formConfigurationResponse = paymentResponseStep2.getPaymentFormConfigurationResponse();

        //TODO REQUEST WITH PAYMENT RESPONSE STEP2
        //Saisir OTP
        Scanner keyboardUser = new Scanner(System.in);
        System.out.println("Entrez votre OTP : ");
        String otp = keyboardUser.nextLine();
        keyboardUser.close();
        // Initialize a fake transaction request to check the validity of the contract parameters
        System.out.print("You entered : ");
        System.out.println(otp);
//        Get OTP from a prompt ?
        //Step 3 confirm with  OTP
        PaymentRequest paymentRequestStep3 = createCustomPaymentRequestStep3(requestContextMain, otp);
        //TODO FIND WHAT IS BUYER PAYMENT ID
//        PaymentResponse paymentResponseStep3 = paymentServiceMain.paymentRequest(paymentRequestStep3);
        PaymentResponseSuccess paymentResponseStep3 = (PaymentResponseSuccess) paymentServiceMain.paymentRequest(paymentRequestStep3);

        System.out.println(paymentResponseStep3);
        Assert.assertNotNull(paymentResponseStep3.getPartnerTransactionId());
        Assert.assertNotNull(paymentResponseStep3.getTransactionAdditionalData());

        //Next step get mandate

    }


    @Test
    public void fullPaymentTest() {
        request = createDefaultPaymentRequest();
        transactionID = request.getTransactionId();

        this.fullRedirectionPayment(request, paymentService, paymentWithRedirectionService);
    }

    @Override
    protected String payOnPartnerWebsite(String step) {

        //step 2 Create a mandate
        PaymentRequest paymentRequestStep2 = createDefaultPaymentRequestStep2();
        PaymentResponseFormUpdated paymentResponseStep2 = (PaymentResponseFormUpdated) paymentService.paymentRequest(paymentRequestStep2);

        requestContext = paymentResponseStep2.getRequestContext().getRequestData();
        formConfigutationResponse = paymentResponseStep2.getPaymentFormConfigurationResponse();
        step = paymentResponseStep2.getRequestContext().getRequestData().get(CONTEXT_DATA__STEP);
        //TODO REQUEST WITH PAYMENT RESPONSE STEP2 doesn't work on Unit test
        //Saisir OTP
        Scanner keyboardUser = new Scanner(System.in);
        System.out.println("Entrez votre OTP : ");
        String otp = keyboardUser.nextLine();
        keyboardUser.close();
        // Initialize a fake transaction request to check the validity of the contract parameters
        System.out.print("You entered : ");
        System.out.println(otp);
        //Get OTP from a prompt ?
        //Step 3 confirm with  OTP
        //Create a third payment request with data from Config
        PaymentRequest paymentRequestStep3 = createCustomPaymentRequestStep3(requestContext, otp);
        PaymentResponse paymentResponseStep3 = paymentService.paymentRequest(paymentRequestStep3);
//            PaymentResponseFormUpdated paymentResponseStep3 = (PaymentResponseFormUpdated) paymentService.paymentRequest(paymentRequestStep3);
        Assert.assertNotNull(paymentRequestStep3);

        return transactionID;
    }

    @Override
    public PaymentRequest createDefaultPaymentRequest() {
        return TestUtils.createCompletePaymentBuilder().build();

    }


    @Override
    protected String cancelOnPartnerWebsite(String s) {
        return null;
    }


    @Override
    public void fullRedirectionPayment(PaymentRequest paymentRequest, PaymentService paymentService, PaymentWithRedirectionService paymentWithRedirectionService) {
        PaymentResponse paymentResponseFromPaymentRequest = paymentService.paymentRequest(paymentRequest);
//        this.checkPaymentResponseIsNotFailure(paymentResponseFromPaymentRequest);
        PaymentResponseFormUpdated paymentResponseRedirect = (PaymentResponseFormUpdated) paymentResponseFromPaymentRequest;
        String step = paymentResponseRedirect.getRequestContext().getRequestData().get(CONTEXT_DATA__STEP);

        //    String redirectionUrl = this.payOnPartnerWebsite(partnerUrl);
        //    String redirectionUrl = this.payOnPartnerWebsite(step);
        //Return transactionID or response ??
        //TODO return an URL to OTP form and check step is different than previous step
        String linkOTP = this.payOnPartnerWebsite(step);
        Assertions.assertNotNull(linkOTP);
        String nextStep = requestContext.get(CONTEXT_DATA__STEP);

        Assertions.assertNotNull(linkOTP);
        //Assert we moving to the next step
        Assert.assertNotEquals(nextStep, step);
        //Assert we moving to  step sign OTP
        Assert.assertEquals( CONTEXT_DATA__STEP_OTP, nextStep);

        //Finalize here ?? or confirm





/*        PaymentResponse paymentResponseFromFinalize = this.handlePartnerResponse(paymentWithRedirectionService, paymentRequest, paymentResponseRedirect);
//        this.checkPaymentResponseIsNotFailure(paymentResponseFromFinalize);
//        this.checkPaymentResponseIsRightClass("redirectionPaymentRequest", paymentResponseFromFinalize, PaymentResponseSuccess.class);
        PaymentResponseSuccess paymentResponseSuccess = (PaymentResponseSuccess)paymentResponseFromFinalize;
        Assertions.assertNotNull(paymentResponseSuccess.getTransactionDetails());
        Assertions.assertEquals(partnerTransactionId, paymentResponseSuccess.getPartnerTransactionId());
        */
    }

//    private PaymentResponse handlePartnerResponse(PaymentWithRedirectionService paymentWithRedirectionService, PaymentRequest paymentRequest, PaymentResponseRedirect paymentResponseRedirect) {
//        ContractConfiguration contractConfiguration = new ContractConfiguration("", this.generateParameterContract());
//        Environment environment = new Environment("http://google.com/", "https://succesurl.com/", "http://localhost/cancelurl.com/", true);
//        RedirectionPaymentRequest redirectionPaymentRequest = (RedirectionPaymentRequest) RedirectionPaymentRequest.builder().withContractConfiguration(contractConfiguration).withPaymentFormContext(this.generatePaymentFormContext()).withEnvironment(environment).withTransactionId(paymentRequest.getTransactionId()).withRequestContext(paymentResponseRedirect.getRequestContext()).withAmount(paymentRequest.getAmount()).build();
//        return paymentWithRedirectionService.finalizeRedirectionPayment(redirectionPaymentRequest);
//    }


}