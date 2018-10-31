package com.payline.payment.docapost.service;

import com.payline.payment.docapost.TestUtils;
import com.payline.payment.docapost.service.impl.PaymentFormConfigurationServiceImpl;
import com.payline.pmapi.bean.paymentform.bean.PaymentFormLogo;
import com.payline.pmapi.bean.paymentform.request.PaymentFormConfigurationRequest;
import com.payline.pmapi.bean.paymentform.request.PaymentFormLogoRequest;
import com.payline.pmapi.bean.paymentform.response.configuration.PaymentFormConfigurationResponse;
import com.payline.pmapi.bean.paymentform.response.logo.PaymentFormLogoResponse;
import com.payline.pmapi.service.PaymentFormConfigurationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Locale;

import static org.mockito.Mockito.mock;

@RunWith( MockitoJUnitRunner.class )
public class PaymentFormConfigurationServiceImplTest {


    @InjectMocks
    private PaymentFormConfigurationService service = new PaymentFormConfigurationServiceImpl();

    @Test
    public void testGetPaymentFormConfiguration(){

        PaymentFormConfigurationRequest paymentFormConfigurationRequest = TestUtils.createDefaultPaymentFormConfigurationRequest();
        PaymentFormConfigurationResponse  paymentFormConfigurationResponse = service.getPaymentFormConfiguration(paymentFormConfigurationRequest);

        Assert.assertNotNull(paymentFormConfigurationResponse);
        //Assert Form contains Button and text ?
    }

    @Test
    public void testGetPaymentFormLogo(){

        PaymentFormLogoRequest paymentFormLogoRequest = mock(PaymentFormLogoRequest.class);
        PaymentFormLogoResponse paymentFormLogoResponse = service.getPaymentFormLogo(paymentFormLogoRequest);

        //TODO implement method
        Assert.assertEquals(null,paymentFormLogoResponse);
    }
    @Test
    public void testGetLogo(){
        String paymentMethodIdentifier= "Docapost" ;
        PaymentFormLogo paymentFormLogo = service.getLogo(paymentMethodIdentifier, Locale.FRANCE);
        //TODO implement method
        Assert.assertEquals(null,paymentFormLogo);

    }

}
