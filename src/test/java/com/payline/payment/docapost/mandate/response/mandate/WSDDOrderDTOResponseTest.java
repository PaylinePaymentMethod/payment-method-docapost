package com.payline.payment.docapost.mandate.response.mandate;

import com.payline.payment.docapost.bean.rest.response.mandate.WSDDOrderDTOResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public class WSDDOrderDTOResponseTest {

    private WSDDOrderDTOResponse wsddOrderDTOResponse;

    @Before
    public void setup() throws JAXBException {
        String xmlContent = "<WSDDOrderDTO>" +
                "<label>myLabel</label>" +
                "<creditorId>MycreditorId</creditorId>" +
                "<amount>10</amount>" +
                "<reference>ref</reference>" +
                "</WSDDOrderDTO>";

        JAXBContext context = JAXBContext.newInstance(WSDDOrderDTOResponse.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StringReader reader = new StringReader(xmlContent);
        wsddOrderDTOResponse = (WSDDOrderDTOResponse) unmarshaller.unmarshal(reader);
    }

    @Test
    public void testToString() {
        String result = wsddOrderDTOResponse.toString();
        Assert.assertTrue(result.contains("myLabel"));
        Assert.assertTrue(result.contains("MycreditorId"));
        Assert.assertTrue(result.contains("ref"));
        Assert.assertTrue(result.contains("10"));
    }


}
