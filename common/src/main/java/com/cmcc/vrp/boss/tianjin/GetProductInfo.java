package com.cmcc.vrp.boss.tianjin;

import com.google.gson.Gson;

import com.cmcc.vrp.boss.tianjin.model.ChargeResponse;
import com.cmcc.vrp.boss.tianjin.model.Result;
import com.cmcc.vrp.boss.tianjin.model.SvcContent;
import com.cmcc.vrp.boss.tianjin.util.GlobalConfig;
import com.cmcc.vrp.boss.tianjin.util.Sign;
import com.cmcc.vrp.util.HttpUtils;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: </p> <p>Description: </p>
 *
 * @author lgk8023
 * @date 2017年1月17日 下午2:20:46
 */
@Component
public class GetProductInfo {
    private final static Logger LOGGER = LoggerFactory.getLogger(GetProductInfo.class);
    @Autowired
    GlobalConfig globalConfig;

    @Autowired
    Gson gson;

//    public static void main(String[] args) {
//        GetProductInfo tjBossServiceImpl = new GetProductInfo();
//        String result = tjBossServiceImpl.getUrlID("2206409888", "7178");
//        System.out.println(result);
//    }

    public String getUrlID(String groupId, String productId) {
        String requestTime = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String serial = "TJURLID" + requestTime;

        Map<String, String> sysParams = buildSysParam(serial, requestTime);
        String busiParams = buildBusiParam(groupId);
        String sign = Sign.generateSign(sysParams, busiParams, globalConfig.getPublicKey());
        String url = buildPara(serial, requestTime) + "&sign=" + sign;
        System.out.println(sysParams);
        System.out.println(busiParams);
        System.out.println(sign);
        System.out.println(url);
        LOGGER.info("天津boss获取分享网络用户公共参数:{}", sysParams);
        LOGGER.info("天津boss获取分享网络用户业务参数:{}", busiParams);
        LOGGER.info("天津boss获取分享网络用户url:{}", url);
        String response = HttpUtils.post(url, busiParams, "text/plain");
        LOGGER.info("天津boss获取分享网络用户返回:{}", response);
        ChargeResponse chargeResponse = null;
        String userId = null;
        Result result = null;
        List<SvcContent> svcContents = null;
        if (response != null
                && (chargeResponse = gson.fromJson(response, ChargeResponse.class)) != null
                && "0".equals(chargeResponse.getRespCode())
                && (result = chargeResponse.getResult()) != null
                && (svcContents = result.getSVC_CONTENT()) != null) {
            for (SvcContent svcContent : svcContents) {
                if (svcContent.getPRODUCT_ID().equals(productId)
                        && svcContent.getREMOVE_TAG().equals("0")) {
                    userId = svcContent.getUSER_ID();
                    break;
                }
            }
        }
        return userId;
    }

    private Map<String, String> buildSysParam(String bossReqNum, String requestTime) {

        Map<String, String> sysParam = new HashMap<String, String>();
        sysParam.put("method", "TJ_UNHQ_GetProductInfo");
        sysParam.put("timestamp", requestTime);
        sysParam.put("format", "json");
        sysParam.put("appId", globalConfig.getAppId());
        sysParam.put("version", "1.0");
        sysParam.put("status", globalConfig.getStatus());
        sysParam.put("flowId", bossReqNum);
        sysParam.put("appKey", globalConfig.getAppKey());
        return sysParam;
    }

    private String buildPara(String flowId, String requestTime) {

        StringBuffer urlSb = new StringBuffer();
        urlSb.append(globalConfig.getUrl());
        urlSb.append("?");

        urlSb.append("method=" + "TJ_UNHQ_GetProductInfo");
        urlSb.append("&");

        urlSb.append("timestamp=" + requestTime);
        urlSb.append("&");

        urlSb.append("format=" + "json");
        urlSb.append("&");

        urlSb.append("appId=" + globalConfig.getAppId());
        urlSb.append("&");

        urlSb.append("version=" + "1.0");
        urlSb.append("&");

        urlSb.append("status=" + globalConfig.getStatus());
        urlSb.append("&");

        urlSb.append("flowId=" + flowId);
        urlSb.append("&");

        urlSb.append("appKey=" + globalConfig.getAppKey());

        return urlSb.toString();
    }

    private String buildBusiParam(String groupId) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("PROVINCE_CODE", "TJIN");
        jsonObject.put("ROUTE_EPARCHY_CODE", "0022");
        jsonObject.put("TRADE_EPARCHY_CODE", "0022");
        jsonObject.put("EPARCHY_CODE", "0022");
        jsonObject.put("TRADE_CITY_CODE", "0022");
        jsonObject.put("TRADE_DEPART_ID", globalConfig.getTradeDepartId());
        jsonObject.put("TRADE_STAFF_ID", globalConfig.getTradeStaffId());
        jsonObject.put("TRADE_DEPART_PASSWD", globalConfig.getTradeDepartPasswd());
        jsonObject.put("IN_MODE_CODE", "Z");
        jsonObject.put("TRADEDEPARTPASSWD", globalConfig.getTradeDepartPasswd());
        jsonObject.put("DEPART_ID", globalConfig.getDepartId());
        jsonObject.put("CITY_CODE", "0022");
        jsonObject.put("LOGIN_EPARCHY_CODE", "0022");
        jsonObject.put("STAFF_EPARCHY_CODE", "0022");

        jsonObject.put("GROUP_ID", groupId);
        jsonObject.put("REMOVE_TAG", "0");
        jsonObject.put("EPARCHY_CODE", "0022");


        return jsonObject.toString();
    }
}
