package com.cmcc.vrp.province.quartz.jobs;

import com.cmcc.vrp.province.model.ChargeStatistic;
import com.cmcc.vrp.province.service.DayChargeStatisticService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 每日的充值报表统计
 *
 * @author wujiamin
 */
//@Component
public class DailyStatisticJob extends QuartzJobBean {
    //@Scheduled(cron="0/5 * *  * * ? ")   //每5秒执行一次
    private static Logger logger = LoggerFactory.getLogger(DailyStatisticJob.class);

    @Autowired
    DayChargeStatisticService dayChargeStatisticService;
    @Autowired
    GlobalConfigService globalConfigService;

    protected void executeInternal(JobExecutionContext context)
        throws JobExecutionException {
        if(!"sc_jizhong".equals(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()))){                         
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            Date yesterday = calendar.getTime();
            logger.info("进入日充值统计定时任务：date={}", format.format(yesterday));
    
            List<ChargeStatistic> list = dayChargeStatisticService.getDayChargeStatistic(yesterday);
    
            if (list.size() > 0) {
                if (dayChargeStatisticService.batchInsert(list)) {
                    logger.info("日充值统计定时任务执行成功，插入记录{}条", list.size());
                } else {
                    logger.info("日充值统计定时任务执行失败");
                }
            } else {
                logger.info("无记录，日统计无数据插入");
            }
        }
    }
}

