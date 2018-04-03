package com.cmcc.vrp.queue;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.queue.enums.Provinces;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by leelyn on 2016/11/15.
 */
@Component
public class DistributeProducer extends QueueEndPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributeProducer.class);

    @PostConstruct
    public void initMq() {
        // init queues
        String[] queues = new String[]{Provinces.BEIJING.getQueueName(),
            Provinces.BEIJING.getQueueName(), Provinces.SHANGHAI.getQueueName(),
            Provinces.TIANJIN.getQueueName(), Provinces.CHONGQING.getQueueName(),
            Provinces.HEBEI.getQueueName(), Provinces.SHANXI1.getQueueName(),
            Provinces.NEIMENG.getQueueName(), Provinces.LIAONING.getQueueName(),
            Provinces.JINLIN.getQueueName(), Provinces.HEILONGJIANG.getQueueName(),
            Provinces.JIANGSU.getQueueName(), Provinces.ZHEJIANG.getQueueName(),
            Provinces.ANHUI.getQueueName(), Provinces.FUJIAN.getQueueName(),
            Provinces.JIANGXI.getQueueName(), Provinces.SHANDONG.getQueueName(),
            Provinces.HENAN.getQueueName(), Provinces.HUBEI.getQueueName(),
            Provinces.HUNAN.getQueueName(), Provinces.GUANGDONG.getQueueName(),
            Provinces.GUANGXI.getQueueName(), Provinces.HAINAN.getQueueName(),
            Provinces.SICHUAN.getQueueName(), Provinces.GUIZHOU.getQueueName(),
            Provinces.YUNNAN.getQueueName(), Provinces.XIZANG.getQueueName(),
            Provinces.SHANXI2.getQueueName(), Provinces.GANSU.getQueueName(),
            Provinces.QINGHAI.getQueueName(), Provinces.NINGXIA.getQueueName(),
            Provinces.XINJIANG.getQueueName()};

        initQueues(queues);
    }


    /**
     * 分发
     *
     * @param queueName
     * @param obj
     * @return
     */
    public boolean distibute(String queueName, Object obj) {
        return produce(queueName, obj);
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
            channel = rmqChannelPool.borrowObject();
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
            LOGGER.error("无法从RMQ连接池中获取RMQ连接.");
        } finally {
            if (channel != null) {
                rmqChannelPool.returnObject(channel);
            }
        }

        return false;
    }
}
