package com.cmcc.vrp.province.quartz.jobs;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmcc.vrp.ec.bean.Constants.ProductType;
import com.cmcc.vrp.province.service.AccountService;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午4:05:40
*/
public class SdMonthlyCleanAccountJob implements Job{

    private Logger logger = Logger.getLogger(SdMonthlyCleanAccountJob.class);

    
    @Autowired
    AccountService accountService;
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("开始启动清空流量池账户定时任务");
        if(!accountService.cleanAccountByTpye(ProductType.FLOW_ACCOUNT)){
            logger.error("流量池账户清空操作失败");
        }else {
            logger.info("流量池账户清空操作成功");
        }    
    }

}
