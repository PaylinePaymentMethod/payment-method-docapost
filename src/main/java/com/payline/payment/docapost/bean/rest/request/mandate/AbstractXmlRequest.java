package com.payline.payment.docapost.bean.rest.request.mandate;

import com.payline.payment.docapost.bean.rest.common.DocapostBean;
import com.payline.payment.docapost.bean.rest.request.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * Created by Thales on 04/09/2018.
 */
public abstract class AbstractXmlRequest extends DocapostBean implements Request {

    private static final Logger LOGGER = LogManager.getLogger(AbstractXmlRequest.class);

    @Override
    public String buildBody() {


        try {

            JAXBContext context = JAXBContext.newInstance(this.getClass());

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter writer = new StringWriter();

            marshaller.marshal(this, writer);

            return writer.toString();

        } catch (JAXBException e) {
            LOGGER.error("XML parsing exception: {}", e.getMessage(), e);
        }

        return "";

    }

}