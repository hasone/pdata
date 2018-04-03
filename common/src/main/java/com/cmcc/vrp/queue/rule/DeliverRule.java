package com.cmcc.vrp.queue.rule;

import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.queue.queue.AbstractQueue;

/**
 * Created by sunyiwei on 2016/11/9.
 */
public interface DeliverRule {
    //根据消息内容进行分发

    /**
     * @Title: deliver
     * @Description: TODO
     */
    AbstractQueue deliver(ChargeDeliverPojo chargeDeliverPojo);
}
