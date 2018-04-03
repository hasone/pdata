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

import com.cmcc.vrp.province.dao.ShOrderProductMapMapper;
import com.cmcc.vrp.province.model.ShOrderProductMap;

/**
 * @author lgk8023
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ShOrderProductMapServiceImplTest {
    @InjectMocks
    ShOrderProductMapServiceImpl shOrderProductMapService = new ShOrderProductMapServiceImpl();

    @Mock
    ShOrderProductMapMapper shOrderProductMapMapper;
    
    /**
     * 
     */
    @Test
     public void testInsert() {
        Assert.assertFalse(shOrderProductMapService.insert(null));
        Mockito.when(shOrderProductMapMapper.insert(Mockito.any(ShOrderProductMap.class)))
            .thenReturn(1).thenReturn(0);
        Assert.assertTrue(shOrderProductMapService.insert(new ShOrderProductMap()));
        Assert.assertFalse(shOrderProductMapService.insert(new ShOrderProductMap()));
    }
    
    /**
     * 
     */
    @Test
    public void testGetByOrderListId() {
        Assert.assertNull(shOrderProductMapService.getByOrderListId(null));
        Mockito.when(shOrderProductMapMapper.getByOrderListId(Mockito.anyLong()))
            .thenReturn(new ArrayList<ShOrderProductMap>());
        Assert.assertNotNull(shOrderProductMapService.getByOrderListId(1l));
    }
    
    /**
     * 
     */
    @Test
    public void testBatchInsert() {
        Assert.assertFalse(shOrderProductMapService.batchInsert(null));
        List<ShOrderProductMap> shOrderProductMaps = new ArrayList<ShOrderProductMap>();
        ShOrderProductMap shOrderProductMap = new ShOrderProductMap();
        shOrderProductMap.setId(1l);
        shOrderProductMaps.add(shOrderProductMap);
        Mockito.when(shOrderProductMapMapper.batchInsert(Mockito.anyList()))
            .thenReturn(1).thenReturn(0);
        Assert.assertTrue(shOrderProductMapService.batchInsert(shOrderProductMaps));
        Assert.assertFalse(shOrderProductMapService.batchInsert(shOrderProductMaps));
    }
    
    /**
     * 
     */
    @Test
    public void testDeleteByPrdId() {
        Mockito.when(shOrderProductMapMapper.deleteByPrdId(Mockito.anyLong()))
        .thenReturn(1).thenReturn(0);
        Assert.assertTrue(shOrderProductMapService.deleteByPrdId(1l));
        Assert.assertFalse(shOrderProductMapService.deleteByPrdId(1l));
    }
}
