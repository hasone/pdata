package com.cmcc.vrp.boss.ecinvoker.ecservice.impl;

import com.cmcc.vrp.boss.ecinvoker.ecservice.EcAuthService;
import com.cmcc.vrp.ec.bean.AuthReq;
import com.cmcc.vrp.ec.bean.AuthReqData;
import com.cmcc.vrp.ec.bean.AuthResp;
import com.cmcc.vrp.util.HttpUtils;
import com.thoughtworks.xstream.XStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by leelyn on 2016/7/14.
 */
@Service
public class EcAuthServiceImpl implements EcAuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EcAuthServiceImpl.class);

    public static void main(String[] args) {
        DateTime startTime = new DateTime();
        DateTime endTime = startTime.plusMinutes(23);
        int a = Minutes.minutesBetween(startTime, endTime).getMinutes();
        System.out.println(a);
    }

    @Override
    public AuthResp auth(String appKey, String appSecrect, String url) {
        if (StringUtils.isBlank(appKey)
                || StringUtils.isBlank(appSecrect)) {
            return null;
        }
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        try {
            xStream.alias("Request", AuthReq.class);
            String reqStr = xStream.toXML(buildAuthReq(appKey, appSecrect));
            String respStr = HttpUtils.post(url, reqStr, ContentType.TEXT_XML.getMimeType());
            xStream.alias("Response", AuthResp.class);
            return (AuthResp) xStream.fromXML(respStr);
        } catch (Exception e) {
            LOGGER.error("Auth throw exception:{}", e);
        }
        return null;
    }

    private AuthReq buildAuthReq(String appKey, String appSecrect) {
        AuthReq requst = new AuthReq();
        AuthReqData authorizationReq = new AuthReqData();
        String date = new DateTime().toString();
        authorizationReq.setAppKey(appKey);
        authorizationReq.setAppSecret(appSecrect);
        authorizationReq.setSecInterface(null);
        authorizationReq.setSign(sign(appKey, appSecrect, date));
        requst.setAuthorization(authorizationReq);
        requst.setRequestTime(date);
        return requst;
    }

    private String sign(String appKey, String appSecrect, String date) {
        return DigestUtils.sha256Hex(appKey + date + appSecrect);
    }
}
