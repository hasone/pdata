package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.cmcc.vrp.province.dao.ActivityTemplateMapper;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityTemplate;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityTemplateService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.HttpConnection;

/**
 * Created by qinqinyan on 2016/11/16.
 */
@PrepareForTest({HttpConnection.class})
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
public class ActivityTemplateServiceImplTest {

    @InjectMocks
    ActivityTemplateService activityTemplateService =
        new ActivityTemplateServiceImpl();
    @Mock
    ActivityTemplateMapper activityTemplateMapper;
    @Mock
    GlobalConfigService globalConfigService;
    @Mock
    ActivityInfoService activityInfoService;

    @Test
    public void testNotifyTemplateToClose(){
        assertFalse(activityTemplateService.notifyTemplateToClose(null, null));

        String activityUrl = "http://activitytest.4ggogo.com/lottery/game/test/index.html";
        Mockito.when(globalConfigService.get("ACTIVITY_OFFLINE_URL"))
            .thenReturn("http://activitytest.4ggogo.com/template/close/index.html");
        PowerMockito.mockStatic(HttpConnection.class);
        PowerMockito.when(HttpConnection.doPost(anyString(), anyString(),
            anyString(), anyString(), anyBoolean()))
            .thenReturn("{\"code\":0,\"message\":\"test\"}");

        ActivityInfo info = new ActivityInfo();
        
        PowerMockito.when(activityInfoService.selectByActivityId(Mockito.anyString())).thenReturn(info);
        PowerMockito.when(activityInfoService.updateByPrimaryKeySelective(Mockito.any(ActivityInfo.class))).thenReturn(false);
        assertTrue(activityTemplateService.notifyTemplateToClose(activityUrl, "test"));
        
        PowerMockito.when(activityInfoService.updateByPrimaryKeySelective(Mockito.any(ActivityInfo.class))).thenReturn(true);
        assertTrue(activityTemplateService.notifyTemplateToClose(activityUrl, "test"));
    }
    
    @Test
    public void testGetDataFromTemplate(){
        assertNull(activityTemplateService.getDataFromTemplate(null));

        String activityUrl = "http://activitytest.4ggogo.com/lottery/game/test/index.html";
        Mockito.when(globalConfigService.get("ACTIVITY_GET_DATA_URL"))
            .thenReturn("http://activitytest.4ggogo.com/template/close/index.html");
        PowerMockito.mockStatic(HttpConnection.class);
        PowerMockito.when(HttpConnection.doPost(anyString(), anyString(),
            anyString(), anyString(), anyBoolean()))
            .thenReturn("{\"visitCount\":0,\"playCount\":2,\"winCount\":1, \"msg\": \"test\"}");

        assertNotNull(activityTemplateService.getDataFromTemplate(activityUrl));
    }

    @Test
    public void testDeleteByPrimaryKey(){
        Long id = 1L;
        assertFalse(activityTemplateService.deleteByPrimaryKey(null));

        Mockito.when(activityTemplateMapper.deleteByPrimaryKey(anyLong())).thenReturn(1);
        assertTrue(activityTemplateService.deleteByPrimaryKey(id));
        Mockito.verify(activityTemplateMapper).deleteByPrimaryKey(anyLong());
    }

    @Test
    public void testInsert(){
        assertFalse(activityTemplateService.insert(null));

        Mockito.when(activityTemplateMapper.insert(any(ActivityTemplate.class))).thenReturn(1);
        assertTrue(activityTemplateService.insert(new ActivityTemplate()));
        Mockito.verify(activityTemplateMapper).insert(any(ActivityTemplate.class));
    }

    @Test
    public void testInsertSelective(){
        assertFalse(activityTemplateService.insertSelective(null));

        Mockito.when(activityTemplateMapper.insertSelective(any(ActivityTemplate.class))).thenReturn(1);
        assertTrue(activityTemplateService.insertSelective(new ActivityTemplate()));
        Mockito.verify(activityTemplateMapper).insertSelective(any(ActivityTemplate.class));
    }

    @Test
    public void testSelectByPrimaryKey(){
        assertNull(activityTemplateService.selectByPrimaryKey(null));

        Long id = 1L;
        Mockito.when(activityTemplateMapper.selectByPrimaryKey(anyLong())).thenReturn(new ActivityTemplate());
        assertNotNull(activityTemplateService.selectByPrimaryKey(id));
        Mockito.verify(activityTemplateMapper).selectByPrimaryKey(anyLong());
    }

    @Test
    public void testUpdateByPrimaryKeySelective(){
        assertFalse(activityTemplateService.updateByPrimaryKeySelective(null));

        Mockito.when(activityTemplateMapper.updateByPrimaryKeySelective(any(ActivityTemplate.class))).thenReturn(1);
        assertTrue(activityTemplateService.updateByPrimaryKeySelective(new ActivityTemplate()));
        Mockito.verify(activityTemplateMapper).updateByPrimaryKeySelective(any(ActivityTemplate.class));
    }

    @Test
    public void testUpdateByPrimaryKey(){
        assertFalse(activityTemplateService.updateByPrimaryKey(null));

        Mockito.when(activityTemplateMapper.updateByPrimaryKey(any(ActivityTemplate.class))).thenReturn(1);
        assertTrue(activityTemplateService.updateByPrimaryKey(new ActivityTemplate()));
        Mockito.verify(activityTemplateMapper).updateByPrimaryKey(any(ActivityTemplate.class));
    }

    @Test
    public void testSelectByActivityId(){
        assertNull(activityTemplateService.selectByActivityId(null));

        String activityId = "test";
        Mockito.when(activityTemplateMapper.selectByActivityId(anyString()))
            .thenReturn(new ActivityTemplate());
        assertNotNull(activityTemplateService.selectByActivityId(activityId));
        Mockito.verify(activityTemplateMapper).selectByActivityId(anyString());
    }

}
