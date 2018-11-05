package com.payline.payment.docapost.bean.rest.response.error;

import javax.xml.bind.annotation.*;

/**
 * Created by Thales on 04/09/2018.
 */
@XmlRootElement(name = "exception")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlErrorResponseException {

    @XmlValue
    private String value;

    @XmlAttribute
    private String code;

    /**
     * Public default constructor
     */
    public XmlErrorResponseException() {
        // ras.
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();

        result.append("value : " + value + "\n");
        result.append("code : " + code + "\n");

        return result.toString();
    }

}