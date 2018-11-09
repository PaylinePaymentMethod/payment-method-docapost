package com.payline.payment.docapost.bean.rest.response.mandate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Thales on 08/10/2018.
 */
@XmlRootElement(name = "WSCTOrderDTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class WSCTOrderDTOResponse extends AbstractXmlResponse {

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

    @XmlElement(name = "creditorAccountName")
    private String creditorAccountName;

    @XmlElement(name = "status")
    private String status;

    @XmlElement(name = "statusDate")
    private String statusDate;

    @XmlElement(name = "amount")
    private Float amount;

    @XmlElement(name = "receiverName")
    private String receiverName;

    @XmlElement(name = "receiverIban")
    private String receiverIban;

    @XmlElement(name = "cancelDate")
    private String cancelDate;

    /**
     * Public default constructor
     */
    public WSCTOrderDTOResponse() {
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

    public String getCreditorAccountName() {
        return creditorAccountName;
    }

    public void setCreditorAccountName(String creditorAccountName) {
        this.creditorAccountName = creditorAccountName;
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

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverIban() {
        return receiverIban;
    }

    public void setReceiverIban(String receiverIban) {
        this.receiverIban = receiverIban;
    }

    public String getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(String cancelDate) {
        this.cancelDate = cancelDate;
    }

    //******************************************************************************************************************
    //***** BUILDER
    public static final class Builder {
        public WSCTOrderDTOResponse fromXml(String xmlContent) {
            return (WSCTOrderDTOResponse) parse(WSCTOrderDTOResponse.class, xmlContent);
        }
    }
    //***** BUILDER
    //******************************************************************************************************************

}