package com.payline.payment.docapost.mandate.signature;

import com.payline.payment.docapost.bean.rest.request.signature.SetCodeRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static com.payline.payment.docapost.utils.DocapostConstants.*;

public class SetCodeRequestTest {


    private SetCodeRequest setCodeRequestTest;

    @Before
    public void setup(){
         setCodeRequestTest = new SetCodeRequest("MycreditorId",
                "MymandateRum",
                "MytransactionId",
                "MysignatureId",
                "Myotp");
    }

    @Test
    public void testToString(){

    String result = setCodeRequestTest.toString();
        Assert.assertTrue(result.contains("MymandateRum"));
        Assert.assertTrue(result.contains("MytransactionId"));
        Assert.assertTrue(result.contains("MycreditorId"));
        Assert.assertTrue(result.contains("Myotp"));

    }

    @Test
    public void testGetRequestBodyMap(){

        Map<String,String> bodyMap = setCodeRequestTest.getRequestBodyMap();
        Assert.assertEquals("MycreditorId",bodyMap.get(SIGNATURE_WS_REQUEST_FIELD_CREDITOR_ID) );
        Assert.assertEquals("MymandateRum", bodyMap.get(SIGNATURE_WS_REQUEST_FIELD_MANDATE_RUM) );
        Assert.assertEquals("MytransactionId",bodyMap.get(SIGNATURE_WS_REQUEST_FIELD_TRANSACTION_ID));
        Assert.assertEquals("MysignatureId",bodyMap.get(SIGNATURE_WS_REQUEST_FIELD_SIGNATURE_ID) );
        Assert.assertEquals("Myotp",bodyMap.get(SIGNATURE_WS_REQUEST_FIELD_OTP) );

    }



}
