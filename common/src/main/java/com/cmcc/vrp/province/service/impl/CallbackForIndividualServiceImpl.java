package com.cmcc.vrp.province.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.IndividualAccountType;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.CallbackPojo;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.CallbackForIndividualService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualFlowOrderService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ActivitiesWinPojo;

/**
 * Created by qinqinyan on 2016/10/9.
 */
@Service("callbackForIndividualService")
public class CallbackForIndividualServiceImpl implements CallbackForIndividualService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackServiceImpl.class);
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    AdministerService administerService;
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    @Autowired
    IndividualAccountService individualAccountService;
    @Autowired
    ChargeRecordService chargeRecordService;
    @Autowired
    ActivityPrizeService activityPrizeService;
    @Autowired
    IndividualProductService individualProductService;
    @Autowired
    TaskProducer producer;
    @Autowired
    ActivityInfoService activityInfoService;
    @Autowired
    IndividualFlowOrderService individualFlowOrderService;

    @Override
    public boolean callback(CallbackPojo pojo, String serialNum) {
        LOGGER.info("callbackPojo= " + pojo.toString());
        
        if(!validate(pojo)){
            LOGGER.info("校验不通过");
            return false;
        }
        
        if (insertInitialRecords(pojo, serialNum)) {
            LOGGER.info("中奖纪录-" + serialNum + "充值成功");
            return true;
        }        
        return false;
    }
    
    /** 
     * 校验
     * @Title: validate 
     */
    private boolean validate(CallbackPojo pojo){
        //1、校验活动状态、活动时间
        Activities activity = activitiesService.selectByActivityId(pojo.getActiveId());
        if(activity == null || !activity.getDeleteFlag().equals(0) || !(ActivityStatus.PROCESSING.getCode()).equals(activity.getStatus())
                || activity.getEndTime().before(new Date())){
            LOGGER.info("activity状态异常, activity={}", JSONObject.toJSONString(activity));
            return false;
        }
        //2、校验活动奖品数量和当前中奖用户数量，活动奖品总数需要大于当前中奖用户数量
        int count = activityWinRecordService.countChargeMobileByActivityId(pojo.getActiveId());
        ActivityInfo activityInfo = activityInfoService.selectByActivityId(pojo.getActiveId());
        if(activityInfo == null){
            LOGGER.info("activityInfo为空, activityId={}", pojo.getActiveId());
            return false;
        }
        if(activityInfo.getPrizeCount()<=count){
            LOGGER.info("已中奖人数数量{}已超过奖品数量{}，activityId={}", count, activityInfo.getPrizeCount(), pojo.getActiveId());
            return false;
        }
        //3、校验当前活动中奖手机号码唯一
        Map map = new HashMap();
        map.put("activityId", pojo.getActiveId());
        map.put("chargeMobile", pojo.getMobile());
        List<ActivityWinRecord> winRecords = activityWinRecordService.selectByMap(map);
        if(winRecords!=null && winRecords.size()>0){
            LOGGER.info("活动acitivityId={}，手机号{}，已经中奖，不能重复中奖", pojo.getActiveId(), pojo.getMobile());
            return false;
        }
        //4、流量账户余额是否足够        
        IndividualAccount activityAccount = individualAccountService.getAccountByOwnerIdAndProductId(activity.getCreatorId(), 
                individualProductService.getDefaultFlowProduct().getId(), IndividualAccountType.INDIVIDUAL_BOSS.getValue());
        if(activityAccount.getCount().intValue() < pojo.getPrizeCount()){
            LOGGER.info("活动activityId={}账户余额{}，中奖需消费{}，余额不足，无法中奖", pojo.getActiveId(), activityAccount.getCount().intValue(),
                    pojo.getPrizeCount() );
            return false;
        }

        return true;
    }

    /**
     * @author qinqinyan
     */
    private boolean insertInitialRecords(CallbackPojo pojo, String serialNum) {
        if (StringUtils.isNotBlank(serialNum)
            && insertRecord(pojo, serialNum)) {
            return true;
        }
        return false;
    }

    private boolean insertRecord(CallbackPojo pojo, String serialNum) {
        boolean bFlag = false;
        ActivityPrize activityPrize = activityPrizeService.selectByPrimaryKey(Long.parseLong(pojo.getPrizeId()));
        //流量红包
        IndividualProduct flow = individualProductService.getDefaultFlowProduct();
        if(activityPrize.getProductId().equals(flow.getId())){
            bFlag = insertIndividualFlowRedpacketRecord(pojo, serialNum);
        }
        
        //流量币红包
        IndividualProduct flowcoin = individualProductService.getFlowcoinProduct();
        if(activityPrize.getProductId().equals(flowcoin.getId())){   
            bFlag = insertIndividualRedpacketRecord(pojo, serialNum);
        }
        
        LOGGER.info("insert activity record,result" + bFlag);
        return bFlag;
    }

    /** 
     * 流量币红包中奖记录
     * @Title: insertIndividualRedpacketRecord 
     */
    @Transactional
    private boolean insertIndividualRedpacketRecord(CallbackPojo pojo, String serialNum) {
        LOGGER.info("Start to insert individual redpacket record.");
        //1、检查是否有个人账户
        Administer administer = administerService.selectByMobilePhone(pojo.getMobile());
        if (administer == null) {
            if (!administerService.insertForScJizhong(pojo.getMobile())) {
                LOGGER.info("fail to create individual account for individual repacket.");
                return false;
            }
        }else{//如果该手机号之前已经是平台的管理员用户，在administer表中会存在该用户，但是该用户并没有个人账户信息，需要检查是否存在账户并插入账户             
            if(!individualAccountService.insertAccountForScJizhong(administer.getId())){
                LOGGER.error("平台已存在该用户，但是用户没有个人账户，创建个人账户时失败，mobile={}，adminId={}", pojo.getMobile(), administer.getId());
                return false;
            }
        }
        
        //2、插入活动中奖纪录记录，扣减冻结账户
        if(!activityWinRecordService.insertForIndividualRedpacket(pojo, serialNum)){
            LOGGER.info("fail to insert win record for individual repacket.");
            return false;
        }
        //3、给个人账户充值
        if (!individualAccountService.chargeFlowcoinForIndividualActivity(pojo.getActiveId(), pojo.getPrizeCount(), pojo.getMobile(), serialNum)) {
            LOGGER.info("fail to charge flowcoin for individual repacket.");
            //4、更新充值记录
            if (!activityWinRecordService.updateForIndividualRedpacket(pojo, serialNum, ChargeRecordStatus.FAILED.getCode(), "充值失败")) {
                LOGGER.info("fail to update charge flowcoin status for individual repacket.");
            }
            return false;
        }
        //4、更新充值记录
        if (!activityWinRecordService.updateForIndividualRedpacket(pojo, serialNum, ChargeRecordStatus.COMPLETE.getCode(), "充值成功")) {
            LOGGER.info("fail to update charge flowcoin status for individual repacket.");
            return false;
        }
        return true;
    }
    
    
    /** 
     * 插入流量红包中奖记录
     * @Title: insertIndividualFlowRedpacketRecord 
     */
    @Transactional
    private boolean insertIndividualFlowRedpacketRecord(CallbackPojo pojo, String serialNum) {
        LOGGER.info("Start to insert individual redpacket record.");

        //1、插入活动中奖纪录记录，扣减账户
        if(!activityWinRecordService.insertForIndividualFlowRedpacket(pojo, serialNum)){
            LOGGER.info("fail to insert win record for individual repacket.");
            return false;
        }
        
        //2、塞入队列
        ActivitiesWinPojo taskPojo = convertToActivitiesWinPojo(pojo, serialNum);
        if(!producer.produceIndividualActivityWinMsg(taskPojo)){
            throw new RuntimeException("消息塞入个人活动队列失败");
        }
        
        return true;
    }
    
    private ActivitiesWinPojo convertToActivitiesWinPojo(CallbackPojo callbackPojo, String serialNum) {
        ActivitiesWinPojo taskPojo = new ActivitiesWinPojo();
        Activities activities = activitiesService.selectByActivityId(callbackPojo.getActiveId());
        taskPojo.setActivities(activities);
        taskPojo.setActivitiesWinRecordId(serialNum);
        return taskPojo;
    }
}
