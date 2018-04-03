package com.cmcc.vrp.queue.queue.busi;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.FlowCardWorker;
import com.cmcc.vrp.queue.task.Worker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 流量卡队列
 * <p>
 * Created by sunyiwei on 2016/12/13.
 */
@Component
public class FlowCardQueue extends AbstractQueue {
    @Value("#{settings['flow.card.queue.name']}")
    private String queueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return FlowCardWorker.class;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    @Override
    public String getFingerPrint() {
        return null;
    }
}
