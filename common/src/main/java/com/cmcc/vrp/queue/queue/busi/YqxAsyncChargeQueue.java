package com.cmcc.vrp.queue.queue.busi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.YqxAsyncChargeWorker;

/**
 * 异常充值队列
 * <p>
 * Created by sunyiwei on 2016/12/13.
 */
@Component
public class YqxAsyncChargeQueue extends AbstractQueue {
    @Value("#{settings['yqx.async.charge.queue.name']}")
    private String queueName;

    @Value("#{settings['yqx.async.charge.count']}")
    private Integer count;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return YqxAsyncChargeWorker.class;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    @Override
    public String getFingerPrint() {
        return null;
    }

    @Override
    public int getWorkerCount() {
        return count == null ? 1 : count;
    }
}
