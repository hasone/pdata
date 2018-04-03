package com.cmcc.vrp.boss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

/**
 * Created by leelyn on 2016/6/26.
 */
@Service
public class RedisUtilServiceImpl implements RedisUtilService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtilServiceImpl.class);
    @Autowired
    private JedisPool jedisPool;

    /**
     * 存缓存
     *
     * @param key
     * @param value
     * @param minutes 单位：分钟
     * @return
     */
    @Override
    public String set(String key, String value, Integer minutes) {
        Jedis jedis = getJedis();
        if (jedis == null) {
            return null;
        }
        try {
            Transaction tx = jedis.multi();
            tx.set(key, value);
            if(minutes!=null){
                tx.expire(key, minutes * 60);
            }            
            tx.exec();
            return "success";
        } catch (Exception e) {
            LOGGER.error(e.toString());
        } finally {
            jedis.close();
            //jedisPool.returnResource(jedis);
        }
        return "failure";
        
    }

    /**
     * 取缓存
     *
     * @param key
     * @return
     */
    @Override
    public String get(String key) {
        Jedis jedis = getJedis();
        if (jedis == null) {
            return null;
        }
        String tmp = null;
        try {
            Transaction tx = jedis.multi();
            Response<String> response = tx.get(key);
            tx.exec();

            tmp = response.get();

        } catch (Exception e) {
            LOGGER.error(e.toString());
        } finally {
            jedis.close();
            //jedisPool.returnResource(jedis);
        }
        return tmp;
    }

    private Jedis getJedis() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
        return jedis;
    }
    @Override
    public boolean del(String key){  
        Jedis jedis = getJedis();  
        if (jedis == null) {
            return false;
        }
        try {  
            jedis.del(key);  
            return true;  
        } catch (Exception e) {  
            LOGGER.error(e.toString());
            return false;  
        }finally{  
            jedis.close();
            //jedisPool.returnResource(jedis);
        }  
    }  
}
