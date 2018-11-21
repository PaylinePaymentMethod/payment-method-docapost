package com.payline.payment.docapost.utils.http;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Thales on 06/09/2018.
 */
public class DocapostHttpClient extends AbstractHttpClient {

    /**
     * Instantiate a HTTP client with default values.
     */
    private DocapostHttpClient() {
        super();
    }

    /**
     * Singleton Holder
     */
    private static class SingletonHolder {
        private static final DocapostHttpClient INSTANCE = new DocapostHttpClient();
    }

    /**
     * @return the singleton instance
     */
    public static DocapostHttpClient getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * Send a POST request, with a XML content type.
     *
     * @param scheme     URL scheme
     * @param host       URL host
     * @param path       URL path
     * @param xmlContent The JSON content, as a string
     * @param credential The authentication credential
     * @return The response returned from the HTTP call
     * @throws IOException        I/O error
     * @throws URISyntaxException URI Syntax Exception
     */
    public StringResponse doPost(String scheme, String host, String path, String xmlContent, String credential) throws IOException, URISyntaxException {

        StringEntity entity = new StringEntity(xmlContent);

        return super.doPost(scheme, host, path, entity, ContentType.APPLICATION_XML.toString(), credential);

    }

    /**
     * Send a POST request, with a XML content type.
     *
     * @param scheme     URL scheme
     * @param host       URL host
     * @param path       URL path
     * @param body       The urlencoded parameter list
     * @param credential The authentication credential
     * @return The response returned from the HTTP call
     * @throws IOException        I/O error
     * @throws URISyntaxException URI Syntax Exception
     */
    public StringResponse doPost(String scheme, String host, String path, Map<String, String> body, String credential) throws IOException, URISyntaxException {

        ArrayList<NameValuePair> parameters = new ArrayList<>();
        for (Map.Entry<String, String> entry : body.entrySet()) {
            parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        InputStreamEntity entity = new InputStreamEntity(new UrlEncodedFormEntity(parameters).getContent());

        return super.doPost(scheme, host, path, entity, ContentType.APPLICATION_FORM_URLENCODED.toString(), credential);

    }

    /**
     * Send a GET request, with a XML content type.
     *
     * @param scheme     URL scheme
     * @param host       URL host
     * @param path       URL path
     * @param credential The authentication credential
     * @return The response returned from the HTTP call
     * @throws IOException        I/O error
     * @throws URISyntaxException URI Syntax Exception
     */
    public StringResponse doGet(String scheme, String host, String path, String credential) throws IOException, URISyntaxException {
        return super.doGet(scheme, host, path, ContentType.APPLICATION_XML.toString(), credential);
    }

}