package com.cmcc.vrp.boss;

/**
 * Created by leelyn on 2016/6/26.
 */
public interface RedisUtilService {

    /**
     * @param key
     * @param value
     * @param minutes
     * @return
     */
    String set(String key, String value, Integer minutes);

    /**
     * @param key
     * @return
     */
    String get(String key);
    
    /**
     * @param key
     * @return
     */
    boolean del(String key);

}
