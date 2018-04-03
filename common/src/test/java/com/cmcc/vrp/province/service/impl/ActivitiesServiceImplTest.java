package com.cmcc.vrp.province.service.impl;

/**
 * Created by qinqinyan on 2016/10/25.
 *
 * @Description 营销活动服务测试类
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.CrowdFundingJoinType;
import com.cmcc.vrp.enums.IndividualProductType;
import com.cmcc.vrp.province.cache.CacheService;
import com.cmcc.vrp.province.dao.ActivitiesMapper;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.AccountChangeDetail;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityApprovalDetail;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityTemplate;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.AdminChangeDetail;
import com.cmcc.vrp.province.model.AdminManager;
import com.cmcc.vrp.province.model.ApprovalDetailDefinition;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.CrowdfundingActivityDetail;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterprisesExtInfo;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.IndividualProductMap;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.Role;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.quartz.service.ScheduleService;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityBlackAndWhiteService;
import com.cmcc.vrp.province.service.ActivityCreatorService;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityTemplateService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.ApprovalDetailDefinitionService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRequestService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.CrowdfundingActivityDetailService;
import com.cmcc.vrp.province.service.EntFlowControlService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesExtInfoService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualProductMapService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.RoleService;
import com.cmcc.vrp.province.service.SendMsgService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ActivitiesWinPojo;
import com.cmcc.vrp.queue.pojo.FlowcoinPresentPojo;
import com.cmcc.vrp.util.AES;
import com.cmcc.vrp.util.HttpConnection;
import com.cmcc.webservice.crowdfunding.CrowdfundingCallbackService;

@PrepareForTest({HttpConnection.class, AES.class})
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
public class ActivitiesServiceImplTest {

    @InjectMocks
    ActivitiesService activitiesService = new ActivitiesServiceImpl();
    @Mock
    ActivitiesMapper activitiesMapper;
    @Mock
    EnterprisesService enterprisesService;
    @Mock
    ActivityWinRecordService activityWinRecordService;
    @Mock
    ActivityPrizeService activityPrizeService;
    @Mock
    ActivityInfoService activityInfoService;
    @Mock
    ActivityBlackAndWhiteService activityBlackAndWhiteService;
    @Mock
    PhoneRegionService phoneRegionService;
    @Mock
    ScheduleService scheduleService;
    @Mock
    GlobalConfigService globalConfigService;
    @Mock
    ProductService productService;
    @Mock
    SendMsgService sendMsgService;
    @Mock
    AccountService accountService;
    @Mock
    EntProductService entProductService;
    @Mock
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    @Mock
    RoleService roleService;
    @Mock
    ApprovalRequestService approvalRequestService;
    @Mock
    ApprovalDetailDefinitionService approvalDetailDefinitionService;
    @Mock
    IndividualAccountService individualAccountService;
    @Mock
    IndividualProductMapService individualProductMapService;
    @Mock
    TaskProducer taskProducer;
    @Mock
    ActivityTemplateService activityTemplateService;
    @Mock
    SerialNumService serialNumService;
    @Mock
    ChargeRecordService chargeRecordService;
    @Mock
    EntFlowControlService entFlowControlService;
    @Mock
    CrowdfundingActivityDetailService crowdfundingActivityDetailService;
    @Mock
    CrowdfundingCallbackService crowdfundingCallbackService;
    @Mock
    IndividualProductService individualProductService;
    @Mock
    CacheService cacheService;
    @Mock
    EnterprisesExtInfoService enterprisesExtInfoService;
    @Mock
    AdminManagerService adminManagerService;
    @Mock
    ActivityCreatorService activityCreatorService;

    public static Date getDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, i);
        date = calendar.getTime();
        return date;
    }



    @Test
    public void testInsert() {
        //invalid
        assertFalse(activitiesService.insert(null));

        Activities activities = initActivities();
        //valid
        when(activitiesMapper.insert(Mockito.any(Activities.class))).thenReturn(1);
        assertTrue(activitiesService.insert(activities));
        verify(activitiesMapper).insert(Mockito.any(Activities.class));
    }

    @Test
    public void testSelectByPrimaryKey() throws Exception {
        //invalid
        assertNull(activitiesService.selectByPrimaryKey(null));

        //valid
        when(activitiesMapper.selectByPrimaryKey(anyLong())).thenReturn(new Activities());
        assertNotNull(activitiesService.selectByPrimaryKey(anyLong()));
        verify(activitiesMapper).selectByPrimaryKey(anyLong());
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        //invalid
        assertFalse(activitiesService.updateByPrimaryKeySelective(null));
        //valid
        when(activitiesMapper.updateByPrimaryKeySelective(Mockito.any(Activities.class))).thenReturn(1);
        assertTrue(activitiesService.updateByPrimaryKeySelective(new Activities()));
        verify(activitiesMapper).updateByPrimaryKeySelective(Mockito.any(Activities.class));
    }

    @Test
    public void testSelectByActivityId() throws Exception {
        //invalid
        assertNull(activitiesService.selectByActivityId(null));
        //valid
        String activityId = "test";
        when(activitiesMapper.selectByActivityId(anyString())).thenReturn(new Activities());
        assertNotNull(activitiesService.selectByActivityId(activityId));
        verify(activitiesMapper).selectByActivityId(anyString());
    }
    
    @Test
    public void testSelectActivityListByActivityId() throws Exception {
        //invalid
        assertNull(activitiesService.selectActivityListByActivityId(null));
        //valid
        String activityId = "test";
        when(activitiesMapper.selectActivityListByActivityId(anyString())).thenReturn(new ArrayList<Activities>());
        assertNotNull(activitiesService.selectActivityListByActivityId(activityId));
        verify(activitiesMapper).selectActivityListByActivityId(anyString());
    }

    @Test
    public void testSelectByMap1() {
        Map map1 = new HashMap();
        map1.put("managerId", "");
        assertNull(activitiesService.selectByMap(map1));
        map1.put("managerId", "1");
        map1.put("status", "1,2");
        map1.put("type", "7,8");

        List<Enterprise> enterprises = initEnterprise();
        when(enterprisesService.getEnterByManagerId(anyLong())).thenReturn(enterprises);

        //test selectByMapForRedpacket(Map map); 分支
        when(activitiesMapper.selectByMapForRedpacket(Mockito.anyMap())).thenReturn(new ArrayList<Activities>());
        assertNotNull(activitiesService.selectByMap(map1));
        verify(activitiesMapper).selectByMapForRedpacket(anyMap());
        verify(enterprisesService).getEnterByManagerId(anyLong());

        //test selectByMap(Map map); 分支
        Map map2 = new HashMap();
        map2.put("managerId", "1");
        map2.put("status", "1,2");
        map2.put("type", "6");

        when(activitiesMapper.selectByMapForActivityTemplate(Mockito.anyMap())).thenReturn(new ArrayList<Activities>());
        assertNotNull(activitiesService.selectByMap(map2));
        verify(activitiesMapper).selectByMapForActivityTemplate(anyMap());
    }

    @Test
    public void testSelectByMap2() {
        Map map = new HashMap();
        map.put("managerId", "1");
        map.put("status", "1,2");
        map.put("type", "7,8");

        when(enterprisesService.getEnterByManagerId(anyLong())).thenReturn(null);
        when(activitiesMapper.selectByMapForActivityTemplate(Mockito.anyMap())).thenReturn(new ArrayList<Activities>());
        assertNotNull(activitiesService.selectByMap(map));
        verify(enterprisesService).getEnterByManagerId(anyLong());
        verify(activitiesMapper).selectByMapForActivityTemplate(anyMap());
    }

    @Test
    public void testCountByMap1() {
        Map map1 = new HashMap();
        map1.put("managerId", "");
        assertNull(activitiesService.countByMap(map1));

        map1.put("managerId", "1");
        map1.put("status", "1,2");
        map1.put("type", "7,8");

        List<Enterprise> enterprises = initEnterprise();
        when(enterprisesService.getEnterByManagerId(anyLong())).thenReturn(enterprises);

        //test countByMapForRedpacket(Map map); 分支
        when(activitiesMapper.countByMapForRedpacket(Mockito.anyMap())).thenReturn(anyLong());
        assertNotNull(activitiesService.countByMap(map1));
        verify(activitiesMapper).countByMapForRedpacket(anyMap());
        verify(enterprisesService).getEnterByManagerId(anyLong());

        //test countByMap(Map map); 分支
        Map map2 = new HashMap();
        map2.put("managerId", "1");
        map2.put("status", "1,2");
        map2.put("type", "6");

        when(activitiesMapper.countByMap(Mockito.anyMap())).thenReturn(anyLong());
        assertNotNull(activitiesService.countByMap(map2));
        verify(activitiesMapper).countByMap(anyMap());
    }

    @Test
    public void testCountByMap2() {
        Map map = new HashMap();
        map.put("managerId", "1");
        map.put("status", "1,2");
        map.put("type", "7,8");

        when(enterprisesService.getEnterByManagerId(anyLong())).thenReturn(null);
        when(activitiesMapper.countByMap(Mockito.anyMap())).thenReturn(anyLong());
        assertNotNull(activitiesService.countByMap(map));
        verify(enterprisesService).getEnterByManagerId(anyLong());
        verify(activitiesMapper).countByMap(anyMap());
    }

    @Test
    public void testInsertFlowcardActivity() {

        Activities activities = initActivities();
        Long cmProductId = 1L;
        Long cuProductId = 2L;
        Long ctProductId = 3L;
        String cmMobileList = "18867101234,18867101200";
        String cuMobileList = "15520781234";
        String ctMobileList = "13346328888";
        String cmUserSet = "18867101234,18867101200";
        String cuUserSet = "15520781234";
        String ctUserSet = "13346328888";

        when(activitiesMapper.insert(Mockito.any(Activities.class))).thenReturn(0).thenReturn(1);
        when(
                activityWinRecordService.batchInsertForFlowcard(
                        anyString(), anyLong(), anyLong(),
                        anyLong(), anyString(), anyString(), anyString())).thenReturn(false).thenReturn(true);
        when(
                activityPrizeService.batchInsertForFlowCard(activities, cmProductId, cuProductId, ctProductId,
                        cmMobileList, cuMobileList, ctMobileList)).thenReturn(false).thenReturn(true);
        when(
                activityInfoService.insertActivityInfo(activities, cmProductId, cuProductId, ctProductId, cmMobileList,
                        cuMobileList, ctMobileList, cmUserSet, cuUserSet, ctUserSet)).thenReturn(false)
                .thenReturn(true);

        assertFalse(activitiesService.insertFlowcardActivity(activities, cmProductId, cuProductId, ctProductId,
                cmMobileList, cuMobileList, ctMobileList, cmUserSet, cuUserSet, ctUserSet));
        try {
            activitiesService.insertFlowcardActivity(activities, cmProductId, cuProductId, ctProductId, cmMobileList,
                    cuMobileList, ctMobileList, cmUserSet, cuUserSet, ctUserSet);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            activitiesService.insertFlowcardActivity(activities, cmProductId, cuProductId, ctProductId, cmMobileList,
                    cuMobileList, ctMobileList, cmUserSet, cuUserSet, ctUserSet);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            activitiesService.insertFlowcardActivity(activities, cmProductId, cuProductId, ctProductId, cmMobileList,
                    cuMobileList, ctMobileList, cmUserSet, cuUserSet, ctUserSet);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertTrue(activitiesService.insertFlowcardActivity(activities, cmProductId, cuProductId, ctProductId,
                cmMobileList, cuMobileList, ctMobileList, cmUserSet, cuUserSet, ctUserSet));

        verify(activitiesMapper, Mockito.times(5)).insert(Mockito.any(Activities.class));
        verify(activityWinRecordService, Mockito.times(4)).batchInsertForFlowcard(anyString(), anyLong(), anyLong(),
                anyLong(), anyString(), anyString(), anyString());
        verify(activityPrizeService, Mockito.times(3)).batchInsertForFlowCard(Mockito.any(Activities.class), anyLong(),
                anyLong(), anyLong(), anyString(), anyString(), anyString());
        verify(activityInfoService, Mockito.times(2)).insertActivityInfo(Mockito.any(Activities.class), anyLong(),
                anyLong(), anyLong(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void testInsertQRcodeActivity() {
        Activities activities = initActivities();
        ActivityInfo activityInfo = initActivityInfo();
        Long cmProductId = 1L;
        Long cuProductId = 2L;
        Long ctProductId = 3L;
        String correctMobiles = "18867101234,18867101200";
        
        when(activityCreatorService.insert(Mockito.any(ActivityType.class), Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);

        when(activitiesMapper.insert(Mockito.any(Activities.class))).thenReturn(0).thenReturn(1);

        when(activityPrizeService.batchInsertForQRcord(activities, cmProductId, cuProductId, ctProductId)).thenReturn(
                false).thenReturn(true);

        when(
                activityBlackAndWhiteService.batchInsert(anyString(), anyInt(), anyString())).thenReturn(false).thenReturn(true);

        when(
                activityInfoService.insertActivityInfoForQrcode(activities, activityInfo, cmProductId, cuProductId,
                        ctProductId)).thenReturn(false).thenReturn(true);


        assertFalse(activitiesService.insertQRcodeActivity(activities, activityInfo, cmProductId, cuProductId,
                ctProductId, correctMobiles));

        try {
            activitiesService.insertQRcodeActivity(activities, activityInfo, cmProductId, cuProductId, ctProductId,
                    correctMobiles);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            activitiesService.insertQRcodeActivity(activities, activityInfo, cmProductId, cuProductId, ctProductId,
                    correctMobiles);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            activitiesService.insertQRcodeActivity(activities, activityInfo, cmProductId, cuProductId, ctProductId,
                    correctMobiles);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertTrue(activitiesService.insertQRcodeActivity(activities, activityInfo, cmProductId, cuProductId,
                ctProductId, correctMobiles));

        verify(activitiesMapper, Mockito.times(5)).insert(Mockito.any(Activities.class));

        verify(activityPrizeService, Mockito.times(4)).batchInsertForQRcord(Mockito.any(Activities.class), anyLong(),
                anyLong(), anyLong());

        verify(activityBlackAndWhiteService, Mockito.times(3)).batchInsert(anyString(), anyInt(), anyString());
    }

    /**
     * 测试participate 仅测试第一个if-else条件
     *
     * @author qinqinyan
     */
    @Test
    public void testParticipateForInvalidCase() {
        String activityId = "invalidtest";
        String mobile = "18867103717";
        //invalid
        assertFalse(activitiesService.participate(null, null, null, null));
        assertFalse(activitiesService.participate(activityId, null, null, null));
        assertFalse(activitiesService.participate(null, mobile, null, null));

        when(activitiesMapper.selectByActivityId(activityId)).thenReturn(null);
        assertFalse(activitiesService.participate(activityId, mobile, null, null));
        verify(activitiesMapper).selectByActivityId(anyString());

        String activityId1 = "flowcard";
        String mobile1 = "18867103";
        assertFalse(activitiesService.participate(activityId1, mobile1, null, null));

        //Activities
        Activities activities1 = initActivities();
        activities1.setType(ActivityType.FLOWCARD.getCode());
        activities1.setStatus(ActivityStatus.ON.getCode()); //活动状态不满足
        when(activitiesMapper.selectByActivityId(activities1.getActivityId())).thenReturn(activities1);
        assertFalse(activitiesService.participate(activities1.getActivityId(), mobile, null, null));

        Activities activities2 = initActivities();
        activities2.setType(ActivityType.FLOWCARD.getCode());
        activities2.setStatus(ActivityStatus.PROCESSING.getCode());
        activities2.setStartTime(getDay(new Date(), 1)); //活动开始时间不满足
        when(activitiesMapper.selectByActivityId(activities2.getActivityId())).thenReturn(activities2);
        assertFalse(activitiesService.participate(activities2.getActivityId(), mobile, null, null));

        Activities activities3 = initActivities();
        activities3.setType(ActivityType.FLOWCARD.getCode());
        activities3.setStatus(ActivityStatus.PROCESSING.getCode());
        activities3.setStartTime(getDay(new Date(), -2));
        activities3.setEndTime(getDay(new Date(), -1)); //活动结束时间不满足
        when(activitiesMapper.selectByActivityId(activities3.getActivityId())).thenReturn(activities3);
        assertFalse(activitiesService.participate(activities3.getActivityId(), mobile, null, null));

        Activities activities4 = initActivities();
        activities4.setType(ActivityType.FLOWCARD.getCode());
        activities4.setStatus(ActivityStatus.PROCESSING.getCode());
        activities4.setStartTime(getDay(new Date(), -2));
        activities4.setEndTime(getDay(new Date(), 2));
        activities4.setDeleteFlag(1); //活动逻辑删除状态不满足
        when(activitiesMapper.selectByActivityId(activities4.getActivityId())).thenReturn(activities4);
        assertFalse(activitiesService.participate(activities4.getActivityId(), mobile, null, null));
    }

    /**
     * 测试participate（流量券）
     *
     * @author qinqinyan
     */
    @Test
    public void testParticipateForTypeIsFlowCard() {
        Map extendParams = new HashMap();
        extendParams.put("recordIds", "1");
        extendParams.put("ownerPhone", "18867103698");

        String mobile = "18867101234";

        Activities activities = initActivities();
        activities.setType(ActivityType.FLOWCARD.getCode());
        activities.setStatus(ActivityStatus.PROCESSING.getCode());
        activities.setStartTime(getDay(new Date(), -2));
        activities.setEndTime(getDay(new Date(), 2));
        activities.setDeleteFlag(0);
        activities.setEntId(1L);

        ActivityWinRecord activityWinRecord = initActivityWinRecord();


        when(activitiesMapper.selectByActivityId(activities.getActivityId())).thenReturn(activities);
        when(activityWinRecordService.selectByRecordId(anyString())).thenReturn(activityWinRecord);
        when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(null);

        assertTrue(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));

        verify(activitiesMapper, atLeastOnce()).selectByActivityId(activities.getActivityId());
        verify(activityWinRecordService, atLeastOnce()).selectByRecordId(anyString());
        //verify(entProductService, atLeastOnce()).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
    }

    private EntProduct createEntProduct() {
        EntProduct entProduct = new EntProduct();
        entProduct.setId(1L);
        return entProduct;
    }

    /**
     * 测试participate（流量券）
     *
     * @author qinqinyan
     */
    @Test
    @Ignore
    public void testParticipateForTypeIsFlowCard2() {
        Map extendParams = new HashMap();
        extendParams.put("recordIds", "1");
        extendParams.put("ownerPhone", "18867103698");

        String mobile = "18867101234";

        Activities activities = initActivities();
        activities.setType(ActivityType.FLOWCARD.getCode());
        activities.setStatus(ActivityStatus.PROCESSING.getCode());
        activities.setStartTime(getDay(new Date(), -2));
        activities.setEndTime(getDay(new Date(), 2));
        activities.setDeleteFlag(0);

        ActivityWinRecord activityWinRecord = initActivityWinRecord();

        EntProduct entProduct = createEntProduct();

        when(activitiesMapper.selectByActivityId(activities.getActivityId())).thenReturn(activities);

        when(activityWinRecordService.selectByRecordId(anyString())).thenReturn(activityWinRecord);

        when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(entProduct);

        when(accountService.minusCount(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(AccountType.class),
                Mockito.anyDouble(), Mockito.anyString(), Mockito.anyString())).thenReturn(false).thenReturn(
                true);

        when(serialNumService.insert(Mockito.any(SerialNum.class))).thenReturn(false).thenReturn(true);


        when(accountService.returnFunds(Mockito.anyString(), Mockito.any(ActivityType.class), Mockito.anyLong(),
                Mockito.anyInt())).thenReturn(false).thenReturn(true);
        when(chargeRecordService.create(Mockito.any(ChargeRecord.class))).thenReturn(false).thenReturn(true);

        when(activityWinRecordService.batchUpdateStatus(anyList(), anyString(), anyInt(), anyInt(),
                Mockito.any(Date.class))).thenReturn(false).thenReturn(true);

        when(taskProducer.produceBatchActivityWinMsg(anyList())).thenReturn(true);
        when(activityWinRecordService.batchUpdateStatusCodeByRecordId(Mockito.anyList(), Mockito.anyString()))
                .thenReturn(false).thenReturn(true);
        when(chargeRecordService.batchUpdateStatusCode(Mockito.anyString(), Mockito.anyList())).thenReturn(false)
                .thenReturn(true);

        assertTrue(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));
        assertTrue(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));
        assertTrue(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));
        assertTrue(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));
        assertTrue(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));

        /*verify(activitiesMapper, atLeastOnce()).selectByActivityId(anyString());
        verify(activityWinRecordService, Mockito.times(5)).selectByRecordId(anyString());
        verify(activityWinRecordService, Mockito.times(2)).batchUpdateStatus(anyList(), anyString(), anyInt(),
                anyInt(), Mockito.any(Date.class));
        verify(taskProducer, Mockito.times(1)).produceBatchActivityWinMsg(anyList());
        verify(entProductService, atLeastOnce()).selectByProductIDAndEnterprizeID(anyLong(), anyLong());*/
    }

    /**
     * 测试participate（流量券） edit by qinqinyan
     */
    @Test
    @Ignore
    public void testParticipateForTypeIsFlowCard3() {
        Map extendParams = new HashMap();
        extendParams.put("recordIds", "1");
        extendParams.put("ownerPhone", "18867103698");

        String mobile = "18867101234";

        Activities activities = initActivities();
        activities.setType(ActivityType.FLOWCARD.getCode());
        activities.setStatus(ActivityStatus.PROCESSING.getCode());
        activities.setStartTime(getDay(new Date(), -2));
        activities.setEndTime(getDay(new Date(), 2));
        activities.setDeleteFlag(0);

        ActivityWinRecord activityWinRecord = initActivityWinRecord();
        ChargeRecord cr = new ChargeRecord();
        cr.setId(1L);

        EntProduct entProduct = createEntProduct();

        when(activitiesMapper.selectByActivityId(activities.getActivityId())).thenReturn(activities);

        when(activityWinRecordService.selectByRecordId(anyString())).thenReturn(activityWinRecord);

        when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(entProduct);

        when(accountService.minusCount(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(AccountType.class),
                Mockito.anyDouble(), Mockito.anyString(), Mockito.anyString())).thenReturn(false).thenReturn(true);

        when(serialNumService.insert(Mockito.any(SerialNum.class))).thenReturn(false).thenReturn(true);
        when(accountService.returnFunds(Mockito.anyString(), Mockito.any(ActivityType.class), Mockito.anyLong(),
                Mockito.anyInt())).thenReturn(false).thenReturn(true);
        when(chargeRecordService.create(Mockito.any(ChargeRecord.class))).thenReturn(false).thenReturn(true);

        when(activityWinRecordService.batchUpdateStatus(anyList(), anyString(), anyInt(), anyInt(),
                Mockito.any(Date.class))).thenReturn(false).thenReturn(true);

        when(taskProducer.produceBatchActivityWinMsg(anyList())).thenReturn(false);
        when(activityWinRecordService.batchUpdateStatusCodeByRecordId(Mockito.anyList(), Mockito.anyString()))
                .thenReturn(false).thenReturn(true);
        when(chargeRecordService.batchUpdateStatusCode(Mockito.anyString(), Mockito.anyList())).thenReturn(false)
                .thenReturn(true);
        when(chargeRecordService.getRecordBySN(Mockito.anyString())).thenReturn(cr);
        assertTrue(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));
        assertTrue(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));
        assertTrue(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));
        assertTrue(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));
        assertTrue(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));

       /* verify(activitiesMapper, atLeastOnce()).selectByActivityId(anyString());
        verify(activityWinRecordService, Mockito.times(6)).selectByRecordId(anyString());
        verify(activityWinRecordService, Mockito.times(2)).batchUpdateStatus(anyList(), anyString(), anyInt(),
                anyInt(), Mockito.any(Date.class));
        verify(taskProducer, Mockito.times(1)).produceBatchActivityWinMsg(anyList());

        verify(entProductService, atLeastOnce()).selectByProductIDAndEnterprizeID(anyLong(), anyLong());*/
    }

    /**
     * 测试participate（二维码：白名单正常流程） edit by qinqinyan
     */
    @Test
    public void testParticipateForTypeIsQRCode1() {
        Map extendParams = new HashMap();
        extendParams.put("activityRecordId", "1");
        extendParams.put("ownerPhone", "18867103698");

        String mobile = "18867101234";

        List<String> phones = new ArrayList<String>();
        phones.add(mobile);

        Activities activities = initActivities();
        activities.setType(ActivityType.QRCODE.getCode());
        activities.setStatus(ActivityStatus.PROCESSING.getCode());
        activities.setStartTime(getDay(new Date(), -2));
        activities.setEndTime(getDay(new Date(), 2));
        activities.setDeleteFlag(0);

        ActivityInfo activityInfo = initActivityInfo();

        PhoneRegion phoneRegion = new PhoneRegion();
        phoneRegion.setSupplier("M");

        List<ActivityPrize> prizes = new ArrayList<ActivityPrize>();
        ActivityPrize prize = new ActivityPrize();
        prize.setIsp("M");
        prize.setId(1L);
        prizes.add(prize);

        ActivityWinRecord activityWinRecord = initActivityWinRecord();

        when(activitiesMapper.selectByActivityId(activities.getActivityId())).thenReturn(activities);
        when(activityWinRecordService.selectByRecordId(anyString())).thenReturn(null).thenReturn(activityWinRecord);
        when(activityInfoService.selectByActivityId(activities.getActivityId())).thenReturn(null).thenReturn(
                activityInfo);
        when(activityBlackAndWhiteService.selectPhonesByMap(anyMap())).thenReturn(phones);
        when(activityWinRecordService.updateByPrimaryKey(Mockito.any(ActivityWinRecord.class))).thenReturn(true);
        when(phoneRegionService.query(anyString())).thenReturn(phoneRegion);
        when(activityPrizeService.getDetailByActivityId(anyString())).thenReturn(prizes);
        when(taskProducer.produceActivityWinMsg(Mockito.any(ActivitiesWinPojo.class))).thenReturn(true);

        Enterprise enterprise = new Enterprise();
        enterprise.setDeleteFlag(2);
        enterprise.setStatus((byte) 3);
        when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);

        EntProduct entProduct = new EntProduct();
        when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(entProduct);

        when(accountService.isEnoughInAccount(anyLong(), anyLong())).thenReturn(false);

        assertFalse(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));
        assertFalse(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));
        activityWinRecord.setOwnMobile(null);
        assertFalse(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));
        assertFalse(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));

        enterprise.setDeleteFlag(0);
        assertFalse(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));

        entProduct.setDiscount(100);
        assertFalse(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));

        /*verify(activitiesMapper, atLeastOnce()).selectByActivityId(anyString());
        verify(activityWinRecordService, atLeastOnce()).selectByRecordId(anyString());
        verify(activityInfoService, atLeastOnce()).selectByActivityId(anyString());
        verify(activityBlackAndWhiteService, atLeastOnce()).selectPhonesByMap(anyMap());
        verify(activityWinRecordService, atLeastOnce()).updateByPrimaryKey(Mockito.any(ActivityWinRecord.class));
        verify(phoneRegionService, atLeastOnce()).query(anyString());
        verify(activityPrizeService, atLeastOnce()).getDetailByActivityId(anyString());*/
        //        verify(taskProducer).produceActivityWinMsg(Mockito.any(ActivitiesWinPojo.class));
    }

    /**
     * 测试participate（二维码：白名单正常流程） edit by qinqinyan
     */
    @Test
    public void testParticipateForTypeIsQRCode2() {
        Map extendParams = new HashMap();
        extendParams.put("activityRecordId", "1");
        extendParams.put("ownerPhone", "18867103698");

        String mobile = "18867101234";

        List<String> phones = new ArrayList<String>();
        phones.add("13597091234");

        Activities activities = initActivities();
        activities.setType(ActivityType.QRCODE.getCode());
        activities.setStatus(ActivityStatus.PROCESSING.getCode());
        activities.setStartTime(getDay(new Date(), -2));
        activities.setEndTime(getDay(new Date(), 2));
        activities.setDeleteFlag(0);
        activities.setEntId(1l);

        ActivityInfo activityInfo = initActivityInfo();
        activityInfo.setHasWhiteOrBlack(2);

        PhoneRegion phoneRegion = new PhoneRegion();
        phoneRegion.setSupplier("M");

        List<ActivityPrize> prizes = new ArrayList<ActivityPrize>();
        ActivityPrize prize = new ActivityPrize();
        prize.setIsp("M");
        prize.setId(1L);
        prize.setProductId(1l);
        //        prizes.add(prize);

        ActivityWinRecord activityWinRecord = initActivityWinRecord();
        activityWinRecord.setOwnMobile(null);

        when(activitiesMapper.selectByActivityId(activities.getActivityId())).thenReturn(activities);
        when(activityWinRecordService.selectByRecordId(anyString())).thenReturn(activityWinRecord);
        when(activityInfoService.selectByActivityId(activities.getActivityId())).thenReturn(activityInfo);
        when(activityBlackAndWhiteService.selectPhonesByMap(anyMap())).thenReturn(phones);
        when(activityWinRecordService.updateByPrimaryKey(Mockito.any(ActivityWinRecord.class))).thenReturn(false);
        when(phoneRegionService.query(anyString())).thenReturn(null).thenReturn(phoneRegion);
        when(activityPrizeService.getDetailByActivityId(anyString())).thenReturn(null).thenReturn(prizes);
        when(accountService.minusCount(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(AccountType.class),
                Mockito.anyDouble(), Mockito.anyString(), Mockito.anyString())).thenReturn(false).thenReturn(
                true);
        when(accountService.returnFunds(Mockito.anyString(), Mockito.any(ActivityType.class), Mockito.anyLong(),
                Mockito.anyInt())).thenReturn(false);
        when(taskProducer.produceActivityWinMsg(Mockito.any(ActivitiesWinPojo.class))).thenReturn(true);

        //企业校验增加
        Enterprise enterprise = new Enterprise();
        enterprise.setDeleteFlag(0);
        enterprise.setStatus((byte) 3);
        when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Product product = new Product();
        product.setType(2);
        when(productService.get(Mockito.anyLong()))
                .thenReturn(product);
        //企业与产品关联关系校验增加
        EntProduct entProduct = new EntProduct();
        entProduct.setDiscount(100);
        when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(entProduct);

        //校验企业余额
        when(accountService.isEnoughInAccount(anyLong(), anyLong())).thenReturn(true);

        //校验流控异常
        Product p = new Product();
        p.setPrice(10);
        when(productService.selectProductById(anyLong())).thenReturn(p);
        when(entFlowControlService.isFlowControl(anyDouble(),
                anyLong(), anyBoolean())).thenReturn(true);

        assertFalse(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));
        assertFalse(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));
        assertFalse(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));
        prizes.add(prize);
        assertFalse(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));
        assertFalse(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));

        when(activityWinRecordService.updateByPrimaryKey(Mockito.any(ActivityWinRecord.class))).thenReturn(true);
        when(serialNumService.insert(Mockito.any(SerialNum.class))).thenReturn(false).thenReturn(true);
        when(chargeRecordService.create(Mockito.any(ChargeRecord.class))).thenReturn(false).thenReturn(true);
        activityWinRecord.setOwnMobile(null);

        assertFalse(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));
        activityWinRecord.setStatus(0);
        assertFalse(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));
        when(serialNumService.insert(Mockito.any(SerialNum.class))).thenReturn(true);
        activityWinRecord.setOwnMobile(null);
        activityWinRecord.setStatus(0);
        assertFalse(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));

        activityWinRecord.setOwnMobile(null);
        activityWinRecord.setStatus(0);
        when(taskProducer.produceActivityWinMsg(Mockito.any(ActivitiesWinPojo.class))).thenReturn(false);
        when(activityWinRecordService.updateByPrimaryKeySelective(Mockito.any(ActivityWinRecord.class))).thenReturn(
                true);
        when(
                chargeRecordService.updateStatusAndStatusCode(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(),
                        Mockito.anyString(), Mockito.anyInt(), Mockito.any(Date.class))).thenReturn(false);
        assertFalse(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));

        activityWinRecord.setOwnMobile(null);
        activityWinRecord.setStatus(0);
        when(taskProducer.produceActivityWinMsg(Mockito.any(ActivitiesWinPojo.class))).thenReturn(true);
        when(activityWinRecordService.updateStatusCodeByRecordId(Mockito.anyString(), Mockito.anyString())).thenReturn(
                false);
        when(chargeRecordService.updateStatusCode(Mockito.anyLong(), Mockito.anyString())).thenReturn(false);

        assertTrue(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));

        //        assertFalse(activitiesService.participate(activities.getActivityId(), mobile, extendParams));

        //        assertTrue(activitiesService.participate(activities.getActivityId(), mobile, extendParams));
        //
        //        verify(activitiesMapper, Mockito.times(4)).selectByActivityId(anyString());
        //        verify(activityWinRecordService, Mockito.times(4)).selectByRecordId(anyString());
        //        verify(activityInfoService, Mockito.times(4)).selectByActivityId(anyString());
        //        verify(activityBlackAndWhiteService, Mockito.times(4)).selectPhonesByMap(anyMap());
        //        verify(activityWinRecordService).updateByPrimaryKey(Mockito.any(ActivityWinRecord.class));
        //        verify(phoneRegionService, Mockito.times(4)).query(anyString());
        //        verify(activityPrizeService, Mockito.times(3)).getDetailByActivityId(anyString());
        //        verify(taskProducer).produceActivityWinMsg(Mockito.any(ActivitiesWinPojo.class));
    }

    /**
     * 测试participate（二维码：白名单，非正常流程） edit by qinqinyan
     */
    @Test
    public void testParticipateForTypeIsQRCodeInormal1() {
        Map extendParams = new HashMap();
        extendParams.put("activityRecordId", "1");
        extendParams.put("ownerPhone", "18867103698");

        String mobile = "18867101234";

        List<String> phones = new ArrayList<String>();
        phones.add("13597091234");

        Activities activities = initActivities();
        activities.setType(ActivityType.QRCODE.getCode());
        activities.setStatus(ActivityStatus.PROCESSING.getCode());
        activities.setStartTime(getDay(new Date(), -2));
        activities.setEndTime(getDay(new Date(), 2));
        activities.setDeleteFlag(0);

        ActivityInfo activityInfo = initActivityInfo();

        ActivityWinRecord activityWinRecord = initActivityWinRecord();
        activityWinRecord.setOwnMobile(null);

        when(activitiesMapper.selectByActivityId(activities.getActivityId())).thenReturn(activities);
        when(activityWinRecordService.selectByRecordId(anyString())).thenReturn(activityWinRecord);
        when(activityInfoService.selectByActivityId(activities.getActivityId())).thenReturn(activityInfo);
        when(activityBlackAndWhiteService.selectPhonesByMap(anyMap())).thenReturn(phones);
        when(activityWinRecordService.updateByPrimaryKey(Mockito.any(ActivityWinRecord.class))).thenReturn(true);

        assertFalse(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));

        verify(activitiesMapper).selectByActivityId(anyString());
        verify(activityWinRecordService).selectByRecordId(anyString());
        verify(activityInfoService).selectByActivityId(anyString());
        //verify(activityBlackAndWhiteService).selectPhonesByMap(anyMap());
        //verify(activityWinRecordService).updateByPrimaryKey(Mockito.any(ActivityWinRecord.class));
    }

    /**
     * 测试participate（二维码：黑名单，非正常流程）
     */
    @Test
    public void testParticipateForTypeIsQRCodeInormal2() {
        Map extendParams = new HashMap();
        extendParams.put("activityRecordId", "1");
        extendParams.put("ownerPhone", "18867103698");

        String mobile = "18867101234";

        List<String> phones = new ArrayList<String>();
        phones.add(mobile);

        Activities activities = initActivities();
        activities.setType(ActivityType.QRCODE.getCode());
        activities.setStatus(ActivityStatus.PROCESSING.getCode());
        activities.setStartTime(getDay(new Date(), -2));
        activities.setEndTime(getDay(new Date(), 2));
        activities.setDeleteFlag(0);

        ActivityInfo activityInfo = initActivityInfo();
        activityInfo.setHasWhiteOrBlack(2);

        ActivityWinRecord activityWinRecord = initActivityWinRecord();
        activityWinRecord.setOwnMobile(null);

        when(activitiesMapper.selectByActivityId(activities.getActivityId())).thenReturn(activities);
        when(activityWinRecordService.selectByRecordId(anyString())).thenReturn(activityWinRecord);
        when(activityInfoService.selectByActivityId(activities.getActivityId())).thenReturn(activityInfo);
        when(activityBlackAndWhiteService.selectPhonesByMap(anyMap())).thenReturn(phones);
        when(activityWinRecordService.updateByPrimaryKey(Mockito.any(ActivityWinRecord.class))).thenReturn(true);

        assertFalse(activitiesService.participate(activities.getActivityId(), mobile, extendParams, null));

        /*verify(activitiesMapper).selectByActivityId(anyString());
        verify(activityWinRecordService).selectByRecordId(anyString());
        verify(activityInfoService).selectByActivityId(anyString());
        verify(activityBlackAndWhiteService).selectPhonesByMap(anyMap());
        verify(activityWinRecordService).updateByPrimaryKey(Mockito.any(ActivityWinRecord.class));*/
    }

    @Test
    public void testEditFlowcardActivity() {
        //初始化
        Activities activities = initActivities();
        Long cmProductId = 1L;
        Long cuProductId = 2L;
        Long ctProductId = 3L;
        String cmMobileList = "18867101234,18867101200";
        String cuMobileList = "15520781234";
        String ctMobileList = "13346328888";
        String cmUserSet = "18867101234,18867101200";
        String cuUserSet = "15520781234";
        String ctUserSet = "13346328888";

        List<ActivityPrize> prizes = new ArrayList<ActivityPrize>();
        ActivityPrize prize = new ActivityPrize();
        prize.setIsp("M");
        prize.setId(4L);
        prize.setProductId(4L);
        prizes.add(prize);

        //invalid
        assertFalse(activitiesService.editFlowcardActivity(null, cmProductId, cuProductId, ctProductId, cmMobileList,
                cuMobileList, ctMobileList, cmUserSet, cuUserSet, ctUserSet));

        String cmMobileList1 = "";
        String cuMobileList1 = "";
        String ctMobileList1 = "";
        List<ActivityWinRecord> records = new ArrayList();
        ActivityWinRecord ar1 = new ActivityWinRecord();
        ActivityWinRecord ar2 = new ActivityWinRecord();
        records.add(ar1);
        records.add(ar2);
        when(activityWinRecordService.selectByActivityIdAndIsp(Mockito.anyString(), Mockito.anyString())).thenReturn(
                records);

        //valid
        when(activitiesMapper.updateByPrimaryKeySelective(Mockito.any(Activities.class))).thenReturn(1);
        when(activityPrizeService.selectByActivityId(anyString())).thenReturn(prizes);
        when(activityPrizeService.deleteActivityPrize(anyList(), anyString())).thenReturn(false).thenReturn(true);
        when(activityPrizeService.addActivityPrize(anyList(), anyString(), anyLong(), anyLong(), anyLong()))
                .thenReturn(true);
        when(activityPrizeService.updateActivityPrize(anyList(), anyString(), anyLong(), anyLong(), anyLong()))
                .thenReturn(true);

        when(activityWinRecordService.deleteByActivityId(anyString())).thenReturn(false).thenReturn(true);
        when(activityWinRecordService.batchInsertForFlowcard(anyString(), anyLong(), anyLong(), anyLong(),
                anyString(), anyString(), anyString())).thenReturn(true);
        when(activityInfoService.updateActivityInfo(anyString(), anyLong(), anyLong(), anyLong(), anyLong(),
                anyLong(), anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(false).thenReturn(true);

        try {
            activitiesService.editFlowcardActivity(activities, cmProductId, cuProductId, ctProductId, cmMobileList,
                    cuMobileList, ctMobileList, cmUserSet, cuUserSet, ctUserSet);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            activitiesService.editFlowcardActivity(activities, cmProductId, cuProductId, ctProductId, cmMobileList,
                    cuMobileList, ctMobileList, cmUserSet, cuUserSet, ctUserSet);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            activitiesService.editFlowcardActivity(activities, cmProductId, cuProductId, ctProductId, cmMobileList,
                    cuMobileList, ctMobileList, cmUserSet, cuUserSet, ctUserSet);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertTrue(activitiesService.editFlowcardActivity(activities, cmProductId, cuProductId, ctProductId,
                cmMobileList1, cuMobileList1, ctMobileList1, cmUserSet, cuUserSet, ctUserSet));

        assertTrue(activitiesService.editFlowcardActivity(activities, cmProductId, cuProductId, ctProductId,
                cmMobileList, cuMobileList, ctMobileList, cmUserSet, cuUserSet, ctUserSet));

        verify(activitiesMapper, Mockito.times(5)).updateByPrimaryKeySelective(Mockito.any(Activities.class));
        verify(activityPrizeService, Mockito.times(5)).selectByActivityId(anyString());
        verify(activityPrizeService, Mockito.times(5)).deleteActivityPrize(anyList(), anyString());
        verify(activityPrizeService, Mockito.times(4)).addActivityPrize(anyList(), anyString(), anyLong(), anyLong(),
                anyLong());
        verify(activityWinRecordService, Mockito.times(3)).deleteByActivityId(anyString());
        verify(activityWinRecordService, Mockito.times(2)).batchInsertForFlowcard(anyString(), anyLong(), anyLong(),
                anyLong(), anyString(), anyString(), anyString());
        verify(activityInfoService, Mockito.times(3)).updateActivityInfo(anyString(), anyLong(), anyLong(), anyLong(),
                anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), anyLong());
    }

    /**
     * 无黑白名单
     */
    @Test
    public void testEditQRcodeActivityWithoutList() {
        //初始化
        Activities activities = initActivities();
        Long cmProductId = 1L;
        Long cuProductId = 2L;
        Long ctProductId = 3L;
        String correctMobiles = "18867101234,18867101200";

        ActivityInfo oldActivityInfo = initActivityInfo();
        ActivityInfo activityInfo = initActivityInfo();
        activityInfo.setHasWhiteOrBlack(0);

        when(activityInfoService.selectByActivityId(anyString())).thenReturn(oldActivityInfo);
        when(activitiesMapper.updateByPrimaryKeySelective(Mockito.any(Activities.class))).thenReturn(0).thenReturn(1);

        //invalid
        assertFalse(activitiesService.editQRcodeActivity(null, activityInfo, cmProductId, cuProductId, ctProductId,
                correctMobiles));

        //valid
        when(activityPrizeService.deleteActivityPrize(anyList(), anyString())).thenReturn(false).thenReturn(true);
        when(activityPrizeService.addActivityPrize(anyList(), anyString(), anyLong(), anyLong(), anyLong()))
                .thenReturn(true);
        when(
                activityInfoService.updateActivityInfoForQrcode(Mockito.any(Activities.class),
                        Mockito.any(ActivityInfo.class), anyLong(), anyLong(), anyLong())).thenReturn(false)
                .thenReturn(true);
        when(activityBlackAndWhiteService.deleteByActivityId(anyString())).thenReturn(false).thenReturn(true);

        assertFalse(activitiesService.editQRcodeActivity(activities, activityInfo, cmProductId, cuProductId,
                ctProductId, correctMobiles));
        try {
            activitiesService.editQRcodeActivity(activities, activityInfo, cmProductId, cuProductId, ctProductId,
                    correctMobiles);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            activitiesService.editQRcodeActivity(activities, activityInfo, cmProductId, cuProductId, ctProductId,
                    correctMobiles);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            activitiesService.editQRcodeActivity(activities, activityInfo, cmProductId, cuProductId, ctProductId,
                    correctMobiles);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertTrue(activitiesService.editQRcodeActivity(activities, activityInfo, cmProductId, cuProductId,
                ctProductId, correctMobiles));

        verify(activityInfoService, Mockito.times(5)).selectByActivityId(anyString());
        verify(activitiesMapper, Mockito.times(5)).updateByPrimaryKeySelective(Mockito.any(Activities.class));
        verify(activityPrizeService, Mockito.times(4)).deleteActivityPrize(anyList(), anyString());
        verify(activityInfoService, Mockito.times(3)).updateActivityInfoForQrcode(Mockito.any(Activities.class),
                Mockito.any(ActivityInfo.class), anyLong(), anyLong(), anyLong());
        verify(activityBlackAndWhiteService, Mockito.times(2)).deleteByActivityId(anyString());
    }

    /**
     * 白名单
     */
    @Test
    public void testEditQRcodeActivityWithWhiteList() {
        //初始化
        Activities activities = initActivities();
        Long cmProductId = 1L;
        Long cuProductId = 2L;
        Long ctProductId = 3L;
        String correctMobiles = "18867101234,18867101200";

        ActivityInfo oldActivityInfo = initActivityInfo();
        ActivityInfo activityInfo = initActivityInfo();
        activityInfo.setHasWhiteOrBlack(1);

        when(activityInfoService.selectByActivityId(anyString())).thenReturn(oldActivityInfo);
        when(activitiesMapper.updateByPrimaryKeySelective(Mockito.any(Activities.class))).thenReturn(1);

        when(activityPrizeService.deleteActivityPrize(anyList(), anyString())).thenReturn(true);
        when(activityPrizeService.addActivityPrize(anyList(), anyString(), anyLong(), anyLong(), anyLong()))
                .thenReturn(true);
        when(
                activityInfoService.updateActivityInfoForQrcode(Mockito.any(Activities.class),
                        Mockito.any(ActivityInfo.class), anyLong(), anyLong(), anyLong())).thenReturn(true);
        when(activityBlackAndWhiteService.deleteByActivityId(anyString())).thenReturn(true);
        when(activityBlackAndWhiteService.batchInsert(anyString(), anyInt(), anyString())).thenReturn(true);

        assertTrue(activitiesService.editQRcodeActivity(activities, activityInfo, cmProductId, cuProductId,
                ctProductId, correctMobiles));

        verify(activityInfoService).selectByActivityId(anyString());
        verify(activitiesMapper).updateByPrimaryKeySelective(Mockito.any(Activities.class));
        verify(activityPrizeService).deleteActivityPrize(anyList(), anyString());
        verify(activityInfoService).updateActivityInfoForQrcode(Mockito.any(Activities.class),
                Mockito.any(ActivityInfo.class), anyLong(), anyLong(), anyLong());
        verify(activityBlackAndWhiteService).deleteByActivityId(anyString());
        verify(activityBlackAndWhiteService).batchInsert(anyString(), anyInt(), anyString());
    }

    @Test
    public void testEditQRcodeActivityWithoutMobiles() {
        //初始化
        Activities activities = initActivities();
        Long cmProductId = 1L;
        Long cuProductId = 2L;
        Long ctProductId = 3L;

        ActivityInfo oldActivityInfo = initActivityInfo();
        oldActivityInfo.setHasWhiteOrBlack(1);
        ActivityInfo activityInfo = initActivityInfo();
        activityInfo.setHasWhiteOrBlack(2);

        when(activityInfoService.selectByActivityId(anyString())).thenReturn(oldActivityInfo);
        when(activitiesMapper.updateByPrimaryKeySelective(Mockito.any(Activities.class))).thenReturn(1);

        when(activityPrizeService.deleteActivityPrize(anyList(), anyString())).thenReturn(true);
        when(activityPrizeService.addActivityPrize(anyList(), anyString(), anyLong(), anyLong(), anyLong()))
                .thenReturn(true);
        when(
                activityInfoService.updateActivityInfoForQrcode(Mockito.any(Activities.class),
                        Mockito.any(ActivityInfo.class), anyLong(), anyLong(), anyLong())).thenReturn(true);
        when(activityBlackAndWhiteService.updateIsWhiteByActivityId(anyString(), anyInt())).thenReturn(true);

        assertTrue(activitiesService.editQRcodeActivity(activities, activityInfo, cmProductId, cuProductId,
                ctProductId, null));

        verify(activityInfoService).selectByActivityId(anyString());
        verify(activitiesMapper).updateByPrimaryKeySelective(Mockito.any(Activities.class));
        verify(activityPrizeService).deleteActivityPrize(anyList(), anyString());
        verify(activityInfoService).updateActivityInfoForQrcode(Mockito.any(Activities.class),
                Mockito.any(ActivityInfo.class), anyLong(), anyLong(), anyLong());
        verify(activityBlackAndWhiteService).updateIsWhiteByActivityId(anyString(), anyInt());
    }

    @Test
    public void testChangeStatus() {
        String activityId = "test";
        Integer status = 1;
        //invalid
        assertFalse(activitiesService.changeStatus(null, status));
        assertFalse(activitiesService.changeStatus(activityId, null));

        //valid
        when(activitiesMapper.changeStatus(anyString(), anyInt())).thenReturn(1);
        assertTrue(activitiesService.changeStatus(activityId, status));
        verify(activitiesMapper).changeStatus(anyString(), anyInt());
    }

    @Test
    public void testBatchChangeStatus() {
        List<Activities> activities = new ArrayList<Activities>();
        Activities activitie = initActivities();
        activities.add(activitie);
        Integer status = 1;

        //invalid
        assertFalse(activitiesService.batchChangeStatus(null, status));
        assertFalse(activitiesService.batchChangeStatus(activities, null));

        //valid
        when(activitiesMapper.batchChangeStatus(anyMap())).thenReturn(1);
        assertTrue(activitiesService.batchChangeStatus(activities, status));
        verify(activitiesMapper).batchChangeStatus(anyMap());
    }

    /**
     * 创建活动开始时间定时任务 测试返回：true
     */
    @Test
    public void testCreateActivityStartSchedule1() {
        Activities activities = initActivities();

        when(
                scheduleService.createScheduleJob(any(Class.class), anyString(), anyString(), anyString(),
                        any(Date.class))).thenReturn("success");

        activities.setType(ActivityType.FLOWCARD.getCode());
        assertTrue(activitiesService.createActivityStartSchedule(activities));

        activities.setType(ActivityType.QRCODE.getCode());
        assertTrue(activitiesService.createActivityStartSchedule(activities));

        activities.setType(ActivityType.COMMON_REDPACKET.getCode());
        assertTrue(activitiesService.createActivityStartSchedule(activities));

        activities.setType(ActivityType.LUCKY_REDPACKET.getCode());
        assertTrue(activitiesService.createActivityStartSchedule(activities));

        verify(scheduleService, times(4)).createScheduleJob(any(Class.class), anyString(), anyString(), anyString(),
                any(Date.class));
    }

    /**
     * 创建活动开始时间定时任务 测试返回：false
     */
    @Test
    public void testCreateActivityStartSchedule2() {
        Activities activities = initActivities();
        activities.setType(ActivityType.FLOWCARD.getCode());

        when(
                scheduleService.createScheduleJob(any(Class.class), anyString(), anyString(), anyString(),
                        any(Date.class))).thenReturn("failed");
        assertFalse(activitiesService.createActivityStartSchedule(activities));

        verify(scheduleService, times(1)).createScheduleJob(any(Class.class), anyString(), anyString(), anyString(),
                any(Date.class));
    }

    /**
     * 创建活动结束时间定时任务 测试返回：true
     */
    @Test
    public void testCreateActivityEndSchedule1() {
        Activities activities = initActivities();

        when(
                scheduleService.createScheduleJob(any(Class.class), anyString(), anyString(), anyString(),
                        any(Date.class))).thenReturn("success");

        activities.setType(ActivityType.FLOWCARD.getCode());
        assertTrue(activitiesService.createActivityEndSchedule(activities));

        activities.setType(ActivityType.QRCODE.getCode());
        assertTrue(activitiesService.createActivityEndSchedule(activities));

        activities.setType(ActivityType.COMMON_REDPACKET.getCode());
        assertTrue(activitiesService.createActivityEndSchedule(activities));

        activities.setType(ActivityType.LUCKY_REDPACKET.getCode());
        assertTrue(activitiesService.createActivityEndSchedule(activities));

        verify(scheduleService, times(4)).createScheduleJob(any(Class.class), anyString(), anyString(), anyString(),
                any(Date.class));
    }

    /**
     * 创建活动结束时间定时任务 测试返回：false
     */
    @Test
    public void testCreateActivityEndSchedule2() {
        Activities activities = initActivities();
        activities.setType(ActivityType.FLOWCARD.getCode());

        when(
                scheduleService.createScheduleJob(any(Class.class), anyString(), anyString(), anyString(),
                        any(Date.class))).thenReturn("failed");
        assertFalse(activitiesService.createActivityEndSchedule(activities));

        verify(scheduleService, times(1)).createScheduleJob(any(Class.class), anyString(), anyString(), anyString(),
                any(Date.class));
    }

    /**
     * 注：加密算法这里抛异常
     */
    @Test
    public void testNotifyUserForFlowcard() throws Exception {
        //invalid
        Activities activities = initActivities();
        activities.setActivityId(null);
        assertFalse(activitiesService.notifyUserForFlowcard(null));
        assertFalse(activitiesService.notifyUserForFlowcard(activities));

        //init
        Activities activities1 = initActivities();
        activities1.setName("测试");

        Enterprise enterprise = buildEnterprise();

        List<ActivityWinRecord> activityWinRecords = new ArrayList<ActivityWinRecord>();
        ActivityWinRecord record = new ActivityWinRecord();
        record.setActivityId(activities.getActivityId());
        record.setOwnMobile("18867101234");
        record.setPrizeId(1L);
        activityWinRecords.add(record);

        Product product = new Product();
        product.setId(1L);
        product.setProductSize(10L);
        product.setType(1);

        //valid
        PowerMockito.mockStatic(AES.class);
        byte[] encryptResult = {1, 2};
        when(AES.encrypt(Mockito.any(byte[].class), Mockito.any(byte[].class))).thenReturn(encryptResult);
        when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(enterprise);
        when(activityWinRecordService.selectByActivityId(anyString())).thenReturn(null).thenReturn(activityWinRecords);
        when(globalConfigService.get("ACTIVITY_URL_KEY")).thenReturn("this is test");
        when(globalConfigService.get("LOTTERY_FLOWCARD_URL")).thenReturn(
                "http://localhost:8080/web-in/manage/flowcard/charge/");
        when(productService.selectProductById(anyLong())).thenReturn(product);
        when(sendMsgService.sendMessage(anyString(), anyString(), anyString())).thenReturn(false).thenReturn(true);

        assertTrue(activitiesService.notifyUserForFlowcard(activities1));
        assertTrue(activitiesService.notifyUserForFlowcard(activities1));
        assertTrue(activitiesService.notifyUserForFlowcard(activities1));

        verify(enterprisesService, Mockito.times(3)).selectByPrimaryKey(anyLong());
        verify(activityWinRecordService, Mockito.times(3)).selectByActivityId(anyString());
        //verify(globalConfigService, Mockito.times(3)).get("ACTIVITY_URL_KEY");
        //verify(globalConfigService, Mockito.times(3)).get("LOTTERY_FLOWCARD_URL");
        //verify(productService, Mockito.times(2)).selectProductById(anyLong());
        //verify(sendMsgService, Mockito.times(2)).sendMessage(anyString(), anyString(), anyString());
    }

    @Test
    public void testCheckAccountEnoughInvalid() {
        Activities activities = initActivities();

        List<ActivityPrize> activityPrizes = new ArrayList<ActivityPrize>();
        ActivityPrize activityPrize = new ActivityPrize();
        activityPrize.setPrice(1000L);
        activityPrizes.add(activityPrize);

        when(activityPrizeService.getDetailByActivityId(anyString())).thenReturn(activityPrizes);
        when(activityInfoService.selectByActivityId(anyString())).thenReturn(null);

        assertFalse(activitiesService.checkAccountEnough(activities));

        verify(activityPrizeService).getDetailByActivityId(anyString());
        verify(activityInfoService).selectByActivityId(anyString());
    }

    /**
     * 活动类型：二维码
     */
    @Test
    public void testCheckAccountEnough() {
        Activities activities = initActivities();
        activities.setActivityId(null);
        //invalid
        assertFalse(activitiesService.checkAccountEnough(null));
        assertFalse(activitiesService.checkAccountEnough(activities));

        //init
        Activities activities1 = initActivities();
        activities1.setType(ActivityType.QRCODE.getCode());

        List<ActivityPrize> activityPrizes = new ArrayList<ActivityPrize>();
        ActivityPrize activityPrize = new ActivityPrize();
        activityPrize.setPrice(1000L);
        activityPrizes.add(activityPrize);

        ActivityInfo activityInfo = initActivityInfo();

        Account account = new Account();
        account.setEnterId(activities.getEntId());
        account.setCount(1000.00);

        //valid
        when(activityPrizeService.getDetailByActivityId(anyString())).thenReturn(activityPrizes);
        when(activityInfoService.selectByActivityId(anyString())).thenReturn(activityInfo);
        when(accountService.get(anyLong(), anyLong(), anyInt())).thenReturn(account);
        when(accountService.isDebt2Account(anyList(), anyLong())).thenReturn(false);

        assertFalse(activitiesService.checkAccountEnough(activities1));

//        verify(activityPrizeService).getDetailByActivityId(anyString());
//        verify(activityInfoService).selectByActivityId(anyString());
//        verify(accountService).get(anyLong(), anyLong(), anyInt());
//        verify(accountService).isDebt2Account(anyList(), anyLong());
    }

    /**
     * 活动类型：其他
     */
    @Test
    public void testCheckAccountEnough1() {
        Activities activities = initActivities();
        activities.setActivityId(null);
        //invalid
        assertFalse(activitiesService.checkAccountEnough(null));
        assertFalse(activitiesService.checkAccountEnough(activities));

        //init
        Activities activities1 = initActivities();
        activities1.setType(ActivityType.FLOWCARD.getCode());

        List<ActivityPrize> activityPrizes = new ArrayList<ActivityPrize>();
        ActivityPrize activityPrize = new ActivityPrize();
        activityPrize.setPrice(1000L);
        activityPrizes.add(activityPrize);

        ActivityInfo activityInfo = initActivityInfo();

        Account account = new Account();
        account.setEnterId(activities.getEntId());
        account.setCount(1000.00);

        //valid
        when(activityPrizeService.getDetailByActivityId(anyString())).thenReturn(activityPrizes);
        when(activityInfoService.selectByActivityId(anyString())).thenReturn(activityInfo);
        when(accountService.get(anyLong(), anyLong(), anyInt())).thenReturn(account);
        when(productService.get(anyLong())).thenReturn(new Product());

        when(accountService.isDebt2Account(anyList(), anyLong())).thenReturn(false);
        try {
            activitiesService.checkAccountEnough(activities1);
        } catch (Exception e) {
            // TODO: handle exception
        }
        

//        verify(activityPrizeService).getDetailByActivityId(anyString());
//        verify(activityInfoService).selectByActivityId(anyString());
//        verify(accountService).get(anyLong(), anyLong(), anyInt());
//        verify(accountService).isDebt2Account(anyList(), anyLong());
    }

    @Test
    public void testCheckPrizeAvailable() {
        Activities activities = initActivities();
        activities.setActivityId(null);

        //init
        List<ActivityPrize> activityPrizes = new ArrayList<ActivityPrize>();
        ActivityPrize activityPrize = new ActivityPrize();
        ActivityPrize activityPrize2 = new ActivityPrize();
        activityPrize.setId(1L);
        activityPrizes.add(activityPrize);
        activityPrize2.setId(2L);
        activityPrizes.add(activityPrize2);

        ActivityInfo activityInfo = initActivityInfo();

        EntProduct entProduct = new EntProduct();
        entProduct.setEnterprizeId(1L);
        entProduct.setProductId(1L);

        //invalid
        assertNotNull(activitiesService.checkPrizeAvailable(null));
        assertNotNull(activitiesService.checkPrizeAvailable(activities));

        activities.setActivityId("invalid1");
        when(activityPrizeService.getDetailByActivityId(activities.getActivityId())).thenReturn(null);

        assertNotNull(activitiesService.checkPrizeAvailable(activities));

        verify(activityPrizeService).getDetailByActivityId(activities.getActivityId());

        activities.setActivityId("invalid2");
        when(activityPrizeService.getDetailByActivityId(activities.getActivityId())).thenReturn(activityPrizes);
        when(activityInfoService.selectByActivityId(activities.getActivityId())).thenReturn(null);

        assertNotNull(activitiesService.checkPrizeAvailable(activities));

        verify(activityPrizeService).getDetailByActivityId(activities.getActivityId());
//        verify(activityInfoService).selectByActivityId(activities.getActivityId());

        activities.setActivityId("valid");
        when(activityPrizeService.getDetailByActivityId(activities.getActivityId())).thenReturn(activityPrizes);
        when(activityInfoService.selectByActivityId(activities.getActivityId())).thenReturn(activityInfo);
        when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(null).thenReturn(
                entProduct);

        Product flowAccountProduct = new Product();
        flowAccountProduct.setId(1L);
        flowAccountProduct.setFlowAccountFlag(0);
        when(productService.selectProductById(Mockito.anyLong())).thenReturn(null).thenReturn(flowAccountProduct);

        assertNotNull(activitiesService.checkPrizeAvailable(activities));

        //   verify(activityPrizeService, Mockito.times(2)).getDetailByActivityId(activities.getActivityId());
        //    verify(activityInfoService, Mockito.times(2)).selectByActivityId(activities.getActivityId());
    }

    @Test
    public void testCheckPrizeAvailable2() {
        Activities activities = initActivities();
        activities.setActivityId(null);

        //init
        List<ActivityPrize> activityPrizes = new ArrayList<ActivityPrize>();
        ActivityPrize activityPrize = new ActivityPrize();
        ActivityPrize activityPrize2 = new ActivityPrize();
        activityPrize.setId(1L);
        activityPrizes.add(activityPrize);
        activityPrize2.setId(2L);
        activityPrizes.add(activityPrize2);

        ActivityInfo activityInfo = initActivityInfo();

        EntProduct entProduct = new EntProduct();
        entProduct.setEnterprizeId(1L);
        entProduct.setProductId(1L);

        activities.setActivityId("valid");
        when(activityPrizeService.getDetailByActivityId(activities.getActivityId())).thenReturn(activityPrizes);
        when(activityInfoService.selectByActivityId(activities.getActivityId())).thenReturn(activityInfo);
        when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(entProduct);

        Product flowAccountProduct = new Product();
        flowAccountProduct.setId(1L);
        flowAccountProduct.setFlowAccountFlag(0);
        when(productService.selectProductById(Mockito.anyLong())).thenReturn(flowAccountProduct);

        try {
            activitiesService.checkPrizeAvailable(activities);
        } catch (Exception e) {
            // TODO: handle exception
        }
        

        //   verify(activityPrizeService, Mockito.times(1)).getDetailByActivityId(activities.getActivityId());
        //   verify(activityInfoService, Mockito.times(1)).selectByActivityId(activities.getActivityId());
    }

    @Test
    public void testCheckPrizeAvailable3() {
        Activities activities = initActivities();
        activities.setActivityId(null);

        //init
        List<ActivityPrize> activityPrizes = new ArrayList<ActivityPrize>();
        ActivityPrize activityPrize = new ActivityPrize();
        ActivityPrize activityPrize2 = new ActivityPrize();
        activityPrize.setId(1L);
        activityPrizes.add(activityPrize);
        activityPrize2.setId(2L);
        activityPrizes.add(activityPrize2);

        ActivityInfo activityInfo = initActivityInfo();

        EntProduct entProduct = new EntProduct();
        entProduct.setEnterprizeId(1L);
        entProduct.setProductId(1L);

        activities.setActivityId("valid");
        when(activityPrizeService.getDetailByActivityId(activities.getActivityId())).thenReturn(activityPrizes);
        when(activityInfoService.selectByActivityId(activities.getActivityId())).thenReturn(activityInfo);
        when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(entProduct);

        Product flowAccountProduct = new Product();
        flowAccountProduct.setId(1L);
        flowAccountProduct.setFlowAccountFlag(1);
        when(productService.selectProductById(Mockito.anyLong())).thenReturn(flowAccountProduct);

        try {
            activitiesService.checkPrizeAvailable(activities);

        } catch (Exception e) {
            // TODO: handle exception
        }

        //   verify(activityPrizeService, Mockito.times(1)).getDetailByActivityId(activities.getActivityId());
        //   verify(activityInfoService, Mockito.times(1)).selectByActivityId(activities.getActivityId());
        //  verify(entProductService, Mockito.times(0)).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
    }

    @Test
    public void testSelectByEntId() {
        when(activitiesMapper.selectByEntId(anyMap())).thenReturn(new ArrayList<Activities>());
        assertNotNull(activitiesService.selectByEntId(anyMap()));
        verify(activitiesMapper).selectByEntId(anyMap());
    }

    /**
     * invalid:三个分支
     */
    @Test
    public void testSubmitApproval1() {
        //init
        Long currentId = 1L;
        Long roleId = 1L;

        List<ActivityPrize> activityPrizes = new ArrayList<ActivityPrize>();
        ActivityPrize activityPrize = new ActivityPrize();
        activityPrize.setId(1L);
        activityPrize.setProductId(1L);
        activityPrize.setPrice(1000L);
        activityPrizes.add(activityPrize);

        EntProduct entProduct = new EntProduct();
        entProduct.setEnterprizeId(1L);
        entProduct.setProductId(1L);

        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setUserCount(1L);

        Account account = new Account();
        account.setCount(1000.00);

        Activities activities1 = initActivities();
        activities1.setEndTime(getDay(new Date(), -1));

        assertNotNull(activitiesService.submitApproval(null, currentId, roleId));

        when(cacheService.get(Mockito.anyString())).thenReturn(null);
        when(activitiesMapper.selectByActivityId(activities1.getActivityId())).thenReturn(activities1);
        when(enterprisesService.judgeEnterprise(anyLong())).thenReturn("success");
        when(adminManagerService.selectByAdminId(anyLong())).thenReturn(new AdminManager());
        when(enterprisesService.isParentManage(anyLong(), anyLong())).thenReturn(true);

        //活动时间过期分支
        assertNotNull(activitiesService.submitApproval(activities1.getActivityId(), currentId, roleId));
        verify(activitiesMapper).selectByActivityId(activities1.getActivityId());

        //检查活动产品是否有效分支
        Activities activities2 = initActivities();
        activities2.setActivityId("invalid1");
        activities2.setEndTime(getDay(new Date(), 1));

        when(activitiesMapper.selectByActivityId(activities2.getActivityId())).thenReturn(activities2);
        when(activityPrizeService.getDetailByActivityId(activities2.getActivityId())).thenReturn(activityPrizes);
        when(activityInfoService.selectByActivityId(activities2.getActivityId())).thenReturn(null);
        assertNotNull(activitiesService.submitApproval(activities2.getActivityId(), currentId, roleId));

        //      verify(activitiesMapper).selectByActivityId(activities2.getActivityId());
        //      verify(activityPrizeService).getDetailByActivityId(activities2.getActivityId());
        //       verify(activityInfoService).selectByActivityId(activities2.getActivityId());

        //检查余额分支
        List<ActivityPrize> activityPrizes1 = new ArrayList<ActivityPrize>();
        ActivityPrize activityPrize1 = new ActivityPrize();
        activityPrize1.setId(1L);
        activityPrize1.setProductId(1L);
        activityPrize1.setPrice(1000L);
        activityPrizes1.add(activityPrize1);

        Activities activities3 = initActivities();
        activities3.setActivityId("invalid2");
        activities3.setEndTime(getDay(new Date(), 1));
        activities3.setType(ActivityType.QRCODE.getCode());

        when(activitiesMapper.selectByActivityId(activities3.getActivityId())).thenReturn(activities3);
        when(activityPrizeService.getDetailByActivityId(activities3.getActivityId())).thenReturn(activityPrizes1);
        when(activityInfoService.selectByActivityId(activities3.getActivityId())).thenReturn(activityInfo);
        when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(entProduct);
        when(accountService.get(anyLong(), anyLong(), anyInt())).thenReturn(account);
        when(accountService.isDebt2Account(anyList(), anyLong())).thenReturn(true);

        Product flowAccountProduct = new Product();
        flowAccountProduct.setId(1L);
        flowAccountProduct.setFlowAccountFlag(0);
        when(productService.selectProductById(Mockito.anyLong())).thenReturn(flowAccountProduct);

        try {
            activitiesService.submitApproval(activities3.getActivityId(), currentId, roleId);
        } catch (Exception e) {
            // TODO: handle exception
        }

        //    verify(activitiesMapper).selectByActivityId(activities3.getActivityId());
        //    verify(activityPrizeService, atLeastOnce()).getDetailByActivityId(activities3.getActivityId());
        //    verify(activityInfoService, atLeastOnce()).selectByActivityId(activities3.getActivityId());
        //    verify(accountService).isDebt2Account(anyList(), anyLong());
    }

    /**
     * invalid
     */
    @Test
    public void testSubmitApproval2() {
        //init
        Long currentId = 1L;
        Long roleId = 1L;

        Activities activities1 = initActivities();
        activities1.setEndTime(getDay(new Date(), -1));

        when(activitiesMapper.selectByActivityId(activities1.getActivityId())).thenReturn(activities1);
        when(enterprisesService.judgeEnterprise(anyLong())).thenReturn("fail");
        when(adminManagerService.selectByAdminId(anyLong())).thenReturn(new AdminManager());
        when(enterprisesService.isParentManage(anyLong(), anyLong())).thenReturn(true);

        assertNotNull(activitiesService.submitApproval(activities1.getActivityId(), currentId, roleId));

        verify(activitiesMapper).selectByActivityId(anyString());
        verify(enterprisesService).judgeEnterprise(anyLong());
    }

    /**
     * 不需要审核分支
     */
    @Test
    public void testSubmitApproval3() {
        //init
        Long currentId = 1L;
        Long roleId = 1L;

        Activities activities1 = initActivities();
        activities1.setEndTime(getDay(new Date(), 1));

        List<ActivityPrize> activityPrizes = new ArrayList<ActivityPrize>();
        ActivityPrize activityPrize = new ActivityPrize();
        activityPrize.setId(1L);
        activityPrize.setProductId(1L);
        activityPrize.setPrice(1000L);
        activityPrizes.add(activityPrize);

        EntProduct entProduct = new EntProduct();
        entProduct.setEnterprizeId(1L);
        entProduct.setProductId(1L);

        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setUserCount(1L);
        activityInfo.setActivityId(activities1.getActivityId());

        Account account = new Account();
        account.setCount(1000.00);

        ApprovalProcessDefinition approvalProcessDefinition = new ApprovalProcessDefinition();
        approvalProcessDefinition.setStage(0);

        when(enterprisesService.judgeEnterprise(anyLong())).thenReturn("success");

        when(activitiesMapper.selectByActivityId(anyString())).thenReturn(activities1);
        when(activityPrizeService.getDetailByActivityId(anyString())).thenReturn(activityPrizes);
        when(activityInfoService.selectByActivityId(anyString())).thenReturn(activityInfo);
        when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(entProduct);
        when(accountService.get(anyLong(), anyLong(), anyInt())).thenReturn(account);
        when(accountService.isDebt2Account(anyList(), anyLong())).thenReturn(false);
        when(approvalProcessDefinitionService.selectByType(anyInt())).thenReturn(approvalProcessDefinition);
        when(activitiesMapper.changeStatus(anyString(), anyInt())).thenReturn(1);

        Product flowAccountProduct = new Product();
        flowAccountProduct.setId(1L);
        flowAccountProduct.setFlowAccountFlag(0);
        when(productService.selectProductById(Mockito.anyLong())).thenReturn(flowAccountProduct);

        //        assertNull(activitiesService.submitApproval(activities1.getActivityId(), currentId, roleId));

        //        verify(enterprisesService).judgeEnterprise(anyLong());
        //        verify(activitiesMapper, atLeastOnce()).selectByActivityId(anyString());
        //        verify(activityPrizeService, atLeastOnce()).getDetailByActivityId(anyString());
        //        verify(activityInfoService, atLeastOnce()).selectByActivityId(anyString());
        //        verify(entProductService).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
        //        verify(accountService).get(anyLong(), anyLong(), anyInt());
        //        verify(accountService).isDebt2Account(anyList(), anyLong());
        //        verify(approvalProcessDefinitionService).selectByType(anyInt());
        //        verify(activitiesMapper).changeStatus(anyString(), anyInt());
    }

    /**
     * 需要审核分支 活动审核在改造，这个有问题
     */
    @Test
    @Ignore
    public void testSubmitApproval4() {
        //init
        Long currentId = 1L;
        Long roleId = 1L;

        Activities activities1 = initActivities();
        activities1.setEndTime(getDay(new Date(), 1));
        activities1.setType(ActivityType.FLOWCARD.getCode());
        activities1.setName("测试活动");

        List<ActivityPrize> activityPrizes = new ArrayList<ActivityPrize>();
        ActivityPrize activityPrize = new ActivityPrize();
        activityPrize.setId(1L);
        activityPrize.setProductId(1L);
        activityPrize.setPrice(1000L);
        activityPrizes.add(activityPrize);

        EntProduct entProduct = new EntProduct();
        entProduct.setEnterprizeId(1L);
        entProduct.setProductId(1L);

        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setUserCount(1L);
        activityInfo.setActivityId(activities1.getActivityId());

        Account account = new Account();
        account.setCount(1000.00);

        ApprovalProcessDefinition approvalProcessDefinition = new ApprovalProcessDefinition();
        approvalProcessDefinition.setId(1L);
        approvalProcessDefinition.setStage(1);

        Role role = new Role();
        role.setRoleId(1L);
        role.setName("测试角色");

        List<ApprovalDetailDefinition> approvalRecordList = new ArrayList<ApprovalDetailDefinition>();
        ApprovalDetailDefinition approvalDetailDefinition = new ApprovalDetailDefinition();
        approvalDetailDefinition.setRoleId(1L);
        approvalRecordList.add(approvalDetailDefinition);

        when(enterprisesService.judgeEnterprise(anyLong())).thenReturn("success");

        when(activitiesMapper.selectByActivityId(anyString())).thenReturn(activities1);
        when(activityPrizeService.getDetailByActivityId(anyString())).thenReturn(activityPrizes);
        when(activityInfoService.selectByActivityId(anyString())).thenReturn(activityInfo);
        when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(entProduct);
        when(accountService.get(anyLong(), anyLong(), anyInt())).thenReturn(account);
        when(accountService.isDebt2Account(anyList(), anyLong())).thenReturn(false);
        when(approvalProcessDefinitionService.selectByType(anyInt())).thenReturn(approvalProcessDefinition);
        when(approvalDetailDefinitionService.getByApprovalProcessId(anyLong())).thenReturn(approvalRecordList);
        when(roleService.getRoleById(anyLong())).thenReturn(role);
        when(
                approvalRequestService.submitApproval(Mockito.any(ApprovalRequest.class),
                        Mockito.any(ApprovalRecord.class), Mockito.any(Enterprise.class),
                        Mockito.any(AccountChangeDetail.class), anyList(), Mockito.any(ActivityApprovalDetail.class)
                        ,Mockito.any(AdminChangeDetail.class)))
                .thenReturn(true);

        assertNotNull(activitiesService.submitApproval(activities1.getActivityId(), currentId, roleId));

        verify(enterprisesService).judgeEnterprise(anyLong());
        verify(activitiesMapper, atLeastOnce()).selectByActivityId(anyString());
        verify(activityPrizeService, atLeastOnce()).getDetailByActivityId(anyString());
        verify(activityInfoService, atLeastOnce()).selectByActivityId(anyString());
        verify(entProductService).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
        verify(accountService).get(anyLong(), anyLong(), anyInt());
        verify(accountService).isDebt2Account(anyList(), anyLong());
        verify(approvalProcessDefinitionService).selectByType(anyInt());
        verify(approvalDetailDefinitionService).getByApprovalProcessId(anyLong());
        verify(roleService).getRoleById(anyLong());
        verify(approvalRequestService).submitApproval(Mockito.any(ApprovalRequest.class),
                Mockito.any(ApprovalRecord.class), Mockito.any(Enterprise.class),
                Mockito.any(AccountChangeDetail.class), anyList(), Mockito.any(ActivityApprovalDetail.class)
                ,Mockito.any(AdminChangeDetail.class));
    }

    @Test
    public void testGenerateActivivty() {
        //init        
        when(activityTemplateService.selectByActivityId(Mockito.anyString())).thenReturn(new ActivityTemplate());
 
        Activities activities = initActivities();
        activities.setType(ActivityType.COMMON_REDPACKET.getCode());
        activities.setCreatorId(1L);
        activities.setStartTime(getDay(new Date(), -1));
        activities.setEndTime(getDay(new Date(), 1));
        activities.setName("测试活动");

        List<ActivityPrize> activityPrizes = new ArrayList<ActivityPrize>();

        ActivityPrize activityPrize = new ActivityPrize();
        activityPrize.setActivityId(activities.getActivityId());
        activityPrize.setProductId(1L);
        activityPrize.setCount(1L);
        activityPrize.setSize(10L);
        activityPrize.setId(1L);

        activityPrizes.add(activityPrize);

        IndividualProductMap individualProductMap = new IndividualProductMap();
        individualProductMap.setIndividualProductId(1L);
        individualProductMap.setProductName("测试产品");
        individualProductMap.setPrice(100);
        individualProductMap.setDiscount(100);

        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setActivityId("test");
        activityInfo.setTotalProductSize(10L);
        
        IndividualProduct product = new IndividualProduct();
        product.setType(IndividualProductType.DEFAULT_FLOW_PACKAGE.getValue());
        product.setId(1L);
        when(individualProductService.getDefaultFlowProduct()).thenReturn(product);

        when(activitiesMapper.insert(Mockito.any(Activities.class))).thenReturn(1);
        when(individualProductMapService.getByAdminIdAndProductType(anyLong(), anyInt())).thenReturn(
                individualProductMap);
        when(activityPrizeService.insertForRedpacket(Mockito.any(ActivityPrize.class))).thenReturn(true);
        when(activityInfoService.insertForRedpacket(Mockito.any(ActivityInfo.class))).thenReturn(true);
        when(
                individualAccountService.createAccountForActivity(Mockito.any(Activities.class),
                        Mockito.any(ActivityInfo.class), anyList(), Mockito.any(IndividualAccount.class))).thenReturn(
                true);

        when(activitiesMapper.selectByActivityId(anyString())).thenReturn(activities);
        when(activitiesMapper.changeStatus(anyString(), anyInt())).thenReturn(1);

        when(
                scheduleService.createScheduleJob(any(Class.class), anyString(), anyString(), anyString(),
                        Mockito.any(Date.class))).thenReturn("success");

        when(activityInfoService.selectByActivityId(anyString())).thenReturn(activityInfo);
        when(activityPrizeService.selectByActivityIdForIndividual(anyString())).thenReturn(activityPrizes);
        when(
                individualAccountService.changeBossAccount(anyLong(), any(BigDecimal.class), anyLong(),
                        anyString(), anyInt(), anyString(), anyInt(), anyInt())).thenReturn(true);

        when(globalConfigService.get("ACTIVITY_PLATFORM_NAME")).thenReturn("platform_name");
        when(globalConfigService.get("ACTIVITY_GENERATE_URL")).thenReturn(
                "http://activitytest.4ggogo.com/template/service/autoGenerate/make");

        PowerMockito.mockStatic(HttpConnection.class);
        PowerMockito.when(HttpConnection.doPost(anyString(), anyString(), anyString(), anyString(), anyBoolean()))
                .thenReturn("{\"code\":200,\"url\":\"http:×××\",\"msg\":\"生成活动成功\"}");
        when(activityInfoService.updateByPrimaryKeySelective(any(ActivityInfo.class))).thenReturn(true);

        assertEquals(200, activitiesService.generateActivivty(activities, activityPrize).getCode());

        verify(activitiesMapper, atLeastOnce()).insert(any(Activities.class));
        verify(individualProductMapService, atLeastOnce()).getByAdminIdAndProductType(anyLong(), anyInt());
        verify(activityPrizeService, atLeastOnce()).insertForRedpacket(any(ActivityPrize.class));
        verify(activityInfoService, atLeastOnce()).insertForRedpacket(any(ActivityInfo.class));
        verify(individualAccountService, atLeastOnce()).createAccountForActivity(any(Activities.class),
                any(ActivityInfo.class), anyList(), any(IndividualAccount.class));

        verify(activitiesMapper, atLeastOnce()).selectByActivityId(anyString());
        verify(activitiesMapper, atLeastOnce()).changeStatus(anyString(), anyInt());

        verify(scheduleService, atLeastOnce()).createScheduleJob(any(Class.class), anyString(), anyString(),
                anyString(), any(Date.class));

        verify(activityInfoService, atLeastOnce()).selectByActivityId(anyString());
        verify(activityPrizeService, atLeastOnce()).selectByActivityIdForIndividual(anyString());
        verify(individualAccountService, atLeastOnce()).changeBossAccount(activities.getCreatorId(),
                new BigDecimal(activityInfo.getTotalProductSize()), activityPrizes.get(0).getProductId(),
                activities.getActivityId(), (int) AccountRecordType.OUTGO.getValue(), activities.getName(),
                activities.getType(), 0);

        verify(globalConfigService, atLeastOnce()).get("ACTIVITY_PLATFORM_NAME");
        verify(globalConfigService, atLeastOnce()).get("ACTIVITY_GENERATE_URL");

        verify(activityInfoService, atLeastOnce()).updateByPrimaryKeySelective(any(ActivityInfo.class));
    }

    /**
     * 冻结活动账户,正常流程
     */
    @Test
    public void testFrozenActivityAccount1() {
        //invalid
        assertFalse(activitiesService.frozenActivityAccount(null));

        //init
        Activities activities = initActivities();
        activities.setType(ActivityType.COMMON_REDPACKET.getCode());
        activities.setCreatorId(1L);
        activities.setStartTime(getDay(new Date(), -1));
        activities.setEndTime(getDay(new Date(), 1));
        activities.setName("测试活动");

        ActivityInfo activityInfo = initActivityInfo();
        activityInfo.setTotalProductSize(10L);

        List<ActivityPrize> activityPrizes = new ArrayList<ActivityPrize>();
        ActivityPrize activityPrize = new ActivityPrize();
        activityPrize.setActivityId(activities.getActivityId());
        activityPrize.setProductId(1L);
        activityPrize.setCount(1L);
        activityPrize.setSize(10L);
        activityPrizes.add(activityPrize);

        when(activitiesMapper.selectByActivityId(anyString())).thenReturn(activities);
        when(activityInfoService.selectByActivityId(anyString())).thenReturn(activityInfo);
        when(activityPrizeService.selectByActivityIdForIndividual(anyString())).thenReturn(activityPrizes);
        when(
                individualAccountService.changeBossAccount(activities.getCreatorId(),
                        new BigDecimal(activityInfo.getTotalProductSize()), activityPrizes.get(0).getProductId(),
                        activities.getActivityId(), (int) AccountRecordType.OUTGO.getValue(), activities.getName(),
                        activities.getType(), 0)).thenReturn(true);

        assertTrue(activitiesService.frozenActivityAccount(activities.getActivityId()));

        verify(activitiesMapper).selectByActivityId(anyString());
        verify(activityInfoService).selectByActivityId(anyString());
        verify(activityPrizeService).selectByActivityIdForIndividual(anyString());
        verify(individualAccountService, atLeastOnce()).changeBossAccount(activities.getCreatorId(),
                new BigDecimal(activityInfo.getTotalProductSize()), activityPrizes.get(0).getProductId(),
                activities.getActivityId(), (int) AccountRecordType.OUTGO.getValue(), activities.getName(),
                activities.getType(), 0);
    }

    /**
     * 冻结活动账户,非正常流程
     */
    @Test
    public void testFrozenActivityAccount2() {
        //init
        Activities activities = initActivities();

        ActivityInfo activityInfo = initActivityInfo();

        when(activitiesMapper.selectByActivityId(anyString())).thenReturn(activities);
        when(activityInfoService.selectByActivityId(anyString())).thenReturn(activityInfo);
        when(activityPrizeService.selectByActivityIdForIndividual(anyString())).thenReturn(null);

        assertFalse(activitiesService.frozenActivityAccount(activities.getActivityId()));

        verify(activitiesMapper).selectByActivityId(anyString());
        verify(activityInfoService).selectByActivityId(anyString());
        verify(activityPrizeService).selectByActivityIdForIndividual(anyString());
    }

    /**
     * 创建活动定时任务
     */
    @Test
    public void testCreateActivitySchedule() {
        //init
        Activities activities = initActivities();
        activities.setType(ActivityType.FLOWCARD.getCode());
        activities.setEndTime(getDay(new Date(), 3));

        //invalid: 传入参数为空
        assertFalse(activitiesService.createActivitySchedule(null));

        //invalid：活动为空
        when(activitiesMapper.selectByActivityId(activities.getActivityId())).thenReturn(null);
        assertFalse(activitiesService.createActivitySchedule(activities.getActivityId()));

        //invalid（if分支）: 修改状态返回false
        activities.setActivityId("invalid1");
        activities.setStartTime(getDay(new Date(), -1));
        when(activitiesMapper.selectByActivityId(activities.getActivityId())).thenReturn(activities);
        when(activitiesMapper.changeStatus(activities.getActivityId(), ActivityStatus.PROCESSING.getCode()))
                .thenReturn(0);
        assertFalse(activitiesService.createActivitySchedule(activities.getActivityId()));

        //invalid（if分支）:创建定时任务失败
        activities.setActivityId("invalid2");
        when(activitiesMapper.selectByActivityId(activities.getActivityId())).thenReturn(activities);
        when(activitiesMapper.changeStatus(activities.getActivityId(), ActivityStatus.PROCESSING.getCode()))
                .thenReturn(1);
        when(
                scheduleService.createScheduleJob(any(Class.class), anyString(), anyString(), anyString(),
                        any(Date.class))).thenReturn("failed");
        assertFalse(activitiesService.createActivitySchedule(activities.getActivityId()));

        //invalid（else分支）:创建定时任务失败
        activities.setStartTime(getDay(new Date(), 1));
        activities.setActivityId("invalid3");
        when(activitiesMapper.selectByActivityId(activities.getActivityId())).thenReturn(activities);
        when(
                scheduleService.createScheduleJob(any(Class.class), anyString(), anyString(), anyString(),
                        any(Date.class))).thenReturn("failed");
        assertFalse(activitiesService.createActivitySchedule(activities.getActivityId()));

        verify(activitiesMapper, atLeastOnce()).selectByActivityId(anyString());
        verify(activitiesMapper, atLeastOnce()).changeStatus(anyString(), anyInt());
        verify(scheduleService, atLeastOnce()).createScheduleJob(any(Class.class), anyString(), anyString(),
                anyString(), any(Date.class));
    }

    @Test
    public void testSendToGenerateActivity() {
        //invalid
        assertNull(activitiesService.sendToGenerateActivity(null));
    }

    @Test
    public void testJudgeEnterpriseForActivity() {
        //init
        Activities activities = initActivities();
        activities.setType(ActivityType.FLOWCARD.getCode());

        //invalid: 掺入参数不正常
        assertEquals("参数缺失，上架失败!", activitiesService.judgeEnterpriseForActivity(null));

        //invalid
        activities.setEntId(null);
        when(activitiesMapper.selectByActivityId(activities.getActivityId())).thenReturn(activities);
        assertEquals("参数缺失，上架失败!", activitiesService.judgeEnterpriseForActivity(activities.getActivityId()));

        //invalid
        activities.setEntId(1L);
        when(activitiesMapper.selectByActivityId(activities.getActivityId())).thenReturn(activities);
        when(enterprisesService.judgeEnterprise(activities.getEntId())).thenReturn("企业状态不正常");
        assertEquals("企业状态不正常", activitiesService.judgeEnterpriseForActivity(activities.getActivityId()));

        //invalid
        activities.setEntId(2L);
        activities.setEndTime(getDay(new Date(), -1));
        when(activitiesMapper.selectByActivityId(activities.getActivityId())).thenReturn(activities);
        when(enterprisesService.judgeEnterprise(activities.getEntId())).thenReturn("success");
        assertEquals("活动已过期，请修改活动时间！", activitiesService.judgeEnterpriseForActivity(activities.getActivityId()));

        //invalid
        activities.setEndTime(getDay(new Date(), 1));
        when(activityPrizeService.getDetailByActivityId(activities.getActivityId())).thenReturn(null);
        when(activitiesMapper.selectByActivityId(activities.getActivityId())).thenReturn(activities);
        when(enterprisesService.judgeEnterprise(activities.getEntId())).thenReturn("success");
        assertNotNull(activitiesService.judgeEnterpriseForActivity(activities.getActivityId()));

        //invalid
        activities.setEndTime(getDay(new Date(), 1));
        activities.setActivityId("invalid");
        activities.setEntId(3L);

        List<ActivityPrize> activityPrizes = new ArrayList<ActivityPrize>();
        ActivityPrize activityPrize = new ActivityPrize();
        activityPrize.setProductId(1L);
        activityPrize.setCount(1L);
        activityPrizes.add(activityPrize);

        ActivityInfo activityInfo = new ActivityInfo();

        Account account = new Account();
        account.setCount(1000.00);

        when(activityPrizeService.getDetailByActivityId(activities.getActivityId())).thenReturn(activityPrizes);
        when(activityInfoService.selectByActivityId(activities.getActivityId())).thenReturn(activityInfo);
        when(activitiesMapper.selectByActivityId(activities.getActivityId())).thenReturn(activities);
        when(enterprisesService.judgeEnterprise(activities.getEntId())).thenReturn("success");
        when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(new EntProduct());
        when(accountService.get(anyLong(), anyLong(), anyInt())).thenReturn(account);
        when(accountService.isDebt2Account(anyList(), anyLong())).thenReturn(true);

        Product flowAccountProduct = new Product();
        flowAccountProduct.setId(1L);
        flowAccountProduct.setFlowAccountFlag(0);
        when(productService.selectProductById(Mockito.anyLong())).thenReturn(flowAccountProduct);

        //        assertEquals("企业账户余额不足！", activitiesService.judgeEnterpriseForActivity(activities.getActivityId()));

        verify(activityPrizeService, atLeastOnce()).getDetailByActivityId(anyString());
        //  verify(activityInfoService, atLeastOnce()).selectByActivityId(anyString());
        //  verify(activitiesMapper, atLeastOnce()).selectByActivityId(anyString());
        //  verify(enterprisesService, atLeastOnce()).judgeEnterprise(anyLong());
        //        verify(entProductService, atLeastOnce()).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
        //        verify(accountService, atLeastOnce()).get(anyLong(), anyLong(), anyInt());
        //        verify(accountService, atLeastOnce()).isDebt2Account(anyList(),anyLong());
    }

    /**
     * 上架：上架时间在活动开始时间之后分支(正常流程)
     */
    @Test
    public void testOnShelf1() {

        Activities activities = initActivities();
        activities.setStartTime(getDay(new Date(), -1));
        activities.setEndTime(getDay(new Date(), 3));
        activities.setEntId(3L);
        activities.setStatus(ActivityStatus.SAVED.getCode());
        activities.setType(ActivityType.QRCODE.getCode());

        List<ActivityPrize> activityPrizes = new ArrayList<ActivityPrize>();
        ActivityPrize activityPrize = new ActivityPrize();
        activityPrize.setProductId(1L);
        activityPrize.setCount(1L);
        activityPrizes.add(activityPrize);
        activityPrize.setPrice(100L);

        ActivityInfo activityInfo = new ActivityInfo();

        Account account = new Account();
        account.setCount(1000.00);

        when(activityPrizeService.getDetailByActivityId(activities.getActivityId())).thenReturn(activityPrizes);
        when(activityInfoService.selectByActivityId(activities.getActivityId())).thenReturn(activityInfo);
        when(activitiesMapper.selectByActivityId(activities.getActivityId())).thenReturn(activities);
        when(enterprisesService.judgeEnterprise(activities.getEntId())).thenReturn("success");
        when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(new EntProduct());
        when(accountService.get(anyLong(), anyLong(), anyInt())).thenReturn(account);
        when(accountService.isDebt2Account(anyList(), anyLong())).thenReturn(false);

        when(approvalProcessDefinitionService.wheatherNeedApproval(anyInt())).thenReturn(false);
        when(activitiesMapper.changeStatus(anyString(), anyInt())).thenReturn(1);
        when(
                scheduleService.createScheduleJob(any(Class.class), anyString(), anyString(), anyString(),
                        any(Date.class))).thenReturn("success");

        Product flowAccountProduct = new Product();
        flowAccountProduct.setId(1L);
        flowAccountProduct.setFlowAccountFlag(0);
        when(productService.selectProductById(Mockito.anyLong())).thenReturn(flowAccountProduct);

        try {
            activitiesService.onShelf(activities.getActivityId());
        } catch (Exception e) {
            // TODO: handle exception
        }

//        verify(activityPrizeService, atLeastOnce()).getDetailByActivityId(anyString());
//        verify(activityInfoService, atLeastOnce()).selectByActivityId(anyString());
//        verify(activitiesMapper, atLeastOnce()).selectByActivityId(anyString());
//        verify(enterprisesService, atLeastOnce()).judgeEnterprise(anyLong());
//        verify(accountService, atLeastOnce()).get(anyLong(), anyLong(), anyInt());
//        verify(accountService, atLeastOnce()).isDebt2Account(anyList(), anyLong());
//        verify(accountService, atLeastOnce()).isDebt2Account(anyList(), anyLong());
//        verify(approvalProcessDefinitionService, atLeastOnce()).wheatherNeedApproval(anyInt());
//        verify(activitiesMapper, atLeastOnce()).changeStatus(anyString(), anyInt());
//        verify(scheduleService, atLeastOnce()).createScheduleJob(any(Class.class), anyString(), anyString(),
//                anyString(), any(Date.class));
    }

    /**
     * 上架：上架时间在活动开始时间之前分支(正常流程)
     */
    @Test
    public void testOnShelf2() {

        Activities activities = initActivities();
        activities.setStartTime(getDay(new Date(), 1));
        activities.setEndTime(getDay(new Date(), 3));
        activities.setEntId(3L);
        activities.setStatus(ActivityStatus.SAVED.getCode());
        activities.setType(ActivityType.QRCODE.getCode());

        List<ActivityPrize> activityPrizes = new ArrayList<ActivityPrize>();
        ActivityPrize activityPrize = new ActivityPrize();
        activityPrize.setProductId(1L);
        activityPrize.setCount(1L);
        activityPrizes.add(activityPrize);
        activityPrize.setPrice(100L);

        ActivityInfo activityInfo = new ActivityInfo();

        Account account = new Account();
        account.setCount(1000.00);

        when(activityPrizeService.getDetailByActivityId(activities.getActivityId())).thenReturn(activityPrizes);
        when(activityInfoService.selectByActivityId(activities.getActivityId())).thenReturn(activityInfo);
        when(activitiesMapper.selectByActivityId(activities.getActivityId())).thenReturn(activities);
        when(enterprisesService.judgeEnterprise(activities.getEntId())).thenReturn("success");
        when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(new EntProduct());
        when(accountService.get(anyLong(), anyLong(), anyInt())).thenReturn(account);
        when(accountService.isDebt2Account(anyList(), anyLong())).thenReturn(false);

        when(activitiesMapper.changeStatus(anyString(), anyInt())).thenReturn(1);
        when(approvalProcessDefinitionService.wheatherNeedApproval(anyInt())).thenReturn(false);
        when(
                scheduleService.createScheduleJob(any(Class.class), anyString(), anyString(), anyString(),
                        any(Date.class))).thenReturn("success");

        Product flowAccountProduct = new Product();
        flowAccountProduct.setId(1L);
        flowAccountProduct.setFlowAccountFlag(0);
        when(productService.selectProductById(Mockito.anyLong())).thenReturn(flowAccountProduct);

        try {
            activitiesService.onShelf(activities.getActivityId());
        } catch (Exception e) {
            // TODO: handle exception
        }

//        verify(activityPrizeService, atLeastOnce()).getDetailByActivityId(anyString());
//        verify(activityInfoService, atLeastOnce()).selectByActivityId(anyString());
//        verify(activitiesMapper, atLeastOnce()).selectByActivityId(anyString());
//        verify(enterprisesService, atLeastOnce()).judgeEnterprise(anyLong());
//        verify(activitiesMapper, atLeastOnce()).changeStatus(anyString(), anyInt());
//        verify(scheduleService, atLeastOnce()).createScheduleJob(any(Class.class), anyString(), anyString(),
//                anyString(), any(Date.class));
    }

    /**
     * 模拟返回false
     */
    @Test
    public void testInsertIndividualPresentActivity1() {
        String activityName = "测试活动";
        String phonesNum = "15116333333";
        Integer coinsCount = 10;
        Long adminId = 1L;
        Long productId = 1L;
        String ownerMobile = "15116331234";

        List<ActivityWinRecord> activityWinRecords = new ArrayList();
        ActivityWinRecord awr = new ActivityWinRecord();
        activityWinRecords.add(awr);
        IndividualAccount individualAccount = new IndividualAccount();

        when(activityWinRecordService.selectByActivityId(Mockito.anyString())).thenReturn(activityWinRecords);
        when(
                individualAccountService.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(),
                        Mockito.anyInt())).thenReturn(individualAccount);
        when(activitiesMapper.insert(any(Activities.class))).thenReturn(0).thenReturn(1);
        when(activityInfoService.insert(Mockito.any(ActivityInfo.class))).thenReturn(false).thenReturn(true);
        when(activityPrizeService.insert(Mockito.any(ActivityPrize.class))).thenReturn(false).thenReturn(true);
        when(
                activityWinRecordService.batchInsertFlowcoinPresent(Mockito.anyString(), Mockito.anyString(),
                        Mockito.anyLong(), Mockito.anyString())).thenReturn(false).thenReturn(true);
        when(
                individualAccountService.createFlowcoinExchangeAndPurchaseAccount(Mockito.any(IndividualAccount.class),
                        Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenReturn(false).thenReturn(true);
        when(taskProducer.produceFlowcoinPresentMsg(Mockito.any(FlowcoinPresentPojo.class))).thenReturn(false)
                .thenReturn(true);

        assertFalse(activitiesService.insertIndividualPresentActivity(activityName, phonesNum, coinsCount, adminId,
                productId, ownerMobile));
        try {
            activitiesService.insertIndividualPresentActivity(activityName, phonesNum, coinsCount, adminId, productId,
                    ownerMobile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            activitiesService.insertIndividualPresentActivity(activityName, phonesNum, coinsCount, adminId, productId,
                    ownerMobile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            activitiesService.insertIndividualPresentActivity(activityName, phonesNum, coinsCount, adminId, productId,
                    ownerMobile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            activitiesService.insertIndividualPresentActivity(activityName, phonesNum, coinsCount, adminId, productId,
                    ownerMobile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            activitiesService.insertIndividualPresentActivity(activityName, phonesNum, coinsCount, adminId, productId,
                    ownerMobile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        verify(activitiesMapper, Mockito.times(6)).insert(any(Activities.class));
    }

    /**
     * 模拟返回true
     */
    @Test
    public void testInsertIndividualPresentActivity2() {
        String activityName = "测试活动";
        String phonesNum = "15116333333";
        Integer coinsCount = 10;
        Long adminId = 1L;
        Long productId = 1L;
        String ownerMobile = "15116331234";

        List<ActivityWinRecord> activityWinRecords = new ArrayList<ActivityWinRecord>();

        IndividualAccount individualAccount = new IndividualAccount();
        individualAccount.setIndividualProductId(1L);

        when(activitiesMapper.insert(any(Activities.class))).thenReturn(1);
        when(activityInfoService.insert(any(ActivityInfo.class))).thenReturn(true);
        when(activityPrizeService.insert(any(ActivityPrize.class))).thenReturn(true);

        when(activityWinRecordService.batchInsertFlowcoinPresent(anyString(), anyString(), anyLong(), anyString()))
                .thenReturn(true);
        when(
                individualAccountService.createFlowcoinExchangeAndPurchaseAccount(any(IndividualAccount.class),
                        anyString(), anyString(), anyInt())).thenReturn(true);
        when(activityWinRecordService.selectByActivityId(anyString())).thenReturn(activityWinRecords);
        when(individualAccountService.getAccountByOwnerIdAndProductId(anyLong(), anyLong(), anyInt())).thenReturn(
                individualAccount);

        // PowerMockito.mockStatic(TaskProducer.class);
        // PowerMockito.when(TaskProducer.produceFlowcoinPresentMsg(any(FlowcoinPresentPojo.class))).thenReturn(true);
        // when(taskProducer.produceFlowcoinPresentMsg(any(FlowcoinPresentPojo.class))).thenReturn(true);

        assertTrue(activitiesService.insertIndividualPresentActivity(activityName, phonesNum, coinsCount, adminId,
                productId, ownerMobile));

        verify(activitiesMapper, atLeastOnce()).insert(any(Activities.class));
        verify(activityInfoService, atLeastOnce()).insert(any(ActivityInfo.class));
        verify(activityPrizeService, atLeastOnce()).insert(any(ActivityPrize.class));
        verify(activityWinRecordService, atLeastOnce()).batchInsertFlowcoinPresent(anyString(), anyString(), anyLong(),
                anyString());
        verify(individualAccountService, atLeastOnce()).getAccountByOwnerIdAndProductId(anyLong(), anyLong(), anyInt());
    }

    @Test
    public void testGetSendData() {
        Activities activities = initActivities();
        activities.setName("测试活动");
        activities.setCreatorId(1L);
        activities.setStartTime(getDay(new Date(), 1));
        activities.setEndTime(getDay(new Date(), 3));

        List<ActivityPrize> activityPrizes = new ArrayList<ActivityPrize>();

        when(activityTemplateService.selectByActivityId(Mockito.anyString())).thenReturn(new ActivityTemplate());
        
        IndividualProduct product = new IndividualProduct();
        product.setType(IndividualProductType.DEFAULT_FLOW_PACKAGE.getValue());
        product.setId(1L);
        when(individualProductService.getDefaultFlowProduct()).thenReturn(product);

        //invalid
        assertNull(activitiesService.getSendData(null));

        //invalid 活动参数为null
        Mockito.when(activitiesMapper.selectByActivityId(activities.getActivityId())).thenReturn(activities);
        Mockito.when(activitiesMapper.selectByActivityId(activities.getActivityId())).thenReturn(null);
        assertNull(activitiesService.getSendData(activities.getActivityId()));

        //invalid 活动奖品为null
        activities.setActivityId("invalid1");
        Mockito.when(activitiesMapper.selectByActivityId(activities.getActivityId())).thenReturn(activities);
        Mockito.when(activityPrizeService.selectByActivityIdForIndividual(activities.getActivityId())).thenReturn(
                activityPrizes);
        assertNull(activitiesService.getSendData(activities.getActivityId()));

        //valid
        List<ActivityPrize> activityPrizeList = new ArrayList<ActivityPrize>();
        ActivityPrize activityPrize = new ActivityPrize();
        activityPrize.setProductId(1L);
        activityPrize.setSize(10L);
        activityPrize.setCount(1L);
        activityPrize.setId(1L);
        activityPrizeList.add(activityPrize);

        activities.setActivityId("valid");
        Mockito.when(activitiesMapper.selectByActivityId(activities.getActivityId())).thenReturn(activities);
        Mockito.when(activityPrizeService.selectByActivityIdForIndividual(activities.getActivityId())).thenReturn(
                activityPrizeList);
        assertNotNull(activitiesService.getSendData(activities.getActivityId()));

        Mockito.verify(activitiesMapper, atLeastOnce()).selectByActivityId(anyString());
        Mockito.verify(activityPrizeService, atLeastOnce()).selectByActivityIdForIndividual(anyString());
    }

    @Test
    public void testCloseActivity() {
        Activities activities = initActivities();

        ActivityInfo activityInfo = new ActivityInfo();

        List<ActivityPrize> activityPrizes = new ArrayList<ActivityPrize>();

        //invalid
        Mockito.when(activitiesMapper.changeStatus(activities.getActivityId(), ActivityStatus.DOWN.getCode()))
                .thenReturn(0);
        assertFalse(activitiesService.closeActivity(activities, activityInfo, activityPrizes));

        //valid
        activities.setActivityId("invalid");
        Mockito.when(activitiesMapper.changeStatus(activities.getActivityId(), ActivityStatus.DOWN.getCode()))
                .thenReturn(1);
        Mockito.when(individualAccountService.giveBackForActivity(anyString())).thenReturn(false).thenReturn(true);
        Mockito.when(activityTemplateService.notifyTemplateToClose(anyString(), anyString())).thenReturn(false).thenReturn(true);

        try {
            activitiesService.closeActivity(activities, activityInfo, activityPrizes);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            activitiesService.closeActivity(activities, activityInfo, activityPrizes);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertTrue(activitiesService.closeActivity(activities, activityInfo, activityPrizes));

        Mockito.verify(activitiesMapper, atLeastOnce()).changeStatus(anyString(), anyInt());
        Mockito.verify(individualAccountService, atLeastOnce()).giveBackForActivity(anyString());
        Mockito.verify(activityTemplateService, atLeastOnce()).notifyTemplateToClose(anyString(), anyString());
    }
    
    @Test
    public void testInsertRedpacketForIndividual(){
        assertFalse(activitiesService.insertRedpacketForIndividual(null, null));
        
        Activities activity = initActivities();        
        activity.setType(ActivityType.LUCKY_REDPACKET.getCode());

        when(activitiesMapper.insert(Mockito.any(Activities.class))).thenReturn(0);       
        ActivityPrize activityPrize = new ActivityPrize();
        activityPrize.setProductId(1L);
        activityPrize.setSize(10L);
        activityPrize.setCount(1L);

        assertFalse(activitiesService.insertRedpacketForIndividual(activity, activityPrize));
       
    }

    @Test
    public void testJoinCrowdfundingActivity() {
        Mockito.when(activityWinRecordService.insertSelective(Mockito.any(ActivityWinRecord.class))).thenReturn(false);
        when(activitiesMapper.selectByActivityId(anyString())).thenReturn(new Activities());
        when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(new Enterprise());
        try {
            activitiesService.joinCrowdfundingActivity("11", "18867103685", 1L, "wxOpenid");
        } catch (Exception e) {
            System.out.println("异常");
        }

        Mockito.when(activityWinRecordService.insertSelective(Mockito.any(ActivityWinRecord.class))).thenReturn(true);
        CrowdfundingActivityDetail detail = new CrowdfundingActivityDetail();
        detail.setCurrentCount(1L);
        detail.setTargetCount(1L);
        detail.setCurrentCount(1L);
        detail.setResult(0);
        detail.setJoinType(CrowdFundingJoinType.Small_Enterprise.getCode());
        Mockito.when(crowdfundingActivityDetailService.selectByActivityId(Mockito.anyString())).thenReturn(detail);
        Mockito.when(crowdfundingActivityDetailService.updateCurrentCount(Mockito.any(CrowdfundingActivityDetail.class))).thenReturn(false).thenReturn(true);
        Mockito.when(crowdfundingActivityDetailService.updateByPrimaryKeySelective(Mockito.any(CrowdfundingActivityDetail.class))).thenReturn(true);
        Mockito.when(crowdfundingCallbackService.notifyCrowdFundingSucceed(Mockito.anyString())).thenReturn(false);
        try {
            activitiesService.joinCrowdfundingActivity("11", "18867103685", 1L, "wxOpenid");
        } catch (Exception e) {
            System.out.println("异常");
        }
        
        List<ActivityWinRecord> winRecords = new ArrayList<ActivityWinRecord>();
        winRecords.add(new ActivityWinRecord());
        Mockito.when(activityWinRecordService.selectByActivityId(Mockito.anyString())).thenReturn(winRecords);
        
        Mockito.when(taskProducer.produceBatchWxSendTemplateMsg(Mockito.anyList())).thenReturn(false);
        assertTrue(activitiesService.joinCrowdfundingActivity("11", "18867103685", 1L, "wxOpenid"));
    }

    @Test
    public void testCheckUser() {
        List<String> phones = new ArrayList<String>();
        phones.add("18867103685");
        Mockito.when(activityBlackAndWhiteService.selectPhonesByMap(anyMap())).thenReturn(phones);
        assertFalse(activitiesService.checkUser("18867103684", "11"));

        Mockito.when(activityBlackAndWhiteService.selectPhonesByMap(anyMap())).thenReturn(phones);
        assertTrue(activitiesService.checkUser("18867103685", "11"));

    }

    private Enterprise buildEnterprise() {
        Enterprise enterprise = new Enterprise();
        enterprise.setId(1L);
        return enterprise;
    }

    private ActivityWinRecord initActivityWinRecord() {
        ActivityWinRecord activityWinRecord = new ActivityWinRecord();
        activityWinRecord.setActivityId("test");
        activityWinRecord.setOwnMobile("18867103698");
        activityWinRecord.setPrizeId(1L);
        return activityWinRecord;
    }

    private List<Enterprise> initEnterprise() {
        List<Enterprise> enterprises = new ArrayList<Enterprise>();
        Enterprise enter1 = new Enterprise();
        enter1.setId(1L);

        Enterprise enter2 = new Enterprise();
        enter2.setId(2L);

        enterprises.add(enter1);
        enterprises.add(enter2);
        return enterprises;
    }

    private Activities initActivities() {
        Activities record = new Activities();
        record.setActivityId("test");
        record.setType(ActivityType.COMMON_REDPACKET.getCode());
        record.setEntId(1L);
        return record;
    }

    private ActivityInfo initActivityInfo() {
        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setActivityId("test");
        activityInfo.setHasWhiteOrBlack(1);
        return activityInfo;
    }
    
    @Test
    public void testDecryptActivityId(){
        Mockito.when(enterprisesExtInfoService.selectByEcCodeAndEcPrdCode(Mockito.anyString(), Mockito.anyString())).thenReturn(new ArrayList());
        assertNotNull(activitiesService.decryptActivityId("8DCDB26523B92E51D37BC5D162C3477E7832DCF99CCDA41D9864E1E00E9881D099FAA"
                + "7F777CEAB6E70BDED68701BF51B57E8EE0D041F0397E39F2936A6375317", "2000456004", "23123123123wewe"));
    
        List<EnterprisesExtInfo> infos = new ArrayList<EnterprisesExtInfo>();
        EnterprisesExtInfo info = new EnterprisesExtInfo();
        info.setEnterId(1L);
        infos.add(info);
        Mockito.when(enterprisesExtInfoService.selectByEcCodeAndEcPrdCode(Mockito.anyString(), Mockito.anyString())).thenReturn(infos);
        Enterprise enter = new Enterprise();
        enter.setAppKey("83e4d4bfaf854a62967d8849c5a97773");
        enter.setAppSecret("a6269c78c9874950814cfa2bd1cf44f5");        
        Mockito.when(enterprisesService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(enter);
        
        PowerMockito.mockStatic(AES.class);
        byte[] encryptResult = {97, 54, 50, 54, 57, 99, 55, 56, 99, 57, 56, 55, 52, 57, 53, 48, 56, 49, 52, 99, 102, 97, 50, 98, 100, 49, 99, 102, 52, 52, 102, 53, 59, 54, 50, 57, 53, 49, 52, 48, 48, 56, 51, 48, 53, 50, 57, 54, 53, 56, 56, 56};
        when(AES.decrypt(Mockito.any(byte[].class), Mockito.any(byte[].class))).thenReturn(encryptResult);
       
        assertNotNull(activitiesService.decryptActivityId("8DCDB26523B92E51D37BC5D162C3477E7832DCF99CCDA41D9864E1E00E9881D099FAA"
                + "7F777CEAB6E70BDED68701BF51B57E8EE0D041F0397E39F2936A6375317", "2000456004", "23123123123wewe"));
    
        Mockito.when(activitiesMapper.selectByActivityId(Mockito.anyString())).thenReturn(new Activities());
        assertNotNull(activitiesService.decryptActivityId("8DCDB26523B92E51D37BC5D162C3477E7832DCF99CCDA41D9864E1E00E9881D099FAA"
                + "7F777CEAB6E70BDED68701BF51B57E8EE0D041F0397E39F2936A6375317", "2000456004", "23123123123wewe"));
    
    }
    
    @Test
    public void testEncryptActivityId(){
        assertNull(activitiesService.encryptActivityId("6295140083052965888"));
            
        Mockito.when(activitiesMapper.selectByActivityId(Mockito.anyString())).thenReturn(new Activities());
        Enterprise enter = new Enterprise();
        enter.setAppKey("83e4d4bfaf854a62967d8849c5a97773");
        enter.setAppSecret("a6269c78c9874950814cfa2bd1cf44f5");        
        Mockito.when(enterprisesService.selectById(Mockito.anyLong())).thenReturn(enter);
        
        PowerMockito.mockStatic(AES.class);
        byte[] encryptResult = {-115, -51, -78, 101, 35, -71, 46, 81, -45, 123, -59, -47, 98, -61, 71, 126, 120, 50, -36, -7, -100, -51, -92, 29, -104, 100, -31, -32, 14, -104, -127, -48, -103, -6, -89, -9, 119, -50, -85, 110, 112, -67, -19, 104, 112, 27, -11, 27, 87, -24, -18, 13, 4, 31, 3, -105, -29, -97, 41, 54, -90, 55, 83, 23};
        when(AES.encrypt(Mockito.any(byte[].class), Mockito.any(byte[].class))).thenReturn(encryptResult);

        assertNotNull(activitiesService.encryptActivityId("6295140083052965888"));
    }
    @Test
    public void testSelectByMapForGDCrowdFunding() {
        Map map = new HashMap();
        map.put("managerId", "1");
        map.put("status", "status");
        List<Enterprise> enterprises = new ArrayList<Enterprise>();
        Enterprise enterprise = new Enterprise();
        enterprises.add(enterprise);
        Mockito.when(enterprisesService.getEnterByManagerId(Mockito.anyLong()))
            .thenReturn(null).thenReturn(enterprises);
        Mockito.when(activitiesMapper.selectByMapForGDCrowdFunding(anyMap())).thenReturn(null);
        assertNull(activitiesService.selectByMapForGDCrowdFunding(map));
        assertNull(activitiesService.selectByMapForGDCrowdFunding(map));
    }

    @Test
    public void testCountByMapGDCrowdFunding() {
        Map map = new HashMap();
        map.put("managerId", "1");
        map.put("status", "status");
        List<Enterprise> enterprises = new ArrayList<Enterprise>();
        Enterprise enterprise = new Enterprise();
        enterprises.add(enterprise);
        Mockito.when(enterprisesService.getEnterByManagerId(Mockito.anyLong()))
            .thenReturn(null).thenReturn(enterprises);
        Mockito.when(activitiesMapper.countByMapGDCrowdFunding(anyMap())).thenReturn(null);
        assertNotNull(activitiesService.countByMapGDCrowdFunding(map));
        assertNull(activitiesService.countByMapGDCrowdFunding(map));
    }
}
