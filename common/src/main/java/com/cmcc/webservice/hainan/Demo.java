/*package com.cmcc.webservice.hainan;

*//**
 * 接口测试类
 *//*

import com.cmcc.vrp.util.Dom4jXml;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.util.StreamUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

*//**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author lgk8023
 * @date 2017年1月22日 下午4:52:34
 *//*
public class Demo {

    private static String TIME_FOMMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";// 时间格式
    private static TrustManager myX509TrustManager = new X509TrustManager() {

        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

    };

    public static void main(String[] args) throws DocumentException {

        // 以下接口测试
        String baseUrl = "http://pdata.4ggogo.com/web-in";
        String appKey = "b395e66de5d9458b8b8eeedd602ce7d3";
        String appSecret = "592cf42e02304aeea3d2ca4208fc7923";

        // 验签
        // String authRequestTime = "2016-07-27T10:14:00.502+08:00";
        // String xml = appKey + authRequestTime + appSecret;
        //
        // String sign = "1c407398376ab27209940d74877ed3ce11624471cb42c979f2212c6bdddca86e";
        //
        // System.out.println(verifySign(xml, sign));

        // 认证接口
        String token = getToken(baseUrl + "/auth.html", appKey, appSecret);

        // // 充值接口
        String chargeUrl = baseUrl + "/boss/charge.html";
        String phone = "18867101970";
        String productId = "99";
        // String serialNum = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        // System.out.println("serialNum======"+serialNum);
        String serialNum = "20160831134807";
        charge(chargeUrl, phone, productId, serialNum, appSecret, token);

        // // 充值记录查询接口
        // String chargeRecord = baseUrl + "/chargeRecords/b14b281738e9e28fe0a1010e9068506f195afff7.html";
        // String requestXML = "";
        // String signatrue = DigestUtils.sha256Hex(requestXML + appSecret);
        // String response = doGet(chargeRecord,requestXML, token, signatrue, "utf-8", false);
        // System.out.println("充值记录查询接口返回报文：" + response);

        // 产品查询接口
        // String prdouctRecord = baseUrl + "/products.html";
        // String requestXML = "";
        // String signatrue = DigestUtils.sha256Hex(requestXML + appSecret);
        // String response = doGet(prdouctRecord,requestXML, token, signatrue, "utf-8", false);
        // System.out.println("产品查询接口返回报文：" + response);

    }

    *//**
     * @param chargeUrl
     * @param mobile
     * @param productId
     * @param serialNum
     * @param appSecret
     * @param token
     * @Title: charge
     * @Description: 充值接口
     * @return: void
     *//*
    public static void charge(String chargeUrl, String mobile, String productId, String serialNum, String appSecret, String token) {
        System.out.println("开始调用充值接口=============");
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
        String requestTime = sdf.format(new Date());
        String requestBody = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><Request><Datetime>" + requestTime + "</Datetime>" + "<ChargeData><Mobile>" + mobile
                + "</Mobile>" + "<ProductId>" + productId + "</ProductId>" + "<SerialNum>" + serialNum + "</SerialNum>" + "</ChargeData></Request>";
        String signatrue = DigestUtils.sha256Hex(requestBody + appSecret);
        System.out.println("请求报文：" + requestBody);
        System.out.println("appSecret:" + appSecret);
        System.out.println("token:" + token);
        System.out.println("签名结果：" + signatrue);
        System.out.println("请求地址：" + chargeUrl);
        String reponse = doPost(chargeUrl, requestBody, token, signatrue, "utf-8", false, "text/plain");
        System.out.println(reponse);
        System.out.println("充值接口结束=============");
    }

    *//**
     * @param authorizationURL
     * @param appKey
     * @param appSecret
     * @return
     * @throws DocumentException
     * @Title: getToken
     * @Description: 获取token
     * @return: String
     *//*
    public static String getToken(String authorizationURL, String appKey, String appSecret) throws DocumentException {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
        String authRequestTime = sdf.format(new Date());
        System.out.println("开始调用认证接口++++++++++++++");
        System.out.println("请求地址：" + authorizationURL);
        System.out.println("appKey：" + appKey);
        System.out.println("appSecret：" + appSecret);
        String xml = appKey + authRequestTime + appSecret;
        String sign = DigestUtils.sha256Hex(xml);
        System.out.println("签名报文：" + xml);
        System.out.println("签名结果：" + sign);
        String requestXML = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><Request><Datetime>" + authRequestTime + "</Datetime><Authorization><AppKey>" + appKey
                + "</AppKey><Sign>" + sign + "</Sign></Authorization></Request>";
        System.out.println("请求参数： " + requestXML);
        String response = doPost(authorizationURL, requestXML, null, null, "utf-8", false, "application/xml");
        System.out.println("响应报文：" + response);

        // 解析报文
        Element rootElement = Dom4jXml.getRootElement(response);
        Element authorization = rootElement.element("Authorization");
        String token = authorization.element("Token").getStringValue();
        return token;
    }

    *//**
     * 
     * title: doPost desc:
     * 
     * @param url
     * @param requestXML
     * @param token
     * @param signatrue
     * @param charset
     * @param pretty
     * @param contentType
     * @return wuguoping 2017年6月1日
     *//*
    public static String doPost(String url, String requestXML, String token, String signatrue, String charset, boolean pretty, String contentType) {
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();

        PostMethod method = new PostMethod(url);
        try {
            if (StringUtils.isNotBlank(token)) {
                method.addRequestHeader("4GGOGO-Auth-Token", token);
            }
            if (StringUtils.isNotBlank(signatrue)) {
                method.addRequestHeader("HTTP-X-4GGOGO-Signature", signatrue);
            }
            method.setRequestEntity(new StringRequestEntity(requestXML, contentType, "utf-8"));
            method.setRequestHeader("Content-Type", "application/xml");
            client.executeMethod(method);
            System.out.println("返回的状态码为" + method.getStatusCode());
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), charset));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (pretty) {
                        response.append(line).append(System.getProperty("line.separator"));
                    } else {
                        response.append(line);
                    }
                }
                System.out.println("返回的数据为" + response.toString());
                reader.close();
            }
        } catch (UnsupportedEncodingException e1) {
            System.out.println(e1.getMessage());
            return "";
        } catch (IOException e) {
            System.out.println("执行HTTP Post请求" + url + "时，发生异常！" + e.getMessage());

            return "";
        } finally {
            method.releaseConnection();
        }
        return response.toString();
    }

    *//**
     * @param url
     * @param queryString
     * @param token
     * @param signatrue
     * @param charset
     * @param pretty
     * @return
     *//*
    public static String doGet(String url, String queryString, String token, String signatrue, String charset, boolean pretty) {
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(url);

        try {
            if (StringUtils.isNotBlank(queryString)) {
                method.setQueryString(URIUtil.encodeQuery(queryString));
            }

            if (StringUtils.isNotBlank(signatrue)) {
                method.addRequestHeader("HTTP-X-4GGOGO-Signature", signatrue);
            }

            if (StringUtils.isNotBlank(token)) {
                method.addRequestHeader("4GGOGO-Auth-Token", token);
            }

            method.setRequestHeader("Content-Type", "application/xml");
            client.executeMethod(method);

            // method.setRequestHeader(header);
            System.out.println("code===" + method.getStatusCode());
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), charset));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (pretty) {
                        response.append(line).append(System.getProperty("line.separator"));
                    } else {
                        response.append(line);
                    }
                }

                reader.close();
            }
        } catch (URIException e) {
            System.out.println("执行HTTP Get请求时，编码查询字符串'" + queryString + "'发生异常！" + e.getMessage());
        } catch (IOException e) {
            System.out.println("执行HTTP Get请求时，编码查询字符串'" + queryString + "'发生异常！" + e.getMessage());
        } finally {
            method.releaseConnection();
        }

        return response.toString();
    }

    *//**
     * @param url
     * @param reqStr
     * @param token
     * @param signatrue
     * @param contentType
     * @param charset
     * @param cls
     * @return
     *//*
    public static <T> T doHttpsPost(String url, String reqStr, String token, String signatrue, String contentType, String charset, Class<T> cls) {
        InputStream is;
        try {
            // 设置SSLContext
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[] { myX509TrustManager }, null);
            URL requestUrl = new URL(url);

            HttpsURLConnection httpsConn = (HttpsURLConnection) requestUrl.openConnection();
            if (StringUtils.isNotBlank(token)) {
                httpsConn.addRequestProperty("4GGOGO-Auth-Token", token);
            }
            if (StringUtils.isNotBlank(signatrue)) {
                httpsConn.addRequestProperty("4GGOGO-Auth-Token", signatrue);
            }

            // 设置套接工厂
            httpsConn.setSSLSocketFactory(sslcontext.getSocketFactory());
            httpsConn.setRequestProperty("Content-Type", contentType);
            // 加入数据
            httpsConn.setRequestMethod("POST");
            httpsConn.setDoOutput(true);
            OutputStream out = httpsConn.getOutputStream();
            if (reqStr != null) {
                out.write(reqStr.getBytes(charset));
            }
            out.flush();
            out.close();
            if (httpsConn.getResponseCode() == org.apache.commons.httpclient.HttpStatus.SC_OK && (is = httpsConn.getInputStream()) != null) {
                String responseBody = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
                System.out.println("Response body:" + responseBody);
                XStream xStream = new XStream();
                xStream.processAnnotations(cls);
                return (T) xStream.fromXML(responseBody);
            }
        } catch (Exception e) {
            System.out.println("Excute post requst throw exception:" + e.getMessage());
        }
        return null;
    }

    *//**
     * @param xml
     * @param sign
     * @return
     *//*
    public static boolean verifySign(String xml, String sign) {
        System.out.println("原始签名sign = " + sign);
        System.out.println("xml = " + xml);
        System.out.println("正确签名sign = " + DigestUtils.sha256Hex(xml));
        return DigestUtils.sha256Hex(xml).equals(sign);
    }

}
*/