package com.payline.payment.docapost.utils;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.payline.payment.docapost.utils.DocapostConstants.MANDATE_RUM_GENERATION_DATE_FORMAT;

public class DocapostUtils {

    private DocapostUtils() {
        // ras.
    }

    public static boolean isEmpty(String s) {

        return s == null || s.isEmpty();
    }

    public static String generateMandateRum() {
        return new SimpleDateFormat(MANDATE_RUM_GENERATION_DATE_FORMAT).format(new Date());
    }

    public static String generateBasicCredentials(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
        String credentials = "Basic " + new String(encodedAuth);
        return credentials;
    }

}