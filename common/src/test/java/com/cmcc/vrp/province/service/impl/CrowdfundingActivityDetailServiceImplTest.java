package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.CrowdfundingActivityDetailMapper;
import com.cmcc.vrp.province.model.CrowdfundingActivityDetail;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.CrowdfundingActivityDetailService;

@RunWith(MockitoJUnitRunner.class)
public class CrowdfundingActivityDetailServiceImplTest {
	@InjectMocks
	CrowdfundingActivityDetailService crowdfundingActivityDetailService = 
			new CrowdfundingActivityDetailServiceImpl();

	@Mock
	ActivitiesService activitiesService;
	@Mock
    CrowdfundingActivityDetailMapper mapper;
	@Mock
    ActivityPrizeService activityPrizeService;
	
	@Test
	public void testDeleteByPrimaryKey(){
		assertFalse(crowdfundingActivityDetailService.deleteByPrimaryKey(null));
		
		Mockito.when(mapper.deleteByPrimaryKey(Mockito.anyLong())).thenReturn(1);
		assertTrue(crowdfundingActivityDetailService.deleteByPrimaryKey(1L));
		Mockito.verify(mapper).deleteByPrimaryKey(Mockito.anyLong());
	}
	
	@Test
	public void testInsert(){
		assertFalse(crowdfundingActivityDetailService.insert(null));
		
		Mockito.when(mapper.insert(Mockito.any(CrowdfundingActivityDetail.class))).thenReturn(1);
		assertTrue(crowdfundingActivityDetailService.insert(initCrowdfundingActivityDetail()));
		Mockito.verify(mapper).insert(Mockito.any(CrowdfundingActivityDetail.class));
	}
	
	@Test
	public void testInsertSelective(){
		assertFalse(crowdfundingActivityDetailService.insertSelective(null));
		
		Mockito.when(mapper.insertSelective(Mockito.any(CrowdfundingActivityDetail.class))).thenReturn(1);
		assertTrue(crowdfundingActivityDetailService.insertSelective(initCrowdfundingActivityDetail()));
		Mockito.verify(mapper).insertSelective(Mockito.any(CrowdfundingActivityDetail.class));
	}
	
	@Test
	public void testSelectByPrimaryKey(){
		assertNull(crowdfundingActivityDetailService.selectByPrimaryKey(null));
		
		Mockito.when(mapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(initCrowdfundingActivityDetail());
		assertNotNull(crowdfundingActivityDetailService.selectByPrimaryKey(1L));
		Mockito.verify(mapper).selectByPrimaryKey(Mockito.anyLong());
	}
	
	@Test
	public void testSelectByActivityId(){
		assertNull(crowdfundingActivityDetailService.selectByActivityId(null));
		
		Mockito.when(mapper.selectByActivityId(Mockito.anyString())).thenReturn(initCrowdfundingActivityDetail());
		assertNotNull(crowdfundingActivityDetailService.selectByActivityId("test"));
		Mockito.verify(mapper).selectByActivityId(Mockito.anyString());
	}
	
	@Test
	public void testUpdateByPrimaryKeySelective(){
		assertFalse(crowdfundingActivityDetailService.updateByPrimaryKeySelective(null));
		
		Mockito.when(mapper.updateByPrimaryKeySelective(Mockito.any(CrowdfundingActivityDetail.class))).thenReturn(1);
		assertTrue(crowdfundingActivityDetailService.updateByPrimaryKeySelective(initCrowdfundingActivityDetail()));
		Mockito.verify(mapper).updateByPrimaryKeySelective(Mockito.any(CrowdfundingActivityDetail.class));
	}
	
	@Test
	public void testUpdateByPrimaryKey(){
		assertFalse(crowdfundingActivityDetailService.updateByPrimaryKey(null));
		
		Mockito.when(mapper.updateByPrimaryKey(Mockito.any(CrowdfundingActivityDetail.class))).thenReturn(1);
		assertTrue(crowdfundingActivityDetailService.updateByPrimaryKey(initCrowdfundingActivityDetail()));
		Mockito.verify(mapper).updateByPrimaryKey(Mockito.any(CrowdfundingActivityDetail.class));
	}
	
	@Test
	public void testUpdateCurrentCount(){
		Mockito.when(mapper.updateCurrentCount(Mockito.any(CrowdfundingActivityDetail.class))).thenReturn(1);
		assertTrue(crowdfundingActivityDetailService.updateCurrentCount(initCrowdfundingActivityDetail()));
		Mockito.verify(mapper).updateCurrentCount(Mockito.any(CrowdfundingActivityDetail.class));
	}
	
	
	private CrowdfundingActivityDetail initCrowdfundingActivityDetail(){
		CrowdfundingActivityDetail record = new CrowdfundingActivityDetail();
		record.setActivityId("test");
		return record;
	}

}
