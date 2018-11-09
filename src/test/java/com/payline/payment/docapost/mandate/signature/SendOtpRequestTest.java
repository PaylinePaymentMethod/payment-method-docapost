package com.payline.payment.docapost.mandate.signature;

import com.payline.payment.docapost.bean.rest.request.signature.SendOtpRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static com.payline.payment.docapost.utils.DocapostConstants.SIGNATURE_WS_REQUEST_FIELD_CREDITOR_ID;
import static com.payline.payment.docapost.utils.DocapostConstants.SIGNATURE_WS_REQUEST_FIELD_MANDATE_RUM;
import static com.payline.payment.docapost.utils.DocapostConstants.SIGNATURE_WS_REQUEST_FIELD_TRANSACTION_ID;

public class SendOtpRequestTest {

   private  SendOtpRequest sendOtpRequest;

    @Before
    public void setup(){

         sendOtpRequest = new SendOtpRequest("creditorId",
                "mandateRum",
                "transactionId");
    }
    @Test
    public void testSendOtpRequest(){

        String result = sendOtpRequest.toString();
        Assert.assertTrue(result.contains("mandateRum"));
        Assert.assertTrue(result.contains("transactionId"));
        Assert.assertTrue(result.contains("creditorId"));

    }

    @Test
    public void testGetRequestBodyMap(){

        Map<String,String> bodyMap = sendOtpRequest.getRequestBodyMap();
        Assert.assertEquals("creditorId",bodyMap.get( SIGNATURE_WS_REQUEST_FIELD_CREDITOR_ID) );
        Assert.assertEquals("mandateRum", bodyMap.get(SIGNATURE_WS_REQUEST_FIELD_MANDATE_RUM));
        Assert.assertEquals("transactionId", bodyMap.get(SIGNATURE_WS_REQUEST_FIELD_TRANSACTION_ID) );
    }

}
