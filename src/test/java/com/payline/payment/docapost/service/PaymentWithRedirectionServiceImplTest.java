package com.payline.payment.docapost.service;

import com.payline.payment.docapost.service.impl.PaymentWithRedirectionServiceImpl;
import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;
import com.payline.pmapi.bean.payment.request.TransactionStatusRequest;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

public class PaymentWithRedirectionServiceImplTest {

    @Mock
    RedirectionPaymentRequest redirectionPaymentRequest;
    @Mock
    TransactionStatusRequest transactionStatusRequest;

    PaymentWithRedirectionServiceImpl service = new PaymentWithRedirectionServiceImpl();


    @Test
    public void finalizeRedirectionPaymentTest() {
        service.finalizeRedirectionPayment(redirectionPaymentRequest);
        Assert.assertTrue(true);
    }

    @Test
    public void handleSessionExpiredTest() {
        service.handleSessionExpired(transactionStatusRequest);
        Assert.assertTrue(true);
    }
}
