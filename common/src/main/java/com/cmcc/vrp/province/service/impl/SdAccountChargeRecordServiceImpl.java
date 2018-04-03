package com.cmcc.vrp.province.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.SdAccountChargeRecordMapper;
import com.cmcc.vrp.province.model.SdAccountChargeRecord;
import com.cmcc.vrp.province.service.SdAccountChargeRecordService;
/**
 * 
 * @ClassName: SdAccountChargeRecordServiceImpl 
 * @Description: 山东BOSS账户充值记录服务类
 * @author: Rowe
 * @date: 2017年9月1日 上午9:47:29
 */
@Service
public class SdAccountChargeRecordServiceImpl implements SdAccountChargeRecordService{
    
    @Autowired
    private SdAccountChargeRecordMapper sdAccountChargeRecordMapper;

    @Override
    public boolean insertSelective(SdAccountChargeRecord record) {
        if(record == null){
            return false;
        }
        return sdAccountChargeRecordMapper.insertSelective(record) == 1;
    }

    @Override
    public boolean updateByPrimaryKeySelective(SdAccountChargeRecord record) {
        if(record == null || record.getId() == null){
            return false;
        }
        return sdAccountChargeRecordMapper.updateByPrimaryKeySelective(record) == 1;
    }
}
