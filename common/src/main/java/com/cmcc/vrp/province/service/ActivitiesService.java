package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.activity.model.AutoResponsePojo;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityPrize;

import java.util.List;
import java.util.Map;

/**
 * 活动服务
 * <p>
 * Created by qinqinyan on 2016/8/17.
 */
public interface ActivitiesService {

    /** 
     * @Title: insert 
     * @param record
     * @return
     * @Author: wujiamin
     * @date 2016年10月17日下午5:27:42
    */
    boolean insert(Activities record);

    /** 
     * @Title: selectByPrimaryKey 
     * @param id
     * @return
     * @Author: wujiamin
     * @date 2016年10月17日下午5:27:47
    */
    Activities selectByPrimaryKey(Long id);

    /** 
     * @Title: updateByPrimaryKeySelective 
     * @param record
     * @return
     * @Author: wujiamin
     * @date 2016年10月17日下午5:27:49
    */
    boolean updateByPrimaryKeySelective(Activities record);

    /** 
     * @Title: selectByActivityId 
     * @param activityId
     * @return
     * @Author: wujiamin
     * @date 2016年10月17日下午5:27:53
    */
    Activities selectByActivityId(String activityId);

    /**
     * @param activityId
     * @return
     */
    List<Activities> selectActivityListByActivityId(String activityId);

    /**
     * 获取活动记录
     */
    List<Activities> selectByMap(Map map);

    /**
     * @param map
     * @return
     */
    Long countByMap(Map map);

    /**
     * 插入流量卡活动记录
     *
     * @param activities   活动
     * @param cmMobileList 移动手机号
     * @param ctMobileList 电信手机号
     * @param cuMobileList 联通手机号
     * @param cmUserSet    移动手机号去重
     * @param ctUserSet    电信手机号去重
     * @param cuUserSet    联通手机号去重
     * @author qinqinyan
     */
    boolean insertFlowcardActivity(Activities activities, Long cmProductId,
                                   Long cuProductId, Long ctProductId, String cmMobileList,
                                   String cuMobileList, String ctMobileList, String cmUserSet,
                                   String cuUserSet, String ctUserSet);

    /**
     * 编辑流量卡活动记录
     *
     * @param activities   活动
     * @param cmMobileList 移动手机号
     * @param ctMobileList 电信手机号
     * @param cuMobileList 联通手机号
     * @param cmUserSet    移动手机号去重
     * @param ctUserSet    电信手机号去重
     * @param cuUserSet    联通手机号去重
     * @author qinqinyan
     */
    boolean editFlowcardActivity(Activities activities, Long cmProductId,
                                 Long cuProductId, Long ctProductId, String cmMobileList,
                                 String cuMobileList, String ctMobileList, String cmUserSet,
                                 String cuUserSet, String ctUserSet);

    /**
     * 插入二维码活动记录
     *
     * @param activities     活动
     * @param activityInfo   活动详情
     * @param cmProductId    移动产品id
     * @param cuProductId    联通产品id
     * @param ctProductId    电信产品id
     * @param correctMobiles 黑白名单手机号
     * @author qinqinyan
     */
    boolean insertQRcodeActivity(Activities activities,
                                 ActivityInfo activityInfo, Long cmProductId, Long cuProductId,
                                 Long ctProductId, String correctMobiles);

    /**
     * 编辑二维码活动记录
     *
     * @param activities     活动
     * @param activityInfo   活动详情
     * @param cmProductId    移动产品id
     * @param cuProductId    联通产品id
     * @param ctProductId    电信产品id
     * @param correctMobiles 黑白名单手机号
     * @author qinqinyan
     */
    boolean editQRcodeActivity(Activities activities,
                               ActivityInfo activityInfo, Long cmProductId, Long cuProductId,
                               Long ctProductId, String correctMobiles);

    /**
     * 插入红包
     *
     * @param activities
     * @param activityPrize
     * @author qinqinyan
     */
    boolean insertRedpacketForIndividual(Activities activities, ActivityPrize activityPrize);

    /**
     * 上下架
     *
     * @param activityId
     * @param status
     * @author qinqniyan
     */
    boolean changeStatus(String activityId, Integer status);

    /**
     * 批量上下架
     *
     * @param activities
     * @param status
     * @author qinqniyan
     */
    boolean batchChangeStatus(List<Activities> activities, Integer status);

    /**
     * 创建定时任务（用于活动开始时）
     *
     * @param activities
     * @author qinqinyan
     */
    boolean createActivityStartSchedule(Activities activities);

    /**
     * 创建定时任务（用于活动结束时）
     *
     * @param activities
     * @author qinqinyan
     */
    boolean createActivityEndSchedule(Activities activities);

    /**
     * 参与活动
     *
     * @param activityId   活动ID
     * @param mobile       参与者的手机号码
     * @param extendParams 扩展参数
     * @return 参与成功返回true, 否则false
     */
    boolean participate(String activityId, String mobile,
                        Map<Object, Object> extendParams,
                        String channel);

    /**
     * 活动开始通知用户
     *
     * @param activities
     * @author qinqinyan
     */
    boolean notifyUserForFlowcard(Activities activities);

    /**
     * 检查该活动余额是否充足，充足返回true，不足返回false
     *
     * @param activities
     * @return
     * @Title: checkAccountEnough
     * @Author: wujiamin
     * @date 2016年8月26日下午4:46:17
     */
    boolean checkAccountEnough(Activities activities);

    /**
     * 获取指定企业的所有活动
     *
     * @param map
     * @author qinqinyan
     */
    List<Activities> selectByEntId(Map map);

    /**
     * 活动上架，返回的是活动上架信息
     *
     * @param activityId 活动uuid
     * @author qinqinyan
     */
    String onShelf(String activityId);

    /** 
     * @Title: checkPrizeAvailable 
     * @param activities
     * @return
     * @Author: wujiamin
     * @date 2016年10月17日下午5:28:03
    */
    String checkPrizeAvailable(Activities activities);


    /**
     * 提交审核(用于二维码、流量券)
     *
     * @author qinqinyan
     */
    Map<String, Object> submitApproval(String activityId, Long currentId,
                                       Long roleId);

    /**
     * 构造活动参数
     *
     * @author qinqinyan
     */
    String getSendData(String activityId);

    /**
     * 发送请求生成活动
     *
     * @author qinqinyan
     */
    AutoResponsePojo sendToGenerateActivity(String activityId);

    /**
     * 生成活动
     *
     * @author qinqinyan
     */
    AutoResponsePojo generateActivivty(Activities activities,
                                       ActivityPrize activityPrize);

    /**
     * 关闭活动
     *
     * @param activities
     * @param activityInfo
     * @param activityPrizes
     * @author qinqinyan
     */
    boolean closeActivity(Activities activities, ActivityInfo activityInfo,
                          List<ActivityPrize> activityPrizes);

    /** 
     * @Title: insertIndividualPresentActivity 
     * @param activityName
     * @param phonesNum
     * @param coinsCount
     * @param adminId
     * @param productId
     * @param ownerMobile
     * @return
     * @Author: wujiamin
     * @date 2016年10月17日下午5:28:08
    */
    boolean insertIndividualPresentActivity(String activityName,
                                            String phonesNum, Integer coinsCount, Long adminId, Long productId, String ownerMobile);

    /**
     * 冻结活动账户(用于集中平台)
     *
     * @author qinqinyan
     */
    boolean frozenActivityAccount(String activityId);

    /**
     * 创建定时任务(用于集中平台)
     *
     * @author qinqinyan
     */
    boolean createActivitySchedule(String activityId);

    /**
     * 校验企业状态、企业余额和活动状态
     *
     * @param activityId
     * @return 企业情况，success,企业状态，余额，活动状态正常
     * @author qinqinyan
     */
    String judgeEnterpriseForActivity(String activityId);

    /** 
     * 批量下架活动
     * @Title: batchDownShelf 
     */
    void batchDownShelf(List<Activities> activities);
    
    /** 
     * @Title: wxCrowdfundingActivityList 
     * 微信众筹活动列表
     */
    List<Activities> wxCrowdfundingActivityList(Map map);

    /** 
     * @Title: joinCrowdfundingActivity 
     */
    boolean joinCrowdfundingActivity(String activityId, String ownMobile, Long prizeId, String wxOpenid);

    /** 
     * 众筹我的活动列表
     * @Title: myCrowdfundingActivityList 
     */
    List<Activities> myCrowdfundingActivityList(Map map);
    /**
     * 因为产品强烈要求特殊展示，so这里额外写一个method
     * 获取活动记录列表
     * @param map
     * @author qinqinyan
     * */
    List<Activities> selectByMapForGDCrowdFunding(Map map);

    /**
     * 因为产品强烈要求特殊展示，so这里额外写一个method
     * 获取活动记录列表
     * @param map
     * @author qinqinyan
     * */
    Long countByMapGDCrowdFunding(Map map);

    /** 
     * 判断黑白名单
     * @Title: checkUser 
     */
    boolean checkUser(String mobile, String activityId);
    
    /**
     * 解密活动ID
     * @param encryptActivitId 加密后的活动ID
     * @param enterpriseCode 企业编码
     * @author qinqinyan
     * */
    Map<String, String> decryptActivityId(String encryptActivitId, String enterpriseCode, String ecProductCode);
    
    /**
     * 加密活动ID
     * @param activitId 活动ID
     * @author qinqinyan
     * */
    String encryptActivityId(String activitId);
    
    /** 
     * 下架四川流量红包活动
     * @author wujiamin
     */
    boolean downShelfIndividualFlowRedpacket(String activityId);

    /** 
     * 四川红包根据订购序列号查找活动
     * @Title: selectForOrder 
     */
    List<Activities> selectForOrder(Long creatorId, String orderSystemNum, Integer type, Integer status);
    
    
    /** 
     * 广东微信查找到最新的大转盘活动
     * @Title: selectLastWxLottery 
     */
    List<Activities> selectLastWxLottery(Map map);
}
