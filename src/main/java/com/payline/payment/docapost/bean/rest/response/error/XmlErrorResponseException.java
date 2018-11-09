package com.payline.payment.docapost.bean.rest.response.error;

import com.google.gson.Gson;

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

        Gson gson = new Gson();
        return gson.toJson(this);
    }

}