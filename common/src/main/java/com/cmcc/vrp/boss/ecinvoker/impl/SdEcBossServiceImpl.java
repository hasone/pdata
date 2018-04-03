package com.cmcc.vrp.boss.ecinvoker.impl;

import com.cmcc.vrp.boss.ecinvoker.AbstractEcBossServiceImpl;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by leelyn on 2016/8/16.
 */
@Service
public class SdEcBossServiceImpl extends AbstractEcBossServiceImpl {

    @Autowired
    GlobalConfigService globalConfigService;

    @Override
    protected String getAppKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SD_EC_APPKEY.getKey());
    }

    @Override
    protected String getAppSecret() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SD_EC_APPSECRET.getKey());
    }

    @Override
    protected String getChargeUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SD_EC_CHARGE_URL.getKey());
    }

    @Override
    protected String getAuthUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SD_EC_AUTH_URL.getKey());
    }

    @Override
    protected String getKeyPrefix() {
        return "shandong";
    }

    @Override
    public String getFingerPrint() {
        return "shangdong123456789";
    }
}
