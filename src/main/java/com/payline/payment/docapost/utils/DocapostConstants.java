package com.payline.payment.docapost.utils;

/**
 * Created by Thales on 29/08/2018.
 */
public class DocapostConstants {

    public static final String PAYMENT_METHOD_NAME = "paymentMethod.name";

    public static final String RELEASE_DATE_FORMAT = "dd/MM/yyyy";
    public static final String RELEASE_DATE = "release.date";
    public static final String RELEASE_VERSION = "release.version";
    public static final String RELEASE_PROPERTIES = "release.properties";

    public static final String MANDATE_RUM_GENERATION_DATE_FORMAT = "yyyyMMddHHmmss";

    public static final String RESOURCE_BUNDLE_BASE_NAME = "messages";

    public static final String I18N_SERVICE_DEFAULT_LOCALE = "en";

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String AUTHORIZATION = "Authorization";

    public static final String CONFIG__HTTP_CONNECT_TIMEOUT                     = "http.connectTimeout";
    public static final String CONFIG__HTTP_WRITE_TIMEOUT                       = "http.writeTimeout";
    public static final String CONFIG__HTTP_READ_TIMEOUT                        = "http.readTimeout";

    public static final String CONFIG__SCHEME                                   = "docapost.scheme";
    public static final String CONFIG__HOST                                     = "docapost.host";
    public static final String CONFIG__PATH_WSMANDATE_MANDATE_CREATE            = "docapost.mandate.create.path";
    public static final String CONFIG__PATH_WSMANDATE_ORDER_CREATE              = "docapost.order.create.path";
    public static final String CONFIG__PATH_WSMANDATE_ORDER_CANCEL              = "docapost.order.cancel.path";
    public static final String CONFIG__PATH_WSMANDATE_SCTORDER_CREATE           = "docapost.sct.order.create.path";
    public static final String CONFIG__PATH_WSMANDATE_SCTORDER_CANCEL           = "docapost.sct.order.cancel.path";
    public static final String CONFIG__PATH_WSSIGNATURE_INITIATE_SIGNATURE      = "docapost.initiate.signature.path";
    public static final String CONFIG__PATH_WSSIGNATURE_TERMINATE_SIGNATURE     = "docapost.terminate.signature.path";
    public static final String CONFIG__PATH_WSSIGNATURE_SEND_OTP                = "docapost.send.otp.path";
    public static final String CONFIG__PATH_WSSIGNATURE_SET_CODE                = "docapost.set.code.path";

    public static final String CONTRACT_CONFIG__CREDITOR_ID                         = "creditorId";
    public static final String CONTRACT_CONFIG__CREDITOR_ID_PROPERTY_LABEL          = "contractConfiguration.creditorId.label";
    public static final String CONTRACT_CONFIG__CREDITOR_ID_PROPERTY_DESCRIPTION    = "contractConfiguration.creditorId.description";
    public static final String CONTRACT_CONFIG__CREDITOR_ID_ERROR                   = "contractConfiguration.creditorId.error";

    public static final String PARTNER_CONFIG__AUTH_LOGIN                           = "authLogin";
    public static final String PARTNER_CONFIG__AUTH_LOGIN_PROPERTY_LABEL           = "partnerConfiguration.authLogin.label";
    public static final String PARTNER_CONFIG__AUTH_LOGIN_PROPERTY_DESCRIPTION     = "partnerConfiguration.authLogin.description";
    public static final String PARTNER_CONFIG__AUTH_LOGIN_ERROR                    = "partnerConfiguration.authLogin.error";

    public static final String PARTNER_CONFIG__AUTH_PASS                           = "authPass";
    public static final String PARTNER_CONFIG__AUTH_PASS_PROPERTY_LABEL            = "partnerConfiguration.authPass.label";
    public static final String PARTNER_CONFIG__AUTH_PASS_PROPERTY_DESCRIPTION      = "partnerConfiguration.authPass.description";
    public static final String PARTNER_CONFIG__AUTH_PASS_ERROR                     = "partnerConfiguration.authPass.error";

    public static final String SIGNATURE_WS_REQUEST_FIELD__CREDITOR_ID      = "creditorId";
    public static final String SIGNATURE_WS_REQUEST_FIELD__MANDATE_RUM      = "mandateRum";
    public static final String SIGNATURE_WS_REQUEST_FIELD__TRANSACTION_ID   = "transactionId";
    public static final String SIGNATURE_WS_REQUEST_FIELD__SIGNATURE_ID     = "signatureId";
    public static final String SIGNATURE_WS_REQUEST_FIELD__OTP              = "otp";
    public static final String SIGNATURE_WS_REQUEST_FIELD__SUCCESS          = "success";

    public static final String FORM_FIELD__IBAN     = "formDebtorIban";
    public static final String FORM_FIELD__PHONE    = "formDebtorPhone";
    public static final String FORM_FIELD__OTP      = "formOtp";

    public static final String CONTEXT_DATA__STEP               = "step";
    public static final String CONTEXT_DATA__STEP_IBAN_PHONE    = "IBAN_PHONE";
    public static final String CONTEXT_DATA__STEP_OTP           = "OTP";
    public static final String CONTEXT_DATA__MANDATE_RUM        = "mandateRum";
    public static final String CONTEXT_DATA__TRANSACTION_ID     = "transactionId";
    public static final String CONTEXT_DATA__SIGNATURE_ID       = "signatureId";

    public static final String MANDATE_WS_XML__SEPALIA_ERROR        = "<sepalia>";
    public static final String MANDATE_WS_XML__WS_MANDATE_DTO       = "<WSMandateDTO>";
    public static final String MANDATE_WS_XML__WS_SDD_ORDER_DTO     = "<WSDDOrderDTO>";
    public static final String MANDATE_WS_XML__WS_SCT_ORDER_DTO     = "<WSCTOrderDTO>";

    public static final int HTTP_OK = 200;

}