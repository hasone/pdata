package com.cmcc.vrp.queue.queue.busi;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.AsynChargeQueryWorker;
import com.cmcc.vrp.queue.task.Worker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 异步的查询队列
 * Created by sunyiwei on 2016/12/13.
 */
@Component
public class AsyncChargeQueryQueue extends AbstractQueue {
    @Value("#{settings['boss.async.charge.query.queue.name']}")
    private String asyncChargeQueryQueueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return AsynChargeQueryWorker.class;
    }

    @Override
    public String getQueueName() {
        return asyncChargeQueryQueueName;
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
