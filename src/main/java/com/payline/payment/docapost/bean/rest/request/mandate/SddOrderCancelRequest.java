package com.payline.payment.docapost.bean.rest.request.mandate;

import com.payline.payment.docapost.bean.PaymentResponseSuccessAdditionalData;
import com.payline.payment.docapost.bean.rest.common.DocapostBean;
import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.utils.PluginUtils;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.reset.request.ResetRequest;

import java.util.Map;

import static com.payline.payment.docapost.utils.DocapostConstants.*;

public class SddOrderCancelRequest extends DocapostBean {

    private String creditorId;

    private String rum;

    private String e2eId;

    /**
     * Public default constructor
     */
    public SddOrderCancelRequest() {
    }

    /**
     * Constructor
     */
    public SddOrderCancelRequest(String creditorId,
                                 String rum,
                                 String e2eId) {

        this.creditorId = creditorId;
        this.rum = rum;
        this.e2eId = e2eId;

    }

    public String getCreditorId() {
        return creditorId;
    }

    public String getRum() {
        return rum;
    }

    public String getE2eId() {
        return e2eId;
    }


    //******************************************************************************************************************
    //***** BUILDER
    public static final class Builder {

        public SddOrderCancelRequest fromPaylineRequest(ResetRequest paylineRequest) throws InvalidRequestException {

            // Check the input request for NPEs and mandatory fields
            this.checkInputRequest(paylineRequest);

            return new SddOrderCancelRequest(
                    paylineRequest.getContractConfiguration().getContractProperties().get(CONTRACT_CONFIG_CREDITOR_ID).getValue(),
                    new PaymentResponseSuccessAdditionalData.Builder().fromJson(paylineRequest.getTransactionAdditionalData()).getMandateRum(),
                    paylineRequest.getPartnerTransactionId()
            );

        }

        private void checkInputRequest(ResetRequest paylineRequest) throws InvalidRequestException {
            if (paylineRequest == null) {
                throw new InvalidRequestException("Request must not be null");
            }

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

            if (paylineRequest.getPartnerConfiguration() == null
                    || paylineRequest.getPartnerConfiguration().getSensitiveProperties() == null) {
                throw new InvalidRequestException("Partner configuration sensitive properties object must not be null");
            }

            if (paylineRequest.getTransactionAdditionalData() == null) {
                throw new InvalidRequestException("Transaction additional data object must not be null");
            }
            String additionalData = paylineRequest.getTransactionAdditionalData();
            PaymentResponseSuccessAdditionalData paymentResponseSuccessAdditionalData = new PaymentResponseSuccessAdditionalData.Builder().fromJson(additionalData);
            if (paymentResponseSuccessAdditionalData == null
                    || paymentResponseSuccessAdditionalData.getMandateRum() == null) {
                throw new InvalidRequestException("Missing additional data property: mandate rum");
            }

            if (PluginUtils.isEmpty(paylineRequest.getPartnerTransactionId())) {
                throw new InvalidRequestException("Missing mandatory property: partner transaction id");
            }

        }

    }
    //***** BUILDER
    //******************************************************************************************************************

}