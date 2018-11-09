package com.payline.payment.docapost.bean.rest.response.signature;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Thales on 29/08/2018.
 */
public class InitiateSignatureResponse extends AbstractWSSignatureResponse {

    @SerializedName("transactionId")
    private String transactionId;

    /**
     * Constructor
     */
    public InitiateSignatureResponse(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }


    //******************************************************************************************************************
    //***** BUILDER
    public static final class Builder {
        public InitiateSignatureResponse fromJson(String jsonContent) {
            Gson gson = new Gson();
            return gson.fromJson(jsonContent, InitiateSignatureResponse.class);
        }
    }
    //***** BUILDER
    //******************************************************************************************************************

}