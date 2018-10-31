package com.payline.payment.docapost.bean;

import com.payline.payment.docapost.bean.rest.common.Debtor;
import org.junit.Assert;
import org.junit.Test;

public class DebtorTest {
    @Test
    public void testDebtor (){

        Debtor debtor = new Debtor ("NICOLAS",
                "M",
                "TESTBIC",
                 "FR7630076020821234567890186",
               "25 RUE GAMBETTA",
                null,
                null,
                "13130",
                "BERRE L'ETANG",
                "0628692878",
                "FR");

        Assert.assertEquals("NICOLAS", debtor.getLastName());
        Assert.assertEquals("M", debtor.getFirstName());
        Assert.assertEquals("TESTBIC", debtor.getBic());
        Assert.assertEquals("FR7630076020821234567890186", debtor.getIban());
        Assert.assertEquals("25 RUE GAMBETTA", debtor.getStreet());
        Assert.assertEquals("BERRE L'ETANG", debtor.getTown());
        Assert.assertEquals("0628692878", debtor.getPhoneNumber());
        Assert.assertEquals("FR", debtor.getCountryCode());
        Assert.assertEquals(null, debtor.getComplement());
        Assert.assertEquals(null, debtor.getComplement2());



    }


}
