package com.cmcc.vrp.province.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.YqxChargeInfoMapper;
import com.cmcc.vrp.province.model.YqxChargeInfo;
import com.cmcc.vrp.province.service.YqxChargeInfoService;

/**
 * YqxChargeInfoServiceImpl.java
 * @author wujiamin
 * @date 2017年5月10日
 */
@Service
public class YqxChargeInfoServiceImpl implements YqxChargeInfoService {
    @Autowired
    YqxChargeInfoMapper mapper;
    
    @Override
    public boolean insert(YqxChargeInfo record) {
        return mapper.insert(record) == 1;
    }
    
    @Override
    public boolean updateReturnSystemNum(YqxChargeInfo record) {
        return mapper.updateReturnSystemNum(record) == 1;
    }
    
    @Override
    public YqxChargeInfo selectByReturnSystemNum(String returnSystemNum) {
        List<YqxChargeInfo> list = mapper.selectByReturnSystemNum(returnSystemNum);
        return list.isEmpty()?null:list.get(0);
    }

    @Override
    public YqxChargeInfo selectBySerialNum(String serialNum) {
        return mapper.selectBySerialNum(serialNum);
    }
}
