package com.payline.payment.docapost.utils.type;

import com.payline.pmapi.bean.common.FailureCause;

/**
 * Created by Thales on 04/09/2018.
 */
public enum WSRequestResultEnum {

    UNKNOWN_ERROR("UNKNOWN_ERROR", FailureCause.INTERNAL_ERROR),
    PARTNER_UNKNOWN_ERROR("PARTNER_UNKNOWN_ERROR", FailureCause.PARTNER_UNKNOWN_ERROR),
    EMPTY_MANDATE_CREDITOR_AUTHORIZED_FLOWS("EMPTY_MANDATE_CREDITOR_AUTHORIZED_FLOWS", FailureCause.PAYMENT_PARTNER_ERROR),
    MISSING_OR_INVALID_FIELD("MISSING_OR_INVALID_FIELD", FailureCause.INVALID_DATA),
    UNIQUE_CP_VALUE_VIOLATION("UNIQUE_CPVALUE_VIOLATION", FailureCause.INVALID_DATA),
    BPI_INVALID_COUNTRY("BPI_INVALIDCOUNTRY", FailureCause.INVALID_DATA),
    ACCOUNT_CREATION_NOT_AUTHORIZED("ACCOUNT_CREATION_NOT_AUTHORIZED", FailureCause.INVALID_DATA),
    SIGN_DATE_IN_FUTURE("SIGN_DATE_IN_FUTURE", FailureCause.INVALID_DATA),
    CONTEXT_NOT_FOUND("CONTEXT_NOT_FOUND", FailureCause.INVALID_DATA),
    DEFAULT_CREDITOR_ACCOUNT_MISSING("DEFAULT_CREDITOR_ACCOUNT_MISSING", FailureCause.INVALID_DATA),
    CREDITOR_NOT_ACTIVE("CREDITOR_NOT_ACTIVE", FailureCause.INVALID_DATA),
    CREDITOR_NOT_FOUND("CREDITOR_NOT_FOUND", FailureCause.INVALID_DATA),
    CREDITOR_MISSING("CREDITOR_MISSING", FailureCause.INVALID_DATA),
    RUM_NOT_UNIQUE("RUM_NOT_UNIQUE", FailureCause.INVALID_DATA),
    CANCEL_NOT_ALLOWED("CANCEL_NOT_ALLOWED", FailureCause.REFUSED),
    UNSUPPORTED_MANDATE_FLOW_CHANGE("UNSUPPORTED_MANDATE_FLOW_CHANGE", FailureCause.REFUSED),
    MISSING_MANDATE_FLOW_NAME("MISSING_MANDATE_FLOW_NAME", FailureCause.INVALID_DATA),
    NON_SUPPORTED_MANDATE_FLOW_NAME("NON_SUPPORTED_MANDATE_FLOW_NAME", FailureCause.REFUSED),
    CONTEXT_ID_MISSING("CONTEXT_ID_MISSING", FailureCause.INVALID_DATA),
    RUM_NOT_ALLOWED("RUM_NOT_ALLOWED", FailureCause.REFUSED),
    DEBTOR_NAME_WITH_CHARACTER_NOT_SEPA("DEBTOR_NAME_WITH_CHARACTER_NOT_SEPA", FailureCause.INVALID_DATA),
    MANDATE_NOT_VALID("MANDATE_NOT_VALID", FailureCause.INVALID_DATA),
    EXISTING_ORDER("EXISTING_ORDER", FailureCause.INVALID_DATA),
    EXISTING_SCT_ORDER("EXISTING_SCTORDER", FailureCause.INVALID_DATA),
    NOT_FOUND("NOT_FOUND", FailureCause.INVALID_DATA),
    ACCOUNT_MISSING("ACCOUNT_MISSING", FailureCause.INVALID_DATA),
    INVALID_STATUS_MODIFICATION("INVALID_STATUS_MODIFICATION", FailureCause.INVALID_DATA),
    UNAUTHORIZED("UNAUTHORIZED", FailureCause.REFUSED);

    /**
     * Docapost code
     */
    private String docapostErrorCode;

    /**
     * PayLine code
     */
    private FailureCause paylineFailureCause;

    WSRequestResultEnum(String docapostErrorCode,
                        FailureCause paylineFailureCause) {

        this.docapostErrorCode = docapostErrorCode;
        this.paylineFailureCause = paylineFailureCause;

    }

    public String getDocapostErrorCode() {
        return this.docapostErrorCode;
    }

    public FailureCause getPaylineFailureCause() {
        return this.paylineFailureCause;
    }

    public static WSRequestResultEnum fromDocapostErrorCode(String text) {

        for (WSRequestResultEnum result : WSRequestResultEnum.values()) {
            if (result.getDocapostErrorCode().equals(text)) {
                return result;
            }
        }

        return WSRequestResultEnum.UNKNOWN_ERROR;
    }

}