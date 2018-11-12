package com.payline.payment.docapost.utils;

import com.payline.payment.docapost.utils.http.StringResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ActionRequestResponseTest {

    StringResponse responseMocked;


    @Before
    public void setup() {
        responseMocked = new StringResponse();
    }

    @Test
    public void checkResponseTestOK() {
        responseMocked.setCode(200);
        responseMocked.setMessage("OK");
        responseMocked.setContent("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><sepalia><exception code=\"INVALID_IBAN\">INVALID_IBAN: iban[] has an invalide length</exception></sepalia>");

        ActionRequestResponse actionResponseOK = ActionRequestResponse.checkResponse(responseMocked);
        Assert.assertEquals(ActionRequestResponse.OK_200, actionResponseOK);
    }

    @Test
    public void checkResponseTestKO() {
        responseMocked.setCode(404);
        responseMocked.setMessage("Bad Request");
        responseMocked.setContent("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><sepalia><exception code=\"INVALID_IBAN\">INVALID_IBAN: iban[] has an invalide length</exception></sepalia>");

        ActionRequestResponse actionResponseKO = ActionRequestResponse.checkResponse(responseMocked);
        Assert.assertEquals(ActionRequestResponse.OTHER_CODE, actionResponseKO);
    }

    @Test
    public void checkResponseTestDefault() {
        ActionRequestResponse actionResponseDefault = ActionRequestResponse.checkResponse(null);
        Assert.assertEquals(ActionRequestResponse.DEFAULT, actionResponseDefault);

    }

}
