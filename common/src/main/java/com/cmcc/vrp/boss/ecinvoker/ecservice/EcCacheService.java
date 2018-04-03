package com.cmcc.vrp.boss.ecinvoker.ecservice;

/**
 * Created by leelyn on 2016/7/14.
 */
public interface EcCacheService {

    /**
     * 保存可访问的token信息
     * @param accessToken  token信息
     * @param expiresIn    过期
     * @param key          key值
     */
    void saveAccessToken(String accessToken, String expiresIn, String key);

    String getAccessToken(String key);
    
    /**
     * @param key
     * @return
     */
    boolean del(String key);
}
