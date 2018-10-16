package com.payline.payment.docapost.bean.rest.response.mandate;

import com.payline.payment.docapost.bean.rest.response.error.XmlErrorResponse;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

/**
 * Created by Thales on 05/09/2018.
 */
public abstract class AbstractXmlResponse {

    public static Object parse(Class clazz, String xmlContent) {

        Object result = null;

        try {

            JAXBContext context = JAXBContext.newInstance(clazz);

            Unmarshaller unmarshaller = context.createUnmarshaller();

            StringReader reader = new StringReader(xmlContent);

            result = unmarshaller.unmarshal(reader);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return result;

    }

    public boolean isResultOk() {
        boolean result = true;
        if (this instanceof XmlErrorResponse) {
            result = false;
        }
        return result;
    }

}