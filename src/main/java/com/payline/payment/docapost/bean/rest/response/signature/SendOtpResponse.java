package com.payline.payment.docapost.bean.rest.response.signature;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Thales on 29/08/2018.
 */
public class SendOtpResponse extends AbstractWSSignatureResponse {

    @SerializedName("signatureID")
    private String signatureId;

    /**
     * Constructor
     */
    public SendOtpResponse(String signatureId) {
        this.signatureId = signatureId;
    }

    public String getSignatureId() {
        return signatureId;
    }


    //******************************************************************************************************************
    //***** BUILDER
    public static final class Builder {
        public SendOtpResponse fromJson(String jsonContent) {
            Gson gson = new Gson();
            return gson.fromJson(jsonContent, SendOtpResponse.class);
        }
    }
    //***** BUILDER
    //******************************************************************************************************************

}