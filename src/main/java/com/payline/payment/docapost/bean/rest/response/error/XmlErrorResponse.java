package com.payline.payment.docapost.bean.rest.response.error;

import com.payline.payment.docapost.bean.rest.response.AbstractXmlResponse;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.StringReader;

/**
 * Created by Thales on 04/09/2018.
 */
@XmlRootElement(name = "sepalia")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlErrorResponse extends AbstractXmlResponse {

    @XmlElement(name = "exception")
    private XmlErrorResponseException exception;

    /**
     * Public default constructor
     */
    public XmlErrorResponse() { }

    public XmlErrorResponseException getException() {
        return exception;
    }

    public void setException(XmlErrorResponseException exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();

        result.append("***** XmlErrorResponse info\n");

        result.append(exception.toString() + "\n");

        return result.toString();
    }

    //******************************************************************************************************************
    //***** BUILDER
    public static final class Builder {
        public XmlErrorResponse fromXml(String xmlContent) {
            return (XmlErrorResponse) parse(XmlErrorResponse.class, xmlContent);
        }
    }
    //***** BUILDER
    //******************************************************************************************************************

}