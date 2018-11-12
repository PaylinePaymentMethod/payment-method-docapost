package com.payline.payment.docapost.utils.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.payline.payment.docapost.utils.DocapostConstants.AUTHORIZATION;
import static com.payline.payment.docapost.utils.DocapostConstants.CONTENT_TYPE;

/**
 * This utility class provides a basic HTTP client to send requests, using OkHttp library.
 * It must be extended to match each payment method needs.
 */
public abstract class AbstractHttpClient {

    private CloseableHttpClient client;

    /**
     * Instantiate a HTTP client.
     *
     * @param connectTimeout Determines the timeout in milliseconds until a connection is established
     * @param requestTimeout The timeout in milliseconds used when requesting a connection from the connection manager
     * @param socketTimeout  Defines the socket timeout (SO_TIMEOUT) in milliseconds, which is the timeout for waiting for data or, put differently, a maximum period inactivity between two consecutive data packets)
     */
    protected AbstractHttpClient(int connectTimeout, int requestTimeout, int socketTimeout) {

        final RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout * 1000)
                .setConnectionRequestTimeout(requestTimeout * 1000)
                .setSocketTimeout(socketTimeout * 1000)
                .build();

        this.client = HttpClientBuilder.create()
                .useSystemProperties()
                .setDefaultRequestConfig(requestConfig)
                .setDefaultCredentialsProvider(new BasicCredentialsProvider())
                .setSSLSocketFactory(new SSLConnectionSocketFactory(HttpsURLConnection.getDefaultSSLSocketFactory(), SSLConnectionSocketFactory.getDefaultHostnameVerifier())).build();

    }

    /**
     * Send a POST request.
     *
     * @param scheme      URL scheme
     * @param host        URL host
     * @param path        URL path
     * @param body        Request body
     * @param contentType The content type of the request body
     * @param credential  The authentication credential
     * @return The response returned from the HTTP call
     * @throws IOException        I/O error
     * @throws URISyntaxException URI Syntax Exception
     */
    protected StringResponse doPost(String scheme, String host, String path, HttpEntity body, String contentType, String credential) throws IOException, URISyntaxException {

        final URI uri = new URIBuilder()
                .setScheme(scheme)
                .setHost(host)
                .setPath(path)
                .build();

        Header[] headers = new Header[]{new BasicHeader(AUTHORIZATION, credential), new BasicHeader(CONTENT_TYPE, contentType)};

        final HttpPost httpPostRequest = new HttpPost(uri);
        httpPostRequest.setHeaders(headers);
        httpPostRequest.setEntity(body);

        try (CloseableHttpResponse httpResponse = this.client.execute(httpPostRequest)) {

            final StringResponse strResponse = new StringResponse();
            strResponse.setCode(httpResponse.getStatusLine().getStatusCode());
            strResponse.setMessage(httpResponse.getStatusLine().getReasonPhrase());

            if (httpResponse.getEntity() != null) {
                final String responseAsString = EntityUtils.toString(httpResponse.getEntity());
                strResponse.setContent(responseAsString);
            }

            return strResponse;

        }

    }

    /**
     * Send a GET request
     *
     * @param scheme      URL scheme
     * @param host        URL host
     * @param path        URL path
     * @param contentType The content type of the request body
     * @param credential  The authentication credential
     * @return The response returned from the HTTP call
     * @throws IOException        I/O error
     * @throws URISyntaxException URI Syntax Exception
     */
    protected StringResponse doGet(String scheme, String host, String path, String contentType, String credential) throws IOException, URISyntaxException {

        final URI uri = new URIBuilder()
                .setScheme(scheme)
                .setHost(host)
                .setPath(path)
                .build();

        Header[] headers = new Header[]{new BasicHeader(AUTHORIZATION, credential), new BasicHeader(CONTENT_TYPE, contentType)};

        final HttpGet httpGetRequest = new HttpGet(uri);
        httpGetRequest.setHeaders(headers);

        try (CloseableHttpResponse httpResponse = this.client.execute(httpGetRequest)) {

            final StringResponse strResponse = new StringResponse();
            strResponse.setCode(httpResponse.getStatusLine().getStatusCode());
            strResponse.setMessage(httpResponse.getStatusLine().getReasonPhrase());

            if (httpResponse.getEntity() != null) {
                final String responseAsString = EntityUtils.toString(httpResponse.getEntity());
                strResponse.setContent(responseAsString);
            }

            return strResponse;

        }

    }

}
