package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.enums.BlackAndWhiteListType;
import com.cmcc.vrp.province.dao.ActivityBlackAndWhiteMapper;
import com.cmcc.vrp.province.service.ActivityBlackAndWhiteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;

/**
 * Created by qinqinyan on 2016/10/28.
 */
@RunWith(MockitoJUnitRunner.class)
public class ActivityBlackAndWhiteServiceImplTest {
    @InjectMocks
    ActivityBlackAndWhiteService activityBlackAndWhiteService
            = new ActivityBlackAndWhiteServiceImpl();
    @Mock
    ActivityBlackAndWhiteMapper activityBlackAndWhiteMapper;

    @Test
    public void testSelectPhonesByMap(){
        Mockito.when(activityBlackAndWhiteMapper.selectPhonesByMap(anyMap())).thenReturn(anyList());
        assertNotNull(activityBlackAndWhiteService.selectPhonesByMap(new HashMap()));
        Mockito.verify(activityBlackAndWhiteMapper).selectPhonesByMap(anyMap());
    }

    @Test
    public void testBatchInsert(){
        String activitId = "test";
        Integer isWhite = BlackAndWhiteListType.BLACKLIST.getCode();
        String phones = "18867101234";

        //invalid
        assertFalse(activityBlackAndWhiteService.batchInsert(null, isWhite, phones));
        assertFalse(activityBlackAndWhiteService.batchInsert(activitId, null, phones));
        assertFalse(activityBlackAndWhiteService.batchInsert(activitId, isWhite, null));

        //valid
        Mockito.when(activityBlackAndWhiteMapper.batchInsert(anyList())).thenReturn(1);
        assertTrue(activityBlackAndWhiteService.batchInsert(activitId, isWhite, phones));
        Mockito.verify(activityBlackAndWhiteMapper).batchInsert(anyList());
    }

    @Test
    public void testDeleteByActivityId(){
        //init
        String activityId = "test";
        //invalid
        assertFalse(activityBlackAndWhiteService.deleteByActivityId(null));
        //valid
        Mockito.when(activityBlackAndWhiteMapper.deleteByActivityId(anyString())).thenReturn(1);
        assertTrue(activityBlackAndWhiteService.deleteByActivityId(activityId));
        Mockito.verify(activityBlackAndWhiteMapper).deleteByActivityId(anyString());
    }

    @Test
    public void testUpdateIsWhiteByActivityId(){
        //init
        String activityId = "test";
        Integer isWhite = BlackAndWhiteListType.BLACKLIST.getCode();

        //invalid
        assertFalse(activityBlackAndWhiteService.updateIsWhiteByActivityId(null, isWhite));
        assertFalse(activityBlackAndWhiteService.updateIsWhiteByActivityId(activityId, null));

        //valid
        Mockito.when(activityBlackAndWhiteMapper.updateIsWhiteByActivityId(anyString(), anyInt())).thenReturn(1);
        assertTrue(activityBlackAndWhiteService.updateIsWhiteByActivityId(activityId, isWhite));
        Mockito.verify(activityBlackAndWhiteMapper).updateIsWhiteByActivityId(anyString(), anyInt());
    }

}
