package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.boss.ecinvoker.impl.SdEcBossServiceImpl;
import com.cmcc.vrp.boss.guangdong.GdBossServiceImpl;
import com.cmcc.vrp.boss.guangxi.GxBossServiceImpl;
import com.cmcc.vrp.boss.shanxi.SxBossServiceImpl;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.charge.ChargeService;
import com.cmcc.vrp.ec.bean.AuthReq;
import com.cmcc.vrp.ec.bean.AuthReqData;
import com.cmcc.vrp.ec.bean.ChargeReq;
import com.cmcc.vrp.ec.bean.ChargeReqData;
import com.cmcc.vrp.ec.bean.ChargeResp;
import com.cmcc.vrp.ec.bean.ProductsResp;
import com.cmcc.vrp.enums.AccountType;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * Created by leelyn on 2016/6/29.
 */
@Ignore
public class ChargeServiceTest extends InterfaceTest {
    @Autowired
    private ChargeService chargeService;

    @Autowired
    private SdEcBossServiceImpl sdBossService;

    @Autowired
    private GdBossServiceImpl gdBossService;

    @Autowired
    private SxBossServiceImpl sxBossService;

    @Autowired
    private GxBossServiceImpl gxBossService;

    @Ignore
    @Test
    public void SdChargeServicImpl() {
        sdBossService.charge(1l, 26l, "18853861538", "123456789", null);
    }

    @Ignore
    @Test
    public void SxChargerServiceImpl() {
        sxBossService.charge(1l, 1l, "15291483570", "sx123456789", null);
    }

    @Ignore
    @Test
    public void gdChargeServicImpl() {
        gdBossService.charge(103L, 23L, "18853861211", "adfasfasf", null);
    }

    @Ignore
    @Test
    public void gxChargeServicImpl() {
        gxBossService.charge(1l, 21l, "13597092654", "123456789", null);
    }

    @Test
    @Ignore
    public void chargeTest() {
        ChargeResult result = chargeService.charge(1L, 96l, 123l, 404l, AccountType.ENTERPRISE, "18837190160", "1234567899henan");
        System.out.println(result);
    }

    @Test
    public void testChargeController() throws Exception {
        //1. 获取token
        String token = getToken();
        System.out.println("Token = " + token);

        //2. 发起充值请求
        ChargeResp chargeResp = charge(token);
        System.out.println(new Gson().toJson(chargeResp));
    }

//    @Test
//    public void testShuanChuan() throws Exception {
//        final String AUTH_URL = "http://localhost:8080/web-in/auth.html";
//
//        String authStr = buildAuthStr(buildShuanChuan());
//        String tokenResult = HttpUtil.doPost(AUTH_URL, authStr, "utf-8", true);
//        AuthResp authResp = parse(tokenResult);
//        System.out.println(new Gson().toJson(authResp));
//    }

    @Test
    @Ignore
    public void testQueryProductController() throws Exception {
        //1. 获取token
        String token = getToken();
        System.out.println("Token = " + token);

        //2. 获取产品信息
        ProductsResp productsResp = query(token);
        System.out.println(new Gson().toJson(productsResp));
    }

    private ProductsResp query(String token) {
        final String QUERY_URL = "http://localhost:8080/web-in/products.html";
        String queryProductResp = doGet(QUERY_URL, buildHeaders(token, ""));
        return parseQueryProductResp(queryProductResp);
    }

    private ProductsResp parseQueryProductResp(String queryResp) {
        XStream xStream = new XStream();
        xStream.alias("Response", ProductsResp.class);
        xStream.autodetectAnnotations(true);

        return (ProductsResp) xStream.fromXML(queryResp);
    }

    private ChargeResp charge(String token) {
        final String CHARGE_URL = "http://gd10086.4ggogo.com/web-in/boss/charge.html";
        String chargeReqStr = buildChargeReqStr();

        String chargeRespStr = doPost(CHARGE_URL, chargeReqStr, buildHeaders(token, chargeReqStr));
        return parseChargeResp(chargeRespStr);
    }

    private ChargeResp parseChargeResp(String chargeRespStr) {
        XStream xStream = new XStream();
        xStream.alias("Response", ChargeResp.class);
        xStream.autodetectAnnotations(true);

        return (ChargeResp) xStream.fromXML(chargeRespStr);
    }


    private String buildChargeReqStr() {
        ChargeReq chargeReq = new ChargeReq();
        ChargeReqData chargeReqData = new ChargeReqData();
        chargeReqData.setSerialNum(DigestUtils.md5Hex(UUID.randomUUID().toString()));
        chargeReqData.setMobile("18867105219");

        //流量池有传递size
//        chargeReqData.setFlowSize("105");
        chargeReqData.setProductCode("22a");

        chargeReq.setChargeReqData(chargeReqData);
        chargeReq.setRequestTime(new DateTime().toString());

        XStream xStream = new XStream();
        xStream.alias("Request", ChargeReq.class);
        xStream.autodetectAnnotations(true);

        return xStream.toXML(chargeReq);
    }

    private String buildAuthStr(AuthReq authReq) {
        XStream xStream = new XStream();
        xStream.alias("Request", AuthReq.class);
        xStream.autodetectAnnotations(true);

        return xStream.toXML(authReq);
    }

    private AuthReq buildShuanChuan() {
        String requestTime = new DateTime().toString();
        String sign = DigestUtils.sha256Hex(shuanchuan_appKey + requestTime + shuanchuan_appSecret);

        AuthReq authReq = new AuthReq();

        AuthReqData ard = new AuthReqData();
        ard.setAppKey(shuanchuan_appKey);
        ard.setSign(sign);
        ard.setSecInterface("SHUANGCHUANG");
        ard.setAppSecret(shuanchuan_appSecret);

        authReq.setAuthorization(ard);
        authReq.setRequestTime(requestTime);

        return authReq;
    }
}
