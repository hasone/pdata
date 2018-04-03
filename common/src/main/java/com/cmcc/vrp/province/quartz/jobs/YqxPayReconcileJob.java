package com.cmcc.vrp.province.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmcc.vrp.pay.service.PayYqxReconcileService;

/**
 * 云企信对账每日对账任务
 *
 */
public class YqxPayReconcileJob implements Job {
    
    @Autowired
    PayYqxReconcileService payYqxReconcileService;

    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        payYqxReconcileService.doDailyJob();
    }

}
