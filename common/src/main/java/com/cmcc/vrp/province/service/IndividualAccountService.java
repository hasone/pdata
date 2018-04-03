package com.cmcc.vrp.province.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.module.Membership;

/**
 * IndividualAccountService.java
 */
public interface IndividualAccountService {
    /** 
     * @Title: selectByPrimaryKey 
     */
    public IndividualAccount selectByPrimaryKey(Long id);

    /** 
     * @Title: batchInsert 
     */
    public boolean batchInsert(List<IndividualAccount> accounts);

    public IndividualAccount getAccountByOwnerIdAndProductId(Long ownerId, Long productId, Integer type);

    /**
     * boss处的账户变更（话费，流量币）
     *
     * @Title: changeBossAccount
     * @Author: wujiamin
     * @date 2016年9月28日上午9:21:19
     */
    public Boolean changeBossAccount(Long adminId, BigDecimal count, Long productId,
                                     String systemSerial, Integer accountRecordType, String desc, Integer activityType, Integer back);

    /** 
     * @Title: addCount 
     */
    public boolean addCount(IndividualAccount account, BigDecimal count);

    /**
     * 根据用户手机号增加相应的账户, 如果用户相应账户不存在, 则会先创建账户
     *
     * @param mobile    用户手机号
     * @param prdId     个人用户产品ID
     * @param serialNum 操作流水号
     * @param count     增加的值
     * @return 增加成功返回true, 否则false
     */
    boolean addCountForcely(String mobile, Long prdId, String serialNum, BigDecimal count, ActivityType activityType, String description);

    /** 
     * @Title: minusCount 
     */
    public boolean minusCount(IndividualAccount account, BigDecimal count);

    /** 
     * @Title: changeFrozenAccount 
     */
    boolean changeFrozenAccount(Long adminId, Long ownerId, Long accountId,
                                BigDecimal count, Long productId, String systemSerial,
                                Integer accountRecordType, String desc, Integer activityType, Integer back);

    /**
     * 创建活动冻结账户（封装了wujiamin底层方法）
     *
     * @author qinqinyan
     */
    boolean createAccountForActivity(Activities activities, ActivityInfo activityInfo, List<ActivityPrize> activityPrizes, IndividualAccount individualAccount);

    /**
     * 将活动账户钱回退至boss（封装了wujiamin底层方法）
     *
     * @author qinqinyan
     */
    boolean giveBackForActivity(String activityId);

    /**
     * 个人红包充值流量币
     *
     * @param count  充值流量币大小
     * @param mobile 中奖手机号
     * @author qinqinyan
     */
    boolean chargeFlowcoinForIndividualActivity(String activityId, Integer count, String mobile, String activityWinRecordId);

    /** 
     * @Title: insert 
     */
    boolean insert(IndividualAccount individualAccount);

    /**
     * 创建流量币购买和兑换冻结账户
     *
     * @Title: createFlowcoinExchangeAndPurchaseAccount
     * @Author: wujiamin
     * @date 2016年9月29日下午5:10:32
     */
    boolean createFlowcoinExchangeAndPurchaseAccount(
            IndividualAccount individualAccount, String systemSerial,
            String desc, Integer activityType);

    /**
     * 创建冻结账户
     *
     * @Title: createFrozenAccount
     * @Author: wujiamin
     * @date 2016年9月29日下午6:20:55
     */
    boolean createFrozenAccount(IndividualAccount individualAccount,
                                String systemSerial, String desc, Integer activityType);

    /** 
     * 更新账户余额
     * @Title: changeAccount 
     */
    boolean changeAccount(IndividualAccount account, BigDecimal count, String systemSerial, Integer accountRecordType, String desc, Integer activityType,
            Integer back);

    /** 
     * 向流量账户回退流量
     * @Title: giveBackFlow 
     */
    boolean giveBackFlow(String activityId);
    
    /** 
     * 获取过期的账户
     * @Title: getExpireAccount 
     */
    List<IndividualAccount> getExpireAccount(Date date);

    /** 
     * @Title: updateExpireTimeAndOrderId 
     */
    boolean updateExpireTimeAndOrderId(Long id, Date endMonthOfDate, Long orderId);
    
    /** 
     * 广东微信公众号绑定用户，检查平台侧是否存在个人积分账户，不存在则创建积分账户
     * @Title: checkAndInsertAccountForWx 
     */
    boolean checkAndInsertAccountForWx(Long adminId, String openid);
    
    /** 
     * 广东众筹获平台取会员列表
     * @Title: getMembershipList 
     */
    List<Membership> getMembershipList(Map map);

    /** 
     * @Title: countMembershipList 
     */
    Integer countMembershipList(Map map);

    /** 
     * 四川红包检查账户，账户不存在则创建
     * @Title: insertAccountForScJizhong 
     */
    boolean insertAccountForScJizhong(Long adminId);
    
    /** 
     * @Title: selectByTypeAndOwnerId 
     */
    List<IndividualAccount> selectByTypeAndOwnerId(Integer type, Long ownerId);

}
