package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.ApprovalRecord;

/**
 * ApprovalRecordMapper.java
 */
public interface ApprovalRecordMapper {
    /** 
     * @Title: deleteByPrimaryKey 
     */
    int deleteByPrimaryKey(Long id);

    /** 
     * @Title: insert 
     */
    int insert(ApprovalRecord record);

    /** 
     * @Title: insertSelective 
     */
    int insertSelective(ApprovalRecord record);

    /** 
     * @Title: selectByPrimaryKey 
     */
    ApprovalRecord selectByPrimaryKey(Long id);

    /** 
     * @Title: updateByPrimaryKey 
     */
    int updateByPrimaryKey(ApprovalRecord record);

    /** 
     * @Title: selectByRequestId 
     */
    List<ApprovalRecord> selectByRequestId(@Param("requestId") Long requestId);

    /** 
     * @Title: selectByRequestIdAll 
     */
    List<ApprovalRecord> selectByRequestIdAll(@Param("requestId") Long requestId);

    /** 
     * @Title: updateApprovalRecord 
     */
    int updateApprovalRecord(ApprovalRecord record);

    //List<ApprovalRecord> selectByApprovalRequests(@Param("approvalRequests")List<ApprovalRequest> approvalRequests);

    /** 
     * @Title: selectByMap 
     */
    List<ApprovalRecord> selectByMap(Map map);

    /** 
     * @Title: selectNewRecordByRequestId 
     */
    ApprovalRecord selectNewRecordByRequestId(@Param("requestId") Long requestId, @Param("currUserId") Long currUserId);

    /**
     * 
     * @Title: getRecords 
     * @Description: TODO
     * @param map
     * @return
     * @return: List<ApprovalRecord>
     */
    List<ApprovalRecord> getRecords(Map map);

    /**
     * 
     * @Title: countRecords 
     * @Description: TODO
     * @param map
     * @return
     * @return: Long
     */
    Long countRecords(Map map);
}