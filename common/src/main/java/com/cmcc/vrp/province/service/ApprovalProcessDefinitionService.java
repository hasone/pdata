package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.ApprovalDetailDefinition;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.util.QueryObject;

import java.util.List;

/**
 * 审核流程服务类
 *
 * @author qinqinyan
 */
public interface ApprovalProcessDefinitionService {
    /**
     * @param id
     * @return
     */
    boolean deleteByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    boolean insert(ApprovalProcessDefinition record);

    /**
     * @param id
     * @return
     */
    ApprovalProcessDefinition selectByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    boolean updateByPrimaryKey(ApprovalProcessDefinition record);

    //获取审批流程列表和数量
    /**
     * @param queryObject
     * @return
     */
    int countApprovalProcess(QueryObject queryObject);

    /**
     * @param queryObject
     * @return
     */
    List<ApprovalProcessDefinition> queryApprovalProcessList(QueryObject queryObject);

    //获取记录
    ApprovalProcessDefinition getApprovalProcessById(Long id);

    //删除审批流程
    /**
     * @param id
     * @return
     */
    boolean deleteApprovalProcess(Long id);

    //插入审批流程
    /**
     * @param approvalProcess
     * @return
     */
    boolean insertApprovalProcess(ApprovalProcessDefinition approvalProcess);

    /**
     * @param approvalProcess
     * @param approvalDetails
     * @return
     */
    boolean insertProcessAndDetails(ApprovalProcessDefinition approvalProcess, List<ApprovalDetailDefinition> approvalDetails);

    //更新审批流程
    /**
     * @param approvalProcess
     * @return
     */
    boolean updateApprovalProcess(ApprovalProcessDefinition approvalProcess);

    /**
     * 根据流程类型获取当前使用的审批流程
     * @param type
     * @author qinqinyan
     * */
    ApprovalProcessDefinition selectByType(Integer type);

    /**
     * 根据流程类型获取所有该类型的审批流程
     * @param type 流程类型
     * @author qinqinyan
     * */
    List<ApprovalProcessDefinition> selectApprovalProcessesByType(Integer type);

    //判断是否有发起审核流程的权限
    /**
     * @param roleId
     * @param type
     * @return
     */
    Boolean hasAuthToSubmitApproval(Long roleId, Integer type);

    /**
     * 判断是否需要审核
     * @param approvalType 审核类型  0,企业开户；1，产品变更；2，账户变更；3，营销活动
     * @author qinqinyan
     * */
    boolean wheatherNeedApproval(Integer approvalType);



}
