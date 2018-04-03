/**
 *
 */
package com.cmcc.vrp.queue;

import com.cmcc.vrp.queue.task.MdrcSmsChargeWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>Title:QueueListener </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年8月1日
 */
@Component
public class QueueListener implements Runnable {

    public static final long MAX_QUEUE_SIZE = 100000l;
    public static final long DEFAULI_WAITING_TIME = 5000;
    public static final String THREAD_PREFIX = "QueueListener-";
    /**
     * 默认超时时间，2秒
     */
    public static final int DEFAULT_TIMEOUT = 2;
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueListener.class);
    @Autowired
    JedisPool pool;
    /**
     * 被监听队列名
     */

    private String queueName = "usSms";
    /**
     * 任务处理服务
     */
    @Autowired
    private MdrcSmsChargeWorker mdrcSmsChargeWorker;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    /**
     * 运行标志位
     */
    private volatile boolean running = false;
    /**
     * 等待时间，单位秒
     */
    private int timeout;

    /**
     * 重连时间  单位毫秒
     */
    private long waitingTime;

    /**
     * 线程名称
     */
    private String threadName;

    public QueueListener() {
        this.threadName = THREAD_PREFIX + queueName;
        setWaitingTime(DEFAULI_WAITING_TIME);
        setTimeout(DEFAULT_TIMEOUT);
    }

    public long size() {

        // 每次使用时 从连接池取
        Jedis jedis = pool.getResource();
        try {
            return jedis.llen(queueName);
        } finally {
            pool.returnResource(jedis);
        }
    }

    /**
     * 启动
     */
    @PostConstruct
    public void start() {
        try {
            // 尝试Redis连接是否可用
            size();
        } catch (JedisConnectionException e) { // 有可能连接失效
            LOGGER.error(e.toString());
            return;
        }
        running = true;
        Thread thread = new Thread(this, threadName);

        thread.start();
        LOGGER.info(threadName + " started.");
    }

    @PreDestroy
    public void stop() {
        if (running) {
            running = false;
            LOGGER.info(threadName + " stopped.");
        }
    }

    public boolean isRunning() {
        return running;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        if (timeout <= 0) {
            this.timeout = DEFAULT_TIMEOUT;
        } else {
            this.timeout = timeout;
        }
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    /**
     * 主循环
     */
    @Override
    public void run() {
        // 每次使用时 从连接池取
        Jedis jedis = pool.getResource();
        while (running) {
            try {
                // 以阻塞的方式从队列上取值 , timeout 为等待时间,超过timeout取不到值返回NULL
                List<String> values = jedis.blpop(timeout, queueName);
                if (values == null || values.size() <= 1) {
                    continue;
                }
                List<String> subList = values.subList(1, values.size());
                // 调用处理
                handle(subList);
            } catch (JedisConnectionException e) {
                LOGGER.error(e.toString());
                pool.returnBrokenResource(jedis);
            } catch (Exception e) {
                LOGGER.error(e.toString());
                pool.returnBrokenResource(jedis);
                // 当异常发生 停止监听
                //stop();
            }
        }
    }

    /**
     * 处理从队列上取到的值
     *
     * @param values
     */
    private void handle(List<String> values) {
        if (values == null || values.size() < 1) {
            return;
        }

        LOGGER.info("从队列里取值 size=" + values.size());
        for (String s : values) {
            // TOOD 调用服务 执行任务
            LOGGER.info("开始任务类型" + s);
            mdrcSmsChargeWorker.setTaskString(s);
            mdrcSmsChargeWorker.exec();
        }
    }

    public void setWaitingTime(long waitingTime) {
        this.waitingTime = waitingTime > 0 ? waitingTime : DEFAULI_WAITING_TIME;
    }

}
