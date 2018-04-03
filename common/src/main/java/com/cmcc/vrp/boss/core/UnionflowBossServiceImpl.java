package com.cmcc.vrp.boss.core;

import com.cmcc.vrp.boss.core.model.OrderResp;
import com.cmcc.vrp.boss.core.model.ZwChargeResponseStatusEnum;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * unionflow渠道
 * <p>
 * Created by sunyiwei on 2016/10/11.
 */
@Service
public class UnionflowBossServiceImpl extends AbstractCoreBossServiceImpl {
    @Override
    protected Map<String, String> buildHeaders(String requestBody, String appSecret) {
        Map<String, String> headers = new LinkedHashMap<String, String>();

        headers.put("X-FLOW-Auth", DigestUtils.md5Hex(requestBody + appSecret));
        return headers;
    }

    @Override
    protected String getSerialNumPrefix() {
        return "UNFL-";
    }

    @Override
    protected String getAppKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_UNIONFLOW_APPKEY.getKey());
    }

    @Override
    protected String getOrderUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_UNIONFLOW_CHARGEURL.getKey());
    }

    @Override
    protected String getAppSecret() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_UNIONFLOW_APPSECRET.getKey());
    }

    @Override
    public String getFingerPrint() {
        return "unionflow";
    }

    @Override
    protected boolean isSuccess(OrderResp orderResp) {
        return orderResp != null
            && orderResp.getStatus().equals(ZwChargeResponseStatusEnum.SUCCESS.getCode());
    }
}
