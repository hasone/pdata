package com.cmcc.vrp.queue.queue.busi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.AsynDeliverQueryWorker;
import com.cmcc.vrp.queue.task.Worker;

/**
 * 异步的查询队列
 * Created by sunyiwei on 2016/12/13.
 */
@Component
public class AsynDeliverQueryQueue extends AbstractQueue {
    @Value("#{settings['boss.async.deliver.query.queue.name']}")
    private String asynDeliverQueryQueueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return AsynDeliverQueryWorker.class;
    }

    @Override
    public String getQueueName() {
        return asynDeliverQueryQueueName;
    }

    @Override
    public String getFingerPrint() {
        return null;
    }

    @Override
    public int getWorkerCount() {
        return 100;
    }
}
