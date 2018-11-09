package com.payline.payment.docapost.bean.rest.request.signature;

import com.payline.payment.docapost.bean.rest.common.DocapostBean;

/**
 * Created by Thales on 05/09/2018.
 */
public abstract class WSSignatureRequest extends DocapostBean {

    protected String creditorId;

    protected String mandateRum;

    public WSSignatureRequest(String creditorId,
                              String mandateRum) {

        this.creditorId = creditorId;
        this.mandateRum = mandateRum;

    }

    public String getCreditorId() {
        return creditorId;
    }

    public String getMandateRum() {
        return mandateRum;
    }

}