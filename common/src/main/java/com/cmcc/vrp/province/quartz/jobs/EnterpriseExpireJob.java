/**
 *
 */
package com.cmcc.vrp.province.quartz.jobs;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.enums.EnterpriseStatus;
import com.cmcc.vrp.enums.InterfaceStatus;
import com.cmcc.vrp.province.model.EntECRecord;
import com.cmcc.vrp.province.model.EntStatusRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.EntECRecordService;
import com.cmcc.vrp.province.service.EntStatusRecordService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;

/**
 * @author JamieWu
 *         企业合同到期后，对企业状态进行更新
 */
public class EnterpriseExpireJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(EnterpriseExpireJob.class);

    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    EntECRecordService entECRecordService;
    @Autowired
    EntStatusRecordService entStatusRecordService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("进入企业合同到期定时任务");

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String jsonStr = (String) jobDataMap.get("param");

        logger.info("请求参数：" + jsonStr);
        EnterpriseExpireJobPojo pojo = check(jsonStr);//检查参数

        Enterprise enterprise = enterprisesService.selectByPrimaryKey(pojo.getEntId());
        if (enterprise == null) {
            logger.info("企业合同到期任务企业不存在，企业ID-" + pojo.getEntId());
            return;
        } else {
            if (enterprise.getDeleteFlag().equals(EnterpriseStatus.NORMAL.getCode())) {//企业正常，则暂停企业                
                //生成企业状态变更记录
                EntStatusRecord entStatusRecord = new EntStatusRecord();
                entStatusRecord.setEntId(enterprise.getId());
                entStatusRecord.setCreateTime(new Date());
                entStatusRecord.setUpdateTime(new Date());
                entStatusRecord.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
                entStatusRecord.setPreStatus(enterprise.getDeleteFlag());//企业先前状态，注：enterprise中deleteFlag表企业状态

                enterprise.setDeleteFlag(EnterpriseStatus.PAUSE.getCode());

                entStatusRecord.setNowStatus(enterprise.getDeleteFlag());
                entStatusRecord.setOpType(enterprise.getDeleteFlag());
                entStatusRecord.setOpDesc("企业合同到期，企业暂停");

                if (!entStatusRecordService.insert(entStatusRecord)) {
                    logger.info("企业合同到期暂停企业，生成企业状态变更记录失败，企业ID-" + pojo.getEntId());
                }

                if (enterprise.getInterfaceFlag().equals(InterfaceStatus.OPEN.getCode())) {//企业暂停时，如果企业EC接口开通，则关闭企业EC接口，生成EC操作记录               
                    //生成企业EC接口变更记录
                    EntECRecord entECRecord = new EntECRecord();
                    entECRecord.setEntId(enterprise.getId());
                    entECRecord.setPreStatus(enterprise.getInterfaceFlag());
                    entECRecord.setCreateTime(new Date());
                    entECRecord.setUpdateTime(new Date());
                    entECRecord.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());

                    enterprise.setInterfaceFlag(InterfaceStatus.CLOSE.getCode());//关闭EC

                    entECRecord.setOpType(InterfaceStatus.CLOSE.getCode());
                    entECRecord.setOpDesc("企业合同到期，企业暂停、EC同步关闭");
                    entECRecord.setNowStatus(enterprise.getInterfaceFlag());

                    if (!entECRecordService.insert(entECRecord)) {
                        logger.info("企业合同到期暂停企业，生成企业EC变更记录失败，企业ID-" + pojo.getEntId());
                    }
                }

                if (enterprisesService.updateByPrimaryKeySelective(enterprise)) {
                    logger.info("企业合同到期更新企业表状态为暂停成功，EC状态，企业ID-" + pojo.getEntId());
                } else {
                    logger.info("企业合同到期更新企业表状态为暂停失败，企业ID-" + pojo.getEntId());
                }
            } else {
                logger.info("企业合同到期，此时企业处于非正常状态deleteFlag={}，不进行企业状态修改为暂停。企业ID-{}", enterprise.getDeleteFlag(),
                        pojo.getEntId());
            }

            /*//企业合同到期，所有全部进行中的活动都进行下架处理
            Map map = new HashedMap();
            map.put("entId", pojo.getEntId());
            List<Integer> status = new ArrayList<Integer>();
            status.add(ActivityStatus.ON.getCode());
            status.add(ActivityStatus.PROCESSING.getCode());
            map.put("status", status);
            List<Activities> activities = activitiesService.selectByEntId(map);
            if (activitiesService.batchChangeStatus(activities, ActivityStatus.DOWN.getCode())) {
                logger.info("企业-" + pojo.getEntId() + "活动下架成功");
            } else {
                logger.info("企业-" + pojo.getEntId() + "活动下架失败");
            }*/
        }
    }

    /**
     * 检查传入参数
     *
     * @return
     */
    private EnterpriseExpireJobPojo check(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            logger.error("参数为空");
            return null;
        }
        EnterpriseExpireJobPojo pojo = JSON.parseObject(jsonStr, EnterpriseExpireJobPojo.class);
        if (pojo == null || pojo.getEntId() == null) {
            logger.error("参数为空");
            return null;
        }
        return pojo;
    }
}
