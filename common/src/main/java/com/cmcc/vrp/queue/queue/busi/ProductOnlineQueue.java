package com.cmcc.vrp.queue.queue.busi;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.ProductOnlineWorker;
import com.cmcc.vrp.queue.task.Worker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 产品上线队列
 * <p>
 * Created by sunyiwei on 2016/12/13.
 */
@Component
public class ProductOnlineQueue extends AbstractQueue {
    @Value("#{settings['product.online.queue.name']}")
    private String queueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return ProductOnlineWorker.class;
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
        return 50;
    }
}
