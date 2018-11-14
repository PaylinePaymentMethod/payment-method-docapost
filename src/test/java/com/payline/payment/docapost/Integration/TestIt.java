package com.payline.payment.docapost.Integration;

import com.payline.payment.docapost.TestUtils;
import com.payline.payment.docapost.service.impl.PaymentServiceImpl;
import com.payline.payment.docapost.service.impl.PaymentWithRedirectionServiceImpl;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.payment.PaymentFormContext;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFormUpdated;
import com.payline.pmapi.integration.AbstractPaymentIntegration;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static com.payline.payment.docapost.TestUtils.*;
import static com.payline.payment.docapost.utils.DocapostConstants.CONTRACT_CONFIG_CREDITOR_ID;


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

        //step 2 Create a mandate
        //Saisir OTP
        PaymentRequest paymentRequestStep2 = createDefaultPaymentRequestStep2(phoneNumber);
        PaymentResponseFormUpdated paymentResponseStep2 = (PaymentResponseFormUpdated) testIt.paymentService.paymentRequest(paymentRequestStep2);

        Map<String, String> requestContextMain = paymentResponseStep2.getRequestContext().getRequestData();
        // Create a Payment request from payment request step 2 result

        //TODO REQUEST WITH PAYMENT RESPONSE STEP2
        Scanner keyboardOTP = new Scanner(System.in);


        System.out.println("Enter your  OTP : ");
        String otp = keyboardOTP.nextLine();
        System.out.print("You entered : ");
        System.out.println(otp);
        keyboardOTP.close();
        //Step 3 confirm  transaction with the OTP inputed
        // !!! PaymentSuccess renvoyé
        PaymentRequest paymentRequestStep3 = createCustomPaymentRequestStep3(requestContextMain, otp, phoneNumber);

        testIt.fullRedirectionPayment(paymentRequestStep3, testIt.paymentService, testIt.paymentWithRedirectionService);

    }

    @Test
    public void fullPaymentTest() {
////        request = createDefaultPaymentRequest();
//        request = createDefaultPaymentRequestStep2(PHONE_NUMBER_TEST);
//        this.fullRedirectionPayment(request, paymentService, paymentWithRedirectionService);
    }

    @Override
    protected String payOnPartnerWebsite(String step) {
        //we are not redirected to Docapost website
        return null;
//            return SUCCESS_URL;
    }

    @Override
    public PaymentRequest createDefaultPaymentRequest() {
        return TestUtils.createCompletePaymentBuilder().build();

    }


    @Override
    protected String cancelOnPartnerWebsite(String s) {
        return null;
    }


}