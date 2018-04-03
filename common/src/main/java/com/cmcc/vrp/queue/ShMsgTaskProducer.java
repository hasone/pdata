package com.cmcc.vrp.queue;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.sms.shanghai.pojos.ShMsgPojo;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by leelyn on 2016/11/16.
 */
@Component
public class ShMsgTaskProducer extends ShMsgQueueEndPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShMsgTaskProducer.class);

    @Value("#{settings['rmq.queue.name']}")
    private String shMsgQueueName;

    @PostConstruct
    public void initMq() {
//        initQueue(shMsgQueueName);
    }

    /**
     * 上海短信生产
     *
     * @param pojo
     * @return
     */
    public boolean produceShMsg(ShMsgPojo pojo) {
        return produce(shMsgQueueName, pojo);
    }

    /***
     * 单消息
     *
     * @param queueName
     * @param obj
     * @returng
     */
    private boolean produce(final String queueName, final Object obj) {
        if (StringUtils.isBlank(queueName) || obj == null) {
            return false;
        }

        Channel channel = null;
        try {
            channel = shRmqChannelPool.borrowObject();
            if (channel == null) {
                LOGGER.error("创建channel出错，生产消息返回false.");
                return false;
            }

            String message = JSONObject.toJSONString(obj);
            channel.basicPublish("", queueName,
                    MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

            return true;
        } catch (IOException e) {

            LOGGER.error("生产消息{}到{}抛出异常，异常信息为{}.",
                    JSONObject.toJSONString(obj), queueName, e.getMessage());
        } catch (TimeoutException e) {
            LOGGER.error("生产消息{}到{}超时, 超时信息为{}.", JSONObject.toJSONString(obj),
                    queueName, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("无法从RMQ连接池中获取RMQ连接.异常信息:{}",e);
        } finally {
            if (channel != null) {
                shRmqChannelPool.returnObject(channel);
            }
        }

        return false;
    }

    /**
     * 批量消息
     *
     * @param queueName
     * @param objects
     * @return
     */
    private boolean batchProduce(String queueName, List objects) {
        if (StringUtils.isBlank(queueName) || objects.isEmpty()) {
            return false;
        }

        Channel channel = null;
        try {
            channel = shRmqChannelPool.borrowObject();
            if (channel == null) {
                LOGGER.error("创建channel出错，生产消息返回false.");
                return false;
            }

            for (Object object : objects) {
                String message = JSONObject.toJSONString(object);
                channel.basicPublish("", queueName,
                        MessageProperties.PERSISTENT_TEXT_PLAIN,
                        message.getBytes());
            }

            LOGGER.info("生产消息{}到{}成功.", JSONObject.toJSONString(objects),
                    queueName);
            return true;
        } catch (IOException e) {
            LOGGER.error("生产消息{}到{}抛出异常，异常信息为{}.",
                    JSONObject.toJSONString(objects), queueName, e.getMessage());
        } catch (TimeoutException e) {
            LOGGER.error("生产消息{}到{}超时, 超时信息为{}.",
                    JSONObject.toJSONString(objects), queueName, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("无法从RMQ连接池中获取RMQ连接.");
        } finally {
            if (channel != null) {
                shRmqChannelPool.returnObject(channel);
            }
        }

        return false;
    }
}
