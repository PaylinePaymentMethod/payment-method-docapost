package com.payline.payment.docapost.mandate.request;

import com.payline.payment.docapost.bean.rest.request.mandate.SddOrderCreateRequest;
import org.junit.Assert;
import org.junit.Test;

public class SddOrderCreateRequestTest {

    @Test
    public void toStringTest() {
        SddOrderCreateRequest mandateCreateRequest = new SddOrderCreateRequest("creditorId",
                "rum",
                new Float(10),
                "label",
                "e2eId");

        String result = mandateCreateRequest.toString();

        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains("creditorId"));
        Assert.assertTrue(result.contains("e2eId"));
    }
}
