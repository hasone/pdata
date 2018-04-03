package com.cmcc.vrp.province.quartz.service;

import org.quartz.Job;

import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午4:07:04
*/
public interface ScheduleService {

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
    String createScheduleJob(Class<? extends Job> jobClazz, String jobType, String jsonStr, String id, Date executeTime);

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
    String createScheduleJob(Class<? extends Job> jobClazz, String groupName, String jobType, String jsonStr, String id, Date executeTime);

    /**
     * 取消注册的定时任务
     *
     * @param jobType：任务类型，不允许为空
     * @param id：业务ID，不允许为空
     * @return 删除成功或任务不存在返回"success"  删除失败返回"fail" 其它错误返回相应错误名称
     */
    String undoScheduleJob(String jobType, String id, Date executeTime);

    /**
     * 取消注册的定时任务
     *
     * @param groupName:调度器组名，不允许为空
     * @param jobType：任务类型，不允许为空
     * @param id：业务ID，不允许为空
     * @return 删除成功或任务不存在返回"success"  删除失败返回"fail" 其它错误返回相应错误名称
     */
    String undoScheduleJob(String groupName, String jobType, String id, Date executeTime);

    /**
     * 取消注册的定时任务
     *
     * @param taskName:任务名称
     * @return 删除成功或任务不存在返回"success"  删除失败返回"fail" 其它错误返回相应错误名称
     */
    String undoScheduleJob(String taskName);

    /**
     * 取消注册的定时任务
     *
     * @param groupName:调度器组名，不允许为空
     * @param taskName:任务名称
     * @return 删除成功或任务不存在返回"success"  删除失败返回"fail" 其它错误返回相应错误名称
     */
    String undoScheduleJob(String groupName, String taskName);

    /**
     * 取消所有定时任务
     */
    boolean cancelAllJobs();

    /**
     * 构建定时任务，企业EC接口过期提醒
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
    String createScheduleJobForEnterpriseInterfaceExpire(Class<? extends Job> jobClazz, 
            String jobType, String jsonStr, String id, String date, Date executeTime);
    
    
    
    /**
     * 创建cronJob
     * qihang
     */
    boolean createCronTrigger(Class<? extends Job> jobClazz,String cronExpression,String taskName,String groupName);

}