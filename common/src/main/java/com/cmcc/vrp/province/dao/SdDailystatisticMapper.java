package com.cmcc.vrp.province.dao;

import java.util.List;

import com.cmcc.vrp.province.model.SdDailystatistic;


/**
 * 山东每日统计mapper
 *
 */
public interface SdDailystatisticMapper {
    /**
     * insertSelective
     */
    int insertSelective(SdDailystatistic record);
    
    /**
     * updateByPrimaryKeySelective
     */
    int updateByPrimaryKeySelective(SdDailystatistic record);
    
    /**
     * selectByPrimaryKey
     */
    SdDailystatistic selectByPrimaryKey(Long id);
    
    /**
     * 按照date日期查找
     */
    List<SdDailystatistic> selectByDate(String date);
}
