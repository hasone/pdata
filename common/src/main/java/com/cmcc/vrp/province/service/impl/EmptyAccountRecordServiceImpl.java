package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.AccountRecordMapper;
import com.cmcc.vrp.province.model.AccountRecord;
import com.cmcc.vrp.province.service.AccountRecordService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 帐户记录服务的实现，在平台中没有相应的企业帐户信息时，可以使用这个实现
 * <p>
 * Created by sunyiwei on 2016/4/14.
 */
public class EmptyAccountRecordServiceImpl implements AccountRecordService {
    @Autowired
    AccountRecordMapper accountRecordMapper;

    @Override
    public boolean batchInsert(List<AccountRecord> records) {
        return true;
    }

    @Override
    public boolean create(AccountRecord accountRecord) {
        return true;
    }

    @Override
    public List<AccountRecord> selectBySerialNumAndEnterId(String serialNum,
                                                           Long enterId) {
        return accountRecordMapper.selectBySerialNumAndEnterId(serialNum, enterId);
    }

    @Override
    public AccountRecord getOutgoingRecordByPltSn(String pltSn) {
        return null;
    }
}
