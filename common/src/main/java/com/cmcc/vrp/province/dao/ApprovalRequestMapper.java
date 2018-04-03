package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.SdAccApprovalRequest;

/**
 * <p>Title:ApprovalRequestMapper </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年10月18日
*/
public interface ApprovalRequestMapper {
    /** 
     * @Title: deleteByPrimaryKey 
     */
    int deleteByPrimaryKey(Long id);

    /** 
     * @Title: insert 
     */
    int insert(ApprovalRequest record);

    /** 
     * @Title: insertSelective 
     */
    int insertSelective(ApprovalRequest record);

    /** 
     * @Title: selectByPrimaryKey 
     */
    ApprovalRequest selectByPrimaryKey(Long id);

    /** 
     * @Title: updateApprovalRequest 
     */
    int updateApprovalRequest(ApprovalRequest record);

    /** 
     * @Title: updateByPrimaryKey 
     */
    int updateByPrimaryKey(ApprovalRequest record);

    /** 
     * @Title: getApprovalRequestList 
     */
    List<ApprovalRequest> getApprovalRequestList(@Param("enterprises") List<Enterprise> enterprises, @Param("processId") Long processId,
                                                 @Param("status") Integer preconditon);

    /** 
     * @Title: countApprovalRequest 
     */
    Long countApprovalRequest(@Param("enterprises") List<Enterprise> enterprises, @Param("processId") Long processId,
                              @Param("status") Integer preconditon);

    /**
     * 获取审核记录（企业开户、产品审核、充值审核）
     */
    List<ApprovalRequest> getApprovalRequestListByMap(Map map);

    /**
     * @Title:countApprovalRequestByMap
     * */
    Long countApprovalRequestByMap(Map map);

    /**
     * 获取审核记录（活动审核）
     */
    List<ApprovalRequest> getActivityApprovalRequestsByMap(Map map);

    /** 
     * @Title: countActivityApprovalRequestsByMap 
     */
    Long countActivityApprovalRequestsByMap(Map map);

    /** 
     * @Title: selectByEntIdAndProcessId 
     */
    List<ApprovalRequest> selectByEntIdAndProcessId(@Param("entId") Long entId, @Param("processId") Long processId);

    /**
     * @Title:queryApprovalRequestsForAccountChange
     * */
    List<ApprovalRequest> queryApprovalRequestsForAccountChange(Map map);

    /**
     * @Title:countApprovalRequestsForAccountChange
     * */
    Long countApprovalRequestsForAccountChange(Map map);

    /**
     * 根据活动id获取审批请求
     */
    List<ApprovalRequest> selectByActivityId(@Param("activityId") String activityId);

    /**
     * 查找企业变更审核记录
     * */
    List<ApprovalRequest> queryForEntChange(Map map);
    /**
     * @Title:countForEntChange
     * */
    Long countForEntChange(Map map);
    
    /**
     * 获取ec变更审核记录分页
     * */
    List<ApprovalRequest> queryApprovalRequestsForEcChange(Map map);
    
    /** 
     * @Title: countApprovalRequestsForEcChange 
     */
    Long countApprovalRequestsForEcChange(Map map);
    
    /**
     * 根据审批类型和企业ID获取记录，并安装创建时间的倒序排序
     * @Title: selectByEntIdAndProcessType 
     * @param map
     * @return
     * @Author: wujiamin
     * @date 2016年10月25日
     */
    List<ApprovalRequest> selectByEntIdAndProcessType(Map map);

    /**
     * 营销模板激活审批请求列表分页
     * @param map
     * @author qinqinyan
     * */
    List<ApprovalRequest> getApprovalRequestForMdrcActive(Map map);
    /**
     * @Title:countApprovalRequestForMdrcActive
     * */
    Long countApprovalRequestForMdrcActive(Map map);

    /**
     * @Title:getApprovalRequestForMdrcCardmake
     * 获取制卡请求审批列表
     * @param map
     * @author qinqinyan
     * */
    List<ApprovalRequest> getApprovalRequestForMdrcCardmake(Map map);

    /**
     * @Title:countApprovalRequestForMdrcCardmake
     * @param map
     * @author qinqinyan
     * */
    Long countApprovalRequestForMdrcCardmake(Map map);
    
    /**
     * 
     * @Title: getRecords 
     * @Description: TODO
     * @param queryObject
     * @return
     * @return: List<ApprovalRequest>
     */
    List<ApprovalRequest> getRecords(Map map);
    
    /**
     * 
     * @Title: getRecords 
     * @Description: TODO
     * @param queryObject
     * @return
     * @return: List<ApprovalRequest>
     */
    List<ApprovalRequest> getECRecords(Map map);

    
    /**
     * 
     * @Title: countRecords 
     * @Description: TODO
     * @param queryObject
     * @return
     * @return: Long
     */
    Long countRecords(Map map);
    
    /**
     * 
     * @Title: countRecords 
     * @Description: TODO
     * @param queryObject
     * @return
     * @return: Long
     */
    Long countECRecords(Map map);
    
    /**
     * @Title: getMakecardRecords 
     * @Description: TODO
     * @param queryObject
     * @return
     * @return: List<ApprovalRequest>
     */
    List<ApprovalRequest> getMakecardRecords(Map map);
    
    /**
     * @Title: countMakecardRecords 
     * @Description: TODO
     * @param queryObject
     * @return
     * @return: List<ApprovalRequest>
     */
    Long countMakecardRecords(Map map);
    
    /**
     * 
     * @Title: getById 
     * @Description: TODO
     * @param id
     * @return
     * @return: ApprovalRequest
     */
    ApprovalRequest getById(Long id);
    
    /**
     * 
     * @Title: getMdrcActiveRecords 
     * @Description: TODO
     * @param map
     * @return
     * @return: List<ApprovalRequest>
     */
    List<ApprovalRequest> getMakecardRecordsOrderBy(Map map);
    
    /**
     * 
     * @Title: getMdrcActiveRecords 
     * @Description: TODO
     * @param map
     * @return
     * @return: List<ApprovalRequest>
     */
    List<ApprovalRequest> getMdrcActiveRecords(Map map);
    
    /**
     * @Title: countMdrcActiveRecords 
     * @Description: TODO
     * @param map
     * @return
     * @return: Long
     */
    Long countMdrcActiveRecords(Map map);
    
    /**
     *  导出山东csv充值记录结构
     */
    List<SdAccApprovalRequest> exportSDAccountChangeRecords(Map map);

    /**
     * @param map
     * @return
     */
    List<ApprovalRequest> queryRecordForAccountChange(Map map);
    
    /**
     * 
     * @Title: getApprovalRequests 
     * @Description: TODO
     * @param entId
     * @param approvalType
     * @param status
     * @return
     * @return: List<ApprovalRequest>
     */
    List<ApprovalRequest> getApprovalRequests(@Param("entId")Long entId, @Param("approvalType") Integer approvalType, @Param("status") Integer status);
}