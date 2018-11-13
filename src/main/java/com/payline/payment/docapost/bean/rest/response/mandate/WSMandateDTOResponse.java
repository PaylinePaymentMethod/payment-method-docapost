package com.payline.payment.docapost.bean.rest.response.mandate;

import com.payline.payment.docapost.bean.rest.common.Debtor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Thales on 29/08/2018.
 */
@XmlRootElement(name = "WSMandateDTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class WSMandateDTOResponse extends AbstractXmlResponse {

    @XmlElement(name = "creditorId")
    private String creditorId;

    @XmlElement(name = "creditorIcs")
    private String creditorIcs;

    @XmlElement(name = "rum")
    private String rum;

    @XmlElement(name = "recurrent")
    private Boolean recurrent;

    @XmlElement(name = "status")
    private String status;

    @XmlElement(name = "debtor")
    private Debtor debtor;

    @XmlElement(name = "mode")
    private String mode;

    @XmlElement(name = "flowName")
    private String flowName;

    @XmlElement(name = "contextIdentifier")
    private String contextIdentifier;

    @XmlElement(name = "language")
    private String language;

    /**
     * Public default constructor
     */
    public WSMandateDTOResponse() {
        // ras.
    }

    public String getCreditorId() {
        return creditorId;
    }

    public String getCreditorIcs() {
        return creditorIcs;
    }

    public String getRum() {
        return rum;
    }

    public Boolean getRecurrent() {
        return recurrent;
    }

    public String getStatus() {
        return status;
    }

    public Debtor getDebtor() {
        return debtor;
    }

    public String getMode() {
        return mode;
    }

    public String getFlowName() {
        return flowName;
    }

    public String getContextIdentifier() {
        return contextIdentifier;
    }

    public String getLanguage() {
        return language;
    }

    //******************************************************************************************************************
    //***** BUILDER
    public static final class Builder {
        public static WSMandateDTOResponse fromXml(String xmlContent) {
            return (WSMandateDTOResponse) parse(WSMandateDTOResponse.class, xmlContent);
        }
    }
    //***** BUILDER
    //******************************************************************************************************************

}