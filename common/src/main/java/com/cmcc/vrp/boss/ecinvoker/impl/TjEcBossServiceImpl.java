package com.cmcc.vrp.boss.ecinvoker.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.ecinvoker.AbstractEcBossServiceImpl;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;


/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年4月21日 下午12:06:35
*/
@Service
public class TjEcBossServiceImpl extends AbstractEcBossServiceImpl {

    @Autowired
    private GlobalConfigService globalConfigService;

    @Override
    protected String getAppKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_EC_TJ_APP_KEY.getKey());
    }

    @Override
    protected String getAppSecret() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_EC_TJ_APP_SECRET.getKey());
    }

    @Override
    protected String getChargeUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_EC_TJ_CHARGE_URL.getKey());
    }

    @Override
    protected String getAuthUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_EC_TJ_AUTH_URL.getKey());
    }

    @Override
    protected String getKeyPrefix() {
        return "tianjin";
    }

    @Override
    public String getFingerPrint() {
        return "tianjin123456";
    }
}
