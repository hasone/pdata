package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.PlatformProductTemplateMap;

/**
 * 平台产品与模板关系服务类
 * create by qinqinyan on 2017/3/9
 * */
public interface PlatformProductTemplateMapService {
    
    /**
     * deleteByPrimaryKey
     * */
    boolean deleteByPrimaryKey(Long id);

    /**
     * insert
     * */
    boolean insert(PlatformProductTemplateMap record);

    /**
     * insertSelective
     * */
    boolean insertSelective(PlatformProductTemplateMap record);

    /**
     * selectByPrimaryKey
     * */
    PlatformProductTemplateMap selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * */
    boolean updateByPrimaryKeySelective(PlatformProductTemplateMap record);

    /**
     * updateByPrimaryKey
     * */
    boolean updateByPrimaryKey(PlatformProductTemplateMap record);
    
    /**
     * 批量插入产品与模板关系
     * @param records
     * @author qinqinyan
     * */
    boolean batchInsert(List<PlatformProductTemplateMap> records);
   
    /**
     * 根据模板Id获取产品模板与平台产品的关系
     * @param templateId
     * @author qinqinyan
     * */
    List<PlatformProductTemplateMap> selectByTemplateId(Long templateId);
    
    /**
     * 批量删除产品与模板关联关系
     * @param records
     * @author qinqinyan
     * */
    boolean batchDelete(List<PlatformProductTemplateMap> records);

}
