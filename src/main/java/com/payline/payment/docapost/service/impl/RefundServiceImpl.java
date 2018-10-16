package com.payline.payment.docapost.service.impl;

import com.payline.payment.docapost.bean.rest.request.RequestBuilderFactory;
import com.payline.payment.docapost.bean.rest.request.mandate.SctOrderCreateRequest;
import com.payline.payment.docapost.bean.rest.response.ResponseBuilderFactory;
import com.payline.payment.docapost.bean.rest.response.error.XmlErrorResponse;
import com.payline.payment.docapost.bean.rest.response.mandate.AbstractXmlResponse;
import com.payline.payment.docapost.bean.rest.response.mandate.WSCTOrderDTOResponse;
import com.payline.payment.docapost.bean.rest.response.mandate.WSDDOrderDTOResponse;
import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.service.AbstractRefundHttpService;
import com.payline.payment.docapost.utils.DocapostUtils;
import com.payline.payment.docapost.utils.config.ConfigEnvironment;
import com.payline.payment.docapost.utils.config.ConfigProperties;
import com.payline.payment.docapost.utils.http.StringResponse;
import com.payline.payment.docapost.utils.type.WSRequestResultEnum;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.refund.request.RefundRequest;
import com.payline.pmapi.bean.refund.response.RefundResponse;
import com.payline.pmapi.bean.refund.response.impl.RefundResponseFailure;
import com.payline.pmapi.bean.refund.response.impl.RefundResponseSuccess;
import com.payline.pmapi.service.RefundService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

import static com.payline.payment.docapost.utils.DocapostConstants.*;
import static com.payline.payment.docapost.utils.DocapostConstants.CONFIG__PATH_WSMANDATE_ORDER_CANCEL;

public class RefundServiceImpl extends AbstractRefundHttpService<RefundRequest> implements RefundService {

    private static final Logger logger = LogManager.getLogger( RefundServiceImpl.class );

    /**
     * Constructeur
     */
    public RefundServiceImpl() {
        super();
    }

    @Override
    public RefundResponse refundRequest(RefundRequest refundRequest) {
        return this.processRequest(refundRequest);
    }

    @Override
    public StringResponse createSendRequest(RefundRequest refundRequest) throws IOException, InvalidRequestException, URISyntaxException {

        // Initialisation de la requete Docapost
        SctOrderCreateRequest sctOrderCreateRequest = RequestBuilderFactory.buildSctOrderCreateRequest(refundRequest);

        ConfigEnvironment env = Boolean.FALSE.equals( refundRequest.getPaylineEnvironment().isSandbox() ) ? ConfigEnvironment.PROD : ConfigEnvironment.DEV;
        String scheme = ConfigProperties.get(CONFIG__SCHEME, env);
        String host = ConfigProperties.get(CONFIG__HOST, env);
        String path = ConfigProperties.get(CONFIG__PATH_WSMANDATE_SCTORDER_CREATE);

        // Recuperation des donnees necessaires pour la generation du Header Basic credentials des appels WS
        String username = refundRequest.getPartnerConfiguration().getSensitiveProperties().get(PARTNER_CONFIG__AUTH_LOGIN);
        String pass = refundRequest.getPartnerConfiguration().getSensitiveProperties().get(PARTNER_CONFIG__AUTH_PASS);

        // Generation des credentials
        String credentials = DocapostUtils.generateBasicCredentials(username, pass);

        // Generation des donnees du body de la requete
        String requestBody = sctOrderCreateRequest.buildBody();

        return this.httpClient.doPost(
                scheme,
                host,
                path,
                requestBody,
                credentials
        );

    }

    @Override
    public RefundResponse processResponse(StringResponse response) throws IOException {

        AbstractXmlResponse sctOrderCreateXmlResponse = getSctOrderCreateResponse(response.getContent().trim());

        if (sctOrderCreateXmlResponse != null) {

            if (sctOrderCreateXmlResponse.isResultOk()) {

                WSCTOrderDTOResponse sctorderCreateResponse = (WSCTOrderDTOResponse) sctOrderCreateXmlResponse;

                return RefundResponseSuccess
                        .RefundResponseSuccessBuilder
                        .aRefundResponseSuccess()
                        .withStatusCode(sctorderCreateResponse.getStatus())
                        .withTransactionId(sctorderCreateResponse.getE2eId())
                        .build();

            } else {

                XmlErrorResponse xmlErrorResponse = (XmlErrorResponse) sctOrderCreateXmlResponse;

                WSRequestResultEnum wsRequestResult = WSRequestResultEnum.fromDocapostErrorCode(xmlErrorResponse.getException().getCode());

                return RefundResponseFailure
                        .RefundResponseFailureBuilder
                        .aRefundResponseFailure()
                        .withErrorCode(wsRequestResult.getDocapostErrorCode())
                        .withFailureCause(wsRequestResult.getPaylineFailureCause())
                        // FIXME : Add fields ?
                        //.withTransactionId()
                        .build();

            }

        } else {

            return RefundResponseFailure
                    .RefundResponseFailureBuilder
                    .aRefundResponseFailure()
                    .withErrorCode("XML RESPONSE PARSING FAILED")
                    .withFailureCause(FailureCause.INVALID_DATA)
                    // FIXME : Add fields ?
                    //.withTransactionId()
                    .build();

        }

    }

    @Override
    public boolean canMultiple() {
        return false;
    }

    @Override
    public boolean canPartial() {
        return false;
    }

    /**
     * Return a AbstractXmlResponse (WSDDOrderDTOResponse or XmlErrorResponse in case of error) based on a XML content
     * @param xmlResponse the XML content
     * @return the AbstractXmlResponse
     */
    private static AbstractXmlResponse getSctOrderCreateResponse(String xmlResponse) {

        XmlErrorResponse xmlErrorResponse = null;
        WSCTOrderDTOResponse sctorderCreateResponse = null;

        if (xmlResponse.contains(MANDATE_WS_XML__SEPALIA_ERROR)) {

            xmlErrorResponse = ResponseBuilderFactory.buildXmlErrorResponse(xmlResponse);

            if (xmlErrorResponse != null) {
                return xmlErrorResponse;
            }

        }

        if (xmlResponse.contains(MANDATE_WS_XML__WS_SCT_ORDER_DTO)) {

            sctorderCreateResponse = ResponseBuilderFactory.buildWsctOrderDTOResponse(xmlResponse);

            if (sctorderCreateResponse != null) {
                return sctorderCreateResponse;
            }

        }

        return null;

    }

}