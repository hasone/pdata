package com.cmcc.vrp.queue.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.charge.BossChargeLog;
import com.cmcc.vrp.charge.ChargeLogPojo;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.charge.ChargeService;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.ChargeType;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.pojo.PresentPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName: FlowCardTask
 * @Description: 执行流量券赠送的worker类
 * @author: sunyiwei
 * @date: 2015年5月21日 上午9:40:45
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FlowCardWorker extends Worker {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowCardWorker.class);

    @Autowired
    ChargeService chargeService;
    @Autowired
    ChargeRecordService chargeRecordService;
    @Autowired
    SerialNumService serialNumService;
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    @Autowired
    ActivitiesService activitiesService;

    @Override
    public void exec() {
        PresentPojo taskPojo = null;
        String taskString = getTaskString();

        try {
            taskPojo = JSON.parseObject(taskString, PresentPojo.class);
            LOGGER.info("开始处理流量券赠送, 赠送参数为{}" + taskString);
        } catch (JSONException e) {
            LOGGER.info("流量券赠送参数反序列化错误, 错误信息为{}， 具体的参数为{}", e.toString(), taskString);
            return;
        }

        ActivityWinRecord record = activityWinRecordService.selectByPrimaryKey(taskPojo.getRecordId());
        if (record == null) {
            LOGGER.error("no record is found by id=\"" + taskPojo.getRecordId() + "\".");
            return;
        }
        Activities activity = activitiesService.selectByActivityId(record.getActivityId());

        String systemNum = SerialNumGenerator.buildSerialNum();
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setEnterId(activity.getEntId());
        chargeRecord.setPrdId(record.getPrizeId());
        chargeRecord.setStatus(ChargeRecordStatus.PROCESSING.getCode());
        chargeRecord.setType(ActivityType.FLOWCARD.getname());
        chargeRecord.setaName(activity.getName());
        chargeRecord.setTypeCode(ActivityType.FLOWCARD.getCode());
        chargeRecord.setPhone(taskPojo.getMobile());
        chargeRecord.setRecordId(taskPojo.getRecordId());
        chargeRecord.setChargeTime(new Date());
        chargeRecord.setSystemNum(systemNum);

        if (chargeRecordService.create(chargeRecord)
            && serialNumService.insert(initSerailNum(systemNum))) {
            // 对接BOSS,包括打日志
            ChargeLogPojo logPojo = new ChargeLogPojo(ChargeType.FLOWCARD_TASK, Long.toString(new Date().getTime()), null, record.getId(),
                taskPojo.getEnterpriseId(), taskPojo.getProductId(), taskPojo.getMobile());

            //打印request日志
            BossChargeLog.printRequestLog(logPojo);

            ChargeResult chargeResult = chargeService.charge(chargeRecord.getId(), taskPojo.getEnterpriseId(), taskPojo.getRuleId(), taskPojo.getProductId(), AccountType.ENTERPRISE, taskPojo.getMobile(), taskPojo.getRequestSerialNum());

            //打印response日志
            BossChargeLog.printResponseLog(logPojo, chargeResult);

            //处理充值结果
            if (chargeResult.isSuccess()) {
                if (chargeResult.getCode() == ChargeResult.ChargeResultCode.PROCESSING) {
                    record.setStatus(ChargeRecordStatus.PROCESSING.getCode());
                    record.setReason(ChargeRecordStatus.PROCESSING.getMessage());

                    chargeRecord.setStatus(ChargeRecordStatus.PROCESSING.getCode());
                    chargeRecord.setErrorMessage(ChargeRecordStatus.PROCESSING.getMessage());
                } else {
                    record.setStatus(ChargeRecordStatus.COMPLETE.getCode());
                    record.setReason(ChargeRecordStatus.COMPLETE.getMessage());
                    chargeRecord.setStatus(ChargeRecordStatus.COMPLETE.getCode());
                    chargeRecord.setErrorMessage(ChargeRecordStatus.COMPLETE.getMessage());
                }
            } else {
                record.setStatus(ChargeRecordStatus.FAILED.getCode());
                record.setReason(chargeResult.getFailureReason());

                chargeRecord.setStatus(ChargeRecordStatus.FAILED.getCode());
                chargeRecord.setErrorMessage(chargeResult.getFailureReason());
            }

            //更新回数据库
            record.setUpdateTime(new Date());
            record.setChargeTime(new Date());

            chargeRecord.setChargeTime(record.getChargeTime());
            if (!chargeRecordService.updateByPrimaryKeySelective(chargeRecord)) {
                LOGGER.error("更新chargeRecord记录时失败" + JSONObject.toJSONString(chargeRecord));
            }

            if (!activityWinRecordService.updateByPrimaryKeySelective(record)) {
                LOGGER.error("更新activityWinRecord记录时失败" + JSONObject.toJSONString(record));
            }

        } else {
            LOGGER.error("chargeRecord记录插入失败,chargeRecord:" + JSON.toJSONString(chargeRecord));

            record.setUpdateTime(new Date());
            record.setChargeTime(new Date());
            record.setStatus(ChargeRecordStatus.FAILED.getCode());
            record.setReason("chargeRecord记录插入失败");
            if (!activityWinRecordService.updateByPrimaryKeySelective(record)) {
                LOGGER.error("更新activityWinRecord记录时失败" + JSONObject.toJSONString(record));
            }
        }
    }

    private SerialNum initSerailNum(String platformSerialNum) {
        SerialNum serialNum = new SerialNum();
        serialNum.setPlatformSerialNum(platformSerialNum);
        serialNum.setUpdateTime(new Date());
        serialNum.setCreateTime(new Date());
        serialNum.setDeleteFlag(0);
        return serialNum;
    }
}
