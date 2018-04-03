package com.cmcc.vrp.queue.task;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.charge.ChargeService;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ActivityWinRecordStatus;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.ChargeType;
import com.cmcc.vrp.enums.FinanceStatus;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.pojo.ActivitiesWinPojo;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.queue.queue.busi.DeliverByBossQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by sunyiwei on 2016/08/24.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ActivityWorker extends Worker {

    private static final Logger logger = LoggerFactory.getLogger(ActivityWorker.class);
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

    //1. 解析充值参数
    //2. 更新记录状态为已发送充值请求
    //3. 发送充值请求
    //4. 更新记录状态为充值结果
    @Override
    public void exec() {
        //0. 获取队列消息
        String taskStr = getTaskString();
        logger.info("从充值队列中消费消息，消息内容为{}.", taskStr);

        //1. 解析充值参数
        ActivitiesWinPojo activityWinPojo;
        if (!validate(activityWinPojo = parse(taskStr))) {
            logger.error("无效的充值请求参数，充值失败.");
            return;
        }

        //获取参数
        Activities activities = activityWinPojo.getActivities();
        String activityWinRecordId = activityWinPojo.getActivitiesWinRecordId();
        ActivityWinRecord record;
        if (activities == null || activityWinRecordId == null
            || (record = activityWinRecordService.selectByRecordId(activityWinRecordId)) == null) {
            logger.error("活动对象或活动中奖记录id或中奖记录为空.");
            return;
        }

        //获取充值类型
        ChargeType chargeType = getChargeType(ActivityType.fromValue(activities.getType()));

        //3. 塞入分发队列
        ChargeDeliverPojo chargeDeliverPojo = buildChargeDeliverPojo(record, activities, chargeType, activityWinRecordId);
        ChargeRecord cr = chargeRecordService.getRecordBySN(activityWinRecordId);
        DeliverByBossQueue adw = applicationContext.getBean(DeliverByBossQueue.class);
        if (!adw.publish(chargeDeliverPojo)) {
            logger.info("入分发队列失败");
            record.setStatus(ActivityWinRecordStatus.FALURE.getCode());
            record.setReason(ActivityWinRecordStatus.FALURE.getname());
            
            Date updateChargeTime = new Date();
            Integer financeStatus = null;
            
            if (!accountService.returnFunds(activityWinRecordId)) {
                logger.error("退款失败, PlatformSerialNum={}." + activityWinRecordId);
            }else{
                financeStatus = FinanceStatus.IN.getCode();
            }

            if (!activityWinRecordService.updateByPrimaryKeySelective(record)) {
                logger.error("更新activityWinRecord记录时失败" + JSONObject.toJSONString(record));
            }
           
            ChargeRecordStatus chargeRecordStatus = ChargeRecordStatus.FAILED;
            chargeRecordStatus.setFinanceStatus(financeStatus);
            chargeRecordStatus.setUpdateChargeTime(updateChargeTime);
            
            if (!chargeRecordService.updateStatus(cr.getId(), chargeRecordStatus, ChargeRecordStatus.FAILED.getMessage())) {
                logger.error("更新充值记录时失败, id = {}." + cr.getId());
            }
        } else {

            if (!activityWinRecordService.updateStatusCodeByRecordId(activityWinRecordId, ChargeResult.ChargeMsgCode.deliverQueue.getCode())) {
                logger.error("更新activityWinRecord记录时失败, recordId =", activityWinRecordId);
            }
            if (!chargeRecordService.updateStatusCode(cr.getId(), ChargeResult.ChargeMsgCode.deliverQueue.getCode())) {
                logger.error("更新充值记录时失败, id = {}." + cr.getId());
            }
            logger.info("入分发队列成功, recordId = {}, 状态码 = {}", activityWinRecordId, ChargeResult.ChargeMsgCode.deliverQueue.getCode());
        }

    }

    /**
     * 根据活动类型获取充值的类型
     *
     * @return
     * @Title: getChargeType
     * @Author: wujiamin
     * @date 2016年8月25日上午11:30:40
     */
    private ChargeType getChargeType(ActivityType type) {

        if (type.getCode().equals(ActivityType.REDPACKET.getCode())
            || type.getCode().equals(ActivityType.LUCKY_REDPACKET.getCode())) {
            return ChargeType.REDPACKET_TASK;
        } else if (type.getCode().equals(ActivityType.LOTTERY.getCode())) {
            return ChargeType.LOTTERY_TASK;
        } else if (type.getCode().equals(ActivityType.GOLDENBALL.getCode())) {
            return ChargeType.GOLDENBALL_TASK;
        } else if (type.getCode().equals(ActivityType.FLOWCARD.getCode())) {
            return ChargeType.FLOWCARD_TASK;
        } else if (type.getCode().equals(ActivityType.QRCODE.getCode())) {
            return ChargeType.QRCODE_TASK;
        } else if(type.getCode().equals(ActivityType.CROWD_FUNDING.getCode())){
            return ChargeType.CROWDFUNDING_TASK;
        } else {
            logger.info("type = {}.", type.getCode());
            return ChargeType.OTHERS;
        }

    }

    //解析充值对象
    private ActivitiesWinPojo parse(String taskString) {
        try {
            return JSONObject.parseObject(taskString, ActivitiesWinPojo.class);
        } catch (Exception e) {
            logger.info("充值参数反序列化错误, 错误信息为{}， 具体的参数为{}", e.getMessage(), taskString);
            return null;
        }
    }

    //校验充值对象的有效性
    private boolean validate(ActivitiesWinPojo taskPojo) {
        if (taskPojo == null
            || taskPojo.getActivities() == null
            || taskPojo.getActivitiesWinRecordId() == null) {
            logger.error("无效的充值请求参数，ActivitiesWinPojo = {}.", JSONObject.toJSONString(taskPojo));
            return false;
        }

        return true;
    }

    private ChargeDeliverPojo buildChargeDeliverPojo(ActivityWinRecord awr, Activities activities, ChargeType chargeType, String serialNum) {
        ChargeDeliverPojo cdp = new ChargeDeliverPojo();
        cdp.setEntId(activities.getEntId());
        if(activities.getType().toString()
                .equals(ActivityType.CROWD_FUNDING.getCode().toString())){
            //众筹活动，中奖纪录里的prizeId是奖品的id，其余活动中奖纪录的prizeId是产品id
            ActivityPrize prize = activityPrizeService.selectByPrimaryKey(awr.getPrizeId());
            cdp.setPrdId(prize.getProductId());
        }else{
            cdp.setPrdId(awr.getPrizeId());
        }
        cdp.setMobile(awr.getChargeMobile() == null ? awr.getOwnMobile() : awr.getChargeMobile());
        cdp.setType(chargeType.getCode());
        cdp.setActivityName(activities.getName());
        cdp.setActivityType(ActivityType.fromValue(activities.getType()));
        cdp.setRecordId(awr.getId());
        cdp.setSerialNum(serialNum);
        return cdp;
    }
}
