package com.payline.payment.docapost.bean.rest.response;

import com.payline.payment.docapost.bean.rest.response.error.XmlErrorResponse;
import com.payline.payment.docapost.bean.rest.response.mandate.WSCTOrderDTOResponse;
import com.payline.payment.docapost.bean.rest.response.mandate.WSDDOrderDTOResponse;
import com.payline.payment.docapost.bean.rest.response.mandate.WSMandateDTOResponse;
import com.payline.payment.docapost.bean.rest.response.signature.InitiateSignatureResponse;
import com.payline.payment.docapost.bean.rest.response.signature.SendOtpResponse;
import com.payline.payment.docapost.bean.rest.response.signature.SetCodeResponse;
import com.payline.payment.docapost.bean.rest.response.signature.TerminateSignatureResponse;

public class ResponseBuilderFactory {

    private ResponseBuilderFactory() {
        // ras.
    }

    public static XmlErrorResponse buildXmlErrorResponse(String xmlResponse) {
        return new XmlErrorResponse.Builder().fromXml(xmlResponse);
    }

    public static WSMandateDTOResponse buildWsMandateDTOResponse(String xmlResponse) {
        return new WSMandateDTOResponse.Builder().fromXml(xmlResponse);
    }

    public static WSDDOrderDTOResponse buildWsddOrderDTOResponse(String xmlResponse) {
        return new WSDDOrderDTOResponse.Builder().fromXml(xmlResponse);
    }

    public static WSCTOrderDTOResponse buildWsctOrderDTOResponse(String xmlResponse) {
        return new WSCTOrderDTOResponse.Builder().fromXml(xmlResponse);
    }

    public static InitiateSignatureResponse buildInitiateSignatureResponse(String jsonResponse) {
        return new InitiateSignatureResponse.Builder().fromJson(jsonResponse);
    }

    public static SendOtpResponse buildSendOtpResponse(String jsonResponse) {
        return new SendOtpResponse.Builder().fromJson(jsonResponse);
    }

    public static SetCodeResponse buildSetCodeResponse(String jsonResponse) {
        return new SetCodeResponse.Builder().fromJson(jsonResponse);
    }

    public static TerminateSignatureResponse buildTerminateSignatureResponse(String jsonResponse) {
        return new TerminateSignatureResponse.Builder().fromJson(jsonResponse);
    }

}