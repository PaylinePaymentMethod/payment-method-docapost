package com.payline.payment.docapost.service.impl;

import com.payline.payment.docapost.bean.rest.request.RequestBuilderFactory;
import com.payline.payment.docapost.bean.rest.request.mandate.SddOrderCancelRequest;
import com.payline.payment.docapost.bean.rest.response.ResponseBuilderFactory;
import com.payline.payment.docapost.bean.rest.response.error.XmlErrorResponse;
import com.payline.payment.docapost.bean.rest.response.mandate.AbstractXmlResponse;
import com.payline.payment.docapost.bean.rest.response.mandate.WSDDOrderDTOResponse;
import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.service.AbstractResetHttpService;
import com.payline.payment.docapost.utils.DocapostUtils;
import com.payline.payment.docapost.utils.PluginUtils;
import com.payline.payment.docapost.utils.config.ConfigEnvironment;
import com.payline.payment.docapost.utils.config.ConfigProperties;
import com.payline.payment.docapost.utils.http.StringResponse;
import com.payline.payment.docapost.utils.type.WSRequestResultEnum;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.reset.request.ResetRequest;
import com.payline.pmapi.bean.reset.response.ResetResponse;
import com.payline.pmapi.bean.reset.response.impl.ResetResponseFailure;
import com.payline.pmapi.bean.reset.response.impl.ResetResponseSuccess;
import com.payline.pmapi.service.ResetService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.payline.payment.docapost.utils.DocapostConstants.*;
import static com.payline.payment.docapost.utils.PluginUtils.URL_DELIMITER;

public class ResetServiceImpl extends AbstractResetHttpService<ResetRequest> implements ResetService {

    private static final Logger LOGGER = LogManager.getLogger(ResetServiceImpl.class);


    /**
     * Constructeur
     */
    public ResetServiceImpl() {
        super();
    }

    @Override
    public ResetResponse resetRequest(ResetRequest resetRequest) {
        return this.processRequest(resetRequest);
    }

    @Override
    public StringResponse createSendRequest(ResetRequest resetRequest) throws IOException, InvalidRequestException, URISyntaxException {

        // Initialisation de la requete Docapost
        SddOrderCancelRequest sddOrderCancelRequest = RequestBuilderFactory.buildSddOrderCancelRequest(resetRequest);

        ConfigEnvironment env = PluginUtils.getEnvironnement(resetRequest);
        String scheme = ConfigProperties.get(CONFIG_SCHEME, env);
        String host = ConfigProperties.get(CONFIG_HOST, env);
        String path = ConfigProperties.get(CONFIG_PATH_WSMANDATE_ORDER_CANCEL)
                + URL_DELIMITER + sddOrderCancelRequest.getCreditorId()
                + URL_DELIMITER + sddOrderCancelRequest.getRum()
                + URL_DELIMITER + sddOrderCancelRequest.getE2eId();


        LOGGER.info("Path {}", path);
        // Recuperation des donnees necessaires pour la generation du Header Basic credentials des appels WS
        String authLogin = resetRequest.getPartnerConfiguration().getSensitiveProperties().get(PARTNER_CONFIG_AUTH_LOGIN);
        String authPass = resetRequest.getPartnerConfiguration().getSensitiveProperties().get(PARTNER_CONFIG_AUTH_PASS);

        return this.httpClient.doGet(
                scheme,
                host,
                path,
                DocapostUtils.generateBasicCredentials(authLogin, authPass)
        );
    }

    @Override
    public ResetResponse processResponse(StringResponse response) {

        AbstractXmlResponse orderCancelXmlResponse = getOrderCancelResponse(response.getContent().trim());

        if (orderCancelXmlResponse != null) {

            if (orderCancelXmlResponse.isResultOk()) {

                LOGGER.info("orderCancelXmlResponse ok");

                WSDDOrderDTOResponse orderCancelResponse = (WSDDOrderDTOResponse) orderCancelXmlResponse;

                return ResetResponseSuccess
                        .ResetResponseSuccessBuilder
                        .aResetResponseSuccess()
                        .withStatusCode(orderCancelResponse.getStatus())
                        .withPartnerTransactionId(orderCancelResponse.getE2eId())
                        .build();

            }


            LOGGER.info("orderCancelXmlResponse ko");
            XmlErrorResponse xmlErrorResponse = (XmlErrorResponse) orderCancelXmlResponse;

            WSRequestResultEnum wsRequestResult = WSRequestResultEnum.fromDocapostErrorCode(xmlErrorResponse.getException().getCode());

            return ResetResponseFailure
                    .ResetResponseFailureBuilder
                    .aResetResponseFailure()
                    .withErrorCode(wsRequestResult.getDocapostErrorCode())
                    .withFailureCause(wsRequestResult.getPaylineFailureCause())
                    .build();


        }

        // case : orderCancelXmlResponse is null
        LOGGER.info("null orderCancelXmlResponse");
        return ResetResponseFailure
                .ResetResponseFailureBuilder
                .aResetResponseFailure()
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
    private static AbstractXmlResponse getOrderCancelResponse(String xmlResponse) {

        XmlErrorResponse xmlErrorResponse;
        WSDDOrderDTOResponse orderCancelResponse;

        if (xmlResponse.contains(MANDATE_WS_XML_SEPALIA_ERROR)) {

            xmlErrorResponse = ResponseBuilderFactory.buildXmlErrorResponse(xmlResponse);

            if (xmlErrorResponse != null) {
                return xmlErrorResponse;
            }

        }

        if (xmlResponse.contains(MANDATE_WS_XML_WS_SDD_ORDER_DTO)) {

            orderCancelResponse = ResponseBuilderFactory.buildWsddOrderDTOResponse(xmlResponse);

            if (orderCancelResponse != null) {
                return orderCancelResponse;
            }

        }

        return null;

    }

}