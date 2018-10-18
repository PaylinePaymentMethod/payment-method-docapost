package com.payline.payment.docapost.bean.rest.request.mandate;

import com.payline.payment.docapost.TmpTestData;
import com.payline.payment.docapost.bean.rest.common.Debtor;
import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.utils.DocapostUtils;
import com.payline.pmapi.bean.common.Buyer;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.payment.request.PaymentRequest;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Map;

import static com.payline.payment.docapost.utils.DocapostConstants.*;

/**
 * Created by Thales on 29/08/2018.
 * Cf. http://blog.paumard.org/cours/jaxb-rest/chap02-jaxb-annotations.html §3.3.3
 */
@XmlRootElement(name = "WSMandateDTO")
@XmlType(
        propOrder = {
                "creditorId",
                "rum",
                "recurrent",
                "language",
                "debtor"
        }
)
@XmlAccessorType(XmlAccessType.FIELD)
public class MandateCreateRequest extends AbstractXmlRequest {

    @XmlElement(name = "creditorId")
    private String creditorId;

    @XmlElement(name = "rum")
    private String rum;

    @XmlElement(name = "recurrent")
    private Boolean recurrent;

    @XmlElement(name = "language")
    private String language;

    @XmlElement(name = "debtor")
    private Debtor debtor;

    /**
     * Public default constructor
     */
    public MandateCreateRequest() { }

    /**
     * Constructor
     */
    public MandateCreateRequest(String creditorId,
                                String rum,
                                String language,
                                Debtor debtor) {

        this.creditorId             = creditorId;
        this.rum                    = rum;
        this.recurrent              = false;
        this.language               = language;
        this.debtor                 = debtor;

    }

    /**
     * Constructor
     */
    public MandateCreateRequest(String creditorId,
                                String rum,
                                Boolean recurrent,
                                String language,
                                Debtor debtor) {

        this.creditorId             = creditorId;
        this.rum                    = rum;
        this.recurrent              = recurrent;
        this.language               = language;
        this.debtor                 = debtor;

    }

    public String getCreditorId() {
        return creditorId;
    }

    public String getRum() {
        return rum;
    }

    public boolean isRecurrent() {
        return recurrent;
    }

    public String getLanguage() {
        return language;
    }

    public Debtor getDebtor() {
        return debtor;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();

        result.append("***** MandateCreateRequest info\n");

        result.append("creditorId : " + creditorId + "\n");
        result.append("rum : " + rum + "\n");
        result.append("recurrent : " + recurrent + "\n");
        result.append("language : " + language + "\n");
        result.append(debtor.toString() + "\n");

        return result.toString();
    }

    //******************************************************************************************************************
    //***** BUILDER
    public static final class Builder {

        public MandateCreateRequest fromPaylineRequest(PaymentRequest paylineRequest) throws InvalidRequestException {

            // Check the input request for NPEs and mandatory fields
            this.checkInputRequest(paylineRequest);

            Debtor debtor = new Debtor()
                    .lastName(paylineRequest.getBuyer().getFullName().getLastName())
                    .firstName(paylineRequest.getBuyer().getFullName().getFirstName())
                    .iban(paylineRequest.getPaymentFormContext().getSensitivePaymentFormParameter().get( FORM_FIELD__IBAN ))
                    .street(paylineRequest.getBuyer().getAddresses().get( Buyer.AddressType.DELIVERY ).getStreet1())
                    .postalCode(paylineRequest.getBuyer().getAddresses().get( Buyer.AddressType.DELIVERY ).getZipCode())
                    .town(paylineRequest.getBuyer().getAddresses().get( Buyer.AddressType.DELIVERY ).getCity())
                    .phoneNumber(paylineRequest.getPaymentFormContext().getPaymentFormParameter().get( FORM_FIELD__PHONE ))
                    .countryCode(paylineRequest.getBuyer().getAddresses().get( Buyer.AddressType.DELIVERY ).getCountry());

            MandateCreateRequest request = new MandateCreateRequest(
                    paylineRequest.getContractConfiguration().getContractProperties().get( CONTRACT_CONFIG__CREDITOR_ID ).getValue(),
                    DocapostUtils.generateMandateRum(),
                    paylineRequest.getLocale().getLanguage(),
                    debtor
            );

            return request;

        }

        private void checkInputRequest(PaymentRequest paylineRequest) throws InvalidRequestException  {
            if ( paylineRequest == null ) {
                throw new InvalidRequestException( "Request must not be null" );
            }

            if ( paylineRequest.getContractConfiguration() == null
                    || paylineRequest.getContractConfiguration().getContractProperties() == null ) {
                throw new InvalidRequestException( "Contract configuration properties object must not be null" );
            }
            Map<String, ContractProperty> contractProperties = paylineRequest.getContractConfiguration().getContractProperties();
            if ( contractProperties.get( CONTRACT_CONFIG__CREDITOR_ID ) == null ) {
                throw new InvalidRequestException( "Missing contract configuration property: creditor id" );
            }

            if ( paylineRequest.getPaymentFormContext() == null
                || paylineRequest.getPaymentFormContext().getPaymentFormParameter() == null
                    || paylineRequest.getPaymentFormContext().getSensitivePaymentFormParameter() == null ) {
                throw new InvalidRequestException( "Payment form context object must not be null" );
            }
            Map<String, String> paymentFormParameter = paylineRequest.getPaymentFormContext().getPaymentFormParameter();
            Map<String, String> sensitivePaymentFormParameter = paylineRequest.getPaymentFormContext().getSensitivePaymentFormParameter();
            if ( sensitivePaymentFormParameter.get( FORM_FIELD__IBAN ) == null ) {
                throw new InvalidRequestException( "Missing payment form context data: form debtor iban" );
            }
            if ( paymentFormParameter.get( FORM_FIELD__PHONE ) == null ) {
                throw new InvalidRequestException( "Missing payment form context data: form debtor phone" );
            }

            if ( paylineRequest.getPartnerConfiguration() == null
                    || paylineRequest.getPartnerConfiguration().getSensitiveProperties() == null ) {
                throw new InvalidRequestException( "Partner configuration sensitive properties object must not be null" );
            }
            Map<String, String> sensitiveProperties = paylineRequest.getPartnerConfiguration().getSensitiveProperties();
            if ( sensitiveProperties.get( PARTNER_CONFIG__AUTH_LOGIN ) == null ) {
                throw new InvalidRequestException( "Missing partner configuration property: auth login" );
            }
            if ( sensitiveProperties.get( PARTNER_CONFIG__AUTH_PASS ) == null ) {
                throw new InvalidRequestException( "Missing partner configuration property: auth pass" );
            }

            if ( paylineRequest.getBuyer() == null
                    || paylineRequest.getBuyer().getAddresses() == null ) {
                throw new InvalidRequestException( "Buyer address object must not be null" );
            }
            Map<Buyer.AddressType, Buyer.Address> addresses = paylineRequest.getBuyer().getAddresses();
            Buyer.Address address = addresses.get( Buyer.AddressType.DELIVERY );
            if ( addresses.get( Buyer.AddressType.DELIVERY ) == null ) {
                throw new InvalidRequestException( "Missing buyer address property: delivery address" );
            }

            if ( paylineRequest.getBuyer() == null
                    || paylineRequest.getBuyer().getFullName() == null ) {
                throw new InvalidRequestException( "Buyer full name object must not be null" );
            }

        }

    }
    //***** BUILDER
    //******************************************************************************************************************

}