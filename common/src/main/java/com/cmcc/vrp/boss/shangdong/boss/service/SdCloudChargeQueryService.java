package com.cmcc.vrp.boss.shangdong.boss.service;

import java.util.List;

import com.cmcc.vrp.province.model.ChargeRecord;

/**
 * 山东云平台查询充值结果服务类
 *
 */
public interface SdCloudChargeQueryService {
    /**
     * 通过系统流水号查询
     */
    ChargeRecord queryStatusBySystemNun(String systemNun);

    /**
     * 通过企业的Id和流水号查询
     */
    List<ChargeRecord> queryStatusBySerialNum(Long enterId , String serialNum);
}
