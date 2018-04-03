/**
 * @Title: HttpUtil.java
 * @Package com.cmcc.vrp.anhui.common.util
 * @author: sunyiwei
 * @date: 2015年6月3日 上午10:46:02
 * @version V1.0
 */
package com.cmcc.vrp.boss.gansu;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @ClassName: HttpUtil
 * @Description: TODO
 * @author: sunyiwei
 * @date: 2015年6月3日 上午10:46:02
 */
public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 执行一个HTTP GET请求，返回请求响应的HTML
     *
     * @param url         请求的URL地址
     * @param queryString 请求的查询参数,可以为null
     * @param charset     字符集
     * @param pretty      是否美化
     * @return 返回请求响应的HTML
     */
    public static String doGet(String url, String queryString, String charset,
                               boolean pretty) {
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(url);

        try {
            if (StringUtils.isNotBlank(queryString)) {
                method.setQueryString(URIUtil.encodeQuery(queryString));
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
            logger.error("执行HTTP Get请求时，编码查询字符串“" + queryString + "”发生异常！", e);
        } catch (IOException e) {
            logger.error("执行HTTP Get请求" + url + "时，发生异常！", e);
        } finally {
            method.releaseConnection();
        }

        return response.toString();
    }

    /**
     * 执行一个HTTP POST请求，返回请求响应的HTML
     *
     * @param url     请求的URL地址
     * @param reqStr  请求的查询参数,可以为null
     * @param charset 字符集
     * @param pretty  是否美化
     * @return 返回请求响应的HTML
     */
    public static String doPost(String url, String reqStr, String contentType, String charset, boolean pretty) {
        HttpClient client = new HttpClient();

        PostMethod method = new PostMethod(url);
        try {          
            HttpConnectionManagerParams managerParams = client 
                    .getHttpConnectionManager().getParams();             
            managerParams.setConnectionTimeout(30000); // 设置连接超时时间(单位毫秒)             
            managerParams.setSoTimeout(30000); // 设置读数据超时时间(单位毫秒) 
            
            method.setRequestEntity(new StringRequestEntity(reqStr, contentType, "utf-8"));

            client.executeMethod(method);
            logger.info("返回的状态码为" + method.getStatusCode());
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                return StreamUtils.copyToString(method.getResponseBodyAsStream(), Charset.forName(charset));
            }
        } catch (UnsupportedEncodingException e1) {
            logger.error(e1.getMessage());
            return "";
        } catch (IOException e) {
            logger.error("执行HTTP Post请求" + url + "时，发生异常！" + e.toString());

            return "";
        } finally {
            method.releaseConnection();
        }

        return null;
    }

    /**
     * 执行一个HTTP POST请求，返回请求响应的HTML
     *
     * @param url     请求的URL地址
     * @param reqStr  请求的查询参数,可以为null
     * @param charset 字符集
     * @param pretty  是否美化
     * @return 返回请求响应的HTML
     */
    public static String doPost(String url, String reqStr, String charset, boolean pretty) {
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();

        PostMethod method = new PostMethod(url);
        try {
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
            System.out.println("执行HTTP Post请求" + url + "时，发生异常！" + e.toString());

            return "";
        } finally {
            method.releaseConnection();
        }

        return response.toString();
    }
    
    /**
     * create by qihang
     * @param url
     * @param reqStr
     * @param charset
     * @param pretty
     * @param headerMap 需要设置到httpHeader中的 元素，key-value格式
     * @return 
     */
    public static String doPost(String url, String reqStr, String charset, boolean pretty,Map<String, String> headerMap) {
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();
        client.getParams().setContentCharset("UTF-8");
        /*method.setRequestHeader("platformId", "TXL160603");
        method.setRequestHeader("userToken", "");*/

        PostMethod method = new PostMethod(url);
        try {
            if(StringUtils.isNotEmpty(reqStr)){
                method.setRequestEntity(new StringRequestEntity(reqStr, "text/plain", "utf-8"));
            }
            //client.setConnectionTimeout(1000 * 60 * 3);
            
            HttpConnectionManagerParams managerParams = client 
                      .getHttpConnectionManager().getParams(); 

            // 设置连接的超时时间 
            managerParams.setConnectionTimeout(3 * 60 * 1000); 

            // 设置读取数据的超时时间 
            managerParams.setSoTimeout(5 * 60 * 1000); 
            
            for(Map.Entry<String, String> entry:headerMap.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                method.setRequestHeader(key, value);
                logger.info("设置header,key:"+key+",value:"+value);
            }
            
            
            client.executeMethod(method);
            
            logger.info("http的请求地址为:"+url+",返回的状态码为"+method.getStatusCode());
            

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(method.getResponseBodyAsStream(),
                            charset));
            String line;
            while ((line = reader.readLine()) != null) {
                if (pretty){
                    response.append(line).append(
                            System.getProperty("line.separator"));
                }else{
                    response.append(line);
                }
            }
            logger.info("http的请求地址为:"+url+",返回的数据为"+response.toString());
            reader.close();
            
        } catch (UnsupportedEncodingException e1) {
            logger.error(e1.getMessage());
            return "";
        }catch (IOException e) {
            logger.error("执行HTTP Post请求" + url + "时，发生异常！"+ e);
            return "";
        }catch (Exception e) {
            logger.error("执行HTTP Post请求" + url + "时，发生异常！"+ e);
            return "";
        }finally {
            method.releaseConnection();
        }
        
        return response.toString();
    }
    
    /**
     * post 返回状态码和信息
     */
    public static HttpRespModel doPostRespCode(String url, String reqStr, String charset, boolean pretty,Map<String, String> headerMap) {
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();
        client.getParams().setContentCharset("UTF-8");

        PostMethod method = new PostMethod(url);
        HttpRespModel respModel = new HttpRespModel();
        try {
            if(StringUtils.isNotEmpty(reqStr)){
                method.setRequestEntity(new StringRequestEntity(reqStr, "text/plain", "utf-8"));
            }
            
            HttpConnectionManagerParams managerParams = client 
                      .getHttpConnectionManager().getParams(); 

            // 设置连接的超时时间 
            managerParams.setConnectionTimeout(3 * 60 * 1000); 

            // 设置读取数据的超时时间 
            managerParams.setSoTimeout(5 * 60 * 1000); 
            
            for(Map.Entry<String, String> entry:headerMap.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                method.setRequestHeader(key, value);
                logger.info("设置header,key:"+key+",value:"+value);
            }
            
            
            client.executeMethod(method);
            
            logger.info("http的请求地址为:"+url+",返回的状态码为"+method.getStatusCode());
            respModel.setCode(method.getStatusCode());
            

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(method.getResponseBodyAsStream(),
                            charset));
            String line;
            while ((line = reader.readLine()) != null) {
                if (pretty){
                    response.append(line).append(
                            System.getProperty("line.separator"));
                }else{
                    response.append(line);
                }
            }
            
            respModel.setMsg(response.toString());
            logger.info("http的请求地址为:"+url+",返回的数据为"+respModel.getMsg());
            
            reader.close();
            
        } catch (UnsupportedEncodingException e1) {
            logger.error(e1.getMessage());
            return new HttpRespModel(-1,"UnsupportedEncodingException");
        }catch (IOException e) {
            logger.error("执行HTTP Post请求" + url + "时，发生异常！"+ e);
            return new HttpRespModel(-1,"IOException");
        }catch (Exception e) {
            logger.error("执行HTTP Post请求" + url + "时，发生异常！"+ e);
            return new HttpRespModel(-1,"Exception");
        }finally {
            method.releaseConnection();
        }
        
        return respModel;
    }

    

    public static void main(String[] args) {
        String y = doPost("https://pdataqa.4ggogo.com/web-in/api/charge.html", "fdsafdas",
            "GBK", true);
        System.out.println(y);
    }
}
