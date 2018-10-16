package com.payline.payment.docapost.bean;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class PaymentResponseSuccessAdditionalData {

    @SerializedName("mandateRum")
    private String mandateRum;

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("signatureId")
    private String signatureId;

    public PaymentResponseSuccessAdditionalData() { }

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

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();

        result.append("***** PaymentResponseSuccessAdditionalData info\n");

        result.append("mandateRum : " + mandateRum + "\n");
        result.append("transactionId : " + transactionId + "\n");
        result.append("signatureId : " + signatureId + "\n");

        return result.toString();
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    //******************************************************************************************************************
    //***** BUILDER
    public static final class Builder {
        public PaymentResponseSuccessAdditionalData fromJson( String jsonContent ) {
            Gson gson = new Gson();
            return gson.fromJson( jsonContent, PaymentResponseSuccessAdditionalData.class );
        }
    }
    //***** BUILDER
    //******************************************************************************************************************

}