package com.cmcc.vrp.queue.task;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.RedisUtilService;
import com.cmcc.vrp.charge.ChargeService;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SendMsgService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.UrlMapService;
import com.cmcc.vrp.queue.pojo.ActivitySendMessagePojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by qinqinyan on 2017/07/11.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ActivitySendMessageWorker extends Worker {

    private static final Logger logger = LoggerFactory.getLogger(ActivitySendMessageWorker.class);
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    @Autowired
    ChargeRecordService chargeRecordService;
    @Autowired
    ChargeService chargeService;
    @Autowired
    SerialNumService serialNumService;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    AccountService accountService;
    @Autowired
    ActivityPrizeService activityPrizeService;
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    SendMsgService sendMsgService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    ProductService productService;
    @Autowired
    RedisUtilService redisUtilService;
    @Autowired
    UrlMapService urlMapService;

    @Override
    public void exec() {
        //0. 获取队列消息
        String taskStr = getTaskString();
        logger.info("从短信构造队列中消费消息，消息内容为{}.", taskStr);

        //1. 解析参数
        ActivitySendMessagePojo activitySendMessagePojo;
        if (!validate(activitySendMessagePojo = parse(taskStr))) {
            logger.error("无效的短信构造队列请求参数，发送短信失败.");
            return;
        }

        //获取参数
        Activities activities = activitiesService.selectByActivityId(activitySendMessagePojo.getActivityId());
        if(activities.getType().toString().equals(ActivityType.FLOWCARD.getCode().toString())){
            if(activitiesService.notifyUserForFlowcard(activities)){
                logger.info("活动发送短信成功，活动id = {}", activities.getActivityId());
            }else{
                logger.info("活动发送短信失败，活动id = {}", activities.getActivityId());
            }
        }
    }

    //解析对象
    private ActivitySendMessagePojo parse(String taskString) {
        try {
            return JSONObject.parseObject(taskString, ActivitySendMessagePojo.class);
        } catch (Exception e) {
            logger.info("充值参数反序列化错误, 错误信息为{}， 具体的参数为{}", e.getMessage(), taskString);
            return null;
        }
    }

    //校验对象的有效性
    private boolean validate(ActivitySendMessagePojo taskPojo) {
        if (taskPojo == null
            || taskPojo.getActivityId() == null) {
            logger.error("无效的充值请求参数，taskPojo = {}.", JSONObject.toJSONString(taskPojo));
            return false;
        }

        return true;
    }
   
}
