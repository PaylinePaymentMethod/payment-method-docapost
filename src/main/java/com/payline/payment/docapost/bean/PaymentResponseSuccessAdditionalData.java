package com.payline.payment.docapost.bean;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.payline.payment.docapost.bean.rest.common.DocapostBean;

public class PaymentResponseSuccessAdditionalData extends DocapostBean {

    @SerializedName("mandateRum")
    private String mandateRum;

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("signatureId")
    private String signatureId;

    public PaymentResponseSuccessAdditionalData() {
        // ras.
    }

    public String getMandateRum() {
        return mandateRum;
    }

    public void setMandateRum(String mandateRum) {
        this.mandateRum = mandateRum;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSignatureId() {
        return signatureId;
    }

    public void setSignatureId(String signatureId) {
        this.signatureId = signatureId;
    }

    public PaymentResponseSuccessAdditionalData mandateRum(String mandateRum) {
        this.mandateRum = mandateRum;
        return this;
    }

    public PaymentResponseSuccessAdditionalData transactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public PaymentResponseSuccessAdditionalData signatureId(String signatureId) {
        this.signatureId = signatureId;
        return this;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    //******************************************************************************************************************
    //***** BUILDER
    public static final class Builder {
        public PaymentResponseSuccessAdditionalData fromJson(String jsonContent) {
            Gson gson = new Gson();
            return gson.fromJson(jsonContent, PaymentResponseSuccessAdditionalData.class);
        }
    }
    //***** BUILDER
    //******************************************************************************************************************

}