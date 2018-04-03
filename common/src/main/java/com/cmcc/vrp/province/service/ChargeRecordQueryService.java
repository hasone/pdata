package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.ChargeRecord;

/**
 * 根据查询数据库充值状态，状态为不确定状态的，查询boss，并将结果更新数据库以及储存到缓存中
 * 
 * 
 *
 */
public interface ChargeRecordQueryService {
    /**
     * 通过系统流水号查询
     */
    ChargeRecord queryStatusBySystemNun(String systemNun);

    /**
     * 通过企业的Id和流水号查询
     */
    List<ChargeRecord> queryStatusBySerialNum(Long enterId , String serialNum);

}
