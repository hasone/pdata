package com.cmcc.vrp.util;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import com.cmcc.vrp.exception.HttpBadRequestException;

/**
 * @author lgk8023
 *
 */
public class NmHttpUtils extends HttpUtils {

    private NmHttpUtils() {
        super();
    }

    /**
     * @param url
     * @param params
     * @param headers
     * @param contentType
     * @param responseHandler
     * @return
     * @throws HttpBadRequestException 
     */
    
    public static <T> T nmGet(String url, Map<String, String> params, Map<String, String> headers, String contentType, ResponseHandler<T> responseHandler)
            throws  HttpBadRequestException {
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
        //throw new HttpBadRequestException();
        try {
            return getCloseableHttpClient().execute(httpGet, responseHandler);
        } catch (HttpResponseException e1) {
            LOGGER.error("向{}地址发起GET请求时抛出异常,异常信息为{}, 异常堆栈为{}.", uri.toString(), e1.getMessage(), e1.getStatusCode());
            int statusCode = e1.getStatusCode();
            switch (statusCode) {
                case 400:
                    throw new HttpBadRequestException();
                default:
                    break;
            }
        } catch (IOException e) {
            LOGGER.error("向{}地址发起GET请求时抛出异常,异常信息为{}, 异常堆栈为{}.", uri.toString(), e.getMessage(), e.getStackTrace());
        }

        return null;
    }
    /**
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws HttpBadRequestException
     */
    public static String nmGet(String url, Map<String, String> params, Map<String, String> headers) throws HttpBadRequestException {
        return nmGet(url, params, headers, ContentType.DEFAULT_TEXT.getMimeType(), new Utf8BasicResponseHandler());
    }
}
