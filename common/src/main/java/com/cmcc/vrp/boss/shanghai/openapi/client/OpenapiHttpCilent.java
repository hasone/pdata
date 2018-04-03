package com.cmcc.vrp.boss.shanghai.openapi.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.protocol.HttpContext;

import com.cmcc.vrp.boss.shanghai.openapi.manage.DefalutSecurity;
import com.cmcc.vrp.boss.shanghai.openapi.manage.SecurityI;
import com.cmcc.vrp.boss.shanghai.openapi.model.OpenapiResponse;
import com.cmcc.vrp.boss.shanghai.openapi.model.ResponseStatus;
import com.cmcc.vrp.boss.shanghai.openapi.util.JsonUtil;


/**
 * @author lgk8023
 *
 */
public class OpenapiHttpCilent {
    private String appCode;
    private String openapiUrl;
    private SecurityI securiytManager;
    private String securityUrl;
    
    private static CloseableHttpClient closeableHttpClient = null;

    static {
        setCloseableHttpClient(HttpClients.custom()
                .setDefaultRequestConfig(buildConfig(30 * 1000, 60 * 1000, 30 * 1000)) //设置超时时间connectTimeout(连接超时),socketTimeout(响应超时),connectRequestTimeout
                .setRedirectStrategy(new LaxRedirectStrategy()) //设置重定向策略
                .setMaxConnPerRoute(100) //对于每个host,最多不超过100个连接
                .setMaxConnTotal(3000) //设置最多的连接数,默认200
                .setKeepAliveStrategy(new ConnectionKeepAliveStrategy(){

                    @Override
                    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                        return 0;
                    }
                    
                })    //一次请求建立一次连接
                .build());
    }

    /**
     * @param appCode 搴旂敤缂栫爜
     * @param apk 搴旂敤涓诲瘑閽ワ紝鍦ㄥ紑鍙戣�呴棬鎴蜂腑搴旂敤绠＄悊鑿滃崟閲岃幏鍙�
     */
    public OpenapiHttpCilent(String appCode, String apk, String securityUrl, String openapiUrl) {
        this.appCode = appCode;
        init(securityUrl, openapiUrl);
        securiytManager = new DefalutSecurity(this.securityUrl, appCode, apk);
    }

    /**
     * @param apiCode 鑳藉姏缂栫爜
     * @param transactionId 涓氬姟缂栫爜
     * @param requestBody 璇锋眰
     * @param accessToken 璁块棶浠ょ墝
     * @return
     * @throws Exception
     */
    public String call(String apiCode, String transactionId, String requestBody, String accessToken) throws Exception {
        // 1.瀵规姤鏂囩鍚�
        String publiceKey = securiytManager.getAsk().getPublicKeyStr();
        Long aedkId = securiytManager.getAedk().getId();
        requestBody = securiytManager.encrypt(requestBody);
        String signValue = securiytManager.sign(requestBody);
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        BufferedReader br = null;
        String tmp = null;
        try {
            httpPost = new HttpPost(openapiUrl + "/access");
            httpPost.addHeader("appCode", appCode);
            httpPost.addHeader("apiCode", apiCode);
            httpPost.addHeader("transactionId", transactionId);
            httpPost.addHeader("aedkId", aedkId + "");
            httpPost.addHeader("signValue", signValue);
            httpPost.addHeader("publicKey", publiceKey);
            httpPost.addHeader("accessToken", accessToken);
            httpPost.addHeader("sdkVersion", "sdk.version.2.2");    
            httpPost.setEntity(new StringEntity(requestBody, ContentType.create("text/plain", "UTF-8")));
            response = getCloseableHttpClient().execute(httpPost);
            InputStreamReader isr = new InputStreamReader(response.getEntity().getContent(),"UTF-8");
            br = new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();
            while ((tmp = br.readLine()) != null) {
                sb.append(tmp);
            }
            String responseBody = sb.toString();
            OpenapiResponse sr = JsonUtil.toBean(responseBody, OpenapiResponse.class);
            if (ResponseStatus.SUCCESS.toString().equals(sr.getStatus())) {
                String result = sr.getResult();
                if (securiytManager.verify(result, sr.getSignValue(), sr.getPublicKey())) {
                    String otext = securiytManager.decrypt(result);
                    return "{\"status\":\"SUCCESS\",\"result\":\"" + otext.replace("\"", "\\\"") + "\"}";
                } else {
                    return "{\"status\":\"ERROR\",\"errorCode\":\"060101\",\"exceptionCode\":\"sign error\"}";
                }
            } else {
                return responseBody;
            }
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
     * @param apiCode 鑳藉姏缂栫爜
     * @param transactionId 涓氬姟缂栫爜
     * @param requestBody 璇锋眰
     * @return
     * @throws Exception
     */
    public String call(String apiCode, String transactionId, String requestBody) throws Exception {
        return call(apiCode, transactionId, requestBody, null);
    }

    private void init(String securityUrl, String openapiUrl) {       
        this.openapiUrl = openapiUrl;  
        this.securityUrl = securityUrl;
    }
    
    private static RequestConfig buildConfig(int connectTimeout, int socketTimeout, int connectRequestTimeout) {
        return RequestConfig.custom()
                .setConnectTimeout(connectTimeout) //这个指的是连接超时, 只在建立TCP连接时有用
                .setSocketTimeout(socketTimeout) //这个指的是响应超时, 服务端需要在这个时间内给出响应
                .setConnectionRequestTimeout(connectRequestTimeout) //这个是指从连接池获取连接的超时时间
                .build(); //两者的单位均为毫秒
    }

    public static CloseableHttpClient getCloseableHttpClient() {
        return closeableHttpClient;
    }

    public static void setCloseableHttpClient(CloseableHttpClient closeableHttpClient) {
        OpenapiHttpCilent.closeableHttpClient = closeableHttpClient;
    }

}
