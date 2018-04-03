package com.cmcc.vrp.province.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.ChangeMobileRecordMapper;
import com.cmcc.vrp.province.model.ChangeMobileRecord;
import com.cmcc.vrp.province.service.ChangeMobileRecordService;

/**
 * 变更手机号服务类
 * @author qinqinyan
 * */
@Service("changeMobileRecordService")
public class ChangeMobileRecordServiceImpl implements ChangeMobileRecordService{
    @Autowired
    ChangeMobileRecordMapper mapper;
    @Override
    public boolean deleteByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        return mapper.deleteByPrimaryKey(id)>=0;
    }

    @Override
    public boolean insert(ChangeMobileRecord record) {
        // TODO Auto-generated method stub
        return mapper.insert(record)>=0;
    }

    @Override
    public boolean insertSelective(ChangeMobileRecord record) {
        // TODO Auto-generated method stub
        return mapper.insertSelective(record)>=0;
    }

    @Override
    public ChangeMobileRecord selectByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateByPrimaryKeySelective(ChangeMobileRecord record) {
        // TODO Auto-generated method stub
        return mapper.updateByPrimaryKeySelective(record)>=0;
    }

    @Override
    public boolean updateByPrimaryKey(ChangeMobileRecord record) {
        // TODO Auto-generated method stub
        return mapper.updateByPrimaryKey(record)>=0;
    }

}
