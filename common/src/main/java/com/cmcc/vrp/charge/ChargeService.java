/**
 * @Title: ChargeService.java
 * @Package com.cmcc.vrp.province.service
 * @author: sunyiwei
 * @date: 2015年6月10日 上午11:46:06
 * @version V1.0
 */
package com.cmcc.vrp.charge;

import com.cmcc.vrp.enums.AccountType;

/**
 * @Description: 充值服务， 调用BOSS服务，并记录相应的充值信息（比如，插入充值记录，更新记录状态等等） <p> 1. 所有的充值服务必须有相应的记录 2.
 * 充值可以考虑使用队列来实现
 * @author: sunyiwei
 * @date: 2015年6月10日 上午11:46:06
 */
public interface ChargeService {
    /**
     * 调用充值服务
     *
     * @param entId          企业ID
     * @param prdId          产品ID
     * @param activityId     活动ID, 充值的费用将从这个活动的帐户中扣除，对于帐户在BOSS侧的平台来讲，这个值可以为null
     * @param accountType    活动类型
     * @param mobile         手机号码
     * @param serialNum      平台侧的充值流水号
     * @param chargeRecordId chargeRecord表中记录的id
     * @return 返回相应的充值结果
     */
    ChargeResult charge(Long chargeRecordId, Long entId, Long activityId,
                        Long prdId, AccountType accountType, String mobile, String serialNum);

    /**
     * @param chargeRecordId
     * @param entId
     * @param activityIdl
     * @param prdId
     * @param accountType
     * @param mobile
     * @param serialNum
     * @param needCallback
     * @return
     */
    ChargeResult charge(Long chargeRecordId, Long entId, Long activityId,
                        Long prdId, AccountType accountType, String mobile, String serialNum, boolean needCallback);

    //仅调用BOSS进行充值,不进行其它操作,如账户等
    /**
     * @param chargeRecordId
     * @param entId
     * @param prdId
     * @param mobile
     * @param serialNum
     * @return
     */
    ChargeResult charge(Long chargeRecordId, Long entId, Long prdId, String mobile, String serialNum);
}
