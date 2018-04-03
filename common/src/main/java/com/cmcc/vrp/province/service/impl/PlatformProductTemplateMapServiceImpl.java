package com.cmcc.vrp.province.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.PlatformProductTemplateMapMapper;
import com.cmcc.vrp.province.model.PlatformProductTemplateMap;
import com.cmcc.vrp.province.service.PlatformProductTemplateMapService;

@Service("platformProductTemplateMapService")
public class PlatformProductTemplateMapServiceImpl implements PlatformProductTemplateMapService{

    private static final Logger logger = LoggerFactory.getLogger(ProductTemplateEnterpriseMapServiceImpl.class);
    
    @Autowired
    PlatformProductTemplateMapMapper mapper;
    
    @Override
    public boolean deleteByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        if(id!=null){
            return mapper.deleteByPrimaryKey(id)>=0;
        }
        return false;
    }

    @Override
    public boolean insert(PlatformProductTemplateMap record) {
        // TODO Auto-generated method stub
        if(record!=null){
            return mapper.insert(record)==1;
        }
        return false;
    }

    @Override
    public boolean insertSelective(PlatformProductTemplateMap record) {
        // TODO Auto-generated method stub
        if(record!=null){
            return mapper.insertSelective(record)==1;
        }
        return false;
    }

    @Override
    public PlatformProductTemplateMap selectByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        if(id!=null){
            return mapper.selectByPrimaryKey(id);
        }
        return null;
    }

    @Override
    public boolean updateByPrimaryKeySelective(PlatformProductTemplateMap record) {
        // TODO Auto-generated method stub
        if(record!=null){
            return mapper.updateByPrimaryKeySelective(record)==1;
        }
        return false;
    }

    @Override
    public boolean updateByPrimaryKey(PlatformProductTemplateMap record) {
        // TODO Auto-generated method stub
        if(record!=null){
            return mapper.updateByPrimaryKey(record)==1;
        }
        return false;
    }

    @Override
    public boolean batchInsert(List<PlatformProductTemplateMap> records) {
        // TODO Auto-generated method stub
        if(records!=null && records.size()>0){
            return mapper.batchInsert(records)==records.size();
        }
        return false;
    }

    @Override
    public List<PlatformProductTemplateMap> selectByTemplateId(Long templateId) {
        // TODO Auto-generated method stub
        if(templateId!=null){
            return mapper.selectByTemplateId(templateId);
        }
        return null;
    }

    @Override
    public boolean batchDelete(List<PlatformProductTemplateMap> records) {
        // TODO Auto-generated method stub
        if(records!=null && records.size()>0){
            return mapper.batchDelete(records)==records.size();
        }
        return false;
    }
    
}
