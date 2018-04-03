package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.ProductTemplateEnterpriseMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * create by qinqinyan on 2017/3/9
 * */
public interface ProductTemplateEnterpriseMapMapper {
    /**
     * deleteByPrimaryKey
     * */
    int deleteByPrimaryKey(Long id);
    
    /**
     * deleteByEntId
     * */
    int deleteByEntId(@Param("enterpriseId")Long entId);

    /**
     * insert
     * */
    int insert(ProductTemplateEnterpriseMap record);

    /**
     * insertSelective
     * */
    int insertSelective(ProductTemplateEnterpriseMap record);

    /**
     * selectByPrimaryKey
     * */
    ProductTemplateEnterpriseMap selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * */
    int updateByPrimaryKeySelective(ProductTemplateEnterpriseMap record);

    /**
     * updateByPrimaryKey
     * */
    int updateByPrimaryKey(ProductTemplateEnterpriseMap record);

    /**
     * selectByProductTemplateId
     * 根据模板id查找
     * */
    List<ProductTemplateEnterpriseMap> selectByProductTemplateId(@Param("productTemplateId") Long productTemplateId);

    /**
     * 根据产品模板id删除
     * @param productTemplateId
     * @author qinqinyan
     * */
    int deleteByProductTemplateId(@Param("productTemplateId") Long productTemplateId);

    /**
     * 根据企业id查找
     * @param entId
     * @author qinqinyan
     * */
    ProductTemplateEnterpriseMap selectByEntId(@Param("entId") Long entId);
}