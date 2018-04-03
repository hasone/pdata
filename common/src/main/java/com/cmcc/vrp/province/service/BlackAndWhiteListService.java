/**
 * @Title: BlackAndWhiteListService.java
 * @Package com.cmcc.vrp.province.service
 * @author: qinqinyan
 * @version:V2.0
 * @description:活动黑白名单，目前只服务于抢红包活动
 */
package com.cmcc.vrp.province.service;

import java.util.List;

/**
 * 
 *
 */
public interface BlackAndWhiteListService {

    /*
     * 删除对象， 逻辑删除
     */
    /**
     * @param phonelist
     * @param activityId
     * @return
     */
    boolean delete(List<String> phonelist, Long activityId);


    /*
     * 根据活动ID删除所有对象，逻辑删除
     */
    /**
     * @param activityId
     * @return
     */
    int deleteByActivityId(Long activityId);

    /*
     * 批量插入
     */
    /**
     * @param list
     * @param activityType
     * @param activityId
     * @param isWhiteFlag
     * @return
     */
    boolean insertBatch(List<String> list, String activityType, Long activityId, Integer isWhiteFlag);

    /*
     * 根据活动id获取所有手机号
     */
    List<String> getPhonesByActivityId(Long activityId);


}
