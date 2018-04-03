package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.YqxRefundRecord;
import com.cmcc.vrp.util.QueryObject;

/**
 * 
 * YqxRefundRecordService
 *
 */
public interface YqxRefundRecordService {
    /**
     * 插入,返回主键
     */
    boolean insert(YqxRefundRecord record);
    
    /**
     * selectByPrimaryKey(Long id)
     */
    YqxRefundRecord selectByPrimaryKey(Long id);
    
    /**
     * updateByPrimaryKeySelective(YqxRefundRecord record)
     */
    boolean updateByPrimaryKeySelective(YqxRefundRecord record);
    
    /**
     * updateByDoneCodeAcceptedRecord
     * 根据DoneCode和status为1的记录更新数据库,只有异步回调时使用
     * 
     */
    boolean updateByDoneCodeAcceptedRecord(String doneCode,int status,String msg);
    
    /**
     * 分页
     */
    List<YqxRefundRecord> queryPaginationRefundList(QueryObject queryObject);
    
    /**
     * 分页
     */
    int queryPaginationRefundCount(QueryObject queryObject);

    /** 
     * @Title: selectByRefundSerialNum 
     */
    YqxRefundRecord selectByRefundSerialNum(String refundSerialNum);
    
    /** 
     * @Title: selectByDoneCodeAndStatus 
     */
    List<YqxRefundRecord> selectByDoneCodeAndStatus(String doneCode, String statusStr);
    
}
