package com.cmcc.vrp.queue.queue.busi;

import com.cmcc.vrp.queue.consumer.Consumer;
import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import org.springframework.stereotype.Component;

/**
 * 死信队列
 * <p>
 * Created by sunyiwei on 2016/12/13.
 */
@Component
public class DeadLetterQueue extends AbstractQueue {
    @Override
    public Class<? extends Worker> getWorkerClass() {
        return null;
    }

    @Override
    public String getQueueName() {
        return "x.dead.letter.queue";
    }

    @Override
    protected Consumer getConsumer() {
        return null;
    }

    @Override
    public String getFingerPrint() {
        return null;
    }

    @Override
    public int getWorkerCount() {
        return 10;
    }

    /**
     * 创建队列时是否需要配置死信队列
     *
     * @return
     */
    @Override
    public boolean needDeadLetterConfig() {
        return false;
    }
}
