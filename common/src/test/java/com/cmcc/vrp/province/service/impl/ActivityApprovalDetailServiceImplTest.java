package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.ActivityApprovalDetailMapper;
import com.cmcc.vrp.province.model.ActivityApprovalDetail;
import com.cmcc.vrp.province.service.ActivityApprovalDetailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;

/**
 * Created by qinqinyan on 2016/10/28.
 */
@RunWith(MockitoJUnitRunner.class)
public class ActivityApprovalDetailServiceImplTest {
    @InjectMocks
    ActivityApprovalDetailService activityApprovalDetailService =
            new ActivityApprovalDetailServiceImpl();
    @Mock
    ActivityApprovalDetailMapper activityApprovalDetailMapper;

    @Test
    public void testInsert(){
        //invalid
        assertFalse(activityApprovalDetailService.insert(null));

        //valid
        Mockito.when(activityApprovalDetailMapper.insert(any(ActivityApprovalDetail.class))).thenReturn(1);
        assertTrue(activityApprovalDetailService.insert(new ActivityApprovalDetail()));
        Mockito.verify(activityApprovalDetailMapper).insert(any(ActivityApprovalDetail.class));
    }

    @Test
    public void testSelectByRequestId(){
        //invalid
        assertNull(activityApprovalDetailService.selectByRequestId(null));

        //valid
        Mockito.when(activityApprovalDetailMapper.selectByRequestId(anyLong())).thenReturn(new ActivityApprovalDetail());
        assertNotNull(activityApprovalDetailService.selectByRequestId(anyLong()));
        Mockito.verify(activityApprovalDetailMapper).selectByRequestId(anyLong());
    }

    @Test
    public void testSelectByActivityId(){
        //invalid
        assertNull(activityApprovalDetailService.selectByActivityId(null));
        //init
        String activitId = "test";
        //valid
        Mockito.when(activityApprovalDetailMapper.selectByActivityId(anyString())).thenReturn(new ArrayList<ActivityApprovalDetail>());
        assertNotNull(activityApprovalDetailService.selectByActivityId(activitId));
        Mockito.verify(activityApprovalDetailMapper).selectByActivityId(anyString());
    }
}
