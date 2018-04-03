package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityPaymentInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.CrowdfundingActivityDetail;
import com.cmcc.vrp.province.model.CrowdfundingQueryUrl;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityBlackAndWhiteService;
import com.cmcc.vrp.province.service.ActivityPaymentInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.CrowdFundingService;
import com.cmcc.vrp.province.service.CrowdfundingActivityDetailService;
import com.cmcc.vrp.province.service.CrowdfundingQueryUrlService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ActivitiesWinPojo;
import com.cmcc.vrp.wx.PayResultQueryService;

/**
 * <p>Description: </p>
 * @author qinqinyan
 */
@RunWith(MockitoJUnitRunner.class)
public class CrowdFundingServiceImplTest {
	
    @InjectMocks
	CrowdFundingService crowdFundingService = new CrowdFundingServiceImpl();
    @Mock
	ActivitiesService activitiesService;
    @Mock
    EnterprisesService enterprisesService;
    @Mock
    CrowdfundingActivityDetailService crowdfundingActivityDetailService;
    @Mock
    ActivityPrizeService activityPrizeService;
    @Mock
    ActivityBlackAndWhiteService activityBlackAndWhiteService;
    @Mock
    CrowdfundingQueryUrlService crowdfundingQueryUrlService;
    @Mock
    ActivityWinRecordService activityWinRecordService;
    @Mock
    SerialNumService serialNumService;
    @Mock
    ChargeRecordService chargeRecordService;
    @Mock
    TaskProducer taskProducer;
    @Mock
    ActivityPaymentInfoService activityPaymentInfoService;
    @Mock
    PayResultQueryService payResultQueryService;
    /**
     * 
     */
    @Test
	public void testInsertActivity(){
        Activities activities = initActivities();
        CrowdfundingActivityDetail crowdfundingActivityDetail = initCrowdfundingActivityDetail();
        List<ActivityPrize> list = initActivityPrizes();
		
        assertFalse(crowdFundingService.insertActivity(activities, crowdfundingActivityDetail, null, null, null));
		
        Mockito.when(activitiesService.insert(Mockito.any(Activities.class))).thenReturn(false);
        assertFalse(crowdFundingService.insertActivity(activities, crowdfundingActivityDetail, list, null, null));
        Mockito.verify(activitiesService).insert(Mockito.any(Activities.class));
    }
	
    /**
     * 
     */
    @Test(expected = RuntimeException.class)
	public void testInsertActivity1(){
        Activities activities = initActivities();
        CrowdfundingActivityDetail crowdfundingActivityDetail = initCrowdfundingActivityDetail();
        List<ActivityPrize> list = initActivityPrizes();
		
        Mockito.when(activitiesService.insert(Mockito.any(Activities.class))).thenReturn(true);
        Mockito.when(activityPrizeService.batchInsertForCrowdFunding(Mockito.anyList())).thenReturn(false);

        assertFalse(crowdFundingService.insertActivity(activities, crowdfundingActivityDetail, list, null, null));
        Mockito.verify(activitiesService).insert(Mockito.any(Activities.class));
        Mockito.verify(activityPrizeService).batchInsertForCrowdFunding(Mockito.anyList());
    }
	
    /**
     * 
     */
    @Test(expected = RuntimeException.class)
	public void testInsertActivity2(){
        Activities activities = initActivities();
        CrowdfundingActivityDetail crowdfundingActivityDetail = initCrowdfundingActivityDetail();
        List<ActivityPrize> list = initActivityPrizes();
		
        Mockito.when(activitiesService.insert(Mockito.any(Activities.class))).thenReturn(true);
        Mockito.when(activityPrizeService.batchInsertForCrowdFunding(Mockito.anyList())).thenReturn(true);
		
        assertFalse(crowdFundingService.insertActivity(activities, crowdfundingActivityDetail, list, null, null));
        Mockito.verify(activitiesService).insert(Mockito.any(Activities.class));
        Mockito.verify(activityPrizeService).batchInsertForCrowdFunding(Mockito.anyList());
    }
	
    /**
     * 
     */
    @Test(expected = RuntimeException.class)
	public void testInsertActivity3(){
        Activities activities = initActivities();
        CrowdfundingActivityDetail crowdfundingActivityDetail = initCrowdfundingActivityDetail();
        List<ActivityPrize> list = initActivityPrizes();
		
        Mockito.when(activitiesService.insert(Mockito.any(Activities.class))).thenReturn(true);
        Mockito.when(activityPrizeService.batchInsertForCrowdFunding(Mockito.anyList())).thenReturn(true);
        Mockito.when(crowdfundingActivityDetailService
				.insert(Mockito.any(CrowdfundingActivityDetail.class))).thenReturn(false);
		
        assertFalse(crowdFundingService.insertActivity(activities, crowdfundingActivityDetail, list, null, null));
    }
	
	/**
	 * 
	 */
    @Test(expected = RuntimeException.class)
	public void testInsertActivity4(){
        Activities activities = initActivities();
        CrowdfundingActivityDetail crowdfundingActivityDetail = initCrowdfundingActivityDetail();
        List<ActivityPrize> list = initActivityPrizes();
        String phones = "18867101111";
        crowdfundingActivityDetail.setUserList(1);
		
        Mockito.when(activitiesService.insert(Mockito.any(Activities.class))).thenReturn(true);
        Mockito.when(activityPrizeService.batchInsertForCrowdFunding(Mockito.anyList())).thenReturn(true);
        Mockito.when(crowdfundingActivityDetailService
				.insert(Mockito.any(CrowdfundingActivityDetail.class))).thenReturn(true);
        Mockito.when(activityBlackAndWhiteService
				.batchInsert(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString())).thenReturn(false);
		
        assertFalse(crowdFundingService.insertActivity(activities, crowdfundingActivityDetail, list, phones, null));
        Mockito.verify(activitiesService).insert(Mockito.any(Activities.class));
        Mockito.verify(activityPrizeService).batchInsertForCrowdFunding(Mockito.anyList());
        Mockito.verify(activityBlackAndWhiteService).batchInsert(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString());
    }
	
    /**
     * 
     */
    @Test
	public void testInsertActivity5(){
        Activities activities = initActivities();
        CrowdfundingActivityDetail crowdfundingActivityDetail = initCrowdfundingActivityDetail();
        crowdfundingActivityDetail.setUserList(1);
        List<ActivityPrize> list = initActivityPrizes();
		
        Mockito.when(activitiesService.insert(Mockito.any(Activities.class))).thenReturn(true);
        Mockito.when(activityPrizeService.batchInsertForCrowdFunding(Mockito.anyList())).thenReturn(true);
        Mockito.when(crowdfundingActivityDetailService
				.insert(Mockito.any(CrowdfundingActivityDetail.class))).thenReturn(true);
		
        assertTrue(crowdFundingService.insertActivity(activities, crowdfundingActivityDetail, list, null, null));
        Mockito.verify(activitiesService).insert(Mockito.any(Activities.class));
        Mockito.verify(activityPrizeService).batchInsertForCrowdFunding(Mockito.anyList());
        Mockito.verify(crowdfundingActivityDetailService).insert(Mockito.any(CrowdfundingActivityDetail.class));
    }
    @Test
    public void testInsertActivity6(){
        Activities activities = initActivities();
        CrowdfundingActivityDetail crowdfundingActivityDetail = initCrowdfundingActivityDetail();
        crowdfundingActivityDetail.setUserList(3);
        crowdfundingActivityDetail.setId(1l);
        List<ActivityPrize> list = initActivityPrizes();
        
        Mockito.when(activitiesService.insert(Mockito.any(Activities.class))).thenReturn(true);
        Mockito.when(activityPrizeService.batchInsertForCrowdFunding(Mockito.anyList())).thenReturn(true);
        Mockito.when(crowdfundingActivityDetailService
                .insert(Mockito.any(CrowdfundingActivityDetail.class))).thenReturn(true);
        Mockito.when(crowdfundingQueryUrlService
                .insert(Mockito.any(CrowdfundingQueryUrl.class))).thenReturn(true);
        
        assertTrue(crowdFundingService.insertActivity(activities, crowdfundingActivityDetail, list, null, null));
        Mockito.verify(activitiesService).insert(Mockito.any(Activities.class));
        Mockito.verify(activityPrizeService).batchInsertForCrowdFunding(Mockito.anyList());
        Mockito.verify(crowdfundingActivityDetailService).insert(Mockito.any(CrowdfundingActivityDetail.class));
    }
    
    /**
     * 
     */
    @Test(expected = RuntimeException.class)
    public void testInsertActivity7(){
        Activities activities = initActivities();
        CrowdfundingActivityDetail crowdfundingActivityDetail = initCrowdfundingActivityDetail();
        crowdfundingActivityDetail.setUserList(3);
        crowdfundingActivityDetail.setId(1l);
        List<ActivityPrize> list = initActivityPrizes();
        
        Mockito.when(activitiesService.insert(Mockito.any(Activities.class))).thenReturn(true);
        Mockito.when(activityPrizeService.batchInsertForCrowdFunding(Mockito.anyList())).thenReturn(true);
        Mockito.when(crowdfundingActivityDetailService
                .insert(Mockito.any(CrowdfundingActivityDetail.class))).thenReturn(true);
        Mockito.when(crowdfundingQueryUrlService
                .insert(Mockito.any(CrowdfundingQueryUrl.class))).thenReturn(false);
        
        assertTrue(crowdFundingService.insertActivity(activities, crowdfundingActivityDetail, list, null, null));
    }
    /**
     * 
     */
    @Test
	public void testUpdateActivity(){
        Mockito.when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(initActivities());
        Mockito.when(activityPrizeService.selectByActivityId(Mockito.anyString())).thenReturn(initActivityPrizes());
        Mockito.when(crowdfundingActivityDetailService.selectByActivityId(Mockito.anyString())).thenReturn(initCrowdfundingActivityDetail());
		
        Activities activities = initActivities();
        CrowdfundingActivityDetail crowdfundingActivityDetail = initCrowdfundingActivityDetail();
        List<ActivityPrize> list = initActivityPrizes();
        list.get(0).setProductId(5L);
        list.get(1).setDiscount(10);
		
        Mockito.when(activityPrizeService.batchInsertForCrowdFunding(Mockito.anyList())).thenReturn(false);
        assertFalse(crowdFundingService.updateActivity(activities, crowdfundingActivityDetail, list, null, null));
        Mockito.verify(activityPrizeService).batchInsertForCrowdFunding(Mockito.anyList());
    }
	
    /**
     * 
     */
    @Test(expected = RuntimeException.class)
	public void testUpdateActivity1(){
        Mockito.when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(initActivities());
        Mockito.when(activityPrizeService.selectByActivityId(Mockito.anyString())).thenReturn(initActivityPrizes());
        Mockito.when(crowdfundingActivityDetailService.selectByActivityId(Mockito.anyString())).thenReturn(initCrowdfundingActivityDetail());
		
        Activities activities = initActivities();
        CrowdfundingActivityDetail crowdfundingActivityDetail = initCrowdfundingActivityDetail();
        List<ActivityPrize> list = initActivityPrizes();
        list.get(0).setProductId(5L);
        list.get(1).setDiscount(10);
		
        Mockito.when(activityPrizeService.batchInsertForCrowdFunding(Mockito.anyList())).thenReturn(true);
        Mockito.when(activityPrizeService.deleteActivityPrize(Mockito.anyList(), Mockito.anyString())).thenReturn(false);
        assertFalse(crowdFundingService.updateActivity(activities, crowdfundingActivityDetail, list, null, null));
    }
	
    /**
     * 
     */
    @Test(expected = RuntimeException.class)
	public void testUpdateActivity2(){
        Mockito.when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(initActivities());
        Mockito.when(activityPrizeService.selectByActivityId(Mockito.anyString())).thenReturn(initActivityPrizes());
        Mockito.when(crowdfundingActivityDetailService.selectByActivityId(Mockito.anyString())).thenReturn(initCrowdfundingActivityDetail());
		
        Activities activities = initActivities();
        CrowdfundingActivityDetail crowdfundingActivityDetail = initCrowdfundingActivityDetail();
        List<ActivityPrize> list = initActivityPrizes();
        list.get(0).setProductId(5L);
        list.get(1).setDiscount(10);
		
        Mockito.when(activityPrizeService.batchInsertForCrowdFunding(Mockito.anyList())).thenReturn(true);
        Mockito.when(activityPrizeService.deleteActivityPrize(Mockito.anyList(), Mockito.anyString())).thenReturn(true);
        Mockito.when(activityPrizeService.batchUpdateDiscount(Mockito.anyList())).thenReturn(false);
        assertFalse(crowdFundingService.updateActivity(activities, crowdfundingActivityDetail, list, null, null));
    }
	
	/**
	 * 
	 */
    @Test(expected = RuntimeException.class)
	public void testUpdateActivity3(){
        Mockito.when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(initActivities());
        Mockito.when(activityPrizeService.selectByActivityId(Mockito.anyString())).thenReturn(initActivityPrizes());
        Mockito.when(crowdfundingActivityDetailService.selectByActivityId(Mockito.anyString())).thenReturn(initCrowdfundingActivityDetail());
		
        Activities activities = initActivities();
        CrowdfundingActivityDetail crowdfundingActivityDetail = initCrowdfundingActivityDetail();
        List<ActivityPrize> list = initActivityPrizes();
        list.get(0).setProductId(5L);
        list.get(1).setDiscount(10);
		
        Mockito.when(activityPrizeService.batchInsertForCrowdFunding(Mockito.anyList())).thenReturn(true);
        Mockito.when(activityPrizeService.deleteActivityPrize(Mockito.anyList(), Mockito.anyString())).thenReturn(true);
        Mockito.when(activityPrizeService.batchUpdateDiscount(Mockito.anyList())).thenReturn(true);
		
        Mockito.when(activitiesService.updateByPrimaryKeySelective(Mockito.any(Activities.class))).thenReturn(false);
        assertFalse(crowdFundingService.updateActivity(activities, crowdfundingActivityDetail, list, null, null));
    }
	
	/**
	 * 
	 */
    @Test(expected = RuntimeException.class)
	public void testUpdateActivity4(){
        Mockito.when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(initActivities());
        Mockito.when(activityPrizeService.selectByActivityId(Mockito.anyString())).thenReturn(initActivityPrizes());
        Mockito.when(crowdfundingActivityDetailService.selectByActivityId(Mockito.anyString())).thenReturn(initCrowdfundingActivityDetail());
		
        Activities activities = initActivities();
        CrowdfundingActivityDetail crowdfundingActivityDetail = initCrowdfundingActivityDetail();
        List<ActivityPrize> list = initActivityPrizes();
        list.get(0).setProductId(5L);
        list.get(1).setDiscount(10);
		
        Mockito.when(activityPrizeService.batchInsertForCrowdFunding(Mockito.anyList())).thenReturn(true);
        Mockito.when(activityPrizeService.deleteActivityPrize(Mockito.anyList(), Mockito.anyString())).thenReturn(true);
        Mockito.when(activityPrizeService.batchUpdateDiscount(Mockito.anyList())).thenReturn(true);
		
        Mockito.when(activitiesService.updateByPrimaryKeySelective(Mockito.any(Activities.class))).thenReturn(true);
		
        Mockito.when(crowdfundingActivityDetailService.updateByPrimaryKeySelective(Mockito.any(CrowdfundingActivityDetail.class))).thenReturn(false);
        assertFalse(crowdFundingService.updateActivity(activities, crowdfundingActivityDetail, list, null, null));
    }
	
    /**
     * 
     */
    @Test(expected = RuntimeException.class)
	@Ignore
	public void testUpdateActivity5(){
        Mockito.when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(initActivities());
        Mockito.when(activityPrizeService.selectByActivityId(Mockito.anyString())).thenReturn(initActivityPrizes());
        Mockito.when(crowdfundingActivityDetailService.selectByActivityId(Mockito.anyString())).thenReturn(initCrowdfundingActivityDetail());
		
        Activities activities = initActivities();
        CrowdfundingActivityDetail crowdfundingActivityDetail = initCrowdfundingActivityDetail();
        List<ActivityPrize> list = initActivityPrizes();
        list.get(0).setProductId(5L);
        list.get(1).setDiscount(10);
		
        Mockito.when(activityPrizeService.batchInsertForCrowdFunding(Mockito.anyList())).thenReturn(true);
        Mockito.when(activityPrizeService.deleteActivityPrize(Mockito.anyList(), Mockito.anyString())).thenReturn(true);
        Mockito.when(activityPrizeService.batchUpdateDiscount(Mockito.anyList())).thenReturn(true);
		
        Mockito.when(activitiesService.updateByPrimaryKeySelective(Mockito.any(Activities.class))).thenReturn(true);
		
        Mockito.when(crowdfundingActivityDetailService.updateByPrimaryKeySelective(Mockito.any(CrowdfundingActivityDetail.class))).thenReturn(true);
		
        crowdfundingActivityDetail.setHasWhiteOrBlack(0);
        Mockito.when(activityBlackAndWhiteService.deleteByActivityId(Mockito.anyString())).thenReturn(false);
        assertFalse(crowdFundingService.updateActivity(activities, crowdfundingActivityDetail, list, null, null));
    }
	
	/**
	 * 
	 */
    @Test(expected = RuntimeException.class)
	public void testUpdateActivity6(){
        Mockito.when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(initActivities());
        Mockito.when(activityPrizeService.selectByActivityId(Mockito.anyString())).thenReturn(initActivityPrizes());
        Mockito.when(crowdfundingActivityDetailService.selectByActivityId(Mockito.anyString())).thenReturn(initCrowdfundingActivityDetail());
		
        Activities activities = initActivities();
        CrowdfundingActivityDetail crowdfundingActivityDetail = initCrowdfundingActivityDetail();
        List<ActivityPrize> list = initActivityPrizes();
        list.get(0).setProductId(5L);
        list.get(1).setDiscount(10);
		
        Mockito.when(activityPrizeService.batchInsertForCrowdFunding(Mockito.anyList())).thenReturn(true);
        Mockito.when(activityPrizeService.deleteActivityPrize(Mockito.anyList(), Mockito.anyString())).thenReturn(true);
        Mockito.when(activityPrizeService.batchUpdateDiscount(Mockito.anyList())).thenReturn(true);
		
        Mockito.when(activitiesService.updateByPrimaryKeySelective(Mockito.any(Activities.class))).thenReturn(true);
		
        Mockito.when(crowdfundingActivityDetailService.updateByPrimaryKeySelective(Mockito.any(CrowdfundingActivityDetail.class))).thenReturn(true);
		
        crowdfundingActivityDetail.setHasWhiteOrBlack(2);
        String phones = "18867101111";
        Mockito.when(activityBlackAndWhiteService.deleteByActivityId(Mockito.anyString())).thenReturn(true);
        Mockito.when(activityBlackAndWhiteService.batchInsert(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString())).thenReturn(false);
        assertFalse(crowdFundingService.updateActivity(activities, crowdfundingActivityDetail, list, phones, null));
    }
	
    /**
     * 
     */
    @Test
	public void testUpdateActivity7(){
        Mockito.when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(initActivities());
        Mockito.when(activityPrizeService.selectByActivityId(Mockito.anyString())).thenReturn(initActivityPrizes());
        Mockito.when(crowdfundingActivityDetailService.selectByActivityId(Mockito.anyString())).thenReturn(initCrowdfundingActivityDetail());
		
        Activities activities = initActivities();
        CrowdfundingActivityDetail crowdfundingActivityDetail = initCrowdfundingActivityDetail();
        List<ActivityPrize> list = initActivityPrizes();
        list.get(0).setProductId(5L);
        list.get(1).setDiscount(10);
		
        Mockito.when(activityPrizeService.batchInsertForCrowdFunding(Mockito.anyList())).thenReturn(true);
        Mockito.when(activityPrizeService.deleteActivityPrize(Mockito.anyList(), Mockito.anyString())).thenReturn(true);
        Mockito.when(activityPrizeService.batchUpdateDiscount(Mockito.anyList())).thenReturn(true);
		
        Mockito.when(activitiesService.updateByPrimaryKeySelective(Mockito.any(Activities.class))).thenReturn(true);
		
        Mockito.when(crowdfundingActivityDetailService.updateByPrimaryKeySelective(Mockito.any(CrowdfundingActivityDetail.class))).thenReturn(true);
		
        crowdfundingActivityDetail.setHasWhiteOrBlack(2);
        crowdfundingActivityDetail.setUserList(1);;
        String phones = "18867101111";
        Mockito.when(activityBlackAndWhiteService.deleteByActivityId(Mockito.anyString())).thenReturn(true);
        Mockito.when(activityBlackAndWhiteService.batchInsert(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString())).thenReturn(true);
        assertTrue(crowdFundingService.updateActivity(activities, crowdfundingActivityDetail, list, phones, null));
    }
	
	/**
	 * 
	 */
    @Test(expected = RuntimeException.class)
	public void testUpdateActivity8(){
        Mockito.when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(initActivities());
        Mockito.when(activityPrizeService.selectByActivityId(Mockito.anyString())).thenReturn(initActivityPrizes());
        Mockito.when(crowdfundingActivityDetailService.selectByActivityId(Mockito.anyString())).thenReturn(initCrowdfundingActivityDetail());
		
        Activities activities = initActivities();
        CrowdfundingActivityDetail crowdfundingActivityDetail = initCrowdfundingActivityDetail();
        List<ActivityPrize> list = initActivityPrizes();
        activities.setEntId(2L);
		//list.get(0).setProductId(5L);
		//list.get(1).setDiscount(10);
		
        Mockito.when(activityPrizeService.deleteByActivityId(Mockito.anyString())).thenReturn(true);
        Mockito.when(activityPrizeService.batchInsert(Mockito.anyList())).thenReturn(false);
        assertFalse(crowdFundingService.updateActivity(activities, crowdfundingActivityDetail, list, null, null));
    }
    @Test
    public void testUpdateActivity9(){
        Mockito.when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(initActivities());
        Mockito.when(activityPrizeService.selectByActivityId(Mockito.anyString())).thenReturn(initActivityPrizes());
        Mockito.when(crowdfundingActivityDetailService.selectByActivityId(Mockito.anyString())).thenReturn(initCrowdfundingActivityDetail());
        
        Activities activities = initActivities();
        CrowdfundingActivityDetail crowdfundingActivityDetail = initCrowdfundingActivityDetail();
        List<ActivityPrize> list = initActivityPrizes();
        list.get(0).setProductId(5L);
        list.get(1).setDiscount(10);
        
        Mockito.when(activityPrizeService.batchInsertForCrowdFunding(Mockito.anyList())).thenReturn(true);
        Mockito.when(activityPrizeService.deleteActivityPrize(Mockito.anyList(), Mockito.anyString())).thenReturn(true);
        Mockito.when(activityPrizeService.batchUpdateDiscount(Mockito.anyList())).thenReturn(true);
        
        Mockito.when(activitiesService.updateByPrimaryKeySelective(Mockito.any(Activities.class))).thenReturn(true);
        
        Mockito.when(crowdfundingActivityDetailService.updateByPrimaryKeySelective(Mockito.any(CrowdfundingActivityDetail.class))).thenReturn(true);
        
        crowdfundingActivityDetail.setHasWhiteOrBlack(2);
        crowdfundingActivityDetail.setUserList(3);;
        String phones = "18867101111";
        String url = "http://test";
        Mockito.when(crowdfundingQueryUrlService.getByCrowdfundingActivityDetailId(Mockito.anyLong())).thenReturn(null);
        Mockito.when(crowdfundingQueryUrlService.insert(Mockito.any(CrowdfundingQueryUrl.class))).thenReturn(true);
        assertTrue(crowdFundingService.updateActivity(activities, crowdfundingActivityDetail, list, phones, url));
        Mockito.when(crowdfundingQueryUrlService.getByCrowdfundingActivityDetailId(Mockito.anyLong())).thenReturn(new CrowdfundingQueryUrl());
        Mockito.when(crowdfundingQueryUrlService.updateByCrowdfundingActivityDetailId(Mockito.any(CrowdfundingQueryUrl.class))).thenReturn(true);
        assertTrue(crowdFundingService.updateActivity(activities, crowdfundingActivityDetail, list, phones, url));
    }
    /**
     * 
     */
    @Test
	public void testOffShelf(){
        assertFalse(crowdFundingService.offShelf(null));
		
        Mockito.when(activitiesService.changeStatus(Mockito.anyString(), Mockito.anyInt())).thenReturn(true);
        assertTrue(crowdFundingService.offShelf("test"));
        Mockito.verify(activitiesService).changeStatus(Mockito.anyString(), Mockito.anyInt());
    }
	
    /**
     * 
     */
    @Test
    public void testOnShelf(){
        assertFalse(crowdFundingService.onShelf(null));
        
        Mockito.when(activitiesService.changeStatus(Mockito.anyString(), Mockito.anyInt())).thenReturn(true);
        assertTrue(crowdFundingService.onShelf("test"));
        Mockito.verify(activitiesService).changeStatus(Mockito.anyString(), Mockito.anyInt());
    }
	
	/*@Test
	public void testGetDelelteActivityPrizes(){
		List<ActivityPrize> prizes = initActivityPrizes();
		List<ActivityPrize> historyPrizes = initActivityPrizes();
		historyPrizes.get(0).setProductId(4L);		
		getDelelteActivityPrizes(prizes, historyPrizes);
	}*/
	
	
	//(expected = RuntimeException.class)
	
    @Test
    public void testCharge(){
        Mockito.when(activityWinRecordService.selectByRecordId(Mockito.anyString())).thenReturn(createActivityWinRecord());
        Mockito.when(activityWinRecordService.updateByPrimaryKey(Mockito.any(ActivityWinRecord.class))).thenReturn(false);
        assertFalse(crowdFundingService.charge(Mockito.anyString()));
        
        Mockito.when(activityWinRecordService.updateByPrimaryKey(Mockito.any(ActivityWinRecord.class))).thenReturn(true);
        Mockito.when(serialNumService.insert(Mockito.any(SerialNum.class))).thenReturn(false);
        assertFalse(crowdFundingService.charge(Mockito.anyString()));        
        
        Mockito.when(serialNumService.insert(Mockito.any(SerialNum.class))).thenReturn(true);
        
        //insertRabbitmq-false
        Mockito.when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(createActivities());
        Mockito.when(chargeRecordService.create(Mockito.any(ChargeRecord.class))).thenReturn(false);
        Mockito.when(activityPrizeService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(initActivityPrize());
        assertFalse(crowdFundingService.charge(Mockito.anyString()));        
        
        //insertRabbitmq-true
        Mockito.when(chargeRecordService.create(Mockito.any(ChargeRecord.class))).thenReturn(true);
        Mockito.when(taskProducer.produceActivityWinMsg(Mockito.any(ActivitiesWinPojo.class))).thenReturn(true);
        Mockito.when(activityWinRecordService.updateStatusCodeByRecordId(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Mockito.when(chargeRecordService.updateStatusCode(Mockito.anyLong(), Mockito.anyString())).thenReturn(true);
        assertTrue(crowdFundingService.charge(Mockito.anyString()));        
        
    }
    
    @Test
    public void testQueryPayResult(){
        Mockito.when(activityWinRecordService.getWinRecordsForCrowdFundingByMap(Mockito.anyMap())).thenReturn(createWinRecords());
        Mockito.when(activityPaymentInfoService.selectByWinRecordId(Mockito.anyString())).thenReturn(createPayInfos());
        Mockito.when(payResultQueryService.checkPayingStatus(Mockito.anyString())).thenReturn(true);
        assertNotNull(crowdFundingService.queryPayResult(Mockito.anyString()));
    }
    
    
    @Test
    public void testProcessPayingRecord(){
        Mockito.when(activityWinRecordService.getWinRecordsForCrowdFundingByMap(Mockito.anyMap())).thenReturn(new ArrayList<ActivityWinRecord>());
        assertFalse(crowdFundingService.processPayingRecord("test", "18867103685"));
        
        Mockito.when(activityWinRecordService.getWinRecordsForCrowdFundingByMap(Mockito.anyMap())).thenReturn(createWinRecords());
        Mockito.when(activityPaymentInfoService.selectByWinRecordId(Mockito.anyString())).thenReturn(createPayInfos());        
        Mockito.when(activityPaymentInfoService.updateBySysSerialNumSelective(Mockito.any(ActivityPaymentInfo.class))).thenReturn(false);
        assertFalse(crowdFundingService.processPayingRecord("test", "18867103685"));
        
        
        Mockito.when(activityPaymentInfoService.updateBySysSerialNumSelective(Mockito.any(ActivityPaymentInfo.class))).thenReturn(true);
        Mockito.when(activityWinRecordService.updateByPrimaryKeySelective(Mockito.any(ActivityWinRecord.class))).thenReturn(false);
        assertFalse(crowdFundingService.processPayingRecord("test", "18867103685"));
        
        Mockito.when(activityWinRecordService.updateByPrimaryKeySelective(Mockito.any(ActivityWinRecord.class))).thenReturn(true);
        assertTrue(crowdFundingService.processPayingRecord("test", "18867103685"));
    }
    
    
    private List<ActivityPaymentInfo> createPayInfos() {        
        ActivityPaymentInfo info = new ActivityPaymentInfo();
        info.setStatus(1);
        
        List<ActivityPaymentInfo> infos = new ArrayList<ActivityPaymentInfo>();
        infos.add(info);
        return infos;
    }

    private List<ActivityWinRecord> createWinRecords() {
        List<ActivityWinRecord> winRecords = new ArrayList<ActivityWinRecord>();
        ActivityWinRecord record = new ActivityWinRecord();
        record.setActivityId("test");
        winRecords.add(record);
        
        return winRecords;
    }

    private Activities createActivities() {
        Activities a = new Activities();
        a.setType(13);
        return a;
    }

    private ActivityWinRecord createActivityWinRecord() {
        ActivityWinRecord record = new ActivityWinRecord();
        return record;
    }

    private Activities initActivities(){
        Activities activities = new Activities();
        activities.setActivityId("test");
        activities.setEntId(1L);
        return activities;
    }
	
    private CrowdfundingActivityDetail initCrowdfundingActivityDetail(){
        CrowdfundingActivityDetail record = new CrowdfundingActivityDetail();
        record.setActivityId("test");
        record.setHasWhiteOrBlack(1);
        return record;
    }
	
    private List<ActivityPrize> initActivityPrizes(){
        List<ActivityPrize> list = new ArrayList<ActivityPrize>();
        ActivityPrize item1 = new ActivityPrize();
        item1.setProductId(1L);
        item1.setDiscount(50);
		
        ActivityPrize item2 = new ActivityPrize();
        item2.setProductId(2L);
        item2.setDiscount(70);
		
        list.add(item1);
        list.add(item2);
        return list;
		
    }
    
    private ActivityPrize initActivityPrize(){
        ActivityPrize item1 = new ActivityPrize();
        item1.setProductId(1L);
        item1.setDiscount(50);

        return item1;
        
    }

}
