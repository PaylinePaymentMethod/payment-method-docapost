package com.payline.payment.docapost.utils;

import com.payline.payment.docapost.TestUtils;
import com.payline.payment.docapost.utils.http.DocapostHttpClient;
import com.payline.payment.docapost.utils.http.StringResponse;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;

public class DocapostHttpClientTest {

    @InjectMocks
    DocapostHttpClient client = Mockito.mock(DocapostHttpClient.class);


    @Test
    public void testDoGetOK() throws IOException, URISyntaxException {

        StringResponse stringResponse1 = new StringResponse();
        stringResponse1.setCode(200);
        stringResponse1.setMessage("OK");
        stringResponse1.setContent("");
        Mockito.when(client.doGet(anyString(), anyString(), anyString(), anyString())).thenReturn(stringResponse1);


        String credentials = DocapostUtils.generateBasicCredentials(TestUtils.GOOD_LOGIN, TestUtils.GOOD_PWD);
        String ics = TestUtils.GOOD_CREDITOR_ID;
        StringResponse response = client.doGet("https", "espaceclient.sepalia.fr", "rcte/api/creditor/" + ics, credentials);

        //Assert we have a response
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getCode());
    }

    @Test
    public void testDoGetKO() throws IOException, URISyntaxException {

        StringResponse stringResponse1 = new StringResponse();
        stringResponse1.setCode(400);
        stringResponse1.setMessage("Bad Request");
        stringResponse1.setContent("");
        Mockito.when(client.doGet(anyString(), anyString(), anyString(), anyString())).thenReturn(stringResponse1);


        String credentials = DocapostUtils.generateBasicCredentials(TestUtils.GOOD_LOGIN, TestUtils.GOOD_PWD);
        String ics = TestUtils.GOOD_CREDITOR_ID;
        StringResponse response = client.doGet("https", "espaceclient.sepalia.fr", "rcte/api/creditor/" + ics, credentials);

        //Assert we have a response
        Assert.assertNotNull(response);
        Assert.assertEquals(400, response.getCode());
    }


    @Test
    public void testDoPostOK() throws IOException, URISyntaxException {

        StringResponse stringResponse1 = new StringResponse();
        stringResponse1.setCode(200);
        stringResponse1.setMessage("OK");
        stringResponse1.setContent("");
        Mockito.when(client.doPost(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(stringResponse1);

        String credentials = DocapostUtils.generateBasicCredentials(TestUtils.GOOD_LOGIN, TestUtils.GOOD_PWD);
        StringResponse response = client.doPost("https", "espaceclient.sepalia.fr", "rcte/api/mandate/create", "<WSMandateDTO><creditorId>MARCHAND1</creditorId></WSMandateDTO>", credentials);

        //Assert we have a response
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getCode());


    }

    @Test
    public void testDoPostKO() throws IOException, URISyntaxException {

        StringResponse stringResponse1 = new StringResponse();
        stringResponse1.setCode(400);
        stringResponse1.setMessage("Bad Request");
        stringResponse1.setContent("");
        Mockito.when(client.doPost(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(stringResponse1);

        String credentials = DocapostUtils.generateBasicCredentials(TestUtils.GOOD_LOGIN, TestUtils.GOOD_PWD);
        StringResponse response = client.doPost("https", "espaceclient.sepalia.fr", "rcte/api/mandate/create", "<WSMandateDTO><creditorId>MARCHAND1</creditorId></WSMandateDTO>", credentials);

        //Assert we have a response
        Assert.assertNotNull(response);
        Assert.assertEquals(400, response.getCode());
        Assert.assertEquals("Bad Request", response.getMessage());

    }


    @Test
    public void testDoPostArrayKO() throws IOException, URISyntaxException {

        StringResponse stringResponse1 = new StringResponse();
        stringResponse1.setCode(500);
        stringResponse1.setMessage("Bad Request");
        stringResponse1.setContent("");
        Mockito.when(client.doPost(anyString(), anyString(), anyString(), Mockito.any(Map.class), anyString())).thenReturn(stringResponse1);


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

    @Test
    public void testDoPostArrayOK() throws IOException, URISyntaxException {

        StringResponse stringResponse1 = new StringResponse();
        stringResponse1.setCode(200);
        stringResponse1.setMessage("OK");
        stringResponse1.setContent("");
        Mockito.when(client.doPost(anyString(), anyString(), anyString(), Mockito.any(Map.class), anyString())).thenReturn(stringResponse1);


        String credentials = DocapostUtils.generateBasicCredentials(TestUtils.GOOD_LOGIN, TestUtils.GOOD_PWD);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("parameter0", "value0");
        parameters.put("parameter1", "value1");
        parameters.put("parameter2", "value2");
        parameters.put("parameter3", "value3");

        StringResponse response = client.doPost("https", "espaceclient.sepalia.fr", "rcte/api/mandate/create", parameters, credentials);
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getCode());
    }
}
