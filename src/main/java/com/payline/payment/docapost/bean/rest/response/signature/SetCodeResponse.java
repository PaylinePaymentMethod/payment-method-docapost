package com.payline.payment.docapost.bean.rest.response.signature;

import com.google.gson.Gson;

/**
 * Created by Thales on 29/08/2018.
 */
public class SetCodeResponse extends AbstractWSSignatureResponse {

    /**
     * Constructor
     */
    public SetCodeResponse() { }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();

        result.append("***** SetCodeResponse info\n");

        if (errors != null && !errors.isEmpty()) {
            result.append(errors.toString() + "\n");
        }

        return result.toString();
    }

    //******************************************************************************************************************
    //***** BUILDER
    public static final class Builder {
        public SetCodeResponse fromJson(String jsonContent ) {
            Gson gson = new Gson();
            return gson.fromJson( jsonContent, SetCodeResponse.class );
        }
    }
    //***** BUILDER
    //******************************************************************************************************************

}