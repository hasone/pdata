package com.cmcc.vrp.queue.queue.busi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.WxScoreExchangeWorker;

/**
 * 微信个人积分兑换充值队列
 * WxScoreExchangeQueue.java
 * @author wujiamin
 * @date 2017年3月16日
 */
@Component
public class WxScoreExchangeQueue extends AbstractQueue {
    @Value("#{settings['wx.score.exchange.queue.name']}")
    private String queueName;

    @Override
    public Class<? extends Worker> getWorkerClass() {
        return WxScoreExchangeWorker.class;
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
