package com.cmcc.vrp.boss.henan;

import com.cmcc.vrp.boss.henan.service.HaQueryBossService;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by leelyn on 2016/8/16.
 */
@Service
public class HaPlatformBossServiceImpl extends AbstractHaBossChargeService {

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    @Qualifier("HaPlatformQueryServiceImpl")
    private HaQueryBossService haQueryService;

    @Autowired
    private EnterprisesService enterprisesService;

    @Override
    protected Enterprise getEnterprise(Long entId) {
        return enterprisesService.selectByPrimaryKey(entId);
    }

    @Override
    protected HaQueryBossService getQueryService() {
        return haQueryService;
    }

    @Override
    protected String getAppkey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_PLATFORM_APPKEY.getKey());
    }

    @Override
    protected String getAppId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_PLATFORM_APPID.getKey());
    }

    public String getSMS_TEMPLATE() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_PLATFORM_SMS_TEMPLATE.getKey());
    }

    public String getVALID_TYPE() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_PLATFORM_VALID_TYPE.getKey());
    }

    public String getVALID_MONTH() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_PLATFORM_VALID_MONTH.getKey());
    }

    @Override
    public String getFingerPrint() {
        return "henanplatform123456789";
    }
}
