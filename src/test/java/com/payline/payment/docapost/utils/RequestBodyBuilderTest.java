package com.payline.payment.docapost.utils;

import com.payline.payment.docapost.utils.http.RequestBodyBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RequestBodyBuilderTest {


    Map<String, String> formData;
    RequestBodyBuilder requestBodyBuilder;

    @Before
    public void setup(){
      formData = new HashMap<>();
      formData.put("key1","value1");
      formData.put("key2","value2");
      requestBodyBuilder = new RequestBodyBuilder();

    }

    @Test
    public void requestBodyBuilderTest(){
        requestBodyBuilder = requestBodyBuilder.withFormData(formData);
        Assert.assertNotNull(requestBodyBuilder);
    }

}
