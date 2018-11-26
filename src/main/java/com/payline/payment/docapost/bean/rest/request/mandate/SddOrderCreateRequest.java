package com.payline.payment.docapost.bean.rest.request.mandate;

import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.payment.request.PaymentRequest;

import javax.xml.bind.annotation.*;
import java.util.Currency;
import java.util.Map;

import static com.payline.payment.docapost.utils.DocapostConstants.*;

/**
 * Created by Thales on 29/08/2018.
 */
@XmlRootElement(name = "WSDDOrderDTO")
@XmlType(
        propOrder = {
                "creditorId",
                "rum",
                "amount",
                "label",
                "e2eId"
        }
)
@XmlAccessorType(XmlAccessType.FIELD)
public class SddOrderCreateRequest extends AbstractXmlRequest {
    @XmlElement(name = "creditorId")
    private String creditorId;

    @XmlElement(name = "rum")
    private String rum;

    @XmlElement(name = "amount")
    private Float amount;

    @XmlElement(name = "label")
    private String label;

    @XmlElement(name = "e2eId")
    private String e2eId;

    /**
     * Constructor
     */
    public SddOrderCreateRequest(String creditorId,
                                 String rum,
                                 Float amount,
                                 String label,
                                 String e2eId) {

        this.creditorId = creditorId;
        this.rum = rum;
        this.amount = amount;
        this.label = label;
        this.e2eId = e2eId;

    }

    /**
     * Public default constructor
     */
    public SddOrderCreateRequest() {
        //ras
    }

    public String getCreditorId() {
        return creditorId;
    }

    public String getRum() {
        return rum;
    }

    public Float getAmount() {
        return amount;
    }

    public String getLabel() {
        return label;
    }

    public String getE2eId() {
        return e2eId;
    }

    //******************************************************************************************************************
    //***** BUILDER
    public static final class Builder {

        public SddOrderCreateRequest fromPaylineRequest(PaymentRequest paylineRequest) throws InvalidRequestException {

            // Check the input request for NPEs and mandatory fields
            this.checkInputRequest(paylineRequest);

            return new SddOrderCreateRequest(
                    paylineRequest.getContractConfiguration().getContractProperties().get(CONTRACT_CONFIG_CREDITOR_ID).getValue(),
                    paylineRequest.getRequestContext().getRequestData().get(CONTEXT_DATA_MANDATE_RUM),
                    paylineRequest.getAmount().getAmountInSmallestUnit().floatValue(),
                    paylineRequest.getSoftDescriptor(),
                    paylineRequest.getTransactionId()
            );


        }

        private void checkInputRequest(PaymentRequest paylineRequest) throws InvalidRequestException {

            if (paylineRequest == null) {
                throw new InvalidRequestException("Request must not be null");
            }
            checkContractConfiguration(paylineRequest);
            checkPaymentFormContext(paylineRequest);
            checkPartnerConfiguration(paylineRequest);
            checkRequestData(paylineRequest);

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
            if (paylineRequest.getSoftDescriptor() == null) {
                throw new InvalidRequestException("Missing mandatory property: soft descriptor");
            }
            if (paylineRequest.getTransactionId() == null) {
                throw new InvalidRequestException("Missing mandatory property: transaction id");
            }

            if (paylineRequest.getAmount() == null) {
                throw new InvalidRequestException("Missing mandatory property: amount");
            }
            if (paylineRequest.getAmount().getCurrency() != Currency.getInstance("EUR")) {
                throw new InvalidRequestException("Currency must be in euro");
            }
            if (paylineRequest.getOrder() == null) {
                throw new InvalidRequestException("Order object must not be null");
            }

        }

        private void checkRequestData(PaymentRequest paylineRequest) throws InvalidRequestException {
            if (paylineRequest.getRequestContext() == null
                    || paylineRequest.getRequestContext().getRequestData() == null) {
                throw new InvalidRequestException("Request context object must not be null");
            }
            Map<String, String> paymentRequestData = paylineRequest.getRequestContext().getRequestData();

            if (paymentRequestData.get(CONTEXT_DATA_MANDATE_RUM) == null) {
                throw new InvalidRequestException("Missing context data: mandate rum");
            }

        }

        private void checkPartnerConfiguration(PaymentRequest paylineRequest) throws InvalidRequestException {
            if (paylineRequest.getPartnerConfiguration() == null
                    || paylineRequest.getPartnerConfiguration().getSensitiveProperties() == null) {
                throw new InvalidRequestException("Partner configuration sensitive properties object must not be null");
            }
        }

    }


//***** BUILDER
//******************************************************************************************************************

}