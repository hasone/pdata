package com.cmcc.vrp.boss.jiangsu.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.util.EntityUtils;


/**
 * @author lgk8023
 *
 */
public class GbkBasicResponseHandler extends BasicResponseHandler{
    @Override
    public String handleResponse(final HttpResponse response)
            throws HttpResponseException, IOException {
        final StatusLine statusLine = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        if (statusLine.getStatusCode() >= 300) {//状态码超过300抛出异常，如果需要更改，请自己进行重载
            EntityUtils.consume(entity);
            throw new HttpResponseException(statusLine.getStatusCode(),
                    statusLine.getReasonPhrase());
        }
        return entity == null ? null : 
            EntityUtils.toString(entity,"GBK");
    }
}

