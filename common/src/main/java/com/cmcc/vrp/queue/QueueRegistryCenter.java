/**
 *
 */
package com.cmcc.vrp.queue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

/**
 * <p>Title:QueueRegistryCenter </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月14日
 */
@Component
public class QueueRegistryCenter {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueRegistryCenter.class);
    protected final String DEAD_LETTER_QUEUE = "x.dead.letter.queue";
    protected final String DEAD_LETTER_EXCHANGE = "x.dead.letter.exchange";
    @Autowired
    ConnectionFactory connectionFactory;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    ChannelObjectPool cop;
    private List<AbstractQueue> queues = new LinkedList<AbstractQueue>();

    private ChannelObjectPool channelObjectPool;

    public QueueRegistryCenter(ChannelObjectPool channelObjectPool) {
        this.channelObjectPool = channelObjectPool;
    }

    /**
     * 初始化注册中心
     */
    @PostConstruct
    public void init() {
//	this.registry(applicationContext.getBean(DeliverByBossQueue.class));
//	QueueRegistryCenter qrc = new QueueRegistryCenter(cop);
//	AbstractQueue abstractQueue = new DeliverByBossQueue(cop, qrc);
        Map<String, AbstractQueue> map = applicationContext.getBeansOfType(AbstractQueue.class);

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            this.registry((AbstractQueue) entry.getValue());
        }
//	
//	for(AbstractQueue aq: map.values()){
//	    this.registry(aq);
//	}
    }

    //注册消息队列
    public boolean registry(AbstractQueue abstractQueue) {
        if (abstractQueue == null //队列不能为空
            || StringUtils.isBlank(abstractQueue.getQueueName()) //队列名为空的不用注册
            || getByQueueName(abstractQueue.getQueueName()) != null) {//不允许重复注册队列
            return false;
        }

        queues.add(abstractQueue);

        //create queue if absent
        createQueueIfAbsent(abstractQueue);
        return abstractQueue.start();
    }

    //移除注册
    public boolean deregistry(AbstractQueue abstractQueue) {
        if (queues.contains(abstractQueue)) {
            abstractQueue.close();
            return queues.remove(abstractQueue);
        }

        return false;
    }

    public AbstractQueue getByQueueName(String queueName) {
        if (StringUtils.isBlank(queueName)) {
            return null;
        }

        for (AbstractQueue queue : queues) {
            if (queueName.equals(queue.getQueueName())) {
                return queue;
            }
        }

        return null;
    }

    public AbstractQueue getByFingerprint(String fingerprint) {
        for (AbstractQueue queue : queues) {
            if (queue.getFingerPrint() != null && queue.getFingerPrint().equals(fingerprint)) {
                return queue;
            }
        }

        return null;
    }

    private void deregistryAll() {
        Iterator<AbstractQueue> iter = queues.iterator();
        while (iter.hasNext()) {
            deregistry(iter.next());
        }
    }

    /**
     * 析构注册中心
     */
    @PreDestroy
    public void close() {
        deregistryAll();
        channelObjectPool.close();
    }

    private void createQueueIfAbsent(AbstractQueue abstractQueue) {
        Channel channel = null;
        try {
            String queueName = abstractQueue.getQueueName();
            channel = channelObjectPool.borrowObject();

            //获取死信队列的配置
            if (abstractQueue.needDeadLetterConfig()) {
                Map<String, Object> args = buildDeadLetterConfig(channel, queueName);
                channel.queueDeclare(queueName, true, false, false, args);
            } else {
                channel.queueDeclare(queueName, true, false, false, null);
            }
        } catch (Exception e) {
            LOGGER.error("创建队列时抛出异常，异常信息为{}, 异常堆栈为{}, 队列名称为{}.",
                e.getMessage(), e.getStackTrace(), abstractQueue.getQueueName());
        } finally {
            if (channel != null) {
                channelObjectPool.returnObject(channel);
            }
        }
    }

    //声明死信队列，用于存储无法处理的信息
    private Map<String, Object> buildDeadLetterConfig(Channel channel, String queueName) {
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
}
