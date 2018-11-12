package com.payline.payment.docapost.mandate.response.error;

import com.payline.payment.docapost.bean.rest.response.error.XmlErrorResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public class XmlErrorResponseTest {

    private XmlErrorResponse xmlErrorResponse;

    @Before
    public void setup() throws JAXBException {

        String xmlContent = "<sepalia>" +
                "<exception code=\"200\">" +
                "Error Value" +
                "</exception>" +
                "</sepalia>";

        JAXBContext context = JAXBContext.newInstance(XmlErrorResponse.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StringReader reader = new StringReader(xmlContent);
        xmlErrorResponse = (XmlErrorResponse) unmarshaller.unmarshal(reader);
    }

    @Test
    public void testToString() {

        String result = xmlErrorResponse.toString();
        Assert.assertTrue(result.contains("200"));
        Assert.assertTrue(result.contains("Error Value"));

    }

    @Test
    public void testGetter() {
        Assert.assertEquals("200", xmlErrorResponse.getException().getCode());
        Assert.assertEquals("Error Value", xmlErrorResponse.getException().getValue());

    }
}
