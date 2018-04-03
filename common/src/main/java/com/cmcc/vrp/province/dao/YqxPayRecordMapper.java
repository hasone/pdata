package com.cmcc.vrp.province.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.YqxPayReconcileRecord;
import com.cmcc.vrp.province.model.YqxPayRecord;

/**
 * 
 * YqxPayRecordMapper
 *
 */
public interface YqxPayRecordMapper {

    /**
     * 插入
     */
    int insertSelective(YqxPayRecord record);

    /**
     * 主键查询
     */
    YqxPayRecord selectByPrimaryKey(Long id);
    
    /**
     * 平台序列号
     */
    List<YqxPayRecord> selectByOrderSerialNum(@Param("orderSerialNum")String orderSerialNum);
    
    /**
     * 支付序列号 + 支付流水号
     */
    List<YqxPayRecord> selectByPayIds(@Param("orderId")String orderId,@Param("transactionId")String transactionId);

    /**
     * 更新
     */
    int updateByPrimaryKeySelective(YqxPayRecord record);

    /** 
     * @Title: selectNewestSuccessRecord 
     */
    YqxPayRecord selectNewestSuccessRecord(@Param("orderSerialNum")String orderSerialNum);

    /** 
     * @Title: selectByTransactionId 
     */
    YqxPayRecord selectByTransactionId(String payTransactionId);
    
    /** 
     * @Title: selectByDoneCode 
     */
    List<YqxPayRecord> selectByDoneCode(@Param("doneCode")String doneCode);

    /** 
     * @Title: countRepeatPayByMap 
     */
    Integer countRepeatPayByMap(Map<String, Object> map);

    /** 
     * @Title: selectRepeatPayByMap 
     */
    List<YqxPayRecord> selectRepeatPayByMap(Map<String, Object> map);

    /** 
     * @Title: countByMap 
     */
    Integer countByMap(Map<String, Object> map);

    /** 
     * @Title: selectByMap 
     */
    List<YqxPayRecord> selectByMap(Map<String, Object> map);
    
    /**
     * reconcileRecords
     */
    List<YqxPayReconcileRecord> reconcileRecords(Map<String, Object> map);
    
    /**
     * reconcileCount
     */
    Integer reconcileCount(Map<String, Object> map);
    
    /**
     * reconcileRangeTime
     */
    List<YqxPayRecord> reconcileRangeTime(Map<String, Object> map);
    
    /**
     * updateReconcileInfo
     */
    int updateReconcileInfo(YqxPayRecord record);

    /** 
     * @Title: updateChargeStatus 
     */
    int updateChargeStatus(@Param("chargeStatus")Integer chargeStatus, @Param("payTransactionId")String payTransactionId, @Param("chargeTime") Date chargeTime);
    
}