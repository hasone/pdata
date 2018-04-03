package com.cmcc.vrp.province.webin.controller;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by sunyiwei on 2016/6/22.
 */
public class OrderFlowControllerTest {
    private static String TIME_FOMMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";//时间格式

    @Ignore
    @Test
    public void testOrderFlow() throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
//
//        //获取token
//        String baseURL = "http://localhost:8080";
//        String authorizationURL = baseURL + "/web-in/auth.html";
//        String authRequestTime = sdf.format(new Date());
//        String appKey = "63949039ad7e358bf67179c3359327f7";
//        String appSecret = "7fb8e2104badd44a825f265536de9f37";
//        String sign = DigestUtils.sha256Hex(appKey + authRequestTime + appSecret);
//        String requestXML = "<Request><Datetime>" + authRequestTime + "</Datetime><Authorization><AppKey>" + appKey + "</AppKey><Sign>" + sign + "</Sign></Authorization></Request>";
//        System.out.println("认证接口调用：url = " + authorizationURL + ",param = " + requestXML);
//
//        String response = HttpUtilService.doPost(authorizationURL, requestXML, "utf-8", false);
//
//
//        //解析报文
//        Element rootElement = Dom4jXml.getRootElement(response);
//        Element authorization = rootElement.element("Authorization");
//        String token = authorization.element("Token").getStringValue();
//
//        String orderURL = baseURL + "/web-in/order/BOSSOrder.html";
//
//        String orderRequestTime = sdf.format(new Date());
//        String transIDO = "999999999";
//        String groupId = "88888888888888888888";
//        String requestBody = "<Request><DATETIME>" + orderRequestTime + "</DATETIME><CONTENT>"
//                + "<TRANS_IDO>" + transIDO + "</TRANS_IDO>"
//                + "<GRP_ID>" + groupId + "</GRP_ID>"
//                + "<ITEM><PAK_NUM>1000</PAK_NUM><PAK_MONEY>45</PAK_MONEY><PAK_GPRS>10</PAK_GPRS><PAK_END_DTAE>" + orderRequestTime + "</PAK_END_DTAE></ITEM>"
//                + "<ITEM><PAK_NUM>2000</PAK_NUM><PAK_MONEY>46</PAK_MONEY><PAK_GPRS>20</PAK_GPRS><PAK_END_DTAE>" + orderRequestTime + "</PAK_END_DTAE></ITEM>"
//                + "<ITEM><PAK_NUM>3000</PAK_NUM><PAK_MONEY>47</PAK_MONEY><PAK_GPRS>30</PAK_GPRS><PAK_END_DTAE>" + orderRequestTime + "</PAK_END_DTAE></ITEM>"
//                + "</CONTENT></Request>";
//
//        String signatrue = DigestUtils.sha256Hex(requestBody + appSecret);
//        System.out.println("订购接口调用：url = " + orderURL + ",param = " + requestBody + ",token = " + token + ",signatrue = " + signatrue);
//        String responseBody = doPost(orderURL, requestBody, token, signatrue, "utf-8", false);
    }

    private String doPost(String url, String reqStr, String hashedToken, String signatrue, String charset, boolean pretty) {
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();

        PostMethod method = new PostMethod(url);
        try {
            method.addRequestHeader("4GGOGO-Auth-Token", hashedToken);
            method.addRequestHeader("HTTP-X-4GGOGO-Signature", signatrue);
            method.addRequestHeader("Content-Type", MediaType.APPLICATION_XML_VALUE);
            method.setRequestEntity(new StringRequestEntity(reqStr, "text/plain", "utf-8"));

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
            System.out.println("执行HTTP Post请求" + url + "时，发生异常！" + e.getMessage());

            return "";
        } finally {
            method.releaseConnection();
        }
        return response.toString();
    }
}
