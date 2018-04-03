package com.cmcc.webservice.hainan;

/**
 * 接口测试类
 */

import com.cmcc.vrp.util.Dom4jXml;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午5:00:53
*/
public class HuNanDemo {

    private static String TIME_FOMMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";// 时间格式

    public static void main(String[] args) throws DocumentException {

        // 以下为湖南线上环境
        String baseUrl = "http://hn.4ggogo.com/web-in";
        String appKey = "c93f6b90a9df491ea60e9681c3145271";
        String appSecret = "d4be98479b86435fb158a73b44ed18bc";


//		String baseUrl = "http://localhost:8080/web-in";

//		String baseUrl = "http://hi.4ggogo.com/hntest";
//        String appKey = "8d3b32aa660f4e3cb7c0f86e1d77801b";
//        String appSecret = "c7550be657024a5a92efcd20009f3cd5";

        // 认证接口
        String token = getToken(baseUrl + "/auth.html", appKey, appSecret);

        String chargeRecord = baseUrl + "/chargeRecords/a386f3746f423f986851a84c6517d96448f202ec.html";
        String requestXML = "";
        String signatrue = DigestUtils.sha256Hex(requestXML + appSecret);
        System.out.println("请求地址：" + chargeRecord);
        System.out.println("请求报文：" + requestXML);
        System.out.println("token:" + token);
        System.out.println("请求签名：" + signatrue);
        String response = doGet(chargeRecord, requestXML, token, signatrue, "utf-8", false);
        System.out.println("充值记录查询接口返回报文：" + response);

        //产品查询接口
//		String prdouctRecord = baseUrl + "/products.html";
//		String requestXML = "";
//		String signatrue = DigestUtils.sha256Hex(requestXML + appSecret);
//		System.out.println("请求地址：" + prdouctRecord);
//		System.out.println("请求报文：" + requestXML);
//		System.out.println("token:" + token);
//		System.out.println("请求签名：" + signatrue);
//		String response = doGet(prdouctRecord,requestXML, token, signatrue, "utf-8", false);
//		System.out.println("产品查询接口返回报文：" + response);


        // 充值接口
//		 String chargeUrl = baseUrl + "/boss/charge.html";
//		 String phone = "13807336777";
//		 String productId = "2";
//		 String serialNum = "123456";
//		 charge(chargeUrl, phone , productId, serialNum, appSecret, token);
    }

    /**
     * @param chargeUrl
     * @param mobile
     * @param productId
     * @param serialNum
     * @param appSecret
     * @param token
     */
    public static void charge(String chargeUrl, String mobile,
                              String productId, String serialNum, String appSecret, String token) {
        System.out.println("开始调用充值接口=============");
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
        String requestTime = sdf.format(new Date());
        String requestBody = "<Request><Datetime>" + requestTime
            + "</Datetime>" + "<ChargeData><Mobile>" + mobile + "</Mobile>"
            + "<ProductId>" + productId + "</ProductId>" + "<SerialNum>"
            + serialNum + "</SerialNum>" + "</ChargeData></Request>";
        String signatrue = DigestUtils.sha256Hex(requestBody + appSecret);
        System.out.println("请求报文：" + requestBody);
        System.out.println("appSecret:" + appSecret);
        System.out.println("token:" + token);
        System.out.println("签名结果：" + signatrue);
        System.out.println("请求地址：" + chargeUrl);
        String reponse = doPost(chargeUrl, requestBody, token, signatrue,
            "utf-8", false, "text/plain");
        System.out.println("充值接口结束=============" + reponse);
    }


    /**
     * @param authorizationURL
     * @param appKey
     * @param appSecret
     * @return
     * @throws DocumentException
     * @Title: getToken
     * @Description: 获取token
     * @return: String
     */
    public static String getToken(String authorizationURL, String appKey,
                                  String appSecret) throws DocumentException {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
        String authRequestTime = sdf.format(new Date());
        String sign = DigestUtils.sha256Hex(appKey + authRequestTime
            + appSecret);
        String requestXML = "<Request><Datetime>" + authRequestTime
            + "</Datetime><Authorization><AppKey>" + appKey
            + "</AppKey><Sign>" + sign
            + "</Sign></Authorization></Request>";
        System.out.println("认证接口调用：url = " + authorizationURL + ",param = "
            + requestXML);
        String response = doPost(authorizationURL, requestXML, null, null,
            "utf-8", false, "application/xml");

        // 解析报文
        Element rootElement = Dom4jXml.getRootElement(response);
        Element authorization = rootElement.element("Authorization");
        String token = authorization.element("Token").getStringValue();
        return token;

    }

    /**
     * @param url
     * @param requestXML
     * @param token
     * @param signatrue
     * @param charset
     * @param pretty
     * @param contentType
     * @return
     */
    public static String doPost(String url, String requestXML, String token,
                                String signatrue, String charset, boolean pretty, String contentType) {
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
            method.setRequestEntity(new StringRequestEntity(requestXML,
                contentType, "utf-8"));

            client.executeMethod(method);
            System.out.println("返回的状态码为" + method.getStatusCode());
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(method.getResponseBodyAsStream(),
                        charset));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (pretty) {
                        response.append(line).append(
                            System.getProperty("line.separator"));
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
            System.out.println("执行HTTP Post请求" + url + "时，发生异常！"
                + e.getMessage());

            return "";
        } finally {
            method.releaseConnection();
        }
        return response.toString();
    }

    /**
     * @param url
     * @param queryString
     * @param token
     * @param signatrue
     * @param charset
     * @param pretty
     * @return
     */
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

            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(method.getResponseBodyAsStream(),
                        charset));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (pretty) {
                        response.append(line).append(
                            System.getProperty("line.separator"));
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
}
