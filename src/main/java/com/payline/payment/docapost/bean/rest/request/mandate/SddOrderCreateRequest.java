package com.payline.payment.docapost.bean.rest.request.mandate;

import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.utils.PluginUtils;
import com.payline.pmapi.bean.payment.request.PaymentRequest;

import javax.xml.bind.annotation.*;

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

    private static final String ERR_REQUEST = "Request must not be null";
    private static final String ERR_CONTRACT = "Contract configuration properties object must not be null";
    private static final String ERR_CREDITOR_ID = "Missing contract configuration property: creditor id";
    private static final String ERR_CONTEXT = "Request context object must not be null";
    private static final String ERR_RUM = "Missing context data: mandate rum";
    private static final String ERR_SENSITIVE = "Partner configuration sensitive properties object must not be null";
    private static final String ERR_AUTH_LOGIN = "Missing partner configuration property: auth login";
    private static final String ERR_AUTH_PASS = "Missing partner configuration property: auth pass";
    private static final String ERR_TRANSACTION_ID = "Missing mandatory property: transaction id";
    private static final String ERR_SOFT_DESCRIPTOR = "Missing mandatory property: soft descriptor";
    private static final String ERR_ORDER = "Order object must not be null";
    private static final String ERR_AMOUNT = "Missing order property: amount";

    private static final String[][] fields = {
            {"contractConfiguration", ERR_CONTRACT},
            {"contractConfiguration.contractProperties", ERR_CONTRACT},
            {"contractConfiguration.contractProperties#" + CONTRACT_CONFIG_CREDITOR_ID, ERR_CREDITOR_ID},
            {"requestContext", ERR_CONTEXT},
            {"requestContext.requestData", ERR_CONTEXT},
            {"requestContext.requestData#" + CONTEXT_DATA_MANDATE_RUM, ERR_RUM},
            {"partnerConfiguration", ERR_SENSITIVE},
            {"partnerConfiguration.sensitivePartnerConfigurationMap", ERR_SENSITIVE},
            {"partnerConfiguration.sensitivePartnerConfigurationMap#" + PARTNER_CONFIG_AUTH_LOGIN, ERR_AUTH_LOGIN},
            {"partnerConfiguration.sensitivePartnerConfigurationMap#" + PARTNER_CONFIG_AUTH_PASS, ERR_AUTH_PASS},
            {"transactionId", ERR_TRANSACTION_ID},
            {"softDescriptor", ERR_SOFT_DESCRIPTOR},
            {"order", ERR_ORDER},
            {"order.amount", ERR_AMOUNT}
    };

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

            return  new SddOrderCreateRequest(
                    paylineRequest.getContractConfiguration().getContractProperties().get(CONTRACT_CONFIG_CREDITOR_ID).getValue(),
                    paylineRequest.getRequestContext().getRequestData().get(CONTEXT_DATA_MANDATE_RUM),
                    paylineRequest.getOrder().getAmount().getAmountInSmallestUnit().floatValue(),
                    paylineRequest.getSoftDescriptor(),
                    paylineRequest.getTransactionId()
            );


        }

        private void checkInputRequest(PaymentRequest paylineRequest) throws InvalidRequestException {

            PluginUtils.validate(paylineRequest, ERR_REQUEST, fields);

            /*
            PluginUtils.requireNonNull(paylineRequest, ERR_REQUEST);

            Class clz = paylineRequest.getClass();
            Object obj = paylineRequest;
            Object parent = paylineRequest;
            Map<String, Object> checkedObject = new HashMap<>();
            String fieldName;
            String key;
            try {
                for (String[] name : fields) {
                    if (name[0].contains(".")) {
                        String[] objNames = name[0].split("\\.");
                        fieldName = objNames[objNames.length - 1];
                        if (fieldName.contains("#")) {
                            String[] mapKey = objNames[objNames.length - 1].split("#");
                            parent = checkedObject.get(mapKey[0]);
                            key = mapKey[1];
                            PluginUtils.requireNonNull((Map) parent, key, name[1]);
                            continue;
                        } else {
                            parent = checkedObject.get(objNames[objNames.length - 2]);
                        }
                    } else {
                        fieldName = name[0];
                        parent = paylineRequest;
                    }
                    clz = parent.getClass();
                    Field f = clz.getDeclaredField(fieldName);
                    if (!Modifier.isPublic(f.getModifiers())) {
                        f.setAccessible(true);
                    }
                    obj = f.get(parent);
                    PluginUtils.requireNonNull(obj, name[1]);
                    checkedObject.put(fieldName, obj);
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            */
        }

    }
    //***** BUILDER
    //******************************************************************************************************************

}