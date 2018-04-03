package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.province.dao.IndividualFlowcoinRecordMapper;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.IndividualAccountRecord;
import com.cmcc.vrp.province.model.IndividualFlowcoinExchange;
import com.cmcc.vrp.province.model.IndividualFlowcoinPurchase;
import com.cmcc.vrp.province.model.IndividualFlowcoinRecord;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.IndividualFlowcoinExchangeService;
import com.cmcc.vrp.province.service.IndividualFlowcoinPurchaseService;
import com.cmcc.vrp.province.service.IndividualFlowcoinRecordService;
import com.cmcc.vrp.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("individualFlowcoinRecordService")
public class IndividualFlowcoinRecordServiceImpl implements IndividualFlowcoinRecordService {
    @Autowired
    IndividualFlowcoinRecordMapper mapper;
    @Autowired
    IndividualFlowcoinExchangeService individualFlowcoinExchangeService;
    @Autowired
    IndividualFlowcoinPurchaseService individualFlowcoinPurchaseService;
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    ActivityWinRecordService activityWinRecordService;

    @Override
    public boolean insert(IndividualFlowcoinRecord record) {
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setDeleteFlag(0);
        return mapper.insert(record) == 1;
    }

    /**
     * 根据账户信息插入流量币变更记录
     */
    @Override
    @Transactional
    public boolean createRecord(IndividualAccountRecord accountRecord) {
        //支出（1、兑换；2、创建活动）
        if (accountRecord.getType().equals((int) AccountRecordType.OUTGO.getValue())) {
            //兑换
            if (accountRecord.getActivityType().equals(ActivityType.INDIVIDUAL_FLOWCOIN_EXCHANGE.getCode())) {
                IndividualFlowcoinExchange exchangeRecord = individualFlowcoinExchangeService.selectBySystemSerial(accountRecord.getSerialNum());
                String mobile = null;
                if (exchangeRecord == null || (mobile = exchangeRecord.getMobile()) == null) {
                    return false;
                }
                if (!insert(bulidIndividualFlowcoinRecord(accountRecord, null, "兑换", mobile))) {
                    return false;
                }
            }
            //创建活动
            //创建活动的支出，在活动中奖用户流量币账户增加时一起添加记录
        
        //收入（1、购买；2、活动所得(需添加活动创建者账户的支出记录)；3、活动退回；4、兑换回退）
        } else {
            //购买
            if (accountRecord.getActivityType().equals(ActivityType.INDIVIDUAL_FLOWCOIN_PURCHASE.getCode())) {
                IndividualFlowcoinPurchase purchaseRecord = individualFlowcoinPurchaseService.selectBySystemSerial(accountRecord.getSerialNum());
                if (purchaseRecord == null) {
                    return false;
                }
                if (insert(bulidIndividualFlowcoinRecord(accountRecord, DateUtil.getEndOfNextYear(new Date()), "购买", null))) {
                    return true;
                }
            } else {
                //回退的操作
                if (accountRecord.getBack() == 1) {
                    //兑换的退回
                    if (accountRecord.getActivityType().equals(ActivityType.INDIVIDUAL_FLOWCOIN_EXCHANGE.getCode())) {
                        IndividualFlowcoinExchange exchangeRecord = individualFlowcoinExchangeService.selectBySystemSerial(accountRecord.getSerialNum());
                        String mobile = null;
                        if (exchangeRecord == null || (mobile = exchangeRecord.getMobile()) == null) {
                            return false;
                        }
                        if (!insert(bulidIndividualFlowcoinRecord(accountRecord, DateUtil.getEndOfNextYear(new Date()), "退回(兑换)", null))) {
                            return false;
                        }
                    } else {//活动的退回
                        Activities activities = activitiesService.selectByActivityId(accountRecord.getSerialNum());
                        if (activities == null) {
                            return false;
                        }
                        if (!insert(bulidIndividualFlowcoinRecord(accountRecord, DateUtil.getEndOfNextYear(new Date()), "退回(活动)", null))) {
                            return false;
                        }
                    }
                } else {//活动中奖
                    ActivityWinRecord winRecord = activityWinRecordService.selectByRecordId(accountRecord.getSerialNum());
                    if (winRecord == null) {
                        return false;
                    }
                    Activities activities = activitiesService.selectByActivityId(winRecord.getActivityId());
                    if (activities == null) {
                        return false;
                    }
                    //插入活动中奖者的流量币收入记录和活动创建者的流量币支出记录
                    if (!insert(bulidIndividualFlowcoinRecord(accountRecord, DateUtil.getEndOfNextYear(new Date()),
                        ActivityType.fromValue(activities.getType()).getname(), null))) {
                        throw new RuntimeException();
                    }
                    if (!insert(bulidIndividualFlowcoinOutRecord(activities.getCreatorId(), accountRecord, DateUtil.getEndOfNextYear(new Date()),
                        ActivityType.fromValue(activities.getType()).getname(), winRecord.getChargeMobile()))) {
                        throw new RuntimeException();
                    }
                }
            }
        }

        return true;
    }

    private IndividualFlowcoinRecord bulidIndividualFlowcoinRecord(IndividualAccountRecord accountRecord, Date expireTime, String description, String mobile) {
        IndividualFlowcoinRecord record = new IndividualFlowcoinRecord();

        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setAdminId(accountRecord.getAdminId());
        record.setIndividualAccountRecordId(accountRecord.getAccountId());
        record.setCount(accountRecord.getCount().intValue());
        record.setType(accountRecord.getType());
        record.setExpireTime(expireTime);
        record.setDescription(description);
        record.setMobile(mobile);
        record.setActivityType(accountRecord.getActivityType());

        return record;
    }

    private IndividualFlowcoinRecord bulidIndividualFlowcoinOutRecord(Long adminId, 
    		IndividualAccountRecord accountRecord, 
    		Date expireTime, 
    		String description, 
    		String mobile) {
        IndividualFlowcoinRecord record = new IndividualFlowcoinRecord();

        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setAdminId(adminId);
        record.setIndividualAccountRecordId(accountRecord.getAccountId());
        record.setCount(accountRecord.getCount().intValue());
        record.setType((int) AccountRecordType.OUTGO.getValue());
        record.setExpireTime(expireTime);
        record.setDescription(description);
        record.setMobile(mobile);
        record.setActivityType(accountRecord.getActivityType());

        return record;
    }

    @Override
    public List<IndividualFlowcoinRecord> selectByMap(Map map) {
        return mapper.selectByMap(map);
    }

    @Override
    public int countByMap(Map map) {
        return mapper.countByMap(map);
    }

}
