package com.payline.payment.docapost.mandate.response.error;

import com.payline.payment.docapost.bean.rest.response.error.XmlErrorResponseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public class XmlErrorResponseExceptionTest {


    XmlErrorResponseException xmlErrorResponseException;

    @Before
    public void setup() throws JAXBException {

        String xmlContent = "<exception code=\"200\">" +
                "Error Value" +
                "</exception>";

        JAXBContext context = JAXBContext.newInstance(XmlErrorResponseException.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StringReader reader = new StringReader(xmlContent);
        xmlErrorResponseException = (XmlErrorResponseException) unmarshaller.unmarshal(reader);
    }

    @Test
    public void testToString(){

        String result = xmlErrorResponseException.toString();
        Assert.assertTrue(result.contains("200"));
        Assert.assertTrue(result.contains("Error Value"));

    }

    @Test
    public void testGetter(){
        Assert.assertEquals("200",xmlErrorResponseException.getCode());
        Assert.assertEquals("Error Value",xmlErrorResponseException.getValue());

    }


    @Test
    public void testSetter(){
        xmlErrorResponseException.setCode("400");
        xmlErrorResponseException.setValue("New error value");
        Assert.assertEquals("400",xmlErrorResponseException.getCode());
        Assert.assertEquals("New error value",xmlErrorResponseException.getValue() );

    }
}
