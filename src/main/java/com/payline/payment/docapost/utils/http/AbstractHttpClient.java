package com.payline.payment.docapost.utils.http;

import com.payline.payment.docapost.utils.config.ConfigProperties;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.payline.payment.docapost.utils.DocapostConstants.*;

/**
 * This utility class provides a basic HTTP client to send requests, using OkHttp library.
 * It must be extended to match each payment method needs.
 */
public abstract class AbstractHttpClient {

    private CloseableHttpClient client;
    private static final Logger LOGGER = LogManager.getLogger(AbstractHttpClient.class);

    /**
     * Instantiate a HTTP client.
     */

    protected AbstractHttpClient() {

        int connectTimeout = Integer.parseInt(ConfigProperties.get(CONFIG_HTTP_CONNECT_TIMEOUT));
        int requestTimeout = Integer.parseInt(ConfigProperties.get(CONFIG_HTTP_WRITE_TIMEOUT));
        int readTimeout = Integer.parseInt(ConfigProperties.get(CONFIG_HTTP_READ_TIMEOUT));


        final RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout * 1000)
                .setConnectionRequestTimeout(requestTimeout * 1000)
                .setSocketTimeout(readTimeout * 1000)
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

        final long start = System.currentTimeMillis();
        int count = 0;
        StringResponse strResponse = null;
        while (count < 3 && strResponse == null) {
            try (CloseableHttpResponse httpResponse = this.client.execute(httpPostRequest)) {

                LOGGER.info("Start partner call... [HOST: {}]", host);

                strResponse = new StringResponse();
                strResponse.setCode(httpResponse.getStatusLine().getStatusCode());
                strResponse.setMessage(httpResponse.getStatusLine().getReasonPhrase());

                if (httpResponse.getEntity() != null) {
                    final String responseAsString = EntityUtils.toString(httpResponse.getEntity());
                    strResponse.setContent(responseAsString);
                }
                final long end = System.currentTimeMillis();

                LOGGER.info("End partner call [T: {}ms] [CODE: {}]", end - start, strResponse.getCode());

            } catch (final IOException e) {
                LOGGER.error("Error while partner call [T: {}ms]", System.currentTimeMillis() - start, e);
                strResponse = null;
            } finally {
                count++;
            }
        }

        if (strResponse == null) {
            throw new IOException("Partner response empty");
        }
        return strResponse;

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

        final long start = System.currentTimeMillis();
        int count = 0;
        StringResponse strResponse = null;
        while (count < 3 && strResponse == null) {
            try (CloseableHttpResponse httpResponse = this.client.execute(httpGetRequest)) {

                LOGGER.info("Start partner call... [HOST: {}]", host);

                strResponse = new StringResponse();
                strResponse.setCode(httpResponse.getStatusLine().getStatusCode());
                strResponse.setMessage(httpResponse.getStatusLine().getReasonPhrase());

                if (httpResponse.getEntity() != null) {
                    final String responseAsString = EntityUtils.toString(httpResponse.getEntity());
                    strResponse.setContent(responseAsString);
                }
                final long end = System.currentTimeMillis();

                LOGGER.info("End partner call [T: {}ms] [CODE: {}]", end - start, strResponse.getCode());

            } catch (final IOException e) {
                LOGGER.error("Error while partner call [T: {}ms]", System.currentTimeMillis() - start, e);
                strResponse = null;
                
            } finally {
                count++;
            }

        }
        if (strResponse == null) {
            throw new IOException("Partner response empty");
        }

        return strResponse;

    }

}
