package com.cmcc.vrp.boss.tianjin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cmcc.vrp.boss.tianjin.model.ChargeResponse;
import com.cmcc.vrp.boss.tianjin.model.Result;
import com.cmcc.vrp.boss.tianjin.model.SvcContent;
import com.cmcc.vrp.boss.tianjin.util.GlobalConfig;
import com.cmcc.vrp.boss.tianjin.util.Sign;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.util.HttpUtils;

import com.google.gson.Gson;

import net.sf.json.JSONObject;

/**
* <p>Title: </p>
* <p>Description: 余额查询接口</p>
* @author lgk8023
* @date 2017年2月15日 下午5:22:39
*/
@Component
public class QueryOweFee {
    private final static Logger LOGGER = LoggerFactory.getLogger(QueryOweFee.class);
    @Autowired
    EnterprisesService enterprisesService;
    
    @Autowired
    GlobalConfig globalConfig;
    
    @Autowired
    Gson gson;
    
    @Autowired
    GetProductInfo getProductInfo;
    
//    public static void main(String[] args) {
//        QueryOweFee queryOweFee = new QueryOweFee();
//        queryOweFee.queryOweFee(null, "7178");
//        
//    }
    public String queryOweFee(Long entId, String productId) {
        Enterprise enterprise = null;
        if (entId == null
                || (enterprise = enterprisesService.selectByPrimaryKey(entId)) == null) {
            LOGGER.error("无效的余额查询参数entId={}", entId);
            return null;
        }
        String entCode = enterprise.getCode();
        //String entCode = "2206409888";
        String userId = getProductInfo.getUrlID(entCode, productId);
        if (userId == null) {
            LOGGER.error("获取userid失败");
            return null;
        }
        String requestTime = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String serial = "TJURLID" + requestTime;
        
        Map<String, String> sysParams = buildSysParam(serial, requestTime);
        String busiParams = buildBusiParam(userId);
        String sign = Sign.generateSign(sysParams, busiParams, globalConfig.getPublicKey());
        String url = buildPara(serial, requestTime) + "&sign=" + sign;
        System.out.println(sysParams);
        System.out.println(busiParams);
        System.out.println(sign);
        System.out.println(url);
        LOGGER.info("天津boss余额查询请求公共参数:{}", sysParams);
        LOGGER.info("天津boss余额查询请求业务参数:{}", busiParams);
        LOGGER.info("天津boss余额查询请求url:{}", url);
        String response = HttpUtils.post(url, busiParams, "text/plain");
        LOGGER.info("天津boss余额查询返回:{}", response);
        Result result = null;
        ChargeResponse chargeResponse = null;
        List<SvcContent> svcContents = null;
        if(response != null
                && (chargeResponse = gson.fromJson(response, ChargeResponse.class)) != null
                && "0".equals(chargeResponse.getRespCode())
                && (result = chargeResponse.getResult()) != null
                && (svcContents = result.getSVC_CONTENT()) != null) {
            return svcContents.get(0).getACCT_NEW_BALANCE();
        }
        return null;
    }
    private Map<String, String> buildSysParam(String bossReqNum, String requestTime) {
        
        Map<String, String> sysParam = new HashMap<String, String>();
        sysParam.put("method", "TJ_UNHQ_QueryOweFee");
        sysParam.put("timestamp", requestTime);
        sysParam.put("format", "json");
        sysParam.put("appId", globalConfig.getAppId());
        sysParam.put("version", "1.0");
        sysParam.put("status", globalConfig.getStatus());
        sysParam.put("flowId", bossReqNum);
        sysParam.put("appKey", globalConfig.getAppKey());
        return sysParam;
    }
    private String buildBusiParam(String userId) {
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
        
        jsonObject.put("USER_ID", userId);
        jsonObject.put("WRITEOFF_MODE", "1");
        jsonObject.put("PAY_MODE_CODE", "0");
        jsonObject.put("REMOVE_TAG", "0");
        jsonObject.put("TARGET_DATA", "0");
        jsonObject.put("EPARCHY_CODE", "0022");
        
        return jsonObject.toString();
    }
    
    private String buildPara(String flowId, String requestTime) {
        
        StringBuffer urlSb = new StringBuffer();
        urlSb.append(globalConfig.getUrl());
        urlSb.append("?");
        
        urlSb.append("method=" + "TJ_UNHQ_QueryOweFee");
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
}
