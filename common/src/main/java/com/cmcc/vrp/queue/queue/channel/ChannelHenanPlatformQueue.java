/**
 *
 */
package com.cmcc.vrp.queue.queue.channel;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.channel.HaPlatformWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>Title:ChannelHenanPlatformQueue </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月17日
 */
@Component
public class ChannelHenanPlatformQueue extends AbstractQueue {
    @Value("#{settings['boss.platform.henan.queue.name']}")
    private String queueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return HaPlatformWorker.class;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    @Override
    public String getFingerPrint() {
        return "henanplatform123456789";
    }

}
