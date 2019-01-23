package com.payline.payment.docapost.service.impl;

import com.payline.payment.docapost.utils.i18n.I18nService;
import com.payline.pmapi.bean.paymentform.bean.PaymentFormLogo;
import com.payline.pmapi.bean.paymentform.bean.form.NoFieldForm;
import com.payline.pmapi.bean.paymentform.request.PaymentFormConfigurationRequest;
import com.payline.pmapi.bean.paymentform.request.PaymentFormLogoRequest;
import com.payline.pmapi.bean.paymentform.response.configuration.PaymentFormConfigurationResponse;
import com.payline.pmapi.bean.paymentform.response.configuration.impl.PaymentFormConfigurationResponseSpecific;
import com.payline.pmapi.bean.paymentform.response.logo.PaymentFormLogoResponse;
import com.payline.pmapi.bean.paymentform.response.logo.impl.PaymentFormLogoResponseFile;
import com.payline.pmapi.service.PaymentFormConfigurationService;
import com.payline.pmapi.logger.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import static com.payline.payment.docapost.utils.DocapostConstants.*;

public class PaymentFormConfigurationServiceImpl implements PaymentFormConfigurationService {


    private static final Logger LOGGER = LogManager.getLogger(PaymentFormConfigurationServiceImpl.class);
    private I18nService i18n = I18nService.getInstance();

    @Override
    public PaymentFormConfigurationResponse getPaymentFormConfiguration(PaymentFormConfigurationRequest paymentFormConfigurationRequest) {

        final NoFieldForm noFieldForm = NoFieldForm
                .NoFieldFormBuilder
                .aNoFieldForm()
                .withDisplayButton(NOFIELDFORM_DISPLAY_PAYMENT_BUTTON)
                .withButtonText(this.i18n.getMessage(NOFIELDFORM_BUTTON_TEXT, paymentFormConfigurationRequest.getLocale()))
//                .withDescription(this.i18n.getMessage(NOFIELDFORM_BUTTON_DESCRIPTION, paymentFormConfigurationRequest.getLocale()))
                .withDescription("")
                .build();

        return PaymentFormConfigurationResponseSpecific
                .PaymentFormConfigurationResponseSpecificBuilder
                .aPaymentFormConfigurationResponseSpecific()
                .withPaymentForm(noFieldForm)
                .build();
    }

    @Override
    public PaymentFormLogoResponse getPaymentFormLogo(PaymentFormLogoRequest paymentFormLogoRequest) {
        Properties props = new Properties();
        try {
            props.load(ConfigurationServiceImpl.class.getClassLoader().getResourceAsStream(LOGO_PROPERTIES));
            return PaymentFormLogoResponseFile.PaymentFormLogoResponseFileBuilder.aPaymentFormLogoResponseFile()
                    .withHeight(Integer.valueOf(props.getProperty(LOGO_HEIGHT)))
                    .withWidth(Integer.valueOf(props.getProperty(LOGO_WIDTH)))
                    .withTitle(this.i18n.getMessage(PAYMENT_METHOD_NAME, paymentFormLogoRequest.getLocale()))
                    .withAlt(this.i18n.getMessage(PAYMENT_METHOD_NAME, paymentFormLogoRequest.getLocale()))
                    .build();
        } catch (IOException e) {
            LOGGER.error("An error occurred reading the file: " + LOGO_PROPERTIES, e);
            throw new RuntimeException("Failed to reading file logo.properties: ", e);

        }


    }

    @Override
    public PaymentFormLogo getLogo(String s, Locale locale) {
        Properties props = new Properties();
        try {
            props.load(ConfigurationServiceImpl.class.getClassLoader().getResourceAsStream(LOGO_PROPERTIES));
        } catch (IOException e) {
            LOGGER.error("An error occurred reading the file: " + LOGO_PROPERTIES, e);
            throw new RuntimeException("Failed to reading file logo.properties: ", e);

        }
        String fileName = props.getProperty(LOGO_FILE_NAME);
        try {
            // Read logo file
            InputStream input = PaymentFormConfigurationServiceImpl.class.getClassLoader().getResourceAsStream(fileName);
            BufferedImage logo = ImageIO.read(input);

            // Recover byte array from image
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(logo, props.getProperty(LOGO_FORMAT), baos);

            return PaymentFormLogo.PaymentFormLogoBuilder.aPaymentFormLogo()
                    .withFile(baos.toByteArray())
                    .withContentType(props.getProperty(LOGO_CONTENT_TYPE))
                    .build();

        } catch (IOException e) {
            LOGGER.error("unable to load the logo", e);
            throw new RuntimeException("Unable to load logo " + fileName);

        }
    }

}