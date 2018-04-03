package com.cmcc.vrp.province.quartz.jobs;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualFlowOrderService;

/**
 * 每月最后一天23:59:59对当月到期的流量账户进行清空
 * CleanIndividualOrderAccountJob.java
 * @author wujiamin
 * @date 2017年3月3日
 */
public class CleanIndividualAccountJob implements Job {
    private static Logger logger = LoggerFactory.getLogger(CleanIndividualAccountJob.class);    
    
    @Autowired
    GlobalConfigService globalConfigService;
    
    @Autowired
    IndividualAccountService individualAccountService;
    
    @Autowired
    ActivitiesService activitiesService;
    
    @Autowired
    IndividualFlowOrderService individualFlowOrderService;
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("进入清零个人流量账户定时任务");
        Date now = new Date();
        //获取过期的个人流量账户
        List<IndividualAccount> accounts = individualAccountService.getExpireAccount(now);        
        if (accounts!=null && accounts.size() > 0) {
            for(IndividualAccount account : accounts){
                if(account.getCount().intValue()>0){
                    logger.info("清空账户accountId={}", account.getId());
                    if(cleanAccount(account)){
                        logger.info("清空账户accountId={}成功", account.getId());
                    }
                }
            }
        } else {
            logger.info("无满足要求的账户记录，不需要清零");
        }
    }
    
    private boolean cleanAccount(IndividualAccount account){
        //1、清空账户余额
        if(!individualAccountService.changeAccount(account, account.getCount(), SerialNumGenerator.buildSerialNum(),
                (int)AccountRecordType.OUTGO.getValue(), "订购的流量过期清零", ActivityType.INDIVIDUAL_REDPACKAGE_ORDER.getCode(), 0)){
            logger.info("清空账户失败，accountId={}", account.getId());
            return false; 
        }
        
        //2、找到关联该账户的活动并下架
        List<Activities> actList = activitiesService.selectForOrder(account.getAdminId(), null, ActivityType.LUCKY_REDPACKET.getCode(), 
                ActivityStatus.PROCESSING.getCode());
        for(Activities act : actList){
            if(!activitiesService.downShelfIndividualFlowRedpacket(act.getActivityId())){
                logger.info("个人红包活动下架失败，活动activityId={}", act.getActivityId());
            }
        }

        return true;               
    }
}
