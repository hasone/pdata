/**
 *
 */
package com.cmcc.vrp.queue.queue.channel;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.channel.GdCardChannelWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>Title:ChannelGdCardQueue </p>
 * <p>Description: 广东流量卡渠道</p>
 *
 * @author xujue
 * @date 2016年11月16日
 */
@Component
public class ChannelGdCardQueue extends AbstractQueue {
    @Value("#{settings['boss.channel.gdcard.queue.name']}")
    private String queueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return GdCardChannelWorker.class;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    public String getFingerPrint() {
        return "guangdongcard";
    }
}
