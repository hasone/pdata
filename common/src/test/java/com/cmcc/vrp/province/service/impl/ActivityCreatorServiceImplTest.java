package com.cmcc.vrp.province.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.province.dao.ActivityCreatorMapper;
import com.cmcc.vrp.province.model.ActivityCreator;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.service.ActivityCreatorService;
import com.cmcc.vrp.province.service.AdministerService;

/**
 * 
 * @author Administrator
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ActivityCreatorServiceImplTest {
    @InjectMocks
    ActivityCreatorService activityCreatorService = new ActivityCreatorServiceImpl();
    
    @Mock
    AdministerService administerService;
    
    @Mock
    ActivityCreatorMapper activityCreatorMapper;
    
    /**
     * 
     */
    @Test
    public void testInsertSelective(){
        Mockito.when(activityCreatorMapper.insertSelective(Mockito.any(ActivityCreator.class))).thenReturn(1);
        Assert.assertNotNull(activityCreatorService.insertSelective(new ActivityCreator()));
        
        Mockito.when(activityCreatorMapper.insertSelective(Mockito.any(ActivityCreator.class))).thenReturn(0);
        Assert.assertNotNull(activityCreatorService.insertSelective(new ActivityCreator()));
    }
    
    /**
     * 
     */
    @Test
    public void testinsert(){
        Mockito.when(activityCreatorMapper.insertSelective(Mockito.any(ActivityCreator.class))).thenReturn(1);
        
        Administer admin = new Administer();
        admin.setId(1L);
        admin.setUserName("aaa");
        admin.setMobilePhone("18867101000");
        
        Mockito.when(administerService.selectAdministerById(Mockito.anyLong())).thenReturn(admin);
        Assert.assertNotNull(activityCreatorService.insert(ActivityType.QRCODE,1L,1L));
        
        Mockito.when(administerService.selectAdministerById(Mockito.anyLong())).thenReturn(null);
        Assert.assertNotNull(activityCreatorService.insert(ActivityType.QRCODE,1L,1L));
        
        Assert.assertNotNull(activityCreatorService.insert(ActivityType.QRCODE,null,1L));
        Assert.assertNotNull(activityCreatorService.insert(null,1L,1L));
    }
    
    /**
     * 
     */
    @Test
    public void testGetByActId(){
        List<ActivityCreator> list = new ArrayList<ActivityCreator>();
        list.add(new ActivityCreator());
        Mockito.when(activityCreatorMapper.selectByActId(Mockito.anyLong(),Mockito.anyInt())).thenReturn(list);
        
        Assert.assertNotNull(activityCreatorService.getByActId(ActivityType.QRCODE, 1L));
        
        list.clear();
        Mockito.when(activityCreatorMapper.selectByActId(Mockito.anyLong(),Mockito.anyInt())).thenReturn(list);
        Assert.assertNull(activityCreatorService.getByActId(ActivityType.QRCODE, 1L));
        
        Assert.assertNull(activityCreatorService.getByActId(ActivityType.QRCODE, null));
        Assert.assertNull(activityCreatorService.getByActId(null, 1L));
        
    }
}
