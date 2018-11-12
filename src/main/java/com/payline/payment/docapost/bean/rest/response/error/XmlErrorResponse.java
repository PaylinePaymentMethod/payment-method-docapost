package com.payline.payment.docapost.bean.rest.response.error;

import com.payline.payment.docapost.bean.rest.response.mandate.AbstractXmlResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
    public XmlErrorResponse() {
        // ras.
    }

    public XmlErrorResponseException getException() {
        return exception;
    }

    public void setException(XmlErrorResponseException exception) {
        this.exception = exception;
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