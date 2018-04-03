/**
 *
 */
package com.cmcc.vrp.queue.queue.channel;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.channel.NmPlatformWorker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年2月7日 下午2:55:44
*/
@Component
public class ChannelNeiMengGuPlatformQueue extends AbstractQueue {
    @Value("#{settings['boss.platform.neimenggu.queue.name']}")
    private String queueName;

    @Override
    public String getFingerPrint() {
        return "neimenggu123456789";
    }

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return NmPlatformWorker.class;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }
}
