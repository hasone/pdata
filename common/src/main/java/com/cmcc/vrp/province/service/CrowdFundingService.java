package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.CrowdfundingActivityDetail;

/**
 * Created by qinqinyan on 2017/1/21.
 * 广东众筹服务类
 */
public interface CrowdFundingService {

    /**
     * 众筹活动插入
     * @param activities
     * @param activityPrizes
     * @param crowdfundingActivityDetail
     * @param phones
     * @author qinqinyan
     * */
    boolean insertActivity(Activities activities,
                           CrowdfundingActivityDetail crowdfundingActivityDetail,
                           List<ActivityPrize> activityPrizes,
                           String phones, String queryurl);
    
    /**
     * 众筹活动更新
     * @param activities
     * @param activityPrizes
     * @param crowdfundingActivityDetail
     * @param phones
     * @author qinqinyan
     * */
    boolean updateActivity(Activities activities,
    		CrowdfundingActivityDetail crowdfundingActivityDetail,
    		List<ActivityPrize> activityPrizes,
    		String phones, String queryurl);
    
    /**
     * 下架
     * @param activityId
     * @author qinqinyan
     * */
    boolean offShelf(String activityId);
    
    /**
     * 上架
     * @param activityId
     * @author qinqinyan
     * */
    boolean onShelf(String activityId);
    
    /**
     * 充值
     * @param activityWinRecordId 中奖uuid
     * @author qinqinyan
     * */
    boolean charge(String activityWinRecordId);
    
    /**
     * 插入充值队列
     * @param recordId 中奖uuid
     * @author qinqinyan
     * */
    boolean insertRabbitmq(String recordId);
    
    /** 
     * 查询某个用户参加的所有活动的支付结果，返回null或者该活动的activityId
     * @Title: queryPayResult 
     */

    String queryPayResult(String mobile);
    
    /** 
     * 处理支付中状态的支付记录及活动参加记录
     * @param currentMobile 
     * @Title: processPayingRecord 
     */
    boolean processPayingRecord(String activityId, String currentMobile);
}
