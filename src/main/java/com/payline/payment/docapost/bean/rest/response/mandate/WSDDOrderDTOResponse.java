package com.payline.payment.docapost.bean.rest.response.mandate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Thales on 29/08/2018.
 */
@XmlRootElement(name = "WSDDOrderDTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class WSDDOrderDTOResponse extends AbstractXmlResponse {

    @XmlElement(name = "label")
    private String label;

    @XmlElement(name = "reference")
    private String reference;

    @XmlElement(name = "dueDate")
    private String dueDate;

    @XmlElement(name = "e2eId")
    private String e2eId;

    @XmlElement(name = "remitDate")
    private String remitDate;

    @XmlElement(name = "sequence")
    private String sequence;

    @XmlElement(name = "identifier")
    private String identifier;

    @XmlElement(name = "rum")
    private String rum;

    @XmlElement(name = "creditorId")
    private String creditorId;

    @XmlElement(name = "status")
    private String status;

    @XmlElement(name = "statusDate")
    private String statusDate;

    @XmlElement(name = "amount")
    private Float amount;

    /**
     * Public default constructor
     */
    public WSDDOrderDTOResponse() {
        // ras.
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getE2eId() {
        return e2eId;
    }

    public void setE2eId(String e2eId) {
        this.e2eId = e2eId;
    }

    public String getRemitDate() {
        return remitDate;
    }

    public void setRemitDate(String remitDate) {
        this.remitDate = remitDate;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getRum() {
        return rum;
    }

    public void setRum(String rum) {
        this.rum = rum;
    }

    public String getCreditorId() {
        return creditorId;
    }

    public void setCreditorId(String creditorId) {
        this.creditorId = creditorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();

        result.append("***** WSDDOrderDTOResponse info\n");

        result.append("label : " + label + "\n");
        result.append("reference : " + reference + "\n");
        result.append("dueDate : " + dueDate + "\n");
        result.append("e2eId : " + e2eId + "\n");
        result.append("remitDate : " + remitDate + "\n");
        result.append("sequence : " + sequence + "\n");
        result.append("identifier : " + identifier + "\n");
        result.append("rum : " + rum + "\n");
        result.append("creditorId : " + creditorId + "\n");
        result.append("status : " + status + "\n");
        result.append("statusDate : " + statusDate + "\n");
        result.append("amount : " + amount + "\n");

        return result.toString();
    }

    //******************************************************************************************************************
    //***** BUILDER
    public static final class Builder {
        public WSDDOrderDTOResponse fromXml(String xmlContent) {
            return (WSDDOrderDTOResponse) parse(WSDDOrderDTOResponse.class, xmlContent);
        }
    }
    //***** BUILDER
    //******************************************************************************************************************

}