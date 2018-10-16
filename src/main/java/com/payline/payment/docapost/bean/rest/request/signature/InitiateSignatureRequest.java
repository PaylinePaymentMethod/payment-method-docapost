package com.payline.payment.docapost.bean.rest.request.signature;

import com.payline.payment.docapost.TmpTestData;
import com.payline.payment.docapost.bean.rest.request.WSSignature;
import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.utils.DocapostLocalParam;
import com.payline.payment.docapost.utils.DocapostUtils;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.payment.request.PaymentRequest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.payline.payment.docapost.utils.DocapostConstants.*;

/**
 * Created by Thales on 29/08/2018.
 */
public class InitiateSignatureRequest extends WSSignatureRequest implements WSSignature {

    /**
     * Constructor
     */
    public InitiateSignatureRequest(String creditorId,
                                    String mandateRum) {

        super(creditorId, mandateRum);

    }

    /**
     * Build an attribute map
     * @return
     */
    @Override
    public Map<String, String> getRequestBodyMap() {

        Map<String, String> bodyMap = new HashMap<>();

        bodyMap.put(SIGNATURE_WS_REQUEST_FIELD__CREDITOR_ID, this.getCreditorId());
        bodyMap.put(SIGNATURE_WS_REQUEST_FIELD__MANDATE_RUM, this.getMandateRum());

        return bodyMap;

    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();

        result.append("***** InitiateSignatureRequest info\n");

        result.append("creditorId : " + creditorId + "\n");
        result.append("mandateRum : " + mandateRum + "\n");

        return result.toString();
    }

    //******************************************************************************************************************
    //***** BUILDER
    public static final class Builder {

        public InitiateSignatureRequest fromPaylineRequest(PaymentRequest paylineRequest, DocapostLocalParam docapostLocalParam) throws InvalidRequestException {

            // Check the input request for NPEs and mandatory fields
            this.checkInputRequest(paylineRequest, docapostLocalParam);

            InitiateSignatureRequest request = new InitiateSignatureRequest(
                    paylineRequest.getContractConfiguration().getContractProperties().get( CONTRACT_CONFIG__CREDITOR_ID ).getValue(),
                    docapostLocalParam.getMandateRum()
            );

            return request;

        }

        private void checkInputRequest(PaymentRequest paylineRequest, DocapostLocalParam docapostLocalParam) throws InvalidRequestException  {
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

            if ( docapostLocalParam == null
                    || DocapostUtils.isEmpty(docapostLocalParam.getMandateRum()) ) {
                throw new InvalidRequestException( "Missing mandatory property: mandate rum" );
            }

        }

    }
    //***** BUILDER
    //******************************************************************************************************************

}