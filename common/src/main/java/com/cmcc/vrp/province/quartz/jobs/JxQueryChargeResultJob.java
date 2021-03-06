package com.cmcc.vrp.province.quartz.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.cmcc.vrp.boss.jiangxi.JxReconcileService;

/**
 * @author lgk8023
 *
 */
public class JxQueryChargeResultJob extends QuartzJobBean{
    @Autowired
    JxReconcileService jxReconcileService;

    @Override
    protected void executeInternal(JobExecutionContext paramJobExecutionContext) throws JobExecutionException {
        jxReconcileService.doDailyJob();         
    }




}
