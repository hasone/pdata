package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.ChargeStatistic;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * DayChargeStatisticMapper.java
 */
public interface DayChargeStatisticMapper {
    /** 
     * @Title: deleteByPrimaryKey 
     */
    int deleteByPrimaryKey(Long id);

    /** 
     * @Title: insert 
     */
    int insert(ChargeStatistic record);

    /** 
     * @Title: insertSelective 
     */
    int insertSelective(ChargeStatistic record);

    /** 
     * @Title: selectByPrimaryKey 
     */
    ChargeStatistic selectByPrimaryKey(Long id);

    /** 
     * @Title: updateByPrimaryKeySelective 
     */
    int updateByPrimaryKeySelective(ChargeStatistic record);

    /** 
     * @Title: updateByPrimaryKey 
     */
    int updateByPrimaryKey(ChargeStatistic record);

    /** 
     * @Title: batchInsert 
     */
    int batchInsert(@Param("records") List<ChargeStatistic> records);

    /** 
     * @Title: getDayChargeStatistic 
     */
    List<ChargeStatistic> getDayChargeStatistic(Date date);
}