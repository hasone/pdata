package com.cmcc.vrp.province.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cmcc.vrp.pay.model.PayBillModel;
import com.cmcc.vrp.province.model.YqxPayReconcileRecord;
import com.cmcc.vrp.province.model.YqxPayRecord;

/**
 * YqxPayRecordService
 *
 */
public interface YqxPayRecordService {
    /**
     * 插入
     */
    boolean insert(YqxPayRecord record);
    
    /**
     * 查询
     */
    YqxPayRecord selectByPrimaryKey(Long id);
    
    /**
     * 查询 -平台订单号
     */
    List<YqxPayRecord> selectByOrderSerialNum(String orderSerialNum);
    
    /**
     * 查询 -支付订单号 + 支付流水号
     */
    YqxPayRecord selectByPayIds(String payOrderId,String payTransactionId);
    
    /**
     * 更新
     */
    boolean updateByPrimaryKeySelective(YqxPayRecord record);
    
    /**
     * 得到最新的交易流水号，保证唯一
     */
    String getNewTransactionId();
    
    /**
     * 查询最新的一次成功支付记录 -平台订单号
     */
    YqxPayRecord selectNewestSuccessRecord(String orderSerialNum);

    /** 
     * @Title: selectByTransactionId 
     */
    YqxPayRecord selectByTransactionId(String payTransactionId);
    
    /** 
     * @Title: selectByDoneCode 
     */
    YqxPayRecord selectByDoneCode(String doneCode);

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
     * 支付对账前端
     */
    Integer reconcileCountByMap(Map<String, Object> map);
    
    /**
     * 支付对账前端
     */
    List<YqxPayReconcileRecord> reconcileSelectByMap(Map<String, Object> map);
    
    /**
     * 支付账单前端
     */
    Integer payBillCountByMap(Map<String, Object> map);
    
    /**
     * 支付账单前端
     */
    List<YqxPayReconcileRecord> payBillSelectByMap(Map<String, Object> map);
    
    /**
     * 支付对账,按天获取记录
     * 
     */
    List<YqxPayRecord> reconcileRangeTime(String date);
    
    /**
     * 更新对账信息
     */
    int updateReconcileInfo(YqxPayRecord record);
    
    /**
     * 插入到云企信账单表
     */
    boolean insertYqxBill(PayBillModel model);
    
    /** 
     * 更新支付记录的充值状态
     * @Title: updateChargeStatus 
     */
    boolean updateChargeStatus(Integer chargeStatus, String payTransactionId, Date chargeTime);
}
