package com.cmcc.vrp.boss.henan;

import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by leelyn on 2016/9/27.
 */
@Service
public class HaChannelQueryServiceImpl extends HaChargeQueryServiceImpl {

    @Autowired
    private GlobalConfigService globalConfigService;


    @Override
    protected String getAppkey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_APPKEY.getKey());
    }

    @Override
    public String getFingerPrint() {
        return "henan123456789";
    }
}
