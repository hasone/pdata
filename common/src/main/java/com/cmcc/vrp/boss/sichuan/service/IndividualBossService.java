package com.cmcc.vrp.boss.sichuan.service;

import java.math.BigDecimal;

import com.cmcc.vrp.boss.sichuan.model.individual.ScAppQryResponse;

/**
 * 个人业务的BOSS服务类
 *
 * @author wujiamin
 * @date 2016年10月9日上午11:03:14
 */
public interface IndividualBossService {
    /**
     * 查询当月流量余额
     * @Title: queryFlow 
     * @param mobile 手机号码
     * @param result 流量查询结果对象
     * @return
     * @Title: queryFlow
     * @Author: wujiamin
     * @date 2016年10月9日上午11:07:38
     */
    Boolean queryFlow(String mobile, ScAppQryResponse result); 
    

    /**
     * 变更boss流量币
     * @Title: changeBossFlowcoin 
     * @param mobile 变更流量币的用户的手机号码
     * @param count 变更的流量币数量
     * @param accountRecordType 变更的状态：收入，支出
     * @return
     * @Author: wujiamin
     * @date 2016年10月10日下午2:25:11
     */
    Boolean changeBossFlowcoin(String mobile, Long count, Integer accountRecordType, String systemSerial);
    
    /**
     * 变更boss话费
     * @Title: changeBossPhoneFare 
     * @param mobile 手机号码
     * @param price 话费金额
     * @param accountRecordType 变更的状态：收入，支出
     * @return
     * @Author: wujiamin
     * @date 2016年10月10日下午3:07:07
     */
    Boolean changeBossPhoneFare(String mobile, BigDecimal price, Integer accountRecordType, String systemSerial);
    
    /**
     * 流量充值
     * @Title: chargeFlow 
     * @param mobile 手机号码
     * @param individualProductId 个人产品id
     * @return
     * @Author: wujiamin
     * @date 2016年10月10日下午3:08:51
     */
    Boolean chargeFlow(String mobile, Long individualProductId, String systemSerial);
}
