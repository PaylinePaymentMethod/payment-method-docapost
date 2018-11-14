package com.payline.payment.docapost.mandate.response.mandate;

import com.payline.payment.docapost.bean.rest.response.mandate.WSMandateDTOResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public class WSMandateDTOResponseTest {


    private WSMandateDTOResponse wsMandateDTOResponse;

    @Before
    public void setup() throws JAXBException {
        String xmlContent = "<WSMandateDTO>" +
                "<creditorIcs>creditorIcs</creditorIcs>" +
                "<creditorId>MycreditorId</creditorId>" +
                "<rum>10XXXXXX</rum>" +
                "</WSMandateDTO>";

        JAXBContext context = JAXBContext.newInstance(WSMandateDTOResponse.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StringReader reader = new StringReader(xmlContent);
        wsMandateDTOResponse = (WSMandateDTOResponse) unmarshaller.unmarshal(reader);
    }

    @Test
    public void testToString() {
        String result = wsMandateDTOResponse.toString();
        Assert.assertTrue(result.contains("creditorIcs"));
        Assert.assertTrue(result.contains("MycreditorId"));
        Assert.assertTrue(result.contains("10"));

    }
}
