package com.cmcc.vrp.queue.queue.channel;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.channel.GdWheelChannelWorker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年4月6日 下午2:36:32
*/
@Component
public class ChannelGdWheelQueue extends AbstractQueue {
    @Value("#{settings['boss.channel.guangdongwheel.queue.name']}")
    private String queueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return GdWheelChannelWorker.class;
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
        return "guangdongwheel";
    }
}
