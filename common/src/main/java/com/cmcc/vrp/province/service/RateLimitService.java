package com.cmcc.vrp.province.service;

/**
 * 频率限制服务
 * <p>
 * Created by sunyiwei on 2016/11/17.
 */
public interface RateLimitService {
    /**
     * 判断某个key值是否超过限制
     *
     * @param key 键
     * @return 允许返回true, 否则false
     */
    boolean allowToContinue(String key);

    /**
     * 增加一次key的操作记录
     *
     * @param key 键
     * @return
     */
    void add(String key);
}
