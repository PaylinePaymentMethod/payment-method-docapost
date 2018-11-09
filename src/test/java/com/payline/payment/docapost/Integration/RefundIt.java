package com.payline.payment.docapost.Integration;

import com.payline.payment.docapost.service.AbstractRefundHttpService;
import com.payline.payment.docapost.service.impl.RefundServiceImpl;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.payment.PaymentFormContext;
import com.payline.pmapi.integration.AbstractPaymentIntegration;
import org.junit.Test;

import java.util.Map;

public class RefundIt {

    private RefundServiceImpl refundService = new RefundServiceImpl();
    public static final String GOOD_CREDITOR_ID = "MARCHAND1";
    //Todo  find a better way to do it
    public Map<String, String> requestContext;


    @Test
    public void refundPaymentIt() {


    }
}
