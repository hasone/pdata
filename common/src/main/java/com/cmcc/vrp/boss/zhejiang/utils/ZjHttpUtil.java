package com.cmcc.vrp.boss.zhejiang.utils;

import com.cmcc.vrp.boss.zhejiang.model.ZjAuth;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by leelyn on 2016/8/3.
 */
public class ZjHttpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZjHttpUtil.class);
    /**
     * The wsse header.
     */
    private static final String WSSE_HEADER = "X-WSSE";

    /**
     * The host header.
     */
    private static final String HOST = "Host";

    /**
     * The host content.
     */
    private static final String HOST_CONTENT = "aep.sdp.com";  // 测试aep.test.sdp.com // 生产 aep.sdp.com

    /**
     * The authorization header.
     */
    private static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * The authorization content.
     */
    private static final String AUTHORIZATION_CONTENT = "WSSE realm=\"SDP\", profile=\"UsernameToken\", type=\"Appkey\"";

    /**
     * POST请求
     *
     * @param url
     * @param reqStr
     * @param appKey
     * @param appSecret
     * @return
     */
    public static String doPost(String url, String reqStr, String appKey, String appSecret) {
        StringBuffer resp = new StringBuffer();
        ZjAuth auth = new ZjAuth(appKey, appSecret);
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        client.getHttpConnectionManager().getParams().setSoTimeout(50000);
        PostMethod method = new PostMethod(url);
        addHttpHeaders(method, auth);
        InputStreamReader isr = null;
        BufferedReader reader = null;
        try {
            method.setRequestEntity(new StringRequestEntity(reqStr, "application/json", "utf-8"));
            client.executeMethod(method);
            int statusCode = method.getStatusCode();
            LOGGER.info("浙江HTTP返回码:{}", statusCode);
            if (statusCode == HttpStatus.SC_OK) {
                isr = new InputStreamReader(method.getResponseBodyAsStream(), "utf-8");
                reader = new BufferedReader(isr);
                String line;
                while ((line = reader.readLine()) != null) {
                    resp.append(line);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (isr != null) {
                    isr.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            method.releaseConnection();
        }
        return resp.toString();
    }

    /**
     * 添加请求头
     *
     * @param method
     * @param auth
     */
    private static void addHttpHeaders(PostMethod method, ZjAuth auth) {
        String wsseHeader = bulidWsseHeader(auth);
        method.setRequestHeader(WSSE_HEADER, wsseHeader);
        LOGGER.debug("The WSSE_HEADER is :{}", wsseHeader);
        method.setRequestHeader(AUTHORIZATION_HEADER, AUTHORIZATION_CONTENT);
        LOGGER.debug("The AUTHORIZATION_HEADER is :{}", AUTHORIZATION_CONTENT);
        method.getParams().setVirtualHost(HOST_CONTENT);
        LOGGER.debug("The HOST is :{}", HOST_CONTENT);
    }

    private static String bulidWsseHeader(ZjAuth auth) {
        String appKey = auth.getAppKey();
        String nonce = RandomNonceUtil.genRandomNonce();
        String created = TimestampUtil.getLocalUtcTimestamp();
        String source = (new StringBuilder(String.valueOf(nonce)))
                .append(created).append(auth.getAppSecret()).toString();
        String pwdDigest = null;
        if (EncryptUtil.ENCRYPT_SHA1_TYPE.equals(auth.getEncryptType())) {
            pwdDigest = EncryptUtil.encryptSha1Base64(source);
        } else {
            pwdDigest = EncryptUtil.encryptSha256Base64(source);
        }

        String header = "UsernameToken Username=" + "\"" + appKey + "\""
                + ", PasswordDigest=" + "\"" + pwdDigest + "\"" + ", Nonce="
                + "\"" + nonce + "\"" + ",Created=" + "\"" + created + "\""
                + "";
        return header;
    }

}
