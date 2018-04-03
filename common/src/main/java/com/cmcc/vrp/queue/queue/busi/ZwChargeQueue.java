package com.cmcc.vrp.queue.queue.busi;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.ZwChargeWorker;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 卓望充值队列，真正向卓望侧请求的队列
 * <p>
 * Created by sunyiwei on 2016/12/13.
 */
@Component
public class ZwChargeQueue extends AbstractQueue {
    //限速器
    RateLimiter rateLimiter = RateLimiter.create(1.0);//Max 1 call per sec

    //消费队列名称
    @Value("#{settings['zw.charge.queue.name']}")
    private String zwChargeQueueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return ZwChargeWorker.class;
    }

    @Override
    public String getQueueName() {
        return zwChargeQueueName;
    }

    @Override
    public String getFingerPrint() {
        return null;
    }

    @Override
    final public int getWorkerCount() {
        return 1;
    }

    @Override
    public void beforeGetWorker() {
        rateLimiter.acquire();
    }
}
