package com.payline.payment.docapost.service;

import com.payline.payment.docapost.service.impl.PaymentServiceStep01;
import com.payline.payment.docapost.utils.DocapostLocalParam;
import com.payline.payment.docapost.utils.config.ConfigEnvironment;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFormUpdated;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.payline.payment.docapost.TestUtils.createDefaultPaymentRequest;

public class PaymentServiceStep01Test {

    private PaymentServiceStep01 service;

    @Before
    public void setup() {

        service = new PaymentServiceStep01();

    }

    @Test
    public void processPaymentStepTest() {
        PaymentRequest request = createDefaultPaymentRequest();
        DocapostLocalParam localParam = DocapostLocalParam.getInstance();
        ConfigEnvironment environment = ConfigEnvironment.DEV;
        String credentials = "somecredentials";
        PaymentResponse paymentResponse = service.processPaymentStep(request, environment, localParam, credentials);

        Assert.assertNotNull(paymentResponse);
        Assert.assertTrue(paymentResponse instanceof PaymentResponseFormUpdated);
        PaymentResponseFormUpdated paymentFormResponse = (PaymentResponseFormUpdated) paymentResponse;
        Assert.assertNotNull(paymentFormResponse);
        Assert.assertNotNull(paymentFormResponse.getPaymentFormConfigurationResponse());
        Assert.assertNotNull(paymentFormResponse.getRequestContext());

    }


}
