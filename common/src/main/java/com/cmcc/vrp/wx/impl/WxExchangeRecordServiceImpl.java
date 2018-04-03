package com.cmcc.vrp.wx.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.WxExchangeRecordMapper;
import com.cmcc.vrp.wx.WxExchangeRecordService;
import com.cmcc.vrp.wx.model.WxExchangeRecord;

/**
 * WxExchangeRecordServiceImpl.java
 * @author wujiamin
 * @date 2017年3月17日
 */
@Service("wxExchangeRecordService")
public class WxExchangeRecordServiceImpl implements WxExchangeRecordService {
    @Autowired
    WxExchangeRecordMapper mapper;
    
    @Override
    public boolean insert(WxExchangeRecord record) {
        if(record == null){
            return false;
        }
        return mapper.insert(record) == 1;
    }

    @Override
    public WxExchangeRecord selectBySystemNum(String systemNum) {
        return mapper.selectBySystemNum(systemNum);
    }
    
    @Override
    public boolean updateByPrimaryKeySelective(WxExchangeRecord record) {
        return mapper.updateByPrimaryKeySelective(record) == 1;
    }

    @Override
    public int sumProductSize(int month, Long adminId) {
        return mapper.sumProductSize(month, adminId);
    }

    @Override
    public int sumProductNum(int month, Long productId) {
        return mapper.sumProductNum(month, productId);
    }

    @Override
    public List<WxExchangeRecord> selectByMap(Map map) {        
        return mapper.selectByMap(map);
    }
    
    @Override
    public int countByMap(Map map) {        
        return mapper.countByMap(map);
    }

    @Override
    public int sumAllProductSize(int month) {
        return mapper.sumAllProductSize(month);
    }

    @Override
    public int sumDayProductSize(Date begin, Date end, Long adminId) {
        return mapper.sumDayProductSize(begin, end, adminId);
    }

}
