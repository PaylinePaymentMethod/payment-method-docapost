package com.payline.payment.docapost.bean.rest.request.mandate;

import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.utils.DocapostUtils;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.payment.request.PaymentRequest;

import javax.xml.bind.annotation.*;
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
     * Public default constructor
     */
    public SddOrderCreateRequest() { }

    /**
     * Constructor
     */
    public SddOrderCreateRequest(String creditorId,
                                 String rum,
                                 Float amount,
                                 String label,
                                 String e2eId) {

        this.creditorId     = creditorId;
        this.rum            = rum;
        this.amount         = amount;
        this.label          = label;
        this.e2eId          = e2eId;

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

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();

        result.append("***** SddOrderCreateRequest info\n");

        result.append("creditorId : " + creditorId + "\n");
        result.append("rum : " + rum + "\n");
        result.append("amount : " + amount + "\n");
        result.append("label : " + label + "\n");
        result.append("e2eId : " + e2eId + "\n");

        return result.toString();
    }

    //******************************************************************************************************************
    //***** BUILDER
    public static final class Builder {

        public SddOrderCreateRequest fromPaylineRequest(PaymentRequest paylineRequest) throws InvalidRequestException {

            // Check the input request for NPEs and mandatory fields
            this.checkInputRequest(paylineRequest);

            SddOrderCreateRequest request = new SddOrderCreateRequest(
                    paylineRequest.getContractConfiguration().getContractProperties().get( CONTRACT_CONFIG__CREDITOR_ID ).getValue(),
                    paylineRequest.getRequestContext().getRequestContext().get( CONTEXT_DATA__MANDATE_RUM ),
                    paylineRequest.getOrder().getAmount().getAmountInSmallestUnit().floatValue(),
                    paylineRequest.getSoftDescriptor(),
                    paylineRequest.getTransactionId()
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

            if ( paylineRequest.getRequestContext() == null
                    || paylineRequest.getRequestContext().getRequestContext() == null ) {
                throw new InvalidRequestException( "Request context object must not be null" );
            }
            Map<String, String> requestContext = paylineRequest.getRequestContext().getRequestContext();
            if ( requestContext.get( CONTEXT_DATA__MANDATE_RUM ) == null ) {
                throw new InvalidRequestException( "Missing context data: mandate rum" );
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

            if ( DocapostUtils.isEmpty(paylineRequest.getTransactionId()) ) {
                throw new InvalidRequestException( "Missing mandatory property: transaction id" );
            }

            if ( DocapostUtils.isEmpty(paylineRequest.getSoftDescriptor()) ) {
                throw new InvalidRequestException( "Missing mandatory property: soft descriptor" );
            }

            if ( paylineRequest.getOrder() == null ) {
                throw new InvalidRequestException( "Order object must not be null" );
            }
            if ( paylineRequest.getOrder().getAmount() == null ) {
                throw new InvalidRequestException( "Missing order property: amount" );
            }

        }

    }
    //***** BUILDER
    //******************************************************************************************************************

}