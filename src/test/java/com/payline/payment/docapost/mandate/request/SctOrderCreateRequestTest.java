package com.payline.payment.docapost.mandate.request;

import com.payline.payment.docapost.bean.rest.request.mandate.SctOrderCreateRequest;
import org.junit.Assert;
import org.junit.Test;

public class SctOrderCreateRequestTest {


    @Test
    public void toStringTest() {
        SctOrderCreateRequest sctOrderCancelRequest = new SctOrderCreateRequest("creditorId",
                "rum",
                new Float(10),
                "label",
                 "e2eId");

        String result = sctOrderCancelRequest.toString();

        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains("creditorId"));
        Assert.assertTrue(result.contains("rum"));
        Assert.assertTrue(result.contains("label"));
        Assert.assertTrue(result.contains("e2eId"));
    }
}
