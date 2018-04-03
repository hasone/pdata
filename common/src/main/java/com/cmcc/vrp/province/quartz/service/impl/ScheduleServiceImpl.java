package com.cmcc.vrp.province.quartz.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.enums.SchedulerGroup;
import com.cmcc.vrp.province.quartz.service.ScheduleService;
import com.cmcc.vrp.util.DateUtil;

/**
 * 任务调度器服务类
 *
 * @author qihang
 *         <p>
 *         现在设置任务名称格式是     类型+活动ID+任务执行时间
 *         如 MP_100_20151101000000 代表 执行包月赠送，ruleId=100，执行时间为2015-11-01 00:00:00
 */
@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

    private static Logger logger = Logger.getLogger(ScheduleService.class);

    @Autowired
    private Scheduler scheduler;

    /**
     * 创建任务调度器
     *
     * @param jobClazz：具体任务类
     * @param jobType：任务类型，不允许为空
     * @param id：业务ID，不允许为空
     * @param executeTime：执行日期，不允许为空
     * @param jsonStr:任务数据，json格式
     * @return 创建成功或任务不存在返回"success"  删除失败返回"fail" 其它错误返回相应错误名称
     */
    @Override
    public String createScheduleJob(Class<? extends Job> jobClazz,
                                    String jobType, String jsonStr, String id, Date executeTime) {
        return createScheduleJob(jobClazz, SchedulerGroup.DEFAULT.getCode(), jobType, jsonStr, id, executeTime);
    }

    /**
     * 创建任务调度器
     *
     * @param jobClazz：具体任务类
     * @param groupName：调度器组名，不允许为空
     * @param jobType：任务类型，不允许为空
     * @param id：业务ID，不允许为空
     * @param executeTime：执行日期，不允许为空
     * @return 创建成功或任务不存在返回"success"  删除失败返回"fail" 其它错误返回相应错误名称
     */
    @Override
    public String createScheduleJob(Class<? extends Job> jobClazz,
                                    String groupName, String jobType, String jsonStr, String id,
                                    Date executeTime) {

        //检验各字段是否合法
        String msg = paramValidate(groupName, jobType, id, executeTime);
        if (StringUtils.isNotBlank(msg)) {
            return msg;
        }

        //检验定时任务时间不得早于当前时间
        if (executeTime.getTime() < new Date().getTime()) {
            return "定时任务时间不得早于当前时间";
        }

        //任务名称，格式:  类型_id_运行时间
        String taskName = jobType + "_" + id + "_" + DateUtil.dateToString(executeTime, "yyyyMMddHHmmss");

        // 构造定时任务数据
        JobDetail job = JobBuilder.newJob(jobClazz).withIdentity(taskName, groupName).build();//任务标识
        job.getJobDataMap().put("param", jsonStr);

        try {
            //检测是否已存在同名任务，如存在则删除
            undoScheduleJob(groupName, taskName);

            //创建定时任务
            Trigger newTrigger = TriggerBuilder.newTrigger().withIdentity(taskName, groupName).startAt(executeTime).build();//构造定时任务触发器
            scheduler.scheduleJob(job, newTrigger);//注册任务并进行调度
        } catch (SchedulerException e) {
            logger.error("注册任务并进行调度失败,taskName=" + taskName, e);
            return "failed";
        }
        logger.info("创建任务调度器并注册任务成功,taskName=" + taskName);
        return "success";

    }


    /**
     * 取消注册的定时任务
     *
     * @param jobType：任务类型，不允许为空
     * @param id：业务ID，不允许为空
     * @return 删除成功或任务不存在返回"success"  删除失败返回"fail" 其它错误返回相应错误名称
     */
    @Override
    public String undoScheduleJob(String jobType, String id, Date executeTime) {
        return undoScheduleJob(SchedulerGroup.DEFAULT.getCode(), jobType, id, executeTime);
    }

    /**
     * 取消注册的定时任务
     *
     * @param groupName:调度器组名，不允许为空
     * @param jobType：任务类型，不允许为空
     * @param id：业务ID，不允许为空
     * @return 删除成功或任务不存在返回"success"  删除失败返回"fail" 其它错误返回相应错误名称
     */
    @Override
    public String undoScheduleJob(String groupName, String jobType, String id,
                                  Date executeTime) {
        //检验各字段是否合法
        String msg = paramValidate(groupName, jobType, id, executeTime);
        if (StringUtils.isNotBlank(msg)) {
            return msg;
        }

        //任务格式 "类型"+"id"+"运行时间"
        String taskName = jobType + "_" + id + "_" + DateUtil.dateToString(executeTime, "yyyyMMddHHmmss");
        return undoScheduleJob(groupName, taskName);
    }

    /**
     * 取消注册的定时任务
     *
     * @param taskName:任务名称
     * @return 删除成功或任务不存在返回"success"  删除失败返回"fail" 其它错误返回相应错误名称
     */
    @Override
    public String undoScheduleJob(String taskName) {
        return undoScheduleJob(SchedulerGroup.DEFAULT.getCode(), taskName);
    }

    /**
     * 取消注册的定时任务
     *
     * @param groupName:调度器组名，不允许为空
     * @param taskName:任务名称
     * @return 删除成功或任务不存在返回"success"  删除失败返回"fail" 其它错误返回相应错误名称
     */
    @Override
    public String undoScheduleJob(String groupName, String taskName) {

        if (StringUtils.isBlank(taskName)) {
            return "任务名称不能为空";
        }

        try {
            Trigger trigger = scheduler.getTrigger(new TriggerKey(taskName, groupName));//通过标识取得触发器
            if (trigger == null) {
                return "success";
            }

            //开始取消定时任务
            scheduler.unscheduleJob(trigger.getKey());//取消定时任务
            logger.info("取消定时任务成功,taskName=" + taskName);
            return "success";

        } catch (SchedulerException e) {
            logger.error("取消定时任务失败", e);
            return "failed";
        }
    }


    /**
     * 检验各字段是否合法
     */
    private String paramValidate(String groupName, String jobType, String id, Date excuteTime) {
        if (StringUtils.isBlank(groupName)) {
            return "调度器组名不能为空";
        }
        if (StringUtils.isBlank(jobType)) {
            return "任务类型不能为空";
        }
        if (StringUtils.isBlank(id)) {
            return "任务唯一标识符不能为空";
        }
        if (excuteTime == null) {
            return "运行时间不能为空";
        }
        return null;
    }

    /**
     * 取消所有定时任务
     */
    @Override
    public boolean cancelAllJobs() {
        try {
            scheduler.clear();
            logger.info("删除所有定时任务成功");
        } catch (SchedulerException e) {
            logger.error("删除所有定时任务失败", e);
            return false;
        }
        return true;
    }


    /**
     * 企业EC过期定时任务创建
     * @Title: createScheduleJobForEnterpriseInterfaceExpire 
     * @param jobClazz
     * @param jobType
     * @param jsonStr
     * @param id
     * @param date
     * @param executeTime
     * @return
     * @Author: wujiamin
     * @date 2016年10月19日
     */
    @Override
    public String createScheduleJobForEnterpriseInterfaceExpire(Class<? extends Job> jobClazz,
            String jobType, String jsonStr, String id,
            String date, Date executeTime) {

        //检验各字段是否合法
        String msg = paramValidate(SchedulerGroup.DEFAULT.getCode(), jobType, id, executeTime);
        if (StringUtils.isNotBlank(msg)) {
            return msg;
        }

        //检验定时任务时间不得早于当前时间
        if (executeTime.getTime() < new Date().getTime()) {
            return "定时任务时间不得早于当前时间";
        }

        //任务名称，格式:  类型_id_date
        String taskName = jobType + "_" + id + "_" + date;

        // 构造定时任务数据
        JobDetail job = JobBuilder.newJob(jobClazz).withIdentity(taskName, SchedulerGroup.DEFAULT.getCode()).build();//任务标识
        job.getJobDataMap().put("param", jsonStr);

        try {
            //检测是否已存在同名任务，如存在则删除
            undoScheduleJob(SchedulerGroup.DEFAULT.getCode(), taskName);

            //创建定时任务
            Trigger newTrigger = TriggerBuilder.newTrigger().withIdentity(taskName, SchedulerGroup.DEFAULT.getCode()).startAt(executeTime).build();//构造定时任务触发器
            scheduler.scheduleJob(job, newTrigger);//注册任务并进行调度
        } catch (SchedulerException e) {
            logger.error("注册任务并进行调度失败,taskName=" + taskName, e);
            return "failed";
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        
        logger.info("创建任务调度器并注册任务成功,taskName=" + taskName + ", 执行时间:" + dateFormat.format(executeTime));
        return "success";

    }

    @Override
    public boolean createCronTrigger(Class<? extends Job> jobClazz,
            String cronExpression, String taskName, String groupName) {
     // 构造定时任务数据
        try {
            JobDetail job = JobBuilder.newJob(jobClazz).withIdentity(taskName, groupName).build();//任务标识
            CronTrigger trigger = null;
            trigger = TriggerBuilder.newTrigger()
                       .withIdentity(taskName, groupName)  
                       .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))  
                       .build();    
            scheduler.scheduleJob(job, trigger);//注册任务并进行调度
        } catch (SchedulerException e) {
            logger.error("注册任务并进行调度失败,taskName="+taskName, e);
            return false;
        }
        logger.info("创建任务调度器并注册任务成功,taskName="+taskName);
        
        return true;
    }


}