package com.payline.payment.docapost;

public class TmpTestData {

    private static TmpTestData INSTANCE;

    public static final String AUTH_LOGIN = "payline@docapost.fr";
    public static final String AUTH_MDP = "J:[ef8dccma";

    private String creditorId;
    private String flowName;
    private String rum;
    private Boolean recurrent;
    private String contextIdentifier;
    private String language;
    private String transactionId;
    private String signatureId;
    private String otp;
    private Float amount;
    private String label;
    private Boolean signatureSuccess;

    private String debtorLastName;
    private String debtorFirstName;
    private String debtorIban;
    private String debtorStreet;
    private String debtorComplement;
    private String debtorComplement2;
    private String debtorPostalCode;
    private String debtorTown;
    private String debtorPhoneNumber;
    private String debtorCountryCode;

    public static synchronized TmpTestData getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TmpTestData();
        }
        return INSTANCE;
    }

    private TmpTestData() {
        this.initData();
    }

    private void initData() {

        this.creditorId = "MARCHAND1";
        this.flowName = null;
        this.rum = "RUM123ZXR987";
        this.recurrent = false;
        this.contextIdentifier = null;
        this.language = "fr";
        this.transactionId = null;
        this.signatureId = null;
        this.otp = null;
        this.amount = 100f;
        this.label = "A simple order";
        this.signatureSuccess = null;

        this.debtorLastName = "Nicolas";
        this.debtorFirstName = "MICHNIEWSKI";
        this.debtorIban = "FR7630076020821234567890186";
        this.debtorStreet = "25 rue Gambetta";
        this.debtorComplement = null;
        this.debtorComplement2 = null;
        this.debtorPostalCode = "13130";
        this.debtorTown = "Berre";
//        this.debtorPhoneNumber = "0628692878";
        this.debtorPhoneNumber = "0623737719";
        this.debtorCountryCode = "FR";

    }

}