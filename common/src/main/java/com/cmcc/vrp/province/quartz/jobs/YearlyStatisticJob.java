package com.cmcc.vrp.province.quartz.jobs;

import com.cmcc.vrp.province.model.ChargeStatistic;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.YearChargeStatisticService;
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
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午4:05:51
*/
public class YearlyStatisticJob extends QuartzJobBean {
    private static Logger logger = LoggerFactory.getLogger(YearlyStatisticJob.class);

    @Autowired
    YearChargeStatisticService yearDayStatisticService;
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
            logger.info("进入年充值统计定时任务：date={}", format.format(yesterday));
    
            List<ChargeStatistic> list = yearDayStatisticService.getYearChargeStatistic(yesterday);
    
            if (list.size() > 0) {
                if (yearDayStatisticService.batchInsert(list)) {
                    logger.info("年充值统计定时任务执行成功，插入记录{}条", list.size());
                } else {
                    logger.info("年充值统计定时任务执行失败");
                }
            } else {
                logger.info("无记录，年统计无数据插入");
            }
        }
    }
}
