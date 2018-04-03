package com.cmcc.vrp.boss.liaoning;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.asiainfo.openplatform.utils.AIESBConstants;
import com.cmcc.vrp.boss.liaoning.model.LnChargeResp;
import com.cmcc.vrp.boss.liaoning.util.LnGlobalConfig;
import com.cmcc.vrp.boss.liaoning.util.SignUtil;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.google.gson.Gson;

import net.sf.json.JSONObject;


/**
* <p>Title: LnQueryProd</p>
* <p>Description: 辽宁查询产品</p>
* @author lgk8023
* @date 2016年12月30日 下午3:05:30
*/
public class LnQueryProd{
    private static final Logger logger = LoggerFactory.getLogger(LnQueryProd.class);
    private static String TIME_FOMMAT = "yyyyMMddHHmmss";
    
    @Autowired
    LnGlobalConfig lnGlobalConfig;
    
    @Autowired
    Gson gson;
//    
//    public static void main(String[] args) {
//        LnQueryProd lnBossServiceImpl = new LnQueryProd();
//        System.out.println(lnBossServiceImpl.queryProd());
//    }
    
    /**
     * @param custId
     * @return
     */
    public String queryProd() {
        
        logger.info("辽宁产品查询开始!");
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
        String requestTime = sdf.format(new Date());
        String bossReqNum = SerialNumGenerator.buildNormalBossReqNum("ln", 10);
        Map<String, String> sysParams = buildSysParam(bossReqNum, requestTime);
        String busiParams = buildBusiParam(bossReqNum, requestTime).toString();
        LnChargeResp lnChargeResp = null;
        try {
            String response = SignUtil.execute(sysParams, busiParams, AIESBConstants.PROTOCOL.HTTP, lnGlobalConfig.getAppKey(), lnGlobalConfig.getUrl());
            lnChargeResp = gson.fromJson(response, LnChargeResp.class);
            logger.info("辽宁boss返回：" + response);
            logger.info("辽宁产品编码" + lnChargeResp.getResponse().getRetInfo().getProdList());
            return response;
                
        } catch (InterruptedException e) {
            logger.error(e.getMessage());

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
            
        return null;
    }
    
    
    /**
     * @param token
     * @param openid
     * @param busiSerial
     * @return
     */
    private Map<String, String> buildSysParam(String bossReqNum, String requestTime) {
        
        Map<String, String> sysParam = new HashMap<String, String>();
        sysParam.put("method", "OI_QueryProd");
        sysParam.put("format", "json");
        sysParam.put("timestamp", requestTime);
        sysParam.put("appId", lnGlobalConfig.getAppId());
        sysParam.put("version", "1.0");
        sysParam.put("operId", lnGlobalConfig.getOperId());
        sysParam.put("accessToken", lnGlobalConfig.getAccessToken());
        sysParam.put("openId", lnGlobalConfig.getOpenId());
        sysParam.put("busiSerial", bossReqNum);
        return sysParam;
    }
    
    /**
     * @param mobile
     * @param sPrdouct
     * @param bossReqNum
     * @return
     */
    private JSONObject buildBusiParam(String bossReqNum, String requestTime) {
        JSONObject busiParam = new JSONObject();    
        busiParam.put("Request", buildRequest());
        busiParam.put("PubInfo", buildPubInfo(bossReqNum, requestTime));
        
        return busiParam;
    }
    
    private JSONObject buildRequest() {
        JSONObject request = new JSONObject();
        request.put("BusiParams", buildBusiParams());
        request.put("BusiCode", "OI_QueryProd"); 
        return request;
    }
    
    private JSONObject buildBusiParams() {
        JSONObject busiParam = new JSONObject();
        busiParam.put("CustId", lnGlobalConfig.getCustId());
        busiParam.put("OfferId", "112001010634");
        return busiParam;
    }
    
    /**
     * @return
     */
    private JSONObject buildPubInfo(String bossReqNum, String requestTime) {
        JSONObject pubInfo = new JSONObject();
        pubInfo.put("TransactionTime", requestTime);
        pubInfo.put("OrgId", "40001000");
        pubInfo.put("ClientIP", "");
        pubInfo.put("RegionCode", "400");
        pubInfo.put("CountyCode", "4000");
        pubInfo.put("InterfaceType", "88");
        pubInfo.put("TransactionId", bossReqNum);
        pubInfo.put("OpId", "40051860");
        pubInfo.put("InterfaceId", "81");
        return pubInfo;
    }

}
