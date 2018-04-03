package com.cmcc.vrp.queue.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.RedisUtilService;
import com.cmcc.vrp.charge.ChargeService;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SendMsgService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.UrlMapService;
import com.cmcc.vrp.queue.pojo.ActivitySendMessagePojo;
/**
 * Created by qinqinyan on 2017/08/09.
 */
@RunWith(MockitoJUnitRunner.class)
public class ActivitySendMessageWorkerTest {
    @InjectMocks
    ActivitySendMessageWorker worker = new ActivitySendMessageWorker();
    
    @Mock
    ActivityWinRecordService activityWinRecordService;
    @Mock
    ChargeRecordService chargeRecordService;
    @Mock
    ChargeService chargeService;
    @Mock
    SerialNumService serialNumService;
    @Mock
    ApplicationContext applicationContext;
    @Mock
    AccountService accountService;
    @Mock
    ActivityPrizeService activityPrizeService;
    @Mock
    ActivitiesService activitiesService;
    @Mock
    EnterprisesService enterprisesService;
    @Mock
    SendMsgService sendMsgService;
    @Mock
    GlobalConfigService globalConfigService;
    @Mock
    ProductService productService;
    @Mock
    RedisUtilService redisUtilService;
    @Mock
    UrlMapService urlMapService;
    
    private ActivitySendMessagePojo createPojo(){
        ActivitySendMessagePojo pojo = new ActivitySendMessagePojo();
        pojo.setActivityId("test");
        return pojo;
    }
    
    
    private Activities createActivities(){
        Activities activities = new Activities();
        activities.setActivityId("test");
        activities.setType(ActivityType.FLOWCARD.getCode());
        return activities;
    }
    
    @Test
    public void exec(){
        worker.exec();
        
        worker.setTaskString(JSONObject.toJSONString(createPojo()));
        
        Mockito.when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(createActivities());
        Mockito.when(activitiesService.notifyUserForFlowcard(Mockito.any(Activities.class))).thenReturn(false).thenReturn(true);
        worker.exec();
        worker.exec();
        
    }

}
