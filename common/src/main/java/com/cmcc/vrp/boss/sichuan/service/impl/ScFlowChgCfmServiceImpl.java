package com.cmcc.vrp.boss.sichuan.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.sichuan.model.Sign;
import com.cmcc.vrp.boss.sichuan.model.individual.ScFlowChgCfmRequest;
import com.cmcc.vrp.boss.sichuan.model.individual.ScFlowChgCfmResponse;
import com.cmcc.vrp.boss.sichuan.service.ScFlowChgCfmService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpConnection;

/**
 * ScFlowChgCfmServiceImpl.java
 * @author wujiamin
 * @date 2016年11月10日
 */
@Service("scFlowChgCfmService")
public class ScFlowChgCfmServiceImpl implements ScFlowChgCfmService{
    private static Logger logger = LoggerFactory.getLogger(ScFlowChgCfmServiceImpl.class);
    @Autowired
    GlobalConfigService globalConfigService;

    @Override
    public String generateRequestString(ScFlowChgCfmRequest request) {
        request.setAppKey(getAppKey());
        request.setLoginNo(getLoginNo());
        request.setUserName(getUserName());
        request.setTradeCode("INTER");
        request.setFlowType("01");

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
        request.setTimeStamp(dateformat.format(new Date()));

        String params = "appKey=" + request.getAppKey() 
            + "&flow_type=" + request.getFlowType()
            + "&inter_opbksn" + request.getInterOpbksn()
            + "&inter_optsn" + request.getInterOptsn()
            + "&login_no=" + request.getLoginNo()
            + "&op_code=" + request.getOpCode()
            + "&op_note=" + request.getOpNote()
            + "&op_type=" + request.getOpType()
            + "&phone_no=" + request.getPhoneNo()
            + "&timeStamp=" + request.getTimeStamp() 
            + "&trade_code=" + request.getTradeCode()
            
            + "&userName=" + request.getUserName()
            
            + "&use_channel=" + request.getUseChannel()
            + "&use_cost=" + request.getUseCost()
            + "&use_flow=" + request.getUseFlow()
            + "&user_pwd=" + request.getUserPwd();

        String sign = null;
        try {
            sign = Sign.sign(params, getPrivateKeyPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        params = params + "&sign=" + sign;
        return params;
    }

    @Override
    public ScFlowChgCfmResponse parseResponse(String responseStr) {
        ScFlowChgCfmResponse response = JSONObject.parseObject(responseStr, ScFlowChgCfmResponse.class);
        return response;
    }

    @Override
    public ScFlowChgCfmResponse sendRequest(ScFlowChgCfmRequest request) {
        String requestStr = generateRequestString(request);
        logger.info("流量币兑换请求参数:" + requestStr);
        try {
            String returnStr = HttpConnection.sendGetRequest(getAppQryUrl(), requestStr);
            logger.info("流量币兑换请求返回:" + returnStr + ", 请求参数:" + requestStr);
            return parseResponse(returnStr);

        } catch (Exception e) {
            logger.info("发送http请求错误:" + e.toString());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * @Title: getPrivateKeyPath 
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
     */
    public String getPrivateKeyPath() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_PRIVATE_KEY_PATH.getKey());
    }

    
    /** 
     * @Title: getAppKey 
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
    */
    public String getAppKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_APP_KEY.getKey());
    }

    /** 
     * @Title: getUserName 
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
    */
    public String getUserName() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_USER_NAME.getKey());
    }

    /** 
     * @Title: getLoginNo 
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
    */
    public String getLoginNo() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_LOGIN_NO.getKey());
    }

    /** 
     * @Title: getAppQryUrl 
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
    */
    public String getAppQryUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_APPQRY_URL.getKey());
    }

}
