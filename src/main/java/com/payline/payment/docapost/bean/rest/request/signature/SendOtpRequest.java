package com.payline.payment.docapost.bean.rest.request.signature;

import com.payline.payment.docapost.bean.rest.request.WSSignature;
import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.utils.DocapostLocalParam;
import com.payline.payment.docapost.utils.PluginUtils;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.payment.request.PaymentRequest;

import java.util.HashMap;
import java.util.Map;

import static com.payline.payment.docapost.utils.DocapostConstants.*;

/**
 * Created by Thales on 29/08/2018.
 */
public class SendOtpRequest extends WSSignatureRequest implements WSSignature {

    private String transactionId;

    /**
     * Constructor
     */
    public SendOtpRequest(String creditorId,
                          String mandateRum,
                          String transactionId) {

        super(creditorId, mandateRum);

        this.transactionId = transactionId;

    }

    public String getTransactionId() {
        return transactionId;
    }

    @Override
    public Map<String, String> getRequestBodyMap() {

        Map<String, String> bodyMap = new HashMap<>();

        bodyMap.put(SIGNATURE_WS_REQUEST_FIELD_CREDITOR_ID, this.getCreditorId());
        bodyMap.put(SIGNATURE_WS_REQUEST_FIELD_MANDATE_RUM, this.getMandateRum());
        bodyMap.put(SIGNATURE_WS_REQUEST_FIELD_TRANSACTION_ID, this.getTransactionId());

        return bodyMap;

    }

    //******************************************************************************************************************
    //***** BUILDER
    public static final class Builder {

        public SendOtpRequest fromPaylineRequest(PaymentRequest paylineRequest, DocapostLocalParam docapostLocalParam) throws InvalidRequestException {

            // Check the input request for NPEs and mandatory fields
            this.checkInputRequest(paylineRequest, docapostLocalParam);

            return new SendOtpRequest(
                    paylineRequest.getContractConfiguration().getContractProperties().get(CONTRACT_CONFIG_CREDITOR_ID).getValue(),
                    docapostLocalParam.getMandateRum(),
                    docapostLocalParam.getTransactionId()
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

            if (docapostLocalParam == null) {
                throw new InvalidRequestException("Missing mandatory properties: mandate rum & transaction id");

            } else {
                if (PluginUtils.isEmpty(docapostLocalParam.getMandateRum())) {
                    throw new InvalidRequestException("Missing mandatory property: mandate rum");
                }

                if (PluginUtils.isEmpty(docapostLocalParam.getTransactionId())) {
                    throw new InvalidRequestException("Missing mandatory property: transaction id");
                }
            }

        }

    }
    //***** BUILDER
    //******************************************************************************************************************

}