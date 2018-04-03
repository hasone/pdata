package com.cmcc.vrp.boss.ecinvoker.ecservice.impl;

import com.cmcc.vrp.boss.RedisUtilService;
import com.cmcc.vrp.boss.ecinvoker.ecservice.EcCacheService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by leelyn on 2016/7/14.
 */
@Service
public class EcCacheServiceImpl implements EcCacheService {

    @Autowired
    private RedisUtilService redisService;

    /**
     * 保存accessToken
     *
     * @param accessToken
     * @param expiresIn
     */
    @Override
    public void saveAccessToken(String accessToken, String expiresIn, String key) {
        if (StringUtils.isBlank(accessToken)
            || StringUtils.isBlank(expiresIn)
            || StringUtils.isBlank(key)) {
            return;
        }
        redisService.set(key, accessToken, Integer.parseInt(expiresIn));
    }

    /**
     * 获取保存accessToken
     *
     * @return
     */
    @Override
    public String getAccessToken(String key) {
        return redisService.get(key);
    }

    @Override
    public boolean del(String key) {
        return redisService.del(key);
    }
}
