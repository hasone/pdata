package com.cmcc.vrp.province.service.impl;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.Charsets;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by sunyiwei on 2016/9/19.
 */
public class StatementControllerTest extends InterfaceTest {
//    @Test
//    public void testGet() throws Exception {
//        final String startTime = "2016-09-01T00:00:00.00+08:00";
//        final String endTime = "2016-09-19T00:00:00.00+08:00";
//        final String url = "http://localhost:8080/web-in/statement.html";
//
//        query(startTime, endTime, url, false);
//        query(startTime, endTime, url, true);
//    }

    @Test
    public void testGetInvalid() throws Exception {
        final String dt1 = "2016-9-01T00:00:00.00+08:00";
        final String dt2 = "2016-09-19T00:00:00.00+08:00";
        final String dt3 = "2016-09-19";
        final String dt4 = "2016-09-19T00:00:00+08:00";
        final String dt5 = "2016-09-19T00:00:00";
        final String dt6 = "2016-09-19T00:00:00+08";
        final String dt7 = "2016-09-19T00:00:00+";

        assertFalse(match(dt1));
        assertTrue(match(dt2));
        assertTrue(match(dt3));
        assertTrue(match(dt4));
        assertFalse(match(dt5));
        assertFalse(match(dt6));
        assertFalse(match(dt7));
    }

    //正则匹配: yyyy-MM-ddThh:mm:ss+08:00
    private boolean match(String time) {
        final String REGEXP = "^[0-9]{4}-[0-9]{2}-[0-9]{2}(T[0-9]{2}:[0-9]{2}:[0-9]{2}(.[0-9]{2})?(\\+|\\-)[0-9]{2}:[0-9]{2})?$";
        Pattern pattern = Pattern.compile(REGEXP);
        Matcher matcher = pattern.matcher(time);
        return matcher.matches();
    }

    private void query(String startTime, String endTime, String url, boolean needDetails) {
        //1. 获取token
        String token = getToken();
        System.out.println("Token = " + token);

        String reqUrl = build(url, encode(startTime), encode(endTime), needDetails);
        String resp = doGet(reqUrl, buildHeaders(token, ""));
        assertNotNull(resp);
        System.out.println(resp);
    }

    private String build(String url, String beginTime, String endTime, boolean needDetails) {
        String tmp = url + "?startTime=" + beginTime + "&endTime=" + endTime;
        return needDetails ? tmp + "&fingerprint" : tmp;
    }

    private String encode(String time) {
        return new String(Base64.encodeBase64(time.getBytes(Charsets.UTF_8)));
    }
}