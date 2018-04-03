package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.util.QueryObject;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午4:25:58
*/
public interface ApprovalRecordService {

    /**
     * @param id
     * @return
     */
    ApprovalRecord selectByPrimaryKey(Long id);

    /**
     * 该方法要求delete_flag=0的状态
     */
    List<ApprovalRecord> selectByRequestId(Long requestId);

    /**
     * 账户变更：一旦被驳回，该条审批delete_flag变为1，则被废弃，因此为了捞出所有记录，该方法忽略delete_flag的状态
     */
    List<ApprovalRecord> selectByRequestIdAll(Long requestId);

    /**
     * 插入审核记录
     */
    boolean insertApprovalRecord(ApprovalRecord record);

    /**
     * 更新审核记录
     */
    boolean updateApprovalRecord(ApprovalRecord record);

    /**
     * 获取企业所有审核记录
     *
     * @param entId 企业id
     * @param type  审批类型  0, 企业开户，1, 产品变更， 2, 账户变更
     */
    List<ApprovalRecord> selectByEndIdAndProcessType(Long entId, Integer type);

    /**
     * @param approvalRequests
     * @return
     */
    List<ApprovalRecord> selectByApprovalRequests(List<ApprovalRequest> approvalRequests);

    /**
     * @param requestId
     * @param currUserId
     * @return
     */
    ApprovalRecord selectNewRecordByRequestId(Long requestId, Long currUserId);

    /**
     * 根据活动uuid查找历史审核记录
     * @param activityId
     * @return 营销活动的历史审核记录
     * @author qinqinyan
     * */
    List<ApprovalRecord> selectByActivityIdForActivity(String activityId);

    /**
     * 
     * @Title: getRecords 
     * @Description: TODO
     * @param queryObject
     * @return
     * @return: List<ApprovalRecord>
     */
    List<ApprovalRecord> getRecords(QueryObject queryObject);

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
     * 获取待审核记录
     * */
    Long getNewApprovalRecord(Long id);

}
