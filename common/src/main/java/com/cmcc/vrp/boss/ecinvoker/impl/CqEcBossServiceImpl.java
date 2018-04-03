package com.cmcc.vrp.boss.ecinvoker.impl;

import com.cmcc.vrp.boss.ecinvoker.AbstractEcBossServiceImpl;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by leelyn on 2016/7/14.
 */
@Service
public class CqEcBossServiceImpl extends AbstractEcBossServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(CqEcBossServiceImpl.class);

    @Autowired
    private GlobalConfigService globalConfigService;

    @Override
    protected String getAppKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_EC_CQ_APP_KEY.getKey());
    }

    @Override
    protected String getAppSecret() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_EC_CQ_APP_SECRET.getKey());
    }

    @Override
    protected String getChargeUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_EC_CQ_CHARGE_URL.getKey());
    }

    @Override
    protected String getAuthUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_EC_CQ_AUTH_URL.getKey());
    }

    @Override
    protected String getKeyPrefix() {
        return "chongqingaccesstoken";
    }

    @Override
    public String getFingerPrint() {
        return "chongqing123456";
    }
}
