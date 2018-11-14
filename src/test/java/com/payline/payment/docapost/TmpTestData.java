package com.payline.payment.docapost;

public class TmpTestData {

    private static TmpTestData INSTANCE;

    public static final String AUTH_LOGIN = "payline@docapost.fr";
    public static final String AUTH_MDP = "J:[ef8dccma";

    protected String creditorId;
    protected String flowName;
    protected String rum;
    protected Boolean recurrent;
    protected String contextIdentifier;
    protected String language;
    protected String transactionId;
    protected String signatureId;
    protected String otp;
    protected Float amount;
    protected String label;
    protected Boolean signatureSuccess;

    protected String debtorLastName;
    protected String debtorFirstName;
    protected String debtorIban;
    protected String debtorStreet;
    protected String debtorComplement;
    protected String debtorComplement2;
    protected String debtorPostalCode;
    protected String debtorTown;
    protected String debtorPhoneNumber;
    protected String debtorCountryCode;

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
        this.debtorPhoneNumber = "06060606";
        this.debtorCountryCode = "FR";

    }

}