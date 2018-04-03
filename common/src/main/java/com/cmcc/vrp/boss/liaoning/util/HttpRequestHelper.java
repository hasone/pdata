/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.cmcc.vrp.boss.liaoning.util;

import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月19日 上午8:45:10
*/
public class HttpRequestHelper {
    private static transient Log log = LogFactory.getLog(HttpRequestHelper.class);

    private static HttpClient httpClient = null;

    static {
        httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        httpClient.getHttpConnectionManager().getParams()
                .setConnectionTimeout(Integer.valueOf("30000").intValue());
        httpClient.getHttpConnectionManager().getParams()
                .setSoTimeout(Integer.valueOf("30000").intValue());
    }

    /**
     * @param sysParams
     * @param busiParams
     * @return
     * @throws Exception
     */
    public static String request(Map<String, String> sysParams, String busiParams, String httpUrl) throws Exception {
        log.error("httpUrl:" + httpUrl);
        StringBuilder urlBuf = new StringBuilder(httpUrl).append("?");

        String[] paramKeys = (String[]) sysParams.keySet().toArray(new String[sysParams.size()]);
        for (int i = 0; i < paramKeys.length; ++i) {
            urlBuf.append(paramKeys[i]).append("=")
                    .append(URLEncoder.encode((String) sysParams.get(paramKeys[i]), "UTF-8"));
            if (i != paramKeys.length - 1) {
                urlBuf.append("&");
            }
        }

        PostMethod postMethod = new PostMethod(urlBuf.toString());
        RequestEntity entity = null;
        String response = null;
        try {
            String format = (String) sysParams.get("format");
            if ("json".equalsIgnoreCase(format)) {
                postMethod.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                entity = new StringRequestEntity(busiParams, "application/json", "UTF-8");
            } else if ("xml".equalsIgnoreCase(format)) {
                postMethod.setRequestHeader("Content-type", "text/xml; charset=UTF-8");
                entity = new StringRequestEntity(busiParams, "text/xml", "UTF-8");
            }

            postMethod.setRequestEntity(entity);
            httpClient.executeMethod(postMethod);
            byte[] result = postMethod.getResponseBody();
            response = new String(result, "UTF-8");
        } finally {
            postMethod.releaseConnection();
        }

        return response;
    }
}