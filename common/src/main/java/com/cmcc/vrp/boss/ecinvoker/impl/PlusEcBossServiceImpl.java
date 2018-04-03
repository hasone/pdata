package com.cmcc.vrp.boss.ecinvoker.impl;

import com.cmcc.vrp.boss.ecinvoker.AbstractEcBossServiceImpl;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sunyiwei on 2016/7/28.
 */
@Service
public class PlusEcBossServiceImpl extends AbstractEcBossServiceImpl {
    @Autowired
    private GlobalConfigService globalConfigService;

    @Override
    protected String getAppKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_EC_PLUS_APP_KEY.getKey());
    }

    @Override
    protected String getAppSecret() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_EC_PLUS_APP_SECRET.getKey());
    }

    @Override
    protected String getChargeUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_EC_PLUS_CHARGE_URL.getKey());
    }

    @Override
    protected String getAuthUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_EC_PLUS_AUTH_URL.getKey());
    }

    @Override
    protected String getKeyPrefix() {
        return "plus";
    }

    @Override
    public String getFingerPrint() {
        return "plus";
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return true;
    }

}
