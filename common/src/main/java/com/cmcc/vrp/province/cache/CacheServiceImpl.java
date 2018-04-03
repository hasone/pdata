package com.cmcc.vrp.province.cache;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by sunyiwei on 2016/7/18.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CacheServiceImpl implements CacheService {
    private static final int EXPIRE = 300;
    private final Logger LOGGER = LoggerFactory.getLogger(CacheServiceImpl.class);
    private final String REPLY_STR = "OK";
    //属性信息
    private String prefix;
    private int expire = EXPIRE;

    @Autowired
    private JedisPool jedisPool;
    
    @Override
    public boolean incrby(final String key, final long detal) {
        // TODO Auto-generated method stub
        return parseBoolean(abstractOperate(new CacheOperator<Boolean>() {
            @Override
            public Boolean operate(Jedis jedis) {
                String tmpKey = getKey(key);
                return jedis.incrBy(tmpKey, detal)>=0;
            }
        }));
    }

    @Override
    public boolean exists(final String key) {
        // TODO Auto-generated method stub
        return parseBoolean(abstractOperate(new CacheOperator<Boolean>() {
            @Override
            public Boolean operate(Jedis jedis) {
                String tmpKey = getKey(key);
                return jedis.exists(tmpKey);
            }
        }));
    }

    @Override
    public Boolean deleteValueOnList(final String key, final Integer count, final String value) {
        // TODO Auto-generated method stub
        return parseBoolean(abstractOperate(new CacheOperator<Boolean>() {
            @Override
            public Boolean operate(Jedis jedis) {
                String tmpKey = getKey(key);
                Long cnt = jedis.lrem(tmpKey, count, value);
                return cnt.intValue()==count?true:false;
            }
        }));
    }

    @Override
    public Long lPush(final String key, final String value, final int seconds) {
        // TODO Auto-generated method stub
        
        return abstractOperate(new CacheOperator<Long>(){
            @Override
            public Long operate(Jedis jedis) {
                String tmpKey = getKey(key);
                String newValue = value;
                Long value = jedis.lpush(tmpKey, newValue);
                jedis.expire(tmpKey, seconds);
                return value;
            }
        }); 
    }

    @Override
    public Long rPop(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long getLengthOfList(final String key) {
        // TODO Auto-generated method stub
        return abstractOperate(new CacheOperator<Long>(){
            @Override
            public Long operate(Jedis jedis) {
                LOGGER.info("获取list的长度。。。");
                String tmpKey = getKey(key);
                LOGGER.info("参数 key = {}, 构造后的key = {}", key, tmpKey);
                Long value = jedis.llen(tmpKey);
                LOGGER.info("value = {}", value);
                return value;
            }
        }); 
    }

    //增加缓存，该缓存必须是不存在
    @Override
    public boolean add(final String key, final String value) {
        return parseBoolean(abstractOperate(new CacheOperator<Boolean>() {
            @Override
            public Boolean operate(Jedis jedis) {
                String newkey = getKey(key);
                System.out.println("key:"+newkey);
                if (!jedis.setex(getKey(key), getExipre(), value).equalsIgnoreCase(REPLY_STR)) {
                    LOGGER.error("设置缓存值时出错，Key = {}，Value = {}.", getKey(key), value);
                    return false;
                }

                return true;
            }
        }));
    }

    @Override
    public boolean incrOrUpdate(final String key, final int seconds) {
        return parseBoolean(abstractOperate(new CacheOperator<Boolean>() {
            @Override
            public Boolean operate(Jedis jedis) {
                String tmpKey = getKey(key);
                if (StringUtils.isBlank(get(key))) {  //不存在的时候就自增且设置有效时间
                    jedis.watch(tmpKey);

                    //transaction
                    Transaction t = jedis.multi();
                    t.incr(tmpKey);
                    t.expire(tmpKey, seconds);
                    t.exec();
                } else { //存在的时候直接自增
                    jedis.incr(tmpKey);
                }

                return true;
            }
        }));
    }

    @Override
    public Long getIncrOrUpdate(final String key, final int seconds) {
        return abstractOperate(new CacheOperator<Long>() {
            @Override
            public Long operate(Jedis jedis) {
                String tmpKey = getKey(key);
                Long value = 0L;

                if (StringUtils.isBlank(tmpKey)) {  
                    return value;
                } 
                
                
                value = jedis.incr(tmpKey);
                if(value.equals(1L) && seconds>0){//取到1时，认为上一次的操作已超时，重置过期时间
                    jedis.expire(tmpKey, seconds);
                }

                return value;
            }
        });
    }
    
    @Override
    public Long getDecr(final String key) {
        return abstractOperate(new CacheOperator<Long>() {
            @Override
            public Long operate(Jedis jedis) {
                String tmpKey = getKey(key);
                Long value = null;

                if (StringUtils.isBlank(get(key))) {  //自增且设置有效时间,redis会保证incr原子操作,仅在没找到时更新expire，expire不用保证原子性
                    value = jedis.decr(tmpKey);
                }
                
                return value;
            }
        });
    }

    //批量增加缓存
    @Override
    public boolean add(final List<String> keys, final String value) {
        return parseBoolean(abstractOperate(new CacheOperator<Boolean>() {
            @Override
            public Boolean operate(Jedis jedis) {
                int expire = getExipre();

                //监控所有key
                for (String key : keys) {
                    jedis.watch(key);
                }

                Transaction transaction = jedis.multi();
                for (String key : keys) {
                    transaction.setex(getKey(key), expire, value);
                }
                transaction.exec();
                return true;
            }
        }));
    }

    //删除缓存
    @Override
    public boolean delete(final String key) {
        return parseBoolean(abstractOperate(new CacheOperator<Boolean>() {
            @Override
            public Boolean operate(Jedis jedis) {
                if (jedis.del(getKey(key)) != 1) {
                    LOGGER.error("删除缓存时出错, Key = {}.", getKey(key));
                    return false;
                }

                return true;
            }
        }));
    }

    @Override
    public boolean delete(final List<String> keys) {
        return parseBoolean(abstractOperate(new CacheOperator<Boolean>() {
            @Override
            public Boolean operate(Jedis jedis) {
                Transaction transaction = jedis.multi();
                for (String key : keys) {
                    transaction.del(getKey(key));
                }
                transaction.exec();
                return true;
            }
        }));
    }

    //更新缓存
    @Override
    public boolean update(final String key, final String newValue) {
        return parseBoolean(abstractOperate(new CacheOperator<Boolean>() {
            @Override
            public Boolean operate(Jedis jedis) {
                String modifyKey = getKey(key);

                if (!jedis.setex(modifyKey, getExipre(), newValue).equalsIgnoreCase(REPLY_STR)) {
                    LOGGER.error("缓存中不存在Key = {}的键值.", modifyKey);
                    return false;
                }

                return true;
            }
        }));
    }

    @Override
    public boolean update(final List<String> keys, final String newValue) {
        return parseBoolean(abstractOperate(new CacheOperator<Boolean>() {
            @Override
            public Boolean operate(Jedis jedis) {
                int expire = getExipre();

                Transaction transaction = jedis.multi();
                for (String key : keys) {
                    transaction.setex(getKey(key), expire, newValue);
                }
                transaction.exec();
                return true;
            }
        }));
    }

    //查询缓存
    @Override
    public String get(final String key) {
        return abstractOperate(new CacheOperator<String>() {
            @Override
            public String operate(Jedis jedis) {
                return jedis.get(getKey(key));
            }
        });
    }

    @Override
    public boolean sadd(final String key, final String member) {
        return parseBoolean(abstractOperate(new CacheOperator<Boolean>() {
            @Override
            public Boolean operate(Jedis jedis) {
                return jedis.sadd(getKey(key), member) == 1;
            }
        }));
    }

    @Override
    public Set<String> smembers(final String key) {
        return abstractOperate(new CacheOperator<Set<String>>() {
            /**
             * @param jedis
             * @return
             */
            @Override
            public Set<String> operate(Jedis jedis) {
                String tmp = getKey(key);
                return jedis.smembers(tmp);
            }
        });
    }

    @Override
    public boolean sadd(final String key, final List<String> members) {
        if (members == null || members.isEmpty()) {
            return false;
        }

        return parseBoolean(abstractOperate(new CacheOperator<Boolean>() {
            @Override
            public Boolean operate(Jedis jedis) {
                String tmp = getKey(key);
                Pipeline pipeline = jedis.pipelined();
                for (String member : members) {
                    pipeline.sadd(tmp, member);
                }
                pipeline.sync();
                return true;
            }
        }));
    }

    @Override
    public boolean srem(final String key, final String member) {
        return parseBoolean(abstractOperate(new CacheOperator<Boolean>() {
            @Override
            public Boolean operate(Jedis jedis) {
                return jedis.srem(getKey(key), member) == 1;
            }
        }));
    }

    @Override
    public boolean srem(final String key, final List<String> members) {
        if (members == null || members.isEmpty()) {
            return false;
        }

        return parseBoolean(abstractOperate(new CacheOperator<Boolean>() {
            @Override
            public Boolean operate(Jedis jedis) {
                return jedis.srem(getKey(key), listToArray(members)) == members.size();
            }
        }));
    }

    @Override
    public boolean sExist(final String key, final String member) {
        return parseBoolean(abstractOperate(new CacheOperator<Boolean>() {
            @Override
            public Boolean operate(Jedis jedis) {
                return jedis.sismember(getKey(key), member);
            }
        }));
    }
    
    @Override
    public boolean setNxAndExpireTime(final String key, final String value, final int seconds) {
        return parseBoolean(abstractOperate(new CacheOperator<Boolean>() {
            @Override
            public Boolean operate(Jedis jedis) {
                String cacheKey = getKey(key);
                
                Long result = jedis.setnx(cacheKey, value);
                if(result.equals(0L)){//为0时，表示之前已经设置过，返回失败
                    return false;
                }
                
                if(seconds > 0){
                    jedis.expire(cacheKey, seconds);
                }
                return true;
                
                
            }
        }));
    }

    private boolean parseBoolean(Boolean value) {
        return value == null ? false : value;
    }

    private String getKey(String key) {
        return prefix + key;
    }

    private <T> T abstractOperate(CacheOperator<T> operator) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            return operator.operate(jedis);
        } catch (Exception e) {
            LOGGER.error("无法从缓存连接池中获取连接对象, 错误信息为{}.", e.toString());
        } finally {
            if (jedis != null) {
                //jedisPool.returnResource(jedis);
                jedis.close();
            }
        }

        return null;
    }

    //随机获取过期时间
    private int getExipre() {
        Random random = new Random();

        return expire + random.nextInt(expire);
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    private String[] listToArray(List<String> list) {
        String[] arr = new String[list.size()];

        int i = 0;
        for (String s : list) {
            arr[i++] = s;
        }

        return arr;
    }

    @Override
    public Long incrby(final String key, final Integer value, final Integer initialValue) {
        return abstractOperate(new CacheOperator<Long>() {
            @Override
            public Long operate(Jedis jedis) {
                Long result = null;
                if (!StringUtils.isBlank(get(key))) {
                    result = jedis.incrBy(getKey(key), value);
                }else{
                    jedis.setnx(getKey(key), String.valueOf(initialValue));
                    result = Long.parseLong(get(key));
                }
                return result;
            }
        });
    }

    


}
