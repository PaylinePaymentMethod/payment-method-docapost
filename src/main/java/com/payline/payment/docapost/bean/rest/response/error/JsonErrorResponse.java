package com.payline.payment.docapost.bean.rest.response.error;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.docapost.bean.rest.common.DocapostBean;

/**
 * Created by Thales on 07/09/2018.
 */
public class JsonErrorResponse extends DocapostBean {

    @SerializedName("reason")
    private String reason;

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public String getReason() {
        return reason;
    }


}