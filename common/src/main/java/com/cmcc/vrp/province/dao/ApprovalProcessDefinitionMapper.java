package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * ApprovalProcessDefinitionMapper.java
 */
public interface ApprovalProcessDefinitionMapper {
    
    /** 
     * 逻辑删除
     * @Title: deleteByPrimaryKey 
     */
    int deleteByPrimaryKey(Long id);

    /** 
     * @Title: insert 
     */
    int insert(ApprovalProcessDefinition record);

    /** 
     * @Title: selectByPrimaryKey 
     */
    ApprovalProcessDefinition selectByPrimaryKey(Long id);

    /** 
     * @Title: updateByPrimaryKey 
     */
    int updateByPrimaryKey(ApprovalProcessDefinition record);

    /**
     * 查新审批列表和个数
     */
    int countApprovalProcess(Map<String, Object> map);

    /** 
     * @Title: queryApprovalProcessList 
     */
    List<ApprovalProcessDefinition> queryApprovalProcessList(Map<String, Object> map);

    /** 
     * @Title: getApprovalProcessById 
     */
    ApprovalProcessDefinition getApprovalProcessById(@Param("id") Long id);

    /** 
     * @Title: selectByType 
     */
    ApprovalProcessDefinition selectByType(@Param("type") Integer type);

    /** 
     * @Title: selectApprovalProcessesByType 
     */
    List<ApprovalProcessDefinition> selectApprovalProcessesByType(@Param("type") Integer type);
}