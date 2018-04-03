/**
 *
 */
package com.cmcc.vrp.queue.queue.channel;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.channel.HiChannelWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>Title:ChannelHainanQueue </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月16日
 */
@Component
public class ChannelHainanQueue extends AbstractQueue {
    @Value("#{settings['boss.channel.hainan.queue.name']}")
    private String queueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return HiChannelWorker.class;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    @Override
    public String getFingerPrint() {
        return "ebb22fbcb9928f128fa480c3d90f7458";
    }
}
