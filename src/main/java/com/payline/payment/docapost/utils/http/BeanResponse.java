package com.payline.payment.docapost.utils.http;

import com.payline.payment.docapost.bean.rest.common.DocapostBean;

public abstract class BeanResponse extends DocapostBean {

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}