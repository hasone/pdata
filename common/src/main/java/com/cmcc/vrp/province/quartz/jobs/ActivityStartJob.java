/**
 *
 */
package com.cmcc.vrp.province.quartz.jobs;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.service.ActivitiesService;
import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 流量券活动开始后更改活动状态
 */
public class ActivityStartJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(ActivityStartJob.class);

    @Autowired
    ActivitiesService activitiesService;

    @Override
    public void execute(JobExecutionContext context)
        throws JobExecutionException {
        logger.info("进入营销活动定时任务");

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String jsonStr = (String) jobDataMap.get("param");

        logger.info("请求参数：" + jsonStr);
        ActivityJobPojo pojo = check(jsonStr);//检查参数

        Activities activities = activitiesService.selectByActivityId(pojo.getActivityId());

        if (activities == null) {
            logger.info("该营销活动不存在，活动ID-" + pojo.getActivityId());
            return;
        } else {
            if (activities.getStatus().toString().equals(ActivityStatus.ON.getCode().toString())
                && activities.getDeleteFlag().toString().equals("0")) {
                if (activitiesService.changeStatus(pojo.getActivityId(), ActivityStatus.PROCESSING.getCode())) {
                    logger.info("营销活动状态变更为活动进行中,活动ID-" + pojo.getActivityId());

                    if (activities.getType().toString().equals(ActivityType.FLOWCARD.getCode().toString())) {
                        //流量券下发短信
                        if (activitiesService.notifyUserForFlowcard(activities)) {
                            logger.info("营销活动活动下发短信成功,活动ID-" + pojo.getActivityId());
                        }
                    }
                } else {
                    logger.info("营销活动状态变更为活动进行中失败,活动ID-" + pojo.getActivityId());
                }
            } else {
                logger.info("营销活动状态变更为活动进行中失败,活动ID-" + pojo.getActivityId() + " ;活动状态-"
                    + activities.getStatus() + " ;活动删除状态-" + activities.getDeleteFlag());
            }
        }
    }

    /**
     * 检查传入参数
     *
     * @return
     */
    private ActivityJobPojo check(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            logger.error("参数为空");
            return null;
        }
        ActivityJobPojo pojo = JSON.parseObject(jsonStr, ActivityJobPojo.class);
        if (pojo == null || StringUtils.isBlank(pojo.getActivityId())) {
            logger.error("参数为空");
            return null;
        }
        return pojo;
    }
}
