package com.cmcc.vrp.province.webin.controller;

import com.cmcc.vrp.ec.bean.AuthReq;
import com.cmcc.vrp.ec.bean.AuthReqData;
import com.cmcc.vrp.ec.bean.AuthResp;
import com.cmcc.vrp.ec.bean.ProductsResp;
import com.cmcc.vrp.ec.exception.ForbiddenException;
import com.cmcc.vrp.ec.exception.ParaErrorException;
import com.cmcc.vrp.ec.service.AuthService;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by leelyn on 2016/5/25.
 */
@Ignore
public class EcControllerTest {

    @Autowired
    AuthService authService;

    @Test
    public void getToken() {
        AuthResp resp;
        String appkey = "e4a94a4585c8e96d81d15bf25a075810";
        String appsecrect = "31a61033a3e4006d83a044cd17d06898";
        String requestTime = String.valueOf(System.currentTimeMillis());
        String sign = sign(requestTime, appkey, appsecrect);
        AuthReqData reqData = new AuthReqData();
        reqData.setAppKey(appkey);
        reqData.setSign(sign);
        AuthReq req = new AuthReq();
        req.setAuthorization(reqData);
        req.setRequestTime(requestTime);
        try {
            resp = authService.getToken(req, "127.0.0.1");
            XStream xStream = new XStream();
            xStream.alias("Response", ProductsResp.class);
            xStream.autodetectAnnotations(true);
            String str = xStream.toXML(resp);
            return;
        } catch (ParaErrorException e) {
            e.printStackTrace();
        } catch (ForbiddenException e) {
            e.printStackTrace();
        }
    }

    private String sign(String requestTime, String appKey, String appsecrect) {
        return DigestUtils.sha256Hex(appKey + requestTime + appsecrect);
    }
}
