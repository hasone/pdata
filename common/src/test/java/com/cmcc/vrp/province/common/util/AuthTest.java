package com.cmcc.vrp.province.common.util;

import com.cmcc.vrp.ec.bean.AuthReq;
import com.cmcc.vrp.ec.bean.AuthReqData;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Created by sunyiwei on 2016/5/30.
 */
public class AuthTest {
    @Test
    public void testAuth() throws Exception {
        String appKey = "63949039ad7e358bf67179c3359327f7";
        String appSecret = "7fb8e2104badd44a825f265536de9f37";

        String requestTime = new DateTime().toString();
        String sign = DigestUtils.sha256Hex(appKey + requestTime + appSecret);

        XStream xStream = new XStream();
        xStream.alias("Request", AuthReq.class);
        xStream.autodetectAnnotations(true);
        System.out.println(xStream.toXML(build(requestTime, appKey, sign)));
        System.out.println(DigestUtils.sha256Hex("7fb8e2104badd44a825f265536de9f37"));
        System.out.println(new DateTime().toString());
    }

    private AuthReq build(String requestTime, String appKey, String sign) {
        AuthReq authReq = new AuthReq();

        AuthReqData ard = new AuthReqData();
        ard.setAppKey(appKey);
        ard.setSign(sign);

        authReq.setAuthorization(ard);
        authReq.setRequestTime(requestTime);

        return authReq;
    }
}

