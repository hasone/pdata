package com.cmcc.vrp.queue.queue.channel;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.channel.CrowdfundingChannelWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by leelyn on 2017/1/9.
 */
@Component
public class ChannelCrowdfundingQueue extends AbstractQueue {

    @Value("#{settings['boss.channel.crowdfunding.queue.name']}")
    private String queueName;

    @Override
    public String getFingerPrint() {
        return "gd_crowdfunding";
    }

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return CrowdfundingChannelWorker.class;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }
}
