package com.cmcc.vrp.async.controller;

import com.cmcc.vrp.ec.bean.AuthReq;
import com.cmcc.vrp.ec.bean.AuthReqData;
import com.cmcc.vrp.ec.bean.AuthResp;
import com.cmcc.vrp.ec.exception.ForbiddenException;
import com.cmcc.vrp.ec.exception.ParaErrorException;
import com.cmcc.vrp.ec.service.AuthService;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * <p>Title:AuthControllerTest </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年6月13日
 */
@Ignore
public class AuthControllerTest {

    @Autowired
    AuthService authService;

    /**
     * 
     * title: authTest desc: wuguoping 2017年6月2日
     */
    @Test
    public void authTest() {
        String appKey = "e4a94a4585c8e96d81d15bf25a075810";
        String appSecret = "31a61033a3e4006d83a044cd17d06898";
        String requestTime = getDeadTime();
        String sign = sign(requestTime, appKey, appSecret);

        AuthReq authReq = new AuthReq();
        AuthReqData authReqData = new AuthReqData();
        authReqData.setAppKey(appKey);
        authReqData.setSecInterface("1");
        authReqData.setSign(sign);
        authReq.setRequestTime(requestTime);
        authReq.setAuthorization(authReqData);

        XStream xStream = new XStream();
        xStream.alias("request", AuthReq.class);
        xStream.autodetectAnnotations(true);
        System.out.println(xStream.toXML(authReq));

        try {
            AuthResp resp = authService.getToken(authReq, "127.0.0.1");
            XStream xStream2 = new XStream();
            xStream2.alias("Response", AuthResp.class);
            xStream2.autodetectAnnotations(true);
            String str = xStream2.toXML(resp);
            System.out.println(str);
        } catch (ParaErrorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ForbiddenException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    private String getDeadTime() {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
        return DateFormatUtils.format(new Date(), pattern);
    }

    private String sign(String requestTime, String appKey, String appsecrect) {
        return DigestUtils.sha256Hex(appKey + requestTime + appsecrect);
    }

}
