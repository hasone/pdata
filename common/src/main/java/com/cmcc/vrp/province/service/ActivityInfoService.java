package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;

/**
 * Created by qinqinyan on 2016/8/17.
 */
public interface ActivityInfoService {

    /**
     * @param record
     * @return
     */
    boolean insert(ActivityInfo record);

    /**
     * @param record
     * @return
     */
    boolean updateByPrimaryKeySelective(ActivityInfo record);

    /**
     * @param activityId
     * @return
     */
    ActivityInfo selectByActivityId(String activityId);

    /**
     * 插入活动信息详情
     *
     * @param activities
     * @param cuProductId
     * @param ctProductId
     * @param cmProductId
     * @param cmMobileList
     * @param ctMobileList
     * @param cuMobileList
     * @param cmUserSet
     * @param ctUserSet
     * @param cuUserSet
     * @author qinqinyan
     */
    boolean insertActivityInfo(Activities activities,
                               Long cmProductId, Long cuProductId, Long ctProductId,
                               String cmMobileList, String cuMobileList, String ctMobileList,
                               String cmUserSet, String cuUserSet, String ctUserSet);

    /**
     * 更新活动信息详情
     *
     * @param activityId
     * @param cmProductId
     * @param cuProductId
     * @param ctProductId
     * @param cmMobileCnt
     * @param cuMobileCnt
     * @param ctMobileCnt
     * @param cuUserCnt
     * @param cmUserCnt
     * @param ctUserCnt
     * @author qinqinyan
     */
    boolean updateActivityInfo(String activityId,
                               Long cmProductId, Long cuProductId, Long ctProductId,
                               Long cmMobileCnt, Long cuMobileCnt, Long ctMobileCnt,
                               Long cmUserCnt, Long cuUserCnt, Long ctUserCnt);

    /**
     * 二维码插入活动详情
     *
     * @param activities
     * @param activityInfo
     * @param cmProductId
     * @param ctProductId
     * @param cuProductId
     * @author qinqinyan
     */
    boolean insertActivityInfoForQrcode(Activities activities, ActivityInfo activityInfo,
                                        Long cmProductId, Long cuProductId, Long ctProductId);

    /**
     * 二维码更新活动详情
     *
     * @param activities
     * @param activityInfo
     * @param cmProductId
     * @param ctProductId
     * @param cuProductId
     * @author qinqinyan
     */
    boolean updateActivityInfoForQrcode(Activities activities, ActivityInfo activityInfo,
                                        Long cmProductId, Long cuProductId, Long ctProductId);

    /**
     * 个人红包详情
     *
     * @param activityInfo
     * @author qinqinyan
     */
    boolean insertForRedpacket(ActivityInfo activityInfo);

    /**
     * 插入
     * @param activityInfo
     * @author qinqinyan
     * */
    boolean insertSelective(ActivityInfo activityInfo);


    /**
     * 更新随机红包使用额度和参与人数
     * @param activityInfo
     * @author qinqinyan
     * */
    boolean updateForRendomPacket(ActivityInfo activityInfo);

}
