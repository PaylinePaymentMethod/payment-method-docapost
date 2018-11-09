package com.payline.payment.docapost.bean.rest.request.mandate;

import com.payline.payment.docapost.bean.PaymentResponseSuccessAdditionalData;
import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.utils.PluginUtils;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.refund.request.RefundRequest;

import javax.xml.bind.annotation.*;
import java.util.Map;

import static com.payline.payment.docapost.utils.DocapostConstants.*;

/**
 * Created by Thales on 08/10/2018.
 */
@XmlRootElement(name = "WSCTOrderDTO")
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
public class SctOrderCreateRequest extends AbstractXmlRequest {

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
    public SctOrderCreateRequest() {
    }

    /**
     * Constructor
     */
    public SctOrderCreateRequest(String creditorId,
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

        public SctOrderCreateRequest fromPaylineRequest(RefundRequest paylineRequest) throws InvalidRequestException {

            // Check the input request for NPEs and mandatory fields
            this.checkInputRequest(paylineRequest);

            return new SctOrderCreateRequest(
                    paylineRequest.getContractConfiguration().getContractProperties().get(CONTRACT_CONFIG_CREDITOR_ID).getValue(),
                    new PaymentResponseSuccessAdditionalData.Builder().fromJson(paylineRequest.getTransactionAdditionalData()).getMandateRum(),
                    paylineRequest.getOrder().getAmount().getAmountInSmallestUnit().floatValue(),
                    paylineRequest.getSoftDescriptor(),
                    paylineRequest.getPartnerTransactionId()
            );

        }

        private void checkInputRequest(RefundRequest paylineRequest) throws InvalidRequestException {
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

            if (PluginUtils.isEmpty(paylineRequest.getSoftDescriptor())) {
                throw new InvalidRequestException("Missing mandatory property: soft descriptor");
            }

            if (paylineRequest.getOrder() == null) {
                throw new InvalidRequestException("Order object must not be null");
            }
            if (paylineRequest.getOrder().getAmount() == null) {
                throw new InvalidRequestException("Missing order property: amount");
            }

        }

    }
    //***** BUILDER
    //******************************************************************************************************************

}