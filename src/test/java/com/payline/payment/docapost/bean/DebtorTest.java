package com.payline.payment.docapost.bean;

import com.payline.payment.docapost.bean.rest.common.Debtor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.MockitoAnnotations;

public class DebtorTest {


    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDebtor() {
        Debtor debtor = new Debtor()
                .lastName("NICOLAS")
                .firstName("M")
                .iban("FR7630076020821234567890186")
                .bic("TESTBIC")
                .street("25 RUE GAMBETTA")
                .postalCode("13130")
                .town("BERRE L'ETANG")
                .phoneNumber("0628692878")
                .countryCode("FR");


        Assert.assertEquals("NICOLAS", debtor.getLastName());
        Assert.assertEquals("M", debtor.getFirstName());
        Assert.assertEquals("TESTBIC", debtor.getBic());
        Assert.assertEquals("FR7630076020821234567890186", debtor.getIban());
        Assert.assertEquals("25 RUE GAMBETTA", debtor.getStreet());
        Assert.assertEquals("BERRE L'ETANG", debtor.getTown());
        Assert.assertEquals("0628692878", debtor.getPhoneNumber());
        Assert.assertEquals("FR", debtor.getCountryCode());
        Assert.assertNull(debtor.getComplement());
        Assert.assertNull(debtor.getComplement2());


    }


}
