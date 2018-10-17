package com.payline.payment.docapost.bean.rest.request.signature;

import static com.payline.payment.docapost.utils.DocapostConstants.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.payline.payment.docapost.TmpTestData;
import com.payline.payment.docapost.bean.rest.request.WSSignature;
import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.utils.DocapostUtils;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.payment.request.PaymentRequest;

/**
 * Created by Thales on 29/08/2018.
 */
public class SetCodeRequest extends WSSignatureRequest implements WSSignature {

    private String transactionId;
    private String signatureId;
    private String otp;

    /**
     * Constructor
     */
    public SetCodeRequest(String creditorId,
                          String mandateRum,
                          String transactionId,
                          String signatureId,
                          String otp) {

        super(creditorId, mandateRum);

        this.transactionId  = transactionId;
        this.signatureId    = signatureId;
        this.otp            = otp;

    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getSignatureId() {
        return signatureId;
    }

    public String getOtp() {
        return otp;
    }

    @Override
    public Map<String, String> getRequestBodyMap() {

        Map<String, String> bodyMap = new HashMap<>();

        bodyMap.put(SIGNATURE_WS_REQUEST_FIELD__CREDITOR_ID, this.getCreditorId());
        bodyMap.put(SIGNATURE_WS_REQUEST_FIELD__MANDATE_RUM, this.getMandateRum());
        bodyMap.put(SIGNATURE_WS_REQUEST_FIELD__TRANSACTION_ID, this.getTransactionId());
        bodyMap.put(SIGNATURE_WS_REQUEST_FIELD__SIGNATURE_ID, this.getSignatureId());
        bodyMap.put(SIGNATURE_WS_REQUEST_FIELD__OTP, this.getOtp());

        return bodyMap;

    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();

        result.append("***** TerminateSignatureRequest info\n");

        result.append("creditorId : " + creditorId + "\n");
        result.append("mandateRum : " + mandateRum + "\n");
        result.append("transactionId : " + transactionId + "\n");
        result.append("signatureId : " + signatureId + "\n");
        result.append("otp : " + otp + "\n");

        return result.toString();
    }

    //******************************************************************************************************************
    //***** BUILDER
    public static final class Builder {

        public SetCodeRequest fromPaylineRequest(PaymentRequest paylineRequest) throws InvalidRequestException {

            // Check the input request for NPEs and mandatory fields
            this.checkInputRequest(paylineRequest);

            SetCodeRequest request = new SetCodeRequest(
                    paylineRequest.getContractConfiguration().getContractProperties().get( CONTRACT_CONFIG__CREDITOR_ID ).getValue(),
                    paylineRequest.getRequestContext().getRequestData().get( CONTEXT_DATA__MANDATE_RUM ),
                    paylineRequest.getRequestContext().getRequestData().get( CONTEXT_DATA__TRANSACTION_ID ),
                    paylineRequest.getRequestContext().getRequestData().get( CONTEXT_DATA__SIGNATURE_ID),
                    paylineRequest.getPaymentFormContext().getPaymentFormParameter().get( FORM_FIELD__OTP )
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
            if ( paymentFormParameter.get( FORM_FIELD__OTP ) == null ) {
                throw new InvalidRequestException( "Missing payment form context data: form debtor OTP" );
            }

            if ( paylineRequest.getRequestContext() == null
                || paylineRequest.getRequestContext().getRequestData() == null ) {
                throw new InvalidRequestException( "Request context data object must not be null" );
            }
            Map<String, String> requestContext = paylineRequest.getRequestContext().getRequestData();
            if ( requestContext.get( CONTEXT_DATA__MANDATE_RUM ) == null ) {
                throw new InvalidRequestException( "Missing request context data: mandate rum" );
            }
            if ( requestContext.get( CONTEXT_DATA__SIGNATURE_ID) == null ) {
                throw new InvalidRequestException( "Missing request context data: signature id" );
            }
            if ( requestContext.get( CONTEXT_DATA__TRANSACTION_ID) == null ) {
                throw new InvalidRequestException( "Missing request context data: transaction id" );
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

        }

    }
    //***** BUILDER
    //******************************************************************************************************************

}