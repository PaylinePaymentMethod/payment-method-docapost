package com.payline.payment.docapost.utils;

public class DocapostLocalParam {

    private static DocapostLocalParam INSTANCE;

    private String      mandateRum;
    private String      transactionId;
    private String      signatureId;
    private Boolean     signatureSuccess;
    private String      orderStatus;

    private DocapostLocalParam() { }

    public static synchronized DocapostLocalParam getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DocapostLocalParam();
        }
        return INSTANCE;
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

}