/**
 *
 */
package com.cmcc.vrp.queue.queue.channel;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.channel.SxChannelWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>Title:ChannelShanxiQueue </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月16日
 */
@Component
public class ChannelShanxiQueue extends AbstractQueue {
    @Value("#{settings['boss.channel.shanxi.queue.name']}")
    private String queueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return SxChannelWorker.class;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    @Override
    public String getFingerPrint() {
        return "shanxi123456789";
    }
}
