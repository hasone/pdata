/**
 * @Title: SmsRedisListener.java
 * @Package com.cmcc.vrp.province.sms.login
 * @author: qihang
 * @date: 2015年6月10日 下午2:29:05
 * @version V1.0
 */
package com.cmcc.vrp.province.sms.login;

import java.util.Date;

import com.cmcc.vrp.util.StringUtils;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.enums.SmsType;
import com.cmcc.vrp.util.DateUtil;

/**
 * @ClassName: SmsRedisListener
 * @Description: 使用redis存储登陆相关对象
 * @author: qihang
 * @date: 2015年6月10日 下午2:29:05
 */
public class SmsRedisListener {
    public static final String msgQueue = "msgQueue";
    private static final Logger logger = Logger.getLogger(SmsRedisListener.class);
    private JedisPool pool;

    public SmsRedisListener(JedisPool pool) {
        this.pool = pool;
    }



    /**
     * 获取倒计时
     * @author qinqinyan
     * @date 2017/09/21
     * */
    public long getRemainTime(String mobile, SmsType smstype){
        Jedis jedis = pool.getResource();
        try {
            String redisKey = mobile + smstype.getSuffix();
            String value = jedis.get(redisKey);
            if(StringUtils.isEmpty(value)){
                logger.info("未从redis获取到短信验证码：rediskey = "+redisKey);
                return 0l;
            }else{
                LoginSmsPojo loginPojo = JSON.parseObject(value, LoginSmsPojo.class);

                //判断前一次发送短信间隔是否已超过1分钟
                Date after = DateUtil.getDateAfterMinutes(loginPojo.getLastTime(), 1);
                long currentTime = System.currentTimeMillis();
                if (after.getTime() > currentTime) {//没有超过
                    logger.info("requests within 1 minutes,rediskey=" + redisKey);
                    return after.getTime() - currentTime;
                } else {
                    return 0l;
                }
            }

        }catch(Exception e){
            logger.error("获取倒计时失败，失败原因："+e.getMessage());
            return 60000l;
        }
    }

    /**
     * @param key 是有oldMobile_change够成
     * @param mobile   手机号码
     * @param password 密码
     * @param smstype  短信类型 1为登陆 2为修改密码
     * @return
     * @throws
     * @Title:setNewPass
     * @Description: 在redis中设置用户的短信信息，key为 手机加后缀，如18800000000SmsLogin
     * @author: qinqinyan
     */
    public boolean setNewPassForGD(String mobile, String key, String password, SmsType smstype) {
        // 每次使用时 从连接池取
        boolean b = true;
        Jedis jedis = pool.getResource();
        try {

            String redisKey = key + smstype.getSuffix();

            long expireAt = (System.currentTimeMillis() / 1000) + 3600L;
            String value = jedis.get(redisKey);
            if (value == null) {//redis没有相关的值
                logger.info("can not get value from redis ,rediskey=" + redisKey);
                LoginSmsPojo loginPojo = new LoginSmsPojo(mobile, password);
                String json = JSON.toJSONString(loginPojo);
                jedis.set(redisKey, json);
                jedis.expireAt(redisKey, expireAt);
            } else {//redis有相关的值
                LoginSmsPojo loginPojo = JSON.parseObject(value, LoginSmsPojo.class);

                //判断前一次发送短信间隔是否已超过1分钟
                Date after = DateUtil.getDateAfterMinutes(loginPojo.getLastTime(), 1);
                if (after.getTime() > (new Date()).getTime()) {//没有超过
                    logger.info("requests within 1 minutes,rediskey=" + redisKey);
                    b = false;
                } else {
                    //将值从redis中取出，修改相关参数
                    loginPojo.setPassword(password);
                    loginPojo.setMobile(mobile);
                    loginPojo.setLastTime(new Date());
                    loginPojo.setTodayFrequency(loginPojo.getTodayFrequency() + 1);


                    //将改完的对象回填
                    String json = JSON.toJSONString(loginPojo);
                    jedis.set(redisKey, json);
                    jedis.expireAt(redisKey, expireAt);

                    logger.info("update " + redisKey + ": value=" + json);
                }
            }

        } catch (JedisConnectionException e) {
            logger.error(e);
            logger.error("连接断开 尝试重连...");
            return false;
        } catch (Exception e) {
            logger.error(e);
            return false;
        } finally {
            pool.returnResource(jedis);
        }
        return b;
    }

    /**
     * @param mobile   手机号码
     * @param password 密码
     * @param smstype  短信类型 1为登陆 2为修改密码
     * @return
     * @throws
     * @Title:setNewPass
     * @Description: 在redis中设置用户的短信信息，key为 手机加后缀，如18800000000SmsLogin
     * @author: qihang
     */
    public boolean setNewPass(String mobile, String password, SmsType smstype) {
        // 每次使用时 从连接池取
        boolean b = true;
        Jedis jedis = pool.getResource();
        try {

            String redisKey = mobile + smstype.getSuffix();
            long expireAt = (System.currentTimeMillis() / 1000) + 3600L;
            String value = jedis.get(redisKey);
            if (value == null) {//redis没有相关的值
                logger.info("can not get value from redis ,rediskey=" + redisKey);
                LoginSmsPojo loginPojo = new LoginSmsPojo(mobile, password);
                String json = JSON.toJSONString(loginPojo);
                jedis.set(redisKey, json);
                jedis.expireAt(redisKey, expireAt);
            } else {//redis有相关的值
                LoginSmsPojo loginPojo = JSON.parseObject(value, LoginSmsPojo.class);

                //判断前一次发送短信间隔是否已超过1分钟
                Date after = DateUtil.getDateAfterMinutes(loginPojo.getLastTime(), 1);
                if (after.getTime() > (new Date()).getTime()) {//没有超过
                    logger.info("requests within 1 minutes,rediskey=" + redisKey);
                    b = false;
                } else {
                    //将值从redis中取出，修改相关参数
                    loginPojo.setPassword(password);
                    loginPojo.setMobile(mobile);
                    loginPojo.setLastTime(new Date());
                    loginPojo.setTodayFrequency(loginPojo.getTodayFrequency() + 1);


                    //将改完的对象回填
                    String json = JSON.toJSONString(loginPojo);
                    jedis.set(redisKey, json);
                    jedis.expireAt(redisKey, expireAt);

                    logger.info("update " + redisKey + ": value=" + json);
                }
            }

        } catch (JedisConnectionException e) {
            logger.error(e);
            logger.error("连接断开 尝试重连...");
            return false;
        } catch (Exception e) {
            logger.error(e);
            return false;
        } finally {
            pool.returnResource(jedis);
        }
        return b;
    }

    /**
     * @param mobile   手机号码
     * @param smstype  短信类型 1为登陆 2为修改密码
     * @return
     * @throws
     * @Title:getLoginInfo
     * @Description: 在redis中得到用户的短信信息，key为 手机加后缀，如18800000000SmsLogin
     * @author: qihang
     */

    public LoginSmsPojo getLoginInfo(String mobile, SmsType smstype) throws JedisConnectionException {
        // 每次使用时 从连接池取
        Jedis jedis = pool.getResource();
        try {
            //从redis中得到手机密码相关信息
            String value = jedis.get(mobile + smstype.getSuffix());

            if (value == null) {//不存在返回空
                return null;
            } else {//存在返回相关对象
                LoginSmsPojo loginPojo = JSON.parseObject(value, LoginSmsPojo.class);
                return loginPojo;
            }
        } catch (JedisConnectionException e) {
            logger.error(e);
            return null;
        } catch (Exception e) {
            logger.error(e);
        } finally {
            pool.returnResource(jedis);
        }

        return null;
    }

    public long size() {
        // 每次使用时 从连接池取
        Jedis jedis = pool.getResource();
        try {
            return jedis.llen(msgQueue);
        } finally {
            pool.returnResource(jedis);
        }
    }

    /**
     * @param key
     * @return
     * @Title: getMsgFromRedis
     * @Description: 从redis获取指定key的value值
     * @return: String
     */
    public String getMsgFromRedis(String key) {
        // 每次使用时 从连接池取
        Jedis jedis = pool.getResource();
        String value = null;
        try {
            value = jedis.get(key);
        } catch (JedisConnectionException e) {
            logger.error(e);
            logger.error("连接断开 尝试重连...");
            return null;
        } catch (Exception e) {
            logger.error(e);
            return null;
        } finally {
            pool.returnResource(jedis);
        }
        return value;
    }

    /**
     * @param key
     * @param value
     * @return
     * @Title: putMsgToRedis
     * @Description: 向redis存放key-value
     * @return: boolean
     */
    public boolean putMsgToRedis(String key, String value) {
        // 每次使用时 从连接池取
        Jedis jedis = pool.getResource();
        try {
            jedis.set(key, value);
        } catch (JedisConnectionException e) {
            logger.error(e);
            logger.error("连接断开 尝试重连...");
            return false;
        } catch (Exception e) {
            logger.error(e);
            return false;
        } finally {
            pool.returnResource(jedis);
        }
        return true;
    }
    
    /** 
     * 在redis中增加验证码验证的次数
     * @Title: incrVerifyTimes 
     */
    public Long incrVerifyTimes(String mobile, SmsType smstype) {
        // 每次使用时 从连接池取
        Jedis jedis = pool.getResource();
        try {
            String key = mobile + smstype.getSuffix() + "Times";
            Long value = jedis.incr(key);
            jedis.expire(key, 3600);//设置一个小时的期限
            return value;
        } catch (JedisConnectionException e) {
            logger.error(e);
            logger.error("连接断开 尝试重连...");
            return null;
        } catch (Exception e) {
            logger.error(e);
            return null;
        } finally {
            jedis.close();
        }
    }

    /** 
     * 删除key值的缓存
     * @Title: deleteVerifyTimes 
     */
    public boolean delete(String key) {
        // 每次使用时 从连接池取
        Jedis jedis = pool.getResource();
        try {
            Long t = jedis.del(key);
            if ( t!= 1 && t!= 0) {
                logger.error("删除缓存时出错, Key = " + key);
                return false;
            }
        } catch (JedisConnectionException e) {
            logger.error(e);
            logger.error("连接断开 尝试重连...");
            return false;
        } catch (Exception e) {
            logger.error(e);
            return false;
        } finally {
            jedis.close();
        }

        return true;
    }

}
