package com.cmcc.vrp.queue.queue.busi;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.DeliverByBossWorker;
import com.cmcc.vrp.queue.task.Worker;
import org.springframework.stereotype.Component;

/**
 * 按渠道分发到下级队列
 * <p>
 * Created by sunyiwei on 2016/11/10.
 */
@Component
public class DeliverByBossQueue extends AbstractQueue {
    /**
     * 获取指纹，注册中心会根据这个值进行分发
     *
     * @return
     */
    @Override
    public String getFingerPrint() {
        return "bossDeliver";
    }

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return DeliverByBossWorker.class;
    }

    @Override
    public String getQueueName() {
        return "deliver.boss.queue";
    }
}
