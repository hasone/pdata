package com.cmcc.vrp.queue.queue.busi;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.FlowcoinPresentWorker;
import com.cmcc.vrp.queue.task.Worker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 流量币赠送队列
 * <p>
 * Created by sunyiwei on 2016/12/13.
 */
@Component
public class FlowCoinPresentQueue extends AbstractQueue {
    @Value("#{settings['flowcoin.present.queue.name']}")
    private String queueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return FlowcoinPresentWorker.class;
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
