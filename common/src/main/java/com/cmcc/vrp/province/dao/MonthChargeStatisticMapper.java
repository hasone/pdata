package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.ChargeStatistic;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:27:59
*/
public interface MonthChargeStatisticMapper {
    /**
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    int insert(ChargeStatistic record);

    /**
     * @param record
     * @return
     */
    int insertSelective(ChargeStatistic record);

    /**
     * @param id
     * @return
     */
    ChargeStatistic selectByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ChargeStatistic record);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKey(ChargeStatistic record);

    /**
     * @param list
     * @return
     */
    int batchInsert(@Param("records") List<ChargeStatistic> list);

    List<ChargeStatistic> getMonthChargeStatistic(Date date);

}