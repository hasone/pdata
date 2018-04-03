package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.ApprovalDetailDefinition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ApprovalDetailDefinitionMapper.java
 */

public interface ApprovalDetailDefinitionMapper {
    /** 
     * @Title: deleteByPrimaryKey 
     */
    int deleteByPrimaryKey(Long id);

    /** 
     * @Title: insert 
     */
    int insert(ApprovalDetailDefinition record);

    /** 
     * @Title: insertSelective 
     */
    int insertSelective(ApprovalDetailDefinition record);

    /** 
     * @Title: selectByPrimaryKey 
     */
    ApprovalDetailDefinition selectByPrimaryKey(Long id);

    /** 
     * @Title: updateByPrimaryKeySelective 
     */
    int updateByPrimaryKeySelective(ApprovalDetailDefinition record);

    /** 
     * @Title: updateByPrimaryKey 
     */
    int updateByPrimaryKey(ApprovalDetailDefinition record);

    /** 
     * @Title: getByApprovalProcessId 
     */
    List<ApprovalDetailDefinition> getByApprovalProcessId(@Param("processId") Long id);

    /** 
     * @Title: deleteByApprovalProcess 
     */
    int deleteByApprovalProcess(@Param("processId") Long id);

    /** 
     * @Title: batchInsert 
     */
    int batchInsert(@Param("approvalDetails") List<ApprovalDetailDefinition> approvalDetails);
}