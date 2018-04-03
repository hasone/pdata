package com.cmcc.vrp.queue.queue.busi;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.MdrcSmsChargeWorker;
import com.cmcc.vrp.queue.task.Worker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 营销卡短信充值队列
 * <p>
 * Created by sunyiwei on 2016/12/13.
 */
@Component
public class MdrcSmsChargeQueue extends AbstractQueue {
    @Value("#{settings['redis.cmpp.upStream.queueName']}")
    private String queueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return MdrcSmsChargeWorker.class;
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
