package com.cmcc.vrp.boss;

/**
 * BOSS接口的抽象类，负责与BOSS完成相应的通讯功能
 * <p>
 * 1. BOSS接口只负责与BOSS相应的充值、查询、对帐等功能，它不负责业务相关的任何功能
 * 业务相关的功能（如增删改查业务记录等）应该在其上面再封装出相应的服务来实现
 * <p>
 * 2. 各渠道在实现时，可以自行增加需要的记录表，但注意不要和业务功能有过多耦合
 * <p>
 * Created by sunyiwei on 2016/4/6.
 */
public interface BossService {
    /**
     * 向BOSS发起充值操作
     *
     * @param entId     企业ID
     * @param splPid    供应商产品ID
     * @param mobile    手机号码
     * @param serialNum 平台的充值序列号
     * @return 返回相应的充值结果
     */
    BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize);

    /**
     * 提供该BossService的指纹标识，只有当这个指纹标识与supplier表中的fingerPrint字段的值匹配时，该bossService才能为该供应商提供的产品进行充值
     * <p>
     * 建议采用随机字符串的MD5码作为指纹
     *
     * @return BossService提供的指纹数据
     */
    String getFingerPrint();

    /**
     * @param cardNumber
     * @param mobile
     * @return
     * @Title: mdrcCharge
     * @Description: TODO
     * @return: BossOperationResult
     */
    BossOperationResult mdrcCharge(String cardNumber, String mobile);

    /**
     * 从BOSS同步账户余额，并写入数据库相应的记录
     *
     * @param entId 企业的平台ID
     * @param prdId 产品的平台ID，这里是平台产品
     * @return
     */
    boolean syncFromBoss(Long entId, Long prdId);
}
