package com.cmcc.vrp.province.cache;

import java.util.List;
import java.util.Set;

/**
 * 缓存服务
 * <p>
 * Created by sunyiwei on 2016/7/18.
 */
public interface CacheService {
    /**
     * 单个增加缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    boolean add(String key, String value);

    /**
     * 有就自增，没有还是自增，但是要设置有效时间
     *
     * @param key 键
     * @param seconds 有效时间
     * @return
     */
    boolean incrOrUpdate(String key, int seconds);
    
    /**
     * 有就自增，没有还是自增，需要得到自增后的key值，但是要设置有效时间
     *
     * @param key 键
     * @param seconds 有效时间
     * @return
     */
    Long getIncrOrUpdate(String key, int seconds);

    /**
     * 批量增加缓存，事务操作
     *
     * @param keys  键
     * @param value 值
     * @return
     */
    boolean add(List<String> keys, String value);

    /**
     * 单个删除缓存
     *
     * @param key
     * @return
     */
    boolean delete(String key);

    /**
     * 批量删除缓存
     *
     * @param keys
     * @return
     */
    boolean delete(List<String> keys);

    /**
     * 更新单个缓存值
     *
     * @param key      单个键值
     * @param newValue 缓存值
     * @return
     */
    boolean update(String key, String newValue);

    /**
     * 批量更新缓存值
     *
     * @param keys     批量键值
     * @param newValue 缓存值
     * @return
     */
    boolean update(List<String> keys, String newValue);

    /**
     * 获取缓存值
     *
     * @param key 缓存的键值
     * @return
     */
    String get(String key);

    //增加成员到集合中
    /**
     * @param key
     * @param member
     * @return
     */
    boolean sadd(String key, String member);

    /**
     * 获取集合中的所有元素
     *
     * @param key
     * @return
     */
    Set<String> smembers(String key);

    //批量增加成员到集合中
    /**
     * @param key
     * @param members
     * @return
     */
    boolean sadd(String key, List<String> members);

    //删除成员
    /**
     * @param key
     * @param member
     * @return
     */
    boolean srem(String key, String member);

    //批量删除成员
    /**
     * @param key
     * @param members
     * @return
     */
    boolean srem(String key, List<String> members);

    //判断成员是否在集合中
    /**
     * @param key
     * @param member
     * @return
     */
    boolean sExist(String key, String member);

    /** 
     * 得到自减后的值，如果redis中没有key则返回空
     * @Title: getDecr 
     */
    Long getDecr(String key);
    
    /**
     * 使用redis的setNx命令设置，成功返回true，失败返回false
     * seconds为key的超时时间，<=0 时，为不设置超时时间，单位秒
     * 
     */
    boolean setNxAndExpireTime(String key,String value,int seconds);
    
    /**
     * 获取List长度
     * */
    Long getLengthOfList(String key);
    
    /**
     * 左插入
     * */
    Long lPush(String key, String value, int seconds);
    
    /**
     * 删除列表里面指定元素的值
     * */
    Boolean deleteValueOnList(String key, Integer count, String value);
    
    
    /**
     * 右出来
     * */
    Long rPop(String key);
    
    /**
     * 判断某个key是否存在
     * */
    boolean exists(String key);
    
    /**
     * 增加
     * */
    boolean incrby(String key, long delta);

    /** 
     * 增加value，如果value不存在，则设置成initialValue
     * @Title: incrby 
     */
    Long incrby(String key, Integer value, Integer initialValue);

}
