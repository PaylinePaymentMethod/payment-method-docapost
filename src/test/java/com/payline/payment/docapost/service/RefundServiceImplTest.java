package com.payline.payment.docapost.service;

import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.service.impl.RefundServiceImpl;
import com.payline.payment.docapost.utils.http.StringResponse;
import com.payline.pmapi.bean.refund.request.RefundRequest;
import com.payline.pmapi.bean.refund.response.RefundResponse;
import com.payline.pmapi.bean.refund.response.impl.RefundResponseFailure;
import com.payline.pmapi.bean.refund.response.impl.RefundResponseSuccess;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.payline.payment.docapost.TestUtils.createRefundRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class RefundServiceImplTest {

    @Mock
    private RefundServiceImpl service;

    @Mock
    private RefundRequest refundRequest;

    private RefundRequest refundRequestNull;


    @Before
    public void setup() {
        this.service = new RefundServiceImpl();
//        this.service = Mockito.mock(RefundServiceImpl.class);
    }


    @Test(expected = InvalidRequestException.class)
    public void refundRequestTestNull() throws URISyntaxException, IOException, InvalidRequestException {
        StringResponse response = service.createSendRequest(refundRequestNull);
        Assert.assertNull(response);
    }


    @Test
    public void refundRequestTest() {

        RefundResponseSuccess responseMocked = RefundResponseSuccess
                .RefundResponseSuccessBuilder
                .aRefundResponseSuccess()
                .withStatusCode("CREATED")
                .withPartnerTransactionId("1108102438")
                .build();
            //TODO mock http call or call to processResponse
    //    doReturn(responseMocked).when(service).processResponse(any(StringResponse.class));
        doReturn(responseMocked).when(service).processResponse(any(StringResponse.class));


        refundRequest = createRefundRequest();
        RefundResponse response = service.refundRequest(refundRequest);
        Assert.assertTrue(response instanceof RefundResponseFailure);
    }

    @Test
    public void createSendRequestTest() throws URISyntaxException, IOException, InvalidRequestException {
        //TODO mock appels doPost
        refundRequest = createRefundRequest();
        StringResponse response = service.createSendRequest(refundRequest);
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

        RefundResponse refundResponseFailure = service.processResponse(responseMocked);
        Assert.assertNotNull(refundResponseFailure);
        Assert.assertTrue(refundResponseFailure instanceof RefundResponseFailure);

        // case null
        responseMocked.setCode(200);
        responseMocked.setMessage("");
        responseMocked.setContent("");

        RefundResponse refundResponseNull = service.processResponse(responseMocked);
        Assert.assertNotNull(refundResponseNull);
        Assert.assertTrue(refundResponseNull instanceof RefundResponseFailure);
    }

    @Test
    public void processResponseTestOK() {
        //Case responseSuccess
        StringResponse responseMocked = new StringResponse();
        responseMocked.setCode(200);
        responseMocked.setMessage("");
        responseMocked.setContent("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><WSCTOrderDTO><label>A simple order</label><dueDate>2018-11-09T00:00:00+01:00</dueDate><e2eId>1108102438</e2eId><remitDate>2018-11-08T00:00:00+01:00</remitDate><rum>PAYLINE-HXGELVOTHM</rum><creditorId>MARCHAND1</creditorId><status>CREATED</status><amount>100.0</amount><receiverName>NICOLAS</receiverName><receiverIban>FR7630076020821234567890186</receiverIban></WSCTOrderDTO>");

        RefundResponse refundResponseSuccess = service.processResponse(responseMocked);
        Assert.assertNotNull(refundResponseSuccess);
        Assert.assertTrue(refundResponseSuccess instanceof RefundResponseSuccess);
        Assert.assertEquals("1108102438", refundResponseSuccess.getPartnerTransactionId());

    }

    @Test
    public void canMultipleTest() {
        Assert.assertFalse(service.canMultiple());
    }

    @Test
    public void canPartialTest() {
        Assert.assertFalse(service.canPartial());
    }


}
