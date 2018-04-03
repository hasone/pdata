package com.cmcc.vrp.queue.queue.busi;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.CallbackEcPlatformWorker;
import com.cmcc.vrp.queue.task.Worker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 回调EC队列
 * <p>
 * Created by sunyiwei on 2016/12/13.
 */
@Component
public class CallbackEcQueue extends AbstractQueue {
    @Value("#{settings['platform.callback.queue.name']}")
    private String platformCallbackQueueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return CallbackEcPlatformWorker.class;
    }

    @Override
    public String getQueueName() {
        return platformCallbackQueueName;
    }

    @Override
    public String getFingerPrint() {
        return null;
    }

    @Override
    public int getWorkerCount() {
        return 100;
    }
}
