package com.payline.payment.docapost.exception;

import org.junit.Assert;
import org.junit.Test;

public class InvalidRequestExceptionTest {

    @Test
    public void invalidRequestExceptionTest(){

        InvalidRequestException exception = new InvalidRequestException("test exception");
        Assert.assertTrue(exception instanceof Exception);
        Assert.assertEquals("test exception",exception.getMessage());
    }
}
