package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.AccountChangeApprovalRecord;

import java.util.List;

/**
 * 账户变更审批记录mapper
 */
public interface AccountChangeApprovalRecordMapper {
    /**
     * 插入账户变更审批记录
     *
     * @param record 记录对象
     * @return
     */
    int insert(AccountChangeApprovalRecord record);

    /**
     * 根据ID获取账户变更审批记录
     *
     * @param id 记录ID
     * @return
     */
    AccountChangeApprovalRecord get(Long id);

    /**
     * 根据请求ID获取账户变更审批记录列表
     *
     * @param requestId 请求ID
     * @return
     */
    List<AccountChangeApprovalRecord> getByRequestId(Long requestId);
}