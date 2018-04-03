package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.EnterpriseMapper;
import com.cmcc.vrp.province.dao.ProductTemplateEnterpriseMapMapper;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.ProductTemplateEnterpriseMap;
import com.cmcc.vrp.province.service.ProductTemplateEnterpriseMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("productTemplateEnterpriseMapService")
public class ProductTemplateEnterpriseMapServiceImpl implements ProductTemplateEnterpriseMapService{
    private static final Logger logger = LoggerFactory.getLogger(ProductTemplateEnterpriseMapServiceImpl.class);
    
    @Autowired
    ProductTemplateEnterpriseMapMapper mapper;
    @Autowired
    EnterpriseMapper enterpriseMapper;
    
    @Override
    public boolean deleteByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        if(id!=null){
            return mapper.deleteByPrimaryKey(id)>=0;
        }
        return false;
    }

    @Override
    public boolean deleteByEntId(Long entId) {
        // TODO Auto-generated method stub
        if(entId!=null){
            return mapper.deleteByEntId(entId)>=0;
        }
        return false;
    }



    @Override
    public boolean insert(ProductTemplateEnterpriseMap record) {
        // TODO Auto-generated method stub
        if(record!=null){
            return mapper.insert(record)==1;
        }
        return false;
    }

    @Override
    public boolean insertSelective(ProductTemplateEnterpriseMap record) {
        // TODO Auto-generated method stub
        if(record!=null){
            return mapper.insertSelective(record)==1;
        }
        return false;
    }

    @Override
    public ProductTemplateEnterpriseMap selectByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        if(id!=null){
            return mapper.selectByPrimaryKey(id);
        }
        return null;
    }

    @Override
    public boolean updateByPrimaryKeySelective(ProductTemplateEnterpriseMap record) {
        // TODO Auto-generated method stub
        if(record!=null){
            return mapper.updateByPrimaryKeySelective(record)==1;
        }
        return false;
    }

    @Override
    public boolean updateByPrimaryKey(ProductTemplateEnterpriseMap record) {
        // TODO Auto-generated method stub
        if(record!=null){
            return mapper.updateByPrimaryKey(record)==1;
        }
        return false;
    }

    @Override
    public List<Enterprise> selectRelatedEnterprises(Map map) {
        // TODO Auto-generated method stub
        return enterpriseMapper.selectRelatedEnterprises(map);
    }

    @Override
    public int countRelatedEnterprises(Map map) {
        // TODO Auto-generated method stub
        return enterpriseMapper.countRelatedEnterprises(map);
    }

    @Override
    public List<ProductTemplateEnterpriseMap> selectByProductTemplateId(Long productTemplateId) {
        if(productTemplateId!=null){
            return mapper.selectByProductTemplateId(productTemplateId);
        }
        return null;
    }

    @Override
    public boolean deleteByProductTemplateId(Long productTemplateId) {
        return mapper.deleteByProductTemplateId(productTemplateId)>=0;
    }

    @Override
    public ProductTemplateEnterpriseMap selectByEntId(Long entId) {
        if(entId!=null){
            return mapper.selectByEntId(entId);
        }
        return null;
    }
}
