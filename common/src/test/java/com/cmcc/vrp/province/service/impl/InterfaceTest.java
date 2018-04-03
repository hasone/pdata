package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.ec.bean.AuthReq;
import com.cmcc.vrp.ec.bean.AuthReqData;
import com.cmcc.vrp.ec.bean.AuthResp;
import com.cmcc.vrp.util.HttpUtils;
import com.thoughtworks.xstream.XStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.Charsets;
import org.joda.time.DateTime;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunyiwei on 2016/9/19.
 */
public class InterfaceTest {
    protected final String appKey = "5b18492537c34dcd8bfe4c2f56878ea8";
    protected final String appSecret = "15f90192011a4e00ac289a97adf12f26";
    protected final String shuanchuan_appKey = "7ca971744c134520b38bc68202cc47c5";
    protected final String shuanchuan_appSecret = "901a5ef5661f4bd7918b1582189c85aa";

    protected String getToken() {
        final String AUTH_URL = "http://localhost:8080/web-in/auth.html";

        String authStr = buildAuthStr(build());
        String tokenResult = HttpUtils.post(AUTH_URL, authStr, "application/xml");

        AuthResp authResp = parse(tokenResult);
        return authResp.getAuthRespData().getToken();
    }

    protected AuthResp parse(String respStr) {
        XStream xStream = new XStream();
        xStream.alias("Response", AuthResp.class);
        xStream.autodetectAnnotations(true);

        return (AuthResp) xStream.fromXML(respStr);
    }

    private String buildAuthStr(AuthReq authReq) {
        XStream xStream = new XStream();
        xStream.alias("Request", AuthReq.class);
        xStream.autodetectAnnotations(true);

        return xStream.toXML(authReq);
    }

    private AuthReq build() {
        String requestTime = new DateTime().toString();
        String sign = DigestUtils.sha256Hex(appKey + requestTime + appSecret);

        AuthReq authReq = new AuthReq();

        AuthReqData ard = new AuthReqData();
        ard.setAppKey(appKey);
        ard.setSign(sign);

        authReq.setAuthorization(ard);
        authReq.setRequestTime(requestTime);

        return authReq;
    }

    protected Map<String, String> buildHeaders(String token, String reqStr) {
        final String TOKEN_HEADER = "4GGOGO-Auth-Token";
        final String SIGNATURE_HEADER = "HTTP-X-4GGOGO-Signature";

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(TOKEN_HEADER, token);
        headers.put(SIGNATURE_HEADER, DigestUtils.sha256Hex(reqStr + appSecret));

        return headers;
    }

    protected String doPost(String url, String requestStr, Map<String, String> headers) {
        HttpClient httpClient = new HttpClient();
        PostMethod httpMethod = new PostMethod(url);

        //add headers
        for (String s : headers.keySet()) {
            httpMethod.addRequestHeader(s, headers.get(s));
        }

        //add request entity
        try {
            httpMethod.setRequestEntity(new StringRequestEntity(requestStr, "application/xml", "utf-8"));

            //执行方法
            System.out.println("请求的内容为" + requestStr);
            httpClient.executeMethod(httpMethod);

            System.out.println("返回的状态码为" + httpMethod.getStatusCode());
            if (httpMethod.getStatusCode() == HttpStatus.SC_OK) {
                String content = StreamUtils.copyToString(httpMethod.getResponseBodyAsStream(), Charsets.UTF_8);
                System.out.println("返回的内容为" + content);

                return content;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected String doGet(String url, Map<String, String> headers) {
        HttpClient httpClient = new HttpClient();
        GetMethod httpMethod = new GetMethod(url);

        //add headers
        for (String s : headers.keySet()) {
            httpMethod.addRequestHeader(s, headers.get(s));
        }

        //add request entity
        try {
            //执行方法
            httpClient.executeMethod(httpMethod);

            System.out.println("返回的状态码为" + httpMethod.getStatusCode());
            if (httpMethod.getStatusCode() == HttpStatus.SC_OK) {
                String content = StreamUtils.copyToString(httpMethod.getResponseBodyAsStream(), Charsets.UTF_8);
                System.out.println("返回的内容为" + content);

                return content;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
