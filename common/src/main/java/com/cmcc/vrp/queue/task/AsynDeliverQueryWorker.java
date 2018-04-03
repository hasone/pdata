package com.cmcc.vrp.queue.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ChargeQueryPojo;
import com.cmcc.vrp.util.DateUtil;
import com.google.gson.Gson;

/**
 * Created by leelyn on 2016/8/10.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AsynDeliverQueryWorker extends Worker {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsynDeliverQueryWorker.class);

    @Autowired
    private TaskProducer taskProducer;

    @Override
    public void exec() {
        //获取消息
        String msg = getTaskString();

        ChargeQueryPojo pojo = new Gson().fromJson(msg, ChargeQueryPojo.class);

        Date beginTime = DateUtil.parse("yyyy-MM-dd HH:mm:ss", pojo.getBeginTime());
        Integer times = pojo.getTimes();
        Double expireSeconds = 5*(Math.pow(2, times));
        
        if (DateUtil.getDateBeforeSeconds(new Date(), -expireSeconds.intValue()).after(beginTime)) {
            LOGGER.info("出队时间{},入队次数{},{}", beginTime,times,expireSeconds);
            LOGGER.info("从查询充值状态消息队列中获取消息:{}", msg);
            taskProducer.produceAsynChargeQueryMsg(pojo);
        } else {
            try {
                Thread.sleep(5000);
                taskProducer.produceAsynDeliverQueryMsg(pojo);
            } catch (InterruptedException e) {
                LOGGER.error("线程等待5秒抛出异常", e.getMessage());
                taskProducer.produceAsynDeliverQueryMsg(pojo);
            }
        }
    }
}
