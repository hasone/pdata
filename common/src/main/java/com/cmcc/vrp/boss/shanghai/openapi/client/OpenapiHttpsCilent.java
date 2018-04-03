package com.cmcc.vrp.boss.shanghai.openapi.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


/**
 * @author lgk8023
 *
 */
public class OpenapiHttpsCilent {
    private CloseableHttpClient client;
    private String appCode;
    private String openapisUrl;

    /**
     * @param appCode
     *            搴旂敤缂栫爜
     * @param keystorePath
     *            keystore鍔犺浇璺緞
     * @param password
     *            key瀵嗙爜
     * @throws Exception
     */
    public OpenapiHttpsCilent(String appCode, String keystorePath, String password, String openapisUrl) throws Exception {
        this.appCode = appCode;
        init(openapisUrl);
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream instream = new FileInputStream(new File(keystorePath));
        try {
            trustStore.load(instream, password.toCharArray());
        } finally {
            instream.close();
        }

        SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
                SSLConnectionSocketFactory.STRICT_HOSTNAME_VERIFIER);
        this.client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }

    /**
     * @param apiCode
     *            搴旂敤缂栫爜
     * @param transactionId
     *            涓氬姟缂栫爜
     * @param requestBody
     *            璇锋眰鎶ユ枃
     * @param accessToken
     *            璁块棶浠ょ墝
     * @return
     * @throws Exception
     */
    public String callHttps(String apiCode, String transactionId, String requestBody, String accessToken) throws Exception {
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        BufferedReader br = null;
        String tmp = null;
        try {
            httpPost = new HttpPost(openapisUrl + "/accessHttps");
            httpPost.addHeader("appCode", appCode);
            httpPost.addHeader("apiCode", apiCode);
            httpPost.addHeader("transactionId", transactionId);
            httpPost.addHeader("accessToken", accessToken);
            httpPost.addHeader("sdkVersion", "sdk.version.2.2");
            httpPost.setEntity(new StringEntity(requestBody, ContentType.create("text/plain", "UTF-8")));
            response = client.execute(httpPost);
            InputStreamReader isr = new InputStreamReader(response.getEntity().getContent(),"UTF-8");
            br = new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();
            while ((tmp = br.readLine()) != null) {
                sb.append(tmp);
            }
            String responseBody = sb.toString();
            return responseBody;
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpPost != null) {
                httpPost.abort();
            }
            if (br != null) {
                br.close();
            }
        }
    }

    /**
     * @param apiCode
     *            鑳藉姏缂栫爜
     * @param transactionId
     *            涓氬姟缂栫爜
     * @param requestBody
     *            璇锋眰鎶ユ枃
     * @return
     * @throws Exception
     */
    public String callHttps(String apiCode, String transactionId, String requestBody) throws Exception {
        return callHttps(apiCode, transactionId, requestBody, null);
    }

    private void init(String openapisUrl) {
        
        this.openapisUrl = openapisUrl;
    }

}
