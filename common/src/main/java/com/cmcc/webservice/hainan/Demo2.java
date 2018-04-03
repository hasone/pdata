package com.cmcc.webservice.hainan;

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
* @date 2017年1月22日 下午4:55:41
*/
public class Demo2 {

    private static String TIME_FOMMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";//时间格式

    public static void main(String[] args) throws DocumentException {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);

        //获取token
//            String baseURL = "http://hainantest.4ggogo.com";
//            String baseURL = "http://sc.4ggogo.com";
//    		String baseURL= "http://localhost:8080";
        String baseURL = "http://hi.4ggogo.com/hntest";
        String authorizationURL = baseURL + "/auth.html";
        String authRequestTime = sdf.format(new Date());
        String appKey = "c03ccba2b3f3eb9493e8005c5210410b";
        String appSecret = "22c0b9c2fa10cc7a1e56830937b48d15";

//            String appKey = "267f132eaebbd3d23055a151e3037a82";
//            String appSecret = "bc822ae8632e75a70b464fa7da9707b4";

        String sign = DigestUtils.sha256Hex(appKey + authRequestTime + appSecret);
        String requestXML = "<Request><Datetime>" + authRequestTime 
                + "</Datetime><Authorization><AppKey>" + appKey 
                + "</AppKey><Sign>" + sign + "</Sign></Authorization></Request>";
        System.out.println("认证接口调用：url = " + authorizationURL + ",param = " + requestXML);

        String response = doPost(authorizationURL, requestXML, null, null, "utf-8", false);

        //解析报文
        Element rootElement = Dom4jXml.getRootElement(response);
        Element authorization = rootElement.element("Authorization");
        String token = authorization.element("Token").getStringValue();

        String orderRequestTime = sdf.format(new Date());

//            //订购接口
//            String orderURL = baseURL + "/web-in/order/BOSSOrder.html";
//            String transIDO = "999999999";
//            String groupId = "222";
//            String orderBody = "<Request><DATETIME>" + orderRequestTime + "</DATETIME><CONTENT>"
//                    + "<TRANS_IDO>" + transIDO + "</TRANS_IDO>"
//                    + "<GRP_ID>" + groupId + "</GRP_ID>"
//                    + "<ITEM><PAK_NUM>1000</PAK_NUM><PAK_MONEY>415</PAK_MONEY><PAK_GPRS>10</PAK_GPRS><PAK_END_DTAE>" + orderRequestTime + "</PAK_END_DTAE></ITEM>"
//                    + "<ITEM><PAK_NUM>2000</PAK_NUM><PAK_MONEY>461</PAK_MONEY><PAK_GPRS>20</PAK_GPRS><PAK_END_DTAE>" + orderRequestTime + "</PAK_END_DTAE></ITEM>"
//                    + "<ITEM><PAK_NUM>3000</PAK_NUM><PAK_MONEY>471</PAK_MONEY><PAK_GPRS>30</PAK_GPRS><PAK_END_DTAE>" + orderRequestTime + "</PAK_END_DTAE></ITEM>"
//                    + "</CONTENT></Request>";
//            String orderSignatrue = DigestUtils.sha256Hex(orderBody + appSecret);
//            System.out.println("订购接口调用：url = " + orderURL + ",param = " + orderBody + ",token = " + token + ",signatrue = " + orderSignatrue);
//            String orderResponseBody = doPost(orderURL, orderBody, token, orderSignatrue, "utf-8", false);
//            System.out.println(orderResponseBody);

        //充值接口
//            String mobile = "13608067194";
//            String productId = "HN5ab11812b86b4b48912987d392134";
//            String serialNum = "201606071199913";
//            String chargeUrl = baseURL + "/boss/charge.html";
//            String chargeBody = "<Request><Datetime>" + orderRequestTime + "</Datetime><ChargeData>"
//        		  + "<Mobile>" + mobile + "</Mobile>"
//        		  + "<ProductId>" + productId + "</ProductId>"
//        		  +	"<SerialNum>" + serialNum + "</SerialNum>"
//        		  + "</ChargeData>";
//            String chargeSignatrue = DigestUtils.sha256Hex(chargeBody + appSecret);
//            String chargeResponse = doPost(chargeUrl, chargeBody, token, chargeSignatrue, "utf-8", false);
//            
//            System.out.println(chargeResponse);

        //mdrc营销口接口
        String operate = "Stock In";
        String cardNum = "100001623000100001000000004";//卡号
        String expireDate = "2016-12-31 23:59:59";//失效日期
        String exchangeCards = "201606071199913";//更改卡号
        String groupId = "8989859421";//企业编码
        String productCode = "201606071199913";//产品编码

        String operateUrl = baseURL + "/mdrc/operate.html";
        String operateBody = "<Request><Datetime>" + orderRequestTime + "</Datetime><Card>"
            + "<Operate>" + operate + "</Operate>"
            + "<CardNum>" + cardNum + "</CardNum>"
            + "<ExpireDate>" + expireDate + "</ExpireDate>"
            + "<ExchangeCards>" + exchangeCards + "</ExchangeCards>"
            + "<GroupId>" + groupId + "</GroupId>"
            + "<ProductCode>" + productCode + "</ProductCode>"
            + "</Card></Request>";
        String operateSignatrue = DigestUtils.sha256Hex(operateBody + appSecret);
        String operateResponse = doPut(operateUrl, operateBody, token, operateSignatrue, "utf-8", false);

        System.out.println(operateResponse);


    }

    /**
     * @param url
     * @param requestXML
     * @param token
     * @param signatrue
     * @param charset
     * @param pretty
     * @return
     */
    public static String doPut(String url, String requestXML, String token, String signatrue, String charset, boolean pretty) {
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
            method.setRequestEntity(new StringRequestEntity(requestXML, "application/xml", "utf-8"));

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
     * @return
     */
    public static String doPost(String url, String requestXML, String token, String signatrue, String charset, boolean pretty) {
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
            method.setRequestEntity(new StringRequestEntity(requestXML, "application/xml", "utf-8"));

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

