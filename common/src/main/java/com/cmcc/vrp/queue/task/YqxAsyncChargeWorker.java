package com.cmcc.vrp.queue.task;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.service.YqxChargeService;
import com.cmcc.vrp.queue.pojo.YqxChargePojo;
import com.google.gson.Gson;

/**
 * Created by qihang
 * 云企信充值线程
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class YqxAsyncChargeWorker extends Worker {

    private static final Logger logger = LoggerFactory.getLogger(YqxAsyncChargeWorker.class);
    
    @Autowired
    private YqxChargeService yqxChargeService;
   
    @Override
    public void exec() {
        //0. 获取队列消息
        Gson gson = new Gson();
        String taskStr = getTaskString();
        logger.info("从云企信充值队列中消费消息，消息内容为{}.", taskStr);
        
        YqxChargePojo chargePojo;
        if (!validate(chargePojo = parse(gson, taskStr))) {
            logger.error("无效的充值请求参数，充值失败.");
            return;
        }

        logger.info("开始充值操作,json=" + new Gson().toJson(chargePojo));
        if(yqxChargeService.charge(chargePojo.getPayTransactionId())){
            logger.info("提交充值成功，payTransactionId={}", chargePojo.getPayTransactionId());
        }
        
    }
    
    //解析充值对象
    private YqxChargePojo parse(Gson gson, String taskString) {
        try {
            return gson.fromJson(taskString, YqxChargePojo.class);
        } catch (Exception e) {
            logger.info("充值参数反序列化错误, 错误信息为{}， 具体的参数为{}", e.getMessage(), taskString);
            return null;
        }
    }
    
    //校验充值对象的有效性
    private boolean validate(YqxChargePojo taskPojo) {
        if (taskPojo == null
            || StringUtils.isBlank(taskPojo.getPayOrderId())
            || StringUtils.isBlank(taskPojo.getPayTransactionId())) {
            logger.error("无效的充值请求参数，ChargePojo = {}.", JSONObject.toJSONString(taskPojo));
            return false;
        }
        return true;
    }

}
