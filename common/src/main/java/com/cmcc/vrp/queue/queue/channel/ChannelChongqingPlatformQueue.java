/**
 *
 */
package com.cmcc.vrp.queue.queue.channel;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.channel.CqPlatformWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>Title:ChannelChongqingPlatformQueue </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月21日
 */
@Component
public class ChannelChongqingPlatformQueue extends AbstractQueue {
    @Value("#{settings['boss.platform.chongqing.queue.name']}")
    private String queueName;

    @Override
    public String getFingerPrint() {
        return "chongqing";
    }

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return CqPlatformWorker.class;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }
}
