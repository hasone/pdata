package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.AccountChangeRequest;
import com.cmcc.vrp.util.QueryObject;

import java.util.List;

/**
 * 帐户充值请求服务
 * <p>
 * Created by sunyiwei on 2016/4/19.
 */
public interface AccountChangeRequestService {
    /**
     * 插入新的帐户余额变更请求
     *
     * @param acr 新的帐户余额变更对象
     * @return
     */
    boolean insert(AccountChangeRequest acr);

    /**
     * 获取审批申请对象
     *
     * @param id 申请ID
     * @return
     */
    AccountChangeRequest get(Long id);

    /**
     * 审批通过指定的申请
     *
     * @param id         申请ID
     * @param operatorId 操作者ID
     * @param comment    审批意见
     * @param serialNum  操作流水号
     * @return 通过成功返回true, 否则false
     */
    boolean approval(Long id, Long operatorId, String comment, String serialNum);

    /**
     * 驳回相应的请求
     *
     * @param id         申请ID
     * @param operatorId 操作者ID
     * @param comment    审批意见
     * @param serialNum  操作流水号
     * @return 驳回申请成功返回true,　否则返回false
     */
    boolean reject(Long id, Long operatorId, String comment, String serialNum);

    /**
     * 提交相应的申请
     *
     * @param id         申请ID
     * @param operatorId 操作者ID
     * @param comment    审批意见
     * @param serialNum  操作流水号
     * @return 驳回申请成功返回true,　否则返回false
     */
    boolean commit(Long id, Long operatorId, String comment, String serialNum);

    /**
     * 按照一定的条件进行检索
     *
     * @param queryObject 检索条件
     * @return 返回满足条件的数量
     */
    int queryCount(QueryObject queryObject);

    /**
     * 按照一定的条件进行检索，并返回相应的列表
     *
     * @param queryObject 检索条件
     * @return 返回满足条件的列表
     */
    List<AccountChangeRequest> query(QueryObject queryObject);

    /**
     * 更新充值申请的金额并重新提交
     *
     * @param accountChangeRequestId
     * @param deltaCount
     * @return
     */
    boolean updateCount(Long accountChangeRequestId, Double deltaCount, Long operatorId);
}
