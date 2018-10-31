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
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.payline.payment.docapost.TestUtils.createDefaultPaymentRequest;
import static com.payline.payment.docapost.TestUtils.createDefaultPaymentRequestStep2;
import static com.payline.payment.docapost.utils.DocapostConstants.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService = new PaymentServiceImpl ();

    @Mock
    private DocapostHttpClient httpClient;

    // TODO

    @Test
    public void test() {
        Assert.assertTrue(true);
    }

//    @Test
//    public void testPaymentRequest_ok(){
//        // TODO
//        Assert.assertTrue( false );
//    }

//TODO tester le format de downloadMandateLink

    @Test
    public void testPaymentServiceImpl(){
        PaymentServiceImpl paymentService = new PaymentServiceImpl();
        //TODO improve this test
        Assert.assertNotNull(paymentService);

    }

    @Test
    public void testPaymentRequestStep1(){
        PaymentServiceImpl paymentService = new PaymentServiceImpl();
        //TODO improve this test
        Assert.assertNotNull(paymentService);

        PaymentRequest paymentRequestStep1 = createDefaultPaymentRequest();
        //assert step is null, STEP 0
        String step = paymentRequestStep1.getRequestContext().getRequestData().get(CONTEXT_DATA__STEP);
        Assert.assertNotNull(paymentRequestStep1);
        Assert.assertNull(step);

        PaymentResponseFormUpdated paymentResponse = (PaymentResponseFormUpdated) paymentService.paymentRequest(paymentRequestStep1);
        //Check we are now on the next step : IBAN_PHONE
        Assert.assertNotNull(paymentResponse);
        Assert.assertNotNull(paymentResponse.getRequestContext().getRequestData().get(CONTEXT_DATA__STEP));
        Assert.assertEquals(CONTEXT_DATA__STEP_IBAN_PHONE,paymentResponse.getRequestContext().getRequestData().get(CONTEXT_DATA__STEP));

    }

    @Test
    public void testPaymentRequestStep2() throws IOException, URISyntaxException {

        //TODO improve this test
        Assert.assertNotNull(paymentService);

        //TODO use valid Credentials and remove the mock ?
        StringResponse defaultResponse =  new StringResponse();
        defaultResponse.setCode(HTTP_OK);
        //Mock de la reponse
        defaultResponse.setContent("<WSMandateDTO><creditorId>MARCHAND1</creditorId>\n" +
                "   <creditorIcs>FR28AAA000000</creditorIcs>\n" +
                "   <rum>PAYLINE-KO4SPCEL98</rum>\n" +
                "   <recurrent>false</recurrent>\n" +
                "   <status>Compliant</status>\n" +
                "   <debtor>\n" +
                "      <lastName>NICOLAS</lastName>\n" +
                "      <bic>NORDFRPPXXX</bic>\n" +
                "      <iban>FR7630076020821234567890186</iban>\n" +
                "      <firstName>MICHNIEWSKI</firstName>\n" +
                "      <street>25 RUE GAMBETTA</street>\n" +
                "      <postalCode>13130</postalCode>\n" +
                "      <town>BERRE L'ETANG</town>\n" +
                "      <phoneNumber>0623737719</phoneNumber>\n" +
                "      <countryCode>FR</countryCode>\n" +
                "      <complement/>\n" +
                "      <complement2/>\n" +
                "   </debtor>\n" +
                "   <mode>READ</mode>\n" +
                "   <flowName>STANDARD</flowName>\n" +
                "   <language>fr</language>\n</WSMandateDTO>");



//        when((httpClient).doPost(anyString(), anyString(),anyString(),anyString(),anyString())).thenReturn(defaultResponse);
        PaymentRequest paymentRequestStep2 = createDefaultPaymentRequestStep2();
        //assert step is null, STEP 0
        String step = paymentRequestStep2.getRequestContext().getRequestData().get(CONTEXT_DATA__STEP);
        Assert.assertNotNull(paymentRequestStep2);
        Assert.assertEquals(CONTEXT_DATA__STEP_IBAN_PHONE,step);

        PaymentResponseFormUpdated paymentResponse = (PaymentResponseFormUpdated) paymentService.paymentRequest(paymentRequestStep2);
//        PaymentResponse paymentResponse = paymentService.paymentRequest(paymentRequestStep2);


        //Check we are now on the next step : IBAN_PHONE
//        Assert.assertNotNull(paymentResponse);
        Class<? extends PaymentResponse> paymentResponseClass = paymentResponse.getClass();
        if (paymentResponseClass.isInstance(PaymentResponseFormUpdated.class)) {
            Assert.assertEquals(CONTEXT_DATA__STEP_IBAN_PHONE, paymentResponse.getRequestContext().getRequestData().get(CONTEXT_DATA__STEP));
        }

    }



}
