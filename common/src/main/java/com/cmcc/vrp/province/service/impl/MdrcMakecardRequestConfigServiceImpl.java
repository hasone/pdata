package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.MdrcMakecardRequestConfigMapper;
import com.cmcc.vrp.province.model.MdrcMakecardRequestConfig;
import com.cmcc.vrp.province.service.MdrcMakecardRequestConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by qinqinyan on 2016/12/1.
 */
@Service("mdrcMakecardRequestConfigService")
public class MdrcMakecardRequestConfigServiceImpl implements MdrcMakecardRequestConfigService{

    @Autowired
    MdrcMakecardRequestConfigMapper mapper;

    @Override
    public boolean deleteByPrimaryKey(Long id) {
        if(id!=null){
            return mapper.deleteByPrimaryKey(id)==1;
        }
        return false;
    }

    @Override
    public boolean insert(MdrcMakecardRequestConfig record) {
        if(record!=null){
            return mapper.insert(record)==1;
        }
        return false;
    }

    @Override
    public boolean insertSelective(MdrcMakecardRequestConfig record) {
        if(record!=null){
            return mapper.insertSelective(record)==1;
        }
        return false;
    }

    @Override
    public MdrcMakecardRequestConfig selectByPrimaryKey(Long id) {
        if(id!=null){
            return mapper.selectByPrimaryKey(id);
        }
        return null;
    }

    @Override
    public boolean updateByPrimaryKeySelective(MdrcMakecardRequestConfig record) {
        if(record!=null){
            return mapper.updateByPrimaryKeySelective(record)==1;
        }
        return false;
    }

    @Override
    public boolean updateByPrimaryKey(MdrcMakecardRequestConfig record) {
        if(record!=null){
            return mapper.updateByPrimaryKey(record)==1;
        }
        return false;
    }

    @Override
    public MdrcMakecardRequestConfig selectByRequestId(Long requestId) {
        if(requestId!=null){
            return mapper.selectByRequestId(requestId);
        }
        return null;
    }

    @Override
    public MdrcMakecardRequestConfig selectByConfigId(Long configId) {
        // TODO Auto-generated method stub
        if(configId!=null){
            return mapper.selectByConfigId(configId);
        }
        return null;
    }
    
    
}
