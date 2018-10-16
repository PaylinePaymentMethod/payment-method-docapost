package com.payline.payment.docapost.bean.rest.response.signature;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.docapost.bean.rest.response.error.JsonErrorResponse;

import java.util.List;

/**
 * Created by Thales on 07/09/2018.
 */
public abstract class AbstractWSSignatureResponse {

    @SerializedName("errors")
    protected List<JsonErrorResponse> errors;

    public List<JsonErrorResponse> getErrors() {
        return errors;
    }

    /**
     * Check the response result
     *
     * @return true if result ok (no errors), false if result KO
     */
    public boolean isResultOk() {
        boolean result = true;
        if (this.errors != null && !this.errors.isEmpty()) {
            result = false;
        }
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();

        result.append("errors : \n");

        for (int index=0 ; index<errors.size() ; index++) {
            result.append(errors.toString() + "\n");
        }

        return result.toString();
    }

}