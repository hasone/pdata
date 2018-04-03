package com.cmcc.webservice.hainan;

/**
 * 接口测试类
 */

import com.cmcc.vrp.util.Dom4jXml;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
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
* @date 2017年1月22日 下午4:56:27
*/
public class HNDemo {

    private static String TIME_FOMMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";//时间格式

    public static void main(String[] args) throws DocumentException {
        //以下为海南接口测试
//            //海南测试
//    		String hnBaseUrl = "http://localhost:8080/web-in";

//    		String hnBaseUrl = "http://hi.4ggogo.com/hntest";
//            String appKey = "ae4305122fbc4abd8e5e020576c8c9ad";
//            String appSecret = "22c0b9c2fa10cc7a1e56830937b48d15";
//            


//            //海南正式
        String hnBaseUrl = "http://hi.4ggogo.com/web-in";
        String appKey = "ae4305122fbc4abd8e5e020576c8c9ad";
        String appSecret = "ba94395a26054f62a0e701e621c9fab5";

        //认证接口
        String authorizationURL = hnBaseUrl + "/auth.html";
        String hnToken = getToken(authorizationURL, appKey, appSecret);

//            String orderUrl = hnBaseUrl + "/order/BOSSOrder.html";


//            //订购接口
//            hainanOrder(orderUrl, hnToken, appSecret);

        String operateUrl = hnBaseUrl + "/mdrc/operate.html";
        String cardNum = "100001623000400009000000001";//卡号
        String expireDate = "2016-09-30";//失效日期
        String exchangeCards = "201606071199913";//更改卡号
        String groupId = "8989859421";//企业编码
        String productCode = "HNa37b6bcbcade462783b6309f9aa4a";//产品编码
//            
//            //营销卡入库操作
//            mdrcOperate(operateUrl, "Stock In", cardNum, expireDate, exchangeCards, groupId, productCode, hnToken, appSecret);            
//           
//            //营销卡激活操作
        mdrcOperate(operateUrl, "Activate", cardNum, expireDate, exchangeCards, groupId, productCode, hnToken, appSecret);
//            
//            //营销卡延期操作
//            mdrcOperate(operateUrl, "Extend", cardNum, expireDate, exchangeCards, groupId, productCode, hnToken, appSecret);
//            
//            //营销卡充值接口
//            String url = hnBaseUrl + "/mdrc/use.html";
//            String card = "100001623000100001000000027";//卡号
//            String password = "60032468162148";//密码
//            String mobile = "18789088277";//手机号码
//            String serial = "8989859421";//序列号
//            mdrcCharge(url, card, password, mobile, serial, hnToken, hnAppSecret);


    }

    /**
     * @param url
     * @param cardNum
     * @param password
     * @param mobile
     * @param serailNum
     * @param token
     * @param appSecret
     * @Title: mdrcCharge
     * @Description: 营销卡充值接口
     * @return: void
     */
    public static void mdrcCharge(String url, String cardNum, String password, String mobile, String serailNum, String token, String appSecret) {
        System.out.println("开始调用营销卡充值接口============");
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
        String requestTime = sdf.format(new Date());
        String mdrcChargeBody = "<Request><Datetime>" + requestTime + "</Datetime><Card>"
            + "<CardNum>" + cardNum + "</CardNum>"
            + "<Password>" + password + "</Password>"
            + "<Mobile>" + mobile + "</Mobile>"
            + "<SerialNum>" + serailNum + "</SerialNum>"
            + "</Card></Request>";
        String mdrcCharegeSignatrue = DigestUtils.sha256Hex(mdrcChargeBody + appSecret);
        String operateResponse = doPost(url, mdrcChargeBody, token, mdrcCharegeSignatrue, "utf-8", false, "application/xml");
        System.out.println("调用营销卡充值接口结束============" + operateResponse);
    }

    /**
     * @param url
     * @param operate
     * @param cardNum
     * @param expireDate
     * @param exchangeCards
     * @param groupId
     * @param productCode
     * @param token
     * @param appSecret
     * @Title: mdrcOperate
     * @Description: 营销卡操作
     * @return: void
     */
    public static void mdrcOperate(String url, String operate, String cardNum, 
            String expireDate, String exchangeCards, String groupId, String productCode, String token, String appSecret) {
        System.out.println("开始调用营销卡操作接口=============");
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
        String requestTime = sdf.format(new Date());
        String operateBody = "<Request><Datetime>" + requestTime + "</Datetime><Card>"
            + "<Operate>" + operate + "</Operate>"
            + "<CardNum>" + cardNum + "</CardNum>"
            + "<ExpireDate>" + expireDate + "</ExpireDate>"
            + "<ExchangeCards>" + exchangeCards + "</ExchangeCards>"
            + "<GroupId>" + groupId + "</GroupId>"
            + "<ProductCode>" + productCode + "</ProductCode>"
            + "</Card></Request>";
        String operateSignatrue = DigestUtils.sha256Hex(operateBody + appSecret);
        String operateResponse = doPut(url, operateBody, token, operateSignatrue, "utf-8", false, "application/xml");

        System.out.println("充值接口结束=============" + operateResponse);
    }

    /**
     * @param orderURL
     * @param token
     * @param appSecret
     * @Title: hainanOrder
     * @Description: 海南订购接口测试
     * @return: void
     */
    public static void hainanOrder(String orderURL, String token, String appSecret) {
        System.out.println("开始调用海南流量订购接口=============");
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
        String orderRequestTime = sdf.format(new Date());
        String transIDO = "999999999";
        String groupId = "8989859421";
        String orderBody = "<Request><DATETIME>" + orderRequestTime + "</DATETIME><CONTENT>"
            + "<TRANS_IDO>" + transIDO + "</TRANS_IDO>"
            + "<GRP_ID>" + groupId + "</GRP_ID>"
            + "<ITEM><PAK_NUM>1000</PAK_NUM><PAK_MONEY>4150</PAK_MONEY><PAK_GPRS>10</PAK_GPRS><PAK_END_DTAE>" + orderRequestTime + "</PAK_END_DTAE></ITEM>"
            + "<ITEM><PAK_NUM>2000</PAK_NUM><PAK_MONEY>4610</PAK_MONEY><PAK_GPRS>20</PAK_GPRS><PAK_END_DTAE>" + orderRequestTime + "</PAK_END_DTAE></ITEM>"
            + "<ITEM><PAK_NUM>3000</PAK_NUM><PAK_MONEY>4710</PAK_MONEY><PAK_GPRS>30</PAK_GPRS><PAK_END_DTAE>" + orderRequestTime + "</PAK_END_DTAE></ITEM>"
            + "</CONTENT></Request>";
        String orderSignatrue = DigestUtils.sha256Hex(orderBody + appSecret);
        System.out.println("订购接口调用：url = " + orderURL + ",param = " + orderBody + ",token = " + token + ",signatrue = " + orderSignatrue);
        String orderResponseBody = doPost(orderURL, orderBody, token, orderSignatrue, "utf-8", false, "application/xml");
        System.out.println("充值接口结束=============" + orderResponseBody);
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
    public static String getToken(String authorizationURL, String appKey, String appSecret) throws DocumentException {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
        String authRequestTime = sdf.format(new Date());
        String sign = DigestUtils.sha256Hex(appKey + authRequestTime + appSecret);
        String requestXML = "<Request><Datetime>" + authRequestTime 
                + "</Datetime><Authorization><AppKey>" + appKey 
                + "</AppKey><Sign>" + sign + "</Sign></Authorization></Request>";
        System.out.println("认证接口调用：url = " + authorizationURL + ",param = " + requestXML);
        String response = doPost(authorizationURL, requestXML, null, null, "utf-8", false, "application/xml");

        //解析报文
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
    public static String doPut(String url, String requestXML, String token, String signatrue, String charset, boolean pretty, String contentType) {
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();

        PutMethod method = new PutMethod(url);
        try {
            if (StringUtils.isNotBlank(token)) {
                method.addRequestHeader("4GGOGO-Auth-Token", token);
            }
            if (StringUtils.isNotBlank(signatrue)) {
                method.addRequestHeader("HTTP-X-4GGOGO-Signature", signatrue);
            }
            method.setRequestEntity(new StringRequestEntity(requestXML, contentType, "utf-8"));

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

