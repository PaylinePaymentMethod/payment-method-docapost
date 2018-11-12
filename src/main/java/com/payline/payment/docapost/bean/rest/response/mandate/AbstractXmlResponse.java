package com.payline.payment.docapost.bean.rest.response.mandate;

import com.google.gson.Gson;
import com.payline.payment.docapost.bean.rest.common.DocapostBean;
import com.payline.payment.docapost.bean.rest.response.error.XmlErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

/**
 * Created by Thales on 05/09/2018.
 */
public abstract class AbstractXmlResponse extends DocapostBean {

    private static final Logger LOGGER = LogManager.getLogger(AbstractXmlResponse.class);

    public static Object parse(Class clazz, String xmlContent) {

        try {

            JAXBContext context = JAXBContext.newInstance(clazz);

            Unmarshaller unmarshaller = context.createUnmarshaller();

            StringReader reader = new StringReader(xmlContent);

            return unmarshaller.unmarshal(reader);

        } catch (JAXBException e) {
            LOGGER.error("XML parsing exception : {}", e.getMessage(), e);
        }

        return null;

    }

    public boolean isResultOk() {
        return !(this instanceof XmlErrorResponse);
    }


    @Override
    public String toString() {

        Gson gson = new Gson();
        return gson.toJson(this);
    }

}