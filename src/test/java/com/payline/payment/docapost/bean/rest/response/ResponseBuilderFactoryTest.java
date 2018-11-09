package com.payline.payment.docapost.bean.rest.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.payline.payment.docapost.bean.rest.response.error.XmlErrorResponse;
import com.payline.payment.docapost.bean.rest.response.mandate.WSCTOrderDTOResponse;
import com.payline.payment.docapost.bean.rest.response.mandate.WSDDOrderDTOResponse;
import com.payline.payment.docapost.bean.rest.response.mandate.WSMandateDTOResponse;
import com.payline.payment.docapost.bean.rest.response.signature.InitiateSignatureResponse;
import com.payline.payment.docapost.bean.rest.response.signature.SendOtpResponse;
import com.payline.payment.docapost.bean.rest.response.signature.SetCodeResponse;
import com.payline.payment.docapost.bean.rest.response.signature.TerminateSignatureResponse;
import org.junit.Assert;
import org.junit.Test;

public class ResponseBuilderFactoryTest {

    private String xmlResponse;


    @Test
    public void testBuildXmlErrorResponse() {
        xmlResponse = "<sepalia><exception>test error</exception></sepalia>";

        XmlErrorResponse xmlResp = ResponseBuilderFactory.buildXmlErrorResponse(xmlResponse);
        Assert.assertNotNull(xmlResp);
        Assert.assertEquals("test error", xmlResp.getException().getValue());
    }

    @Test
    public void testBuildWsMandateDTOResponse() {
        xmlResponse = "<WSMandateDTO>" +
                "<rum>123</rum>" +
                "<creditorId>creditorId</creditorId>" +
                "<creditorIcs>creditorIcs</creditorIcs>" +
                "<recurrent>true</recurrent>" +
                "<status>Ok</status>" +
                "<mode>testmode</mode>" +
                "<flowName>flow00</flowName>" +
                "<mode>testmode</mode>" +
                "<language>FR</language>" +
                "<debtor></debtor>" +
                "</WSMandateDTO>";


        WSMandateDTOResponse xmlResp = ResponseBuilderFactory.buildWsMandateDTOResponse(xmlResponse);
        Assert.assertNotNull(xmlResp);
        Assert.assertEquals("123", xmlResp.getRum());
        Assert.assertEquals("creditorId", xmlResp.getCreditorId());
        Assert.assertEquals("creditorIcs", xmlResp.getCreditorIcs());
        Assert.assertEquals("flow00", xmlResp.getFlowName());
        Assert.assertEquals("testmode", xmlResp.getMode());
        Assert.assertEquals("Ok", xmlResp.getStatus());


    }

    @Test
    public void testBuildWsddOrderDTOResponse() {

        xmlResponse = "<WSDDOrderDTO>" +
                "<rum>123</rum>" +
                "<creditorId>creditorId</creditorId>" +
                "<creditorIcs>creditorIcs</creditorIcs>" +
                "<label>some label</label>" +
                "<status>Ok</status>" +
                "<reference>reference</reference>" +
                "<e2eId>e2eId</e2eId>" +
                "<amount>10</amount>" +
                "<remitDate>2018-12-12</remitDate>" +
                "<dueDate>2018-12-12</dueDate>" +
                "<identifier></identifier>" +
                "</WSDDOrderDTO>";


        WSDDOrderDTOResponse xmlResp = ResponseBuilderFactory.buildWsddOrderDTOResponse(xmlResponse);
        Assert.assertNotNull(xmlResp);
        Assert.assertEquals("123", xmlResp.getRum());
        Assert.assertEquals("creditorId", xmlResp.getCreditorId());
        Assert.assertEquals("reference", xmlResp.getReference());
        Assert.assertEquals("Ok", xmlResp.getStatus());
        Assert.assertEquals("", xmlResp.getIdentifier());
        Assert.assertEquals("10.0", xmlResp.getAmount().toString());

    }

    @Test
    public void testBuildWsctOrderDTOResponse() {

        xmlResponse = "<WSCTOrderDTO>" +
                "<rum>123</rum>" +
                "<creditorId>creditorId</creditorId>" +
                "<creditorIcs>creditorIcs</creditorIcs>" +
                "<label>some label</label>" +
                "<status>Ok</status>" +
                "<sequence>test</sequence>" +
                "<reference>reference</reference>" +
                "<e2eId>e2eId</e2eId>" +
                "<amount>10</amount>" +
                "<creditorAccountName>John Doe</creditorAccountName>" +
                "<receiverName>Foo Bar</receiverName>" +
                "<receiverIban>FR780508091254689753</receiverIban>" +
                "<remitDate>2018-12-12</remitDate>" +
                "<dueDate>2018-12-12</dueDate>" +
                "<identifier></identifier>" +
                "</WSCTOrderDTO>";


        WSCTOrderDTOResponse xmlResp = ResponseBuilderFactory.buildWsctOrderDTOResponse(xmlResponse);
        Assert.assertNotNull(xmlResp);
        Assert.assertEquals("123", xmlResp.getRum());
        Assert.assertEquals("some label", xmlResp.getLabel());
        Assert.assertEquals("creditorId", xmlResp.getCreditorId());
        Assert.assertEquals("reference", xmlResp.getReference());
        Assert.assertEquals("Ok", xmlResp.getStatus());
        Assert.assertEquals("", xmlResp.getIdentifier());
        Assert.assertEquals("John Doe", xmlResp.getCreditorAccountName());
        Assert.assertEquals("Foo Bar", xmlResp.getReceiverName());
        Assert.assertEquals("FR780508091254689753", xmlResp.getReceiverIban());
        Assert.assertEquals("2018-12-12", xmlResp.getDueDate());
        Assert.assertEquals("2018-12-12", xmlResp.getRemitDate());
        Assert.assertEquals("test", xmlResp.getSequence());
    }

    @Test
    public void testBuildInitiateSignatureResponse() {

        JsonObject jsonElement = new JsonObject();
        jsonElement.getAsJsonObject().addProperty("transactionId", "1234");
        String jsonResponse = jsonElement.toString();
        InitiateSignatureResponse initiateSignatureResponse = ResponseBuilderFactory.buildInitiateSignatureResponse(jsonResponse);
        Assert.assertNotNull(initiateSignatureResponse);
        Assert.assertEquals("1234", initiateSignatureResponse.getTransactionId());

    }

    @Test
    public void testBuildSendOtpResponse() {

        JsonObject jsonElement = new JsonObject();
        jsonElement.getAsJsonObject().addProperty("signatureID", "1234");
        String jsonResponse = jsonElement.toString();


        SendOtpResponse sendOtpResponse = ResponseBuilderFactory.buildSendOtpResponse(jsonResponse);
        Assert.assertNotNull(sendOtpResponse);
        Assert.assertNotNull(sendOtpResponse);
        Assert.assertEquals("1234", sendOtpResponse.getSignatureId());

    }

    @Test
    public void testBuildSetCodeResponse() {

        JsonObject jsonElement = new JsonObject();
        JsonArray errorList = new JsonArray();
        jsonElement.getAsJsonObject().add("errors", errorList);

        String errors = jsonElement.toString();
        SetCodeResponse setCodeResponse = ResponseBuilderFactory.buildSetCodeResponse(errors);
        Assert.assertNotNull(setCodeResponse);
        Assert.assertTrue(setCodeResponse.isResultOk());

    }


    @Test
    public void testBuildTerminateSignatureResponse() {

        JsonObject jsonElement = new JsonObject();
        JsonArray errors = new JsonArray();
        jsonElement.getAsJsonObject().add("errors", errors);

        String jsonResponse = jsonElement.toString();
        TerminateSignatureResponse terminateSignatureResponse = ResponseBuilderFactory.buildTerminateSignatureResponse(jsonResponse);
        Assert.assertNotNull(terminateSignatureResponse);
        Assert.assertTrue(terminateSignatureResponse.isResultOk());
    }


}
