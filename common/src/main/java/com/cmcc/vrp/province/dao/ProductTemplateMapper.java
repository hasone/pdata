package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.ProductTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * create by qinqinyan on 2017/3/9
 * */
public interface ProductTemplateMapper {
    
    /**
     * deleteByPrimaryKey
     * */
    int deleteByPrimaryKey(@Param("id") Long id);

    /**
     * insert
     * */
    int insert(ProductTemplate record);

    /**
     * insertSelective
     * */
    int insertSelective(ProductTemplate record);

    /**
     * selectByPrimaryKey
     * */
    ProductTemplate selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * */
    int updateByPrimaryKeySelective(ProductTemplate record);

    /**
     * updateByPrimaryKey
     * */
    int updateByPrimaryKey(ProductTemplate record);
    
    /**
     * selectByMap
     * */
    List<ProductTemplate> selectByMap(Map map);
    
    /**
     * countByMap
     * */
    long countByMap(Map map);
    
    /**
     * selectAllProductTemplates
     * */
    List<ProductTemplate> selectAllProductTemplates();

    /**
     * selectByName
     * */
    List<ProductTemplate> selectByName(@Param("name") String name);
    /**
     * getDefaultProductTemplate
     * */
    List<ProductTemplate> getDefaultProductTemplate();
    
}