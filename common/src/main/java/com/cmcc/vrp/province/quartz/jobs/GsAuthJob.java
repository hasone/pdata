package com.cmcc.vrp.province.quartz.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.cmcc.vrp.boss.gansu.service.GsAuthService;

/**
 * @author lgk8023
 *
 */
public class GsAuthJob extends QuartzJobBean{
    @Autowired
    GsAuthService gsAuthService;

    @Override
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
        gsAuthService.auth(); 
    }

}
