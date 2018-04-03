package com.cmcc.vrp.boss;

/**
 * Created by sunyiwei on 2016/6/16.
 */
public interface BossServiceProxyService {
    /**
     * BOSS充值代理服务，根据传入的平台产品ID，企业ID等信息选择最合适的BOSS进行充值
     *
     * @param entId          企业ID
     * @param prdId          平台产品ID
     * @param mobile         手机号
     * @param serialNum      操作序列号
     * @param chargeRecordId 充值记录id
     * @return BOSS充值操作结果
     */
    BossOperationResult charge(Long chargeRecordId, Long entId, Long prdId,
                               String mobile, String serialNum);

    /**
     * 是否需要从BOSS侧同步余额
     *
     * @return true表示需要，false表示不需要
     */
    boolean needSyncFromBoss();

    /**
     * 根据手机号，企业ID以及平台产品ID获取相应的供应商
     *
     * @param mobile 手机号
     * @param entId  企业的平台ID
     * @param prdId  产品的平台ID
     * @return 匹配的相应结果
     */
    BossMatchResult chooseBossService(String mobile, Long entId, Long prdId);
}
