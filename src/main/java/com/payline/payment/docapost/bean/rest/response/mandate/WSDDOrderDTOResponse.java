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

    public String getReference() {
        return reference;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getE2eId() {
        return e2eId;
    }

    public String getRemitDate() {
        return remitDate;
    }

    public String getSequence() {
        return sequence;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getRum() {
        return rum;
    }

    public String getCreditorId() {
        return creditorId;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public Float getAmount() {
        return amount;
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