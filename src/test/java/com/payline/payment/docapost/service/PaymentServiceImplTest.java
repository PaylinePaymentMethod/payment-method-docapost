package com.payline.payment.docapost.service;

import com.payline.payment.docapost.service.impl.PaymentServiceImpl;
import com.payline.payment.docapost.utils.http.DocapostHttpClient;
import com.payline.payment.docapost.utils.http.StringResponse;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFormUpdated;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static com.payline.payment.docapost.TestUtils.*;
import static com.payline.payment.docapost.utils.DocapostConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceImplTest {


    @InjectMocks
    private PaymentServiceImpl paymentService = new PaymentServiceImpl();

    @Mock
    private DocapostHttpClient httpClient;

    @Test
    public void testPaymentServiceImpl() {
        PaymentServiceImpl paymentService = new PaymentServiceImpl();
        Assert.assertNotNull(paymentService);


    }

    @Test
    public void testPaymentRequestStep1() {
        PaymentServiceImpl paymentService = new PaymentServiceImpl();
        Assert.assertNotNull(paymentService);

        PaymentRequest paymentRequestStep1 = createDefaultPaymentRequest();
        //assert step is null, STEP 0
        String step = paymentRequestStep1.getRequestContext().getRequestData().get(CONTEXT_DATA_STEP);
        Assert.assertNotNull(paymentRequestStep1);
        Assert.assertNull(step);

        PaymentResponseFormUpdated paymentResponse = (PaymentResponseFormUpdated) paymentService.paymentRequest(paymentRequestStep1);
        //Check we are now on the next step : IBAN_PHONE
        Assert.assertNotNull(paymentResponse);
        Assert.assertNotNull(paymentResponse.getRequestContext().getRequestData().get(CONTEXT_DATA_STEP));
        Assert.assertEquals(CONTEXT_DATA_STEP_IBAN_PHONE, paymentResponse.getRequestContext().getRequestData().get(CONTEXT_DATA_STEP));

    }

    @Test
    public void testPaymentRequestStepIbanPhone() throws Exception {


        StringResponse stringResponse1 = new StringResponse();
        stringResponse1.setCode(200);
        stringResponse1.setMessage("OK");
        stringResponse1.setContent("{\"transactionId\":\"2c969e2c66b0cd3201670e46b10a4d39\"}");

        StringResponse stringResponse2 = new StringResponse();
        stringResponse2.setCode(200);
        stringResponse2.setMessage("OK");
        stringResponse2.setContent("{\"signatureID\":\"76797019\"}");

        StringResponse stringResponseXml = new StringResponse();
        stringResponseXml.setCode(200);
        stringResponseXml.setMessage("OK");
        stringResponseXml.setContent("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<WSMandateDTO>" +
                "<creditorId>MARCHAND1</creditorId>" +
                "<creditorIcs>FR28AAA000000</creditorIcs>" +
                "<rum>PAYLINE-GL6M8KY98H</rum>" +
                "<recurrent>false</recurrent>" +
                "<status>Compliant</status>" +
                "<debtor><lastName>BAR</lastName><bic>NORDFRPPXXX</bic><iban>FR7630076020821234567890186</iban><firstName>FOO</firstName><street>12 RUE PARADIS</street><postalCode>75001</postalCode><town>PARIS</town><phoneNumber>06060606</phoneNumber><countryCode>FR</countryCode></debtor>" +
                "<mode>READ</mode><flowName>STANDARD</flowName><language>fr</language>" +
                "</WSMandateDTO>");


        PaymentRequest paymentRequestStep2 = createDefaultPaymentRequestStep2(PHONE_NUMBER_TEST);
        //assert step is null, STEP 0
        String step = paymentRequestStep2.getRequestContext().getRequestData().get(CONTEXT_DATA_STEP);
        Assert.assertNotNull(paymentRequestStep2);

        //Assert we are now on the next step : IBAN_PHONE
        Assert.assertEquals(CONTEXT_DATA_STEP_IBAN_PHONE, step);


        Mockito.when(httpClient.doPost(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(stringResponseXml);
        Mockito.when(httpClient.doPost(anyString(), anyString(), anyString(), any(Map.class), anyString())).thenReturn(stringResponse1, stringResponse2);

        PaymentResponse paymentResponse = paymentService.paymentRequest(paymentRequestStep2);
        //Check we have a PaymentResponseUpdated
        Assert.assertTrue(paymentResponse instanceof PaymentResponseFormUpdated);

        //cast to PaymentResponseUpdated
        PaymentResponseFormUpdated paymentResponseFormUpdated = (PaymentResponseFormUpdated) paymentResponse;

        //Check we move on the next step : OTP
        Assert.assertEquals(CONTEXT_DATA_STEP_OTP, paymentResponseFormUpdated.getRequestContext().getRequestData().get(CONTEXT_DATA_STEP));

    }


    @Test
    public void testPaymentRequestStepOTP() throws IOException, URISyntaxException {
//        PaymentServiceImpl paymentServiceImpl = new PaymentServiceImpl();

        Map<String, String> requestContextMap = new HashMap<>();
        requestContextMap.put(CONTEXT_DATA_STEP, CONTEXT_DATA_STEP_OTP);
        requestContextMap.put(CONTEXT_DATA_MANDATE_RUM, "testRum");
        requestContextMap.put(CONTEXT_DATA_TRANSACTION_ID, "123456789");
        requestContextMap.put(CONTEXT_DATA_SIGNATURE_ID, "aSignature");


        StringResponse stringResponse1 = new StringResponse();
        stringResponse1.setCode(200);
        stringResponse1.setMessage("OK");
        stringResponse1.setContent("{\"transactionId\":\"2c969e2c66b0cd3201670e46b10a4d39\"}");

        StringResponse stringResponse2 = new StringResponse();
        stringResponse2.setCode(200);
        stringResponse2.setMessage("OK");
        stringResponse2.setContent("{\"signatureID\":\"76797019\"}");

        StringResponse swddOrdercreatedMocked = new StringResponse();
        swddOrdercreatedMocked.setCode(200);
        swddOrdercreatedMocked.setMessage("OK");
        swddOrdercreatedMocked.setContent("<WSMandateDTO>\n" +
                "   <label>A simple order</label>\n" +
                "   <dueDate>2018-12-27T00:00:00+01:00</dueDate>\n" +
                "   <e2eId>1112140545</e2eId>\n" +
                "   <remitDate>2018-12-05T00:00:00+01:00</remitDate>\n" +
                "   <sequence>Recurrent</sequence>\n" +
                "   <identifier>QIVP1F0GE6HE8KZFURENGNFWJ4N55BRD</identifier>\n" +
                "   <rum>PAYLINE-3KTJVIKM5J</rum>\n" +
                "   <creditorId>MARCHAND1</creditorId>\n" +
                "   <status>Created</status>\n" +
                "   <amount>100.0</amount>\n" +
                "</WSMandateDTO>");

        Mockito.when(httpClient.doPost(anyString(), anyString(), anyString(), any(Map.class), anyString())).thenReturn(stringResponse1, stringResponse2);
        Mockito.when(httpClient.doPost(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(swddOrdercreatedMocked);

        PaymentRequest paymentRequestStep2 = createDefaultPaymentRequestStep2(PHONE_NUMBER_TEST);
        PaymentResponseFormUpdated paymentResponseStep2 = (PaymentResponseFormUpdated) paymentService.paymentRequest(paymentRequestStep2);


        Map<String, String> requestContext = paymentResponseStep2.getRequestContext().getRequestData();
        PaymentRequest paymentRequestStepOTP = createCustomPaymentRequestStep3(requestContext, CONTEXT_DATA_STEP_OTP, PHONE_NUMBER_TEST);

        Assert.assertNotNull(paymentRequestStepOTP);
        Assert.assertNotNull(paymentRequestStepOTP.getPaymentFormContext().getPaymentFormParameter().get("formDebtorPhone"));
        Assert.assertNotNull(paymentRequestStepOTP.getPaymentFormContext().getPaymentFormParameter().get("formOtp"));
        Assert.assertNotNull(paymentRequestStepOTP.getRequestContext().getRequestData().get("mandateRum"));
        Assert.assertNotNull(paymentRequestStepOTP.getRequestContext().getRequestData().get("signatureId"));
        Assert.assertNotNull(paymentRequestStepOTP.getRequestContext().getRequestData().get("step"));
        Assert.assertNotNull(paymentRequestStepOTP.getRequestContext().getRequestData().get("transactionId"));

    }


}
