package com.payline.payment.docapost.service.impl;

import com.payline.pmapi.bean.paymentform.bean.PaymentFormLogo;
import com.payline.pmapi.bean.paymentform.bean.form.NoFieldForm;
import com.payline.pmapi.bean.paymentform.request.PaymentFormConfigurationRequest;
import com.payline.pmapi.bean.paymentform.request.PaymentFormLogoRequest;
import com.payline.pmapi.bean.paymentform.response.configuration.PaymentFormConfigurationResponse;
import com.payline.pmapi.bean.paymentform.response.configuration.impl.PaymentFormConfigurationResponseSpecific;
import com.payline.pmapi.bean.paymentform.response.logo.PaymentFormLogoResponse;
import com.payline.pmapi.service.PaymentFormConfigurationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

import static com.payline.payment.docapost.utils.DocapostConstants.*;

public class PaymentFormConfigurationServiceImpl implements PaymentFormConfigurationService {

    private static final Logger LOGGER = LogManager.getLogger(PaymentFormConfigurationServiceImpl.class);

    @Override
    public PaymentFormConfigurationResponse getPaymentFormConfiguration(PaymentFormConfigurationRequest paymentFormConfigurationRequest) {

        final NoFieldForm noFieldForm = NoFieldForm
                .NoFieldFormBuilder
                .aNoFieldForm()
                // FIXME : get text to define this fields which are mandatory ?
                .withDisplayButton(NOFIELDFORM_DISPLAY_PAYMENT_BUTTON)
                .withButtonText(NOFIELDFORM_BUTTON_TEXT)
                .withDescription(NOFIELDFORM_BUTTON_DESCRIPTION)
                .build();

        return PaymentFormConfigurationResponseSpecific
                .PaymentFormConfigurationResponseSpecificBuilder
                .aPaymentFormConfigurationResponseSpecific()
                .withPaymentForm(noFieldForm)
                .build();
    }

    @Override
    public PaymentFormLogoResponse getPaymentFormLogo(PaymentFormLogoRequest paymentFormLogoRequest) {
        return null;
    }

    @Override
    public PaymentFormLogo getLogo(String s, Locale locale) {

        // FIXME
        return null;
    }

}