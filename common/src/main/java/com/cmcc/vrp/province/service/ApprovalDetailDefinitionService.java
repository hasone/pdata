package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.ApprovalDetailDefinition;

import java.util.List;

/**
 * 审批流程详情定义服务类
 * @author qinqinyan
 * */
public interface ApprovalDetailDefinitionService {

    /**
     * @param record
     * @return
     */
    boolean updateByPrimaryKey(ApprovalDetailDefinition record);

    /**
     * 获取审批流程信息
     */
    List<ApprovalDetailDefinition> getByApprovalProcessId(Long id);

    /**
     * @param id
     * @return
     */
    boolean deleteByApprovalProcess(Long id);

    /**
     * 批量插入
     */
    boolean batchInsert(List<ApprovalDetailDefinition> approvalDetails);

    //获取当前审批层级详情
    ApprovalDetailDefinition getCurrentApprovalDetailByProccessId(Long processId, Integer currentStatus);

    //获取下一审批层级详情
    ApprovalDetailDefinition getNextApprovalDetailByProccessId(Long processId, Integer currentStatus);

    //获取最后一个审批层级
    ApprovalDetailDefinition getLastApprovalDetailByProccessId(Long processId);


}
