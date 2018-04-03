package com.cmcc.vrp.province.quartz.jobs;

import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.cmcc.vrp.province.service.DailyStatisticService;

/**
 * 每日的充值报表统计
 *
 * @author wujiamin
 */
//@Component
public class DailyStatisticAnalyseJob extends QuartzJobBean {
    //@Scheduled(cron="0/5 * *  * * ? ")   //每5秒执行一次
    private static Logger logger = LoggerFactory.getLogger(DailyStatisticAnalyseJob.class);

    @Autowired
    DailyStatisticService dailyStatisticService;

    protected void executeInternal(JobExecutionContext context)
        throws JobExecutionException {                       
        DateTime endTime = new DateTime().withTimeAtStartOfDay();
        DateTime startTime = endTime.minusDays(1);
        logger.info("日统计插入数据定时任务：date={}", startTime.toString("yyyy-MM-dd"));
        if (dailyStatisticService.insertDailyStatistic(startTime.toString("yyyy-MM-dd HH:mm:SS"), 
                    endTime.toString("yyyy-MM-dd HH:mm:SS")) > 0) {
            logger.info("日统计插入数据成功");
        } else {
            logger.info("日统计插入数据失败");
        }
            
    }
}

