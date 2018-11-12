package com.payline.payment.docapost.bean.rest.request.mandate;

import com.payline.payment.docapost.utils.DocapostUtils;
import org.junit.Assert;
import org.junit.Test;

public class MandateCreateRequestTest {

    @Test
    public void toStringTest() {
        MandateCreateRequest mandateCreateRequest = new MandateCreateRequest("creditorId",
                "rum",
                null,
                "fr",
                DocapostUtils.defaultDebtor());

        String result = mandateCreateRequest.toString();

        Assert.assertNotNull(result);
        Assert.assertNotNull(mandateCreateRequest);
        Assert.assertTrue(result.contains("creditorId"));
        Assert.assertTrue(result.contains("fr"));
        Assert.assertFalse((Boolean) mandateCreateRequest.isRecurrent());
    }
}
