package com.cmcc.vrp.queue.task;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.sichuan.service.IndividualBossService;
import com.cmcc.vrp.charge.ChargeService;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.FlowCoinExchangeStatus;
import com.cmcc.vrp.enums.IndividualAccountRecordStatus;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.IndividualAccountRecord;
import com.cmcc.vrp.province.model.IndividualFlowcoinExchange;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.IndividualAccountRecordService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualFlowcoinExchangeService;
import com.cmcc.vrp.province.service.IndividualFlowcoinRecordService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.pojo.FlowcoinExchangePojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author wujiamin
 * @date 2016年10月17日下午4:52:56
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FlowcoinExchangeWorker extends Worker {
    private static final Logger logger = LoggerFactory.getLogger(FlowcoinExchangeWorker.class);

    @Autowired
    IndividualAccountService accountService;
    @Autowired
    ChargeRecordService chargeRecordService;
    @Autowired
    ChargeService chargeService;
    @Autowired
    SerialNumService serialNumService;
    @Autowired
    IndividualFlowcoinExchangeService individualFlowcoinExchangeService;
    @Autowired
    IndividualBossService individualBossService;
    @Autowired
    IndividualAccountRecordService individualAccountRecordService;
    @Autowired
    IndividualFlowcoinRecordService individualFlowcoinRecordService;

    @Override
    public void exec() {
        //0. 获取队列消息
        String taskStr = getTaskString();
        logger.info("从充值队列中消费消息，消息内容为{}.", taskStr);

        //1. 解析充值参数
        FlowcoinExchangePojo pojo;
        if ((pojo = parse(taskStr)) == null) {
            logger.error("无效的充值请求参数，充值失败.");
            return;
        }

        //获取参数
        IndividualAccountRecord accountRecord = pojo.getIndividualAccountRecord();
        IndividualFlowcoinExchange record = pojo.getIndividualFlowcoinExchangeRecord();

        //1、发起向boss兑换流量包的请求       
        //创建充值记录
        ChargeRecord chargeRecord = null;
        if ((chargeRecord = insertRecord(record)) == null) {
            logger.error("插入充值记录和调用记录时失败. 充值参数为{}.", JSONObject.toJSONString(record));
            return;
        }

        //发送充值请求
        Boolean chargeResult = individualBossService.chargeFlow(record.getMobile(), record.getIndividualProductId(), record.getSystemSerial());

        //2、充值结果成功
        if (chargeResult) {
            chargeRecord.setStatus(ChargeRecordStatus.COMPLETE.getCode());
            chargeRecord.setErrorMessage(ChargeRecordStatus.COMPLETE.getMessage());
            chargeRecord.setChargeTime(new Date());
            //更新充值状态
            if (!chargeRecordService.updateByPrimaryKeySelective(chargeRecord)) {
                logger.error("更新chargeRecord记录时失败" + JSONObject.toJSONString(chargeRecord));
            }
            //更新兑换记录状态
            if (!individualFlowcoinExchangeService.updateStatus(record.getId(), FlowCoinExchangeStatus.FAIL_BACK_PROC.getCode())) {
                logger.error("更新兑换记录状态失败，individualFlowcoinExchange记录recordId={}", record.getId());
            }
            //更新account_record记录状态
            record.setStatus(IndividualAccountRecordStatus.SUCCESS.getValue());
            if (!individualAccountRecordService.updateByPrimaryKeySelective(accountRecord)) {
                if (accountRecord != null) {
                    logger.error("更新兑换记录账户记录状态失败，individualAccountRecord记录recordId={}", accountRecord.getId());
                }
            }
            //插入流量币记录flowcoin_record
            if (!individualFlowcoinRecordService.createRecord(accountRecord)) {
                if (accountRecord != null) {
                    logger.error("在流量币记录中插入失败，accountRecord记录recordId={}", accountRecord.getId());
                }

            }

        }

        //3、充值结果失败
        if (!chargeResult) {
            chargeRecord.setStatus(ChargeRecordStatus.FAILED.getCode());
            chargeRecord.setErrorMessage(ChargeRecordStatus.FAILED.getMessage());
            chargeRecord.setChargeTime(new Date());
            //更新充值状态
            if (!chargeRecordService.updateByPrimaryKeySelective(chargeRecord)) {
                logger.error("更新chargeRecord记录时失败" + JSONObject.toJSONString(chargeRecord));
            }

            //更新兑换记录状态
            if (!individualFlowcoinExchangeService.updateStatus(record.getId(), FlowCoinExchangeStatus.FAIL_BACK_SUCC.getCode())) {
                logger.error("更新兑换记录状态失败，individualFlowcoinExchange记录recordId={}", record.getId());
            }
            //更新account_record记录状态
            record.setStatus(IndividualAccountRecordStatus.FAIL.getValue());
            if (!individualAccountRecordService.updateByPrimaryKeySelective(accountRecord)) {
                if (accountRecord != null) {
                    logger.error("更新兑换记录账户记录状态失败，individualAccountRecord记录recordId={}", accountRecord.getId());
                }
            }
        }
    }

    //解析充值对象
    private FlowcoinExchangePojo parse(String taskString) {
        try {
            return JSONObject.parseObject(taskString, FlowcoinExchangePojo.class);
        } catch (Exception e) {
            logger.info("参数反序列化错误, 错误信息为{}， 具体的参数为{}", e.getMessage(), taskString);
            return null;
        }
    }

    private ChargeRecord insertRecord(IndividualFlowcoinExchange record) {

        if (record == null || record.getId() == null) {
            logger.error("IndividualFlowcoinExchange is null");
            return null;
        }

        String systemNum = SerialNumGenerator.buildSerialNum();
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setEnterId(record.getAdminId());
        chargeRecord.setPrdId(record.getIndividualProductId());
        chargeRecord.setStatus(ChargeRecordStatus.PROCESSING.getCode());
        chargeRecord.setType(ActivityType.INDIVIDUAL_FLOWCOIN_EXCHANGE.getname());
        chargeRecord.setaName(ActivityType.INDIVIDUAL_FLOWCOIN_EXCHANGE.getname());
        chargeRecord.setTypeCode(ActivityType.INDIVIDUAL_FLOWCOIN_EXCHANGE.getCode());
        chargeRecord.setPhone(record.getMobile());
        chargeRecord.setRecordId(record.getId());
        chargeRecord.setChargeTime(new Date());
        chargeRecord.setSystemNum(systemNum);

        if (!serialNumService.insert(initSerailNum(systemNum)) || !chargeRecordService.create(chargeRecord)) {
            return null;
        }

        return chargeRecord;
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
