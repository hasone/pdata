package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.AccountChangeApprovalRecord;

import java.util.List;

/**
 * 审批意见记录表
 * <p>
 * Created by sunyiwei on 2016/4/19.
 */
public interface AccountChangeApprovalRecordService {
    /**
     * 插入新的审批意见记录
     *
     * @param acar
     * @return
     */
    boolean insert(AccountChangeApprovalRecord acar);

    /**
     * 获取相应的审批记录
     *
     * @param id 记录ID
     * @return
     */
    AccountChangeApprovalRecord get(Long id);

    /**
     * 获取请求的所有审批记录
     *
     * @param requestId 请求ID
     * @return 请求相关的所有审批记录
     */
    List<AccountChangeApprovalRecord> getByRequestId(Long requestId);
}
