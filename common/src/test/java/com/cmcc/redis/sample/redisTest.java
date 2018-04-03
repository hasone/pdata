/**
 * @Title: redisTest.java
 * @Package com.cmcc.redis.sample
 * @author: qihang
 * @date: 2015年8月11日 上午10:18:38
 * @version V1.0
 */
package com.cmcc.redis.sample;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.queue.pojo.PresentPojo;
import org.junit.Ignore;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import static org.springframework.test.util.AssertionErrors.assertEquals;

/**
 * @ClassName: redisTest
 * @Description: TODO
 * @author: qihang
 * @date: 2015年8月11日 上午10:18:38
 */
public class redisTest {
    private static JedisPool pool;
    private String sampleQueue = "sampleQueue";

    /**
     * 建立连接池 真实环境，一般把配置参数缺抽取出来。
     */
    private static void createJedisPool() {

        // 建立连接池配置参数
        JedisPoolConfig config = new JedisPoolConfig();

        // 设置最大连接数
        config.setMaxTotal(50);

        // 设置最大阻塞时间，记住是毫秒数milliseconds	        
        config.setMaxWaitMillis(1000);


        // 设置空间连接
        config.setMaxIdle(10);

        // 创建连接池
        pool = new JedisPool(config, "127.0.0.1", 6379);

    }

    @Ignore
    @Test
    public void testReconnect() throws Exception {
        createJedisPool();

        Jedis jedis = pool.getResource();
        jedis.close();

        jedis = pool.getResource();
        jedis.set("name", "sunyiwei");
        assertEquals("NAME", "sunyiwei", jedis.get("name"));
    }

    @Ignore
    @Test
    public void producer() {
        createJedisPool();
        Jedis jedis = pool.getResource();

        PresentPojo presentPojo = new PresentPojo();

        presentPojo.setRecordId(3L);
        presentPojo.setRuleId(3L);
        presentPojo.setMobile("18867102100");
        presentPojo.setEnterpriseId(3L);
        presentPojo.setProductId(3L);
        presentPojo.setRequestSerialNum(SerialNumGenerator.buildSerialNum());
        jedis.rpush("batch.present.queue", JSONObject.toJSONString(presentPojo));
    }

}
