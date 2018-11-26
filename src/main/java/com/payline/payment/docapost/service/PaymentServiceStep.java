package com.payline.payment.docapost.service;

import com.payline.payment.docapost.utils.DocapostLocalParam;
import com.payline.payment.docapost.utils.config.ConfigEnvironment;
import com.payline.payment.docapost.utils.type.WSRequestResultEnum;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFailure;

public interface PaymentServiceStep {

    String HTTP_SENDING_ERROR_MESSAGE = "An HTTP error occurred while sending the request: ";
    String HTTP_NULL_RESPONSE_ERROR_MESSAGE = "The HTTP response or its body is null and should not be";
    String DEFAULT_ERROR_CODE = "no code transmitted";
    String UNEXPECTED_ERROR_MESSAGE = "An unexpected error occurred: {}";


    PaymentResponse processPaymentStep(PaymentRequest paymentRequest,
                                       ConfigEnvironment env,
                                       DocapostLocalParam docapostLocalParam,
                                       String credencials);

    /**
     * Utility method to instantiate {@link PaymentResponseFailure
     * } objects, using the class' builder.
     *
     * @param wsRequestResult The enum representig the error code and the failure cause
     * @return The instantiated object
     */
    default PaymentResponseFailure buildPaymentResponseFailure(WSRequestResultEnum wsRequestResult) {
        return PaymentResponseFailure.PaymentResponseFailureBuilder.aPaymentResponseFailure()
                .withFailureCause(wsRequestResult.getPaylineFailureCause())
                .withErrorCode(wsRequestResult.getDocapostErrorCode())
                .build();
    }

    /**
     * Utility method to instantiate {@link PaymentResponseFailure} objects, using the class' builder.
     *
     * @param errorCode    The error code
     * @param failureCause The failure cause
     * @return The instantiated object
     */
    default PaymentResponseFailure buildPaymentResponseFailure(String errorCode, FailureCause failureCause) {
        return PaymentResponseFailure.PaymentResponseFailureBuilder.aPaymentResponseFailure()
                .withFailureCause(failureCause)
                .withErrorCode(errorCode)
                .build();
    }

}
