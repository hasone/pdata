package com.cmcc.vrp.queue.queue.busi;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.DeliverSmsWorker;
import com.cmcc.vrp.queue.task.Worker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 通知短信队列
 * <p>
 * Created by sunyiwei on 2016/12/13.
 */
@Component
public class DeliverNoticeSmsQueue extends AbstractQueue {
    @Value("#{settings['deliver.sms.notice.queue.name']}")
    private String deliverNoticeSmsQueueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return DeliverSmsWorker.class;
    }

    @Override
    public String getQueueName() {
        return deliverNoticeSmsQueueName;
    }

    @Override
    public String getFingerPrint() {
        return null;
    }

    @Override
    public int getWorkerCount() {
        return 30;
    }
}
