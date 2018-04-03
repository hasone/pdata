package com.cmcc.vrp.queue.queue.channel;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.channel.HlFeeChannelWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by leelyn on 2016/12/13.
 */
@Component
public class ChannelHljFeddQueue extends AbstractQueue {

    @Value("#{settings['boss.channel.heilongjiang.fee.queue.name']}")
    private String queueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return HlFeeChannelWorker.class;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    @Override
    public String getFingerPrint() {
        return "hlj_fee";
    }
}
