package com.payline.payment.docapost.bean.rest.response.signature;

import com.google.gson.Gson;

/**
 * Created by Thales on 07/09/2018.
 */
public class TerminateSignatureResponse extends AbstractWSSignatureResponse {

    /**
     * Constructor
     */
    public TerminateSignatureResponse() {
        //ras.
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();

        result.append("***** TerminateSignatureResponse info\n");

        if (errors != null && !errors.isEmpty()) {
            result.append(errors.toString() + "\n");
        }

        return result.toString();
    }

    //******************************************************************************************************************
    //***** BUILDER
    public static final class Builder {
        public TerminateSignatureResponse fromJson(String jsonContent) {
            Gson gson = new Gson();
            return gson.fromJson(jsonContent, TerminateSignatureResponse.class);
        }
    }
    //***** BUILDER
    //******************************************************************************************************************

}