package com.payline.payment.docapost.bean.rest.request;

import com.payline.payment.docapost.bean.rest.request.mandate.*;
import com.payline.payment.docapost.bean.rest.request.signature.InitiateSignatureRequest;
import com.payline.payment.docapost.bean.rest.request.signature.SendOtpRequest;
import com.payline.payment.docapost.bean.rest.request.signature.SetCodeRequest;
import com.payline.payment.docapost.bean.rest.request.signature.TerminateSignatureRequest;
import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.utils.DocapostLocalParam;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.refund.request.RefundRequest;
import com.payline.pmapi.bean.reset.request.ResetRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.payline.payment.docapost.TestUtils.*;

public class RequestBuilderFactoryTest {

    private PaymentRequest request;
    private String telephone;


    @Before
    public void setup() {

        request = createDefaultPaymentRequestStep2(PHONE_NUMBER_TEST);

    }

    /*
        TEST create mandate request
     */
    @Test
    public void testBuildMandateCreateRequest() throws InvalidRequestException {

        MandateCreateRequest mandateCreateRequest = RequestBuilderFactory.buildMandateCreateRequest(request);
        //check request is well built
        Assert.assertNotNull(mandateCreateRequest);
        Assert.assertTrue(true);
        Assert.assertNotNull(mandateCreateRequest.getCreditorId());
        Assert.assertNotNull(mandateCreateRequest.getRum());

        //check debtor has an iban and phone number is well built
        Assert.assertNotNull(mandateCreateRequest.getDebtor());
        Assert.assertNotNull(mandateCreateRequest.getDebtor().getFirstName());
        Assert.assertNotNull(mandateCreateRequest.getDebtor().getIban());
        Assert.assertNotNull(mandateCreateRequest.getDebtor().getPhoneNumber());
    }

    /*
        TEST create SDD order request
     */
    @Test
    public void testBuildSddOrderCreateRequest() throws InvalidRequestException {

        SddOrderCreateRequest mandateCreateRequest = RequestBuilderFactory.buildSddOrderCreateRequest(request);
        //check request is well built
        Assert.assertNotNull(mandateCreateRequest);
        //check debtor has an iban and phone number is well built
        Assert.assertNotNull(mandateCreateRequest.getCreditorId());
        Assert.assertNotNull(mandateCreateRequest.getRum());
        Assert.assertNotNull(mandateCreateRequest.getAmount());

    }

    /*
        TEST cancel request
     */
    @Test
    public void testBuildSddOrderCancelRequest() throws InvalidRequestException {

        ResetRequest resetRequest = createResetRequest();
        SddOrderCancelRequest sddResetRequest = RequestBuilderFactory.buildSddOrderCancelRequest(resetRequest);

        Assert.assertNotNull(sddResetRequest);
        Assert.assertNotNull(sddResetRequest.getCreditorId());
        Assert.assertNotNull(sddResetRequest.getRum());
        Assert.assertNotNull(sddResetRequest.getE2eId());
        Assert.assertEquals(resetRequest.getPartnerTransactionId(), sddResetRequest.getE2eId());


    }

    /*
        TEST refund request
     */
    @Test
    public void testBuildSctOrderCreateRequest() throws InvalidRequestException {
        RefundRequest refundRequest = createRefundRequest();
        SctOrderCreateRequest sctOrderRequest = RequestBuilderFactory.buildSctOrderCreateRequest(refundRequest);

        Assert.assertNotNull(sctOrderRequest);
        Assert.assertNotNull(sctOrderRequest.getCreditorId());
        Assert.assertNotNull(sctOrderRequest.getRum());
        Assert.assertNotNull(sctOrderRequest.getE2eId());
        Assert.assertEquals(refundRequest.getPartnerTransactionId(), sctOrderRequest.getE2eId());
        Assert.assertEquals(refundRequest.getAmount().getAmountInSmallestUnit().floatValue(), sctOrderRequest.getAmount(), 0.01);

    }

    @Test
    public void testBuildSctOrderCancelRequest() throws InvalidRequestException {
        ResetRequest resetRequest = createResetRequest();
        SctOrderCancelRequest sctOrderRequest = RequestBuilderFactory.buildSctOrderCancelRequest(resetRequest);

        Assert.assertNotNull(sctOrderRequest);
        Assert.assertNotNull(sctOrderRequest.getCreditorId());
        Assert.assertNotNull(sctOrderRequest.getE2eId());
        Assert.assertEquals(resetRequest.getPartnerTransactionId(), sctOrderRequest.getE2eId());

    }

    @Test
    public void testBuildInitiateSignatureRequest() throws InvalidRequestException {
        request = createDefaultPaymentRequestStep2(telephone);
        DocapostLocalParam docapostLocalParam = DocapostLocalParam.getInstance();
        docapostLocalParam.restoreFromPaylineRequest(request);

        InitiateSignatureRequest initiateSignatureRequest = RequestBuilderFactory.buildInitiateSignatureRequest(request, docapostLocalParam);
        Assert.assertNotNull(initiateSignatureRequest);
        Assert.assertNotNull(initiateSignatureRequest.getCreditorId());
        Assert.assertEquals(docapostLocalParam.getMandateRum(), initiateSignatureRequest.getMandateRum());

    }

    @Test
    public void testBuildSendOtpRequest() throws InvalidRequestException {
        request = createDefaultPaymentRequestStep2(telephone);
        DocapostLocalParam docapostLocalParam = DocapostLocalParam.getInstance();
        docapostLocalParam.restoreFromPaylineRequest(request);

        SendOtpRequest sendOtpRequest = RequestBuilderFactory.buildSendOtpRequest(request, docapostLocalParam);
        Assert.assertNotNull(sendOtpRequest);
        Assert.assertNotNull(sendOtpRequest.getCreditorId());
        Assert.assertEquals(docapostLocalParam.getMandateRum(), sendOtpRequest.getMandateRum());
    }

    @Test
    public void testBuildSetCodeRequest() throws InvalidRequestException {
        request = createDefaultPaymentRequestStep2(telephone);
        SetCodeRequest setCodeRequest = RequestBuilderFactory.buildSetCodeRequest(request);
        Assert.assertNotNull(setCodeRequest);
        Assert.assertNotNull(setCodeRequest.getCreditorId());
        Assert.assertNotNull(setCodeRequest.getOtp());
        Assert.assertNotNull(setCodeRequest.getMandateRum());
        Assert.assertNotNull(setCodeRequest.getTransactionId());
        Assert.assertEquals(request.getTransactionId(), setCodeRequest.getTransactionId());

    }

    @Test
    public void testBuildTerminateSignatureRequest() throws InvalidRequestException {
        request = createDefaultPaymentRequestStep2(telephone);
        DocapostLocalParam docapostLocalParam = DocapostLocalParam.getInstance();
        docapostLocalParam.restoreFromPaylineRequest(request);
        docapostLocalParam.setSignatureSuccess(false);


        TerminateSignatureRequest terminateSignatureRequest = RequestBuilderFactory.buildTerminateSignatureRequest(request, docapostLocalParam);
        Assert.assertNotNull(terminateSignatureRequest);
        Assert.assertNotNull(terminateSignatureRequest.getCreditorId());
        Assert.assertNotNull(terminateSignatureRequest.getMandateRum());
        Assert.assertNotNull(terminateSignatureRequest.getTransactionId());
        Assert.assertNotNull(terminateSignatureRequest.getSuccess());
        Assert.assertEquals(request.getTransactionId(), terminateSignatureRequest.getTransactionId());

    }


}
