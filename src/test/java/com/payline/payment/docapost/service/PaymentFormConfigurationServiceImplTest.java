package com.payline.payment.docapost.service;

import com.payline.payment.docapost.TestUtils;
import com.payline.payment.docapost.service.impl.PaymentFormConfigurationServiceImpl;
import com.payline.pmapi.bean.paymentform.bean.PaymentFormLogo;
import com.payline.pmapi.bean.paymentform.request.PaymentFormConfigurationRequest;
import com.payline.pmapi.bean.paymentform.request.PaymentFormLogoRequest;
import com.payline.pmapi.bean.paymentform.response.configuration.PaymentFormConfigurationResponse;
import com.payline.pmapi.bean.paymentform.response.logo.PaymentFormLogoResponse;
import com.payline.pmapi.bean.paymentform.response.logo.impl.PaymentFormLogoResponseFile;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Locale;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaymentFormConfigurationServiceImplTest {


    @InjectMocks
    private PaymentFormConfigurationServiceImpl service = new PaymentFormConfigurationServiceImpl();

    @Test
    public void testGetPaymentFormConfiguration() {

        PaymentFormConfigurationRequest paymentFormConfigurationRequest = TestUtils.createDefaultPaymentFormConfigurationRequest();
        PaymentFormConfigurationResponse paymentFormConfigurationResponse = service.getPaymentFormConfiguration(paymentFormConfigurationRequest);

        Assert.assertNotNull(paymentFormConfigurationResponse);
        //Assert Form contains Button and text ?
    }

    @Test
    public void testGetPaymentFormLogo() {
        //Mock PaymentFormLogoRequest
        PaymentFormLogoRequest paymentFormLogoRequest = mock(PaymentFormLogoRequest.class);
        when(paymentFormLogoRequest.getLocale()).thenReturn(Locale.FRANCE);

        PaymentFormLogoResponse paymentFormLogoResponse = service.getPaymentFormLogo(paymentFormLogoRequest);

        Assert.assertNotNull(paymentFormLogoResponse);
        Assert.assertTrue(paymentFormLogoResponse instanceof PaymentFormLogoResponseFile);

        PaymentFormLogoResponseFile casted = (PaymentFormLogoResponseFile) paymentFormLogoResponse;
        Assert.assertEquals(200, casted.getHeight());
        Assert.assertEquals(200, casted.getWidth());
    }

    @Test
    public void testGetLogo() {
        // when: getLogo is called
        String paymentMethodIdentifier = "Docapost";
        PaymentFormLogo paymentFormLogo = service.getLogo(paymentMethodIdentifier, Locale.FRANCE);


        // then: returned elements are not null
        Assert.assertNotNull(paymentFormLogo.getFile());
        Assert.assertNotNull(paymentFormLogo.getContentType());

    }

}
