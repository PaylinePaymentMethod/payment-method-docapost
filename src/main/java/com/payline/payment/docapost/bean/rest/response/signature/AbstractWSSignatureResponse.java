package com.payline.payment.docapost.bean.rest.response.signature;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.docapost.bean.rest.common.DocapostBean;
import com.payline.payment.docapost.bean.rest.response.error.JsonErrorResponse;

import java.util.List;

/**
 * Created by Thales on 07/09/2018.
 */
public abstract class AbstractWSSignatureResponse extends DocapostBean {

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

        return !(this.errors != null && !this.errors.isEmpty());
    }


}