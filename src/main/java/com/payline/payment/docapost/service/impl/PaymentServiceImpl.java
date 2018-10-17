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
import com.payline.payment.docapost.utils.DocapostLocalParam;
import com.payline.payment.docapost.utils.DocapostUtils;
import com.payline.payment.docapost.utils.config.ConfigEnvironment;
import com.payline.payment.docapost.utils.config.ConfigProperties;
import com.payline.payment.docapost.utils.http.DocapostHttpClient;
import com.payline.payment.docapost.utils.http.StringResponse;
import com.payline.payment.docapost.utils.type.WSRequestResultEnum;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.payment.RequestContext;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFailure;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFormUpdated;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseSuccess;
import com.payline.pmapi.bean.paymentform.bean.field.InputType;
import com.payline.pmapi.bean.paymentform.bean.field.PaymentFormField;
import com.payline.pmapi.bean.paymentform.bean.field.PaymentFormInputFieldText;
import com.payline.pmapi.bean.paymentform.bean.field.specific.PaymentFormInputFieldIban;
import com.payline.pmapi.bean.paymentform.bean.form.CustomForm;
import com.payline.pmapi.bean.paymentform.response.configuration.PaymentFormConfigurationResponse;
import com.payline.pmapi.bean.paymentform.response.configuration.impl.PaymentFormConfigurationResponseSpecific;
import com.payline.pmapi.service.PaymentService;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.payline.payment.docapost.utils.DocapostConstants.*;

public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LogManager.getLogger( PaymentServiceImpl.class );

    private static final String DEFAULT_ERROR_CODE = "no code transmitted";

    private DocapostHttpClient httpClient;

    private DocapostLocalParam docapostLocalParam;

    /**
     * Constructeur
     */
    public PaymentServiceImpl() {

        int connectTimeout  = Integer.parseInt( ConfigProperties.get(CONFIG__HTTP_CONNECT_TIMEOUT) );
        int writeTimeout    = Integer.parseInt( ConfigProperties.get(CONFIG__HTTP_WRITE_TIMEOUT) );
        int readTimeout     = Integer.parseInt( ConfigProperties.get(CONFIG__HTTP_READ_TIMEOUT) );

        this.httpClient     = new DocapostHttpClient( connectTimeout, writeTimeout, readTimeout );

        this.docapostLocalParam = DocapostLocalParam.getInstance();

    }

    @Override
    public PaymentResponse paymentRequest( PaymentRequest paymentRequest ) {

        try {

            PaymentResponse response = null;

            // Recuperation des donnees necessaires pour la generation du Header Basic credentials des appels WS
            String authLogin = paymentRequest.getPartnerConfiguration().getSensitiveProperties().get(PARTNER_CONFIG__AUTH_LOGIN);
            String authPass = paymentRequest.getPartnerConfiguration().getSensitiveProperties().get(PARTNER_CONFIG__AUTH_PASS);

            ConfigEnvironment env = Boolean.FALSE.equals( paymentRequest.getPaylineEnvironment().isSandbox() ) ? ConfigEnvironment.PROD : ConfigEnvironment.DEV;
            String scheme = StringUtils.EMPTY;
            String host = StringUtils.EMPTY;
            String path = StringUtils.EMPTY;

            String requestBody = StringUtils.EMPTY;
            String responseBody = StringUtils.EMPTY;

            // Recuperation de l'information de step (etape du processus)
            String step = paymentRequest.getRequestContext().getRequestContext().get(CONTEXT_DATA__STEP);

            this.logger.debug("PaymentRequest step : " + step);

            //----------------------------------------------------------------------------------------------------------
            //**********************************************************************************************************
            //**********************************************************************************************************
            //**********************************************************************************************************
            // Cas 1 : 1ere reception (contextData.get("step") = null ou vide
            if (DocapostUtils.isEmpty(step)) {

                // Pas de donnees à consommer ni appels WS a effectuer...

                /*

                    On doit retourner une reponse de type PaymentResponseFormUpdated pour faire afficher un formulaire de saisie
                    avec un champ IBAN et un champ TELEPHONE

                 */

                PaymentFormInputFieldIban ibanForm = PaymentFormInputFieldIban
                        .IbanFieldBuilder
                        .anIbanField()
                        // FIXME : Add fields ?
                        //.withKey()
                        //.withLabel()
                        //.withRequired()
                        //.withRequiredErrorMessage()
                        .build();

                PaymentFormInputFieldText phoneForm = PaymentFormInputFieldText
                        .PaymentFormFieldTextBuilder
                        .aPaymentFormFieldText()
                        .withInputType(InputType.TEL)
                        // FIXME : Add fields ?
                        //.withFieldIcon()
                        //.withKey()
                        //.withLabel()
                        //.withPlaceholder()
                        //.withRequired()
                        //.withRequiredErrorMessage()
                        //.withSecured()
                        //.withValidation()
                        //.withValidationErrorMessage()
                        //.withValue()
                        .build();

                List<PaymentFormField> customFields = new ArrayList<>();
                customFields.add(ibanForm);
                customFields.add(phoneForm);

                CustomForm customForm = CustomForm
                        .builder()
                        .withCustomFields(customFields)
                        // FIXME : Add fields ?
                        //.withButtonText()
                        //.withDescription()
                        //.withDisplayButton()
                        .build();

                PaymentFormConfigurationResponse paymentFormConfigurationResponse = PaymentFormConfigurationResponseSpecific
                        .PaymentFormConfigurationResponseSpecificBuilder
                        .aPaymentFormConfigurationResponseSpecific()
                        .withPaymentForm(customForm)
                        .build();

                // Pour le step suivant, on doit envoyer :
                // - Le step IBAN_PHONE
                Map<String, String> requestContextMap = new HashMap<>();
                requestContextMap.put(CONTEXT_DATA__STEP, CONTEXT_DATA__STEP_IBAN_PHONE);

                RequestContext requestContext = RequestContext
                        .RequestContextBuilder
                        .aRequestContext()
                        .withRequestContext(requestContextMap)
                        // FIXME : Add fields ?
                        //.withSensitiveRequestContext()
                        .build();

                response =  PaymentResponseFormUpdated
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
            if (CONTEXT_DATA__STEP_IBAN_PHONE.equals(step)) {

                scheme = ConfigProperties.get(CONFIG__SCHEME, env);
                host = ConfigProperties.get(CONFIG__HOST, env);

                //######################################################################################################
                //######################################################################################################
                //######################################################################################################
                //### API MandateWS /api/mandate/create

                // Initialisation de la requete Docapost
                MandateCreateRequest mandateCreateRequest = RequestBuilderFactory.buildMandateCreateRequest(paymentRequest);

                // Generation des donnees du body de la requete
                requestBody = mandateCreateRequest.buildBody();

                this.logger.debug("MandateCreateRequest XML body :");
                this.logger.debug(requestBody);

                // Execution de l'appel WS Docapost /api/mandate/create et recuperation de l'information "mandateRum"
                path = ConfigProperties.get(CONFIG__PATH_WSMANDATE_MANDATE_CREATE);
                final StringResponse mandateCreateStringResponse = this.httpClient.doPost(
                        scheme,
                        host,
                        path,
                        requestBody,
                        DocapostUtils.generateBasicCredentials(authLogin, authPass)
                );

                if ( mandateCreateStringResponse != null ) {
                    this.logger.debug("MandateCreateRequest StringResponse :");
                    this.logger.debug(mandateCreateStringResponse.toString());
                } else {
                    this.logger.debug("MandateCreateRequest StringResponse is null !");
                }

                if ( mandateCreateStringResponse != null && mandateCreateStringResponse.getCode() == 200 && mandateCreateStringResponse.getContent() != null ) {

                    responseBody = mandateCreateStringResponse.getContent().trim();

                    this.logger.debug("WSMandateDTOResponse XML body :");
                    this.logger.debug(responseBody);

                    AbstractXmlResponse mandateCreateXmlResponse = getMandateCreateResponse(responseBody);

                    if (mandateCreateXmlResponse != null) {

                        if (mandateCreateXmlResponse.isResultOk()) {

                            WSMandateDTOResponse mandateCreateResponse = (WSMandateDTOResponse) mandateCreateXmlResponse;

                            this.logger.debug("WSMandateDTOResponse :");
                            this.logger.debug(mandateCreateResponse.toString());

                            // Recuperation du parametre mandateRum
                            this.docapostLocalParam.setMandateRum(mandateCreateResponse.getRum());

                        } else {

                            XmlErrorResponse xmlErrorResponse = (XmlErrorResponse) mandateCreateXmlResponse;

                            this.logger.debug("WSMandateDTOResponse error :");
                            this.logger.debug(xmlErrorResponse.toString());

                            WSRequestResultEnum wsRequestResult = WSRequestResultEnum.fromDocapostErrorCode(xmlErrorResponse.getException().getCode());

                            return buildPaymentResponseFailure( wsRequestResult );

                        }

                    } else {
                        return buildPaymentResponseFailure( "XML RESPONSE PARSING FAILED", FailureCause.INVALID_DATA );
                    }

                } else if( mandateCreateStringResponse != null && mandateCreateStringResponse.getCode() != 200 ) {
                    this.logger.error( "An HTTP error occurred while sending the request: " + mandateCreateStringResponse.getMessage() );
                    return buildPaymentResponseFailure( Integer.toString( mandateCreateStringResponse.getCode() ), FailureCause.COMMUNICATION_ERROR );
                } else {
                    this.logger.error( "The HTTP response or its body is null and should not be" );
                    return buildPaymentResponseFailure( DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR );
                }

                //######################################################################################################
                //######################################################################################################
                //######################################################################################################
                //### API MandateWS /api/initiateSignature

                // Initialisation de la requete Docapost
                InitiateSignatureRequest initiateSignatureRequest = RequestBuilderFactory.buildInitiateSignatureRequest(paymentRequest, this.docapostLocalParam);

                // Execution de l'appel WS Docapost /api/initiateSignature et recuperation de l'information "transactionId"
                path = ConfigProperties.get(CONFIG__PATH_WSSIGNATURE_INITIATE_SIGNATURE);
                final StringResponse initiateSignatureStringResponse = this.httpClient.doPost(
                        scheme,
                        host,
                        path,
                        initiateSignatureRequest.getRequestBodyMap(),
                        DocapostUtils.generateBasicCredentials(authLogin, authPass)
                );

                if ( initiateSignatureStringResponse != null ) {
                    this.logger.debug("InitiateSignatureResponse StringResponse :");
                    this.logger.debug(initiateSignatureStringResponse.toString());
                } else {
                    this.logger.debug("InitiateSignatureResponse StringResponse is null !");
                }

                if ( initiateSignatureStringResponse != null && initiateSignatureStringResponse.getCode() == 200 && initiateSignatureStringResponse.getContent() != null ) {

                    responseBody = initiateSignatureStringResponse.getContent().trim();

                    this.logger.debug("InitiateSignatureResponse JSON body :");
                    this.logger.debug(responseBody);

                    InitiateSignatureResponse initiateSignatureResponse = ResponseBuilderFactory.buildInitiateSignatureResponse(responseBody);

                    if (initiateSignatureResponse.isResultOk()) {

                        this.logger.debug("InitiateSignatureResponse :");
                        this.logger.debug(initiateSignatureResponse.toString());

                        // Recuperation du parametre transactionId
                        this.docapostLocalParam.setTransactionId(initiateSignatureResponse.getTransactionId());

                    } else {

                        this.logger.debug("InitiateSignatureResponse error :");
                        this.logger.debug(initiateSignatureResponse.getErrors().get(0));

                        return buildPaymentResponseFailure( WSRequestResultEnum.PARTNER_UNKNOWN_ERROR );

                    }

                } else if( initiateSignatureStringResponse != null && initiateSignatureStringResponse.getCode() != 200 ) {
                    this.logger.error( "An HTTP error occurred while sending the request: " + initiateSignatureStringResponse.getMessage() );
                    return buildPaymentResponseFailure( Integer.toString( initiateSignatureStringResponse.getCode() ), FailureCause.COMMUNICATION_ERROR );
                } else {
                    this.logger.error( "The HTTP response or its body is null and should not be" );
                    return buildPaymentResponseFailure( DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR );
                }

                //######################################################################################################
                //######################################################################################################
                //######################################################################################################
                //### API MandateWS /api/api/sendOTP

                // Initialisation de la requete Docapost
                SendOtpRequest sendOtpRequest = RequestBuilderFactory.buildSendOtpRequest(paymentRequest, this.docapostLocalParam);

                // Execution de l'appel WS Docapost /api/sendOTP et recuperation de l'information "signatureId"
                path = ConfigProperties.get(CONFIG__PATH_WSSIGNATURE_SEND_OTP);
                final StringResponse sendOTPStringResponse = this.httpClient.doPost(
                        scheme,
                        host,
                        path,
                        sendOtpRequest.getRequestBodyMap(),
                        DocapostUtils.generateBasicCredentials(authLogin, authPass)
                );

                if ( sendOTPStringResponse != null ) {
                    this.logger.debug("SendOTPResponse StringResponse :");
                    this.logger.debug(sendOTPStringResponse.toString());
                } else {
                    this.logger.debug("SendOTPResponse StringResponse is null !");
                }

                if ( sendOTPStringResponse != null && sendOTPStringResponse.getCode() == 200 && sendOTPStringResponse.getContent() != null ) {

                    responseBody = sendOTPStringResponse.getContent().trim();

                    this.logger.debug("SendOTPResponse JSON body :");
                    this.logger.debug(responseBody);

                    SendOtpResponse sendOtpResponse = ResponseBuilderFactory.buildSendOtpResponse(responseBody);

                    if (sendOtpResponse.isResultOk()) {

                        this.logger.debug("SendOTPResponse :");
                        this.logger.debug(sendOtpResponse.toString());

                        // Recuperation du parametre transactionId
                        this.docapostLocalParam.setSignatureId(sendOtpResponse.getSignatureId());

                    } else {

                        this.logger.debug("SendOTPResponse error :");
                        this.logger.debug(sendOtpResponse.getErrors().get(0));

                        return buildPaymentResponseFailure( WSRequestResultEnum.PARTNER_UNKNOWN_ERROR );

                    }

                } else if( sendOTPStringResponse != null && sendOTPStringResponse.getCode() != 200 ) {
                    this.logger.error( "An HTTP error occurred while sending the request: " + sendOTPStringResponse.getMessage() );
                    return buildPaymentResponseFailure( Integer.toString( sendOTPStringResponse.getCode() ), FailureCause.COMMUNICATION_ERROR );
                } else {
                    this.logger.error( "The HTTP response or its body is null and should not be" );
                    return buildPaymentResponseFailure( DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR );
                }

                /*

                    On doit retourner une reponse de type PaymentResponseFormUpdated pour faire afficher un formulaire de saisie
                    avec un champ OTP, des textes d'information, des liens (telechargement du mandat, renvoie du code OTP), des checkbox

                 */

                // TODO : Ajouter les autres CustomForm

                PaymentFormInputFieldText otpForm = PaymentFormInputFieldText
                        .PaymentFormFieldTextBuilder
                        .aPaymentFormFieldText()
                        .withInputType(InputType.NUMBER)
                        // TODO : Add fields
                        //.withFieldIcon()
                        //.withKey()
                        //.withLabel()
                        //.withPlaceholder()
                        //.withRequired()
                        //.withRequiredErrorMessage()
                        //.withSecured()
                        //.withValidation()
                        //.withValidationErrorMessage()
                        //.withValue()
                        .build();

                List<PaymentFormField> customFields = new ArrayList<>();
                customFields.add(otpForm);

                CustomForm customForm = CustomForm
                        .builder()
                        .withCustomFields(customFields)
                        .build();

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
                requestContextMap.put(CONTEXT_DATA__STEP, CONTEXT_DATA__STEP_OTP);
                requestContextMap.put(CONTEXT_DATA__MANDATE_RUM, this.docapostLocalParam.getMandateRum());
                requestContextMap.put(CONTEXT_DATA__TRANSACTION_ID, this.docapostLocalParam.getTransactionId());
                requestContextMap.put(CONTEXT_DATA__SIGNATURE_ID, this.docapostLocalParam.getSignatureId());

                RequestContext requestContext = RequestContext
                        .RequestContextBuilder
                        .aRequestContext()
                        .withRequestContext(requestContextMap)
                        // FIXME : Add fields ?
                        //.withSensitiveRequestContext()
                        .build();

                // FIXME : PaymentResponseFormUpdated ne possede pas d'attribut de type contextData pour retourner a Payline les parametres mandateRum, transactionId et signatureId
                response =  PaymentResponseFormUpdated
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
            if (CONTEXT_DATA__STEP_OTP.equals(step)) {

                scheme = ConfigProperties.get(CONFIG__SCHEME, env);
                host = ConfigProperties.get(CONFIG__HOST, env);

                this.docapostLocalParam.setSignatureSuccess(new Boolean(false));

                //######################################################################################################
                //######################################################################################################
                //######################################################################################################
                //### API MandateWS /api/setCode

                // Initialisation de la requete Docapost
                SetCodeRequest setCodeRequest = RequestBuilderFactory.buildSetCodeRequest(paymentRequest);

                // Execution de l'appel WS Docapost /api/setCode
                path = ConfigProperties.get(CONFIG__PATH_WSSIGNATURE_SET_CODE);
                final StringResponse setCodeStringResponse = this.httpClient.doPost(
                        scheme,
                        host,
                        path,
                        setCodeRequest.getRequestBodyMap(),
                        DocapostUtils.generateBasicCredentials(authLogin, authPass)
                );

                if ( setCodeStringResponse != null ) {
                    this.logger.debug("SetCodeResponse StringResponse :");
                    this.logger.debug(setCodeStringResponse.toString());
                } else {
                    this.logger.debug("SetCodeResponse StringResponse is null !");
                }

                if ( setCodeStringResponse != null && setCodeStringResponse.getCode() == 200 && setCodeStringResponse.getContent() != null ) {

                    responseBody = setCodeStringResponse.getContent().trim();

                    this.logger.debug("SetCodeResponse JSON body :");
                    this.logger.debug(responseBody);

                    SetCodeResponse setCodeResponse = ResponseBuilderFactory.buildSetCodeResponse(responseBody);

                    if (setCodeResponse.isResultOk()) {

                        this.logger.debug("SetCodeResponse :");
                        this.logger.debug(setCodeResponse.toString());

                        // Update du parametre success
                        this.docapostLocalParam.setSignatureSuccess(new Boolean(true));

                    } else {

                        this.logger.debug("SetCodeResponse error :");
                        this.logger.debug(setCodeResponse.getErrors().get(0));

                        return buildPaymentResponseFailure( WSRequestResultEnum.PARTNER_UNKNOWN_ERROR );

                    }

                } else if( setCodeStringResponse != null && setCodeStringResponse.getCode() != 200 ) {
                    this.logger.error( "An HTTP error occurred while sending the request: " + setCodeStringResponse.getMessage() );
                    return buildPaymentResponseFailure( Integer.toString( setCodeStringResponse.getCode() ), FailureCause.COMMUNICATION_ERROR );
                } else {
                    this.logger.error( "The HTTP response or its body is null and should not be" );
                    return buildPaymentResponseFailure( DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR );
                }

                //######################################################################################################
                //######################################################################################################
                //######################################################################################################
                //### API MandateWS /api/terminateSignature

                // Initialisation de la requete Docapost
                TerminateSignatureRequest terminateSignatureRequest = RequestBuilderFactory.buildTerminateSignatureRequest(paymentRequest, this.docapostLocalParam);

                // Execution de l'appel WS Docapost /api/terminateSignature
                path = ConfigProperties.get(CONFIG__PATH_WSSIGNATURE_TERMINATE_SIGNATURE);
                final StringResponse terminateSignatureStringResponse = this.httpClient.doPost(
                        scheme,
                        host,
                        path,
                        terminateSignatureRequest.getRequestBodyMap(),
                        DocapostUtils.generateBasicCredentials(authLogin, authPass)
                );

                if ( terminateSignatureStringResponse != null ) {
                    this.logger.debug("TerminateSignatureResponse StringResponse :");
                    this.logger.debug(terminateSignatureStringResponse.toString());
                } else {
                    this.logger.debug("TerminateSignatureResponse StringResponse is null !");
                }

                if ( terminateSignatureStringResponse != null && terminateSignatureStringResponse.getCode() == 200 && terminateSignatureStringResponse.getContent() != null ) {

                    responseBody = terminateSignatureStringResponse.getContent().trim();

                    this.logger.debug("TerminateSignatureResponse JSON body :");
                    this.logger.debug(responseBody);

                    TerminateSignatureResponse terminateSignatureResponse = ResponseBuilderFactory.buildTerminateSignatureResponse(responseBody);

                    if (terminateSignatureResponse.isResultOk()) {

                        this.logger.debug("TerminateSignatureResponse :");
                        this.logger.debug(terminateSignatureResponse.toString());

                        // Nothing to do

                    } else {

                        this.logger.debug("TerminateSignatureResponse error :");
                        this.logger.debug(terminateSignatureResponse.getErrors().get(0));

                        return buildPaymentResponseFailure( WSRequestResultEnum.PARTNER_UNKNOWN_ERROR );

                    }

                } else if( terminateSignatureStringResponse != null && terminateSignatureStringResponse.getCode() != 200 ) {
                    this.logger.error( "An HTTP error occurred while sending the request: " + terminateSignatureStringResponse.getMessage() );
                    return buildPaymentResponseFailure( Integer.toString( terminateSignatureStringResponse.getCode() ), FailureCause.COMMUNICATION_ERROR );
                } else {
                    this.logger.error( "The HTTP response or its body is null and should not be" );
                    return buildPaymentResponseFailure( DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR );
                }

                //######################################################################################################
                //######################################################################################################
                //######################################################################################################
                //### API MandateWS /api/order/create

                // Initialisation de la requete Docapost
                SddOrderCreateRequest orderCreateRequest = RequestBuilderFactory.buildSddOrderCreateRequest(paymentRequest);

                // Generation des donnees du body de la requete
                requestBody = orderCreateRequest.buildBody();

                this.logger.debug("SddOrderCreateRequest XML body :");
                this.logger.debug(requestBody);

                // Execution de l'appel WS Docapost /api/order/create
                path = ConfigProperties.get(CONFIG__PATH_WSMANDATE_ORDER_CREATE);
                final StringResponse orderCreateStringResponse = this.httpClient.doPost(
                        scheme,
                        host,
                        path,
                        requestBody,
                        DocapostUtils.generateBasicCredentials(authLogin, authPass)
                );

                if ( orderCreateStringResponse != null ) {
                    this.logger.debug("SddOrderCreateRequest StringResponse :");
                    this.logger.debug(orderCreateStringResponse.toString());
                } else {
                    this.logger.debug("SddOrderCreateRequest StringResponse is null !");
                }

                if ( orderCreateStringResponse != null && orderCreateStringResponse.getCode() == 200 && orderCreateStringResponse.getContent() != null ) {

                    responseBody = orderCreateStringResponse.getContent().trim();

                    this.logger.debug("SddOrderCreateRequest XML body :");
                    this.logger.debug(responseBody);

                    AbstractXmlResponse orderCreateXmlResponse = getOrderCreateResponse(responseBody);

                    if (orderCreateXmlResponse != null) {

                        if (orderCreateXmlResponse.isResultOk()) {

                            WSDDOrderDTOResponse orderCreateResponse = (WSDDOrderDTOResponse) orderCreateXmlResponse;

                            this.logger.debug("SddOrderCreateRequest :");
                            this.logger.debug(orderCreateResponse.toString());

                            // Nothing to do
                            this.docapostLocalParam.setOrderStatus(orderCreateResponse.getStatus());

                        } else {

                            XmlErrorResponse xmlErrorResponse = (XmlErrorResponse) orderCreateXmlResponse;

                            this.logger.debug("SddOrderCreateRequest error :");
                            this.logger.debug(xmlErrorResponse.toString());

                            WSRequestResultEnum wsRequestResult = WSRequestResultEnum.fromDocapostErrorCode(xmlErrorResponse.getException().getCode());

                            return buildPaymentResponseFailure( wsRequestResult );

                        }

                    } else {
                        return buildPaymentResponseFailure( "XML RESPONSE PARSING FAILED", FailureCause.INVALID_DATA );
                    }

                } else if( orderCreateStringResponse != null && orderCreateStringResponse.getCode() != 200 ) {
                    this.logger.error( "An HTTP error occurred while sending the request: " + orderCreateStringResponse.getMessage() );
                    return buildPaymentResponseFailure( Integer.toString( orderCreateStringResponse.getCode() ), FailureCause.COMMUNICATION_ERROR );
                } else {
                    this.logger.error( "The HTTP response or its body is null and should not be" );
                    return buildPaymentResponseFailure( DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR );
                }

                /*

                    On doit retourner une reponse de type PaymentResponseSuccess et retourner toutes les infos (mandateRum, transactionId, signatureId) au niveau des additionalData

                 */

                PaymentResponseSuccessAdditionalData paymentResponseSuccessAdditionalData = new PaymentResponseSuccessAdditionalData()
                        .mandateRum(this.docapostLocalParam.getMandateRum())
                        .transactionId(this.docapostLocalParam.getTransactionId())
                        .signatureId(this.docapostLocalParam.getSignatureId());

                // TODO : Renvoyer les infos collectees (mandateRum, transactionId, signatureId) via le paramatre additionnalData
                response = PaymentResponseSuccess
                        .PaymentResponseSuccessBuilder
                        .aPaymentResponseSuccess()
                        .withTransactionIdentifier(paymentRequest.getTransactionId())
                        .withTransactionAdditionalData(paymentResponseSuccessAdditionalData.toJson())
                        .withStatusCode(this.docapostLocalParam.getOrderStatus())
                        // FIXME : Add fields ?
                        //.withMessage()
                        //.withTransactionDetails()
                        .build();

            }

            return response;

        } catch( InvalidRequestException e ){
            this.logger.error( "The input payment request is invalid: " + e.getMessage() );
            return buildPaymentResponseFailure( DEFAULT_ERROR_CODE, FailureCause.INVALID_DATA );
        } catch( IOException e ){
            this.logger.error( "An IOException occurred while sending the HTTP request or receiving the response: " + e.getMessage() );
            return buildPaymentResponseFailure( DEFAULT_ERROR_CODE, FailureCause.COMMUNICATION_ERROR );
        } catch( Exception e ){
            this.logger.error( "An unexpected error occurred: ", e );
            return buildPaymentResponseFailure( DEFAULT_ERROR_CODE, FailureCause.INTERNAL_ERROR );
        }

    }

    /**
     * Utility method to instantiate {@link PaymentResponseFailure} objects, using the class' builder.
     *
     * @param errorCode The error code
     * @param failureCause The failure cause
     * @return The instantiated object
     */
    protected PaymentResponseFailure buildPaymentResponseFailure(String errorCode, FailureCause failureCause ){
        return PaymentResponseFailure.PaymentResponseFailureBuilder.aPaymentResponseFailure()
                .withFailureCause( failureCause )
                .withErrorCode( errorCode )
                .build();
    }

    /**
     * Utility method to instantiate {@link PaymentResponseFailure} objects, using the class' builder.
     *
     * @param wsRequestResult The enum representig the error code and the failure cause
     * @return The instantiated object
     */
    protected PaymentResponseFailure buildPaymentResponseFailure(WSRequestResultEnum wsRequestResult ){
        return PaymentResponseFailure.PaymentResponseFailureBuilder.aPaymentResponseFailure()
                .withFailureCause( wsRequestResult.getPaylineFailureCause() )
                .withErrorCode( wsRequestResult.getDocapostErrorCode() )
                .build();
    }

    /**
     * Return a AbstractXmlResponse (WSDDOrderDTOResponse or XmlErrorResponse in case of error) based on a XML content
     * @param xmlResponse the XML content
     * @return the AbstractXmlResponse
     */
    private static AbstractXmlResponse getMandateCreateResponse(String xmlResponse) {

        XmlErrorResponse xmlErrorResponse = null;
        WSMandateDTOResponse mandateCreateResponse = null;

        if (xmlResponse.contains(MANDATE_WS_XML__SEPALIA_ERROR)) {

            xmlErrorResponse = ResponseBuilderFactory.buildXmlErrorResponse(xmlResponse);

            if (xmlErrorResponse != null) {
                return xmlErrorResponse;
            }

        }

        if (xmlResponse.contains(MANDATE_WS_XML__WS_MANDATE_DTO)) {

            mandateCreateResponse = ResponseBuilderFactory.buildWsMandateDTOResponse(xmlResponse);

            if (mandateCreateResponse != null) {
                return mandateCreateResponse;
            }

        }

        return null;

    }

    /**
     * Return a AbstractXmlResponse (WSDDOrderDTOResponse or XmlErrorResponse in case of error) based on a XML content
     * @param xmlResponse the XML content
     * @return the AbstractXmlResponse
     */
    private static AbstractXmlResponse getOrderCreateResponse(String xmlResponse) {

        XmlErrorResponse xmlErrorResponse = null;
        WSDDOrderDTOResponse orderCreateResponse = null;

        if (xmlResponse.contains(MANDATE_WS_XML__SEPALIA_ERROR)) {

            xmlErrorResponse = ResponseBuilderFactory.buildXmlErrorResponse(xmlResponse);

            if (xmlErrorResponse != null) {
                return xmlErrorResponse;
            }

        }

        if (xmlResponse.contains(MANDATE_WS_XML__WS_SDD_ORDER_DTO)) {

            orderCreateResponse = ResponseBuilderFactory.buildWsddOrderDTOResponse(xmlResponse);

            if (orderCreateResponse != null) {
                return orderCreateResponse;
            }

        }

        return null;

    }

}
