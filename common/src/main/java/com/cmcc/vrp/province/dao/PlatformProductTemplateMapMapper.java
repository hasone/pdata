package com.cmcc.vrp.province.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.PlatformProductTemplateMap;

/**
 * create by qinqinyan on 2017/3/9
 * */
public interface PlatformProductTemplateMapMapper {
    /**
     * deleteByPrimaryKey
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * insert
     * */
    int insert(PlatformProductTemplateMap record);

    /**
     * insertSelective
     * */
    int insertSelective(PlatformProductTemplateMap record);

    /**
     * selectByPrimaryKey
     * */
    PlatformProductTemplateMap selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * */
    int updateByPrimaryKeySelective(PlatformProductTemplateMap record);

    /**
     * updateByPrimaryKey
     * */
    int updateByPrimaryKey(PlatformProductTemplateMap record);
    
    /**
     * 批量插入
     * */
    int batchInsert(@Param("records")List<PlatformProductTemplateMap> records);
    
    /**
     * 根据模板id查询平台产品与模板的关系
     * */
    List<PlatformProductTemplateMap> selectByTemplateId(@Param("templateId")Long templateId);
    
    /**
     * 批量删除
     * */
    int batchDelete(@Param("records")List<PlatformProductTemplateMap> records);
}