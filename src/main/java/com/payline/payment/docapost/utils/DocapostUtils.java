package com.payline.payment.docapost.utils;

import com.payline.payment.docapost.bean.rest.common.Debtor;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.payline.payment.docapost.utils.DocapostConstants.MANDATE_RUM_GENERATION_DATE_FORMAT;

public class DocapostUtils {

    private DocapostUtils() {
        // ras.
    }

    public static String generateMandateRum() {
        return new SimpleDateFormat(MANDATE_RUM_GENERATION_DATE_FORMAT).format(new Date());
    }

    public static String generateBasicCredentials(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
        return "Basic " + new String(encodedAuth);

    }

    public static Debtor defaultDebtor() {
        return new Debtor()
                .lastName("Toto")
                .firstName("Dupond")
                .iban("FR7630076020821234567890186")
                .street("666 rue Paradis")
                .postalCode("13008")
                .town("Marseille")
                .phoneNumber("+33601020304")
                .countryCode("FR");
    }

}