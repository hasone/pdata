/**
 *
 */
package com.cmcc.vrp.queue.queue.channel;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.channel.SdChannelWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>Title:ChannelShandongQueue </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月21日
 */
@Component
public class ChannelShandongQueue extends AbstractQueue {
    @Value("#{settings['boss.channel.shandong.queue.name']}")
    private String queueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return SdChannelWorker.class;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    @Override
    public String getFingerPrint() {
        return "abandon_shangdong123456789";
    }
}
