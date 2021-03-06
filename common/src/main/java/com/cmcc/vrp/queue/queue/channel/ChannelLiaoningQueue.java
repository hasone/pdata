/**
 *
 */
package com.cmcc.vrp.queue.queue.channel;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.channel.LnChannelWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月18日 上午11:02:23
*/
@Component
public class ChannelLiaoningQueue extends AbstractQueue {
    @Value("#{settings['boss.channel.liaoning.queue.name']}")
    private String queueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return LnChannelWorker.class;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    @Override
    public String getFingerPrint() {
        return "liaoning";
    }
}
