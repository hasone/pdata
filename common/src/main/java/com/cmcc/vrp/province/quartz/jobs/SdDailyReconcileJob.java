package com.cmcc.vrp.province.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmcc.vrp.boss.shangdong.reconcile.SdReconcileFuncService;

/**
 * 山东每日对账任务
 *
 */
public class SdDailyReconcileJob implements Job {

    @Autowired
    private SdReconcileFuncService sdReconcileFuncService;

    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        sdReconcileFuncService.doDailyJob();
    }

}
