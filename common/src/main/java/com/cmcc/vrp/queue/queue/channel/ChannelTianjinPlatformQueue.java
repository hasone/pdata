/**
 *
 */
package com.cmcc.vrp.queue.queue.channel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.channel.TjPlatformWorker;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年2月7日 下午2:55:44
*/
@Component
public class ChannelTianjinPlatformQueue extends AbstractQueue {
    @Value("#{settings['boss.platform.tianjin.queue.name']}")
    private String queueName;

    @Override
    public String getFingerPrint() {
        return "tianjin123456";
    }

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return TjPlatformWorker.class;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }
}
