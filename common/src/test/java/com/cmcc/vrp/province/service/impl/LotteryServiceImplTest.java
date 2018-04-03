package com.cmcc.vrp.province.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.enums.ActivityPrizeRank;
import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.enums.ActivityTemplateType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.IspType;
import com.cmcc.vrp.province.activity.model.AutoGeneratePojo;
import com.cmcc.vrp.province.activity.model.AutoPrizesPojo;
import com.cmcc.vrp.province.activity.model.AutoTimePojo;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityTemplate;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.quartz.jobs.ActivityEndJob;
import com.cmcc.vrp.province.quartz.jobs.ActivityStartJob;
import com.cmcc.vrp.province.quartz.service.ScheduleService;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityCreatorService;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityTemplateService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.HttpConnection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by qinqinyan on 2016/11/15.
 */
@PrepareForTest({HttpConnection.class})
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
public class LotteryServiceImplTest {
    @InjectMocks
    @Qualifier("lotteryService")
    LotteryServiceImpl lotteryService = new LotteryServiceImpl();
    @Mock
    ActivitiesService activitiesService;
    @Mock
    ActivityTemplateService activityTemplateService;
    @Mock
    ActivityInfoService activityInfoService;
    @Mock
    ActivityPrizeService activityPrizeService;
    @Mock
    ScheduleService scheduleService;
    @Mock
    EnterprisesService enterprisesService;
    @Mock
    GlobalConfigService globalConfigService;
    @Mock
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    @Mock
    ProductService productService;
    @Mock
    ActivityCreatorService activityCreatorService;

    /**
     * insertActivity
     * 分支1：插入活动失败
     * */
    @Test
    public void testInsertActivity1() throws RuntimeException{
        Activities activities1 = createActivities(1L);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        assertFalse(lotteryService.insertActivity(activities1, activityPrizeList,
            null, null));

        Activities activities2 = createActivities(2L);
        ActivityTemplate activityTemplate = new ActivityTemplate();
        activityTemplate.setId(1L);

        Mockito.when(activitiesService.insert(any(Activities.class))).thenReturn(false);
        assertFalse(lotteryService.insertActivity(activities2, activityPrizeList, activityTemplate, null));
        Mockito.verify(activitiesService).insert(any(Activities.class));
    }

    /**
     * insertActivity
     * 分支2：插入活动奖品失败
     * */
    @Test(expected = RuntimeException.class)
    public void testInsertActivity2() throws RuntimeException{
        when(activityCreatorService.insert(Mockito.any(ActivityType.class), Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);
        Activities activities1 = createActivities(1L);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        ActivityTemplate activityTemplate = new ActivityTemplate();
        activityTemplate.setId(1L);

        Mockito.when(activitiesService.insert(any(Activities.class))).thenReturn(true);
        Mockito.when(activityPrizeService.batchInsert(anyList())).thenReturn(false);

        assertFalse(lotteryService.insertActivity(activities1, activityPrizeList, activityTemplate, null));
    }

    /**
     * insertActivity
     * 分支3：插入营销活动模板失败
     * */
    @Test(expected = RuntimeException.class)
    public void testInsertActivity3() throws RuntimeException{
        when(activityCreatorService.insert(Mockito.any(ActivityType.class), Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);
        Activities activities1 = createActivities(1L);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        ActivityTemplate activityTemplate = new ActivityTemplate();
        activityTemplate.setId(1L);

        Mockito.when(activitiesService.insert(any(Activities.class))).thenReturn(true);
        Mockito.when(activityPrizeService.batchInsert(anyList())).thenReturn(true);
        Mockito.when(activityTemplateService.insertSelective(any(ActivityTemplate.class))).thenReturn(false);

        assertFalse(lotteryService.insertActivity(activities1, activityPrizeList, activityTemplate, null));
    }

    /**
     * insertActivity
     * 分支4：插入营销活动模板失败
     * */
    @Test(expected = RuntimeException.class)
    public void testInsertActivity4() throws RuntimeException{
        when(activityCreatorService.insert(Mockito.any(ActivityType.class), Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);
        Activities activities1 = createActivities(1L);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        ActivityTemplate activityTemplate = new ActivityTemplate();
        activityTemplate.setId(1L);

        Mockito.when(activitiesService.insert(any(Activities.class))).thenReturn(true);
        Mockito.when(activityPrizeService.batchInsert(anyList())).thenReturn(true);
        Mockito.when(activityTemplateService.insertSelective(any(ActivityTemplate.class))).thenReturn(true);
        Mockito.when(activityInfoService.insertSelective(any(ActivityInfo.class))).thenReturn(false);

        assertFalse(lotteryService.insertActivity(activities1, activityPrizeList,
            activityTemplate, new ActivityInfo()));
    }

    /**
     * insertActivity
     * 正常流程
     * */
    @Test
    public void testInsertActivity5() throws RuntimeException{
        Activities activities1 = createActivities(1L);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        ActivityTemplate activityTemplate = new ActivityTemplate();
        activityTemplate.setId(1L);

        when(activityCreatorService.insert(Mockito.any(ActivityType.class), Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(activitiesService.insert(any(Activities.class))).thenReturn(true);
        Mockito.when(activityPrizeService.batchInsert(anyList())).thenReturn(true);
        Mockito.when(activityTemplateService.insertSelective(any(ActivityTemplate.class))).thenReturn(true);
        Mockito.when(activityInfoService.insertSelective(any(ActivityInfo.class))).thenReturn(true);

        assertTrue(lotteryService.insertActivity(activities1, activityPrizeList,
            activityTemplate, new ActivityInfo()));

        Mockito.verify(activitiesService).insert(any(Activities.class));
        Mockito.verify(activityPrizeService).batchInsert(anyList());
        Mockito.verify(activityTemplateService).insertSelective(any(ActivityTemplate.class));
        Mockito.verify(activityInfoService).insertSelective(any(ActivityInfo.class));
    }



    private Activities createActivities(Long id){
        Activities activities = new Activities();
        activities.setId(id);
        activities.setType(ActivityType.LOTTERY.getCode());
        activities.setEntId(1L);

        activities.setStartTime(new Date());
        activities.setEndTime(new Date());
        activities.setName("test");
        activities.setType(ActivityType.LOTTERY.getCode());

        activities.setStatus(ActivityStatus.SAVED.getCode());
        return activities;
    }

    private List<ActivityPrize> createActivityPrizeList(){
        List<ActivityPrize> activityPrizeList = new ArrayList<ActivityPrize>();
        ActivityPrize activityPrize = new ActivityPrize();
        activityPrize.setId(1L);
        activityPrize.setIdPrefix("0");
        activityPrize.setRankName("一等奖");
        activityPrize.setCount(1L);
        activityPrize.setProductId(1L);
        activityPrizeList.add(activityPrize);
        return activityPrizeList;
    }

    @Test
    public void testInitActivity(){
        Activities activities1 = createActivities(1L);
        lotteryService.initActivity(activities1);

        Activities activities2 = createActivities(2L);
        activities2.setType(ActivityType.LUCKY_REDPACKET.getCode());
        lotteryService.initActivity(activities2);
    }

    /**
     * 创建活动开始定时任务测试方法
     * */
    @Test
    public void testCreateActivityStartSchedule(){
        String activityId = "test";
        String schedulerType = "testType";
        Date startTime = new Date();

        assertFalse(lotteryService.createActivityStartSchedule(activityId, schedulerType, null));

        Mockito.when(scheduleService.createScheduleJob(any(Class.class), anyString(), anyString(),
            anyString(), any(Date.class))).thenReturn("success");
        assertTrue(lotteryService.createActivityStartSchedule(activityId, schedulerType, startTime));
        Mockito.verify(scheduleService).createScheduleJob(any(Class.class), anyString(), anyString(),
            anyString(), any(Date.class));
    }

    /**
     * 创建活动结束定时任务测试方法
     * */
    @Test
    public void testCreateActivityEndSchedule(){
        String activityId = "test";
        String schedulerType = "testType";
        Date endTime = new Date();

        assertFalse(lotteryService.createActivityEndSchedule(activityId, schedulerType, null));

        Mockito.when(scheduleService.createScheduleJob(any(Class.class), anyString(), anyString(),
            anyString(), any(Date.class))).thenReturn("success");
        assertTrue(lotteryService.createActivityEndSchedule(activityId, schedulerType, endTime));
        Mockito.verify(scheduleService).createScheduleJob(any(Class.class), anyString(), anyString(),
            anyString(), any(Date.class));
    }

    /**
     * 测试上架
     * 分支1:请求生成活动失败
     * */
    @Test
    public void testOnShelf1(){
        String activityId1 = "test1";
        String schedulerStart = "start";
        String schedulerEnd = "end";

        Mockito.when(activitiesService.judgeEnterpriseForActivity(activityId1)).thenReturn("fail");
        assertSame("fail", lotteryService.onShelf(activityId1, schedulerStart, schedulerEnd));

        String activityId2 = "test2";
        Mockito.when(activitiesService.judgeEnterpriseForActivity(activityId2)).thenReturn("success");

        Activities activities = createActivities(1L);
        ActivityTemplate activityTemplate = createActivityTemplate(1L);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        Enterprise enterprise = createEnterprise();

        Product product = createProduct("M", 1L);
        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);

        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(activities);
        Mockito.when(activityTemplateService.selectByActivityId(anyString())).thenReturn(activityTemplate);
        Mockito.when(activityPrizeService.selectByActivityId(anyString())).thenReturn(activityPrizeList);
        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(globalConfigService.get("ACTIVITY_PLATFORM_NAME")).thenReturn("sc_test");

        Mockito.when(globalConfigService.get("ACTIVITY_GENERATE_URL"))
            .thenReturn("http://activitytest.4ggogo.com/template/service/autoGenerate/make");
        PowerMockito.mockStatic(HttpConnection.class);
        PowerMockito.when(HttpConnection.doPost(anyString(), anyString(),
            anyString(), anyString(), anyBoolean()))
            .thenReturn("{\"code\":201,\"url\":\"http:×××\",\"msg\":\"生成活动失败\"}");

        assertNotNull(lotteryService.onShelf(activityId2, schedulerStart, schedulerEnd));

        Mockito.verify(activitiesService, Mockito.atLeastOnce()).judgeEnterpriseForActivity(anyString());
        Mockito.verify(activitiesService).selectByActivityId(anyString());
        Mockito.verify(activityTemplateService).selectByActivityId(anyString());
        Mockito.verify(activityPrizeService).selectByActivityId(anyString());
        Mockito.verify(globalConfigService, Mockito.atLeastOnce()).get(anyString());

        Mockito.verify(productService).selectProductById(anyLong());
    }

    /**
     * 测试上架
     * 分支2:创建活动开始定时任务异常
     * */
    @Test
    public void testOnShelf2(){
        String schedulerStart = "start";
        String schedulerEnd = "end";
        String activityId2 = "test2";
        Activities activities = createActivities(1L);
        activities.setStartTime(getDay(new Date(), 1));
        activities.setEndTime(getDay(new Date(), 3));

        ActivityTemplate activityTemplate = createActivityTemplate(1L);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        Enterprise enterprise = createEnterprise();

        Product product = createProduct("M", 1L);
        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);

        Mockito.when(activitiesService.judgeEnterpriseForActivity(activityId2)).thenReturn("success");
        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(activities);
        Mockito.when(activityTemplateService.selectByActivityId(anyString())).thenReturn(activityTemplate);
        Mockito.when(activityPrizeService.selectByActivityId(anyString())).thenReturn(activityPrizeList);
        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(globalConfigService.get("ACTIVITY_PLATFORM_NAME")).thenReturn("sc_test");

        Mockito.when(globalConfigService.get("ACTIVITY_GENERATE_URL"))
            .thenReturn("http://activitytest.4ggogo.com/template/service/autoGenerate/make");
        PowerMockito.mockStatic(HttpConnection.class);
        PowerMockito.when(HttpConnection.doPost(anyString(), anyString(),
            anyString(), anyString(), anyBoolean()))
            .thenReturn("{\"code\":200,\"url\":\"http:×××\",\"msg\":\"生成活动成功\"}");

        Mockito.when(approvalProcessDefinitionService.wheatherNeedApproval(anyInt())).thenReturn(false);

        Mockito.when(activitiesService.changeStatus(anyString(), anyInt())).thenReturn(true);
        Mockito.when(scheduleService.createScheduleJob(any(Class.class), anyString(), anyString(),
            anyString(), any(Date.class))).thenReturn("fail");


        assertNotNull(lotteryService.onShelf(activityId2, schedulerStart, schedulerEnd));

        Mockito.verify(activitiesService, Mockito.atLeastOnce()).judgeEnterpriseForActivity(anyString());
        Mockito.verify(activitiesService, Mockito.atLeastOnce()).selectByActivityId(anyString());
        Mockito.verify(activityTemplateService).selectByActivityId(anyString());
        Mockito.verify(activityPrizeService).selectByActivityId(anyString());
        Mockito.verify(globalConfigService, Mockito.atLeastOnce()).get(anyString());
        Mockito.verify(approvalProcessDefinitionService).wheatherNeedApproval(anyInt());
        Mockito.verify(activitiesService).changeStatus(anyString(), anyInt());
        Mockito.verify(scheduleService).createScheduleJob(any(Class.class), anyString(), anyString(),
            anyString(), any(Date.class));

        Mockito.verify(productService).selectProductById(anyLong());
    }

    /**
     * 测试上架
     * 分支3:创建活动结束定时任务异常
     * */
    @Test
    public void testOnShelf3(){
        String schedulerStart = "start";
        String schedulerEnd = "end";
        String activityId2 = "test2";
        Activities activities = createActivities(1L);
        activities.setStartTime(getDay(new Date(), 1));
        activities.setEndTime(getDay(new Date(), 3));

        ActivityTemplate activityTemplate = createActivityTemplate(1L);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        Enterprise enterprise = createEnterprise();

        Product product = createProduct("M", 1L);
        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);

        Mockito.when(activitiesService.judgeEnterpriseForActivity(activityId2)).thenReturn("success");
        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(activities);
        Mockito.when(activityTemplateService.selectByActivityId(anyString())).thenReturn(activityTemplate);
        Mockito.when(activityPrizeService.selectByActivityId(anyString())).thenReturn(activityPrizeList);
        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(globalConfigService.get("ACTIVITY_PLATFORM_NAME")).thenReturn("sc_test");

        Mockito.when(globalConfigService.get("ACTIVITY_GENERATE_URL"))
            .thenReturn("http://activitytest.4ggogo.com/template/service/autoGenerate/make");
        PowerMockito.mockStatic(HttpConnection.class);
        PowerMockito.when(HttpConnection.doPost(anyString(), anyString(),
            anyString(), anyString(), anyBoolean()))
            .thenReturn("{\"code\":200,\"url\":\"http:×××\",\"msg\":\"生成活动成功\"}");

        Mockito.when(approvalProcessDefinitionService.wheatherNeedApproval(anyInt())).thenReturn(false);

        Mockito.when(activitiesService.changeStatus(anyString(), anyInt())).thenReturn(true);

        Mockito.when(scheduleService.createScheduleJob(ActivityStartJob.class, schedulerStart, "{\"activityId\":\"test2\"}",
            activityId2, activities.getStartTime())).thenReturn("success");

        Mockito.when(scheduleService.createScheduleJob(ActivityEndJob.class, schedulerEnd, "{\"activityId\":\"test2\"}",
            activityId2, activities.getEndTime())).thenReturn("fail");


        assertNotNull(lotteryService.onShelf(activityId2, schedulerStart, schedulerEnd));

        Mockito.verify(activitiesService, Mockito.atLeastOnce()).judgeEnterpriseForActivity(anyString());
        Mockito.verify(activitiesService, Mockito.atLeastOnce()).selectByActivityId(anyString());
        Mockito.verify(activityTemplateService).selectByActivityId(anyString());
        Mockito.verify(activityPrizeService).selectByActivityId(anyString());
        Mockito.verify(globalConfigService, Mockito.atLeastOnce()).get(anyString());
        Mockito.verify(approvalProcessDefinitionService).wheatherNeedApproval(anyInt());
        Mockito.verify(activitiesService).changeStatus(anyString(), anyInt());
        Mockito.verify(scheduleService, Mockito.atLeastOnce()).createScheduleJob(any(Class.class), anyString(), anyString(),
            anyString(), any(Date.class));

        Mockito.verify(productService).selectProductById(anyLong());
    }

    /**
     * 测试上架
     * 正常流程
     * */
    @Test
    public void testOnShelf4(){
        String schedulerStart = "start";
        String schedulerEnd = "end";
        String activityId2 = "test2";
        Activities activities = createActivities(1L);
        activities.setStartTime(getDay(new Date(), 1));
        activities.setEndTime(getDay(new Date(), 3));

        ActivityTemplate activityTemplate = createActivityTemplate(1L);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        Enterprise enterprise = createEnterprise();

        Product product = createProduct("M", 1L);
        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);

        Mockito.when(activitiesService.judgeEnterpriseForActivity(activityId2)).thenReturn("success");
        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(activities);
        Mockito.when(activityTemplateService.selectByActivityId(anyString())).thenReturn(activityTemplate);
        Mockito.when(activityPrizeService.selectByActivityId(anyString())).thenReturn(activityPrizeList);
        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(globalConfigService.get("ACTIVITY_PLATFORM_NAME")).thenReturn("sc_test");

        Mockito.when(globalConfigService.get("ACTIVITY_GENERATE_URL"))
            .thenReturn("http://activitytest.4ggogo.com/template/service/autoGenerate/make");
        PowerMockito.mockStatic(HttpConnection.class);
        PowerMockito.when(HttpConnection.doPost(anyString(), anyString(),
            anyString(), anyString(), anyBoolean()))
            .thenReturn("{\"code\":200,\"url\":\"http:×××\",\"msg\":\"生成活动成功\"}");

        Mockito.when(approvalProcessDefinitionService.wheatherNeedApproval(anyInt())).thenReturn(false);

        Mockito.when(activitiesService.changeStatus(anyString(), anyInt())).thenReturn(true);

        Mockito.when(scheduleService.createScheduleJob(ActivityStartJob.class, schedulerStart, "{\"activityId\":\"test2\"}",
            activityId2, activities.getStartTime())).thenReturn("success");

        Mockito.when(scheduleService.createScheduleJob(ActivityEndJob.class, schedulerEnd, "{\"activityId\":\"test2\"}",
            activityId2, activities.getEndTime())).thenReturn("success");


        assertNotNull(lotteryService.onShelf(activityId2, schedulerStart, schedulerEnd));

        Mockito.verify(activitiesService, Mockito.atLeastOnce()).judgeEnterpriseForActivity(anyString());
        Mockito.verify(activitiesService, Mockito.atLeastOnce()).selectByActivityId(anyString());
        Mockito.verify(activityTemplateService).selectByActivityId(anyString());
        Mockito.verify(activityPrizeService).selectByActivityId(anyString());
        Mockito.verify(globalConfigService, Mockito.atLeastOnce()).get(anyString());
        Mockito.verify(approvalProcessDefinitionService).wheatherNeedApproval(anyInt());
        Mockito.verify(activitiesService).changeStatus(anyString(), anyInt());
        Mockito.verify(scheduleService, Mockito.atLeastOnce()).createScheduleJob(any(Class.class), anyString(), anyString(),
            anyString(), any(Date.class));

        Mockito.verify(productService).selectProductById(anyLong());
    }

    /**
     * 测试上架
     * 分支4:创建活动结束定时任务异常
     * */
    @Test
    public void testOnShelf5(){
        String schedulerStart = "start";
        String schedulerEnd = "end";
        String activityId2 = "test2";
        Activities activities = createActivities(1L);
        activities.setStartTime(getDay(new Date(), -1));
        activities.setEndTime(getDay(new Date(), 3));

        ActivityTemplate activityTemplate = createActivityTemplate(1L);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        Enterprise enterprise = createEnterprise();

        Product product = createProduct("M", 1L);
        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);

        Mockito.when(activitiesService.judgeEnterpriseForActivity(activityId2)).thenReturn("success");
        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(activities);
        Mockito.when(activityTemplateService.selectByActivityId(anyString())).thenReturn(activityTemplate);
        Mockito.when(activityPrizeService.selectByActivityId(anyString())).thenReturn(activityPrizeList);
        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(globalConfigService.get("ACTIVITY_PLATFORM_NAME")).thenReturn("sc_test");

        Mockito.when(globalConfigService.get("ACTIVITY_GENERATE_URL"))
            .thenReturn("http://activitytest.4ggogo.com/template/service/autoGenerate/make");
        PowerMockito.mockStatic(HttpConnection.class);
        PowerMockito.when(HttpConnection.doPost(anyString(), anyString(),
            anyString(), anyString(), anyBoolean()))
            .thenReturn("{\"code\":200,\"url\":\"http:×××\",\"msg\":\"生成活动成功\"}");

        Mockito.when(approvalProcessDefinitionService.wheatherNeedApproval(anyInt())).thenReturn(false);

        Mockito.when(activitiesService.changeStatus(anyString(), anyInt())).thenReturn(true);

        Mockito.when(scheduleService.createScheduleJob(ActivityEndJob.class, schedulerEnd, "{\"activityId\":\"test2\"}",
            activityId2, activities.getEndTime())).thenReturn("fail");


        assertNotNull(lotteryService.onShelf(activityId2, schedulerStart, schedulerEnd));

        Mockito.verify(activitiesService, Mockito.atLeastOnce()).judgeEnterpriseForActivity(anyString());
        Mockito.verify(activitiesService, Mockito.atLeastOnce()).selectByActivityId(anyString());
        Mockito.verify(activityTemplateService).selectByActivityId(anyString());
        Mockito.verify(activityPrizeService).selectByActivityId(anyString());
        Mockito.verify(globalConfigService, Mockito.atLeastOnce()).get(anyString());
        Mockito.verify(approvalProcessDefinitionService).wheatherNeedApproval(anyInt());
        Mockito.verify(activitiesService).changeStatus(anyString(), anyInt());
        Mockito.verify(scheduleService).createScheduleJob(any(Class.class), anyString(), anyString(),
            anyString(), any(Date.class));

        Mockito.verify(productService).selectProductById(anyLong());
    }

    /**
     * 测试上架
     * 分支5:正常流程，活动已经开始，只需要创建活动结束定时任务
     * */
    @Test
    public void testOnShelf6(){
        String schedulerStart = "start";
        String schedulerEnd = "end";
        String activityId2 = "test2";
        Activities activities = createActivities(1L);
        activities.setStartTime(getDay(new Date(), -1));
        activities.setEndTime(getDay(new Date(), 3));

        ActivityTemplate activityTemplate = createActivityTemplate(1L);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        Enterprise enterprise = createEnterprise();

        Product product = createProduct("M", 1L);
        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);

        Mockito.when(activitiesService.judgeEnterpriseForActivity(activityId2)).thenReturn("success");
        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(activities);
        Mockito.when(activityTemplateService.selectByActivityId(anyString())).thenReturn(activityTemplate);
        Mockito.when(activityPrizeService.selectByActivityId(anyString())).thenReturn(activityPrizeList);
        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(globalConfigService.get("ACTIVITY_PLATFORM_NAME")).thenReturn("sc_test");

        Mockito.when(globalConfigService.get("ACTIVITY_GENERATE_URL"))
            .thenReturn("http://activitytest.4ggogo.com/template/service/autoGenerate/make");
        PowerMockito.mockStatic(HttpConnection.class);
        PowerMockito.when(HttpConnection.doPost(anyString(), anyString(),
            anyString(), anyString(), anyBoolean()))
            .thenReturn("{\"code\":200,\"url\":\"http:×××\",\"msg\":\"生成活动成功\"}");

        Mockito.when(approvalProcessDefinitionService.wheatherNeedApproval(anyInt())).thenReturn(false);

        Mockito.when(activitiesService.changeStatus(anyString(), anyInt())).thenReturn(true);

        Mockito.when(scheduleService.createScheduleJob(ActivityEndJob.class, schedulerEnd, "{\"activityId\":\"test2\"}",
            activityId2, activities.getEndTime())).thenReturn("success");

        assertNotNull(lotteryService.onShelf(activityId2, schedulerStart, schedulerEnd));

        Mockito.verify(activitiesService, Mockito.atLeastOnce()).judgeEnterpriseForActivity(anyString());
        Mockito.verify(activitiesService, Mockito.atLeastOnce()).selectByActivityId(anyString());
        Mockito.verify(activityTemplateService).selectByActivityId(anyString());
        Mockito.verify(activityPrizeService).selectByActivityId(anyString());
        Mockito.verify(globalConfigService, Mockito.atLeastOnce()).get(anyString());
        Mockito.verify(approvalProcessDefinitionService).wheatherNeedApproval(anyInt());
        Mockito.verify(activitiesService).changeStatus(anyString(), anyInt());
        Mockito.verify(scheduleService).createScheduleJob(any(Class.class), anyString(), anyString(),
            anyString(), any(Date.class));

        Mockito.verify(productService).selectProductById(anyLong());
    }

    /**
     * 测试上架
     * 分支5:活动已经开始，更改活动状态失败
     * */
    @Test
    public void testOnShelf7(){
        String schedulerStart = "start";
        String schedulerEnd = "end";
        String activityId2 = "test2";
        Activities activities = createActivities(1L);
        activities.setStartTime(getDay(new Date(), -1));
        activities.setEndTime(getDay(new Date(), 3));

        ActivityTemplate activityTemplate = createActivityTemplate(1L);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        Enterprise enterprise = createEnterprise();

        Product product = createProduct("M", 1L);
        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);


        Mockito.when(activitiesService.judgeEnterpriseForActivity(activityId2)).thenReturn("success");
        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(activities);
        Mockito.when(activityTemplateService.selectByActivityId(anyString())).thenReturn(activityTemplate);
        Mockito.when(activityPrizeService.selectByActivityId(anyString())).thenReturn(activityPrizeList);
        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(globalConfigService.get("ACTIVITY_PLATFORM_NAME")).thenReturn("sc_test");

        Mockito.when(globalConfigService.get("ACTIVITY_GENERATE_URL"))
            .thenReturn("http://activitytest.4ggogo.com/template/service/autoGenerate/make");
        PowerMockito.mockStatic(HttpConnection.class);
        PowerMockito.when(HttpConnection.doPost(anyString(), anyString(),
            anyString(), anyString(), anyBoolean()))
            .thenReturn("{\"code\":200,\"url\":\"http:×××\",\"msg\":\"生成活动成功\"}");

        Mockito.when(approvalProcessDefinitionService.wheatherNeedApproval(anyInt())).thenReturn(false);

        Mockito.when(activitiesService.changeStatus(anyString(), anyInt())).thenReturn(false);

        assertNotNull(lotteryService.onShelf(activityId2, schedulerStart, schedulerEnd));

        Mockito.verify(activitiesService, Mockito.atLeastOnce()).judgeEnterpriseForActivity(anyString());
        Mockito.verify(activitiesService, Mockito.atLeastOnce()).selectByActivityId(anyString());
        Mockito.verify(activityTemplateService).selectByActivityId(anyString());
        Mockito.verify(activityPrizeService).selectByActivityId(anyString());
        Mockito.verify(globalConfigService, Mockito.atLeastOnce()).get(anyString());
        Mockito.verify(approvalProcessDefinitionService).wheatherNeedApproval(anyInt());
        Mockito.verify(activitiesService).changeStatus(anyString(), anyInt());

        Mockito.verify(productService).selectProductById(anyLong());
    }

    /**
     * 测试上架
     * 分支6:活动未开始，更改活动状态失败
     * */
    @Test
    public void testOnShelf8(){
        String schedulerStart = "start";
        String schedulerEnd = "end";
        String activityId2 = "test2";
        Activities activities = createActivities(1L);
        activities.setStartTime(getDay(new Date(), 1));
        activities.setEndTime(getDay(new Date(), 3));

        ActivityTemplate activityTemplate = createActivityTemplate(1L);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        Enterprise enterprise = createEnterprise();

        Product product = createProduct("M", 1L);
        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);

        Mockito.when(activitiesService.judgeEnterpriseForActivity(activityId2)).thenReturn("success");
        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(activities);
        Mockito.when(activityTemplateService.selectByActivityId(anyString())).thenReturn(activityTemplate);
        Mockito.when(activityPrizeService.selectByActivityId(anyString())).thenReturn(activityPrizeList);
        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(globalConfigService.get("ACTIVITY_PLATFORM_NAME")).thenReturn("sc_test");

        Mockito.when(globalConfigService.get("ACTIVITY_GENERATE_URL"))
            .thenReturn("http://activitytest.4ggogo.com/template/service/autoGenerate/make");
        PowerMockito.mockStatic(HttpConnection.class);
        PowerMockito.when(HttpConnection.doPost(anyString(), anyString(),
            anyString(), anyString(), anyBoolean()))
            .thenReturn("{\"code\":200,\"url\":\"http:×××\",\"msg\":\"生成活动成功\"}");

        Mockito.when(approvalProcessDefinitionService.wheatherNeedApproval(anyInt())).thenReturn(false);

        Mockito.when(activitiesService.changeStatus(anyString(), anyInt())).thenReturn(false);

        assertNotNull(lotteryService.onShelf(activityId2, schedulerStart, schedulerEnd));

        Mockito.verify(activitiesService, Mockito.atLeastOnce()).judgeEnterpriseForActivity(anyString());
        Mockito.verify(activitiesService, Mockito.atLeastOnce()).selectByActivityId(anyString());
        Mockito.verify(activityTemplateService).selectByActivityId(anyString());
        Mockito.verify(activityPrizeService).selectByActivityId(anyString());
        Mockito.verify(globalConfigService, Mockito.atLeastOnce()).get(anyString());
        Mockito.verify(approvalProcessDefinitionService).wheatherNeedApproval(anyInt());
        Mockito.verify(activitiesService).changeStatus(anyString(), anyInt());

        Mockito.verify(productService).selectProductById(anyLong());
    }



    public static Date getDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, i);
        date = calendar.getTime();
        return date;
    }

    /**
     * 测试下架
     * 分支1：向营销模板服务发送下架请求失败
     * */
    @Test
    public void testOffShelf1(){
        assertFalse(lotteryService.offShelf(null));

        String activityId = "test";
        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setUrl("http:");
        Mockito.when(activityInfoService.selectByActivityId(anyString())).thenReturn(activityInfo);
        Mockito.when(activityTemplateService.notifyTemplateToClose(anyString(), anyString())).thenReturn(false);

        assertFalse(lotteryService.offShelf(activityId));
        Mockito.verify(activityInfoService).selectByActivityId(anyString());
        Mockito.verify(activityTemplateService).notifyTemplateToClose(anyString(), anyString());
    }

    /**
     * 测试下架
     * 分支2：更改活动状态失败
     * */
    @Test(expected = RuntimeException.class)
    public void testOffShelf2(){
        String activityId = "test";
        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setUrl("http:");
        Mockito.when(activityInfoService.selectByActivityId(anyString())).thenReturn(activityInfo);
        Mockito.when(activityTemplateService.notifyTemplateToClose(anyString(), anyString())).thenReturn(true);
        Mockito.when(activitiesService.changeStatus(anyString(), anyInt())).thenReturn(false);

        assertFalse(lotteryService.offShelf(activityId));
    }

    /**
     * 测试下架
     * 正常流程
     * */
    @Test
    public void testOffShelf3(){
        String activityId = "test";
        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setUrl("http:");
        Mockito.when(activityInfoService.selectByActivityId(anyString())).thenReturn(activityInfo);
        Mockito.when(activityTemplateService.notifyTemplateToClose(anyString(), anyString())).thenReturn(true);
        Mockito.when(activitiesService.changeStatus(anyString(), anyInt())).thenReturn(true);

        assertTrue(lotteryService.offShelf(activityId));

        Mockito.verify(activityInfoService).selectByActivityId(anyString());
        Mockito.verify(activityTemplateService).notifyTemplateToClose(anyString(), anyString());
        Mockito.verify(activitiesService).changeStatus(anyString(), anyInt());
    }

    /**
     * 生成活动测试方法
     * */
    @Test
    public void testGenerateActivity(){
        assertNull(lotteryService.generateActivity(null));

        String activityId = "test";

        Activities activities = createActivities(1L);
        ActivityTemplate activityTemplate = createActivityTemplate(1L);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        Enterprise enterprise = createEnterprise();
        Product product = createProduct("M", 1L);

        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(activities);
        Mockito.when(activityTemplateService.selectByActivityId(anyString())).thenReturn(activityTemplate);
        Mockito.when(activityPrizeService.selectByActivityId(anyString())).thenReturn(activityPrizeList);
        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(globalConfigService.get("ACTIVITY_PLATFORM_NAME")).thenReturn("sc_test");

        Mockito.when(globalConfigService.get("ACTIVITY_GENERATE_URL"))
            .thenReturn("http://activitytest.4ggogo.com/template/service/autoGenerate/make");
        PowerMockito.mockStatic(HttpConnection.class);
        PowerMockito.when(HttpConnection.doPost(anyString(), anyString(),
            anyString(), anyString(), anyBoolean()))
            .thenReturn("{\"code\":200,\"url\":\"http:×××\",\"msg\":\"生成活动成功\"}");

        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);

        Mockito.when(activityInfoService.updateByPrimaryKeySelective(any(ActivityInfo.class))).thenReturn(true);

        assertNotNull(lotteryService.generateActivity(activityId));

        Mockito.verify(activitiesService).selectByActivityId(anyString());
        Mockito.verify(activityTemplateService).selectByActivityId(anyString());
        Mockito.verify(activityPrizeService).selectByActivityId(anyString());
        Mockito.verify(enterprisesService).selectById(anyLong());
        Mockito.verify(globalConfigService, Mockito.atLeastOnce()).get(anyString());
        Mockito.verify(activityInfoService).updateByPrimaryKeySelective(any(ActivityInfo.class));

        Mockito.verify(productService).selectProductById(anyLong());
    }

    @Test
    public void testGetUrl(){
        Mockito.when(globalConfigService.get(anyString()))
            .thenReturn("http://activitytest.4ggogo.com/template/service/autoGenerate/make");
        assertNotNull(lotteryService.getUrl());
        Mockito.verify(globalConfigService).get(anyString());
    }

    /**
     * 生成活动参数测试方法
     * */
    @Test
    public void testGetSendData(){
        assertNull(lotteryService.getSendData(null));

        String activityId = "test";

        Activities activities = createActivities(1L);
        ActivityTemplate activityTemplate = createActivityTemplate(1L);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        Enterprise enterprise = createEnterprise();
        Product product = createProduct("M", 1L);

        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(activities);
        Mockito.when(activityTemplateService.selectByActivityId(anyString())).thenReturn(activityTemplate);
        Mockito.when(activityPrizeService.selectByActivityId(anyString())).thenReturn(activityPrizeList);
        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(globalConfigService.get("ACTIVITY_PLATFORM_NAME")).thenReturn("sc_test");

        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);

        assertNotNull(lotteryService.getSendData(activityId));

        Mockito.verify(activitiesService).selectByActivityId(anyString());
        Mockito.verify(activityTemplateService).selectByActivityId(anyString());
        Mockito.verify(activityPrizeService).selectByActivityId(anyString());
        Mockito.verify(enterprisesService).selectById(anyLong());
        Mockito.verify(globalConfigService).get("ACTIVITY_PLATFORM_NAME");

        Mockito.verify(productService).selectProductById(anyLong());
    }

    @Test
    public void testGetSendData1(){
        assertNull(lotteryService.getSendData(null));

        String activityId = "test";

        Activities activities = createActivities(1L);
        ActivityTemplate activityTemplate = createActivityTemplate(1L);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        activityPrizeList.get(0).setIdPrefix("1");
        activityPrizeList.get(0).setRankName("二等奖");
        Enterprise enterprise = createEnterprise();
        Product product = createProduct("T", 1L);

        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(activities);
        Mockito.when(activityTemplateService.selectByActivityId(anyString())).thenReturn(activityTemplate);
        Mockito.when(activityPrizeService.selectByActivityId(anyString())).thenReturn(activityPrizeList);
        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(globalConfigService.get("ACTIVITY_PLATFORM_NAME")).thenReturn("sc_test");

        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);

        assertNotNull(lotteryService.getSendData(activityId));

        Mockito.verify(activitiesService).selectByActivityId(anyString());
        Mockito.verify(activityTemplateService).selectByActivityId(anyString());
        Mockito.verify(activityPrizeService).selectByActivityId(anyString());
        Mockito.verify(enterprisesService).selectById(anyLong());
        Mockito.verify(globalConfigService).get("ACTIVITY_PLATFORM_NAME");

        Mockito.verify(productService).selectProductById(anyLong());
    }

    @Test
    public void testGetSendData2(){
        assertNull(lotteryService.getSendData(null));

        String activityId = "test";

        Activities activities = createActivities(1L);
        ActivityTemplate activityTemplate = createActivityTemplate(1L);
        activityTemplate.setDaily(1);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        activityPrizeList.get(0).setIdPrefix("2");
        activityPrizeList.get(0).setRankName("三等奖");
        Enterprise enterprise = createEnterprise();
        Product product = createProduct("U", 1L);

        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(activities);
        Mockito.when(activityTemplateService.selectByActivityId(anyString())).thenReturn(activityTemplate);
        Mockito.when(activityPrizeService.selectByActivityId(anyString())).thenReturn(activityPrizeList);
        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(globalConfigService.get("ACTIVITY_PLATFORM_NAME")).thenReturn("sc_test");

        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);

        assertNotNull(lotteryService.getSendData(activityId));

        Mockito.verify(activitiesService).selectByActivityId(anyString());
        Mockito.verify(activityTemplateService).selectByActivityId(anyString());
        Mockito.verify(activityPrizeService).selectByActivityId(anyString());
        Mockito.verify(enterprisesService).selectById(anyLong());
        Mockito.verify(globalConfigService).get("ACTIVITY_PLATFORM_NAME");

        Mockito.verify(productService).selectProductById(anyLong());
    }

    @Test
    public void testGetSendData3(){
        assertNull(lotteryService.getSendData(null));

        String activityId = "test";

        Activities activities = createActivities(1L);
        ActivityTemplate activityTemplate = createActivityTemplate(1L);
        activityTemplate.setDaily(1);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        activityPrizeList.get(0).setIdPrefix("3");
        activityPrizeList.get(0).setRankName("四等奖");
        Enterprise enterprise = createEnterprise();
        Product product = createProduct("A", 1L);

        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(activities);
        Mockito.when(activityTemplateService.selectByActivityId(anyString())).thenReturn(activityTemplate);
        Mockito.when(activityPrizeService.selectByActivityId(anyString())).thenReturn(activityPrizeList);
        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(globalConfigService.get("ACTIVITY_PLATFORM_NAME")).thenReturn("sc_test");

        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);

        assertNotNull(lotteryService.getSendData(activityId));

        Mockito.verify(activitiesService).selectByActivityId(anyString());
        Mockito.verify(activityTemplateService).selectByActivityId(anyString());
        Mockito.verify(activityPrizeService).selectByActivityId(anyString());
        Mockito.verify(enterprisesService).selectById(anyLong());
        Mockito.verify(globalConfigService).get("ACTIVITY_PLATFORM_NAME");

        Mockito.verify(productService).selectProductById(anyLong());
    }

    @Test
    public void testGetSendData4(){
        assertNull(lotteryService.getSendData(null));

        String activityId = "test";

        Activities activities = createActivities(1L);
        ActivityTemplate activityTemplate = createActivityTemplate(1L);
        activityTemplate.setDaily(1);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        activityPrizeList.get(0).setIdPrefix("4");
        activityPrizeList.get(0).setRankName("五等奖");
        Enterprise enterprise = createEnterprise();
        Product product = createProduct("M", 1L);

        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(activities);
        Mockito.when(activityTemplateService.selectByActivityId(anyString())).thenReturn(activityTemplate);
        Mockito.when(activityPrizeService.selectByActivityId(anyString())).thenReturn(activityPrizeList);
        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(globalConfigService.get("ACTIVITY_PLATFORM_NAME")).thenReturn("sc_test");

        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);

        assertNotNull(lotteryService.getSendData(activityId));

        Mockito.verify(activitiesService).selectByActivityId(anyString());
        Mockito.verify(activityTemplateService).selectByActivityId(anyString());
        Mockito.verify(activityPrizeService).selectByActivityId(anyString());
        Mockito.verify(enterprisesService).selectById(anyLong());
        Mockito.verify(globalConfigService).get("ACTIVITY_PLATFORM_NAME");

        Mockito.verify(productService).selectProductById(anyLong());
    }

    @Test
    public void testGetSendData5(){
        assertNull(lotteryService.getSendData(null));

        String activityId = "test";

        Activities activities = createActivities(1L);
        ActivityTemplate activityTemplate = createActivityTemplate(1L);
        activityTemplate.setDaily(1);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        activityPrizeList.get(0).setIdPrefix("5");
        activityPrizeList.get(0).setRankName("六等奖");
        Enterprise enterprise = createEnterprise();
        Product product = createProduct("M", 1L);

        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(activities);
        Mockito.when(activityTemplateService.selectByActivityId(anyString())).thenReturn(activityTemplate);
        Mockito.when(activityPrizeService.selectByActivityId(anyString())).thenReturn(activityPrizeList);
        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(globalConfigService.get("ACTIVITY_PLATFORM_NAME")).thenReturn("sc_test");

        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);

        assertNotNull(lotteryService.getSendData(activityId));

        Mockito.verify(activitiesService).selectByActivityId(anyString());
        Mockito.verify(activityTemplateService).selectByActivityId(anyString());
        Mockito.verify(activityPrizeService).selectByActivityId(anyString());
        Mockito.verify(enterprisesService).selectById(anyLong());
        Mockito.verify(globalConfigService).get("ACTIVITY_PLATFORM_NAME");

        Mockito.verify(productService).selectProductById(anyLong());
    }


    private Product createProduct(String isp, Long id){
        Product product = new Product();
        product.setIsp(isp);
        product.setId(id);
        product.setType(0);
        return product;
    }

    @Test
    public void testGetCallbackUrl(){
        Mockito.when(globalConfigService.get("ACTIVITY_CALLBACK_URL")).thenReturn("http");
        assertSame("http", lotteryService.getCallbackUrl());
        Mockito.verify(globalConfigService).get("ACTIVITY_CALLBACK_URL");
    }

    /**
     * 平台活动类型与营销活动服务活动类型转化器测试方法
     * */
    @Test
    public void testConverterForGameType(){
        assertSame(ActivityTemplateType.REDPACKET.getCode(),
            lotteryService.converterForGameType(ActivityType.REDPACKET.getCode()));
        assertSame(ActivityTemplateType.LOTTERY.getCode(),
            lotteryService.converterForGameType(ActivityType.LOTTERY.getCode()));
        assertSame(ActivityTemplateType.GOLDENBALL.getCode(),
            lotteryService.converterForGameType(ActivityType.GOLDENBALL.getCode()));
        assertNull(lotteryService.converterForGameType(ActivityType.FLOWCARD.getCode()));
    }



    /**
     * 获取平台标示测试方法
     * */
    @Test
    public void testGetPlateName(){
        Mockito.when(globalConfigService.get("ACTIVITY_PLATFORM_NAME")).thenReturn("sc");
        assertSame("sc", lotteryService.getPlateName());
        Mockito.verify(globalConfigService).get("ACTIVITY_PLATFORM_NAME");
    }


    private Enterprise createEnterprise(){
        Enterprise enterprise = new Enterprise();
        enterprise.setId(1L);
        enterprise.setName("test");
        return enterprise;
    }

    private ActivityTemplate createActivityTemplate(Long id){
        ActivityTemplate activityTemplate = new ActivityTemplate();
        activityTemplate.setId(id);
        activityTemplate.setUserType(1);
        activityTemplate.setGivedNumber(1);
        activityTemplate.setDaily(0);
        activityTemplate.setCheckType(0);
        activityTemplate.setObject("1");
        activityTemplate.setRules("1");
        return activityTemplate;
    }

    /**
     * 更新营销活动信息
     * 分支1:更新活动基本信息失败
     * */
    @Test
    public void testUpdateActivity1(){
        Activities activities = createActivities(1L);
        Mockito.when(activitiesService.updateByPrimaryKeySelective(activities)).thenReturn(false);
        assertFalse(lotteryService.updateActivity(activities, null, null, null));
        Mockito.verify(activitiesService).updateByPrimaryKeySelective(any(Activities.class));
    }

    /**
     * 更新营销活动信息
     * 分支2:更新活动奖品信息失败
     * */
    @Test(expected = RuntimeException.class)
    public void testUpdateActivity2(){
        Activities activities = createActivities(1L);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        Mockito.when(activitiesService.updateByPrimaryKeySelective(any(Activities.class))).thenReturn(true);
        Mockito.when(activityPrizeService.deleteByActivityId(anyString())).thenReturn(false);
        assertFalse(lotteryService.updateActivity(activities, activityPrizeList, null, null));
    }

    /**
     * 更新营销活动信息
     * 分支3:更新活动具体信息失败
     * */
    @Test(expected = RuntimeException.class)
    public void testUpdateActivity3(){
        Activities activities = createActivities(1L);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        Mockito.when(activitiesService.updateByPrimaryKeySelective(any(Activities.class))).thenReturn(true);
        Mockito.when(activityPrizeService.deleteByActivityId(anyString())).thenReturn(true);
        Mockito.when(activityPrizeService.batchInsert(anyList())).thenReturn(false);

        assertFalse(lotteryService.updateActivity(activities, activityPrizeList, null, null));
    }

    /**
     * 更新营销活动信息
     * 分支4:更新活动信息失败
     * */
    @Test(expected = RuntimeException.class)
    public void testUpdateActivity4(){
        Activities activities = createActivities(1L);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        ActivityTemplate activityTemplate = createActivityTemplate(1L);
        ActivityInfo activityInfo = new ActivityInfo();
        Mockito.when(activitiesService.updateByPrimaryKeySelective(any(Activities.class))).thenReturn(true);
        Mockito.when(activityPrizeService.deleteByActivityId(anyString())).thenReturn(true);
        Mockito.when(activityPrizeService.batchInsert(anyList())).thenReturn(true);
        Mockito.when(activityTemplateService.updateByPrimaryKeySelective(any(ActivityTemplate.class))).thenReturn(false);
        assertFalse(lotteryService.updateActivity(activities, activityPrizeList, activityTemplate, activityInfo));
    }

    /**
     * 更新营销活动信息
     * 分支4:更新活动信息失败
     * */
    @Test(expected = RuntimeException.class)
    public void testUpdateActivity5(){
        Activities activities = createActivities(1L);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        ActivityTemplate activityTemplate = createActivityTemplate(1L);
        ActivityInfo activityInfo = new ActivityInfo();
        Mockito.when(activitiesService.updateByPrimaryKeySelective(any(Activities.class))).thenReturn(true);
        Mockito.when(activityPrizeService.deleteByActivityId(anyString())).thenReturn(true);
        Mockito.when(activityPrizeService.batchInsert(anyList())).thenReturn(true);
        Mockito.when(activityTemplateService.updateByPrimaryKeySelective(any(ActivityTemplate.class))).thenReturn(true);
        Mockito.when(activityInfoService.updateByPrimaryKeySelective(any(ActivityInfo.class))).thenReturn(false);

        assertFalse(lotteryService.updateActivity(activities, activityPrizeList, activityTemplate, activityInfo));
    }


    /**
     * 更新营销活动信息
     * 正常流程
     * */
    @Test
    public void testUpdateActivity(){
        Activities activities = createActivities(1L);
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        ActivityTemplate activityTemplate = createActivityTemplate(1L);
        ActivityInfo activityInfo = new ActivityInfo();
        Mockito.when(activitiesService.updateByPrimaryKeySelective(any(Activities.class))).thenReturn(true);
        Mockito.when(activityPrizeService.deleteByActivityId(anyString())).thenReturn(true);
        Mockito.when(activityPrizeService.batchInsert(anyList())).thenReturn(true);
        Mockito.when(activityTemplateService.updateByPrimaryKeySelective(any(ActivityTemplate.class))).thenReturn(true);
        Mockito.when(activityInfoService.updateByPrimaryKeySelective(any(ActivityInfo.class))).thenReturn(true);

        assertTrue(lotteryService.updateActivity(activities, activityPrizeList, activityTemplate, activityInfo));

        Mockito.verify(activitiesService).updateByPrimaryKeySelective(any(Activities.class));
        Mockito.verify(activityPrizeService).deleteByActivityId(anyString());
        Mockito.verify(activityPrizeService).batchInsert(anyList());
        Mockito.verify(activityTemplateService).updateByPrimaryKeySelective(any(ActivityTemplate.class));
        Mockito.verify(activityInfoService).updateByPrimaryKeySelective(any(ActivityInfo.class));
    }

    @Test
    public void testGetAutoPrizesPojo(){
        List<ActivityPrize> activityPrizeList = createActivityPrizeList();
        activityPrizeList.get(0).setIsp(IspType.CMCC.getValue());
        String idPrefix = ActivityPrizeRank.FirstRankPrize.getIdPrefix();
        lotteryService.getAutoPrizesPojo(activityPrizeList, idPrefix);

        activityPrizeList.get(0).setIsp(IspType.UNICOM.getValue());
        lotteryService.getAutoPrizesPojo(activityPrizeList, idPrefix);

        activityPrizeList.get(0).setIsp(IspType.TELECOM.getValue());
        lotteryService.getAutoPrizesPojo(activityPrizeList, idPrefix);

        activityPrizeList.get(0).setIsp(IspType.ALL.getValue());
        lotteryService.getAutoPrizesPojo(activityPrizeList, idPrefix);
    }

    /**
     * 生成活动报文
     * */
    @Test
    public void test(){
        //活动时间
        AutoTimePojo timePojo = new AutoTimePojo();
        timePojo.setStartTime("2017-01-23 00:00:00");
        timePojo.setEndTime("2017-02-23 00:00:00");

        //奖品信息
        List<AutoPrizesPojo> autoPrizesPojoList = new ArrayList<AutoPrizesPojo>();

        AutoPrizesPojo autoPrizesPojo = new AutoPrizesPojo();

        autoPrizesPojo.setIdPrefix(0);
        autoPrizesPojo.setRankName("一等奖");
        autoPrizesPojo.setProbability("1");
        autoPrizesPojo.setCount(10);
        autoPrizesPojo.setCmccName("10M");
        autoPrizesPojo.setCmccEnterId("1");
        autoPrizesPojo.setCmccId("1");
        autoPrizesPojo.setCmccType(0);
        autoPrizesPojo.setCmccResponse(1);
        autoPrizesPojo.setCmccCount(10);

        autoPrizesPojoList.add(autoPrizesPojo);


        //平台信息
        AutoGeneratePojo pojo = new AutoGeneratePojo();
        pojo.setPlateName("sc_sys");
        pojo.setEnterpriseId("1");
        pojo.setEnterpriseName("test");
        pojo.setActiveId("test");
        pojo.setActiveName("test");
        pojo.setUserType(0);
        pojo.setGameType(2);
        pojo.setGivedNumber(5);
        pojo.setMaxPlayNumber(5);
        pojo.setDaily("true");

        pojo.setCheckType(0);
        pojo.setCheckUrl("");
        pojo.setFixedProbability(1); //默认都是固定概率


        pojo.setObject("test");
        pojo.setRules("test");

        pojo.setStatus(ActivityStatus.ON.getCode());
        pojo.setChargeUrl("");
        pojo.setTime(timePojo);
        pojo.setPrizes(autoPrizesPojoList);

        System.out.println(JSON.toJSONString(pojo));

    }







}
