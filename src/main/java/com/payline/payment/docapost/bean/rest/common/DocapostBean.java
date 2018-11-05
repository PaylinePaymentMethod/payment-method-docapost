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
public class Debtor {

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
    public Debtor() { }

    /**

     * Constructor
     */
    public Debtor(String lastName,
                  String firstName,
                  String bic,
                  String iban,
                  String street,
                  String complement,
                  String complement2,
                  String postalCode,
                  String town,
                  String phoneNumber,
                  String countryCode) {

        this.lastName       = lastName;
        this.firstName      = firstName;
        this.bic            = bic;
        this.iban           = iban;
        this.street         = street;
        this.complement     = complement;
        this.complement2    = complement2;
        this.postalCode     = postalCode;
        this.town           = town;
        this.phoneNumber    = phoneNumber;
        this.countryCode    = countryCode;

    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getComplement2() {
        return complement2;
    }

    public void setComplement2(String complement2) {
        this.complement2 = complement2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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

    @Override
    public String toString() {


        final StringBuilder result = new StringBuilder();

        result.append("debtor.lastName : " + lastName + "\n");
        result.append("debtor.firstName : " + firstName + "\n");
        result.append("debtor.bic : " + bic + "\n");
        result.append("debtor.iban : " + iban + "\n");
        result.append("debtor.street : " + street + "\n");
        result.append("debtor.complement : " + complement + "\n");
        result.append("debtor.complement2 : " + complement2 + "\n");
        result.append("debtor.postalCode : " + postalCode + "\n");
        result.append("debtor.town : " + town + "\n");
        result.append("debtor.phoneNumber : " + phoneNumber + "\n");
        result.append("debtor.countryCode : " + countryCode + "\n");

        return result.toString();
    }

}