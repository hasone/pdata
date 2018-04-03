package com.cmcc.vrp.wx.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.WxInviteRecordMapper;
import com.cmcc.vrp.wx.WxInviteRecordService;
import com.cmcc.vrp.wx.model.WxInviteRecord;

@Service
public class WxInviteRecordServiceImpl implements WxInviteRecordService {
    @Autowired
    WxInviteRecordMapper mapper;
    
    @Override
    public boolean insert(WxInviteRecord record) {        
        return mapper.insert(record) == 1;
    }

    @Override
    public boolean insertSelective(WxInviteRecord record) {
        return mapper.insertSelective(record) == 1;
    }

    @Override
    public WxInviteRecord selectByPrimaryKey(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateByPrimaryKeySelective(WxInviteRecord record) {
        return mapper.updateByPrimaryKeySelective(record) == 1;
    }

    @Override
    public boolean updateByPrimaryKey(WxInviteRecord record) {
        return mapper.updateByPrimaryKey(record) == 1;
    }

    @Override
    public WxInviteRecord selectBySerialNum(String serialNum) {
        return mapper.selectBySerialNum(serialNum);
    }

    @Override
    public List<WxInviteRecord> selectByMap(Map map) {
        return mapper.selectByMap(map);
    }

}
