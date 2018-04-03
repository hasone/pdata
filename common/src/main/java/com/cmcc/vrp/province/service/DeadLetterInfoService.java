package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.DeadLetterInfo;

import java.util.List;

/**
 * Created by sunyiwei on 2016/6/17.
 */
public interface DeadLetterInfoService {
    /**
     * 创建新的死信消息
     *
     * @param deadLetterInfo 死信消息对象
     * @return 创建成功返回true, 否则false
     */
    boolean create(DeadLetterInfo deadLetterInfo);

    /**
     * 批量逻辑删除记录信息
     *
     * @param ids 记录id列表
     * @return 删除成功返回true, 否则false
     */
    boolean batchDelete(List<Long> ids);

    /**
     * 获取当前所有未被删除的记录信息
     *
     * @return 未被删除的记录列表
     */
    List<DeadLetterInfo> getAllUndeletedInfos();
}
