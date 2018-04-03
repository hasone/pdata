package com.cmcc.vrp.queue;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leelyn on 2016/11/16.
 */
public abstract class AbstractQueueEndPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractQueueEndPoint.class);

    protected final String DEAD_LETTER_QUEUE = "x.dead.letter.queue";
    protected final String DEAD_LETTER_EXCHANGE = "x.dead.letter.exchange";

    protected abstract ChannelObjectPool getChannelPool();

    //声明死信队列，用于存储无法处理的信息
    protected Map<String, Object> buildDeadLetterConfig(Channel channel, String queueName) {
        try {
            channel.exchangeDeclare("x.dead.letter.exchange", "direct", true);
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
            args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUE);

            channel.queueDeclare(DEAD_LETTER_QUEUE, true, false, false, null);
            channel.queueBind(DEAD_LETTER_QUEUE, DEAD_LETTER_EXCHANGE, DEAD_LETTER_QUEUE);

            return args;
        } catch (IOException e) {
            LOGGER.error("队列[{}]的死信队列未成功配置，所有该队列无法处理的消息将被直接丢弃！", queueName);
            return null;
        }
    }

    /**
     * 创建channel.
     *
     * @param queueName 创建的channel需要消费的队列名称
     * @return
     */
    protected boolean declareQueue(String queueName) {
        Channel channel = null;
        ChannelObjectPool pool = null;
        try {

            pool = getChannelPool();
            channel = pool.borrowObject();

            //获取死信队列的配置
            Map<String, Object> args = buildDeadLetterConfig(channel, queueName);
            channel.queueDeclare(queueName, true, false, false, args);

            return true;
        } catch (Exception e) {
            LOGGER.error("声明queue时出错, queueName = {}，错误信息为{}. ", queueName, e);
            return false;
        } finally {
            if (channel != null) {
                pool.returnObject(channel);
            }
        }
    }

    protected void initQueues(String[] queues) {
        for (String queue : queues) {
            if (!declareQueue(queue)) {
                LOGGER.error("声明Queue[Name = {}]时出错.", queue);
            }
        }
    }

    protected void initQueue(String queue) {
        if (!declareQueue(queue)) {
            LOGGER.error("声明Queue[Name = {}]时出错.", queue);
        }
    }
}
