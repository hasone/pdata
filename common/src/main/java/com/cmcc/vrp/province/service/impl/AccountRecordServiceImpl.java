package com.cmcc.vrp.province.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.dao.AccountRecordMapper;
import com.cmcc.vrp.province.model.AccountRecord;
import com.cmcc.vrp.province.service.AccountRecordService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 默认的帐户记录服务的实现，在平台中有企业的帐户体系的时候需要使用这个实现
 * <p>
 * Created by sunyiwei on 2016/4/14.
 */
public class AccountRecordServiceImpl implements AccountRecordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountRecordServiceImpl.class);

    @Autowired
    AccountRecordMapper accountRecordMapper;

    @Override
    public boolean batchInsert(List<AccountRecord> records) {
        return batchValidate(records)
            && (records.isEmpty() || accountRecordMapper.batchInsert(records) == records.size());
    }

    @Override
    public boolean create(AccountRecord accountRecord) {
        return validate(accountRecord)
            && accountRecordMapper.insert(accountRecord) == 1;
    }

    @Override
    public List<AccountRecord> selectBySerialNumAndEnterId(String serialNum, Long enterId) {
        if (StringUtils.isBlank(serialNum) || enterId == null) {
            return null;
        }

        return accountRecordMapper.selectBySerialNumAndEnterId(serialNum, enterId);
    }

    @Override
    public AccountRecord getOutgoingRecordByPltSn(String pltSn) {
        return StringUtils.isBlank(pltSn) ? null : accountRecordMapper.getOutgoingRecordByPltSn(pltSn);
    }

    private boolean validate(AccountRecord ar) {
        LOGGER.info("开始校验帐户记录...AccountRecord = {}.", JSONObject.toJSONString(ar));

        boolean flag = (ar != null
            && ar.getEnterId() != null
            && ar.getOwnerId() != null
            && ar.getAccountId() != null
            && ar.getType() != null
            && StringUtils.isNotBlank(ar.getSerialNum())
            && ar.getCount() != 0);

        LOGGER.info("校验结果为{}.", flag);
        return flag;
    }

    private boolean batchValidate(List<AccountRecord> ars) {

        LOGGER.info("开始批量校验帐户记录.");
        if (ars == null) {
            return false;
        }

        for (AccountRecord ar : ars) {
            if (!validate(ar)) {
                LOGGER.error("校验帐户记录失败. AccountRecord = {}.", JSONObject.toJSONString(ar));
                return false;
            }
        }

        LOGGER.info("批量校验帐户记录通过.");
        return true;
    }
}
