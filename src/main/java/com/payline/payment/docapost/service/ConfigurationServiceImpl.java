package com.payline.payment.docapost.service;

import static com.payline.payment.docapost.utils.DocapostConstants.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.payline.payment.docapost.bean.rest.common.Debtor;
import com.payline.payment.docapost.bean.rest.request.mandate.MandateCreateRequest;
import com.payline.payment.docapost.utils.DocapostUtils;
import com.payline.payment.docapost.utils.config.ConfigEnvironment;
import com.payline.payment.docapost.utils.config.ConfigProperties;
import com.payline.payment.docapost.utils.http.DocapostHttpClient;
import com.payline.payment.docapost.utils.http.StringResponse;
import com.payline.payment.docapost.utils.i18n.I18nService;
import com.payline.pmapi.bean.configuration.ReleaseInformation;
import com.payline.pmapi.bean.configuration.parameter.AbstractParameter;
import com.payline.pmapi.bean.configuration.parameter.impl.InputParameter;
import com.payline.pmapi.bean.configuration.request.ContractParametersCheckRequest;
import com.payline.pmapi.service.ConfigurationService;

public class ConfigurationServiceImpl implements ConfigurationService {

    private static final Logger logger = LogManager.getLogger( ConfigurationServiceImpl.class );

    private I18nService i18n = I18nService.getInstance();

    private DocapostHttpClient httpClient = new DocapostHttpClient();

    @Override
    public List<AbstractParameter> getParameters(Locale locale ) {
        List<AbstractParameter> parameters = new ArrayList<>();

        // Creditor id
        final InputParameter merchantName = new InputParameter();
        merchantName.setKey(CONTRACT_CONFIG__CREDITOR_ID);
        merchantName.setLabel(CONTRACT_CONFIG__CREDITOR_ID_PROPERTY_LABEL);
        merchantName.setDescription(CONTRACT_CONFIG__CREDITOR_ID_PROPERTY_DESCRIPTION);
        merchantName.setRequired(true);

        // Auth login
        final InputParameter authLogin = new InputParameter();
        authLogin.setKey(PARTNER_CONFIG__AUTH_LOGIN);
        authLogin.setLabel(PARTNER_CONFIG__AUTH_LOGIN_PROPERTY_LABEL);
        authLogin.setDescription(PARTNER_CONFIG__AUTH_LOGIN_PROPERTY_DESCRIPTION);
        authLogin.setRequired(true);

        // Auth pwd
        final InputParameter authPwd = new InputParameter();
        authPwd.setKey(PARTNER_CONFIG__AUTH_PASS);
        authPwd.setLabel(PARTNER_CONFIG__AUTH_PASS_PROPERTY_LABEL);
        authPwd.setDescription(PARTNER_CONFIG__AUTH_PASS_PROPERTY_DESCRIPTION);
        authPwd.setRequired(true);

        return parameters;
    }

    @Override
    public Map<String, String> check( ContractParametersCheckRequest contractParametersCheckRequest ) {

        Locale locale = contractParametersCheckRequest.getLocale();
        final Map<String, String> accountInfo = contractParametersCheckRequest.getAccountInfo();
        final Map<String, String> errors = new HashMap<>();

        // Creditor id
        final String creditorId = accountInfo.get( CONTRACT_CONFIG__CREDITOR_ID );
        if( DocapostUtils.isEmpty(creditorId) ){
            errors.put( CONTRACT_CONFIG__CREDITOR_ID, i18n.getMessage( CONTRACT_CONFIG__CREDITOR_ID_ERROR, locale ) );
        }

        // Credential auth login
        final String authLogin = contractParametersCheckRequest.getPartnerConfiguration().getSensitiveProperties().get(PARTNER_CONFIG__AUTH_LOGIN);
        if( DocapostUtils.isEmpty(authLogin) ){
            errors.put( PARTNER_CONFIG__AUTH_LOGIN, i18n.getMessage( PARTNER_CONFIG__AUTH_LOGIN_ERROR, locale ) );
        }

        // Credential auth password
        final String authPass = contractParametersCheckRequest.getPartnerConfiguration().getSensitiveProperties().get(PARTNER_CONFIG__AUTH_PASS);
        if( DocapostUtils.isEmpty(authPass) ){
            errors.put( PARTNER_CONFIG__AUTH_PASS, i18n.getMessage(PARTNER_CONFIG__AUTH_PASS_ERROR, locale ) );
        }

        // No need to go forward if there is an error at this point
        if( errors.size() > 0 ){
            return errors;
        }

        // Initialize a fake transaction request to check the validity of the contract parameters
        MandateCreateRequest mandateCreateRequest = new MandateCreateRequest(
                creditorId,
                DocapostUtils.generateMandateRum(),
                false,
                "fr",
                new Debtor()
                    .lastName("Toto")
                    .firstName("Dupond")
                    .iban("FR7630076020821234567890186")
                    .street("666 rue Paradis")
                    .postalCode("13008")
                    .town("Marseille")
                    .phoneNumber("+33601020304")
                    .countryCode("FR")
        );

        // Check the connection
        ConfigEnvironment env = Boolean.FALSE.equals( contractParametersCheckRequest.getPaylineEnvironment().isSandbox() ) ? ConfigEnvironment.PROD : ConfigEnvironment.DEV;
        String scheme = ConfigProperties.get(CONFIG__SCHEME, env);
        String host = ConfigProperties.get(CONFIG__HOST, env);
        String path = ConfigProperties.get(CONFIG__PATH_WSMANDATE_MANDATE_CREATE);

        try {

            final StringResponse mandateCreateResponse = this.httpClient.doPost(
                    scheme,
                    host,
                    path,
                    mandateCreateRequest.buildBody(),
                    DocapostUtils.generateBasicCredentials(authLogin, authPass)
            );

            if ( mandateCreateResponse != null && mandateCreateResponse.getCode() == HTTP_OK && mandateCreateResponse.getContent() != null ) {

            } else {
                String message = "Can't read a correct response from TSI server.";
                if( mandateCreateResponse != null ){
                    message += " HTTP status: " + mandateCreateResponse.getCode();
                }
                throw new Exception( message );
            }

        } catch( Exception e ){
            this.logger.error( "An error occurred sending the validation request to the Docapost server: " + e.getMessage() );
            errors.put( CONTRACT_CONFIG__CREDITOR_ID, i18n.getMessage( CONTRACT_CONFIG__CREDITOR_ID_ERROR, locale ) );
            errors.put( PARTNER_CONFIG__AUTH_LOGIN, i18n.getMessage( PARTNER_CONFIG__AUTH_LOGIN_ERROR, locale ) );
            errors.put( PARTNER_CONFIG__AUTH_PASS, i18n.getMessage( PARTNER_CONFIG__AUTH_PASS_ERROR, locale ) );
        }

        return errors;
    }

    @Override
    public ReleaseInformation getReleaseInformation() {
        Properties props = new Properties();
        try {
            props.load( ConfigurationServiceImpl.class.getClassLoader().getResourceAsStream( RELEASE_PROPERTIES ) );
        } catch( IOException e ){
            this.logger.error("An error occurred reading the file: " + RELEASE_PROPERTIES );
            props.setProperty( RELEASE_VERSION, "unknown" );
            props.setProperty( RELEASE_DATE, "01/01/1900" );
        }

        LocalDate date = LocalDate.parse( props.getProperty( RELEASE_DATE ), DateTimeFormatter.ofPattern( RELEASE_DATE_FORMAT ) );
        return ReleaseInformation.ReleaseBuilder.aRelease()
                .withDate( date )
                .withVersion( props.getProperty( RELEASE_VERSION ) )
                .build();
    }

    @Override
    public String getName(Locale locale) {
        return this.i18n.getMessage( PAYMENT_METHOD_NAME, locale );
    }

}
