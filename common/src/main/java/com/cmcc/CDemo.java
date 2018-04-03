package com.cmcc;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @ClassName: CDemo 
 * @Description: TODO
 * @author: Rowe
 * @date: 2017年4月18日 上午10:15:04
 */
public class CDemo {

    private static String TIME_FOMMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";// 时间格式

    private static String appKey = "40ef6ede7adh40c49f31cb09fb96d1b0";
    private static String appSecret = "5d338a3fe46a4d9abdcbder1272f9bc0";

    /**
     * 
     * @Title: main 
     * @Description: TODO
     * @param args
     * @throws DocumentException
     * @return: void
     */
    public static void main(String[] args) throws DocumentException {
        getToken(appKey, appSecret);
    }

    /**
     * 
     * @Title: getToken 
     * @Description: TODO
     * @param appKey
     * @param appSecret
     * @throws DocumentException
     * @return: void
     */
    public static void getToken(String appKey, String appSecret) throws DocumentException {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
        String authRequestTime = sdf.format(new Date());
        System.out.println("开始调用认证接口++++++++++++++");
        System.out.println("appKey：" + appKey);
        System.out.println("appSecret：" + appSecret);
        String xml = appKey + authRequestTime + appSecret;
        String sign = DigestUtils.sha256Hex(xml);
        System.out.println("签名报文：" + xml);
        System.out.println("签名结果：" + sign);
        String requestXML = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><Request><Datetime>" + authRequestTime
                + "</Datetime><Authorization><AppKey>" + appKey + "</AppKey><Sign>" + sign
                + "</Sign></Authorization></Request>";
        System.out.println("请求参数： " + requestXML);

        HttpPost httpPost = new HttpPost("https://pdataqa.4ggogo.com/web-in/auth.html");
        httpPost.setHeader("Content-Type", "application/xml");
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(requestXML);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        httpPost.setEntity(stringEntity);
        CloseableHttpClient httpClient = createSSLClientDefault();

        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                // 按指定编码转换结果实体为String类型
                requestXML = EntityUtils.toString(entity, "UTF-8");
            }
            EntityUtils.consume(entity);
            System.out.println(requestXML);
            System.out.println(httpResponse.getStatusLine().getStatusCode());
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 
     * @Title: createSSLClientDefault 
     * @Description: TODO
     * @return
     * @return: CloseableHttpClient
    */
    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }
}
