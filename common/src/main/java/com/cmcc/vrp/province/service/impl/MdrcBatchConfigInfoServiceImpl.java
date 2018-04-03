package com.cmcc.vrp.province.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.MdrcBatchConfigInfoMapper;
import com.cmcc.vrp.province.model.MdrcBatchConfigInfo;
import com.cmcc.vrp.province.service.MdrcBatchConfigInfoService;

/**
 * 营销模板扩展信息
 * */
@Service("MdrcBatchConfigInfoService")
public class MdrcBatchConfigInfoServiceImpl implements MdrcBatchConfigInfoService{
    @Autowired
    MdrcBatchConfigInfoMapper mapper;
    
    @Override
    public boolean deleteByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        return mapper.deleteByPrimaryKey(id)==1;
    }

    @Override
    public boolean insertSelective(MdrcBatchConfigInfo record) {
        // TODO Auto-generated method stub
        return mapper.insertSelective(record)==1;
    }

    @Override
    public MdrcBatchConfigInfo selectByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateByPrimaryKeySelective(MdrcBatchConfigInfo record) {
        // TODO Auto-generated method stub
        return mapper.updateByPrimaryKeySelective(record)==1;
    }

    @Override
    public boolean updateByPrimaryKey(MdrcBatchConfigInfo record) {
        // TODO Auto-generated method stub
        return mapper.updateByPrimaryKey(record)==1;
    }

}
