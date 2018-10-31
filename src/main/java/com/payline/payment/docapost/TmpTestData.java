package com.payline.payment.docapost;

public class TmpTestData {

    private static TmpTestData INSTANCE;

    public static final String AUTH_LOGIN = "payline@docapost.fr";
    public static final String AUTH_MDP = "J:[ef8dccma";

    public String   creditorId;
    public String   flowName;
    public String   rum;
    public Boolean  recurrent;
    public String   contextIdentifier;
    public String   language;
    public String   transactionId;
    public String   signatureId;
    public String   otp;
    public Float    amount;
    public String   label;
    public Boolean  signatureSuccess;

    public String   debtorLastName;
    public String   debtorFirstName;
    public String   debtorIban;
    public String   debtorStreet;
    public String   debtorComplement;
    public String   debtorComplement2;
    public String   debtorPostalCode;
    public String   debtorTown;
    public String   debtorPhoneNumber;
    public String   debtorCountryCode;

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
        this.recurrent = new Boolean(false);
        this.contextIdentifier = null;
        this.language = "fr";
        this.transactionId = null;
        this.signatureId = null;
        this.otp = null;
        this.amount = new Float(100);
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