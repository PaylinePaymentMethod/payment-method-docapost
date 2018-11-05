package com.payline.payment.docapost.bean.rest.request;

import com.payline.payment.docapost.bean.rest.request.mandate.*;
import com.payline.payment.docapost.bean.rest.request.signature.InitiateSignatureRequest;
import com.payline.payment.docapost.bean.rest.request.signature.SendOtpRequest;
import com.payline.payment.docapost.bean.rest.request.signature.SetCodeRequest;
import com.payline.payment.docapost.bean.rest.request.signature.TerminateSignatureRequest;
import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.utils.DocapostLocalParam;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.refund.request.RefundRequest;
import com.payline.pmapi.bean.reset.request.ResetRequest;

public class RequestBuilderFactory {

    private RequestBuilderFactory() {
        // ras.
    }

    public static MandateCreateRequest buildMandateCreateRequest(PaymentRequest request) throws InvalidRequestException {
        return new MandateCreateRequest.Builder().fromPaylineRequest(request);
    }

    public static SddOrderCreateRequest buildSddOrderCreateRequest(PaymentRequest request) throws InvalidRequestException {
        return new SddOrderCreateRequest.Builder().fromPaylineRequest(request);
    }

    public static SddOrderCancelRequest buildSddOrderCancelRequest(ResetRequest request) throws InvalidRequestException {
        return new SddOrderCancelRequest.Builder().fromPaylineRequest(request);
    }

    public static SctOrderCreateRequest buildSctOrderCreateRequest(RefundRequest request) throws InvalidRequestException {
        return new SctOrderCreateRequest.Builder().fromPaylineRequest(request);
    }

    public static SctOrderCancelRequest buildSctOrderCancelRequest(ResetRequest request) throws InvalidRequestException {
        return new SctOrderCancelRequest.Builder().fromPaylineRequest(request);
    }

    public static InitiateSignatureRequest buildInitiateSignatureRequest(PaymentRequest request, DocapostLocalParam param) throws InvalidRequestException {
        return new InitiateSignatureRequest.Builder().fromPaylineRequest(request, param);
    }

    public static SendOtpRequest buildSendOtpRequest(PaymentRequest request, DocapostLocalParam param) throws InvalidRequestException {
        return new SendOtpRequest.Builder().fromPaylineRequest(request, param);
    }

    public static SetCodeRequest buildSetCodeRequest(PaymentRequest request) throws InvalidRequestException {
        return new SetCodeRequest.Builder().fromPaylineRequest(request);
    }

    public static TerminateSignatureRequest buildTerminateSignatureRequest(PaymentRequest request, DocapostLocalParam param) throws InvalidRequestException {
        return new TerminateSignatureRequest.Builder().fromPaylineRequest(request, param);
    }

}