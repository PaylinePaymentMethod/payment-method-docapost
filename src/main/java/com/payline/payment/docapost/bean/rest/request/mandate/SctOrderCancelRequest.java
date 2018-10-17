package com.payline.payment.docapost.bean.rest.request.mandate;

import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.utils.DocapostUtils;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.reset.request.ResetRequest;

import java.util.Map;

import static com.payline.payment.docapost.utils.DocapostConstants.CONTRACT_CONFIG__CREDITOR_ID;
import static com.payline.payment.docapost.utils.DocapostConstants.PARTNER_CONFIG__AUTH_LOGIN;
import static com.payline.payment.docapost.utils.DocapostConstants.PARTNER_CONFIG__AUTH_PASS;

public class SctOrderCancelRequest {

    private String creditorId;

    private String e2eId;

    /**
     * Public default constructor
     */
    public SctOrderCancelRequest() { }

    /**
     * Constructor
     */
    public SctOrderCancelRequest(String creditorId,
                                 String e2eId) {

        this.creditorId     = creditorId;
        this.e2eId          = e2eId;

    }

    public String getCreditorId() {
        return creditorId;
    }

    public String getE2eId() {
        return e2eId;
    }


    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();

        result.append("***** SctOrderCancelRequest info\n");

        result.append("creditorId : " + creditorId + "\n");
        result.append("e2eId : " + e2eId + "\n");

        return result.toString();
    }

    //******************************************************************************************************************
    //***** BUILDER
    public static final class Builder {

        public SctOrderCancelRequest fromPaylineRequest(ResetRequest paylineRequest) throws InvalidRequestException {

            // Check the input request for NPEs and mandatory fields
            this.checkInputRequest(paylineRequest);

            SctOrderCancelRequest request = new SctOrderCancelRequest(
                    paylineRequest.getContractConfiguration().getContractProperties().get( CONTRACT_CONFIG__CREDITOR_ID ).getValue(),
                    paylineRequest.getPartnerTransactionId()
            );

            return request;

        }

        private void checkInputRequest(ResetRequest paylineRequest) throws InvalidRequestException  {
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

            if ( DocapostUtils.isEmpty(paylineRequest.getPartnerTransactionId()) ) {
                throw new InvalidRequestException( "Missing mandatory property: partner transaction id" );
            }

        }

    }
    //***** BUILDER
    //******************************************************************************************************************

}