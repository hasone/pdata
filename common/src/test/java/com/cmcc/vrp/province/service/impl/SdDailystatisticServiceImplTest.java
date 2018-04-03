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

import com.cmcc.vrp.province.dao.SdDailystatisticMapper;
import com.cmcc.vrp.province.model.SdDailystatistic;
import com.cmcc.vrp.province.service.SdDailystatisticService;

/**
 * 
 * SdDailystatisticServiceImplTest
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SdDailystatisticServiceImplTest {

    @InjectMocks
    SdDailystatisticService sdDailystatisticService = new SdDailystatisticServiceImpl();
    
    @Mock
    SdDailystatisticMapper sdDailystatisticMapper;
    
    /**
     * testInsert()
     */
    @Test
    public void testInsert() {
        SdDailystatistic dailystatistic = new SdDailystatistic();
        dailystatistic.setDate("20161117");
        
        Mockito.when(sdDailystatisticMapper.insertSelective(dailystatistic)).thenReturn(1);
        Assert.assertTrue(sdDailystatisticService.insert(dailystatistic));
        
        Mockito.when(sdDailystatisticMapper.insertSelective(dailystatistic)).thenReturn(0);
        Assert.assertFalse(sdDailystatisticService.insert(dailystatistic));
        
        Assert.assertFalse(sdDailystatisticService.insert(null));    
    }
    
    /**
     * testUpdate()
     */
    @Test
    public void testUpdate(){
        SdDailystatistic dailystatistic = new SdDailystatistic();
        dailystatistic.setDate("20161117");
        dailystatistic.setId(1L);
        
        Mockito.when(sdDailystatisticMapper.updateByPrimaryKeySelective(dailystatistic))
            .thenReturn(1);
        
        Assert.assertTrue(sdDailystatisticService.update(dailystatistic));
        
        Mockito.when(sdDailystatisticMapper.updateByPrimaryKeySelective(dailystatistic))
            .thenReturn(0);
        Assert.assertFalse(sdDailystatisticService.update(dailystatistic));
        
        dailystatistic.setId(null);
        Assert.assertFalse(sdDailystatisticService.update(dailystatistic));   
        Assert.assertFalse(sdDailystatisticService.update(null));
    }
    
    /**
     * testSelectByDate()
     */
    @Test
    public void testSelectByDate(){
        String date = "20161117";
        
        SdDailystatistic dailystatistic = new SdDailystatistic();
        dailystatistic.setDate(date);
        dailystatistic.setId(1L);
        
        List<SdDailystatistic> list = new ArrayList<SdDailystatistic>();
        list.add(dailystatistic);
        
        Mockito.when(sdDailystatisticMapper.selectByDate(date)).thenReturn(list);
        
        Assert.assertEquals(sdDailystatisticService.selectByDate(date).size(), list.size());
    }
    
    /**
     * seleceByKey()
     */
    @Test
    public void seleceByKey(){
        Long id = 1L;
        
        SdDailystatistic dailystatistic = new SdDailystatistic();
        dailystatistic.setDate("20161117");
        dailystatistic.setId(1L);
        
        Mockito.when(sdDailystatisticMapper.selectByPrimaryKey(id)).thenReturn(dailystatistic);
        Assert.assertEquals(sdDailystatisticService.seleceByKey(id), dailystatistic);
        
    }
    

}
