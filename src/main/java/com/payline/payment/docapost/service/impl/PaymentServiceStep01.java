package com.payline.payment.docapost.service.impl;

import com.payline.payment.docapost.service.PaymentServiceStep;
import com.payline.payment.docapost.utils.DocapostFormUtils;
import com.payline.payment.docapost.utils.DocapostLocalParam;
import com.payline.payment.docapost.utils.config.ConfigEnvironment;
import com.payline.pmapi.bean.payment.RequestContext;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFormUpdated;
import com.payline.pmapi.bean.paymentform.bean.form.CustomForm;
import com.payline.pmapi.bean.paymentform.response.configuration.PaymentFormConfigurationResponse;
import com.payline.pmapi.bean.paymentform.response.configuration.impl.PaymentFormConfigurationResponseSpecific;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.payline.payment.docapost.utils.DocapostConstants.CONTEXT_DATA_STEP;
import static com.payline.payment.docapost.utils.DocapostConstants.CONTEXT_DATA_STEP_IBAN_PHONE;

public class PaymentServiceStep01 implements PaymentServiceStep {

    @Override
    public PaymentResponse processPaymentStep(PaymentRequest paymentRequest,
                                              ConfigEnvironment env,
                                              DocapostLocalParam docapostLocalParam,
                                              String credencials) {
        Locale locale = paymentRequest.getLocale();
        // Pas de donnees à consommer ni appels WS a effectuer...

        /*
            On doit retourner une reponse de type PaymentResponseFormUpdated pour faire afficher un formulaire de saisie
            avec un champ IBAN et un champ TELEPHONE

         */
        // Création d'un formulaire de saisie d'un IBAN et numéro de téléphone
        CustomForm customForm = DocapostFormUtils.createEmptyIbanPhonePaymentForm(locale);

        PaymentFormConfigurationResponse paymentFormConfigurationResponse = PaymentFormConfigurationResponseSpecific
                .PaymentFormConfigurationResponseSpecificBuilder
                .aPaymentFormConfigurationResponseSpecific()
                .withPaymentForm(customForm)
                .build();

        // Pour le step suivant, on doit envoyer :
        // - Le step IBAN_PHONE
        Map<String, String> requestContextMap = new HashMap<>();
        requestContextMap.put(CONTEXT_DATA_STEP, CONTEXT_DATA_STEP_IBAN_PHONE);

        //Get sensitiveRequestContext from Payment request
        Map<String, String> requestSensitiveContext = paymentRequest.getRequestContext().getSensitiveRequestData();
        RequestContext requestContext = RequestContext
                .RequestContextBuilder
                .aRequestContext()
                .withRequestData(requestContextMap)
                .withSensitiveRequestData(requestSensitiveContext)
                .build();

        return PaymentResponseFormUpdated
                .PaymentResponseFormUpdatedBuilder
                .aPaymentResponseFormUpdated()
                .withPaymentFormConfigurationResponse(paymentFormConfigurationResponse)
                .withRequestContext(requestContext)
                .build();

    }
}
