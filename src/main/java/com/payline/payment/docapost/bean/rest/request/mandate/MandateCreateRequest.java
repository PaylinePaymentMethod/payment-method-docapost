package com.payline.payment.docapost.bean.rest.request.mandate;

import com.payline.payment.docapost.bean.rest.common.Debtor;
import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.utils.DocapostUtils;
import com.payline.pmapi.bean.common.Buyer;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.payment.request.PaymentRequest;

import javax.xml.bind.annotation.*;
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
    private Boolean recurrent = Boolean.FALSE;

    @XmlElement(name = "language")
    private String language;

    @XmlElement(name = "debtor")
    private Debtor debtor;

    /**
     * Public default constructor
     */
    public MandateCreateRequest() {
    }

    /**
     * Constructor
     */
    public MandateCreateRequest(String creditorId,
                                String rum,
                                Boolean recurrent,
                                String language,
                                Debtor debtor) {

        this.creditorId = creditorId;
        this.rum = rum;
        if (recurrent != null) {
            this.recurrent = recurrent;
        }
        this.language = language;
        this.debtor = debtor;

    }

    public String getCreditorId() {
        return creditorId;
    }

    public String getRum() {
        return rum;
    }

    public Boolean isRecurrent() {
        return recurrent;
    }

    public String getLanguage() {
        return language;
    }

    public Debtor getDebtor() {
        return debtor;
    }


    //******************************************************************************************************************
    //***** BUILDER
    public static final class Builder {

        public MandateCreateRequest fromPaylineRequest(PaymentRequest paylineRequest) throws InvalidRequestException {

            // Check the input request for NPEs and mandatory fields
            this.checkInputRequest(paylineRequest);

            Buyer buyer = paylineRequest.getBuyer();
            Debtor debtor = new Debtor()
                    .lastName(buyer.getFullName().getLastName())
                    .firstName(buyer.getFullName().getFirstName())
                    .street(buyer.getAddresses().get(Buyer.AddressType.DELIVERY).getStreet1())
                    .postalCode(buyer.getAddresses().get(Buyer.AddressType.DELIVERY).getZipCode())
                    .town(buyer.getAddresses().get(Buyer.AddressType.DELIVERY).getCity())
                    .countryCode(buyer.getAddresses().get(Buyer.AddressType.DELIVERY).getCountry())
                    .phoneNumber(paylineRequest.getPaymentFormContext().getPaymentFormParameter().get(FORM_FIELD_PHONE))
                    .iban(paylineRequest.getPaymentFormContext().getSensitivePaymentFormParameter().get(FORM_FIELD_IBAN));

            return new MandateCreateRequest(
                    paylineRequest.getContractConfiguration().getContractProperties().get(CONTRACT_CONFIG_CREDITOR_ID).getValue(),
                    DocapostUtils.generateMandateRum(),
                    null,
                    paylineRequest.getLocale().getLanguage(),
                    debtor
            );

        }

        private void checkInputRequest(PaymentRequest paylineRequest) throws InvalidRequestException {
            if (paylineRequest == null) {
                throw new InvalidRequestException("Request must not be null");
            }

            checkContractConfiguration(paylineRequest);

            checkPaymentFormContext(paylineRequest);

            checkPartnerConfiguration(paylineRequest);

            checkBuyer(paylineRequest);

        }

        private void checkContractConfiguration(PaymentRequest paylineRequest) throws InvalidRequestException {
            if (paylineRequest.getContractConfiguration() == null
                    || paylineRequest.getContractConfiguration().getContractProperties() == null) {
                throw new InvalidRequestException("Contract configuration properties object must not be null");
            }
            Map<String, ContractProperty> contractProperties = paylineRequest.getContractConfiguration().getContractProperties();
            if (contractProperties.get(CONTRACT_CONFIG_CREDITOR_ID) == null) {
                throw new InvalidRequestException("Missing contract configuration property: creditor id");
            }
            if (contractProperties.get(PARTNER_CONFIG_AUTH_LOGIN) == null) {
                throw new InvalidRequestException("Missing contract configuration property: auth login");
            }
            if (contractProperties.get(PARTNER_CONFIG_AUTH_PASS) == null) {
                throw new InvalidRequestException("Missing contract configuration property: auth pass");
            }
        }

        private void checkPaymentFormContext(PaymentRequest paylineRequest) throws InvalidRequestException {

            if (paylineRequest.getPaymentFormContext() == null
                    || paylineRequest.getPaymentFormContext().getPaymentFormParameter() == null
                    || paylineRequest.getPaymentFormContext().getSensitivePaymentFormParameter() == null) {
                throw new InvalidRequestException("Payment form context object must not be null");
            }
            Map<String, String> paymentFormParameter = paylineRequest.getPaymentFormContext().getPaymentFormParameter();
            Map<String, String> sensitivePaymentFormParameter = paylineRequest.getPaymentFormContext().getSensitivePaymentFormParameter();
            if (sensitivePaymentFormParameter.get(FORM_FIELD_IBAN) == null) {
                throw new InvalidRequestException("Missing payment form context data: form debtor iban");
            }
            if (paymentFormParameter.get(FORM_FIELD_PHONE) == null) {
                throw new InvalidRequestException("Missing payment form context data: form debtor phone");
            }
        }

        private void checkPartnerConfiguration(PaymentRequest paylineRequest) throws InvalidRequestException {
            if (paylineRequest.getPartnerConfiguration() == null
                    || paylineRequest.getPartnerConfiguration().getSensitiveProperties() == null) {
                throw new InvalidRequestException("Partner configuration sensitive properties object must not be null");
            }
        }

        private void checkBuyer(PaymentRequest paylineRequest) throws InvalidRequestException {
            if (paylineRequest.getBuyer() == null) {
                throw new InvalidRequestException("Buyer object must not be null");
            }
            if (paylineRequest.getBuyer().getAddresses() == null) {
                throw new InvalidRequestException("Buyer address object must not be null");
            }

            if (paylineRequest.getBuyer().getFullName() == null) {
                throw new InvalidRequestException("Buyer full name object must not be null");
            }

            Map<Buyer.AddressType, Buyer.Address> addresses = paylineRequest.getBuyer().getAddresses();

            if (addresses.get(Buyer.AddressType.DELIVERY) == null) {
                throw new InvalidRequestException("Missing buyer address property: delivery address");
            }
        }

    }
    //***** BUILDER
    //******************************************************************************************************************

}