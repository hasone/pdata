/**
 *
 */
package com.cmcc.vrp.queue.queue.channel;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.channel.ShycChannelWorker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 上海月呈渠道队列
 *
 * @author sunyiwei
 */
@Component
public class ChannelShycQueue extends AbstractQueue {
    @Value("#{settings['boss.channel.shyc.queue.name']}")
    private String queueName;

    @Override
    public String getFingerPrint() {
        return "shanghaiyuecheng";
    }

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return ShycChannelWorker.class;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }
}
