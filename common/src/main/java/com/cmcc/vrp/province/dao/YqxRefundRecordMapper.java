package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.YqxRefundRecord;

/**
 * 
 * YqxRefundRecordMapper
 *
 */
public interface YqxRefundRecordMapper {
    /**
     * insertSelective
     * useGeneratedKeys
     */
    int insertSelective(YqxRefundRecord record);

    /**
     * selectByPrimaryKey
     */
    YqxRefundRecord selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     */
    int updateByPrimaryKeySelective(YqxRefundRecord record);
    
    /**
     * 
     * updateByDoneCodeAcceptedRecord
     * 根据DoneCode和status为1的记录更新数据库,只有异步回调时使用
     * 
     */
    int updateByDoneCodeAcceptedRecord(@Param("doneCode") String doneCode,@Param("status") int status,@Param("msg")String msg);
    
    /** 
     * @Title: queryPaginationRefundList 
     */
    List<YqxRefundRecord> queryPaginationRefundList(Map<String, Object> map);
    
    /** 
     * @Title: queryPaginationRefundCount 
     */
    int queryPaginationRefundCount(Map<String, Object> map);

    /** 
     * @Title: selectByRefundSerialNum 
     */
    YqxRefundRecord selectByRefundSerialNum(String refundSerialNum);

    /** 
     * @Title: selectByDoneCodeAndStatus 
     */
    List<YqxRefundRecord> selectByDoneCodeAndStatus(@Param("doneCode")String doneCode, @Param("statusStr")String statusStr);
    
}