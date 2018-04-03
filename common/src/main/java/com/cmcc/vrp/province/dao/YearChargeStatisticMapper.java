package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.ChargeStatistic;

import java.util.Date;
import java.util.List;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:33:56
*/
public interface YearChargeStatisticMapper {
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

    List<ChargeStatistic> getYearChargeStatistic(Date date);

    /**
     * @param list
     * @return
     */
    int batchInsert(List<ChargeStatistic> list);

}