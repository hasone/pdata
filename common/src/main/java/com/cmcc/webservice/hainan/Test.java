package com.cmcc.webservice.hainan;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.dom4j.Element;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午5:03:12
*/
public class Test {
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static Logger logger = Logger.getLogger(Test.class);
    public static String TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static void main(String[] args) {

        callHttpFlowPlat();
    }

    /**
     * @return
     */
    public static String callHttpFlowPlat() {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);

//        String baseURL = BizEnv.getEnvString("crm.call.FlowPlatUrl");
//        String svcname="authorizations";

        String baseURL = "http://hi.4ggogo.com";
        String authURL = baseURL + "/web-in/open/authorizations";

        //调认证接口
        String authRequestTime = sdf.format(new Date());
        String appKey = "c1aa3ad1b687de7ec7d84b7babc83318";
        String appSecret = "bd21b69cf6229443a5ffd8e1ef269079";

        String response = "";
        String sign = sha256Hex(appKey + authRequestTime + appSecret);
        String requestXML = "<Request><DATETIME>" + authRequestTime 
                + "</DATETIME><AUTHORIZATION><APP_KEY>" + appKey + "</APP_KEY><SIGN>" 
                + sign + "</SIGN></AUTHORIZATION></Request>";
        logger.debug("==============sign========" + sign);
        logger.debug("==============requestXML========" + requestXML);
        logger.debug("==============authURL========" + authURL);
        response = doPostAuth(authURL, requestXML, "utf-8", false);
        System.out.println(response);
        return response;
    }

    private static Element addString(Element element, String s) {

        element.addAttribute("Value", s);
        return element;
    }

    /**
     * @param text
     * @param key
     * @return
     */
    public static String HMACSign(String text, String key) {
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA256");
            byte[] secretByte = key.getBytes("UTF-8");
            byte[] dataBytes = text.getBytes("UTF-8");
            SecretKey secret = new SecretKeySpec(secretByte, "HMACSHA256");
            mac.init(secret);
            byte[] doFinal = mac.doFinal(dataBytes);
            byte[] hexB = new Hex().encode(doFinal);
            String checksum = new String(hexB);
            return checksum;
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param url
     * @param reqStr
     * @param charset
     * @param pretty
     * @return
     */
    public static String doPostAuth(String url, String reqStr, String charset, boolean pretty) {
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();

        PostMethod method = new PostMethod(url);
        try {
            method.setRequestEntity(new StringRequestEntity(reqStr, "text/plain", "utf-8"));
            client.executeMethod(method);
            System.out.println("返回的状态码为" + method.getStatusCode());
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), charset));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (pretty) {
                        response.append(line).append(
                            System.getProperty("line.separator"));
                    } else {
                        response.append(line);
                    }
                }
                //System.out.println("返回的数据为"+response.toString());
                reader.close();
            }
        } catch (UnsupportedEncodingException e1) {
            //System.out.println(e1.getMessage());
            logger.debug("=====UnsupportedEncodingException====" + e1.getMessage());
            return "";
        } catch (IOException e) {

            logger.debug("执行HTTP Post请求" + url + "时，发生异常！" + e.toString());
            //System.out.println("执行HTTP Post请求" + url + "时，发生异常！"+ e.getMessage());
            return "";
        } finally {
            method.releaseConnection();
        }
        return response.toString();
    }

    /**
     * @param url
     * @param reqStr
     * @param hashedToken
     * @param signatrue
     * @param charset
     * @param pretty
     * @return
     */
    public static String doPost(String url, String reqStr, String hashedToken, String signatrue, String charset, boolean pretty) {
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();

        PostMethod method = new PostMethod(url);
        try {
            method.addRequestHeader("4GGOGO-Auth-Token", hashedToken);
            method.addRequestHeader("HTTP-X-4GGOGO-Signature", signatrue);
            method.setRequestEntity(new StringRequestEntity(reqStr, "text/plain", "utf-8"));

            client.executeMethod(method);
            System.out.println("返回的状态码为" + method.getStatusCode());
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), charset));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (pretty) {
                        response.append(line).append(
                            System.getProperty("line.separator"));
                    } else {
                        response.append(line);
                    }
                }
                //System.out.println("返回的数据为"+response.toString());
                reader.close();
            }
        } catch (UnsupportedEncodingException e1) {
            //System.out.println(e1.getMessage());
            return "";
        } catch (IOException e) {
            //System.out.println("执行HTTP Post请求" + url + "时，发生异常！"+ e.getMessage());
            return "";
        } finally {
            method.releaseConnection();
        }
        return response.toString();
    }

    /**
     * @param data
     * @return
     */
    public static String sha256Hex(String data) {
        return encodeHexString(sha256(data));
    }

    /**
     * @param data
     * @return
     */
    public static byte[] sha256(String data) {
        return sha256(getBytesUtf8(data));
    }

    /**
     * @param data
     * @return
     */
    public static byte[] sha256(byte[] data) {
        return getSha256Digest().digest(data);
    }

    public static MessageDigest getSha256Digest() {
        return getDigest("SHA-256");
    }

    public static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @param data
     * @return
     */
    public static String encodeHexString(byte[] data) {
        return new String(Hex.encodeHex(data));
    }

    public static byte[] getBytesUtf8(String string) {
        if (string == null) {
            return null;
        } else {
            return string.getBytes(UTF_8);
        }
    }

    private static byte[] getBytes(String string, Charset charset) {
        if (string == null) {
            return null;
        } else {
            return string.getBytes(charset);
        }
    }
}

