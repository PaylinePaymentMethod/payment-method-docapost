package com.payline.payment.docapost.service.impl;

import com.payline.payment.docapost.bean.rest.request.RequestBuilderFactory;
import com.payline.payment.docapost.bean.rest.request.mandate.SctOrderCreateRequest;
import com.payline.payment.docapost.bean.rest.response.ResponseBuilderFactory;
import com.payline.payment.docapost.bean.rest.response.error.XmlErrorResponse;
import com.payline.payment.docapost.bean.rest.response.mandate.AbstractXmlResponse;
import com.payline.payment.docapost.bean.rest.response.mandate.WSCTOrderDTOResponse;
import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.service.AbstractRefundHttpService;
import com.payline.payment.docapost.utils.DocapostUtils;
import com.payline.payment.docapost.utils.PluginUtils;
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

import static com.payline.payment.docapost.utils.DocapostConstants.*;

public class RefundServiceImpl extends AbstractRefundHttpService<RefundRequest> implements RefundService {

    private static final Logger LOGGER = LogManager.getLogger(RefundServiceImpl.class);

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

        ConfigEnvironment env = PluginUtils.getEnvironnement(refundRequest);
        String scheme = ConfigProperties.get(CONFIG_SCHEME, env);
        String host = ConfigProperties.get(CONFIG_HOST, env);
        String path = ConfigProperties.get(CONFIG_PATH_WSMANDATE_SCTORDER_CREATE);

        // Recuperation des donnees necessaires pour la generation du Header Basic credentials des appels WS
        String authLogin = refundRequest.getPartnerConfiguration().getSensitiveProperties().get(PARTNER_CONFIG_AUTH_LOGIN);
        String authPass = refundRequest.getPartnerConfiguration().getSensitiveProperties().get(PARTNER_CONFIG_AUTH_PASS);

        // Generation des donnees du body de la requete
        String requestBody = sctOrderCreateRequest.buildBody();

        return this.httpClient.doPost(
                scheme,
                host,
                path,
                requestBody,
                DocapostUtils.generateBasicCredentials(authLogin, authPass)
        );

    }

    @Override
    public RefundResponse processResponse(StringResponse response) {

        AbstractXmlResponse sctOrderCreateXmlResponse = getSctOrderCreateResponse(response.getContent().trim());

        if (sctOrderCreateXmlResponse != null) {

            if (sctOrderCreateXmlResponse.isResultOk()) {

                WSCTOrderDTOResponse sctOrderCreateResponse = (WSCTOrderDTOResponse) sctOrderCreateXmlResponse;

                LOGGER.info("sctOrderCreateXmlResponse ok");
                return RefundResponseSuccess
                        .RefundResponseSuccessBuilder
                        .aRefundResponseSuccess()
                        .withStatusCode(sctOrderCreateResponse.getStatus())
                        .withPartnerTransactionId(sctOrderCreateResponse.getE2eId())
                        .build();

            }

            XmlErrorResponse xmlErrorResponse = (XmlErrorResponse) sctOrderCreateXmlResponse;

            WSRequestResultEnum wsRequestResult = WSRequestResultEnum.fromDocapostErrorCode(xmlErrorResponse.getException().getCode());

            LOGGER.info("sctOrderCreateXmlResponse ko");

            return RefundResponseFailure
                    .RefundResponseFailureBuilder
                    .aRefundResponseFailure()
                    .withErrorCode(wsRequestResult.getDocapostErrorCode())
                    .withFailureCause(wsRequestResult.getPaylineFailureCause())
//                    .withPartnerTransactionId()
                    .build();

        }

        // case : sctOrderCreateXmlResponse is null
        LOGGER.info("null sctOrderCreateXmlResponse");
        return RefundResponseFailure
                .RefundResponseFailureBuilder
                .aRefundResponseFailure()
                .withErrorCode("XML RESPONSE PARSING FAILED")
                .withFailureCause(FailureCause.INVALID_DATA)
                .build();


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
     *
     * @param xmlResponse the XML content
     * @return the AbstractXmlResponse
     */
    private static AbstractXmlResponse getSctOrderCreateResponse(String xmlResponse) {

        XmlErrorResponse xmlErrorResponse;
        WSCTOrderDTOResponse sctorderCreateResponse;

        if (xmlResponse.contains(MANDATE_WS_XML_SEPALIA_ERROR)) {

            xmlErrorResponse = ResponseBuilderFactory.buildXmlErrorResponse(xmlResponse);

            if (xmlErrorResponse != null) {
                return xmlErrorResponse;
            }

        }

        if (xmlResponse.contains(MANDATE_WS_XML_WS_SCT_ORDER_DTO)) {

            sctorderCreateResponse = ResponseBuilderFactory.buildWsctOrderDTOResponse(xmlResponse);

            if (sctorderCreateResponse != null) {
                return sctorderCreateResponse;
            }

        }

        return null;

    }

}