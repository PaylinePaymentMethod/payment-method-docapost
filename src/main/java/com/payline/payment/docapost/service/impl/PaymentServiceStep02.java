package com.payline.payment.docapost.service.impl;

import com.payline.payment.docapost.bean.rest.request.RequestBuilderFactory;
import com.payline.payment.docapost.bean.rest.request.mandate.MandateCreateRequest;
import com.payline.payment.docapost.bean.rest.request.signature.InitiateSignatureRequest;
import com.payline.payment.docapost.bean.rest.request.signature.SendOtpRequest;
import com.payline.payment.docapost.bean.rest.response.ResponseBuilderFactory;
import com.payline.payment.docapost.bean.rest.response.error.XmlErrorResponse;
import com.payline.payment.docapost.bean.rest.response.mandate.AbstractXmlResponse;
import com.payline.payment.docapost.bean.rest.response.mandate.WSMandateDTOResponse;
import com.payline.payment.docapost.bean.rest.response.signature.InitiateSignatureResponse;
import com.payline.payment.docapost.bean.rest.response.signature.SendOtpResponse;
import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.service.PaymentServiceStep;
import com.payline.payment.docapost.utils.ActionRequestResponse;
import com.payline.payment.docapost.utils.DocapostFormUtils;
import com.payline.payment.docapost.utils.DocapostLocalParam;
import com.payline.payment.docapost.utils.config.ConfigEnvironment;
import com.payline.payment.docapost.utils.config.ConfigProperties;
import com.payline.payment.docapost.utils.http.DocapostHttpClient;
import com.payline.payment.docapost.utils.http.StringResponse;
import com.payline.payment.docapost.utils.type.WSRequestResultEnum;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.payment.RequestContext;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFormUpdated;
import com.payline.pmapi.bean.paymentform.bean.form.CustomForm;
import com.payline.pmapi.bean.paymentform.response.configuration.PaymentFormConfigurationResponse;
import com.payline.pmapi.bean.paymentform.response.configuration.impl.PaymentFormConfigurationResponseSpecific;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.payline.payment.docapost.utils.DocapostConstants.*;

public class PaymentServiceStep02 implements PaymentServiceStep {

    private static final Logger LOGGER = LogManager.getLogger(PaymentServiceStep02.class);

    private final DocapostHttpClient httpClient;

    private DocapostLocalParam docapostLocalParam;

    public PaymentServiceStep02(DocapostHttpClient httpClient) {
        this.httpClient = httpClient;
        //TEST ?
        this.docapostLocalParam = DocapostLocalParam.getInstance();
    }

    @Override
    public PaymentResponse processPaymentStep(PaymentRequest paymentRequest,
                                              ConfigEnvironment env,
                                              DocapostLocalParam docapostLocalParam,
                                              String credencials) {
        String scheme;
        String host;
        String path;

        String requestBody;
        String responseBody;

        scheme = ConfigProperties.get(CONFIG_SCHEME, env);
        host = ConfigProperties.get(CONFIG_HOST, env);

        //######################################################################################################
        //######################################################################################################
        //######################################################################################################
        //### API MandateWS /api/mandate/create
        try {
            // Initialisation de la requete Docapost
            MandateCreateRequest mandateCreateRequest = RequestBuilderFactory.buildMandateCreateRequest(paymentRequest);

            // Generation des donnees du body de la requete
            requestBody = mandateCreateRequest.buildBody();

            LOGGER.debug("MandateCreateRequest XML body : {}", requestBody);

            // Execution de l'appel WS Docapost /api/mandate/create et recuperation de l'information "mandateRum"
            path = ConfigProperties.get(CONFIG_PATH_WSMANDATE_MANDATE_CREATE);
            final StringResponse mandateCreateStringResponse = this.httpClient.doPost(
                    scheme,
                    host,
                    path,
                    requestBody,
                    credencials
            );

            if (mandateCreateStringResponse == null) {

                LOGGER.debug("MandateCreateRequest StringResponse is null !");
                LOGGER.error(HTTP_NULL_RESPONSE_ERROR_MESSAGE);
                return buildPaymentResponseFailure(DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR);
            }
            LOGGER.debug("MandateCreateRequest StringResponse  {}", mandateCreateStringResponse.toString());

            switch (ActionRequestResponse.checkResponse(mandateCreateStringResponse)) {
                case OK_200:
                    responseBody = mandateCreateStringResponse.getContent().trim();


                    AbstractXmlResponse mandateCreateXmlResponse = getMandateCreateResponse(responseBody);

                    if (mandateCreateXmlResponse != null) {


                        if (mandateCreateXmlResponse.isResultOk()) {

                            LOGGER.debug("WSMandateDTOResponse : {}", mandateCreateXmlResponse.toString());

                            // Recuperation du parametre mandateRum
                            this.docapostLocalParam.setMandateRum(((WSMandateDTOResponse) mandateCreateXmlResponse).getRum());

                        } else {

                            XmlErrorResponse xmlErrorResponse = (XmlErrorResponse) mandateCreateXmlResponse;
                            LOGGER.debug("xmlErrorResponse error : {}", xmlErrorResponse.toString());

                            WSRequestResultEnum wsRequestResult = WSRequestResultEnum.fromDocapostErrorCode(xmlErrorResponse.getException().getCode());

                            return buildPaymentResponseFailure(wsRequestResult);

                        }

                    } else {
                        return buildPaymentResponseFailure("XML RESPONSE PARSING FAILED", FailureCause.INVALID_DATA);
                    }
                    break;
                case OTHER_CODE:
                    LOGGER.error(HTTP_SENDING_ERROR_MESSAGE + mandateCreateStringResponse.getMessage());
                    return buildPaymentResponseFailure(Integer.toString(mandateCreateStringResponse.getCode()), FailureCause.COMMUNICATION_ERROR);
                default:
                    LOGGER.error(HTTP_NULL_RESPONSE_ERROR_MESSAGE);
                    return buildPaymentResponseFailure(DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR);

            }


            //######################################################################################################
            //######################################################################################################
            //######################################################################################################
            //### API MandateWS /api/initiateSignature

            // Initialisation de la requete Docapost
            InitiateSignatureRequest initiateSignatureRequest = RequestBuilderFactory.buildInitiateSignatureRequest(paymentRequest, this.docapostLocalParam);

            // Execution de l'appel WS Docapost /api/initiateSignature et recuperation de l'information "transactionId"
            path = ConfigProperties.get(CONFIG_PATH_WSSIGNATURE_INITIATE_SIGNATURE);
            final StringResponse initiateSignatureStringResponse = this.httpClient.doPost(
                    scheme,
                    host,
                    path,
                    initiateSignatureRequest.getRequestBodyMap(),
                    credencials
            );

            if (initiateSignatureStringResponse == null) {

                LOGGER.debug("InitiateSignatureResponse StringResponse is null !");
                LOGGER.error(HTTP_NULL_RESPONSE_ERROR_MESSAGE);
                return buildPaymentResponseFailure(DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR);
            }
            LOGGER.debug("InitiateSignatureResponse StringResponse : {}", initiateSignatureStringResponse.toString());

            switch (ActionRequestResponse.checkResponse(initiateSignatureStringResponse)) {
                case OK_200:
                    responseBody = initiateSignatureStringResponse.getContent().trim();

                    LOGGER.debug("InitiateSignatureResponse JSON body : {}", responseBody);

                    InitiateSignatureResponse initiateSignatureResponse = ResponseBuilderFactory.buildInitiateSignatureResponse(responseBody);

                    if (initiateSignatureResponse.isResultOk()) {

                        LOGGER.debug("InitiateSignatureResponse : {}", initiateSignatureResponse.toString());

                        // Recuperation du parametre transactionId
                        this.docapostLocalParam.setTransactionId(initiateSignatureResponse.getTransactionId());

                    } else {

                        LOGGER.debug("InitiateSignatureResponse error : {}", initiateSignatureResponse.getErrors().get(0));

                        return buildPaymentResponseFailure(WSRequestResultEnum.PARTNER_UNKNOWN_ERROR);

                    }
                    break;
                case OTHER_CODE:
                    LOGGER.error(HTTP_SENDING_ERROR_MESSAGE + initiateSignatureStringResponse.getMessage());
                    return buildPaymentResponseFailure(Integer.toString(initiateSignatureStringResponse.getCode()), FailureCause.COMMUNICATION_ERROR);
                default:
                    LOGGER.error(HTTP_NULL_RESPONSE_ERROR_MESSAGE);
                    return buildPaymentResponseFailure(DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR);

            }

            //######################################################################################################
            //######################################################################################################
            //######################################################################################################
            //### API MandateWS /api/sendOTP
            // Initialisation de la requete Docapost
            SendOtpRequest sendOtpRequest = RequestBuilderFactory.buildSendOtpRequest(paymentRequest, this.docapostLocalParam);

            // Execution de l'appel WS Docapost /api/sendOTP et recuperation de l'information "signatureId"
            path = ConfigProperties.get(CONFIG_PATH_WSSIGNATURE_SEND_OTP);
            final StringResponse sendOTPStringResponse = this.httpClient.doPost(
                    scheme,
                    host,
                    path,
                    sendOtpRequest.getRequestBodyMap(),
                    credencials
            );

            if (sendOTPStringResponse == null) {

                LOGGER.debug("SendOTPResponse StringResponse is null !");
                LOGGER.error(HTTP_NULL_RESPONSE_ERROR_MESSAGE);
                return buildPaymentResponseFailure(DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR);
            }
            LOGGER.debug("SendOTPResponse StringResponse : {}", sendOTPStringResponse.toString());

            switch (ActionRequestResponse.checkResponse(sendOTPStringResponse)) {
                case OK_200:

                    responseBody = sendOTPStringResponse.getContent().trim();

                    LOGGER.debug("SendOTPResponse JSON body : {}", responseBody);

                    SendOtpResponse sendOtpResponse = ResponseBuilderFactory.buildSendOtpResponse(responseBody);

                    if (sendOtpResponse.isResultOk()) {

                        LOGGER.debug("SendOTPResponse : {}", sendOtpResponse.toString());

                        // Recuperation du parametre transactionId
                        this.docapostLocalParam.setSignatureId(sendOtpResponse.getSignatureId());

                    } else {

                        LOGGER.debug("SendOTPResponse error : {}", sendOtpResponse.getErrors().get(0));

                        return buildPaymentResponseFailure(WSRequestResultEnum.PARTNER_UNKNOWN_ERROR);

                    }
                    break;
                case OTHER_CODE:
                    LOGGER.error(HTTP_SENDING_ERROR_MESSAGE + sendOTPStringResponse.getMessage());
                    return buildPaymentResponseFailure(Integer.toString(sendOTPStringResponse.getCode()), FailureCause.COMMUNICATION_ERROR);
                default:
                    LOGGER.error(HTTP_NULL_RESPONSE_ERROR_MESSAGE);
                    return buildPaymentResponseFailure(DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR);
            }

                /*

                    On doit retourner une reponse de type PaymentResponseFormUpdated pour faire afficher un formulaire de saisie
                    avec un champ OTP, des textes d'information, des liens (telechargement du mandat, renvoie du code OTP), des checkbox

                 */
            /*
             * Creation d'un  customForm contenant
             * - link to download mandate
             * - link to resend otp code
             * - link to CGV
             */
            CustomForm customForm = DocapostFormUtils.createOTPPaymentForm(docapostLocalParam, paymentRequest, sendOtpRequest);

            PaymentFormConfigurationResponse paymentFormConfigurationResponse = PaymentFormConfigurationResponseSpecific
                    .PaymentFormConfigurationResponseSpecificBuilder
                    .aPaymentFormConfigurationResponseSpecific()
                    .withPaymentForm(customForm)
                    .build();

            // Pour le step suivant, on doit envoyer :
            // - Le step IBAN_PHONE
            // - La valeur du MandateRum obtenu lors de l'appel /api/mandate/create
            // - La valeur du transactionId obtenu lors de l'appel /api/initiateSignature
            // - la valeur du signatureId obtenu lors de l'appel /api/sendOTP
            Map<String, String> requestContextMap = new HashMap<>();
            requestContextMap.put(CONTEXT_DATA_STEP, CONTEXT_DATA_STEP_OTP);
            requestContextMap.put(CONTEXT_DATA_MANDATE_RUM, this.docapostLocalParam.getMandateRum());
            requestContextMap.put(CONTEXT_DATA_TRANSACTION_ID, this.docapostLocalParam.getTransactionId());
            requestContextMap.put(CONTEXT_DATA_SIGNATURE_ID, this.docapostLocalParam.getSignatureId());

            Map<String, String> sensitiveRequestContextMap = new HashMap<>();

            RequestContext requestContext = RequestContext
                    .RequestContextBuilder
                    .aRequestContext()
                    .withRequestData(requestContextMap)
                    .withSensitiveRequestData(sensitiveRequestContextMap)
                    .build();

            return PaymentResponseFormUpdated
                    .PaymentResponseFormUpdatedBuilder
                    .aPaymentResponseFormUpdated()
                    .withPaymentFormConfigurationResponse(paymentFormConfigurationResponse)
                    .withRequestContext(requestContext)
                    .build();
        } catch (InvalidRequestException e) {
            LOGGER.error("The input payment request is invalid: ", e.getMessage(), e);
            return buildPaymentResponseFailure(DEFAULT_ERROR_CODE, FailureCause.INVALID_DATA);
        } catch (IOException e) {
            LOGGER.error("An IOException occurred while sending the HTTP request or receiving the response: {}", e.getMessage(), e);
            return buildPaymentResponseFailure(DEFAULT_ERROR_CODE, FailureCause.COMMUNICATION_ERROR);
        } catch (Exception e) {
            LOGGER.error(UNEXPECTED_ERROR_MESSAGE, e.getMessage(), e);
            return buildPaymentResponseFailure(DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR);
        }

    }


    /**
     * Return a AbstractXmlResponse (WSDDOrderDTOResponse or XmlErrorResponse in case of error) based on a XML content
     *
     * @param xmlResponse the XML content
     * @return the AbstractXmlResponse
     */
    private static AbstractXmlResponse getMandateCreateResponse(String xmlResponse) {

        XmlErrorResponse xmlErrorResponse;
        WSMandateDTOResponse mandateCreateResponse;

        if (xmlResponse.contains(MANDATE_WS_XML_SEPALIA_ERROR)) {

            xmlErrorResponse = ResponseBuilderFactory.buildXmlErrorResponse(xmlResponse);

            if (xmlErrorResponse != null) {
                return xmlErrorResponse;
            }

        }

        if (xmlResponse.contains(MANDATE_WS_XML_WS_MANDATE_DTO)) {

            mandateCreateResponse = ResponseBuilderFactory.buildWsMandateDTOResponse(xmlResponse);

            if (mandateCreateResponse != null) {
                return mandateCreateResponse;
            }

        }

        return null;

    }

}
