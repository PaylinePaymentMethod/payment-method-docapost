package com.payline.payment.docapost.bean.rest.request.signature;

import com.payline.payment.docapost.bean.rest.request.WSSignature;
import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.utils.DocapostLocalParam;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.payment.request.PaymentRequest;

import java.util.HashMap;
import java.util.Map;

import static com.payline.payment.docapost.utils.DocapostConstants.*;

/**
 * Created by Thales on 05/09/2018.
 */
public class TerminateSignatureRequest extends WSSignatureRequest implements WSSignature {

    private String transactionId;
    private Boolean success;

    /**
     * Constructor
     */
    public TerminateSignatureRequest(String creditorId,
                                     String mandateRum,
                                     String transactionId,
                                     Boolean success) {

        super(creditorId, mandateRum);

        this.transactionId = transactionId;
        this.success = success;

    }

    public String getTransactionId() {
        return transactionId;
    }

    public Boolean getSuccess() {
        return success;
    }

    @Override
    public Map<String, String> getRequestBodyMap() {

        Map<String, String> bodyMap = new HashMap<>();

        bodyMap.put(SIGNATURE_WS_REQUEST_FIELD_CREDITOR_ID, this.getCreditorId());
        bodyMap.put(SIGNATURE_WS_REQUEST_FIELD_MANDATE_RUM, this.getMandateRum());
        bodyMap.put(SIGNATURE_WS_REQUEST_FIELD_TRANSACTION_ID, this.getTransactionId());
        bodyMap.put(SIGNATURE_WS_REQUEST_FIELD_SUCCESS, String.valueOf(this.getSuccess().booleanValue()));

        return bodyMap;

    }


    //******************************************************************************************************************
    //***** BUILDER
    public static final class Builder {

        public TerminateSignatureRequest fromPaylineRequest(PaymentRequest paylineRequest, DocapostLocalParam docapostLocalParam) throws InvalidRequestException {

            // Check the input request for NPEs and mandatory fields
            this.checkInputRequest(paylineRequest, docapostLocalParam);

            return new TerminateSignatureRequest(
                    paylineRequest.getContractConfiguration().getContractProperties().get(CONTRACT_CONFIG_CREDITOR_ID).getValue(),
                    paylineRequest.getRequestContext().getRequestData().get(CONTEXT_DATA_MANDATE_RUM),
                    paylineRequest.getRequestContext().getRequestData().get(CONTEXT_DATA_TRANSACTION_ID),
                    docapostLocalParam.getSignatureSuccess()
            );

        }

        private void checkInputRequest(PaymentRequest paylineRequest, DocapostLocalParam docapostLocalParam) throws InvalidRequestException {
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

            if (paylineRequest.getRequestContext() == null
                    || paylineRequest.getRequestContext().getRequestData() == null) {
                throw new InvalidRequestException("Request context data object must not be null");
            }
            Map<String, String> requestContext = paylineRequest.getRequestContext().getRequestData();
            if (requestContext.get(CONTEXT_DATA_MANDATE_RUM) == null) {
                throw new InvalidRequestException("Missing request context data: mandate rum");
            }
            if (requestContext.get(CONTEXT_DATA_TRANSACTION_ID) == null) {
                throw new InvalidRequestException("Missing request context data: transaction id");
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

            if (docapostLocalParam == null
                    || docapostLocalParam.getSignatureSuccess() == null) {
                throw new InvalidRequestException("Missing mandatory property: signature success");
            }

        }

    }
    //***** BUILDER
    //******************************************************************************************************************

}