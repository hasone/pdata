package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.ProductTemplateEnterpriseMap;

import java.util.List;
import java.util.Map;

/**
 * 产品模板与企业关系服务类
 * create by qinqinyan on 2017/3/9
 * */
public interface ProductTemplateEnterpriseMapService {
    
    /**
     * deleteByPrimaryKey
     * */
    boolean deleteByPrimaryKey(Long id);
    
    /**
     * 根据企业id删除产品模板和企业关联关系
     * @param entId
     * @author qinqinyan
     * */
    boolean deleteByEntId(Long entId);

    /**
     * insert
     * */
    boolean insert(ProductTemplateEnterpriseMap record);

    /**
     * insertSelective
     * */
    boolean insertSelective(ProductTemplateEnterpriseMap record);

    /**
     * selectByPrimaryKey
     * */
    ProductTemplateEnterpriseMap selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * */
    boolean updateByPrimaryKeySelective(ProductTemplateEnterpriseMap record);

    /**
     * updateByPrimaryKey
     * */
    boolean updateByPrimaryKey(ProductTemplateEnterpriseMap record);
    
    /**
     * 根据产品模板ID查询企业信息
     * @param map
     * @author qinqinyan
     * */
    List<Enterprise> selectRelatedEnterprises(Map map);
    /**
     * 根据产品模板ID查询企业信息
     * @param map
     * @author qinqinyan
     * */
    int countRelatedEnterprises(Map map);

    /**
     * 根据productTemplateId查找产品模板与模板Id的关联关系
     * @param productTemplateId
     * @author qinqinyan
     * */
    List<ProductTemplateEnterpriseMap> selectByProductTemplateId(Long productTemplateId);

    /**
     * 根据产品模板id删除产品模板与模板id的关联关系
     * @param productTemplateId
     * @author qinqinyan
     * */
    boolean deleteByProductTemplateId(Long productTemplateId);

    /**
     * 根据企业id查找企业与产品模板的关联关系
     * @param entId
     * @author qinqinyan
     * */
    ProductTemplateEnterpriseMap selectByEntId(Long entId);
}
