package com.cmcc.vrp.sms.cmppredis;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.sms.SendMessageService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.Provinces;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * 
 *
 */
public class CmppRedisSendMessageServiceImpl implements SendMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmppRedisSendMessageServiceImpl.class);

    @Value("#{settings['redis.cmpp.url']}")
    private String cmppRedisUrl; //cmpp使用的url

    @Value("#{settings['redis.cmpp.port']}")
    private int cmppRedisPort;

    @Value("#{settings['redis.cmpp.downstream.password']}")
    private String cmppPassword;

    @Value("#{settings['redis.cmpp.downstream.queueName']}")
    private String cmppQueueName;
    
    @Value("#{settings['redis.cmpp.downstream.queueName1']}")
    private String cmppQueueName1;   //广东异网

    @Value("#{settings['redis.cmpp.sign']}")
    private String sign;

    //redis连接池
    private JedisPool cmppPool;
    @Autowired
    private GlobalConfigService globalConfigService;
    @Autowired
    PhoneRegionService phoneRegionService;

    /**
     * 
     */
    @PostConstruct
    public void init() {
        LOGGER.info("开始初始化连接池");
        createJedisPool();
    }

    private boolean createJedisPool() {

        try {
            // 建立连接池配置参数
            JedisPoolConfig config = new JedisPoolConfig();

            // 设置最大连接数
            config.setMaxTotal(100);


            // 设置最大阻塞时间，记住是毫秒数milliseconds
            config.setMaxWaitMillis(1000);


            // 设置空间连接
            config.setMaxIdle(10);

            // 创建连接池
            if (StringUtils.isBlank(cmppPassword)) { //如果密码为空，则采用无密码登录的方式
                cmppPool = new JedisPool(config, cmppRedisUrl, cmppRedisPort);
            } else { //否则使用密码方式
                cmppPool = new JedisPool(config, cmppRedisUrl, cmppRedisPort, 5000, cmppPassword);
            }


            return true;
        } catch (JedisConnectionException e) {
            LOGGER.error("无法连接到Redis，url:" + cmppRedisUrl);
            return false;
        }

    }

    /**
     * 发送单条短信
     *
     * @param smsPojo 短信对象
     * @return 发送成功返回true， 否则false
     */
    @Override
    public boolean send(SmsPojo smsPojo) {
        if (cmppPool == null && !createJedisPool()) {
            return false;
        }

        Jedis client = null;
        try {
            client = cmppPool.getResource();

            // 执行指令
            String content = smsPojo.getContent();
            
            if (StringUtils.isNotBlank(sign)) {
                content += sign;
                smsPojo.setContent(content);
            }

            content = JSON.toJSONString(smsPojo);
            if ("gd_mdrc".equals(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()))) {
                String mobile = smsPojo.getMobile();
                if (!chinaMobile(mobile)) {
                    Long l = client.rpush(cmppQueueName1, content);
                    LOGGER.info("发送短信[{}]到{}结果为{}.", content, cmppQueueName1, l == 1 ? "成功" : "失败");
                    return true;
                }
            }

            Long l = client.rpush(cmppQueueName, content);
            LOGGER.info("发送短信[{}]到{}结果为{}.", content, cmppQueueName, l == 1 ? "成功" : "失败");
            return true;
        } catch (Exception e) {
            LOGGER.error("CmppQueueName = {}, RedisUrl = {}, ErrorMsg = {}.",
                    cmppQueueName, cmppRedisUrl, e.getMessage());

            e.printStackTrace();
        } finally {
            // 业务操作完成，将连接返回给连接池
            if (client != null) {
                client.close();
            }
        }

        return false;
    }

    private boolean chinaMobile(String mobile) {
        PhoneRegion phoneRegion = phoneRegionService.query(mobile);
        String supplier = (phoneRegion == null) ? Provinces.Unknown.getCode() : phoneRegion.getSupplier();
        if (!"M".equals(supplier)) {
            return false;
        }
        return true;
    }

}
