package com.payline.payment.docapost.mandate.response.mandate;

import com.payline.payment.docapost.bean.rest.response.mandate.WSCTOrderDTOResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public class WSCTOrderDTOResponseTest {

    private WSCTOrderDTOResponse wsctOrderDTOResponse;

    @Before
    public void setup() throws JAXBException {
        String xmlContent = "<WSCTOrderDTO>" +
                "<label>myLabel</label>" +
                "<creditorId>MycreditorId</creditorId>" +
                "<amount>10</amount>" +
                "</WSCTOrderDTO>";

        JAXBContext context = JAXBContext.newInstance(WSCTOrderDTOResponse.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StringReader reader = new StringReader(xmlContent);
        wsctOrderDTOResponse = (WSCTOrderDTOResponse) unmarshaller.unmarshal(reader);
    }

    @Test
    public void testToString() {
        String result = wsctOrderDTOResponse.toString();
        Assert.assertTrue(result.contains("myLabel"));
        Assert.assertTrue(result.contains("MycreditorId"));
        Assert.assertTrue(result.contains("10"));

    }
}
