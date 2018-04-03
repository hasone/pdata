package com.cmcc.vrp.boss.liaoning.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年2月23日 上午8:50:03
*/
@Component
public class LnGlobalConfig {
    
    @Autowired
    GlobalConfigService globalConfigService;
    
    public String getAppId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_LN_APP_ID.getKey());
        //return "501800";
    }
    
    public String getAppKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_LN_APP_KEY.getKey());
        //return "09b149b082d320cb54cd7385b129371f";
    }
    
    public String getCustId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_LN_CUST_ID.getKey());
        //return "100016261826";
    }
    
    public String getEffectiveWay() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_LN_EFFECTIVE_WAY.getKey());
        //return "0";
    }
    
    public String getSendMsg() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_LN_SEND_MSG.getKey());
        //return "0";
    }
    
    public String getGiveMonth() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_LN_GIVE_MONTH.getKey());
        //return "1";
    }
    
    public String getOpenId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_LN_OPENID.getKey());
        //return "e2f94078-4c61-4d25-96ec-9a5aa82cb063";
    }
    
    public String getUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_LN_URL.getKey());
        //return "http://221.180.247.69:5291/oppf";
    }
    
    public String getAccessToken() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_LN_ACCESSTOKEN.getKey());
        //return "58d238d7-45ff-4f02-bc94-6cd93c5fff2c";
    }
    
    public String getOperId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_LN_OPERID.getKey());
        //return "100004638522";
    } 
}
