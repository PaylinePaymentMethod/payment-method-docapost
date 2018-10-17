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
import com.payline.payment.docapost.utils.config.ConfigEnvironment;
import com.payline.payment.docapost.utils.config.ConfigProperties;
import com.payline.payment.docapost.utils.http.DocapostHttpClient;
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

public class ResetServiceImpl extends AbstractResetHttpService<ResetRequest> implements ResetService {

    private static final Logger logger = LogManager.getLogger( ResetServiceImpl.class );

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

        ConfigEnvironment env = Boolean.FALSE.equals( resetRequest.getEnvironment().isSandbox() ) ? ConfigEnvironment.PROD : ConfigEnvironment.DEV;
        String scheme = ConfigProperties.get(CONFIG__SCHEME, env);
        String host = ConfigProperties.get(CONFIG__HOST, env);
        String path = ConfigProperties.get(CONFIG__PATH_WSMANDATE_ORDER_CANCEL)
                + "/" + sddOrderCancelRequest.getCreditorId()
                + "/" + sddOrderCancelRequest.getRum()
                + "/" + sddOrderCancelRequest.getE2eId();

        // Recuperation des donnees necessaires pour la generation du Header Basic credentials des appels WS
        String authLogin = resetRequest.getPartnerConfiguration().getSensitiveProperties().get(PARTNER_CONFIG__AUTH_LOGIN);
        String authPass = resetRequest.getPartnerConfiguration().getSensitiveProperties().get(PARTNER_CONFIG__AUTH_PASS);

        return this.httpClient.doGet(
                scheme,
                host,
                path,
                DocapostUtils.generateBasicCredentials(authLogin, authPass)
        );
    }

    @Override
    public ResetResponse processResponse(StringResponse response) throws IOException {

        AbstractXmlResponse orderCancelXmlResponse = getOrderCancelResponse(response.getContent().trim());

        if (orderCancelXmlResponse != null) {

            if (orderCancelXmlResponse.isResultOk()) {

                WSDDOrderDTOResponse orderCancelResponse = (WSDDOrderDTOResponse) orderCancelXmlResponse;

                return ResetResponseSuccess
                        .ResetResponseSuccessBuilder
                        .aResetResponseSuccess()
                        .withStatusCode(orderCancelResponse.getStatus())
                        .withPartnerTransactionId(orderCancelResponse.getE2eId())
                        .build();

            } else {

                XmlErrorResponse xmlErrorResponse = (XmlErrorResponse) orderCancelXmlResponse;

                WSRequestResultEnum wsRequestResult = WSRequestResultEnum.fromDocapostErrorCode(xmlErrorResponse.getException().getCode());

                return ResetResponseFailure
                        .ResetResponseFailureBuilder
                        .aResetResponseFailure()
                        .withErrorCode(wsRequestResult.getDocapostErrorCode())
                        .withFailureCause(wsRequestResult.getPaylineFailureCause())
                        // FIXME : Add fields ?
                        //.withTransactionId()
                        .build();

            }

        } else {

            return ResetResponseFailure
                    .ResetResponseFailureBuilder
                    .aResetResponseFailure()
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
    private static AbstractXmlResponse getOrderCancelResponse(String xmlResponse) {

        XmlErrorResponse xmlErrorResponse = null;
        WSDDOrderDTOResponse orderCancelResponse = null;

        if (xmlResponse.contains(MANDATE_WS_XML__SEPALIA_ERROR)) {

            xmlErrorResponse = ResponseBuilderFactory.buildXmlErrorResponse(xmlResponse);

            if (xmlErrorResponse != null) {
                return xmlErrorResponse;
            }

        }

        if (xmlResponse.contains(MANDATE_WS_XML__WS_SDD_ORDER_DTO)) {

            orderCancelResponse = ResponseBuilderFactory.buildWsddOrderDTOResponse(xmlResponse);

            if (orderCancelResponse != null) {
                return orderCancelResponse;
            }

        }

        return null;

    }

}