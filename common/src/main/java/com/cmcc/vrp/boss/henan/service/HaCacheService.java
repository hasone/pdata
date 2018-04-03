package com.cmcc.vrp.boss.henan.service;

/**
 * Created by leelyn on 2016/6/26.
 */
public interface HaCacheService {

    /**
     * @param accessToken
     * @param expiresIn
     */
    void saveAccessToken(String accessToken, String expiresIn);

    String getAccessToken();
}
