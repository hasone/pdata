package com.cmcc.vrp.province.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.FlowCoinExchangeStatus;
import com.cmcc.vrp.enums.IndividualAccountRecordStatus;
import com.cmcc.vrp.enums.IndividualAccountType;
import com.cmcc.vrp.province.dao.IndividualFlowcoinExchangeMapper;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualAccountRecord;
import com.cmcc.vrp.province.model.IndividualFlowcoinExchange;
import com.cmcc.vrp.province.service.IndividualAccountRecordService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualFlowcoinExchangeService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.FlowcoinExchangePojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
/**
 * 个人流量币交换服务实现类
 * */
@Service("individualFlowcoinExchangeService")
public class IndividualFlowcoinExchangeServiceImpl implements IndividualFlowcoinExchangeService {
    private static final Logger logger = LoggerFactory.getLogger(IndividualFlowcoinExchangeServiceImpl.class);

    @Autowired
    IndividualFlowcoinExchangeMapper mapper;

    @Autowired
    IndividualAccountService individualAccountService;

    @Autowired
    IndividualProductService individualProductService;
    
    @Autowired
    IndividualAccountRecordService individualAccountRecordService;

    @Autowired
    TaskProducer producer;

    @Override
    public boolean insert(IndividualFlowcoinExchange record) {
        return mapper.insert(record) == 1;
    }

    @Override
    @Transactional
    public boolean createExchange(Long adminId, Long productId, Integer count, String mobile) {
        if (adminId == null || productId == null || count == null || count == 0 || StringUtils.isEmpty(mobile)) {
            return false;
        }

        IndividualFlowcoinExchange record = bulidIndividualFlowcoinExchangeRecord(adminId, productId, count, mobile);

        //1、插入流量币兑换记录
        if (!insert(record)) {
            throw new RuntimeException("插入兑换记录失败！");
        }
        
        //2、插入account_record记录
        //2-1、流量币减少记录
        IndividualAccount account = individualAccountService.getAccountByOwnerIdAndProductId(adminId, productId, 
                IndividualAccountType.INDIVIDUAL_BOSS.getValue());
        IndividualAccountRecord accountRecord = bulidIndividualAccountRecordForExchange(adminId, adminId, new BigDecimal(count), 
                record.getSystemSerial(), account.getId(), (int) AccountRecordType.OUTGO.getValue(), "流量币兑换-流量币减少");

        if (!individualAccountRecordService.create(accountRecord)) {
            logger.error("插入流量币兑换流量币扣减记录individualAccountRecord失败，individualAccountRecord={}", JSONObject.toJSONString(record));
            throw new RuntimeException("插入流量币兑换流量币扣减记录individualAccountRecord失败");
        }

        //插入队列
        FlowcoinExchangePojo pojo = new FlowcoinExchangePojo();
        pojo.setIndividualAccountRecord(accountRecord);
        pojo.setIndividualFlowcoinExchangeRecord(record);
        boolean bFlag = producer.produceFlowcoinExchangeMsg(pojo);
        if (!bFlag) {
            logger.error("Adding to charge queue returns {}.", bFlag);
            throw new RuntimeException("插入队列失败！");
        }
        return bFlag;
    }


    private IndividualFlowcoinExchange bulidIndividualFlowcoinExchangeRecord(
        Long adminId, Long productId, Integer count, String mobile) {
        IndividualFlowcoinExchange record = new IndividualFlowcoinExchange();
        record.setAdminId(adminId);
        record.setCount(count);
        record.setMobile(mobile);
        record.setIndividualProductId(productId);
        record.setStatus(FlowCoinExchangeStatus.PROCESSING.getCode());
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setDeleteFlag(0);
        record.setVersion(0L);
        record.setSystemSerial(generateSystemSerial(adminId));
        return record;
    }


    /**
     * 订单号生成
     *
     * @param adminId
     * @return
     * @Title: generateSystemSerial
     * @Author: wujiamin
     * @date 2016年9月29日上午10:43:37
     */
    private String generateSystemSerial(Long adminId) {
        Long time = System.currentTimeMillis();
        Random random = new Random();
        Integer randomInt = Math.abs(random.nextInt());
        return time.toString() + adminId.toString() + randomInt.toString();
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        return mapper.updateStatus(id, status) == 1;
    }

    @Override
    public int countByMap(Map map) {
        return mapper.countByMap(map);
    }

    @Override
    public List<IndividualFlowcoinExchange> selectByMap(Map map) {
        return mapper.selectByMap(map);
    }

    @Override
    public IndividualFlowcoinExchange selectBySystemSerial(String systemSerial) {
        return mapper.selectBySystemSerial(systemSerial);
    }
    
    private IndividualAccountRecord bulidIndividualAccountRecordForExchange(Long adminId, Long ownerId, BigDecimal count, 
            String systemSerial, Long accountId, Integer type, String desc) {
        IndividualAccountRecord record = new IndividualAccountRecord();
        record.setAdminId(adminId);
        record.setOwnerId(ownerId);
        record.setAccountId(accountId);
        record.setType(type);
        record.setStatus(IndividualAccountRecordStatus.PROCESSING.getValue());
        record.setSerialNum(systemSerial);
        record.setCount(count);
        record.setDescription(desc);
        record.setDeleteFlag(0);
        record.setActivityType(ActivityType.INDIVIDUAL_FLOWCOIN_EXCHANGE.getCode());
        record.setBack(0);
        return record;
    }
}
