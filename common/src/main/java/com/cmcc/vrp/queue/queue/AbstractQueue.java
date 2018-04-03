package com.cmcc.vrp.queue.queue;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.queue.ChannelObjectPool;
import com.cmcc.vrp.queue.WorkerInfo;
import com.cmcc.vrp.queue.consumer.Consumer;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.utils.PorpertiesConfigurer;
import com.cmcc.vrp.queue.utils.WorkerUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 队列的抽象, 每个队列包含以下内容：
 * <p>
 * 1. 队列中的对象类型
 * 2. 队列的消费者
 * 3. 绑定的channel对象
 * <p>
 * Created by sunyiwei on 2016/11/9.
 */
@Component
public abstract class AbstractQueue implements WorkerInfo {
    //绑定的channel对象，后续所有对队列的操作（入队、出队）都会通过这个队列进行
    @Autowired
    private ChannelObjectPool channelObjectPool;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PorpertiesConfigurer propertyConfigurer;

    private QueueStatus status = QueueStatus.CLOSE;

    /**
     * 启动队列， 包括消费者监听
     *
     * @return
     */
    public final boolean start() {
        if (status == QueueStatus.CLOSE) {
            Consumer consumer = getConsumer();
            if (consumer != null) {
                consumer.start();
                status = QueueStatus.OPEN;
                return true;
            }
        }

        return false;
    }

    /**
     * 关闭队列，停止监听
     */
    public final void close() {
        if (status == QueueStatus.OPEN) {
            Consumer consumer = getConsumer();
            if (consumer != null) {
                consumer.stop();
                status = QueueStatus.CLOSE;
            }
        }
    }

    /**
     * 向队列中发布消息
     *
     * @return 成功返回true， 否则false
     */
    public final boolean publish(ChargeDeliverPojo chargeDeliverPojo) {
        if (QueueStatus.CLOSE == status) {
            return false;
        }

        String queueName = getQueueName();
        if (StringUtils.isBlank(queueName)) {
            return false;
        }

        Channel channel = null;
        try {
            channel = channelObjectPool.borrowObject();
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, JSON.toJSONString(chargeDeliverPojo).getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                channelObjectPool.returnObject(channel);
            }
        }

        return false;
    }
    /**
     * @title: getConsumer
     * */
    protected Consumer getConsumer() {
        return applicationContext.getBean(Consumer.class, this);
    }

    /**
     * 获取指纹，注册中心会根据这个值进行分发
     *
     * @return
     */
    public abstract String getFingerPrint();
    /**
     * @title: getWorkerCount
     * */
    @Override
    public int getWorkerCount() {
        String key = WorkerUtil.getPropertyKey(getQueueName());
        String workCount = (String) propertyConfigurer.getPropertiesValue(key);
        if (workCount != null) {
            return Integer.parseInt(workCount);
        }
        return 1;
    }

    /**
     * @title: beforeGetWorker
     * */
    @Override
    public void beforeGetWorker() {

    }
    /**
     * @title: afterGetWorker
     * */
    @Override
    final public void afterGetWorker(Worker worker, String value) {
        worker.setTaskString(value);
    }

    /**
     * 创建队列时是否需要配置死信队列
     * @return
     */
    public boolean needDeadLetterConfig() {
        return true;
    }

    /**
     * 队列枚举类
     * */
    private enum QueueStatus {
        //队列开启，消费者开始监听，可以开始投递信息
        OPEN,

        //队列关闭，监听结束，无法投递信息
        CLOSE
    }
}
