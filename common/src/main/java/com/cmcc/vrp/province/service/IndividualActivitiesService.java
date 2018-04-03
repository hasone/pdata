package com.cmcc.vrp.province.service;

import java.util.Map;

import com.cmcc.vrp.ec.bean.individual.IndividualRedpacketActivityParam;
import com.cmcc.vrp.ec.bean.individual.IndividualRedpacketReq;
import com.cmcc.vrp.ec.bean.individual.IndividualRedpacketResp;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityTemplate;
import com.cmcc.vrp.province.module.ScUserInfoRespData;

/**
 * IndividualActivitiesService.java
 * 个人集中化能力平台活动服务
 * @author wujiamin
 * @date 2017年1月11日
 */
public interface IndividualActivitiesService {

    /** 
     * 生成流量红包
     * @Title: generateFlowRedpacket 
     */
    IndividualRedpacketResp generateFlowRedpacket(IndividualRedpacketReq req);

    /** 
     * @Title: insertFlowRedpacket 
     */
    boolean insertFlowRedpacket(Activities activities, ActivityPrize activityPrize, ActivityInfo activityInfo, ActivityTemplate template);

    /** 
     * @Title: generateFlowRedpacketForPage 
     */
    Map generateFlowRedpacketForPage(IndividualRedpacketActivityParam param);
    
    /** 
     * 获取四川和生活用户信息
     * @Title: getScUserInfo 
     */
    ScUserInfoRespData getScUserInfo(String tokenJson);
   
}
