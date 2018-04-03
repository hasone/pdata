/**
 *
 */
package com.cmcc.vrp.queue.queue.channel;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.channel.JxNewChannelWorker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author lgk8023
 *
 */
@Component
public class ChannelJiangxiNewQueue extends AbstractQueue {
    @Value("#{settings['boss.channel.jiangxinew.queue.name']}")
    private String queueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return JxNewChannelWorker.class;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    @Override
    public String getFingerPrint() {
        return "jiangxinew";
    }
}
