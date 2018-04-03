package com.cmcc.vrp.queue.queue.busi;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.BatchPresentWorker;
import com.cmcc.vrp.queue.task.Worker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 批量赠送队列
 * <p>
 * Created by sunyiwei on 2016/12/13.
 */
@Component
public class BatchPresentQueue extends AbstractQueue {
    @Value("#{settings['batch.present.queue.name']}")
    private String queueName;

    @Value("#{settings['batch.present.count']}")
    private Integer count;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return BatchPresentWorker.class;
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
