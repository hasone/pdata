package com.cmcc.vrp.province.service;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.AccountChangeDetail;
import com.cmcc.vrp.province.model.ActivityApprovalDetail;
import com.cmcc.vrp.province.model.AdminChangeDetail;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.model.EcApprovalDetail;
import com.cmcc.vrp.province.model.EntCallbackAddr;
import com.cmcc.vrp.province.model.EntWhiteList;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterpriseFile;
import com.cmcc.vrp.province.model.HistoryEnterprises;
import com.cmcc.vrp.province.model.MdrcActiveDetail;
import com.cmcc.vrp.province.model.ProductChangeDetail;
import com.cmcc.vrp.province.model.SdAccApprovalRequest;
import com.cmcc.vrp.util.QueryObject;

/***
 * 审批请求服务类
 * @author qinqinyan
 */
/**
 * <p>Title:ApprovalRequestService </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年10月18日
*/
public interface ApprovalRequestService {

    /**
    * @Title: insert
    * @Description: TODO
    */
    boolean insert(ApprovalRequest record);

    /**
    * @Title: selectByPrimaryKey
    * @Description: TODO
    */
    ApprovalRequest selectByPrimaryKey(Long id);

    /**
     * 获取审批请求列表(用于企业开户、充值审批、产品变更审批)
     *
     * @param processId 审批流程id
     */
    List<ApprovalRequest> getApprovalRequestList(QueryObject queryObject, List<Enterprise> enterprises, Long processId,
            List<Integer> preconditions, boolean isMakecardRole);

    /**
    * @Title: countApprovalRequest
    * @Description: TODO
    */
    Long countApprovalRequest(QueryObject queryObject, List<Enterprise> enterprises, Long processId,
            List<Integer> preconditions, boolean isMakecardRole);

    /**
    * @Title: updateByPrimaryKeySelective
    * @Description: TODO
    */
    boolean updateByPrimaryKeySelective(ApprovalRequest record);

    /**
    * @Title: updateLastRecordAndInsertNewRecord
    * @Description: TODO
    */
    boolean updateLastRecordAndInsertNewRecord(ApprovalRecord updateApprovalRecord,
            ApprovalRequest updateApprovalRequest, ApprovalRecord newApprovalRecord, Enterprise enterprise,
            List<ProductChangeDetail> productChangeDetails, AccountChangeDetail accountChangeDetail,
            ActivityApprovalDetail activityApprovalDetail, HistoryEnterprises historyEnterprises,
            EnterpriseFile enterpriseFile, EntCallbackAddr entCallbackAddr, List<EntWhiteList> entWhiteLists,
            AdminChangeDetail adminChangeDetail);

    /**
     * 活动审核方法
     * @param updateApprovalRecord 当前待审批的审核记录
     * @param newApprovalRecord 下一级审批的审核记录
     * @param updateApprovalRequest 审批请求记录
     * @param activityApprovalDetail 该活动审核详情
     * @author qinqinyan
     */
    boolean approvalForActivity(ApprovalRecord updateApprovalRecord, ApprovalRequest updateApprovalRequest,
            ApprovalRecord newApprovalRecord, ActivityApprovalDetail activityApprovalDetail);

    /**
     * 活动审核方法
     * @param updateApprovalRecord 当前待审批的审核记录
     * @param newApprovalRecord 下一级审批的审核记录
     * @param updateApprovalRequest 审批请求记录
     * @author qinqinyan
     */
    boolean updateLastRecordAndInsertNewRecord(ApprovalRecord updateApprovalRecord,
            ApprovalRequest updateApprovalRequest, ApprovalRecord newApprovalRecord);

    /**
    * @Title: submitApproval
    * @Description: 
    */
    boolean submitApproval(ApprovalRequest approvalRequest, ApprovalRecord approvalRecord, Enterprise enterprise,
            AccountChangeDetail accountChangeDetail, List<ProductChangeDetail> productChangeDetails,
            ActivityApprovalDetail activityApprovalDetail, AdminChangeDetail adminChangeDetail);

    /**
     * 企业信息变更审核提交
     * @param entId
     * @param creatorId
     * @return 请求id
     * @author qinqinyan
     * */
    Long submitEnterpriseChange(Long entId, Long creatorId);

    /**
     * 提交无审核的审批记录
     * 企业开户：潜在客户直接变为合作企业
     * 账户变更：变更账户记录
     * 产品变更：直接变更账户
     */
    boolean submitWithoutApproval(ApprovalRequest approvalRequest, Enterprise enterprise,
            AccountChangeDetail accountChangeDetail, List<ProductChangeDetail> productChangeDetails,
            AdminChangeDetail adminChangeDetail);

    /**
     * 获取所有的历史审核记录,用于企业审核
     */
    List<ApprovalRequest> selectByEntIdAndProcessId(Long entId, Long processId);

    /**
     * 获取所用的充值请求记录
     */
    List<ApprovalRequest> queryApprovalRequestsForAccountChange(QueryObject queryObject, Long adminId,
            Integer approvalType);
    /**
     * @param queryObject
     * @param adminId
     * @param approvalType
     * @return
     */
    List<ApprovalRequest> queryRecordForAccountChange(QueryObject queryObject, Long adminId,
            Integer approvalType);
    
    /**
     * 获取所用的充值请求记录
     */
    List<SdAccApprovalRequest> querySdAccountChangeRecord(QueryObject queryObject, Long adminId,
            Integer approvalType);

    /**
     * @param queryObject
     * @param AdminId
     * @param approvalType
     * @return
     */
    Long countApprovalRequestsForAccountChange(QueryObject queryObject, Long AdminId, Integer approvalType);

    /**
     * 获取当前审批请求的状态
     */
    String getCurrentStatus(ApprovalRequest approvalRequest);

    /**
     * 获取活动所有审批申请，用于活动审批
     */
    List<ApprovalRequest> selectByActivityId(String activityId);

    /**
     * 查找指定企业信息变更审核记录
     * @param queryObject
     * @author qinqinyan
     * */
    List<ApprovalRequest> queryForEntChange(QueryObject queryObject);

    /**
     * 计算指定企业信息变更记录
     * @param queryObject
     * @author qinqinyan
     * */
    Long countForEntChange(QueryObject queryObject);

    /**
     * 提交EC信息审核
     * @Title: submitEcApproval 
     * @param interfaceFlag
     * @param approvalRequest
     * @param approvalRecord
     * @param ecApprovalDetail
     * @return
     * @Author: wujiamin
     * @date 2016年10月21日
     */
    boolean submitEcApproval(Integer interfaceFlag, ApprovalRequest approvalRequest, ApprovalRecord approvalRecord,
            EcApprovalDetail ecApprovalDetail);

    /**
     * 提交EC信息，无需审核
     * @Title: submitEcWithoutApproval 
     * @param interfaceFlag
     * @param ips
     * @param approvalRequest
     * @param approvalRecord
     * @param ecApprovalDetail
     * @return
     * @Author: wujiamin
     * @date 2016年10月24日
     */
    boolean submitEcWithoutApproval(Integer interfaceFlag, ApprovalRequest approvalRequest,
            ApprovalRecord approvalRecord, EcApprovalDetail ecApprovalDetail, List<String> ips);

    /**
     * 获取所用的ec变更记录
     */
    List<ApprovalRequest> queryApprovalRequestsForEcChange(QueryObject queryObject);

    /**
     * @param queryObject
     * @return
     */
    Long countApprovalRequestsForEcChange(QueryObject queryObject);

    /**
     * 根据审批类型和企业ID获取记录，并安装创建时间的倒序排序
     * @Title: selectByEntIdAndProcessType 
     * @param entId
     * @param type
     * @return
     * @Author: wujiamin
     * @date 2016年10月25日
     */
    List<ApprovalRequest> selectByEntIdAndProcessType(Long entId, Integer type);

    /**
     * 提交审核（营销活动）
     * @param approvalRequest
     * @param approvalRecord
     * @param activityApprovalDetail
     * @return
     * @author qinqinyan
     * */
    boolean submitApprovalForActivity(ApprovalRequest approvalRequest, ApprovalRecord approvalRecord,
            ActivityApprovalDetail activityApprovalDetail);

    /**
     * 获取营销模板激活审批请求列表
     * @param queryObject
     * @author qinqinyan
     * */
    List<ApprovalRequest> getApprovalRequestForMdrcActive(QueryObject queryObject);

    /**
     * 计算营销模板激活审批请求个数
     * @param queryObject
     * @author qinqinyan
     * */
    Long countApprovalRequestForMdrcActive(QueryObject queryObject);

    /**
     * 获取营销模板制卡审批请求列表
     * @param queryObject
     * @author qinqinyan
     * */
    List<ApprovalRequest> getApprovalRequestForMdrcCardmake(QueryObject queryObject);

    /**
     * 计算营销模板制卡审批请求个数
     * @param queryObject
     * @author qinqinyan
     * */
    Long countApprovalRequestForMdrcCardmake(QueryObject queryObject);

    /**
     * 提交营销卡激活审批申请
     * @param approvalRequest
     * @param mdrcActiveDetail
     * @return
     * @author qinqinyan
     * */
    boolean submitApprovalForMdrcActive(ApprovalRequest approvalRequest, MdrcActiveDetail mdrcActiveDetail);

    /**
     * 编辑营销卡激活审批申请
     * @param mdrcActiveDetail
     * @author qinqinyan
     * */
    boolean editApprovalForMdrcActive(ApprovalRequest approvalRequest, MdrcActiveDetail mdrcActiveDetail);

    /**
     * 审核营销卡激活审批
     * @param mdrcActiveDetail
     * @param newApprovalRecord
     * @param updateApprovalRecord
     * @param updateApprovalRequest
     * @author qinqinyan
     * */
    boolean approvalForMdrcActive(ApprovalRecord updateApprovalRecord, ApprovalRequest updateApprovalRequest,
            ApprovalRecord newApprovalRecord, MdrcActiveDetail mdrcActiveDetail);

    /**
     * 审核营销卡制卡审批
     * @param newApprovalRecord
     * @param updateApprovalRecord
     * @param updateApprovalRequest
     * @author qinqinyan
     * */
    boolean approvalForMdrcCardmake(ApprovalRecord updateApprovalRecord, ApprovalRequest updateApprovalRequest,
            ApprovalRecord newApprovalRecord);

    /**
     * 产品变更审批
     * @param newApprovalRecord
     * @param updateApprovalRecord
     * @param updateApprovalRequest
     * @author qinqinyan
     * */
    boolean approvalForProductChange(ApprovalRecord updateApprovalRecord, ApprovalRequest updateApprovalRequest,
            ApprovalRecord newApprovalRecord, List<ProductChangeDetail> productChangeDetails);

    /**
     * 产品变更审批
     * @author qinqinyan
     * */
    boolean submitWithoutApprovalForProductChange(ApprovalRequest approvalRequest,
            List<ProductChangeDetail> productChangeDetails);

    /**
     * 
     * @Title: getRecords 
     * @Description: TODO
     * @param queryObject
     * @return
     * @return: List<ApprovalRequest>
     */
    List<ApprovalRequest> getRecords(QueryObject queryObject);
    
    /**
     * 
     * @Title: getRecords 
     * @Description: TODO
     * @param queryObject
     * @return
     * @return: List<ApprovalRequest>
     */
    List<ApprovalRequest> getECRecords(QueryObject queryObject);

    /**
     * 
     * @Title: countRecords 
     * @Description: TODO
     * @param queryObject
     * @return
     * @return: Long
     */
    Long countRecords(QueryObject queryObject);
    
    /**
     * 
     * @Title: countRecords 
     * @Description: TODO
     * @param queryObject
     * @return
     * @return: Long
     */
    Long countECRecords(QueryObject queryObject);
    
    /**
     * 获取制卡审批列表
     * @Title: getMakecardRecords 
     * @Description: TODO
     * @param queryObject
     * @return
     * @return: List<ApprovalRequest>
     * @author qinqinyan
     * @date 2017/08/07
     */
    List<ApprovalRequest> getMakecardRecords(QueryObject queryObject);
    
    /**
     * 获取制卡审批列表
     * @Title: getMakecardRecordsOrderBy 
     * @Description: TODO
     * @param queryObject
     * @return
     * @return: List<ApprovalRequest>
     * @author qinqinyan
     * @date 2017/11/09
     */
    List<ApprovalRequest> getMakecardRecordsOrderBy(QueryObject queryObject);
    
    /**
     * @Title: countMakecardRecords 
     * @Description: TODO
     * @param queryObject
     * @return
     * @return: List<ApprovalRequest>
     * @author qinqinyan
     * @date 2017/08/07
     */
    Long countMakecardRecords(QueryObject queryObject);
    
    /**
     * 
     * @Title: getById 
     * @Description: 根据主键查询
     * @param id
     * @return
     * @return: ApprovalRequest
     */
    ApprovalRequest getById(Long id);
    
    /**
     * 
     * @Title: getMdrcActiveRecords 
     * @Description: 激活审批记录
     * @param map
     * @return
     * @return: List<ApprovalRequest>
     */
    List<ApprovalRequest> getMdrcActiveRecords(Map map);
    
    /**
     * 
     * @Title: countMdrcActiveRecords 
     * @Description: 激活审批记录
     * @param map
     * @return
     * @return: Long
     */
    Long countMdrcActiveRecords(Map map);
    
    /**
     * 
     * @Title: isOverAuth 
     * @Description: 校验当前用户是否有权限查看审批请求权限
     * @param currentUserId
     * @param requestId
     * @return
     * @return: boolean
     */
    boolean isOverAuth(Long currentUserId, Long requestId);
    
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
    List<ApprovalRequest> getApprovalRequests(Long entId, Integer approvalType, Integer status);
    
}
