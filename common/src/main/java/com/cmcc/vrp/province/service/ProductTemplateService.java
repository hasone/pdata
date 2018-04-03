package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.PlatformProductTemplateMap;
import com.cmcc.vrp.province.model.ProductTemplate;

import java.util.List;
import java.util.Map;

/**
 * 产品模板与企业关系服务类
 * create by qinqinyan on 2017/3/9
 * */
public interface ProductTemplateService {
    /**
     * deleteByPrimaryKey
     * */
    boolean deleteByPrimaryKey(Long id);

    /**
     * insert
     * */
    boolean insert(ProductTemplate record);

    /**
     * insertSelective
     * */
    boolean insertSelective(ProductTemplate record);

    /**
     * selectByPrimaryKey
     * */
    ProductTemplate selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * */
    boolean updateByPrimaryKeySelective(ProductTemplate record);

    /**
     * updateByPrimaryKey
     * */
    boolean updateByPrimaryKey(ProductTemplate record);
    
    /**
     * 获取产品模板列表
     * @param map
     * @author qinqinyan
     * */
    List<ProductTemplate> selectByMap(Map map);
    
    /**
     * 统计产品模板
     * @param map
     * @author qinqinyan
     * */
    long countByMap(Map map);
    
    /**
     * 获取所有产品模板
     * @author qinqinyan
     * */
    List<ProductTemplate> selectAllProductTemplates();
    
    /**
     * 创建产品模板
     * @param productTemplate
     * @param maps
     * @author qinqinyan
     * */
    boolean createProductTemplate(ProductTemplate productTemplate, 
            List<PlatformProductTemplateMap> maps);
    
    /**
     * 编辑产品模板
     * @param productTemplate
     * @param maps
     * @author qinqinyan
     * */
    boolean editProductTemplate(ProductTemplate productTemplate, 
            List<PlatformProductTemplateMap> maps);

    /**
     * 该模板id是否有关联企业
     * @param id
     * @author qinqinyan
     * */
    boolean whetherUseProdTemplate(Long id);

    /**
     * 删除产品模板
     * @param id
     * @author qinqinyan
     * */
    boolean deleteProductTemplate(Long id);

    /**
     * 根据name查找
     * @param name
     * @author qinqinyan
     * */
    List<ProductTemplate> selectByName(String name);

    /**
     * 获取默认产品组
     * @author qinqinyan
     * */
    ProductTemplate getDefaultProductTemplate();

    /**
     * 将企业entId关联到默认产品组
     * @param entId
     * @author qinqinyan
     * */
    boolean relatedDefaultProductTemplate(Long entId);


    //List<PlatformProductTemplateMap> getDefaultProductByTemplate

}
