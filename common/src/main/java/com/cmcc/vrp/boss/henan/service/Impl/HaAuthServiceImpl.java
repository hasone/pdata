package com.cmcc.vrp.boss.henan.service.Impl;

import com.cmcc.vrp.boss.henan.model.HaAuthReq;
import com.cmcc.vrp.boss.henan.model.HaAuthResp;
import com.cmcc.vrp.boss.henan.service.HaAuthService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by leelyn on 2016/6/24.
 */
@Service
public class HaAuthServiceImpl implements HaAuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HaAuthServiceImpl.class);
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    private Gson gson;

    @Override
    public HaAuthResp auth(HaAuthReq authReq) {
        if (authReq == null) {
            LOGGER.error("Henan auth para is null");
            return null;
        }
        String url = getDomain() + "?app_id=" + authReq.getAppId() + "&app_key=" + authReq.getAppKey() + "&grant_type=" + authReq.getGrantType();
        String result = HttpUtils.get(url);
        if (StringUtils.isBlank(result)) {
            LOGGER.error("Henan auth interface return null");
            return null;
        }
        return gson.fromJson(result, HaAuthResp.class);
    }

    public String getDomain() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_AUTH_DOMAIN.getKey());
    }
}
