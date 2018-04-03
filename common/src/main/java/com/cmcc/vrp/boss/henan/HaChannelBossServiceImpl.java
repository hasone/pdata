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
 * Created by leelyn on 2016/6/24.
 */
@Service
public class HaChannelBossServiceImpl extends AbstractHaBossChargeService {

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    @Qualifier("HaQueryServiceImpl")
    private HaQueryBossService haQueryService;

    @Autowired
    private EnterprisesService enterprisesService;

    @Override
    protected Enterprise getEnterprise(Long entId) {
        return enterprisesService.selectByPrimaryKey(Long.parseLong(getZYHYEntId()));
    }

    @Override
    public String getFingerPrint() {
        return "henan123456789";
    }

    @Override
    protected HaQueryBossService getQueryService() {
        return haQueryService;
    }

    @Override
    protected String getAppkey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_APPKEY.getKey());
    }

    @Override
    protected String getAppId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_APPID.getKey());
    }

    public String getSMS_TEMPLATE() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_SMS_TEMPLATE.getKey());
    }

    public String getVALID_TYPE() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_VALID_TYPE.getKey());
    }

    public String getVALID_MONTH() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_VALID_MONTH.getKey());
    }

    public String getZYHYEntId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_ZYHY_ENTID.getKey());
    }

}
