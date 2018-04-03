/**
 *
 */
package com.cmcc.vrp.queue.queue.channel;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.channel.BjymChannelWorker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 北京云漫渠道队列
 *
 * @author sunyiwei
 */
@Component
public class ChannelBjymQueue extends AbstractQueue {
    @Value("#{settings['boss.channel.bjym.queue.name']}")
    private String queueName;

    @Override
    public String getFingerPrint() {
        return "beijingyunman";
    }

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return BjymChannelWorker.class;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }
}
