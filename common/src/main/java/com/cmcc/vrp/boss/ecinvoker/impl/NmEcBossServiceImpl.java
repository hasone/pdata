package com.cmcc.vrp.boss.ecinvoker.impl;

import com.cmcc.vrp.boss.ecinvoker.AbstractNmEcBossServiceImpl;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年2月7日 下午2:47:59
*/
@Service
public class NmEcBossServiceImpl extends AbstractNmEcBossServiceImpl {

    @Autowired
    GlobalConfigService globalConfigService;

    @Override
    protected String getAppKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_NM_EC_APPKEY.getKey());
    }

    @Override
    protected String getAppSecret() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_NM_EC_APPSECRET.getKey());
    }

    @Override
    protected String getChargeUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_NM_EC_CHARGE_URL.getKey());
    }

    @Override
    protected String getAuthUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_NM_EC_AUTH_URL.getKey());
    }

    @Override
    protected String getKeyPrefix() {
        return "neimenggu";
    }

    @Override
    public String getFingerPrint() {
        return "neimenggu123456789";
    }
}
