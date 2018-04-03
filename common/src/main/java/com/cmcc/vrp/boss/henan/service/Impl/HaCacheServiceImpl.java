package com.cmcc.vrp.boss.henan.service.Impl;

import com.cmcc.vrp.boss.RedisUtilService;
import com.cmcc.vrp.boss.henan.model.HaConstats;
import com.cmcc.vrp.boss.henan.service.HaCacheService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by leelyn on 2016/6/26.
 */
@Service
public class HaCacheServiceImpl implements HaCacheService {


    @Autowired
    private RedisUtilService redisService;

    /**
     * 保存accessToken
     *
     * @param accessToken
     * @param expiresIn
     */
    @Override
    public void saveAccessToken(String accessToken, String expiresIn) {
        if (StringUtils.isBlank(accessToken)
            || StringUtils.isBlank(expiresIn)) {
            return;
        }
        redisService.set(HaConstats.HENANAT_KET, accessToken, Integer.parseInt(expiresIn));
    }

    /**
     * 获取保存accessToken
     *
     * @return
     */
    @Override
    public String getAccessToken() {
        return redisService.get(HaConstats.HENANAT_KET);
    }
}
