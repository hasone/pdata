package com.cmcc.vrp.boss.henan.service.Impl;

import com.cmcc.vrp.boss.henan.service.AbstractHaBossService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by leelyn on 2016/8/17.
 */
@Service("HaQueryServiceImpl")
public class HaQueryServiceImpl extends AbstractHaBossService {

    @Autowired
    private GlobalConfigService globalConfigService;

    @Override
    protected String getAPPID() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_APPID.getKey());
    }

    @Override
    protected String getAPPKEY() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_APPKEY.getKey());
    }
}
