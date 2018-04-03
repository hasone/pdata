/**
 *
 */
package com.cmcc.vrp.queue.queue.channel;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.channel.JsChannelWorker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年4月5日 上午10:27:04
*/
@Component
public class ChannelJiangSuQueue extends AbstractQueue {
    @Value("#{settings['boss.channel.jiangsu.queue.name']}")
    private String queueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return JsChannelWorker.class;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    @Override
    public String getFingerPrint() {
        return "jiangsu";
    }
}
