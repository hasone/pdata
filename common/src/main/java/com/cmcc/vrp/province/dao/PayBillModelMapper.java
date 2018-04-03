package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.pay.model.PayBillModel;
import com.cmcc.vrp.province.model.YqxPayReconcileRecord;

/**
 * PayBillModelMapper
 *
 */
public interface PayBillModelMapper {

    /**
     * insertSelective
     */
    int insertSelective(PayBillModel record);

    /**
     * selectByPrimaryKey
     */
    PayBillModel selectByPrimaryKey(Long id);
    
    /**
     * reconcileRecords
     */
    List<YqxPayReconcileRecord> billRecords(Map<String, Object> map);
    
    /**
     * reconcileCount
     */
    Integer billCount(Map<String, Object> map);
    

    
}