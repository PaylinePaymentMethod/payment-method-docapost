package com.payline.payment.docapost.bean.rest.request.mandate;

import com.payline.payment.docapost.bean.rest.common.DocapostBean;
import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.utils.PluginUtils;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.reset.request.ResetRequest;

import java.util.Map;

import static com.payline.payment.docapost.utils.DocapostConstants.*;

public class SctOrderCancelRequest extends DocapostBean {

    private String creditorId;

    private String e2eId;

    /**
     * Public default constructor
     */
    public SctOrderCancelRequest() {
    }

    /**
     * Constructor
     */
    public SctOrderCancelRequest(String creditorId,
                                 String e2eId) {

        this.creditorId = creditorId;
        this.e2eId = e2eId;

    }

    public String getCreditorId() {
        return creditorId;
    }

    public String getE2eId() {
        return e2eId;
    }


    //******************************************************************************************************************
    //***** BUILDER
    public static final class Builder {

        public SctOrderCancelRequest fromPaylineRequest(ResetRequest paylineRequest) throws InvalidRequestException {

            // Check the input request for NPEs and mandatory fields
            this.checkInputRequest(paylineRequest);

            return new SctOrderCancelRequest(
                    paylineRequest.getContractConfiguration().getContractProperties().get(CONTRACT_CONFIG_CREDITOR_ID).getValue(),
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

            if (paylineRequest.getPartnerConfiguration() == null
                    || paylineRequest.getPartnerConfiguration().getSensitiveProperties() == null) {
                throw new InvalidRequestException("Partner configuration sensitive properties object must not be null");
            }
            Map<String, String> sensitiveProperties = paylineRequest.getPartnerConfiguration().getSensitiveProperties();
            if (sensitiveProperties.get(PARTNER_CONFIG_AUTH_LOGIN) == null) {
                throw new InvalidRequestException("Missing partner configuration property: auth login");
            }
            if (sensitiveProperties.get(PARTNER_CONFIG_AUTH_PASS) == null) {
                throw new InvalidRequestException("Missing partner configuration property: auth pass");
            }

            if (PluginUtils.isEmpty(paylineRequest.getPartnerTransactionId())) {
                throw new InvalidRequestException("Missing mandatory property: partner transaction id");
            }

        }

    }
    //***** BUILDER
    //******************************************************************************************************************

}