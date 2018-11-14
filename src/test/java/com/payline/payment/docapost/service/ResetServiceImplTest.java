package com.payline.payment.docapost.service;

import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.service.impl.ResetServiceImpl;
import com.payline.payment.docapost.utils.http.StringResponse;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.reset.request.ResetRequest;
import com.payline.pmapi.bean.reset.response.ResetResponse;
import com.payline.pmapi.bean.reset.response.impl.ResetResponseFailure;
import com.payline.pmapi.bean.reset.response.impl.ResetResponseSuccess;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.payline.payment.docapost.TestUtils.createResetRequest;

public class ResetServiceImplTest {

    private ResetServiceImpl service;


    @Before
    public void setup() {
        this.service = new ResetServiceImpl();
    }


    @Test
    public void resetRequestTest() {

    }

    @Test
    public void createSendRequestTest() throws URISyntaxException, IOException, InvalidRequestException {

        ResetRequest resetRequest = createResetRequest();
        StringResponse response = service.createSendRequest(resetRequest);
        String stringResponse = response.toString();

        Assert.assertTrue(stringResponse.contains("code"));
        Assert.assertTrue(stringResponse.contains("message"));
    }

    @Test
    public void processResponseTestKO() {

        //Case responseFailure
        StringResponse responseMocked = new StringResponse();
        responseMocked.setCode(400);
        responseMocked.setMessage("Bad Request");
        responseMocked.setContent("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><sepalia><exception code=\"INVALID_IBAN\">INVALID_IBAN: iban[] has an invalide length</exception></sepalia>");

        ResetResponse resetResponseFailure = service.processResponse(responseMocked);
        Assert.assertNotNull(resetResponseFailure);
        Assert.assertTrue(resetResponseFailure instanceof ResetResponseFailure);

        // case null
        responseMocked.setCode(200);
        responseMocked.setMessage("");
        responseMocked.setContent("<WSCTOrderDTO></WSCTOrderDTO>");

        ResetResponse resetResponseNull = service.processResponse(responseMocked);
        Assert.assertNotNull(resetResponseNull);
        Assert.assertTrue(resetResponseNull instanceof ResetResponseFailure);
    }

    @Test
    public void processResponseTestOK() {
        //Case responseSuccess
        StringResponse responseMocked = new StringResponse();
        responseMocked.setCode(200);
        responseMocked.setMessage("");
        responseMocked.setContent("<WSDDOrderDTO>\n" +
                "   <label>A simple order</label>\n" +
                "   <dueDate>2018-12-27T00:00:00+01:00</dueDate>\n" +
                "   <e2eId>1108102438</e2eId>\n" +
                "   <remitDate>2018-12-05T00:00:00+01:00</remitDate>\n" +
                "   <sequence>Recurrent</sequence>\n" +
                "   <identifier>2I39G6W4VEDXM1DPYXEGERGN1098S04E</identifier>\n" +
                "   <rum>PAYLINE-HXGELVOTHM</rum>\n" +
                "   <creditorId>MARCHAND1</creditorId>\n" +
                "   <status>Cancelled</status>\n" +
                "   <amount>100.0</amount>\n" +
                "</WSDDOrderDTO>");

        ResetResponse resetResponseSuccess = service.processResponse(responseMocked);
        Assert.assertNotNull(resetResponseSuccess);
        Assert.assertTrue(resetResponseSuccess instanceof ResetResponseSuccess);
        Assert.assertEquals("1108102438", resetResponseSuccess.getPartnerTransactionId());
    }

    @Test
    public void canMultipleTest() {

        Assert.assertFalse(service.canMultiple());
    }

    @Test
    public void canPartialTest() {
        Assert.assertFalse(service.canPartial());
    }

    @Test
    public void buildRefundResponseFailure() {
        ResetResponseFailure response = service.buildResetResponseFailure("thisIsAnError", FailureCause.INVALID_DATA);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getErrorCode());
        Assert.assertNotNull(response.getFailureCause());
    }
}
