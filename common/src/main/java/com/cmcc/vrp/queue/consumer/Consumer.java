package com.cmcc.vrp.queue.consumer;

import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.queue.QueueEndPoint;
import com.cmcc.vrp.queue.WorkerInfo;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.utils.PorpertiesConfigurer;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.CustomQueueingConsumer;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 消息队列的消费者， 线程池实现 <p> Created by sunyiwei on 2016/4/8.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Consumer extends QueueEndPoint {
    protected static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    private static final String defaultTimeOut = "2000";

    private final WorkerInfo workerInfo;
    //容器对象
    @Autowired
    protected ApplicationContext applicationContext;

    @Autowired
    GlobalConfigService globalConfigService;

    @Autowired
    PorpertiesConfigurer propertyConfigurer;

    //工作者线程池
    private ExecutorService executorService;
    private List<WorkerRunner> runners;

    public Consumer(WorkerInfo workerInfo) {
        this.workerInfo = workerInfo;
    }

    /**
     * 启动监听
     */
    public void start() {
        //初始化RMQ的连接参数
        initMq();

        //初始化线程池
        initExecutorService();
    }

    /**
     * 暂停监听
     */
    public void stop() {
        for (WorkerRunner runner : runners) {
            runner.stop();
        }

        //关闭线程池
        closeExecutorService();
    }

    //初始化工作线程池
    private void initExecutorService() {
        int count = workerInfo.getWorkerCount();
        final String queueName = getConsumeQueueName();

        //初始化线程组
        runners = new ArrayList<WorkerRunner>(count);
        executorService = count == 1 ? Executors.newSingleThreadExecutor() : Executors.newFixedThreadPool(count);

        //线程池中的每个线程充当一个消费者的角色
        for (int i = 0; i < count; i++) {
            WorkerRunner workerRunner = new WorkerRunner(queueName, getTimeOut());
            runners.add(workerRunner);
            executorService.submit(workerRunner);
        }
    }

    //关闭线程池
    private void closeExecutorService() {
        //shutdown
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                executorService.awaitTermination(1000, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                LOGGER.error("线程池关闭过程中被打断! 错误信息为{}.", e.toString());
            }
        }
    }

    //初始化连接参数
    private void initMq() {
        //init queue
        initQueue(getConsumeQueueName());
    }

    /**
     * 根据队列中获取的内容返回相应的工作worker，这里要注意返回的对象必须是线程安全的，否则在多线程情况下会有问题
     *
     * @param value 队列中获取到的内容
     * @return 工作runner，用于在线程池中执行
     */
    private Worker getWorker(String value) {
        Class workerClass = workerInfo.getWorkerClass();
        if (workerClass == null) {
            return null;
        }

        //allow subclass to hook before getting a new worker
        workerInfo.beforeGetWorker();

        //get a new worker
        Worker worker = (Worker) applicationContext.getBean(workerClass);
        if (worker == null) {
            return null;
        }

        //allow subclass to hook after getting a new worker
        workerInfo.afterGetWorker(worker, value);

        return worker;
    }

    /**
     * 获取该消费者需要消费的缓存队列的名称
     *
     * @return 需要消费的缓存队列的名称
     */
    private String getConsumeQueueName() {
        return workerInfo.getQueueName();
    }

    /**
     * 业务消费者、渠道消费者消费超时时间
     */
    private Integer getTimeOut() {
        String configTime;
        String timeOut = StringUtils.isBlank(configTime =
                globalConfigService.get(GlobalConfigKeyEnum.ZHUOWANG_PROVINCE_PACKAGE_TIME.getKey()))
                ? defaultTimeOut : configTime;
        return Integer.parseInt(timeOut);
    }

    private class WorkerRunner implements Runnable {
        private String queueName;

        private volatile boolean isRunning;

        private int timeout; //in millimSeconds

        private WorkerRunner(String queueName, int timeout) {
            this.queueName = queueName;
            this.timeout = timeout;

            isRunning = true;
        }

        public void stop() {
            isRunning = false;
        }


        @Override
        public void run() {
            Channel channel = null;

            try {
                channel = rmqChannelPool.borrowObject();

                CustomQueueingConsumer consumer = new CustomQueueingConsumer(channel);
                channel.basicConsume(queueName, false, consumer);

                while (isRunning) {
                    CustomQueueingConsumer.Delivery delivery = consumer.nextDelivery(timeout);
                    if (delivery == null) {
                        continue;
                    }

                    long deliveryTag = delivery.getEnvelope().getDeliveryTag();
                    String message = new String(delivery.getBody());

                    if (!"boss.async.deliver.query.queue".equals(queueName)) {
                        LOGGER.info("从消息队列{}获取得消息{}[DeliveryTag={}]. 开始进行处理...", queueName, message, deliveryTag);
                    }

                    boolean flag = false;
                    try {
                        Worker worker = getWorker(message);
                        if (worker == null) {
                            LOGGER.info("无法获取相应的worker对象. QueueName = {}", queueName);
                        } else {
                            worker.exec();
                            flag = true;
                        }
                    } catch (Exception e) {
                        LOGGER.error("消费队列[{}]时抛出异常，该消息进入死信队列[{}]. 异常信息为{}.", queueName, DEAD_LETTER_QUEUE, e.toString());
                        channel.basicReject(deliveryTag, false);
                    } finally {
                        if (flag) {
                            LOGGER.debug("消息[DeliveryTag = {}]处理完毕, 返回确认信息", deliveryTag);
                            channel.basicAck(deliveryTag, false);
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("在{}类中消费队列[{}]时出错, 错误信息为{}.", getClass().getName(), queueName, e.getStackTrace());
            } finally {
                if (channel != null) {
                    rmqChannelPool.returnObject(channel);
                }
            }
        }
    }
}
