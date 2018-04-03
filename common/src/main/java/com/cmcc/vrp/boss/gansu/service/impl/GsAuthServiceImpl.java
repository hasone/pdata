package com.cmcc.vrp.boss.gansu.service.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.boss.ecinvoker.ecservice.EcCacheService;
import com.cmcc.vrp.boss.gansu.model.GSDynamicTokenRequest;
import com.cmcc.vrp.boss.gansu.model.GSDynamicTokenResponse;
import com.cmcc.vrp.boss.gansu.service.GsAuthService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;

/**
 * 甘肃获取验证码定时任务 
 * 一个半小时执行一次
 * @author lgk8023
 *
 */
@Service
public class GsAuthServiceImpl implements GsAuthService{
    private final static Logger LOGGER = LoggerFactory.getLogger(GsAuthServiceImpl.class);
    @Autowired
    GlobalConfigService globalConfigService;

    @Autowired
    private EcCacheService cacheService;
    @Override
    public void auth() {
        if ("gansu".equals(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()))) {
            LOGGER.info("甘肃认证定时任务start " + cacheService.getAccessToken(getKey()));
            //获取动态验证码
            String appId = getAppId();
            String accessToken = getAccessToken();
            GSDynamicTokenRequest gsDynamicTokenRequest = new GSDynamicTokenRequest();
            gsDynamicTokenRequest.setAccessToken(accessToken);
            gsDynamicTokenRequest.setAppId(appId);

            String request = JSON.toJSONString(gsDynamicTokenRequest);
            LOGGER.info("认证请求发送参数为{}.", request);
            String response = HttpUtils.post(getDynamicTokenUrl(), request, "application/json");
            LOGGER.info("认证请求返回参数为{}.", response);
            if (StringUtils.isNotBlank(response)) {
                GSDynamicTokenResponse gsDynamicTokenResponse = JSON.parseObject(response, GSDynamicTokenResponse.class);
                cacheService.saveAccessToken(gsDynamicTokenResponse.getDynamicToken(), getExpiredTime(), getKey()); 
                LOGGER.info("甘肃认证定时任务end " + cacheService.getAccessToken(getKey())); 
            }
        }       
    }
    private String getKey() {
        return getKeyPrefix() + ".token";
    }

    protected String getKeyPrefix() {
        return "gansuaccesstoken";
    }
    public String getAppId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_APPID.getKey());
    }

    public String getAccessToken() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_ACCESS_TOKEN.getKey());
    }
    public String getDynamicTokenUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_DYNAMIC_TOKEN_URL.getKey());
    }
    public String getExpiredTime() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_EXPIRED_TIME.getKey());
    }
}
