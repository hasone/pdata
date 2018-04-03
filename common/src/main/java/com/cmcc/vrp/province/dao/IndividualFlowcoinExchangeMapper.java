package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.IndividualFlowcoinExchange;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:25:48
*/
public interface IndividualFlowcoinExchangeMapper {
    /**
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    int insert(IndividualFlowcoinExchange record);

    /**
     * @param record
     * @return
     */
    int insertSelective(IndividualFlowcoinExchange record);

    /**
     * @param id
     * @return
     */
    IndividualFlowcoinExchange selectByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(IndividualFlowcoinExchange record);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKey(IndividualFlowcoinExchange record);

    /**
     * @param id
     * @param status
     * @return
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * @param map
     * @return
     */
    int countByMap(Map map);

    /**
     * @param map
     * @return
     */
    List<IndividualFlowcoinExchange> selectByMap(Map map);

    /**
     * @param systemSerial
     * @return
     */
    IndividualFlowcoinExchange selectBySystemSerial(String systemSerial);
}