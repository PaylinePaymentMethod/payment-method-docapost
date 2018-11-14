package com.payline.payment.docapost.utils;

import com.payline.payment.docapost.bean.rest.request.signature.SendOtpRequest;
import com.payline.payment.docapost.utils.config.ConfigEnvironment;
import com.payline.payment.docapost.utils.config.ConfigProperties;
import com.payline.payment.docapost.utils.i18n.I18nService;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.paymentform.bean.field.*;
import com.payline.pmapi.bean.paymentform.bean.field.specific.PaymentFormInputFieldIban;
import com.payline.pmapi.bean.paymentform.bean.form.CustomForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.payline.payment.docapost.utils.DocapostConstants.*;
import static com.payline.payment.docapost.utils.PluginUtils.URL_DELIMITER;

public class DocapostFormUtils {

    private I18nService i18n = I18nService.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(DocapostFormUtils.class);
    private static final String UNEXPECTED_ERROR_MESSAGE = "An unexpected error occurred: ";


    /**
     * Create a form to input phone number and iban
     *
     * @return a CustomForm composed by a field PaymentFormInputFieldIban and a field PaymentFormDisplayFieldText
     */
    public static CustomForm createEmptyIbanPhonePaymentForm(Locale locale) {
        I18nService i18n = I18nService.getInstance();

        PaymentFormDisplayFieldText inputIban = PaymentFormDisplayFieldText
                .PaymentFormDisplayFieldTextBuilder
                .aPaymentFormDisplayFieldText()
                .withContent(i18n.getMessage(OTP_IBAN_PHONE_TEXT_SET_IBAN, locale))
                .build();

        PaymentFormInputFieldIban ibanForm = PaymentFormInputFieldIban
                .IbanFieldBuilder
                .anIbanField()
                .withKey(IBAN_KEY)
                .withLabel(IBAN_TEXT)
                .withRequired(IBAN_REQUIRED)
                .withRequiredErrorMessage(IBAN_REQUIRED_ERROR_MESSAGE)
                .build();

        PaymentFormDisplayFieldText inputPhone = PaymentFormDisplayFieldText
                .PaymentFormDisplayFieldTextBuilder
                .aPaymentFormDisplayFieldText()
                .withContent(i18n.getMessage(OTP_IBAN_PHONE_TEXT_SET_PHONE, locale))
                .build();

        PaymentFormInputFieldText phoneForm = PaymentFormInputFieldText
                .PaymentFormFieldTextBuilder
                .aPaymentFormFieldText()
                .withInputType(InputType.TEL)
                .withFieldIcon(PHONE_FIELD_ICON)
                .withKey(PHONE_KEY)
                .withLabel(PHONE_LABEL)
                .withPlaceholder(PHONE_PLACEHOLDER)
                .withRequired(PHONE_REQUIRED)
                .withRequiredErrorMessage(PHONE_REQUIRED_ERROR_MESSAGE)
                .withSecured(PHONE_SECURED)
                .withValidation(PHONE_VALIDATION)
                .withValidationErrorMessage(PHONE_VALIDATION_MESSAGE)
                .withInputType(INPUT_TYPE)
                //.withValue()
                .build();

        PaymentFormDisplayFieldText inputPhoneInfo = PaymentFormDisplayFieldText
                .PaymentFormDisplayFieldTextBuilder
                .aPaymentFormDisplayFieldText()
                .withContent(i18n.getMessage(OTP_IBAN_PHONE_TEXT_SET_PHONE_INFO, locale))
                .build();

        List<PaymentFormField> customFields = new ArrayList<>();
        customFields.add(inputIban);
        customFields.add(ibanForm);
        customFields.add(inputPhone);
        customFields.add(phoneForm);
        customFields.add(inputPhoneInfo);

        return CustomForm
                .builder()
                .withCustomFields(customFields)
                .withButtonText(CUSTOMFORM_TEXT)
                .withDescription(CUSTOMFORM_DESCRIPTION)
                .withDisplayButton(DISPLAY_CUSTOMFORM_BUTTON)
                .build();

    }

    /**
     * Create a paymentForm with
     * - link to download mandate
     * - link to resend otp code
     * - link to CGV
     *
     * @return a CustomForm composed by a field PaymentFormInputFieldIban and a field PaymentFormDisplayFieldText
     */
    public static CustomForm createOTPPaymentForm(DocapostLocalParam docapostLocalParam, PaymentRequest request, SendOtpRequest sendOtpRequest) {

        Locale locale = request.getLocale();
        I18nService i18n = I18nService.getInstance();
        String phone = request.getPaymentFormContext().getPaymentFormParameter().get(FORM_FIELD_PHONE);
        ConfigEnvironment env = PluginUtils.getEnvironnement(request);


        PaymentFormDisplayFieldText downloadMandateText = PaymentFormDisplayFieldText
                .PaymentFormDisplayFieldTextBuilder
                .aPaymentFormDisplayFieldText()
                .withContent(i18n.getMessage(OTP_FORM_TEXT_DOWNLOAD_MANDATE, locale))
                .build();

        PaymentFormDisplayFieldLink downloadMandateLink = PaymentFormDisplayFieldLink
                .PaymentFormDisplayFieldLinkBuilder
                .aPaymentFormDisplayFieldLink()
                .withUrl(getDownloadMandateLinkUrl(
                        env,
                        sendOtpRequest.getCreditorId(),
                        docapostLocalParam.getMandateRum()
                ))
                .withName(i18n.getMessage(OTP_FORM_LINK_DOWNLOAD_MANDATE, locale))
                .withTitle(OTP_FORM_LINK_DOWNLOAD_MANDATE)
                .build();

        PaymentFormDisplayFieldText otpText = PaymentFormDisplayFieldText
                .PaymentFormDisplayFieldTextBuilder
                .aPaymentFormDisplayFieldText()
                .withContent(i18n.getMessage(OTP_FORM_TEXT_OTP, locale))
                .build();

        PaymentFormDisplayFieldText setOtpText = PaymentFormDisplayFieldText
                .PaymentFormDisplayFieldTextBuilder
                .aPaymentFormDisplayFieldText()
                .withContent(i18n.getMessage(OTP_FORM_TEXT_SET_OTP, locale) + " " + phone)
                .build();

        PaymentFormInputFieldText otpForm = PaymentFormInputFieldText
                .PaymentFormFieldTextBuilder
                .aPaymentFormFieldText()
                .withInputType(InputType.NUMBER)
                .withFieldIcon(FieldIcon.PHONE)
                .withKey(OTP_FORM_KEY)
                .withLabel(OTP_FORM_LABEL)
                .withPlaceholder(OTP_FORM_PLACEHOLDER)
                .withRequired(false)
                .withRequiredErrorMessage(i18n.getMessage(OTP_FORM_REQUIRED_ERROR_MESSAGE, locale))
                .withSecured(false)
                .withValidation(OTP_FORM_VALIDATION)
                .withValidationErrorMessage(i18n.getMessage(OTP_FORM_VALIDATION_ERROR_MESSAGE, locale))
                .withValue(OTP_FORM_VALUE)
                .build();

        PaymentFormDisplayFieldLink resendOtpLink = PaymentFormDisplayFieldLink
                .PaymentFormDisplayFieldLinkBuilder
                .aPaymentFormDisplayFieldLink()
                .withUrl(getResendOtpLinkUrl(
                        env,
                        sendOtpRequest.getCreditorId(),
                        docapostLocalParam.getMandateRum(),
                        docapostLocalParam.getTransactionId()
                ))
                .withName(i18n.getMessage(OTP_FORM_TEXT_RESEND_OTP, locale))
                .withTitle(i18n.getMessage(OTP_FORM_TEXT_RESEND_OTP, locale))
                .build();

        PaymentFormInputFieldCheckbox acceptCondition = PaymentFormInputFieldCheckbox
                .PaymentFormFieldCheckboxBuilder
                .aPaymentFormFieldCheckbox()
                .withRequired(true)
                .withLabel(i18n.getMessage(OTP_FORM_CHECKBOX_ACCEPT_CONDITION, locale))
                .withRequiredErrorMessage(i18n.getMessage(ACCEPT_CONDITION_REQUIRED_ERROR_MESSAGE, locale))
                .withKey(ACCEPT_CONDITION_KEY)
                .withPrechecked(ACCEPT_CONDITION_PRECHECKED)
                .withSecured(ACCEPT_CONDITION_SECURED)
                .build();

        PaymentFormInputFieldCheckbox saveMandate = PaymentFormInputFieldCheckbox
                .PaymentFormFieldCheckboxBuilder
                .aPaymentFormFieldCheckbox()
                .withRequired(SAVE_MANDATE_REQUIRED)
                .withLabel(i18n.getMessage(OTP_FORM_CHECKBOX_SAVE_MANDATE, locale))
                .withRequiredErrorMessage(i18n.getMessage(SAVE_MANDATE_REQUIRED_ERROR_MESSAGE, locale))
                .withKey(SAVE_MANDATE_KEY)
                .withPrechecked(SAVE_MANDATE_PRECHECKED)
                .withSecured(SAVE_MANDATE_SECURED)
                .build();

        List<PaymentFormField> customFields = new ArrayList<>();
        customFields.add(downloadMandateText);
        customFields.add(downloadMandateLink);
        customFields.add(otpText);
        customFields.add(setOtpText);
        customFields.add(otpForm);
        customFields.add(resendOtpLink);
        customFields.add(acceptCondition);
        customFields.add(saveMandate);

        return CustomForm
                .builder()
                .withCustomFields(customFields)
                .withDescription(i18n.getMessage(CUSTOMFORM_DESCRIPTION, locale))
                .withButtonText(CUSTOMFORM_TEXT)
                .withDisplayButton(true)
                .build();


    }


    /**
     * Generate the URL to download the mandate document
     *
     * @param env        ConfigEnvironment
     * @param creditorId creditor id
     * @param mandateRum RUM
     * @return URI Syntax Exception
     */
    private static URL getDownloadMandateLinkUrl(ConfigEnvironment env, String creditorId, String mandateRum) {

        URL url = null;

        try {

            String strUrl = ConfigProperties.get(CONFIG_SCHEME, env)
                    + "://"
                    + ConfigProperties.get(CONFIG_HOST, env)
                    + URL_DELIMITER
                    + ConfigProperties.get(CONFIG_PATH_WSMANDATE_MANDATE_PDFTPL)
                    + URL_DELIMITER
                    + creditorId
                    + URL_DELIMITER
                    + mandateRum;

            LOGGER.debug("Mandate download URL : " + strUrl);

            url = new URL(strUrl);

        } catch (MalformedURLException e) {
            LOGGER.error(UNEXPECTED_ERROR_MESSAGE, e);
        }

        return url;

    }

    /**
     * Generate the URL to download the mandate document
     *
     * @return url
     */
    private static URL getResendOtpLinkUrl(ConfigEnvironment env, String creditorId, String mandateRum, String
            transactionId) {

        URL url = null;

        try {

            String query = "creditorId=" + creditorId
                    + "&mandateRum=" + mandateRum
                    + "&transactionId=" + transactionId;

            String strUrl = ConfigProperties.get(CONFIG_SCHEME, env)
                    + "://"
                    + ConfigProperties.get(CONFIG_HOST, env)
                    + URL_DELIMITER
                    + ConfigProperties.get(CONFIG_PATH_WSSIGNATURE_SEND_OTP)
                    + "?"
                    + URLEncoder.encode(query, StandardCharsets.UTF_8.name());

            LOGGER.debug("Mandate download URL : " + strUrl);

            url = new URL(strUrl);

        } catch (UnsupportedEncodingException | MalformedURLException e) {
            LOGGER.error(UNEXPECTED_ERROR_MESSAGE, e);

        }

        return url;

    }


}
