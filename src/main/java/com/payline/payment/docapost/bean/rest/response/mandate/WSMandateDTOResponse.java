package com.payline.payment.docapost.bean.rest.response.mandate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.payline.payment.docapost.bean.rest.common.Debtor;

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
    public WSMandateDTOResponse() { }

    public String getCreditorId() {
        return creditorId;
    }

    public void setCreditorId(String creditorId) {
        this.creditorId = creditorId;
    }

    public String getCreditorIcs() {
        return creditorIcs;
    }

    public void setCreditorIcs(String creditorIcs) {
        this.creditorIcs = creditorIcs;
    }

    public String getRum() {
        return rum;
    }

    public void setRum(String rum) {
        this.rum = rum;
    }

    public Boolean getRecurrent() {
        return recurrent;
    }

    public void setRecurrent(Boolean recurrent) {
        this.recurrent = recurrent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Debtor getDebtor() {
        return debtor;
    }

    public void setDebtor(Debtor debtor) {
        this.debtor = debtor;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getContextIdentifier() {
        return contextIdentifier;
    }

    public void setContextIdentifier(String contextIdentifier) {
        this.contextIdentifier = contextIdentifier;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();

        result.append("***** WSMandateDTOResponse info\n");

        result.append("creditorId : " + creditorId + "\n");
        result.append("creditorIcs : " + creditorIcs + "\n");
        result.append("rum : " + rum + "\n");
        result.append("recurrent : " + recurrent + "\n");
        result.append("status : " + status + "\n");
        result.append("mode : " + mode + "\n");
        result.append("flowName : " + flowName + "\n");
        result.append("contextIdentifier : " + contextIdentifier + "\n");
        result.append("language : " + language + "\n");

        result.append(debtor.toString() + "\n");

        return result.toString();
    }

    //******************************************************************************************************************
    //***** BUILDER
    public static final class Builder {
        public WSMandateDTOResponse fromXml(String xmlContent) {
            return (WSMandateDTOResponse) parse(WSMandateDTOResponse.class, xmlContent);
        }
    }
    //***** BUILDER
    //******************************************************************************************************************

}