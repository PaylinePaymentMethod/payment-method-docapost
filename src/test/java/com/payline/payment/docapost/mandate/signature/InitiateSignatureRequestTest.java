package com.payline.payment.docapost.mandate.signature;

import com.payline.payment.docapost.bean.rest.request.signature.InitiateSignatureRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static com.payline.payment.docapost.utils.DocapostConstants.SIGNATURE_WS_REQUEST_FIELD_CREDITOR_ID;
import static com.payline.payment.docapost.utils.DocapostConstants.SIGNATURE_WS_REQUEST_FIELD_MANDATE_RUM;

public class InitiateSignatureRequestTest {

    InitiateSignatureRequest initiateSignatureRequest;

    @Before
    public void setup(){
        initiateSignatureRequest = new InitiateSignatureRequest("MycreditorId",
                "MymandateRum");
    }

    @Test
    public void testToString(){

        String result = initiateSignatureRequest.toString();
        Assert.assertTrue(result.contains("MymandateRum"));
        Assert.assertTrue(result.contains("MycreditorId"));
    }

    @Test
    public void testGetRequestBodyMap(){

        Map<String,String> bodyMap = initiateSignatureRequest.getRequestBodyMap();
        Assert.assertEquals("MycreditorId",bodyMap.get(SIGNATURE_WS_REQUEST_FIELD_CREDITOR_ID) );
        Assert.assertEquals("MymandateRum",bodyMap.get(SIGNATURE_WS_REQUEST_FIELD_MANDATE_RUM));
    }
}
