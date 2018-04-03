package com.cmcc.vrp.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用httpclient(apache, not commons-httpclient)实现的httpUtils类
 *
 * Created by sunyiwei on 2017/5/18.
 * 
 * 注，现在使用的默认responseHandle为Utf8BasicResponseHandler,如果需要特殊需求，请自行重载
 * Update by qihang on 2017/06/07 
 */
public abstract class HttpUtils {
    protected static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    protected static final String DEFAULT_CHARSET = Charsets.UTF_8.name();

    private static CloseableHttpClient closeableHttpClient = null;

    static {
        setCloseableHttpClient(HttpClients.custom()
                .setDefaultRequestConfig(buildConfig(10 * 1000, 60 * 1000, 5 * 1000)) //设置超时时间connectTimeout(连接超时),socketTimeout(响应超时),connectRequestTimeout
                .setRedirectStrategy(new LaxRedirectStrategy()) //设置重定向策略
                .setMaxConnPerRoute(100) //对于每个host,最多不超过100个连接
                .setMaxConnTotal(3000) //设置最多的连接数,默认200
                .setKeepAliveStrategy(new ConnectionKeepAliveStrategy(){

                    @Override
                    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                        return 0;
                    }
                    
                })    //一次请求建立一次连接
                .build());
    }

    protected HttpUtils() {
    }

    /**
     * 执行get请求, 并返回指定类型的对象
     *
     * @param url             请求地址
     * @param params          请求参数
     * @param contentType     content-type头
     * @param headers         请求头
     * @param responseHandler 响应消息的处理器,由该处理器将响应消息转化成相应的类型(如果需要的话)
     * @param <T>             期望的返回类型
     * @return 返回响应的对象
     */
    public static <T> T get(String url, Map<String, String> params, Map<String, String> headers,
                            String contentType, ResponseHandler<T> responseHandler) {
        ensureNotNull(url);

        URI uri = build(url, params);
        if (uri == null) {
            LOGGER.error("构建URI时返回null, 无法发起GET请求. Url = {}, Params = {}.", url, params);
            return null;
        }

        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader(buildContentTypeHeader(contentType));

        if (headers != null) {
            for (String key : headers.keySet()) {
                httpGet.setHeader(key, headers.get(key));
            }
        }

        LOGGER.debug("开始向{}发起GET请求.", uri.toString());

        try {
            return getCloseableHttpClient().execute(httpGet, responseHandler);
        } catch (IOException e) {
            LOGGER.error("向{}地址发起GET请求时抛出异常,异常信息为{}, 异常堆栈为{}.", uri.toString(), e.getMessage(), e.getStackTrace());
        }

        return null;
    }

    /**
     * 执行get请求, 并返回指定类型的对象
     *
     * @param url         请求地址
     * @param params      请求参数
     * @param contentType content-type头
     * @param headers     请求头
     * @param contentType contentType头
     * @return 返回响应的对象
     */
    public static String get(String url, Map<String, String> params, Map<String, String> headers, String contentType) {
        return get(url, params, headers, contentType, new Utf8BasicResponseHandler());
    }

    /**
     * 执行POST请求, 并返回指定类型的对象
     *
     * @param url             请求地址
     * @param body            请求消息体
     * @param headers         请求头
     * @param contentType     content-type
     * @param responseHandler 响应消息的处理器, 由该处理器将响应消息转化成相应的类型(如果有需要的话)
     * @param <T>             期望的返回类型
     * @return 返回响应的对象
     */
    public static <T> T post(String url, String body, Map<String, String> headers, String contentType, ResponseHandler<T> responseHandler) {
        ensureNotNull(url);

        HttpPost httpPost = new HttpPost(url);

        if (org.apache.commons.lang.StringUtils.isNotBlank(contentType)) {
            httpPost.setHeader(buildContentTypeHeader(contentType));
        }

        //设置头信息
        if (headers != null) {
            for (String name : headers.keySet()) {
                httpPost.setHeader(name, headers.get(name));
            }
        }

        //设置消息体
        if (org.apache.commons.lang.StringUtils.isNotBlank(body)) {
            httpPost.setEntity(new StringEntity(body, DEFAULT_CHARSET));
        }

        LOGGER.debug("开始向{}发起POST请求, 请求体为{}.", url, body);

        try {
            return getCloseableHttpClient().execute(httpPost, responseHandler);
        } catch (IOException e) {
            LOGGER.error("向{}地址发起POST请求时抛出异常,异常信息为{}, 异常堆栈为{}.", url, e.getMessage(), e.getStackTrace());
        }

        return null;
    }

    /**
     * 执行POST请求, 并返回指定类型的对象
     *
     * @param url             请求地址
     * @param body            请求消息体
     * @param contentType     content-type
     * @param responseHandler 响应消息的处理器, 由该处理器将响应消息转化成相应的类型(如果有需要的话)
     * @param <T>             期望的返回类型
     * @return 返回响应的对象
     */
    public static <T> T post(String url, String body, String contentType, ResponseHandler<T> responseHandler) {
        return post(url, body, null, contentType, responseHandler);
    }

    /**
     * 执行POST请求, 并返回指定类型的对象
     *
     * @param url         请求地址
     * @param body        请求消息体
     * @param contentType content-type
     * @return 返回响应的对象
     */
    public static String post(String url, String body, String contentType) {
        return post(url, body, contentType, new Utf8BasicResponseHandler());
    }

    /**
     * 执行POST请求, 并返回指定类型的对象
     *
     * @param url  请求地址
     * @param body 请求消息体
     * @return 返回响应的对象
     */
    public static String post(String url, String body) {
        return post(url, body, ContentType.DEFAULT_TEXT.getMimeType());
    }

    /**
     * 执行POST请求, 并返回指定类型的对象
     *
     * @param url         请求地址
     * @param body        请求消息体
     * @param contentType content-type
     * @param headers     请求头
     * @return 返回响应的对象
     */
    public static String post(String url, String body, String contentType, Map<String, String> headers) {
        return post(url, body, headers, contentType, new Utf8BasicResponseHandler());
    }

    /**
     * 执行get请求, 并默认返回消息体内容
     *
     * @param url         请求地址
     * @param params      请求参数
     * @param contentType content-type
     * @return 消息响应体
     */
    public static String get(String url, Map<String, String> params, String contentType) {
        return get(url, params, null, contentType, new Utf8BasicResponseHandler());
    }

    /**
     * 执行get请求, 并默认返回消息体内容
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @return 消息响应体
     */
    public static String get(String url, Map<String, String> params, Map<String, String> headers) {
        return get(url, params, headers, ContentType.DEFAULT_TEXT.getMimeType(), new BasicResponseHandler());
    }

    /**
     * 执行get请求,并返回消息体内容
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 消息响应体
     */
    public static String get(String url, Map<String, String> params) {
        return get(url, params, ContentType.DEFAULT_TEXT.getMimeType());
    }

    /**
     * 执行get请求, 并返回消息体内容
     *
     * @param url 请求地址
     * @return 消息响应体
     */
    public static String get(String url) {
        return get(url, null);
    }


    protected static Header buildContentTypeHeader(String contentTypeStr) {
        ContentType contentType = ContentType.create(contentTypeStr, DEFAULT_CHARSET);
        return new BasicHeader("Content-Type", contentType.toString());
    }

    protected static URI build(String url, Map<String, String> params) {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);

            List<NameValuePair> nvps = convert(params);
            if (nvps != null && !nvps.isEmpty()) {
                uriBuilder.setParameters(nvps);
            }

            return uriBuilder.build();
        } catch (URISyntaxException e) {
            LOGGER.error("构建URI时抛出异常,异常信息为{}, 异常堆栈为{}.", e.getMessage(), e.getStackTrace());
        }

        return null;
    }

    private static List<NameValuePair> convert(Map<String, String> params) {
        if (params == null) {
            return null;
        }

        List<NameValuePair> nvps = new LinkedList<NameValuePair>();
        for (String key : params.keySet()) {
            NameValuePair nvp = new BasicNameValuePair(key, params.get(key));
            nvps.add(nvp);
        }

        return nvps;
    }

    protected static void ensureNotNull(String value) {
        if (org.apache.commons.lang.StringUtils.isBlank(value)) {
            throw new NullPointerException();
        }
    }

    private static RequestConfig buildConfig(int connectTimeout, int socketTimeout, int connectRequestTimeout) {
        return RequestConfig.custom()
                .setConnectTimeout(connectTimeout) //这个指的是连接超时, 只在建立TCP连接时有用
                .setSocketTimeout(socketTimeout) //这个指的是响应超时, 服务端需要在这个时间内给出响应
                .setConnectionRequestTimeout(connectRequestTimeout) //这个是指从连接池获取连接的超时时间
                .build(); //两者的单位均为毫秒
    }

    public static CloseableHttpClient getCloseableHttpClient() {
        return closeableHttpClient;
    }

    public static void setCloseableHttpClient(CloseableHttpClient closeableHttpClient) {
        HttpUtils.closeableHttpClient = closeableHttpClient;
    }
   
}
