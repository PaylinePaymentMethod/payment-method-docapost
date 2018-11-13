package com.payline.payment.docapost.bean.rest.common;

import javax.xml.bind.annotation.*;

/**
 * Created by Thales on 04/09/2018.
 */
@XmlRootElement(name = "Debtor")
@XmlType(
        propOrder = {
                "lastName",
                "firstName",
                "bic",
                "iban",
                "street",
                "complement",
                "complement2",
                "postalCode",
                "town",
                "phoneNumber",
                "countryCode"
        }
)
@XmlAccessorType(XmlAccessType.FIELD)
public class Debtor extends DocapostBean {

    @XmlElement(name = "lastName")
    private String lastName;

    @XmlElement(name = "firstName")
    private String firstName;

    @XmlElement(name = "bic")
    private String bic;

    @XmlElement(name = "iban")
    private String iban;

    @XmlElement(name = "street")
    private String street;

    @XmlElement(name = "complement")
    private String complement;

    @XmlElement(name = "complement2")
    private String complement2;

    @XmlElement(name = "postalCode")
    private String postalCode;

    @XmlElement(name = "town")
    private String town;

    @XmlElement(name = "phoneNumber")
    private String phoneNumber;

    @XmlElement(name = "countryCode")
    private String countryCode;

    /**
     * Public default constructor
     */
    public Debtor() {
        // ras.
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getBic() {
        return bic;
    }

    public String getIban() {
        return iban;
    }

    public String getStreet() {
        return street;
    }

    public String getComplement() {
        return complement;
    }

    public String getComplement2() {
        return complement2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getTown() {
        return town;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Debtor lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Debtor firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Debtor bic(String bic) {
        this.bic = bic;
        return this;
    }

    public Debtor iban(String iban) {
        this.iban = iban;
        return this;
    }

    public Debtor street(String street) {
        this.street = street;
        return this;
    }

    public Debtor complement(String complement) {
        this.complement = complement;
        return this;
    }

    public Debtor complement2(String complement2) {
        this.complement2 = complement2;
        return this;
    }

    public Debtor postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public Debtor town(String town) {
        this.town = town;
        return this;
    }

    public Debtor phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public Debtor countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }
}