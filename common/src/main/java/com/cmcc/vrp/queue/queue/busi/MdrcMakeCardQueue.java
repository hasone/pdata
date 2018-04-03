package com.cmcc.vrp.queue.queue.busi;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.MdrcMakeCardWorker;
import com.cmcc.vrp.queue.task.Worker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 营销卡生成卡数据队列
 * <p>
 * Created by qinqinyan on 2017/08/08.
 */
@Component
public class MdrcMakeCardQueue extends AbstractQueue {
    @Value("#{settings['mdrc.make.card.queue.name']}")
    private String queueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return MdrcMakeCardWorker.class;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    @Override
    public String getFingerPrint() {
        return null;
    }

}
