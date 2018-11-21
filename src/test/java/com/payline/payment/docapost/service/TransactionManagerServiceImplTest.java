package com.payline.payment.docapost.service;

import com.google.gson.JsonSyntaxException;
import com.payline.payment.docapost.service.impl.TransactionManagerServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Map;

public class TransactionManagerServiceImplTest {


    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private Map<String, String> additionalData;
    private TransactionManagerServiceImpl service;

    @Before
    public void setup() {
        service = new TransactionManagerServiceImpl();

    }

    @Test
    public void readAdditionalDataKo() {
        expectedEx.expect(JsonSyntaxException.class);
        expectedEx.expectMessage("Additional data syntax incorrect [{}]");
        String malformedJson = "{mandateRum: \"RUMTEST01\",transactionId: \"Transaction01\", signatureId: \"007\"";
        additionalData = service.readAdditionalData(malformedJson, "PaymentResponseSuccessAdditionalData");


    }

    @Test
    public void readAdditionalData() {
        String dataJson = "{mandateRum: \"RUMTEST01\",transactionId: \"Transaction01\", signatureId: \"007\"}";
        additionalData = service.readAdditionalData(dataJson, "PaymentResponseSuccessAdditionalData");
        Assert.assertNotNull(additionalData);
        Assert.assertEquals(3, additionalData.size());
        Assert.assertEquals("RUMTEST01", additionalData.get("mandateRum"));

    }

    @Test
    public void readAdditionalDataNull() {
        final TransactionManagerServiceImpl tmsi = new TransactionManagerServiceImpl();
        final Map<String, String> addData = tmsi.readAdditionalData(null, null);
        Assert.assertTrue(addData.isEmpty());
    }

}
