package com.cmcc.vrp.province.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.IndividualActivitySerialNumMapper;
import com.cmcc.vrp.province.model.IndividualActivitySerialNum;
import com.cmcc.vrp.province.service.IndividualActivitySerialNumService;

/**
 * IndividualActivitySerialNumServiceImpl.java
 * @author wujiamin
 * @date 2017年1月12日
 */
@Service("individualActivitySerialNumService")
public class IndividualActivitySerialNumServiceImpl implements IndividualActivitySerialNumService{
    @Autowired
    IndividualActivitySerialNumMapper mapper;

    @Override
    public boolean insert(IndividualActivitySerialNum record) {        
        return mapper.insert(record) == 1;
    }

    @Override
    public boolean insertSelective(IndividualActivitySerialNum record) {
        return mapper.insertSelective(record) == 1;
    }

    @Override
    public IndividualActivitySerialNum selectByPrimaryKey(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateByPrimaryKeySelective(IndividualActivitySerialNum record) {
        return mapper.updateByPrimaryKeySelective(record) == 1;
    }

    @Override
    public boolean updateByPrimaryKey(IndividualActivitySerialNum record) {
        return mapper.updateByPrimaryKey(record) == 1;
    }

}
