package com.payline.payment.docapost.utils;

import com.payline.pmapi.bean.payment.request.PaymentRequest;

import static com.payline.payment.docapost.utils.DocapostConstants.*;

public class DocapostLocalParam {

    private String mandateRum;
    private String transactionId;
    private String signatureId;
    private Boolean signatureSuccess;
    private String orderStatus;

    private DocapostLocalParam() {
    }

    private static class SingletonHolder {
        private static final DocapostLocalParam INSTANCE = new DocapostLocalParam();
    }

    /**
     * @return the singleton instance
     */
    public static DocapostLocalParam getInstance() {
        return SingletonHolder.INSTANCE;
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

    public Boolean getSignatureSuccess() {
        return signatureSuccess;
    }

    public void setSignatureSuccess(Boolean signatureSuccess) {
        this.signatureSuccess = signatureSuccess;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void restoreFromPaylineRequest(PaymentRequest request) {

        String requestContextMandateRum = request.getRequestContext().getRequestData().get(CONTEXT_DATA_MANDATE_RUM);
        String requestContextTransactionId = request.getRequestContext().getRequestData().get(CONTEXT_DATA_TRANSACTION_ID);
        String requestContextSignatureId = request.getRequestContext().getRequestData().get(CONTEXT_DATA_SIGNATURE_ID);

        if (!PluginUtils.isEmpty(requestContextMandateRum)) {
            this.mandateRum = requestContextMandateRum;
        }

        if (!PluginUtils.isEmpty(requestContextTransactionId)) {
            this.transactionId = requestContextTransactionId;
        }

        if (!PluginUtils.isEmpty(requestContextSignatureId)) {
            this.signatureId = requestContextSignatureId;
        }

    }

}