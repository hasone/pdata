package com.cmcc.vrp.province.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cmcc.vrp.enums.ActivityWinRecordStatus;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.CallbackPojo;
import com.cmcc.vrp.province.module.CurrentActivityInfo;
import com.cmcc.vrp.util.QueryObject;

/**
 * Created by qinqinyan on 2016/8/17.
 */
public interface ActivityWinRecordService {
    /**
     * selectByPrimaryKey
     */
    ActivityWinRecord selectByPrimaryKey(Long id);

    /**
     * @titile:insertSelective
     * @Description:
     */
    boolean insertSelective(ActivityWinRecord record);


    /**
     * @titile:updateByPrimaryKeySelective
     * @Description:
     */
    boolean updateByPrimaryKeySelective(ActivityWinRecord record);

    /**
     * @titile:updateByPrimaryKey
     * @Description:
     */
    boolean updateByPrimaryKey(ActivityWinRecord record);

    /**
     * @param recordIds
     * @param chargePhone
     * @param newStatus
     * @param oldStatus
     * @param winTime
     * @return
     * @Title: batchUpdateStatus
     * @Description: 批量修改记录
     * @Author: wujiamin
     * @date 2016年8月18日下午1:43:01
     */
    boolean batchUpdateStatus(List<String> recordIds, String chargePhone, int newStatus,
                              int oldStatus, Date winTime);
    
    
    /**
     * 批量修改记录，仅用于流量券，增加了渠道（channel）
     * @author qinqinyan
     * */
    boolean batchUpdateForFlowcard(List<String> recordIds, String chargePhone, int newStatus,
            int oldStatus, Date winTime, String channel);

    /**
     * @param activityId
     * @return
     * @Title: getCurrentActivityInfo
     * @Description: 获得当前活动的情况
     * @Author: wujiamin
     * @date 2016年8月18日下午1:42:57
     */
    CurrentActivityInfo getCurrentActivityInfo(String activityId);

    /**
     * @titile:showForPageResultCount
     * @Description:
     */
    int showForPageResultCount(QueryObject queryObject);
    
    /**
     * 用于广东众筹，prizeId是activity_prize的id，不是product的id
     * @titile:countWinRecords
     * @Description:
     * @author qinqinyan
     */
    int countWinRecords(Map map);

    /**
     * @titile:showForPageResult
     * @Description:
     */
    List<ActivityWinRecord> showForPageResult(QueryObject queryObject);
    
    /**
     * @titile:showForPageResult
     * 用于广东众筹，prizeId是activity_prize的id，不是product的id
     * @Description:
     * @author qinqinyan
     */
    List<ActivityWinRecord> showWinRecords(Map map);

    /**
     * @titile:selectByMap
     * @Description:
     */
    List<ActivityWinRecord> selectByMap(Map map);

    /**
     * 批量插入手机号记录
     *
     * @param activityId
     * @param cmProductId
     * @param cuProductId
     * @param ctProductId
     * @param cmMobileList
     * @param cuMobileList
     * @param ctMobileList
     * @author qinqinyan
     */
    boolean batchInsertForFlowcard(String activityId, Long cmProductId, Long cuProductId, Long ctProductId,
                                   String cmMobileList, String cuMobileList, String ctMobileList);

    /**
     * 批量插入手机号记录
     *
     * @param activityId
     * @param activityInfo
     * @author qinqinyan
     */
    boolean batchInsertForQRcode(String activityId, ActivityInfo activityInfo);

    /**
     * 下载手机号
     *
     * @param request
     * @param response
     * @param phoneList
     * @param operatorId
     * @param filename
     * @author qinqinyan
     */
    boolean downLoadPhones(HttpServletRequest request, HttpServletResponse response,
                           Long operatorId, List<String> phoneList, String filename,
                           String activityType);

    /**
     * 根据activityId获取记录
     *
     * @param activityId
     * @author qinqinyan
     */
    List<ActivityWinRecord> selectByActivityId(String activityId);

    /**
     * 获取指定运营商的记录
     *
     * @param activityId
     * @param isp
     * @author qinqinyan
     */
    List<ActivityWinRecord> selectByActivityIdAndIsp(String activityId, String isp);

    /**
     * 删除原有赠送手机号
     *
     * @param activityId
     * @author qinqinyan
     */
    boolean deleteByActivityId(String activityId);

    /**
     * @titile:selectByRecordId
     * @Description:
     */
    ActivityWinRecord selectByRecordId(String recordId);

    /**
     * 更新状态
     *
     * @titile:
     * @Description:
     */
    boolean updateStatus(Long id, ChargeRecordStatus status, String errorMsg, String phone);


    /**
     * 更新状态
     *
     * @titile:
     * @Description:
     */
    boolean updateActivityStatus(Long id, ActivityWinRecordStatus status, String errorMsg, String phone);

    /**
     * 更新状态
     * @titile:
     * @Description:
     * */
    //boolean updateStatus(Long id, ChargeRecordStatus status, String errorMsg);

    /**
     * 获取个人红包中奖用户
     *
     * @param map
     * @author qinqinyan
     */
    List<ActivityWinRecord> selectByMapForRedpacket(Map map);

    /**
     * @titile:countByMapForRedpacket
     * @Description:
     */
    Long countByMapForRedpacket(Map map);

    /**
     * 获取个人红包中奖用户
     *
     * @param activityId
     * @param ownerMobile
     * @param phonesNum
     * @param productId
     * @author xujue
     */
    boolean batchInsertFlowcoinPresent(String activityId, String phonesNum, Long productId, String ownerMobile);

    /**
     * 插入个人红包中奖用户（集中平台）
     *
     * @author qinqinyan
     */
    boolean insertForIndividualRedpacket(CallbackPojo pojo, String serialNum);

    /**
     * 更新个人红包中奖用户（集中平台）
     *
     * @author qinqinyan
     */
    boolean updateForIndividualRedpacket(CallbackPojo pojo, String serialNum, Integer chargeStatus, String reason);


    /**
     * @return
     * @Title: showForPageResultCountIndividual
     * @Description: 获得个人流量币赠送详情列表
     * @Author: xujue
     * @date 2016年10月10日
     */
    int showForPageResultCountIndividualPresent(QueryObject queryObject);

    /**
     * @titile:showForPageResultIndividualPresent
     * @Description:
     */
    List<ActivityWinRecord> showForPageResultIndividualPresent(QueryObject queryObject);

    /**
     * @titile:batchUpdate
     * @Description:
     */
    boolean batchUpdate(List<ActivityWinRecord> records);
    
    /**
     * 更新updateStatusCodeByRecordId
     * @param recordId   id
     * @param statusCode statusCode
     * @return
     */
    boolean updateStatusCodeByRecordId(String recordId, String statusCode);
    
    /**
     * 更新Status
     * @param sns   sns
     * @param statusCode statusCode
     * @return
     */
    boolean batchUpdateStatusCodeByRecordId(List<String> sns, String statusCode);

    /**
     * 根据活动ID与手机号查找中奖纪录，仅用于广东流量众筹接口（一个活动只会有一个手机号）
     * @param activityId
     * @param mobile
     * @author qinqinyan
     * */
    ActivityWinRecord selectByActivityIdAndMobile(String activityId, String mobile);
    
    /**
     * 获取活动中奖列表，显示于领取流量领取页面，仅用于广东众筹
     * @param map
     * @author qinqinyan
     * */
    List<ActivityWinRecord> getWinRecordsForCrowdFunding(Map map);
    
    /**
     * 获取所有中奖纪录，仅用于广东众筹
     * @param map
     * @author qinqinyan
     * */
    List<ActivityWinRecord> selectAllWinRecords(Map map);

    /** 
     * @Title: countChargeMobileByActivityId 
     */
    int countChargeMobileByActivityId(String activityId);

    /** 
     * 四川流量红包
     * @Title: insertForIndividualFlowRedpacket 
     */
    boolean insertForIndividualFlowRedpacket(CallbackPojo pojo, String serialNum);

    /** 
     * @Title: selectIndividualFlowRedpacketList 
     */
    List<ActivityWinRecord> selectIndividualFlowRedpacketList(Map map);

    /** 
     * @Title: countIndividualFlowRedpacketList 
     */
    Integer countIndividualFlowRedpacketList(Map map);

    /** 
     * @Title: getWinRecordsForCrowdFundingByMap 
     */
    List<ActivityWinRecord> getWinRecordsForCrowdFundingByMap(Map map);
    
    /** 
     * @Title: selectActivityIdByMobile 
     */
    List<String> selectActivityIdByMobile(String mobile);
}
