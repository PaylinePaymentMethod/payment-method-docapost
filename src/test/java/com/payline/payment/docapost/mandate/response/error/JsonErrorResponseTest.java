package com.payline.payment.docapost.mandate.response.error;

import com.google.gson.Gson;
import com.payline.payment.docapost.bean.rest.response.error.JsonErrorResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JsonErrorResponseTest {


    JsonErrorResponse jsonErrorResponse;

    @Before
    public void setup(){
        String data = "{reason:\"error reason\", message:\"error message\"}";
        Gson gson = new Gson();
        jsonErrorResponse = gson.fromJson(data,JsonErrorResponse.class);

    }

    @Test
    public void testToString(){

        String result = jsonErrorResponse.toString();
        Assert.assertTrue(result.contains("error reason"));
        Assert.assertTrue(result.contains("error message"));

    }

    @Test
    public void testGetter(){
        Assert.assertEquals( "error message",jsonErrorResponse.getMessage());
        Assert.assertEquals("error reason", jsonErrorResponse.getReason());

    }
}
