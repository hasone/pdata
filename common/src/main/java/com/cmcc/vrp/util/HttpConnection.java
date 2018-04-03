package com.cmcc.vrp.util;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * http请求方法
 *
 * @author JamieWu
 */
public class HttpConnection {
    private static Logger logger = LoggerFactory.getLogger(HttpConnection.class);
    private static String responseMsg = "";

    @Autowired
    MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager;

    /** 
     * @Title: sendPostByString 
     */
    public static String sendPostByString(String url, String requestMsg) throws Exception {
        if (url == null) {
            logger.error("ERROR: url in node is null!");
            throw new Exception();
        }

        HttpClient client = new HttpClient();
        
        //  链接超时
        client.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        //	读取超时
        client.getHttpConnectionManager().getParams().setSoTimeout(50000);

        PostMethod postMethod = new UTF8PostMethod(url);

        BufferedReader reader = null;
        try {
            postMethod.setRequestBody(requestMsg);
            // 执行postMethod

            int statusCode = client.executeMethod(postMethod);

            logger.info("statusCode: " + statusCode);

            if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
                || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                // 从头中取出转向的地址
                Header locationHeader = postMethod
                    .getResponseHeader("location");
                String location = null;
                if (locationHeader != null) {
                    location = locationHeader.getValue();
                    logger.warn("The page was redirected to:" + location);
                } else {
                    logger.warn("Location field value is null.");
                }
                return null;
            } else if (statusCode != HttpStatus.SC_OK) {
                logger.warn("Method is wrong " + postMethod.getStatusLine());
            }

            InputStream responseBody = postMethod.getResponseBodyAsStream();

            if (responseBody == null) {
                logger.debug("post方法返回报文数据流为空！");
                return responseMsg;
            }

            reader = new BufferedReader(new InputStreamReader(
                responseBody, "utf-8"));

            String line = reader.readLine();
            while (line != null) {
                responseMsg += line;
                System.out.println(new String(line.getBytes()));
                line = reader.readLine();
            }
            return responseMsg;

        } catch (HttpException e) {
            // TODO: handle exception
            System.out.println("Check http address!");
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println("the line is wrong!");
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            postMethod.releaseConnection();
        }
        return null;
    }

    /** 
     * @Title: sendPostByForm 
     */
    public static String sendPostByForm(String url, List<BasicNameValuePair> formParams) {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost(url);

        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
            httppost.setEntity(uefEntity);

            logger.debug("请求参数  " + uefEntity.toString());
            logger.debug("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();

                if (null != entity) {
                    String responseStr = EntityUtils.toString(entity, "UTF-8");
                    logger.debug("请求返回内容: " + responseStr);
                    return responseStr;
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            logger.info("http请求出错: " + e.toString());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            logger.info("http请求出错: " + e1.toString());
            e1.printStackTrace();
        } catch (IOException e) {
            logger.info("http请求出错: " + e.toString());
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                logger.info("http关闭出错: " + e.toString());
                e.printStackTrace();
            }
        }
        return null;
    }

    /** 
     * @Title: sendGetRequest 
     */
    public static String sendGetRequest(String url, String params) throws IllegalStateException, IOException {
        HttpClient client = new HttpClient();       
        
        //  链接超时
        client.getHttpConnectionManager().getParams().setConnectionTimeout(50000);
        //  读取超时
        client.getHttpConnectionManager().getParams().setSoTimeout(50000);

        StringBuilder sb = new StringBuilder();
        //路径与参数组合
        String totalUrl = url;
        if(!StringUtils.isEmpty(params)){
            totalUrl = totalUrl + "?" + params;
        }
        
        // InputStream ins = null;
        // Create a method instance.
        GetMethod method = new GetMethod(totalUrl);

        method.addRequestHeader("Connection", "close");

        //method.addRequestHeader("Content-Type", "text/html; charset=UTF-8");
        client.getParams().setContentCharset("UTF-8");
        // Provide custom retry handler is necessary

        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
            new DefaultHttpMethodRetryHandler(3, false));
        try {
            // Execute the method.
            logger.info("http get start!");
            int statusCode = client.executeMethod(method);

            if (statusCode == HttpStatus.SC_OK) {
//                ins = method.getResponseBodyAsStream();
//                byte[] b = new byte[1024];
//                int r_len = 0;
//                while ((r_len = ins.read(b)) > 0) {
//                    sb.append(new String(b, 0, r_len, method
//                            .getResponseCharSet()));
//                }

                return StreamUtils.copyToString(method.getResponseBodyAsStream(), Charset.forName(method.getResponseCharSet()));
            } else {
                logger.info("Response Code: " + statusCode);
            }
        } catch (HttpException e) {
            logger.info("Fatal protocol violation: " + e.toString());
        } catch (IOException e) {
            logger.info("Fatal transport error: " + e.toString());
        } finally {
            method.releaseConnection();
            /*if (ins != null) {
                ins.close();
            } */
            logger.info("http get end!");
        }
        return sb.toString();
    }

    public static String doPost(String url, String reqStr, String contentType, String charset, boolean pretty) {
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();

        PostMethod method = new PostMethod(url);
        try {
            method.setRequestEntity(new StringRequestEntity(reqStr, contentType, "utf-8"));

            client.executeMethod(method);
            //if (method.getStatusCode() == HttpStatus.SC_OK) {
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
            //}
        } catch (UnsupportedEncodingException e1) {
//		      log.error(e1.getMessage());
            System.out.println(e1.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println(e.getMessage());
//		      log.error("执行HTTP Post请求" + url + "时，发生异常！", e);
            return null;
        } finally {
            method.releaseConnection();
        }

        return response.toString();
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    // Inner class for UTF-8 support
    public static class UTF8PostMethod extends PostMethod {
        public UTF8PostMethod(String url) {
            super(url);
        }

        @Override
        public String getRequestCharSet() {
            // return super.getRequestCharSet();
            return "UTF-8";
        }
    }
}
