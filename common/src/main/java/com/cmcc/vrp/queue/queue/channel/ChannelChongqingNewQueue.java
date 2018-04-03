/**
 *
 */
package com.cmcc.vrp.queue.queue.channel;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.channel.CqNewChannelWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author lgk8023
 *
 */
@Component
public class ChannelChongqingNewQueue extends AbstractQueue {
    @Value("#{settings['boss.channel.chongqing.new.queue.name']}")
    private String queueName;

    @Override
    public String getFingerPrint() {
        return "chongqingnew";
    }

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return CqNewChannelWorker.class;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }
}
