package com.payline.payment.docapost;

import static com.payline.payment.docapost.utils.DocapostConstants.*;

import com.payline.payment.docapost.bean.PaymentResponseSuccessAdditionalData;
import com.payline.payment.docapost.bean.rest.request.mandate.MandateCreateRequest;
import com.payline.payment.docapost.bean.rest.request.mandate.OrderCreateRequest;
import com.payline.payment.docapost.bean.rest.request.signature.InitiateSignatureRequest;
import com.payline.payment.docapost.bean.rest.request.signature.SendOtpRequest;
import com.payline.payment.docapost.bean.rest.request.signature.SetCodeRequest;
import com.payline.payment.docapost.bean.rest.request.signature.TerminateSignatureRequest;
import com.payline.payment.docapost.bean.rest.response.AbstractXmlResponse;
import com.payline.payment.docapost.bean.rest.response.error.XmlErrorResponse;
import com.payline.payment.docapost.bean.rest.response.mandate.MandateCreateResponse;
import com.payline.payment.docapost.bean.rest.response.mandate.OrderCreateResponse;
import com.payline.payment.docapost.bean.rest.response.signature.InitiateSignatureResponse;
import com.payline.payment.docapost.bean.rest.response.signature.SendOtpResponse;
import com.payline.payment.docapost.bean.rest.response.signature.SetCodeResponse;
import com.payline.payment.docapost.bean.rest.response.signature.TerminateSignatureResponse;
import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.utils.DocapostUtils;
import com.payline.payment.docapost.utils.config.ConfigEnvironment;
import com.payline.payment.docapost.utils.config.ConfigProperties;
import com.payline.payment.docapost.utils.http.DocapostHttpClient;
import com.payline.payment.docapost.utils.http.StringResponse;

/**
 * Created by Thales on 30/08/2018.
 */
public class Main {

    public static void main( String[] args ) {

        String requestBody = "";
        String xmlResponse = "";
        String jsonResponse = "";

        DocapostHttpClient docapostHttpClient = new DocapostHttpClient(10, 10, 15);

        String credentials = DocapostUtils.generateBasicCredentials(TmpTestData.AUTH_LOGIN, TmpTestData.AUTH_MDP);

        //##############################################################################################################
        // MandateCreate
        try {

            MandateCreateRequest mandateCreateRequest = new MandateCreateRequest.Builder().fromPaylineRequest(null);

            System.out.println(mandateCreateRequest.toString());

            requestBody = mandateCreateRequest.buildBody();

            System.out.println("MandateCreateRequest XML body :");
            System.out.println(requestBody);

            String scheme = ConfigProperties.get(CONFIG__SCHEME, ConfigEnvironment.DEV);
            String host = ConfigProperties.get(CONFIG__HOST, ConfigEnvironment.DEV);
            String path = ConfigProperties.get(CONFIG__PATH_WSMANDATE_MANDATE_CREATE);

            final StringResponse mandateCreateResponse = docapostHttpClient.doPost(
                    scheme,
                    host,
                    path,
                    requestBody,
                    credentials /*Credentials.basic(TmpTestData.AUTH_LOGIN, TmpTestData.AUTH_MDP)*/
            );

            System.out.println(mandateCreateResponse);

            xmlResponse = mandateCreateResponse.getContent().trim();

            System.out.println(xmlResponse);

        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        AbstractXmlResponse mandateCreateXmlResponse = getMandateCreateResponse(xmlResponse);

        if (mandateCreateXmlResponse == null) {
            return;
        }

        if (!mandateCreateXmlResponse.isResultOk()) {

            XmlErrorResponse xmlErrorResponse = (XmlErrorResponse) mandateCreateXmlResponse;

            System.out.println(xmlErrorResponse.toString());

            return;

        } else {

            MandateCreateResponse mandateCreateResponse = (MandateCreateResponse) mandateCreateXmlResponse;

            System.out.println(mandateCreateResponse.toString());

            TmpTestData.getInstance().rum = mandateCreateResponse.getRum();

        }

        //##############################################################################################################
        // InitiateSignature
        try {

            InitiateSignatureRequest initiateSignatureRequest = new InitiateSignatureRequest.Builder().fromPaylineRequest(null, null);

            System.out.println(initiateSignatureRequest.toString());

            String scheme = ConfigProperties.get(CONFIG__SCHEME, ConfigEnvironment.DEV);
            String host = ConfigProperties.get(CONFIG__HOST, ConfigEnvironment.DEV);
            String path = ConfigProperties.get(CONFIG__PATH_WSSIGNATURE_INITIATE_SIGNATURE);

            final StringResponse initiateSignatureResponse = docapostHttpClient.doPost(
                    scheme,
                    host,
                    path,
                    initiateSignatureRequest.getRequestBodyMap(),
                    credentials /*Credentials.basic(TmpTestData.AUTH_LOGIN, TmpTestData.AUTH_MDP)*/
            );

            System.out.println(initiateSignatureResponse);

            jsonResponse = initiateSignatureResponse.getContent().trim();

            System.out.println(jsonResponse);

        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        InitiateSignatureResponse initiateSignatureResponse = getInitiateSignatureResponse(jsonResponse);

        if (initiateSignatureResponse == null) {
            return;
        }

        System.out.println(initiateSignatureResponse.toString());

        if (!initiateSignatureResponse.isResultOk()) {

            return;

        } else {

            TmpTestData.getInstance().transactionId = initiateSignatureResponse.getTransactionId();

        }

        //##############################################################################################################
        // sendOTP
        try {

            SendOtpRequest sendOtpRequest = new SendOtpRequest.Builder().fromPaylineRequest(null, null);

            System.out.println(sendOtpRequest.toString());

            String scheme = ConfigProperties.get(CONFIG__SCHEME, ConfigEnvironment.DEV);
            String host = ConfigProperties.get(CONFIG__HOST, ConfigEnvironment.DEV);
            String path = ConfigProperties.get(CONFIG__PATH_WSSIGNATURE_SEND_OTP);

            final StringResponse sendOTPResponse = docapostHttpClient.doPost(
                    scheme,
                    host,
                    path,
                    sendOtpRequest.getRequestBodyMap(),
                    credentials /*Credentials.basic(TmpTestData.AUTH_LOGIN, TmpTestData.AUTH_MDP)*/
            );

            System.out.println(sendOTPResponse);

            jsonResponse = sendOTPResponse.getContent().trim();

            System.out.println(jsonResponse);

        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SendOtpResponse sendOtpResponse = getSendOtpResponse(jsonResponse);

        if (sendOtpResponse == null) {
            return;
        }

        System.out.println(sendOtpResponse.toString());

        if (!sendOtpResponse.isResultOk()) {

            return;

        } else {

            TmpTestData.getInstance().signatureId = sendOtpResponse.getSignatureId();

        }

        //##############################################################################################################
        // setCode
        try {

            // MAKE BREAKPOINT ON THE REQUEST BUILDING TO SET THE RECEIVED OTP CODE ON YOUR MOBILE PHONE
            SetCodeRequest setCodeRequest = new SetCodeRequest.Builder().fromPaylineRequest(null);

            System.out.println(setCodeRequest.toString());

            String scheme = ConfigProperties.get(CONFIG__SCHEME, ConfigEnvironment.DEV);
            String host = ConfigProperties.get(CONFIG__HOST, ConfigEnvironment.DEV);
            String path = ConfigProperties.get(CONFIG__PATH_WSSIGNATURE_SET_CODE);

            final StringResponse setCodeResponse = docapostHttpClient.doPost(
                    scheme,
                    host,
                    path,
                    setCodeRequest.getRequestBodyMap(),
                    credentials /*Credentials.basic(TmpTestData.AUTH_LOGIN, TmpTestData.AUTH_MDP)*/
            );

            System.out.println(setCodeResponse);

            jsonResponse = setCodeResponse.getContent().trim();

            System.out.println(jsonResponse);

        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SetCodeResponse setCodeResponse = getSetCodeResponse(jsonResponse);

        if (setCodeResponse == null) {
            return;
        }

        System.out.println(setCodeResponse.toString());

        if (!setCodeResponse.isResultOk()) {

            TmpTestData.getInstance().signatureSuccess = new Boolean(false);

            return;

        } else {

            TmpTestData.getInstance().signatureSuccess = new Boolean(true);

        }

        //##############################################################################################################
        // terminateSignature
        try {

            TerminateSignatureRequest terminateSignatureRequest = new TerminateSignatureRequest.Builder().fromPaylineRequest(null, null);

            System.out.println(terminateSignatureRequest.toString());

            String scheme = ConfigProperties.get(CONFIG__SCHEME, ConfigEnvironment.DEV);
            String host = ConfigProperties.get(CONFIG__HOST, ConfigEnvironment.DEV);
            String path = ConfigProperties.get(CONFIG__PATH_WSSIGNATURE_TERMINATE_SIGNATURE);

            final StringResponse terminateSignatureResponse = docapostHttpClient.doPost(
                    scheme,
                    host,
                    path,
                    terminateSignatureRequest.getRequestBodyMap(),
                    credentials /*Credentials.basic(TmpTestData.AUTH_LOGIN, TmpTestData.AUTH_MDP)*/
            );

            System.out.println(terminateSignatureResponse);

            jsonResponse = terminateSignatureResponse.getContent().trim();

            System.out.println(jsonResponse);

        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        TerminateSignatureResponse terminateSignatureResponse = getTerminateSignatureResponse(jsonResponse);

        if (terminateSignatureResponse == null) {
            return;
        }

        System.out.println(terminateSignatureResponse.toString());

        if (!terminateSignatureResponse.isResultOk()) {

            return;

        } else {

            // Empty

        }

        //##############################################################################################################
        // orderCreate
        try {

            OrderCreateRequest orderCreateRequest = new OrderCreateRequest.Builder().fromPaylineRequest(null);

            System.out.println(orderCreateRequest.toString());

            requestBody = orderCreateRequest.buildBody();

            System.out.println("OrderCreateRequest XML body :");
            System.out.println(requestBody);

            String scheme = ConfigProperties.get(CONFIG__SCHEME, ConfigEnvironment.DEV);
            String host = ConfigProperties.get(CONFIG__HOST, ConfigEnvironment.DEV);
            String path = ConfigProperties.get(CONFIG__PATH_WSMANDATE_ORDER_CREATE);

            final StringResponse orderCreateResponse = docapostHttpClient.doPost(
                    scheme,
                    host,
                    path,
                    requestBody,
                    credentials /*Credentials.basic(TmpTestData.AUTH_LOGIN, TmpTestData.AUTH_MDP)*/
            );

            System.out.println(orderCreateResponse);

            xmlResponse = orderCreateResponse.getContent().trim();

            System.out.println(xmlResponse);

        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        AbstractXmlResponse orderCreateXmlResponse = getOrderCreateResponse(xmlResponse);

        if (orderCreateXmlResponse == null) {
            return;
        }

        if (!orderCreateXmlResponse.isResultOk()) {

            XmlErrorResponse xmlErrorResponse = (XmlErrorResponse) orderCreateXmlResponse;

            System.out.println(xmlErrorResponse.toString());

            return;

        } else {

            OrderCreateResponse orderCreateResponse = (OrderCreateResponse) orderCreateXmlResponse;

            System.out.println(orderCreateResponse.toString());

        }

    }

    private static AbstractXmlResponse getMandateCreateResponse(String xmlResponse) {

        XmlErrorResponse xmlErrorResponse = null;
        MandateCreateResponse mandateCreateResponse = null;

        if (xmlResponse.contains(MANDATE_WS_XML__SEPALIA_ERROR)) {

            xmlErrorResponse = new XmlErrorResponse.Builder().fromXml(xmlResponse);

            if (xmlErrorResponse != null) {
                return xmlErrorResponse;
            }

        }

        if (xmlResponse.contains(MANDATE_WS_XML__MANDATE_CREATE_DTO)) {

            mandateCreateResponse = new MandateCreateResponse.Builder().fromXml(xmlResponse);

            if (mandateCreateResponse != null) {
                return mandateCreateResponse;
            }

        }

        return null;

    }

    private static AbstractXmlResponse getOrderCreateResponse(String xmlResponse) {

        XmlErrorResponse xmlErrorResponse = null;
        OrderCreateResponse orderCreateResponse = null;

        if (xmlResponse.contains(MANDATE_WS_XML__SEPALIA_ERROR)) {

            xmlErrorResponse = new XmlErrorResponse.Builder().fromXml(xmlResponse);

            if (xmlErrorResponse != null) {
                return xmlErrorResponse;
            }

        }

        if (xmlResponse.contains(MANDATE_WS_XML__ORDER_CREATE_DTO)) {

            orderCreateResponse = new OrderCreateResponse.Builder().fromXml(xmlResponse);

            if (orderCreateResponse != null) {
                return orderCreateResponse;
            }

        }

        return null;

    }

    private static InitiateSignatureResponse getInitiateSignatureResponse(String jsonResponse) {
        return new InitiateSignatureResponse.Builder().fromJson(jsonResponse);
    }

    private static SendOtpResponse getSendOtpResponse(String jsonResponse) {
        return new SendOtpResponse.Builder().fromJson(jsonResponse);
    }

    private static SetCodeResponse getSetCodeResponse(String jsonResponse) {
        return new SetCodeResponse.Builder().fromJson(jsonResponse);
    }

    private static TerminateSignatureResponse getTerminateSignatureResponse(String jsonResponse) {
        return new TerminateSignatureResponse.Builder().fromJson(jsonResponse);
    }

}