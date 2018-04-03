package com.cmcc.vrp.province.service.impl;

import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.model.ChargeRecord;

/**
 * ChargeRecordQueryServiceImpl 的空实现, 只需要从数据库读取记录存到缓存
 *
 */
@Service("emptyChargeRecordQueryService")
public class EmptyChargeRecordQueryServiceImpl extends
        ChargeRecordQueryServiceImpl {

    /**
     * 从boss查询结果并更新record对象的充值状态和返回信息，并更新到数据库
     * 不需要从boss更新结果
     */
    @Override
    protected boolean updateResultFromBoss(ChargeRecord record){
        return true;
    }
}
