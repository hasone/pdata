package com.cmcc.vrp.province.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.PoolUsedStatistic;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:30:39
*/
public interface PoolUsedStatisticMapper {
    
    /**
     * @param id
     * @return
     */
    PoolUsedStatistic selectByPrimaryKey(Long id);
    
    /**
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    int insertSelective(PoolUsedStatistic record);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(PoolUsedStatistic record);

    /**
     * 跟据起始时间和结束时间，计算手机号使用的总额 startTime 和 endTime的格式为YYYYMMDD
     */
    List<PoolUsedStatistic> getStatisticByTime(
            @Param("startTime") String startTime,
            @Param("endTime") String endTime);
    
    /**
     * 删除某一天的所有记录
     */
    int deleteByDate(String date);

}