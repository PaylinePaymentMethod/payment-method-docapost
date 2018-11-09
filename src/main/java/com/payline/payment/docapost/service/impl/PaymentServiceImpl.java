package com.payline.payment.docapost.service.impl;

import com.payline.payment.docapost.bean.PaymentResponseSuccessAdditionalData;
import com.payline.payment.docapost.bean.rest.request.RequestBuilderFactory;
import com.payline.payment.docapost.bean.rest.request.mandate.MandateCreateRequest;
import com.payline.payment.docapost.bean.rest.request.mandate.SddOrderCreateRequest;
import com.payline.payment.docapost.bean.rest.request.signature.InitiateSignatureRequest;
import com.payline.payment.docapost.bean.rest.request.signature.SendOtpRequest;
import com.payline.payment.docapost.bean.rest.request.signature.SetCodeRequest;
import com.payline.payment.docapost.bean.rest.request.signature.TerminateSignatureRequest;
import com.payline.payment.docapost.bean.rest.response.ResponseBuilderFactory;
import com.payline.payment.docapost.bean.rest.response.error.XmlErrorResponse;
import com.payline.payment.docapost.bean.rest.response.mandate.AbstractXmlResponse;
import com.payline.payment.docapost.bean.rest.response.mandate.WSDDOrderDTOResponse;
import com.payline.payment.docapost.bean.rest.response.mandate.WSMandateDTOResponse;
import com.payline.payment.docapost.bean.rest.response.signature.InitiateSignatureResponse;
import com.payline.payment.docapost.bean.rest.response.signature.SendOtpResponse;
import com.payline.payment.docapost.bean.rest.response.signature.SetCodeResponse;
import com.payline.payment.docapost.bean.rest.response.signature.TerminateSignatureResponse;
import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.utils.DocapostFormUtils;
import com.payline.payment.docapost.utils.DocapostLocalParam;
import com.payline.payment.docapost.utils.DocapostUtils;
import com.payline.payment.docapost.utils.PluginUtils;
import com.payline.payment.docapost.utils.config.ConfigEnvironment;
import com.payline.payment.docapost.utils.config.ConfigProperties;
import com.payline.payment.docapost.utils.http.DocapostHttpClient;
import com.payline.payment.docapost.utils.http.StringResponse;
import com.payline.payment.docapost.utils.i18n.I18nService;
import com.payline.payment.docapost.utils.type.WSRequestResultEnum;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.common.Message;
import com.payline.pmapi.bean.payment.RequestContext;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.buyerpaymentidentifier.impl.EmptyTransactionDetails;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFailure;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFormUpdated;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseSuccess;
import com.payline.pmapi.bean.paymentform.bean.field.*;
import com.payline.pmapi.bean.paymentform.bean.form.CustomForm;
import com.payline.pmapi.bean.paymentform.response.configuration.PaymentFormConfigurationResponse;
import com.payline.pmapi.bean.paymentform.response.configuration.impl.PaymentFormConfigurationResponseSpecific;
import com.payline.pmapi.service.PaymentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import static com.payline.payment.docapost.utils.DocapostConstants.*;
import static com.payline.pmapi.bean.common.Message.MessageType.SUCCESS;

public class PaymentServiceImpl implements PaymentService   {

    private static final Logger LOGGER = LogManager.getLogger(PaymentServiceImpl.class);

    private static final String DEFAULT_ERROR_CODE = "no code transmitted";
    private static final String HTTP_SENDING_ERROR_MESSAGE = "An HTTP error occurred while sending the request: " ;
    private static final String HTTP_NULL_RESPONSE_ERROR_MESSAGE = "The HTTP response or its body is null and should not be" ;
    private static final String UNEXPECTED_ERROR_MESSAGE = "An unexpected error occurred: " ;

    private I18nService i18n = I18nService.getInstance();

    private DocapostHttpClient httpClient;

    private DocapostLocalParam docapostLocalParam;

    public DocapostLocalParam getDocapostLocalParam(){return docapostLocalParam;}

    /**
     * Constructeur
     */
    public PaymentServiceImpl() {

        int connectTimeout = Integer.parseInt(ConfigProperties.get(CONFIG_HTTP_CONNECT_TIMEOUT));
        int writeTimeout = Integer.parseInt(ConfigProperties.get(CONFIG_HTTP_WRITE_TIMEOUT));
        int readTimeout = Integer.parseInt(ConfigProperties.get(CONFIG_HTTP_READ_TIMEOUT));

        this.httpClient = new DocapostHttpClient(connectTimeout, writeTimeout, readTimeout);

        this.docapostLocalParam = DocapostLocalParam.getInstance();

    }

    @Override
    public PaymentResponse paymentRequest(PaymentRequest paymentRequest) {

        Locale locale = paymentRequest.getLocale();

        try {

            PaymentResponse response = null;

            // Recuperation des donnees necessaires pour la generation du Header Basic credentials des appels WS
            String authLogin = paymentRequest.getPartnerConfiguration().getSensitiveProperties().get(PARTNER_CONFIG_AUTH_LOGIN);
            String authPass = paymentRequest.getPartnerConfiguration().getSensitiveProperties().get(PARTNER_CONFIG_AUTH_PASS);

            // On recharge en local les parametres contextuels de requete
            this.docapostLocalParam.restoreFromPaylineRequest(paymentRequest);

            ConfigEnvironment env = Boolean.FALSE.equals(paymentRequest.getEnvironment().isSandbox()) ? ConfigEnvironment.PROD : ConfigEnvironment.DEV;
            String scheme;
            String host;
            String path;

            String requestBody;
            String responseBody;

            // Recuperation de l'information de step (etape du processus)
            String step = paymentRequest.getRequestContext().getRequestData().get(CONTEXT_DATA_STEP);

            LOGGER.debug("PaymentRequest step : " + step);

            //----------------------------------------------------------------------------------------------------------
            //**********************************************************************************************************
            //**********************************************************************************************************
            //**********************************************************************************************************
            // Cas 1 : 1ere reception (contextData.get("step") = null ou vide
            if (PluginUtils.isEmpty(step)) {

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

                response = PaymentResponseFormUpdated
                        .PaymentResponseFormUpdatedBuilder
                        .aPaymentResponseFormUpdated()
                        .withPaymentFormConfigurationResponse(paymentFormConfigurationResponse)
                        .withRequestContext(requestContext)
                        .build();

            }

            //----------------------------------------------------------------------------------------------------------
            //**********************************************************************************************************
            //**********************************************************************************************************
            //**********************************************************************************************************
            // Cas 2 : 2nde reception (contextData.get("step") = IBAN_PHONE
            if (CONTEXT_DATA_STEP_IBAN_PHONE.equals(step)) {

                // On recupere le numero de telephone saisi par l'utilisateur a l'etape precedente
                String phone = paymentRequest.getPaymentFormContext().getPaymentFormParameter().get(FORM_FIELD_PHONE);

                scheme = ConfigProperties.get(CONFIG_SCHEME, env);
                host = ConfigProperties.get(CONFIG_HOST, env);

                //######################################################################################################
                //######################################################################################################
                //######################################################################################################
                //### API MandateWS /api/mandate/create

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
                        DocapostUtils.generateBasicCredentials(authLogin, authPass)
                );

                if (mandateCreateStringResponse != null) {
                    LOGGER.debug("MandateCreateRequest StringResponse  {}", mandateCreateStringResponse.toString());
                } else {
                    LOGGER.debug("MandateCreateRequest StringResponse is null !");
                }

                if (mandateCreateStringResponse != null && mandateCreateStringResponse.getCode() == 200 && mandateCreateStringResponse.getContent() != null) {

                    responseBody = mandateCreateStringResponse.getContent().trim();


                    AbstractXmlResponse mandateCreateXmlResponse = getMandateCreateResponse(responseBody);

                    if (mandateCreateXmlResponse != null) {

                        if (mandateCreateXmlResponse.isResultOk()) {

                            WSMandateDTOResponse mandateCreateResponse = (WSMandateDTOResponse) mandateCreateXmlResponse;

                            LOGGER.debug("WSMandateDTOResponse : {}", mandateCreateResponse.toString());

                            // Recuperation du parametre mandateRum
                            this.docapostLocalParam.setMandateRum(mandateCreateResponse.getRum());

                        } else {

                            XmlErrorResponse xmlErrorResponse = (XmlErrorResponse) mandateCreateXmlResponse;

                            LOGGER.debug("WSMandateDTOResponse error : {}", xmlErrorResponse.toString());

                            WSRequestResultEnum wsRequestResult = WSRequestResultEnum.fromDocapostErrorCode(xmlErrorResponse.getException().getCode());

                            return buildPaymentResponseFailure(wsRequestResult);

                        }

                    } else {
                        return buildPaymentResponseFailure("XML RESPONSE PARSING FAILED", FailureCause.INVALID_DATA);
                    }

                } else if (mandateCreateStringResponse != null && mandateCreateStringResponse.getCode() != 200) {
                    LOGGER.error(HTTP_SENDING_ERROR_MESSAGE + mandateCreateStringResponse.getMessage());
                    return buildPaymentResponseFailure(Integer.toString(mandateCreateStringResponse.getCode()), FailureCause.COMMUNICATION_ERROR);
                } else {
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
                        DocapostUtils.generateBasicCredentials(authLogin, authPass)
                );

                if (initiateSignatureStringResponse != null) {
                    LOGGER.debug("InitiateSignatureResponse StringResponse : {}", initiateSignatureStringResponse.toString());
                } else {
                    LOGGER.debug("InitiateSignatureResponse StringResponse is null !");
                }

                if (initiateSignatureStringResponse != null && initiateSignatureStringResponse.getCode() == 200 && initiateSignatureStringResponse.getContent() != null) {

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

                } else if (initiateSignatureStringResponse != null && initiateSignatureStringResponse.getCode() != 200) {
                    LOGGER.error(HTTP_SENDING_ERROR_MESSAGE + initiateSignatureStringResponse.getMessage());
                    return buildPaymentResponseFailure(Integer.toString(initiateSignatureStringResponse.getCode()), FailureCause.COMMUNICATION_ERROR);
                } else {
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
                        DocapostUtils.generateBasicCredentials(authLogin, authPass)
                );

                if (sendOTPStringResponse != null) {
                    LOGGER.debug("SendOTPResponse StringResponse : {}", sendOTPStringResponse.toString());
                } else {
                    LOGGER.debug("SendOTPResponse StringResponse is null !");
                }

                if (sendOTPStringResponse != null && sendOTPStringResponse.getCode() == 200 && sendOTPStringResponse.getContent() != null) {

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

                } else if (sendOTPStringResponse != null && sendOTPStringResponse.getCode() != 200) {
                    LOGGER.error(HTTP_SENDING_ERROR_MESSAGE + sendOTPStringResponse.getMessage());
                    return buildPaymentResponseFailure(Integer.toString(sendOTPStringResponse.getCode()), FailureCause.COMMUNICATION_ERROR);
                } else {
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
              CustomForm customForm =   DocapostFormUtils.createOTPPaymentForm(this, paymentRequest,sendOtpRequest);

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

                response = PaymentResponseFormUpdated
                        .PaymentResponseFormUpdatedBuilder
                        .aPaymentResponseFormUpdated()
                        .withPaymentFormConfigurationResponse(paymentFormConfigurationResponse)
                        .withRequestContext(requestContext)
                        .build();

            }

            //----------------------------------------------------------------------------------------------------------
            //**********************************************************************************************************
            //**********************************************************************************************************
            //**********************************************************************************************************
            //*** Cas 3 : 3eme reception (contextData.get("step") = 3
            if (CONTEXT_DATA_STEP_OTP.equals(step)) {

                scheme = ConfigProperties.get(CONFIG_SCHEME, env);
                host = ConfigProperties.get(CONFIG_HOST, env);

                this.docapostLocalParam.setSignatureSuccess(false);

                //######################################################################################################
                //######################################################################################################
                //######################################################################################################
                //### API MandateWS /api/setCode

                // Initialisation de la requete Docapost
                SetCodeRequest setCodeRequest = RequestBuilderFactory.buildSetCodeRequest(paymentRequest);

                // Execution de l'appel WS Docapost /api/setCode
                path = ConfigProperties.get(CONFIG_PATH_WSSIGNATURE_SET_CODE);
                final StringResponse setCodeStringResponse = this.httpClient.doPost(
                        scheme,
                        host,
                        path,
                        setCodeRequest.getRequestBodyMap(),
                        DocapostUtils.generateBasicCredentials(authLogin, authPass)
                );

                if (setCodeStringResponse != null) {
                    LOGGER.debug("SetCodeResponse StringResponse : {}", setCodeStringResponse.toString());
                } else {
                    LOGGER.debug("SetCodeResponse StringResponse is null !");
                }

                if (setCodeStringResponse != null && setCodeStringResponse.getCode() == 200 && setCodeStringResponse.getContent() != null) {

                    responseBody = setCodeStringResponse.getContent().trim();

                    LOGGER.debug("SetCodeResponse JSON body : {}", responseBody);

                    SetCodeResponse setCodeResponse = ResponseBuilderFactory.buildSetCodeResponse(responseBody);

                    if (setCodeResponse.isResultOk()) {

                        LOGGER.debug("SetCodeResponse : {}", setCodeResponse.toString());

                        // Update du parametre success
                        this.docapostLocalParam.setSignatureSuccess(true);

                    } else {

                        LOGGER.debug("SetCodeResponse error : {}", setCodeResponse.getErrors().get(0));

                        return buildPaymentResponseFailure(WSRequestResultEnum.PARTNER_UNKNOWN_ERROR);

                    }

                } else if (setCodeStringResponse != null && setCodeStringResponse.getCode() != 200) {
                    LOGGER.error(HTTP_SENDING_ERROR_MESSAGE + setCodeStringResponse.getMessage());
                    return buildPaymentResponseFailure(Integer.toString(setCodeStringResponse.getCode()), FailureCause.COMMUNICATION_ERROR);
                } else {
                    LOGGER.error(HTTP_NULL_RESPONSE_ERROR_MESSAGE);
                    return buildPaymentResponseFailure(DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR);
                }

                //######################################################################################################
                //######################################################################################################
                //######################################################################################################
                //### API MandateWS /api/terminateSignature

                // Initialisation de la requete Docapost
                TerminateSignatureRequest terminateSignatureRequest = RequestBuilderFactory.buildTerminateSignatureRequest(paymentRequest, this.docapostLocalParam);

                // Execution de l'appel WS Docapost /api/terminateSignature
                path = ConfigProperties.get(CONFIG_PATH_WSSIGNATURE_TERMINATE_SIGNATURE);
                final StringResponse terminateSignatureStringResponse = this.httpClient.doPost(
                        scheme,
                        host,
                        path,
                        terminateSignatureRequest.getRequestBodyMap(),
                        DocapostUtils.generateBasicCredentials(authLogin, authPass)
                );

                if (terminateSignatureStringResponse != null) {
                    LOGGER.debug("TerminateSignatureResponse StringResponse : {}", terminateSignatureStringResponse.toString());
                } else {
                    LOGGER.debug("TerminateSignatureResponse StringResponse is null !");
                }

                if (terminateSignatureStringResponse != null && terminateSignatureStringResponse.getCode() == 200 && terminateSignatureStringResponse.getContent() != null) {

                    responseBody = terminateSignatureStringResponse.getContent().trim();

                    LOGGER.debug("TerminateSignatureResponse JSON body : {}", responseBody);

                    TerminateSignatureResponse terminateSignatureResponse = ResponseBuilderFactory.buildTerminateSignatureResponse(responseBody);

                    if (terminateSignatureResponse.isResultOk()) {

                        LOGGER.debug("TerminateSignatureResponse : {}", terminateSignatureResponse.toString());

                        // Nothing to do

                    } else {

                        LOGGER.debug("TerminateSignatureResponse error : {}", terminateSignatureResponse.getErrors().get(0));

                        return buildPaymentResponseFailure(WSRequestResultEnum.PARTNER_UNKNOWN_ERROR);

                    }

                } else if (terminateSignatureStringResponse != null && terminateSignatureStringResponse.getCode() != 200) {
                    LOGGER.error(HTTP_SENDING_ERROR_MESSAGE + terminateSignatureStringResponse.getMessage());
                    return buildPaymentResponseFailure(Integer.toString(terminateSignatureStringResponse.getCode()), FailureCause.COMMUNICATION_ERROR);
                } else {
                    LOGGER.error(HTTP_NULL_RESPONSE_ERROR_MESSAGE);
                    return buildPaymentResponseFailure(DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR);
                }

                //######################################################################################################
                //######################################################################################################
                //######################################################################################################
                //### API MandateWS /api/order/create

                // Initialisation de la requete Docapost
                SddOrderCreateRequest orderCreateRequest = RequestBuilderFactory.buildSddOrderCreateRequest(paymentRequest);

                // Generation des donnees du body de la requete
                requestBody = orderCreateRequest.buildBody();

                LOGGER.debug("SddOrderCreateRequest XML body : {}", requestBody);

                // Execution de l'appel WS Docapost /api/order/create
                path = ConfigProperties.get(CONFIG_PATH_WSMANDATE_ORDER_CREATE);
                final StringResponse orderCreateStringResponse = this.httpClient.doPost(
                        scheme,
                        host,
                        path,
                        requestBody,
                        DocapostUtils.generateBasicCredentials(authLogin, authPass)
                );

                if (orderCreateStringResponse != null) {
                    LOGGER.debug("SddOrderCreateRequest StringResponse : {}", orderCreateStringResponse.toString());
                } else {
                    LOGGER.debug("SddOrderCreateRequest StringResponse is null !");
                }

                if (orderCreateStringResponse != null && orderCreateStringResponse.getCode() == 200 && orderCreateStringResponse.getContent() != null) {

                    responseBody = orderCreateStringResponse.getContent().trim();

                    LOGGER.debug("SddOrderCreateRequest XML body : {}", responseBody);

                    AbstractXmlResponse orderCreateXmlResponse = getOrderCreateResponse(responseBody);

                    if (orderCreateXmlResponse != null) {

                        if (orderCreateXmlResponse.isResultOk()) {

                            WSDDOrderDTOResponse orderCreateResponse = (WSDDOrderDTOResponse) orderCreateXmlResponse;

                            LOGGER.debug("SddOrderCreateRequest : {}", orderCreateResponse.toString());

                            // Nothing to do
                            this.docapostLocalParam.setOrderStatus(orderCreateResponse.getStatus());

                        } else {

                            XmlErrorResponse xmlErrorResponse = (XmlErrorResponse) orderCreateXmlResponse;

                            LOGGER.debug("SddOrderCreateRequest error : {}", xmlErrorResponse.toString());

                            WSRequestResultEnum wsRequestResult = WSRequestResultEnum.fromDocapostErrorCode(xmlErrorResponse.getException().getCode());

                            return buildPaymentResponseFailure(wsRequestResult);

                        }

                    } else {
                        return buildPaymentResponseFailure("XML RESPONSE PARSING FAILED", FailureCause.INVALID_DATA);
                    }

                } else if (orderCreateStringResponse != null && orderCreateStringResponse.getCode() != 200) {
                    LOGGER.error(HTTP_SENDING_ERROR_MESSAGE + orderCreateStringResponse.getMessage());
                    return buildPaymentResponseFailure(Integer.toString(orderCreateStringResponse.getCode()), FailureCause.COMMUNICATION_ERROR);
                } else {
                    LOGGER.error(HTTP_NULL_RESPONSE_ERROR_MESSAGE);
                    return buildPaymentResponseFailure(DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR);
                }

                /*

                    On doit retourner une reponse de type PaymentResponseSuccess et retourner toutes les infos (mandateRum, transactionId, signatureId) au niveau des additionalData

                 */

                PaymentResponseSuccessAdditionalData paymentResponseSuccessAdditionalData = new PaymentResponseSuccessAdditionalData()
                        .mandateRum(this.docapostLocalParam.getMandateRum())
                        .transactionId(this.docapostLocalParam.getTransactionId())
                        .signatureId(this.docapostLocalParam.getSignatureId());

                response = PaymentResponseSuccess
                        .PaymentResponseSuccessBuilder
                        .aPaymentResponseSuccess()
                        .withPartnerTransactionId(this.docapostLocalParam.getTransactionId())
                        .withTransactionAdditionalData(paymentResponseSuccessAdditionalData.toJson())
                        .withStatusCode(this.docapostLocalParam.getOrderStatus())
                        .withMessage(new Message(SUCCESS, this.i18n.getMessage(PAYMENT_RESPONSE_SUCCESS_MESSAGE, locale)))
                        .withTransactionDetails(new EmptyTransactionDetails())
                        .build();

            }

            return response;

        } catch (InvalidRequestException e) {
            LOGGER.error("The input payment request is invalid: " + e.getMessage());
            return buildPaymentResponseFailure(DEFAULT_ERROR_CODE, FailureCause.INVALID_DATA);
        } catch (IOException e) {
            LOGGER.error("An IOException occurred while sending the HTTP request or receiving the response: " + e.getMessage());
            return buildPaymentResponseFailure(DEFAULT_ERROR_CODE, FailureCause.COMMUNICATION_ERROR);
        } catch (Exception e) {
            LOGGER.error(UNEXPECTED_ERROR_MESSAGE, e);
            return buildPaymentResponseFailure(DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR);
        }

    }

    /**
     * Generate the URL to download the mandate document
     *
     * @param env        ConfigEnvironment
     * @param creditorId creditor id
     * @param mandateRum RUM
     * @return URI Syntax Exception
     */
    public URL getDownloadMandateLinkUrl(ConfigEnvironment env, String creditorId, String mandateRum) {

        URL url = null;

        try {

            String strUrl = ConfigProperties.get(CONFIG_SCHEME, env)
                    + "://"
                    + ConfigProperties.get(CONFIG_HOST, env)
                    + "/"
                    + ConfigProperties.get(CONFIG_PATH_WSMANDATE_MANDATE_PDFTPL)
                    + "/"
                    + creditorId
                    + "/"
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
    public URL getResendOtpLinkUrl(ConfigEnvironment env, String creditorId, String mandateRum, String transactionId) {
// FIXME : string ?
        URL url = null;

        try {

            String query = "creditorId=" + creditorId
                    + "&"
                    + "mandateRum=" + mandateRum
                    + "&"
                    + "transactionId" + transactionId;

            String strUrl = ConfigProperties.get(CONFIG_SCHEME, env)
                    + "://"
                    + ConfigProperties.get(CONFIG_HOST, env)
                    + "/"
                    + ConfigProperties.get(CONFIG_PATH_WSSIGNATURE_SEND_OTP)
                    + "?"
                    + URLEncoder.encode(query, "UTF8");

            LOGGER.debug("Mandate download URL : " + strUrl);

            url = new URL(strUrl);

        } catch (UnsupportedEncodingException | MalformedURLException e) {
            LOGGER.error(UNEXPECTED_ERROR_MESSAGE, e);

        }

        return url;

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

    /**
     * Utility method to instantiate {@link PaymentResponseFailure} objects, using the class' builder.
     *
     * @param wsRequestResult The enum representig the error code and the failure cause
     * @return The instantiated object
     */
    protected PaymentResponseFailure buildPaymentResponseFailure(WSRequestResultEnum wsRequestResult) {
        return PaymentResponseFailure.PaymentResponseFailureBuilder.aPaymentResponseFailure()
                .withFailureCause(wsRequestResult.getPaylineFailureCause())
                .withErrorCode(wsRequestResult.getDocapostErrorCode())
                .build();
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

    /**
     * Return a AbstractXmlResponse (WSDDOrderDTOResponse or XmlErrorResponse in case of error) based on a XML content
     *
     * @param xmlResponse the XML content
     * @return the AbstractXmlResponse
     */
    private static AbstractXmlResponse getOrderCreateResponse(String xmlResponse) {

        XmlErrorResponse xmlErrorResponse;
        WSDDOrderDTOResponse orderCreateResponse;

        if (xmlResponse.contains(MANDATE_WS_XML_SEPALIA_ERROR)) {

            xmlErrorResponse = ResponseBuilderFactory.buildXmlErrorResponse(xmlResponse);

            if (xmlErrorResponse != null) {
                return xmlErrorResponse;
            }

        }

        if (xmlResponse.contains(MANDATE_WS_XML_WS_SDD_ORDER_DTO)) {

            orderCreateResponse = ResponseBuilderFactory.buildWsddOrderDTOResponse(xmlResponse);

            if (orderCreateResponse != null) {
                return orderCreateResponse;
            }

        }

        return null;

    }

}
