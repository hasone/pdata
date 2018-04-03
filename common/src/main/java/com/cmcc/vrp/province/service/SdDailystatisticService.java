package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.SdDailystatistic;

/**
 * 
 * SdDailystatisticService 山东每日对账统计接口
 *
 */
public interface SdDailystatisticService {
    /**
     * insert 插入
     * @param dailystatistic
     * @return
     */
    boolean insert(SdDailystatistic dailystatistic);
    
    /**
     * update 更新
     * @param dailystatistic
     * @return
     */
    boolean update(SdDailystatistic dailystatistic);
    
    /**
     * selectByDate 按日期查询
     * @param date 格式 20160616
     * @return
     */
    List<SdDailystatistic> selectByDate(String date);
    
    /**
     * seleceByKey 按主键查询
     * @param id
     * @return
     */
    SdDailystatistic seleceByKey(Long id);
}
