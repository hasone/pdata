package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.IndividualFlowcoinExchange;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 */
public interface IndividualFlowcoinExchangeService {
    /**
     * @param record
     * @return
     */
    public boolean insert(IndividualFlowcoinExchange record);

    /**
     * @param adminId
     * @param productId
     * @param count
     * @param mobile
     * @return
     */
    public boolean createExchange(Long adminId, Long productId, Integer count, String mobile);

    /**
     * @param id
     * @param status
     * @return
     */
    public boolean updateStatus(Long id, Integer status);

    /**
     * @param map
     * @return
     */
    public int countByMap(Map map);

    /**
     * @param map
     * @return
     */
    public List<IndividualFlowcoinExchange> selectByMap(Map map);

    /**
     * @param systemSerial
     * @return
     */
    public IndividualFlowcoinExchange selectBySystemSerial(String systemSerial);

}
