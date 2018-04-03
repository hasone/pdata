package com.cmcc.vrp.queue.consumer.provinces;

import com.google.gson.Gson;

import com.cmcc.vrp.province.cache.CacheService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.queue.QueueEndPoint;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.queue.pojo.ZwBossPojo;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.provinces.PackageWorker;
import com.cmcc.vrp.queue.utils.PorpertiesConfigurer;
import com.cmcc.vrp.queue.utils.WorkerUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.CustomQueueingConsumer;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by leelyn on 2016/11/15.
 */
public abstract class ProvinceConsumer extends QueueEndPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProvinceConsumer.class);
    private static final String DEFAULT_TIMEOUT = "200";
    private static final String FREFIX = "ZW_PACKAGE_";
    @Autowired
    PackageWorker packageWorker;
    @Autowired
    Gson gson;
    @Autowired
    CacheService cacheService;
    @Autowired
    TaskProducer producer;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    PorpertiesConfigurer propertyConfigurer;
    //工作者线程池
    private ExecutorService executorService;
    private List<WorkerRunner> runners;

    protected abstract String getPackageKey();

    protected abstract String getConsumeQueueName();

    final protected Worker getWorker(String value) {
        packageWorker.setTaskString(value);
        return packageWorker;
    }


    @PostConstruct
    public void start() {
        //初始化RMQ的连接参数
        initQueue(getConsumeQueueName());

        //初始化线程池
        initExecutorService();

        //重新发送上次服务退出时,从队列中已经取出但尚未发送出现的消息
        reSendPackage();
    }

    @PreDestroy
    public void stop() {
        //停止取消息
        for (WorkerRunner runner : runners) {
            runner.stop();
        }
        // 缓存已经取出但未发出的消息
        cachePackage(runners);

        //关闭线程池
        closeExecutorService();
    }


    protected int getWorkerCount() {
        String key = WorkerUtil.getPropertyKey(getConsumeQueueName());
        String workCount = (String) propertyConfigurer.getPropertiesValue(key);
        if (workCount != null) {
            return Integer.parseInt(workCount);
        }
        return 1;
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

    //初始化工作线程池
    private void initExecutorService() {
        int count = getWorkerCount();
        final String queueName = getConsumeQueueName();

        //初始化线程组
        runners = new ArrayList<WorkerRunner>(count);
        executorService = count == 1 ? Executors.newSingleThreadExecutor() : Executors.newFixedThreadPool(count);

        //线程池中的每个线程充当一个消费者的角色
        for (int i = 0; i < count; i++) {
            WorkerRunner workerRunner = new WorkerRunner(queueName, getTimeOut(), new LinkedList<String>());
            runners.add(workerRunner);
            executorService.submit(workerRunner);
        }
    }

    /**
     * 打包
     */
    private String getPackage(List<String> messages) {
        ZwBossPojo zwBossPojo = new ZwBossPojo();
        List<ChargeDeliverPojo> pojos = new ArrayList<ChargeDeliverPojo>();
        for (String msg : messages) {
            pojos.add(gson.fromJson(msg, ChargeDeliverPojo.class));
        }
        zwBossPojo.setPojos(pojos);
        return gson.toJson(zwBossPojo);
    }

    /**
     * 重新卓望BOSS队列中发送消息包
     */
    private void reSendPackage() {
        String packageStr;
        ZwBossPojo pojo;
        if (StringUtils.isNotBlank(packageStr = cacheService.get(FREFIX + getPackageKey()))
                && (pojo = gson.fromJson(packageStr, ZwBossPojo.class)) != null) {
            producer.produceZwPackage(pojo);
        }
    }

    /**
     * 服务退出时缓存尚未来得及发送的消息包
     */
    private void cachePackage(List<WorkerRunner> runners) {
        for (WorkerRunner runner : runners) {
            if ((runner.msgContainer).isEmpty()) {
                continue;
            }
            String packages = getPackage(runner.msgContainer);
            cacheService.add(FREFIX + getPackageKey(), packages);
        }
    }

    /**
     * 获取打包超时时间
     */
    private Integer getTimeOut() {
        String configTime;
        String timeOut = StringUtils.isBlank(configTime =
                globalConfigService.get(GlobalConfigKeyEnum.ZHUOWANG_PROVINCE_PACKAGE_TIME.getKey())) ? DEFAULT_TIMEOUT : configTime;
        return Integer.parseInt(timeOut);
    }

    private class WorkerRunner implements Runnable {

        private String queueName;

        private volatile boolean isRunning;

        private List<String> msgContainer;

        private int timeout; //in millimSeconds

        private WorkerRunner(String queueName, int timeout, List<String> msgContainer) {
            this.queueName = queueName;
            this.timeout = timeout;
            this.msgContainer = msgContainer;
            isRunning = true;
        }

        @Override
        public void run() {
            Channel channel = null;
            Long deliveryTag = null;
            try {
                channel = rmqChannelPool.borrowObject();

                CustomQueueingConsumer consumer = new CustomQueueingConsumer(channel);
                channel.basicConsume(queueName, false, consumer);

                while (isRunning) {
                    CustomQueueingConsumer.Delivery delivery = consumer.nextDelivery(timeout);
                    boolean flag = false;
                    try {
                        if (delivery == null) {
                            if (msgContainer.isEmpty()) {
                                continue;
                            } else {
                                getWorker(getPackage(msgContainer)).exec();
                                System.out.println("超时" + timeout + "ms" + "打包消息,消息个数为:" + msgContainer.size());
                                LOGGER.info("超时{}ms,打包消息,消息个数为:{}", timeout, msgContainer.size());
                                msgContainer.clear();
                                continue;
                            }
                        }
                        deliveryTag = delivery.getEnvelope().getDeliveryTag();
                        String message = new String(delivery.getBody());

                        msgContainer.add(message);
                        System.out.println("已准备的消息个数:" + msgContainer.size());
                        LOGGER.info("已准备的消息个数:{}", msgContainer.size());

                        if (msgContainer.size() == 50) {
                            getWorker(getPackage(msgContainer)).exec();
                            msgContainer.clear();
                            System.out.println("已打包消息50个");
                            LOGGER.info("已打包消息50个");
                        }
                        flag = true;
                    } catch (Exception e) {
                        LOGGER.error("消费队列[{}]时抛出异常，该消息进入死信队列[{}]. 异常信息为{}.", queueName, DEAD_LETTER_QUEUE, e.toString());
                        channel.basicReject(deliveryTag, false);
                    } finally {
                        if (flag) {
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

        public void stop() {
            isRunning = false;
        }
    }

}
