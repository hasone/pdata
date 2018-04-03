/**
 *
 */
package com.cmcc.vrp.queue.queue.channel;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.channel.GdChannelWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>Title:ChannelGuangdongQueue </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月16日
 */
@Component
public class ChannelGuangdongQueue extends AbstractQueue {
    @Value("#{settings['boss.channel.guangdong.queue.name']}")
    private String queueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return GdChannelWorker.class;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    /**
     * @Title: getFingerprint
     * @Description: TODO
     */
    @Override
    public String getFingerPrint() {
        return "guangdong123456789";
    }
}
