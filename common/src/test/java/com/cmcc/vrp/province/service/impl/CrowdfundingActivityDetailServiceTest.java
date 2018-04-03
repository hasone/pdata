package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.CrowdfundingActivityDetailMapper;
import com.cmcc.vrp.province.model.CrowdfundingActivityDetail;
import com.cmcc.vrp.province.service.CrowdfundingActivityDetailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;

/**
 * Created by qinqinyan on 2017/1/6.
 */
@RunWith(MockitoJUnitRunner.class)
public class CrowdfundingActivityDetailServiceTest {

    @InjectMocks
    CrowdfundingActivityDetailService crowdfundingActivityDetailService
        = new CrowdfundingActivityDetailServiceImpl();

    @Mock
    CrowdfundingActivityDetailMapper crowdfundingActivityDetailMapper;


    @Test
    public void testDeleteByPrimaryKey(){
        assertFalse(crowdfundingActivityDetailService.deleteByPrimaryKey(null));

        Mockito.when(crowdfundingActivityDetailMapper.deleteByPrimaryKey(anyLong())).thenReturn(1);
        assertTrue(crowdfundingActivityDetailService.deleteByPrimaryKey(1L));
        Mockito.verify(crowdfundingActivityDetailMapper).deleteByPrimaryKey(anyLong());
    }

    @Test
    public void testInsert(){
        assertFalse(crowdfundingActivityDetailService.insert(null));

        Mockito.when(crowdfundingActivityDetailMapper.insert(Mockito.any(CrowdfundingActivityDetail.class))).thenReturn(1);
        assertTrue(crowdfundingActivityDetailService.insert(new CrowdfundingActivityDetail()));
        Mockito.verify(crowdfundingActivityDetailMapper).insert(Mockito.any(CrowdfundingActivityDetail.class));
    }

    @Test
    public void testInsertSelective(){
        assertFalse(crowdfundingActivityDetailService.insertSelective(null));

        Mockito.when(crowdfundingActivityDetailMapper.insertSelective(Mockito.any(CrowdfundingActivityDetail.class))).thenReturn(1);
        assertTrue(crowdfundingActivityDetailService.insertSelective(new CrowdfundingActivityDetail()));
        Mockito.verify(crowdfundingActivityDetailMapper).insertSelective(Mockito.any(CrowdfundingActivityDetail.class));
    }

    @Test
    public void testSelectByPrimaryKey(){
        assertNull(crowdfundingActivityDetailService.selectByPrimaryKey(null));

        Mockito.when(crowdfundingActivityDetailMapper.selectByPrimaryKey(anyLong())).thenReturn(new CrowdfundingActivityDetail());
        assertNotNull(crowdfundingActivityDetailService.selectByPrimaryKey(anyLong()));
        Mockito.verify(crowdfundingActivityDetailMapper).selectByPrimaryKey(anyLong());
    }

    @Test
    public void testUpdateByPrimaryKeySelective(){
        assertFalse(crowdfundingActivityDetailService.updateByPrimaryKeySelective(null));

        Mockito.when(crowdfundingActivityDetailMapper.updateByPrimaryKeySelective(Mockito.any(CrowdfundingActivityDetail.class))).thenReturn(1);
        assertTrue(crowdfundingActivityDetailService.updateByPrimaryKeySelective(new CrowdfundingActivityDetail()));
        Mockito.verify(crowdfundingActivityDetailMapper).updateByPrimaryKeySelective(Mockito.any(CrowdfundingActivityDetail.class));
    }

    @Test
    public void testUpdateByPrimaryKey(){
        assertFalse(crowdfundingActivityDetailService.updateByPrimaryKey(null));

        Mockito.when(crowdfundingActivityDetailMapper.updateByPrimaryKey(Mockito.any(CrowdfundingActivityDetail.class))).thenReturn(1);
        assertTrue(crowdfundingActivityDetailService.updateByPrimaryKey(new CrowdfundingActivityDetail()));
        Mockito.verify(crowdfundingActivityDetailMapper).updateByPrimaryKey(Mockito.any(CrowdfundingActivityDetail.class));
    }
    
    @Test
    public void testSelectByActivityId(){
        assertNull(crowdfundingActivityDetailService.selectByActivityId(null));

        Mockito.when(crowdfundingActivityDetailMapper.selectByActivityId(Mockito.anyString())).thenReturn(new CrowdfundingActivityDetail());
        assertNotNull(crowdfundingActivityDetailService.selectByActivityId("11"));
        Mockito.verify(crowdfundingActivityDetailMapper).selectByActivityId(Mockito.anyString());
    }
    
    @Test
    public void testUpdateCurrentCount(){
        Mockito.when(crowdfundingActivityDetailMapper.updateCurrentCount(Mockito.any(CrowdfundingActivityDetail.class))).thenReturn(0).thenReturn(1);
        assertFalse(crowdfundingActivityDetailService.updateCurrentCount(new CrowdfundingActivityDetail()));
        assertTrue(crowdfundingActivityDetailService.updateCurrentCount(new CrowdfundingActivityDetail()));
        Mockito.verify(crowdfundingActivityDetailMapper, Mockito.times(2)).updateCurrentCount(Mockito.any(CrowdfundingActivityDetail.class));
    }
    

}
