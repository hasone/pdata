package com.cmcc.redis.sample;

import org.junit.Ignore;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 * Redis操作示例
 * <p>
 *
 * @author qiaohao
 */
@Ignore
public class RedisSample {

    private static JedisPool pool;

    /**
     * 建立连接池 真实环境，一般把配置参数缺抽取出来。
     */
    private static void createJedisPool() {

        // 建立连接池配置参数
        JedisPoolConfig config = new JedisPoolConfig();

        // 设置最大连接数

        // 设置最大阻塞时间，记住是毫秒数milliseconds
        config.setMaxWaitMillis(1000);

        // 设置空间连接
        config.setMaxIdle(10);

        // 创建连接池
        pool = new JedisPool(config, "127.0.0.1", 6379);

    }

    @Ignore
    @Test
    public void setAndGet() {
        createJedisPool();
        Jedis jedis = pool.getResource();
        String value = jedis.get("test_keyxxx");
        System.out.println(value);
    }


}
