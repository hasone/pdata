package com.cmcc.vrp.queue.queue.busi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.IndividualActivityWorker;
import com.cmcc.vrp.queue.task.Worker;

/**
 * IndividualActivityQueue.java
 * @author wujiamin
 * @date 2017年1月13日
 */
@Component
public class IndividualActivityQueue extends AbstractQueue {
    @Value("#{settings['individual.activities.win.queue.name']}")
    private String queueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return IndividualActivityWorker.class;
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

