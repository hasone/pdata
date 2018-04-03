package com.cmcc.vrp.province.cache;

import redis.clients.jedis.Jedis;

/**
 * 缓存操作的抽象接口
 * Created by sunyiwei on 2016/7/18.
 */
public interface CacheOperator<T> {
    /**
     * @param jedis
     * @return
     */
    T operate(Jedis jedis);
}
