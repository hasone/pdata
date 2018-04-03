package com.cmcc.vrp.province.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.cmcc.vrp.province.reconcile.service.ReconcileService;

/**
 * 江苏每日对账任务
 *
 */
public class JSReconcileJob implements Job{
    
    @Autowired
    @Qualifier("jxReconcileService")
    ReconcileService reconcileService;

    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        reconcileService.doDailyJob();
    }
}
