package com.payline.payment.docapost.service;

import com.payline.payment.docapost.utils.ActionRequestResponse;
import com.payline.payment.docapost.utils.http.DocapostHttpClient;
import com.payline.payment.docapost.utils.http.StringResponse;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFailure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This abstract service handles the common issues encountered when sending, receiving and processing a {@link PaymentRequest} (or subclass)
 * It delegates the specific parts to the classes that will extends it, through the abstract methods.
 * This way, most of the exception handling can be done here, once.
 */
public abstract class AbstractPaymentHttpService<T extends PaymentRequest> {

    private static final Logger logger = LogManager.getLogger(AbstractPaymentHttpService.class);

    private static final String DEFAULT_ERROR_CODE = "no code transmitted";

    protected DocapostHttpClient httpClient;

    protected AbstractPaymentHttpService() {
        this.httpClient = DocapostHttpClient.getInstance();

    }

    /**
     * Builds the request, sends it through HTTP using the httpClient and recovers the response.
     *
     * @param paymentRequest The input request provided by Payline
     * @return The {@link StringResponse} from the HTTP call
     */
    public abstract StringResponse createSendRequest(T paymentRequest);

    /**
     * Process the response from the HTTP call.
     * It focuses on business aspect of the processing : the technical part has already been done by {@link #processRequest(PaymentRequest)} .
     *
     * @param response The {@link StringResponse} from the HTTP call, which HTTP code is 200 and which body is not null.
     * @return The {@link PaymentResponse}
     */
    public abstract PaymentResponse processResponse(StringResponse response);

    /**
     * Process a {@link PaymentRequest} (or subclass), handling all the generic error cases
     *
     * @param paymentRequest The input request from Payline
     * @return The corresponding {@link PaymentResponse}
     */
    protected PaymentResponse processRequest(T paymentRequest) {
        try {

            // Mandate the child class to create and send the request (which is specific to each implementation)
            StringResponse response = this.createSendRequest(paymentRequest);

            switch (ActionRequestResponse.checkResponse(response)) {
                case OK_200:
                    // Mandate the child class to process the request when it's OK (which is specific to each implementation)
                    return this.processResponse(response);
                case OTHER_CODE:
                    logger.error("An HTTP error occurred while sending the request: " + response.getMessage());
                    return buildPaymentResponseFailure(Integer.toString(response.getCode()), FailureCause.COMMUNICATION_ERROR);
                default:
                    logger.error("The HTTP response or its body is null and should not be");
                    return buildPaymentResponseFailure(DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR);
            }

        } catch (Exception e) {
            logger.error("An unexpected error occurred: {}", e.getMessage(), e);
            return buildPaymentResponseFailure(DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR);
        }

    }

    /**
     * Utility method to instantiate {@link PaymentResponseFailure} objects, using the class' builder.
     *
     * @param errorCode    The error code
     * @param failureCause The failure cause
     * @return The instantiated object
     */
    protected PaymentResponseFailure buildPaymentResponseFailure(String errorCode, FailureCause failureCause) {
        return PaymentResponseFailure.PaymentResponseFailureBuilder.aPaymentResponseFailure()
                .withFailureCause(failureCause)
                .withErrorCode(errorCode)
                .build();
    }

}
