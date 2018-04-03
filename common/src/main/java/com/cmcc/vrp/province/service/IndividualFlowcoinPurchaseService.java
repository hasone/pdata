package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.IndividualFlowcoinPurchase;

import java.util.List;
import java.util.Map;

/**
* <p>Title: </p>
* <p>Description: </p>
*/
public interface IndividualFlowcoinPurchaseService {
    /**
     * @param record
     * @return
     */
    public boolean insert(IndividualFlowcoinPurchase record);

    /**
     * @param record
     * @return
     */
    public boolean saveFlowcoinPurchase(IndividualFlowcoinPurchase record);

    /**
     * @param map
     * @return
     */
    public int countByMap(Map map);

    /**
     * @param map
     * @return
     */
    public List<IndividualFlowcoinPurchase> selectByMap(Map map);

    /**
     * @param id
     * @return
     */
    public IndividualFlowcoinPurchase selectByPrimaryKey(Long id);

    /**
     * @param systemSerial
     * @return
     */
    public IndividualFlowcoinPurchase selectBySystemSerial(String systemSerial);

    /**
     * @param adminId
     * @param systemSerial
     * @return
     */
    public boolean pay(Long adminId, String systemSerial);

    /**
     * @param syetemSerial
     * @param status
     * @return
     */
    boolean updateStatus(String syetemSerial, Integer status);

    /**
     * @param record
     * @return
     */
    boolean updateBySystemSerial(IndividualFlowcoinPurchase record);

}
