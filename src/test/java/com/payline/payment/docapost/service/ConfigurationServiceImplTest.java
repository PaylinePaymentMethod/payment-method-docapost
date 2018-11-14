package com.payline.payment.docapost.service;

import com.payline.payment.docapost.service.impl.ConfigurationServiceImpl;
import com.payline.payment.docapost.utils.http.DocapostHttpClient;
import com.payline.payment.docapost.utils.http.StringResponse;
import com.payline.pmapi.bean.configuration.ReleaseInformation;
import com.payline.pmapi.bean.configuration.parameter.AbstractParameter;
import com.payline.pmapi.bean.configuration.request.ContractParametersCheckRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.payline.payment.docapost.TestUtils.createContractParametersCheckRequest;
import static com.payline.payment.docapost.utils.DocapostConstants.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationServiceImplTest {


    @InjectMocks
    private ConfigurationServiceImpl service = new ConfigurationServiceImpl();
    @Mock
    private DocapostHttpClient httpClient;

    @Test
    public void test() {
        Assert.assertTrue(true);
    }

    @Test
    public void checkGood() throws IOException, URISyntaxException {
        StringResponse defaultResponse = new StringResponse();
        defaultResponse.setCode(HTTP_OK);
        defaultResponse.setContent("post OK");
        when((httpClient).doPost(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(defaultResponse);

        // when: checking configuration fields values
        ContractParametersCheckRequest contractParametersCheckRequest = createContractParametersCheckRequest();
        Map<String, String> errors = service.check(contractParametersCheckRequest);

        // then: result contains no error
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testCheck_wrongAccountData() throws IOException, URISyntaxException {
        // given: invalid contract properties
        ContractParametersCheckRequest contractParametersCheckRequest = createContractParametersCheckRequest();

        //Mocked http response
        StringResponse defaultResponse = new StringResponse();
        defaultResponse.setCode(401);
        defaultResponse.setContent("Unauthorized");

        when((httpClient).doPost(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(defaultResponse);
        Map<String, String> errors = service.check(contractParametersCheckRequest);

        // then: result contains  errors
        Assert.assertTrue(errors.size() > 0);
        Assert.assertEquals(3, errors.size());
    }


    @Test
    public void testCheck_null() throws IOException, URISyntaxException {
        // given: invalid contract properties
        ContractParametersCheckRequest contractParametersCheckRequest = createContractParametersCheckRequest();
        when((httpClient).doPost(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(null);
        Map<String, String> errors = service.check(contractParametersCheckRequest);

        // then: result contains  errors
        Assert.assertTrue(errors.size() > 0);
        Assert.assertEquals(3, errors.size());
    }

    @Test
    public void testGetParameters() {

        List<AbstractParameter> parameters = service.getParameters(Locale.FRANCE);
        //Assert we have 3 parameters
        Assert.assertNotNull(parameters);
        Assert.assertEquals(3, parameters.size());
        Assert.assertEquals(CONTRACT_CONFIG_CREDITOR_ID, parameters.get(0).getKey());
        Assert.assertEquals(PARTNER_CONFIG_AUTH_LOGIN, parameters.get(1).getKey());
        Assert.assertEquals(PARTNER_CONFIG_AUTH_PASS, parameters.get(2).getKey());

    }

    @Test
    public void testGetReleaseInformation_ok() {
        // when: getReleaseInformation method is called
        ReleaseInformation releaseInformation = service.getReleaseInformation();

        // then: result is not null
        Assert.assertNotNull(releaseInformation);
        // then: assert release version and release date are not null
        Assert.assertNotNull(releaseInformation.getVersion());
        Assert.assertFalse(releaseInformation.getVersion().isEmpty());

        Assert.assertNotNull(releaseInformation.getDate());
    }

    //
    @Test
    public void testGetReleaseInformation_versionFormat() {
        // when: getReleaseInformation method is called
        ReleaseInformation releaseInformation = service.getReleaseInformation();

        // then: the version has a valid format
        Assert.assertNotNull(releaseInformation);
        Assert.assertTrue(releaseInformation.getVersion().matches("^\\d\\.\\d(\\.\\d)?$"));
    }

    @Test
    public void testGetName_notNull() {
        // when: getReleaseInformation method is called
        String name = service.getName(Locale.FRANCE);

        // then: result is not null and not empty
        Assert.assertNotNull(name);
        Assert.assertFalse(name.isEmpty());
    }

}
