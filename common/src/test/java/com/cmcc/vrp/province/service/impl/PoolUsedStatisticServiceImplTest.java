package com.cmcc.vrp.province.service.impl;



import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import com.cmcc.vrp.province.dao.PoolUsedStatisticMapper;
import com.cmcc.vrp.province.model.PoolUsedStatistic;
import com.cmcc.vrp.province.service.PoolUsedStatisticService;

/**
 * 测试PoolUsedStatisticServiceImpl
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class PoolUsedStatisticServiceImplTest {
    
    @InjectMocks
    PoolUsedStatisticService poolUsedStatisticService = new PoolUsedStatisticServiceImpl();
    
    @Mock
    PoolUsedStatisticMapper poolUsedStatisticMapper;
   
    /**
     * testGetById
     */
    @Test
    public void testGetById() {
        PoolUsedStatistic statistic = initRecord();
        when(poolUsedStatisticMapper.selectByPrimaryKey(statistic.getId())).thenReturn(statistic);
        Assert.assertEquals(poolUsedStatisticService.getById(statistic.getId()), statistic);
    }
    
    /**
     * testDeleteByPrimaryKey
     */
    @Test
    public void testDeleteByPrimaryKey() {
        when(poolUsedStatisticMapper.deleteByPrimaryKey(1L)).thenReturn(1);
        Assert.assertTrue(poolUsedStatisticService.deleteByPrimaryKey(1L));
        
        when(poolUsedStatisticMapper.deleteByPrimaryKey(1L)).thenReturn(0);
        Assert.assertFalse(poolUsedStatisticService.deleteByPrimaryKey(1L));
    }
    
    /**
     * testInsert
     */
    @Test
    public void testInsert() {
        PoolUsedStatistic statistic = initRecord();
        when(poolUsedStatisticMapper.insertSelective(statistic)).thenReturn(1);
        Assert.assertTrue(poolUsedStatisticService.insertSelective(statistic));
        
        when(poolUsedStatisticMapper.insertSelective(statistic)).thenReturn(0);
        Assert.assertFalse(poolUsedStatisticService.insertSelective(statistic));
    }
    
    /**
     * testUpdateByPrimaryKeySelective
     */
    @Test
    public void testUpdateByPrimaryKeySelective() {
        PoolUsedStatistic statistic = initRecord();
        when(poolUsedStatisticMapper.updateByPrimaryKeySelective(statistic)).thenReturn(1);
        Assert.assertTrue(poolUsedStatisticService.updateByPrimaryKeySelective(statistic));
        
        when(poolUsedStatisticMapper.updateByPrimaryKeySelective(statistic)).thenReturn(0);
        Assert.assertFalse(poolUsedStatisticService.updateByPrimaryKeySelective(statistic));
    }
    
    /**
     * testGetStatisticByTime
     */
    @Test
    public void testGetStatisticByTime() {
        PoolUsedStatistic statistic = initRecord();
        List<PoolUsedStatistic> list = new ArrayList<PoolUsedStatistic>();
        list.add(statistic);
        
        when(poolUsedStatisticMapper.getStatisticByTime(Mockito.anyString(), Mockito.anyString())).thenReturn(list);
        Assert.assertEquals(poolUsedStatisticService.getStatisticByTime("20161201", "20161202"),list);    
    }
    
    /**
     * testDeleteByDate
     */
    @Test
    public void testDeleteByDate() {
        when(poolUsedStatisticMapper.deleteByDate(Mockito.anyString())).thenReturn(1);
        Assert.assertTrue(poolUsedStatisticService.deleteByDate("20161201"));  
        
        when(poolUsedStatisticMapper.deleteByDate(Mockito.anyString())).thenReturn(0);
        Assert.assertFalse(poolUsedStatisticService.deleteByDate("20161201"));  
    }
    
    /**
     * testGetUsedFlowByTime
     */
    @Test
    public void testGetUsedFlowByTime() {
        PoolUsedStatistic statistic = initRecord();
        List<PoolUsedStatistic> list = new ArrayList<PoolUsedStatistic>();
        list.add(statistic);
        
        when(poolUsedStatisticMapper.getStatisticByTime(Mockito.anyString(), Mockito.anyString())).thenReturn(list);
        
        Assert.assertEquals(poolUsedStatisticService.getUsedFlowByTime("20161201", "20161202").size(), 1);
    }
    
    
    private PoolUsedStatistic initRecord(){
        PoolUsedStatistic poolUsedStatistic = new PoolUsedStatistic();
        poolUsedStatistic.setId(1L);
        poolUsedStatistic.setDate("20161203");
        poolUsedStatistic.setEnterCode("11111");
        poolUsedStatistic.setMobile("13900000000");
        poolUsedStatistic.setProductCode("109901");
        poolUsedStatistic.setUsedAmount(1024L);
        
        return poolUsedStatistic;
    }

}
