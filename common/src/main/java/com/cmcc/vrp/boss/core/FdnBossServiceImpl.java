package com.cmcc.vrp.boss.core;

import com.cmcc.vrp.boss.core.model.OrderResp;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by leelyn on 2016/8/17.
 */
@Service
public class FdnBossServiceImpl extends AbstractCoreBossServiceImpl {
    @Override
    protected Map<String, String> buildHeaders(String requestBody, String appSecret) {
        return null;
    }

    @Override
    protected String getSerialNumPrefix() {
        return "FDN-";
    }

    @Override
    protected String getAppKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_FDN_APPKEY.getKey());
    }

    @Override
    protected String getOrderUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_FDN_ORDER_URL.getKey());
    }

    @Override
    protected String getAppSecret() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_FDN_APPSECRET.getKey());
    }

    @Override
    public String getFingerPrint() {
        return "wangsu123456789";
    }

    @Override
    protected boolean isSuccess(OrderResp orderResp) {
        return orderResp != null
            && orderResp.getStatus().equals(FdnChargeResponseStatusEnum.OK.getStatus());
    }
}
