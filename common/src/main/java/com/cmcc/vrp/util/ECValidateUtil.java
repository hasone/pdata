package com.cmcc.vrp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: wuguoping
 * @data: 2017年5月9日
 */
public class ECValidateUtil {
    private static String TIME_FOMMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    private static final Logger logger = LoggerFactory
			.getLogger(ECValidateUtil.class);

	/**
	 * 用于截取Url
	 * 
	 * @param url
	 * @param startPattern
	 * @param endPattern
	 * @return
	 */
    public static String getInterceptUrl(String url, String pattern) {
        int start = 0;
        int end = url.indexOf(pattern);

        return url.substring(start, end);
    }

	/**
	 * 验证某节点是否为空
	 * 
	 * @param resultMap
	 * @param node
	 * @return
	 */
    public static boolean isBlank(Map<String, Object> resultMap, String node) {
        boolean flag = false;
        if ("".equals(node) || node == null) {
            resultMap.put("message", "发送请求前，有错误。");
            resultMap.put("errorMessage", node + "节点为空。");
            resultMap.put("apiResponse", null);
            flag = true;
        }
        return flag;
    }

	/**
	 * 验证是否是大陆号码
	 * 
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 */
    public static boolean isChinaPhoneLegal(String str)
			throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

	/**
	 * 验证是否是香港号码
	 * 
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 */
    public static boolean isHKPhoneLegal(String str)
			throws PatternSyntaxException {
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

	/**
	 * 获取格式错误
	 * 
	 * @param str
	 * @return
	 */
    public static String getInfo(String str) {
        String[] patton = { "Caused by: java.io.EOFException",
            "Caused by: org.xmlpull.v1.XmlPullParserException:",
            "input contained no data" };

        String result = "";
        for (String strP : patton) {
            int startIndex = 0;
            int endInde = 0;
            startIndex = str.indexOf(strP);
            if (startIndex == -1) {
                continue;
            }

            endInde = str.indexOf("\n", startIndex);
            result = str.substring(startIndex, endInde);
        }
        if ("".equals(result)) {
            result = "节点标签书写不正确。";
        }
        return result;
    }

	/**
	 * 处理http post()请求 ， 返回map对象
	 * 
	 * @param url
	 * @param requestXML
	 * @param token
	 * @param signatrue
	 * @param charset
	 * @param pretty
	 * @param contentType
	 * @return
	 */
    public static Map<String, String> doPost(String url, String requestXML,
			String token, String signatrue, String charset, boolean pretty,
			String contentType) {
        logger.info("---开始执行Post请求--- ");
        StringBuffer response = new StringBuffer();
        Map<String, String> map = new HashMap<String, String>();
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);
        String code;

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
            logger.info("执行Post()返回的状态码为:" + method.getStatusCode());

            code = String.valueOf(method.getStatusCode());
            map.put("code", code + " " + method.getStatusText());

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
                logger.info("返回的数据为" + response.toString());
                reader.close();
                map.put("message", response.toString());
            } else {
                logger.info("返回的状态码不为200，但是请求过程没错。");
                map.put("message", response.toString());
            }
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            logger.error("message",
					"执行HTTP Post请求" + url + "时，发生异常！" + e1.getMessage());
            map.put("message",
					"执行HTTP Post请求" + url + "时，发生异常！" + e1.getMessage());
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("message",
					"执行HTTP Post请求" + url + "时，发生异常！" + e.getMessage());
            map.put("message",
					"执行HTTP Post请求" + url + "时，发生异常！" + e.getMessage());
            return map;
        } catch (Exception e2) {
            e2.printStackTrace();
            logger.error("message",
					"执行HTTP Post请求" + url + "时，发生异常！" + e2.getMessage());
            map.put("message",
					"执行HTTP Post请求" + url + "时，发生异常！" + e2.getMessage());
            return map;
        } finally {
            method.releaseConnection();
        }
        return map;
    }

	/**
	 * 处理http get()请求 ， 返回map对象
	 * 
	 * @param url
	 * @param queryString
	 * @param token
	 * @param signatrue
	 * @param charset
	 * @param pretty
	 * @return
	 */
    public static Map<String, String> doGet(String url, String queryString,
			String token, String signatrue, String charset, boolean pretty) {
        logger.info("---开始执行Get请求--- ");
        StringBuffer response = new StringBuffer();
        Map<String, String> map = new HashMap<String, String>();
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(url);
        String code;

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
            code = String.valueOf(method.getStatusCode());
            map.put("code", code + " " + method.getStatusText());
            logger.info("返回的code为： " + code);
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
                map.put("message", response.toString());
                logger.info("返回的数据为" + response.toString());
            } else {
                map.put("message", response.toString());
                logger.info("返回的状态码不为200，但是请求过程没错。");
            }
        } catch (URIException e) {
            e.printStackTrace();
            logger.error("执行HTTP Get请求时，编码查询字符串'" + queryString + "'发生异常！"
					+ e.getMessage());
            map.put("message", "执行HTTP Get请求时，编码查询字符串'" + queryString
					+ "'发生异常！" + e.getMessage());
            return map;
        } catch (IOException e1) {
            e1.printStackTrace();
            logger.error("执行HTTP Get请求时，编码查询字符串'" + queryString + "'发生异常！"
					+ e1.getMessage());
            map.put("message", "执行HTTP Get请求时，编码查询字符串'" + queryString
					+ "'发生异常！" + e1.getMessage());
            return map;
        } catch (Exception e2) {
            e2.printStackTrace();
            logger.error("执行HTTP Get请求时，编码查询字符串'" + queryString + "'发生异常！"
					+ e2.getMessage());
            map.put("message",
					"执行HTTP Get请求" + url + "时，发生异常！" + e2.getMessage());
            return map;
        } finally {
            method.releaseConnection();
        }
        return map;
    }

	/**
	 * 时间格式校验
	 * 
	 * @param str
	 * @return
	 */
    public static boolean validateDateFormat(String str) {
        boolean flag = true;
        SimpleDateFormat format = new SimpleDateFormat(TIME_FOMMAT);
        try {
            format.parse(str);
        } catch (ParseException e) {
            flag = false;
        }
        return flag;
    }

	/**
	 * 获取给出时间与当前时间的时间间隔
	 * 
	 * @param createtime
	 * @return
	 */
    public static long getInterval(String createtime) {
        SimpleDateFormat sd = new SimpleDateFormat(TIME_FOMMAT);
        ParsePosition pos = new ParsePosition(0);
        Date d1 = (Date) sd.parse(createtime, pos);
        double time = new Date().getTime() - d1.getTime();

        return (long) Math.ceil(time);
    }

	/**
	 * 延签
	 * 
	 * @param originalSign
	 * @param xml
	 * @return
	 */
    public static boolean verifySign(String originalSign, String xml) {
        return DigestUtils.sha256Hex(xml).equals(originalSign);
    }

	/**
	 * 获取token
	 * 
	 * @param authorizationURL
	 * @param appKey
	 * @param appSecret
	 * @return
	 * @throws DocumentException
	 */
    public static Map<String, String> getToken(String authorizationURL,
			String appKey, String appSecret, String authRequestTime,
			String authRequesSign) throws DocumentException {

        logger.info("----开始执行getToken()------");
        String requestXML = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><Request><Datetime>"
				+ authRequestTime
				+ "</Datetime><Authorization><AppKey>"
				+ appKey
				+ "</AppKey><Sign>"
				+ authRequesSign
				+ "</Sign></Authorization></Request>";
        Map<String, String> map = ECValidateUtil.doPost(authorizationURL,
				requestXML, null, null, "utf-8", false, "application/xml");

        return map;
    }
    
    /**
     * 保留2位小数
     * title: get2Double
     * desc: 
     * @param a
     * @return
     * wuguoping
     * 2017年6月19日
     */
    public static double get2Double(double number){
        DecimalFormat df=new DecimalFormat("0.00");
        return new Double(df.format(number).toString());
    }
    
    /**
     * double 转 String
     * title: double2String
     * desc: 
     * @param num
     * @return
     * wuguoping
     * 2017年6月19日
     */
    public static String double2String(double num){
        DecimalFormat format = new DecimalFormat("0.00");
        
        return format.format(num);
    }
}
