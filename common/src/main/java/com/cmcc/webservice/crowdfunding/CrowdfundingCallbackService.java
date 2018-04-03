package com.cmcc.webservice.crowdfunding;

/**
 * 广东众筹活动回调服务
 * @author qinqinyan
 * */
public interface CrowdfundingCallbackService {
    
    /**
     * 通知EC平台创建活动成功
     * @param activityId
     * @author qinqinyan
     * */
    boolean notifyCreateActivity(String activityId);
    
    /**
     * 通知EC平台活动众筹成功
     * @param activityId
     * @author qinqinyan
     * */
    boolean notifyCrowdFundingSucceed(String activityId);

    /** 
     * 通知EC平台众筹活动的充值结果
     * @Title: notifyChargeResult 
     */
    boolean notifyChargeResult(String activityWinRecordId);

}
