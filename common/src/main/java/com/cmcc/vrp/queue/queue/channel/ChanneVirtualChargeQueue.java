package com.cmcc.vrp.queue.queue.channel;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.channel.VirtualChargeChannelWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by qinqinyan on 2017/5/9.
 * 虚拟充值队列
 */
@Component
public class ChanneVirtualChargeQueue extends AbstractQueue {
    @Value("#{settings['boss.channel.virtualcharge.queue.name']}")
    private String queueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return VirtualChargeChannelWorker.class;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    @Override
    public String getFingerPrint() {
        return "virtualcharge";
    }
}
