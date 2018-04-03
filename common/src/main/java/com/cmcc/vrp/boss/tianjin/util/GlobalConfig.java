package com.cmcc.vrp.boss.tianjin.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月17日 下午3:34:38
*/
@Component
public class GlobalConfig {
    @Autowired
    GlobalConfigService globalConfigService;
    
    public String getUrl() {
        //return "http://211.103.90.123:80/oppf";
        return globalConfigService.get(GlobalConfigKeyEnum.TIANJIN_BOSS_URL.getKey());
    }
    
    public String getAppId() {
        //return "505386";
        return globalConfigService.get(GlobalConfigKeyEnum.TIANJIN_BOSS_APPID.getKey());
    }
  
    public String getStatus() {
        //return "1";
        return globalConfigService.get(GlobalConfigKeyEnum.TIANJIN_BOSS_STATUS.getKey());
    }
    
    public String getAppKey() {
        //return "d82ab7f18cf511977a42551e4f784ac4";
        return globalConfigService.get(GlobalConfigKeyEnum.TIANJIN_BOSS_APPKEY.getKey());
    }
    
    public String getPublicKey() {
        //return "MGcwDQYJKoZIhvcNAQEBBQADVgAwUwJMAMRdM2AsG5vcvr+miFJEscDecDBKGYlXDL2RWDe6hGVTXjdKM73gv+hv2Enj3N6vUEFzrrSI6Tv97k4nfuNMd8YXq70IF5Tg899rnwIDAQAB";
        return globalConfigService.get(GlobalConfigKeyEnum.TIANJIN_BOSS_PUBLICKEY.getKey());
    }
    
    public String getTradeDepartId() {
        //return "30824";
        return globalConfigService.get(GlobalConfigKeyEnum.TIANJIN_BOSS_TRADEDEPARTID.getKey());
    }
    
    public String getTradeStaffId(){
        //return "KVZY0001";
        return globalConfigService.get(GlobalConfigKeyEnum.TIANJIN_BOSS_TRADESTAFFID.getKey());
    } 
    
    public String getDepartId() {
        //return "30824";
        return globalConfigService.get(GlobalConfigKeyEnum.TIANJIN_BOSS_DEPARTID.getKey());
    } 
    
    public String getTradeDepartPasswd() {
        //return "lc1234";
        return globalConfigService.get(GlobalConfigKeyEnum.TIANJIN_BOSS_TRADEDEPARTPASSWD.getKey());
    }
}
