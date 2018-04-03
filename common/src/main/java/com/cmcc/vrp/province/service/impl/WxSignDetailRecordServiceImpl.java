package com.cmcc.vrp.province.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.WxSignDetailRecordMapper;
import com.cmcc.vrp.province.model.WxSignDetailRecord;
import com.cmcc.vrp.province.service.WxSignDetailRecordService;

@Service("wxSignDetailRecordService")
public class WxSignDetailRecordServiceImpl implements WxSignDetailRecordService{
    @Autowired
    WxSignDetailRecordMapper mapper;
    
    @Override
    public boolean insertSelective(WxSignDetailRecord record) {
        // TODO Auto-generated method stub
        return mapper.insertSelective(record)>=0;
    }

    @Override
    public List<WxSignDetailRecord> selectByMap(Map map) {
        // TODO Auto-generated method stub
        return mapper.selectByMap(map);
    }

}
