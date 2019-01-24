package com.payline.payment.docapost;

import com.payline.payment.docapost.utils.DocapostConstants;
import com.payline.pmapi.bean.configuration.PartnerConfiguration;
import com.payline.pmapi.bean.payment.PaymentFormContext;
import com.payline.pmapi.bean.payment.RequestContext;
import com.payline.pmapi.integration.bean.ExpectedConfigurationError;
import com.payline.pmapi.integration.bean.PaymentMethodTestDataProvider;
import com.payline.pmapi.integration.bean.PaymentMethodType;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DocapostTestDataProvider implements PaymentMethodTestDataProvider {

    private static final String GOOD_LOGIN = "payline@docapost.fr";
    private static final String GOOD_PWD = "J:[ef8dccma";
    private static final String GOOD_CREDITOR_ID = "MARCHAND1"; //testItSlimpay //democreditor01
    private static final String HOST = "https://espaceclient.sepalia.fr/rcte";
    private static final String MANDATE_CREATE = "mandate";

    @Override
    public PartnerConfiguration getPartnerConfiguration() {
        final Map<String, String> partnerConfigurationMap = new HashMap<>();
        partnerConfigurationMap.put(DocapostConstants.PARTNER_CONFIG_AUTH_LOGIN, GOOD_LOGIN);

        final Map<String, String> sensitivePartnerConfigurationMap = new HashMap<>();
        sensitivePartnerConfigurationMap.put(DocapostConstants.PARTNER_CONFIG_AUTH_PASS, GOOD_PWD);

        return new PartnerConfiguration(partnerConfigurationMap, sensitivePartnerConfigurationMap);
    }

    @Override
    public Map<String, String> getValidAccountInfo() {
        Map<String, String> accountInfo = new HashMap<>();

        accountInfo.put(DocapostConstants.CONTRACT_CONFIG_CREDITOR_ID, GOOD_CREDITOR_ID);
        accountInfo.put(DocapostConstants.CONFIG_HOST, HOST);
        accountInfo.put(DocapostConstants.CONFIG_PATH_WSMANDATE_MANDATE_CREATE, MANDATE_CREATE);

        return accountInfo;
    }

    @Override
    public List<ExpectedConfigurationError> getExpectedConfigurationError() {
        // TODO
        return Collections.emptyList();
    }

    @Override
    public PaymentFormContext getPaymentFormContext(final RequestContext previousRequestContext) {
        String step = null;
        if (previousRequestContext != null) {
            step = previousRequestContext.getRequestData().get(DocapostConstants.CONTEXT_DATA_STEP);
        }
        if (step == null || step.isEmpty()) {
            return null;
        }

        if (DocapostConstants.CONTEXT_DATA_STEP_IBAN_PHONE.equals(step)) {
            // First paymentFormContext
            Map<String, String> paymentFormParameter = new HashMap<>();
            paymentFormParameter.put(DocapostConstants.FORM_FIELD_PHONE, setupNumber());

            Map<String, String> sensitivePaymentFormParameter = new HashMap<>();
            sensitivePaymentFormParameter.put(DocapostConstants.FORM_FIELD_IBAN, "FR7630076020821234567890186");

            //Ajout du numero de telephone et IBAN a la requête
            return PaymentFormContext.PaymentFormContextBuilder
                    .aPaymentFormContext()
                    .withPaymentFormParameter(paymentFormParameter)
                    .withSensitivePaymentFormParameter(sensitivePaymentFormParameter)
                    .build();
        } else if (DocapostConstants.CONTEXT_DATA_STEP_OTP.equals(step)) {
            // Second paymentFormContext
            Map<String, String> paymentFormParameter = new HashMap<>();
            paymentFormParameter.put(DocapostConstants.OTP_FORM_KEY, setupOtp());

            Map<String, String> sensitivePaymentFormParameter = new HashMap<>();
            sensitivePaymentFormParameter.put(DocapostConstants.ACCEPT_CONDITION_KEY, "true");

            //Ajout du numero de telephone et IBAN a la requête
            return PaymentFormContext.PaymentFormContextBuilder
                    .aPaymentFormContext()
                    .withPaymentFormParameter(paymentFormParameter)
                    .withSensitivePaymentFormParameter(sensitivePaymentFormParameter)
                    .build();
        }
        return null;
    }

    @Override
    public EnumSet<PaymentMethodType> getPaymentMethodType() {
        return EnumSet.noneOf(PaymentMethodType.class);
    }

    private String setupNumber() {
        System.out.println("Enter your  phone number : ");
        return getValueFromUser();
    }

    private String setupOtp() {
        System.out.println("Enter your  OTP : ");
        return getValueFromUser();
    }

    private String getValueFromUser() {
        Scanner keyboardUser = new Scanner(System.in);
        String value = keyboardUser.nextLine();
        keyboardUser.reset();
        return value;
    }
}
