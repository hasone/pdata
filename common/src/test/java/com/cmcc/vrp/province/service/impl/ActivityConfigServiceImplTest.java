package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.cache.CacheService;
import com.cmcc.vrp.province.dao.ActivityConfigMapper;
import com.cmcc.vrp.province.model.ActivityConfig;
import com.cmcc.vrp.province.service.ActivityConfigService;

@RunWith(MockitoJUnitRunner.class)
public class ActivityConfigServiceImplTest {
    @InjectMocks
    ActivityConfigService activityConfigService = new ActivityConfigServiceImpl();
    @Mock
    ActivityConfigMapper mapper;
    @Mock
    CacheService cacheService;
    
    private ActivityConfig createActivityConfig(){
        ActivityConfig record = new ActivityConfig();
        record.setId(1L);
        record.setIsp("A");
        record.setProvince("全国");
        return record;
    }
    
    @Test
    public void testInsert(){
        Mockito.when(mapper.insert(Mockito.any(ActivityConfig.class))).thenReturn(0).thenReturn(1);
        assertFalse(activityConfigService.insert(createActivityConfig()));
        
        Mockito.when(cacheService.add(Mockito.anyString(), Mockito.anyString())).thenReturn(false).thenReturn(false);
        assertTrue(activityConfigService.insert(createActivityConfig()));
        
        assertFalse(activityConfigService.insert(null));
    }
    
    @Test
    public void testUpdateByPrimaryKeySelective(){
        Mockito.when(mapper.updateByPrimaryKeySelective(Mockito.any(ActivityConfig.class)))
        .thenReturn(0).thenReturn(1);
        assertFalse(activityConfigService.updateByPrimaryKeySelective(createActivityConfig()));
        
        Mockito.when(cacheService.update(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(false).thenReturn(false);
        assertTrue(activityConfigService.updateByPrimaryKeySelective(createActivityConfig()));
        
        assertFalse(activityConfigService.updateByPrimaryKeySelective(null));
    }
    
    @Test
    public void testGetPrefix(){
    }
    
    @Test
    public void testGetActivityConfig(){
        List<ActivityConfig> list = new ArrayList<ActivityConfig>();
        list.add(createActivityConfig());
        Mockito.when(mapper.getActivityConfig()).thenReturn(list).thenReturn(null);
        assertNotNull(activityConfigService.getActivityConfig());
        assertNull(activityConfigService.getActivityConfig());
    }
    
    @Test
    public void testGetFromCache(){
        Mockito.when(cacheService.get(Mockito.anyString())).thenReturn("A").thenReturn(null);
        assertNotNull(activityConfigService.getFromCache("test"));
        assertNull(activityConfigService.getFromCache("test"));
    }
    
    @Test
    public void testGetIsp(){
        Mockito.when(cacheService.get(Mockito.anyString())).thenReturn("A").thenReturn(null);
        assertNotNull(activityConfigService.getIsp("test"));
        
        List<ActivityConfig> list = new ArrayList<ActivityConfig>();
        list.add(createActivityConfig());
        Mockito.when(mapper.getActivityConfig()).thenReturn(list);
        assertNotNull(activityConfigService.getIsp("test"));
    }
    
    @Test
    public void testGetProvince(){
        Mockito.when(cacheService.get(Mockito.anyString())).thenReturn("全国").thenReturn(null);
        assertNotNull(activityConfigService.getProvince("test"));
        
        List<ActivityConfig> list = new ArrayList<ActivityConfig>();
        list.add(createActivityConfig());
        Mockito.when(mapper.getActivityConfig()).thenReturn(list);
        assertNotNull(activityConfigService.getProvince("test"));
    }
    
    

}
