package com.payline.payment.docapost.utils;

import com.payline.payment.docapost.utils.config.ConfigProperties;
import org.junit.Test;

public class ConfigPropertiesTest {

    private ConfigProperties configProperties;
    String key;


    @Test
    public void getFromKeyKO() {

        key = ConfigProperties.get("BadKey");
    }


    @Test
    public void getFromKeyOK() {

    }

    @Test
    public void readProperty() {


    }
}
