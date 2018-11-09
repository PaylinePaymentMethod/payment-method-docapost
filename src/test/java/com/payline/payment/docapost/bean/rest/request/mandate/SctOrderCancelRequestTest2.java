package com.payline.payment.docapost.bean.rest.request.mandate;

import org.junit.Assert;
import org.junit.Test;

public class SctOrderCancelRequestTest2 {

    @Test
    public void toStringTest() {
        SctOrderCancelRequest sctOrderCancelRequest = new SctOrderCancelRequest("creditorId",
                "e2eId");

        String result = sctOrderCancelRequest.toString();

        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains("creditorId"));
    }
}
