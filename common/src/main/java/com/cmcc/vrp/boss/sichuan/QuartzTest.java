package com.cmcc.vrp.boss.sichuan;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.enums.SchedulerType;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.quartz.jobs.EnterpriseExpireJob;
import com.cmcc.vrp.province.quartz.jobs.EnterpriseExpireJobPojo;
import com.cmcc.vrp.province.quartz.service.ScheduleService;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/applicationContext.xml" })
public class QuartzTest {
    @Autowired
    ScheduleService scheduleService;

    @Test
    public void test() throws ParseException {
        Enterprise enterprise = new Enterprise();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟  
        String dstr="2017-11-30 23:59:59";  
        java.util.Date date= sdf.parse(dstr);
        enterprise.setId(12L);
        enterprise.setEndTime(date);
        EnterpriseExpireJobPojo pojo = new EnterpriseExpireJobPojo(enterprise.getId());
        String jsonStr = JSON.toJSONString(pojo);
        String msg = scheduleService.createScheduleJob(EnterpriseExpireJob.class, SchedulerType.ENTERPRISE_EXPIRE.getCode(), jsonStr, enterprise.getId()
                .toString(), enterprise.getEndTime());

        System.out.print(msg);
    }

}
