/**
 *
 */
package com.cmcc.vrp.queue.queue.channel;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.channel.JsofChannelWorker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>Title:ChannelBeijingQueue </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月16日
 */
@Component
public class ChannelJsofQueue extends AbstractQueue {
    @Value("#{settings['boss.channel.jsof.queue.name']}")
    private String queueName;

    @Override
    public String getFingerPrint() {
        return "jiangsuoufei";
    }

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return JsofChannelWorker.class;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }
}
