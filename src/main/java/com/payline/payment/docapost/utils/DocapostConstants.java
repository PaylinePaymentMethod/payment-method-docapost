package com.payline.payment.docapost.utils;

import com.payline.pmapi.bean.paymentform.bean.field.FieldIcon;
import com.payline.pmapi.bean.paymentform.bean.field.InputType;

import java.util.regex.Pattern;

/**
 * Created by Thales on 29/08/2018.
 */
public class DocapostConstants {

    private DocapostConstants() {
        // ras.
    }

    public static final String PAYMENT_METHOD_NAME = "paymentMethod.name";

    public static final String RELEASE_DATE_FORMAT = "dd/MM/yyyy";
    public static final String RELEASE_DATE = "release.date";
    public static final String RELEASE_VERSION = "release.version";
    public static final String RELEASE_PROPERTIES = "release.properties";

    public static final String LOGO_FILE_NAME = "logo.file.name";
    public static final String LOGO_FORMAT = "logo.format";
    public static final String LOGO_CONTENT_TYPE = "logo.content.type";
    public static final String LOGO_HEIGHT = "logo.height";
    public static final String LOGO_WIDTH = "logo.width";
    public static final String LOGO_PROPERTIES = "logo.properties";

    public static final String MANDATE_RUM_GENERATION_DATE_FORMAT = "yyyyMMddHHmmss";

    public static final String RESOURCE_BUNDLE_BASE_NAME = "messages";

    public static final String I18N_SERVICE_DEFAULT_LOCALE = "en";

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String AUTHORIZATION = "Authorization";

    public static final String CONFIG_HTTP_CONNECT_TIMEOUT = "http.connectTimeout";
    public static final String CONFIG_HTTP_WRITE_TIMEOUT = "http.writeTimeout";
    public static final String CONFIG_HTTP_READ_TIMEOUT = "http.readTimeout";

    public static final String CONFIG_SCHEME = "docapost.scheme";
    public static final String CONFIG_HOST = "docapost.host";
    public static final String CONFIG_PATH_WSMANDATE_MANDATE_CREATE = "docapost.mandate.create.path";
    public static final String CONFIG_PATH_WSMANDATE_MANDATE_PDFTPL = "docapost.mandate.pdftpl.path";
    public static final String CONFIG_PATH_WSMANDATE_ORDER_CREATE = "docapost.order.create.path";
    public static final String CONFIG_PATH_WSMANDATE_ORDER_CANCEL = "docapost.order.cancel.path";
    public static final String CONFIG_PATH_WSMANDATE_SCTORDER_CREATE = "docapost.sct.order.create.path";
    public static final String CONFIG_PATH_WSMANDATE_SCTORDER_CANCEL = "docapost.sct.order.cancel.path";
    public static final String CONFIG_PATH_WSSIGNATURE_INITIATE_SIGNATURE = "docapost.initiate.signature.path";
    public static final String CONFIG_PATH_WSSIGNATURE_TERMINATE_SIGNATURE = "docapost.terminate.signature.path";
    public static final String CONFIG_PATH_WSSIGNATURE_SEND_OTP = "docapost.send.otp.path";
    public static final String CONFIG_PATH_WSSIGNATURE_SET_CODE = "docapost.set.code.path";

    public static final String CONTRACT_CONFIG_CREDITOR_ID = "creditorId";
    public static final String CONTRACT_CONFIG_CREDITOR_ID_PROPERTY_LABEL = "contractConfiguration.creditorId.label";
    public static final String CONTRACT_CONFIG_CREDITOR_ID_PROPERTY_DESCRIPTION = "contractConfiguration.creditorId.description";
    public static final String CONTRACT_CONFIG_CREDITOR_ID_ERROR = "contractConfiguration.creditorId.error";

    public static final String PARTNER_CONFIG_AUTH_LOGIN = "authLogin";
    public static final String PARTNER_CONFIG_AUTH_LOGIN_PROPERTY_LABEL = "partnerConfiguration.authLogin.label";
    public static final String PARTNER_CONFIG_AUTH_LOGIN_PROPERTY_DESCRIPTION = "partnerConfiguration.authLogin.description";
    public static final String PARTNER_CONFIG_AUTH_LOGIN_ERROR = "partnerConfiguration.authLogin.error";

    public static final String PARTNER_CONFIG_AUTH_PASS = "authPass";
    public static final String PARTNER_CONFIG_AUTH_PASS_PROPERTY_LABEL = "partnerConfiguration.authPass.label";
    public static final String PARTNER_CONFIG_AUTH_PASS_PROPERTY_DESCRIPTION = "partnerConfiguration.authPass.description";
    public static final String PARTNER_CONFIG_AUTH_PASS_ERROR = "partnerConfiguration.authPass.error";

    public static final String OTP_IBAN_PHONE_TEXT_SET_IBAN = "form.iban.phone.text.setIban";
    public static final String OTP_IBAN_PHONE_TEXT_SET_PHONE = "form.iban.phone.text.setPhone";
    public static final String OTP_IBAN_PHONE_TEXT_SET_PHONE_INFO = "form.iban.phone.text.setPhoneInfo";

    public static final String OTP_FORM_TEXT_DOWNLOAD_MANDATE = "form.otp.text.downloadMandate";
    public static final String OTP_FORM_LINK_DOWNLOAD_MANDATE = "form.otp.link.downloadMandate";
    public static final String OTP_FORM_TEXT_OTP = "form.otp.text.otp";
    public static final String OTP_FORM_TEXT_SET_OTP = "form.otp.text.setOtp";
    public static final String OTP_FORM_TEXT_RESEND_OTP = "form.otp.link.resendOtp";
    public static final String OTP_FORM_CHECKBOX_ACCEPT_CONDITION = "form.otp.checkbox.acceptCondition";
    public static final String OTP_FORM_CHECKBOX_SAVE_MANDATE = "form.otp.checkbox.saveMandate";

    public static final Pattern OTP_FORM_VALIDATION = Pattern.compile("\\d{6}");
    public static final String OTP_FORM_REQUIRED_ERROR_MESSAGE = "form.otp.text.requiredErrorMessage";
    public static final String OTP_FORM_KEY = "formOtp";
    public static final String OTP_FORM_VALUE = "";
    public static final String OTP_FORM_LABEL = "form.otp.label";
    public static final String OTP_FORM_PLACEHOLDER = "123456";
    public static final String OTP_FORM_VALIDATION_ERROR_MESSAGE = "form.otp.text.validationErrorMessage";

    public static final String SIGNATURE_WS_REQUEST_FIELD_CREDITOR_ID = "creditorId";
    public static final String SIGNATURE_WS_REQUEST_FIELD_MANDATE_RUM = "mandateRum";
    public static final String SIGNATURE_WS_REQUEST_FIELD_TRANSACTION_ID = "transactionId";
    public static final String SIGNATURE_WS_REQUEST_FIELD_SIGNATURE_ID = "signatureId";
    public static final String SIGNATURE_WS_REQUEST_FIELD_OTP = "otp";
    public static final String SIGNATURE_WS_REQUEST_FIELD_SUCCESS = "success";

    public static final String FORM_FIELD_IBAN = "formDebtorIban";
    public static final String FORM_FIELD_PHONE = "formDebtorPhone";
    public static final String FORM_FIELD_OTP = "formOtp";

    public static final String CONTEXT_DATA_STEP = "step";
    public static final String CONTEXT_DATA_STEP_IBAN_PHONE = "IBAN_PHONE";
    public static final String CONTEXT_DATA_STEP_OTP = "OTP";
    public static final String CONTEXT_DATA_MANDATE_RUM = "mandateRum";
    public static final String CONTEXT_DATA_TRANSACTION_ID = "transactionId";
    public static final String CONTEXT_DATA_SIGNATURE_ID = "signatureId";

    public static final String MANDATE_WS_XML_SEPALIA_ERROR = "<sepalia>";
    public static final String MANDATE_WS_XML_WS_MANDATE_DTO = "<WSMandateDTO>";
    public static final String MANDATE_WS_XML_WS_SDD_ORDER_DTO = "<WSDDOrderDTO>";
    public static final String MANDATE_WS_XML_WS_SCT_ORDER_DTO = "<WSCTOrderDTO>";


    public static final int HTTP_OK = 200;

    //Data used by the PaymentFormConfiguration noField Form
    public static final boolean NOFIELDFORM_DISPLAY_PAYMENT_BUTTON = true;
    public static final String NOFIELDFORM_BUTTON_TEXT = "form.nofield.button.text";
    public static final String NOFIELDFORM_BUTTON_DESCRIPTION = "form.nofield.button.description";

    //Data used by the IbanForm Object
    public static final String IBAN_TEXT = "form.iban.phone.text.setIban";
    public static final String IBAN_KEY = "formDebtorIban";
    public static final boolean IBAN_REQUIRED = false;
    public static final String IBAN_REQUIRED_ERROR_MESSAGE = "form.iban.phone.text.ibanRequiredErrorMessage";

    //Data used by the phoneForm Object
    public static final FieldIcon PHONE_FIELD_ICON = FieldIcon.PHONE;
    public static final String PHONE_KEY = "formDebtorPhone";
    public static final String PHONE_LABEL = "form.iban.phone.text.setPhone";
    public static final boolean PHONE_REQUIRED = true;
    public static final String PHONE_REQUIRED_ERROR_MESSAGE = "form.iban.phone.text.phoneRequiredErrorMessage";
    public static final boolean PHONE_SECURED = false;
    public static final String PHONE_VALIDATION_MESSAGE = "form.iban.phone.text.phoneValidationMessage";
    public static final String PHONE_PLACEHOLDER = "0606060606";
    public static final InputType INPUT_TYPE = InputType.TEL;
    //PATTERN TO DEFINE
    public static final Pattern PHONE_VALIDATION = Pattern.compile("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}");

    //Data used by the CustomForm Object
    public static final String CUSTOMFORM_TEXT = "form.customForm.text.buttonText";
    public static final String CUSTOMFORM_DESCRIPTION = "form.customForm.text.description";
    public static final boolean DISPLAY_CUSTOMFORM_BUTTON = true;
    public static final String CUSTOMFORM_TEXT_SIGN = "form.customForm.sign.text.buttonText";
    public static final String CUSTOMFORM_TEXT_SIGN_DESCRIPTION = "form.customForm.sign.text.description";


    public static final String SAVE_MANDATE_KEY = "formSaveMandate";
    public static final boolean SAVE_MANDATE_REQUIRED = true;
    public static final boolean SAVE_MANDATE_PRECHECKED = true;
    public static final boolean SAVE_MANDATE_SECURED = true;
    public static final String SAVE_MANDATE_REQUIRED_ERROR_MESSAGE = "form.saveMandate.text.requiredErrorMessage";

    public static final boolean ACCEPT_CONDITION_REQUIRED = true;
    public static final boolean ACCEPT_CONDITION_PRECHECKED = false;
    public static final String ACCEPT_CONDITION_KEY = "formAcceptCondition";
    public static final boolean ACCEPT_CONDITION_SECURED = true;
    public static final String ACCEPT_CONDITION_REQUIRED_ERROR_MESSAGE = "form.acceptCondition.text.requiredErrorMessage";

    public static final String PAYMENT_RESPONSE_SUCCESS_MESSAGE = "payment.response.success.message";


}