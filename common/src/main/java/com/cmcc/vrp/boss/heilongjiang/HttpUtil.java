package com.cmcc.vrp.boss.heilongjiang;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

    private static Log log = LogFactory.getLog(HttpUtil.class);

    private static final int HTTP_METHOD_GET = 1;
    private static final int HTTP_METHOD_POST = 2;

    private static Charset CHARSET = Charset.forName("utf-8");
    private static HttpClient httpClient = null;
    private static final int HTTPCLIENT_MAXTOTAL = 100;
    private static final int HTTPCLIENT_MAXPERROUTE = 20;
    private static final int HTTPCLIENT_CONTIMEOUT = 60000;
    private static final int HTTPCLIENT_SOTIMEOUT = 50000;

    static {
        // 长连接保持30秒
        PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(30,
                TimeUnit.SECONDS);
        // 总连接数
        pollingConnectionManager.setMaxTotal(HTTPCLIENT_MAXTOTAL);
        // 同路由的并发数
        pollingConnectionManager.setDefaultMaxPerRoute(HTTPCLIENT_MAXPERROUTE);
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(pollingConnectionManager);
        // 重试次数，默认是3次，没有开启
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(2, true));
        // 保持长连接配置，需要在头添加Keep-Alive
        httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
        RequestConfig.Builder builder = RequestConfig.custom();
        builder.setConnectionRequestTimeout(200);
        builder.setConnectTimeout(HTTPCLIENT_CONTIMEOUT);
        builder.setSocketTimeout(HTTPCLIENT_SOTIMEOUT);

        RequestConfig requestConfig = builder.build();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
        headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
        //headers.add(new BasicHeader("Accept-Language", "zh-CN"));
        httpClientBuilder.setDefaultHeaders(headers);
        httpClient = httpClientBuilder.build();

    }

    public static void setCharset(String charset) {
        if ((null != charset) && (!"".equals(charset))) {
            CHARSET = Charset.forName(charset);
        }
    }

    public static String invoke(String url, String pin) {
        return invoke(url, HTTP_METHOD_POST, pin);
    }

    public static String invoke(String url, int method, String pin) {

        url = url.trim();
        URI uri = URI.create(url);
        return invoke(uri, method, pin);
    }

    public static String invoke(URI uri, int method, String pin) {
        if (log.isDebugEnabled()) {
            log.debug("pin=" + pin);
        }
        try {
            HttpUriRequest request = httpMethod(uri, method, pin);
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();
            String returnStr = EntityUtils.toString(entity, CHARSET);
            if (HttpStatus.SC_OK != statusCode) {
                log.debug("http status code " + statusCode + "\n" + returnStr);
            }
            return returnStr;
        } catch (ClientProtocolException cpe) {
            throw new RuntimeException("call [" + uri + "] error!", cpe);
        } catch (IOException ioe) {
            throw new RuntimeException("call [" + uri + "] error!", ioe);
        }
    }

    private static HttpUriRequest httpMethod(URI uri, int method, String pin) {
        switch (method) {
            case HTTP_METHOD_GET:
                return new HttpGet(uri);
            case HTTP_METHOD_POST:
                HttpPost httpPost = new HttpPost(uri);
                if ((null != pin) && (!"".equals(pin))) {
                    StringEntity reqEntity = new StringEntity(pin, CHARSET);
                    if ('<' == pin.charAt(0)) {
                        reqEntity.setContentType("application/xml;charset=" + CHARSET.name());
                        httpPost.setHeader("Accept", "application/xml");
                    } else {
                        reqEntity.setContentType("application/json;charset=" + CHARSET.name());
                        httpPost.setHeader("Accept", "application/json");
                    }
                    httpPost.setEntity(reqEntity);
                }
                return httpPost;
            default:
                throw new RuntimeException("http method [" + method + "] no implement!");
        }
    }
}
