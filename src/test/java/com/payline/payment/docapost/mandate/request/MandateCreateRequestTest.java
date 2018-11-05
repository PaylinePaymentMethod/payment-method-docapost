package com.payline.payment.docapost.mandate.request;

import com.payline.payment.docapost.bean.rest.request.mandate.MandateCreateRequest;
import com.payline.payment.docapost.utils.DocapostUtils;
import org.junit.Assert;
import org.junit.Test;

public class MandateCreateRequestTest {

    @Test
    public void toStringTest() {
        MandateCreateRequest mandateCreateRequest = new MandateCreateRequest("creditorId",
                "rum",
                "fr",
                DocapostUtils.defaultDebtor());

        String result = mandateCreateRequest.toString();

        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains("creditorId"));
    }
}
