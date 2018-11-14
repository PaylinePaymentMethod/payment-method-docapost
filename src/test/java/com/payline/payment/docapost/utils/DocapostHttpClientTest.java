package com.payline.payment.docapost.utils;

import com.payline.payment.docapost.TestUtils;
import com.payline.payment.docapost.utils.http.DocapostHttpClient;
import com.payline.payment.docapost.utils.http.StringResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class DocapostHttpClientTest {

    private DocapostHttpClient client = new DocapostHttpClient();


    @Test
    public void testDoGetOK() throws IOException, URISyntaxException {

        String credentials = DocapostUtils.generateBasicCredentials(TestUtils.GOOD_LOGIN, TestUtils.GOOD_PWD);
        String ics = TestUtils.GOOD_CREDITOR_ID;
        StringResponse response = client.doGet("https", "espaceclient.sepalia.fr", "rcte/api/creditor/" + ics, credentials);

        //Assert we have a response
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getCode());
    }


    //    public StringResponse doPost(String scheme, String host, String path, String xmlContent, String credential ) throws IOException, URISyntaxException {
    @Test
    public void testDoPostOK() throws IOException, URISyntaxException {

        String credentials = DocapostUtils.generateBasicCredentials(TestUtils.GOOD_LOGIN, TestUtils.GOOD_PWD);
        StringResponse response = client.doPost("https", "espaceclient.sepalia.fr", "rcte/api/mandate/create", "<WSMandateDTO><creditorId>MARCHAND1</creditorId></WSMandateDTO>", credentials);

        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getCode());


    }


    @Test
    public void testDoPostArray() throws IOException, URISyntaxException {

        String credentials = DocapostUtils.generateBasicCredentials(TestUtils.GOOD_LOGIN, TestUtils.GOOD_PWD);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("parameter0", "value0");
        parameters.put("parameter1", "value1");
        parameters.put("parameter2", "value2");
        parameters.put("parameter3", "value3");

        StringResponse response = client.doPost("https", "espaceclient.sepalia.fr", "rcte/api/mandate/create", parameters, credentials);
        Assert.assertNotNull(response);
        Assert.assertEquals(500, response.getCode());
    }

}
