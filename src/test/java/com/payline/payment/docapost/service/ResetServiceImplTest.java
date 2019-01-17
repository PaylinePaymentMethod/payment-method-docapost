package com.payline.payment.docapost.service;

import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.service.impl.ResetServiceImpl;
import com.payline.payment.docapost.utils.http.DocapostHttpClient;
import com.payline.payment.docapost.utils.http.StringResponse;
import com.payline.payment.docapost.utils.type.WSRequestResultEnum;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.reset.request.ResetRequest;
import com.payline.pmapi.bean.reset.response.ResetResponse;
import com.payline.pmapi.bean.reset.response.impl.ResetResponseFailure;
import com.payline.pmapi.bean.reset.response.impl.ResetResponseSuccess;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.payline.payment.docapost.TestUtils.createResetRequest;

public class ResetServiceImplTest {

    @InjectMocks
    private ResetServiceImpl service;

    @Spy
    private DocapostHttpClient httpClient;

    @Before
    public void setup() {
        this.service = new ResetServiceImpl();
        MockitoAnnotations.initMocks(this);

    }


    @Test
    public void resetRequestTest() {

    }

    @Test
    public void createSendRequestTest() throws URISyntaxException, IOException, InvalidRequestException {

        StringResponse stringResponseMockded = new StringResponse();
        stringResponseMockded.setContent("content");
        stringResponseMockded.setMessage("Message");
        stringResponseMockded.setCode(200);

        Mockito.doReturn(stringResponseMockded).when(httpClient).doGet(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString());


        ResetRequest resetRequest = createResetRequest();
        StringResponse response = service.createSendRequest(resetRequest);
        String stringResponse = response.toString();

        Assert.assertTrue(stringResponse.contains("code"));
        Assert.assertTrue(stringResponse.contains("message"));
    }

    @Test
    public void processResponseTestKO_INVALID_STATUS_MODIFICATION() {

        //Case responseFailure processResponseTestKO_INVALID_STATUS_MODIFICATION
        StringResponse responseMocked = new StringResponse();
        responseMocked.setCode(400);
        responseMocked.setMessage("Bad Request");
        responseMocked.setContent("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><sepalia><exception code=\"INVALID_STATUS_MODIFICATION\">INVALID_STATUS_MODIFICATION: occurs if sdd order can't be cancelled</exception></sepalia>");

        ResetResponse resetResponse = service.processResponseFailure(responseMocked);
        Assert.assertNotNull(resetResponse);
        Assert.assertTrue(resetResponse instanceof ResetResponseFailure);

        ResetResponseFailure resetResponseFailure = (ResetResponseFailure) resetResponse;
        Assert.assertEquals(WSRequestResultEnum.INVALID_STATUS_MODIFICATION.getDocapostErrorCode(), resetResponseFailure.getErrorCode());
        Assert.assertEquals(WSRequestResultEnum.INVALID_STATUS_MODIFICATION.getPaylineFailureCause(), resetResponseFailure.getFailureCause());

    }

    @Test
    public void processResponseTestKO_MANDATE_NOT_VALID() {

        //Case responseFailure MANDATE_NOT_VALID
        StringResponse responseMocked = new StringResponse();
        responseMocked.setCode(400);
        responseMocked.setMessage("Bad Request");
        responseMocked.setContent("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><sepalia><exception code=\"MANDATE_NOT_VALID\">MANDATE_NOT_VALID: occurs if mandate is already revoked</exception></sepalia>");

        ResetResponse resetResponse = service.processResponseFailure(responseMocked);
        Assert.assertNotNull(resetResponse);
        Assert.assertTrue(resetResponse instanceof ResetResponseFailure);

        ResetResponseFailure resetResponseFailure = (ResetResponseFailure) resetResponse;
        Assert.assertEquals(WSRequestResultEnum.MANDATE_NOT_VALID.getDocapostErrorCode(), resetResponseFailure.getErrorCode());
        Assert.assertEquals(WSRequestResultEnum.MANDATE_NOT_VALID.getPaylineFailureCause(), resetResponseFailure.getFailureCause());

    }

    @Test
    public void processResponseTestKO_UNAUTHORIZED() {

        //Case responseFailure UNAUTHORIZED
        StringResponse responseMocked = new StringResponse();
        responseMocked.setCode(400);
        responseMocked.setMessage("Bad Request");
        responseMocked.setContent("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><sepalia><exception code=\"UNAUTHORIZED\">UNAUTHORIZED: occurs when the creditor is not found or the user have no access to this creditor</exception></sepalia>");

        ResetResponse resetResponse = service.processResponseFailure(responseMocked);
        Assert.assertNotNull(resetResponse);
        Assert.assertTrue(resetResponse instanceof ResetResponseFailure);

        ResetResponseFailure resetResponseFailure = (ResetResponseFailure) resetResponse;
        Assert.assertEquals(WSRequestResultEnum.UNAUTHORIZED.getDocapostErrorCode(), resetResponseFailure.getErrorCode());
        Assert.assertEquals(WSRequestResultEnum.UNAUTHORIZED.getPaylineFailureCause(), resetResponseFailure.getFailureCause());

    }

    @Test
    public void processResponseTestKO_NOT_FOUND() {

        //Case responseFailure NOT_FOUND
        StringResponse responseMocked = new StringResponse();
        responseMocked.setCode(400);
        responseMocked.setMessage("Bad Request");
        responseMocked.setContent("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><sepalia><exception code=\"NOT_FOUND\">NOT_FOUND: occurs when XXX is not found (sdd order or mandate)</exception></sepalia>");

        ResetResponse resetResponse = service.processResponseFailure(responseMocked);
        Assert.assertNotNull(resetResponse);
        Assert.assertTrue(resetResponse instanceof ResetResponseFailure);

        ResetResponseFailure resetResponseFailure = (ResetResponseFailure) resetResponse;
        Assert.assertEquals(WSRequestResultEnum.NOT_FOUND.getDocapostErrorCode(), resetResponseFailure.getErrorCode());
        Assert.assertEquals(WSRequestResultEnum.NOT_FOUND.getPaylineFailureCause(), resetResponseFailure.getFailureCause());

    }

    @Test
    public void processResponseTestKO_NullResponse() {

        //Case responseFailure null response
        StringResponse responseMocked = new StringResponse();
        responseMocked.setCode(400);
        responseMocked.setMessage("");
        responseMocked.setContent("");

        ResetResponse resetResponse = service.processResponseFailure(responseMocked);
        Assert.assertNotNull(resetResponse);
        Assert.assertTrue(resetResponse instanceof ResetResponseFailure);

        ResetResponseFailure resetResponseFailure = (ResetResponseFailure) resetResponse;
        Assert.assertEquals("XML RESPONSE PARSING FAILED", resetResponseFailure.getErrorCode());
        Assert.assertEquals(FailureCause.INVALID_DATA, resetResponseFailure.getFailureCause());
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

        ResetResponse resetResponseSuccess = service.processResponseSuccess(responseMocked);
        Assert.assertNotNull(resetResponseSuccess);
        Assert.assertTrue(resetResponseSuccess instanceof ResetResponseSuccess);
        Assert.assertEquals("1108102438", resetResponseSuccess.getPartnerTransactionId());
    }

    @Test
    public void canMultipleTest() {

        Assert.assertTrue(service.canMultiple());
    }

    @Test
    public void canPartialTest() {
        Assert.assertTrue(service.canPartial());
    }

    @Test
    public void buildRefundResponseFailure() {
        ResetResponseFailure response = service.buildResetResponseFailure("thisIsAnError", FailureCause.INVALID_DATA);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getErrorCode());
        Assert.assertNotNull(response.getFailureCause());
    }
}
