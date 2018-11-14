package com.payline.payment.docapost.service;

import com.payline.payment.docapost.service.impl.PaymentServiceStep03;
import com.payline.payment.docapost.utils.DocapostLocalParam;
import com.payline.payment.docapost.utils.config.ConfigEnvironment;
import com.payline.payment.docapost.utils.http.DocapostHttpClient;
import com.payline.payment.docapost.utils.http.StringResponse;
import com.payline.payment.docapost.utils.i18n.I18nService;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFailure;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseSuccess;
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
import java.util.HashMap;
import java.util.Map;

import static com.payline.payment.docapost.TestUtils.createCustomPaymentRequestStep3;
import static com.payline.payment.docapost.service.PaymentServiceStep.DEFAULT_ERROR_CODE;
import static com.payline.payment.docapost.utils.DocapostConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceStep03Test {

    @InjectMocks
    private PaymentServiceStep03 paymentServiceStep03;

    @Mock
    private DocapostHttpClient httpClient;

    private I18nService i18n = I18nService.getInstance();

    private PaymentRequest paymentRequest;
    private ConfigEnvironment env;
    private DocapostLocalParam docapostLocalParam;
    private String credencials;


    @Before
    public void setup() {

        env = ConfigEnvironment.DEV;
        docapostLocalParam = DocapostLocalParam.getInstance();
        paymentServiceStep03 = new PaymentServiceStep03(httpClient);


        docapostLocalParam.setMandateRum("PAYLINE-KO4SPCEL98");
        docapostLocalParam.setSignatureId("963");
        docapostLocalParam.setTransactionId("2c949e2f66b0c86c0167082539c5332f");
    }


    @Test
    public void processPaymentStepTestKO() {
        credencials = "Wrongcredentielas==";
        paymentServiceStep03 = new PaymentServiceStep03(httpClient);
        PaymentResponse paymentResponse = paymentServiceStep03.processPaymentStep(paymentRequest, env, docapostLocalParam, credencials);

//        Cas wrong credentials
        Assert.assertNotNull(paymentResponse instanceof PaymentResponseFailure);
        PaymentResponseFailure paymentResponseFailure = (PaymentResponseFailure) paymentResponse;
        Assert.assertNotNull(paymentResponseFailure.getFailureCause());
        Assert.assertEquals(DEFAULT_ERROR_CODE, paymentResponseFailure.getErrorCode());
        Assert.assertEquals(FailureCause.INVALID_DATA, paymentResponseFailure.getFailureCause());


    }

    @Test
    public void processPaymentStepTestOK() throws IOException, URISyntaxException {
        credencials = "Basic cGF5bGluZUBkb2NhcG9zdC5mcjpKOltlZjhkY2NtYQ==";
        String otp = "123456";
        String phoneNumber = "0605040302";

        Map<String, String> requestContextMap = new HashMap<>();
        requestContextMap.put(CONTEXT_DATA_STEP, CONTEXT_DATA_STEP_OTP);
        requestContextMap.put(CONTEXT_DATA_MANDATE_RUM, docapostLocalParam.getMandateRum());
        requestContextMap.put(CONTEXT_DATA_TRANSACTION_ID, docapostLocalParam.getTransactionId());
        requestContextMap.put(CONTEXT_DATA_SIGNATURE_ID, docapostLocalParam.getSignatureId());


        StringResponse stringResponse = new StringResponse();
        stringResponse.setCode(200);
        stringResponse.setMessage("OK");
        stringResponse.setContent("{}");


        StringResponse swddOrdercreatedMocked = new StringResponse();
        swddOrdercreatedMocked.setCode(200);
        swddOrdercreatedMocked.setMessage("OK");
        swddOrdercreatedMocked.setContent("<WSDDOrderDTO>\n" +
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
                "</WSDDOrderDTO>");

        Mockito.when(httpClient.doPost(anyString(), anyString(), anyString(), any(Map.class), anyString())).thenReturn(stringResponse);
        Mockito.when(httpClient.doPost(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(swddOrdercreatedMocked);
        paymentRequest = createCustomPaymentRequestStep3(requestContextMap, otp, phoneNumber);
        PaymentResponse paymentResponse = paymentServiceStep03.processPaymentStep(paymentRequest, env, docapostLocalParam, credencials);

        Assert.assertTrue(paymentResponse instanceof PaymentResponseSuccess);

        PaymentResponseSuccess paymentResponseSuccess = (PaymentResponseSuccess) paymentResponse;
        Assert.assertNotNull(paymentResponseSuccess);
        Assert.assertEquals(true, docapostLocalParam.getSignatureSuccess());


    }


}
