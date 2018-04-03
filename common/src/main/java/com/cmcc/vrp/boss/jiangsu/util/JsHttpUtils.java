package com.cmcc.vrp.boss.jiangsu.util;

import java.io.IOException;
import java.util.Map;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import com.cmcc.vrp.util.HttpUtils;

/**
 * @author lgk8023
 *
 */
public class JsHttpUtils extends HttpUtils{

    private JsHttpUtils() {
        super();
    }
    /**
     * @param url
     * @param body
     * @param headers
     * @param contentType
     * @param responseHandler
     * @return
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
            httpPost.setEntity(new StringEntity(body, "GBK"));
        }

        LOGGER.debug("开始向{}发起POST请求, 请求体为{}.", url, body);
        try {
            return getCloseableHttpClient().execute(httpPost, responseHandler);
        } catch (IOException e) {
            LOGGER.error("向{}地址发起POST请求时抛出异常,异常信息为{}, 异常堆栈为{}.", url, e.getMessage(), e.getStackTrace());
        }

        return null;
    }
}
