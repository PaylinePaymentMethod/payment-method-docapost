package com.payline.payment.docapost.service;

import com.payline.payment.docapost.service.impl.PaymentServiceStep02;
import com.payline.payment.docapost.utils.DocapostLocalParam;
import com.payline.payment.docapost.utils.config.ConfigEnvironment;
import com.payline.payment.docapost.utils.http.DocapostHttpClient;
import com.payline.payment.docapost.utils.http.StringResponse;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFailure;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFormUpdated;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import static com.payline.payment.docapost.TestUtils.createCompletePaymentRequest;
import static com.payline.payment.docapost.TestUtils.createDefaultPaymentRequestStep2;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;


@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceStep02Test {

    @InjectMocks
    private PaymentServiceStep02 paymentServiceStep02;

    @Mock
    DocapostHttpClient httpClient;

    private PaymentRequest paymentRequest;
    private ConfigEnvironment env;
    private DocapostLocalParam docapostLocalParam;
    private String credencials;

    @Before
    public void setup() {

        paymentRequest = createDefaultPaymentRequestStep2("0606060606");
        env = ConfigEnvironment.DEV;
        docapostLocalParam = DocapostLocalParam.getInstance();
        credencials = "Basic cGF5bGluZUBkb2NhcG9zdC5mcjpKOltlZjhkY2NtYQ==";
    }


    @Test
    public void testProcessPaymentStepKO() throws IOException, URISyntaxException {


        credencials = "Wrongcredentielas==";
        paymentServiceStep02 = new PaymentServiceStep02(httpClient);
        StringResponse stringResponse1 = new StringResponse();
        stringResponse1.setCode(401);
        stringResponse1.setMessage("KO");
        stringResponse1.setContent("");
        Mockito.when(httpClient.doPost(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(stringResponse1);


        PaymentResponse paymentResponse = paymentServiceStep02.processPaymentStep(paymentRequest, env, docapostLocalParam, credencials);

//        Cas wrong credentials
        Assert.assertTrue(paymentResponse instanceof PaymentResponseFailure);
        PaymentResponseFailure paymentResponseFailure = (PaymentResponseFailure) paymentResponse;
        Assert.assertNotNull(paymentResponseFailure.getFailureCause());
        Assert.assertEquals("401", paymentResponseFailure.getErrorCode());

    }

    @Test
    public void testProcessPaymentStepOK() throws IOException, URISyntaxException {

        credencials = "GoodCredentials";
        paymentRequest = createCompletePaymentRequest();
        paymentServiceStep02 = new PaymentServiceStep02(httpClient);


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


        Mockito.when(httpClient.doPost(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(stringResponseXml);
        Mockito.when(httpClient.doPost(anyString(), anyString(), anyString(), any(Map.class), anyString())).thenReturn(stringResponse1, stringResponse2);


        PaymentResponse paymentResponse = paymentServiceStep02.processPaymentStep(paymentRequest, env, docapostLocalParam, credencials);

//        Cas bon credentials
        PaymentResponseFormUpdated paymentResponseSuccess = (PaymentResponseFormUpdated) paymentResponse;
        Assert.assertNotNull(paymentResponseSuccess);
        Assert.assertNotNull(paymentResponseSuccess.getRequestContext());
        Assert.assertNotNull(paymentResponseSuccess.getRequestContext().getRequestData().get("mandateRum"));
        Assert.assertNotNull(paymentResponseSuccess.getRequestContext().getRequestData().get("signatureId"));
        Assert.assertNotNull(paymentResponseSuccess.getRequestContext().getRequestData().get("step"));
        Assert.assertNotNull(paymentResponseSuccess.getRequestContext().getRequestData().get("transactionId"));
        Assert.assertNotNull(paymentResponseSuccess.getPaymentFormConfigurationResponse());

    }

}
