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

import com.cmcc.vrp.province.dao.PlatformProductTemplateMapMapper;
import com.cmcc.vrp.province.model.PlatformProductTemplateMap;
import com.cmcc.vrp.province.service.PlatformProductTemplateMapService;

@RunWith(MockitoJUnitRunner.class)
public class PlatformProductTemplateMapServiceImplTest {
    
    @InjectMocks
    PlatformProductTemplateMapService platformProductTemplateMapService = 
            new PlatformProductTemplateMapServiceImpl();
    @Mock
    PlatformProductTemplateMapMapper mapper;
    
    @Test
    public void testDeleteByPrimaryKey(){
        assertFalse(platformProductTemplateMapService.deleteByPrimaryKey(null));
        Mockito.when(mapper.deleteByPrimaryKey(Mockito.anyLong())).thenReturn(1);
        assertTrue(platformProductTemplateMapService.deleteByPrimaryKey(1L));
        Mockito.verify(mapper).deleteByPrimaryKey(Mockito.anyLong());
    }
    
    @Test
    public void testInsert(){
        assertFalse(platformProductTemplateMapService.insert(null));
        Mockito.when(mapper.insert(Mockito.any(PlatformProductTemplateMap.class))).thenReturn(1);
        assertTrue(platformProductTemplateMapService.insert(new PlatformProductTemplateMap()));
        Mockito.verify(mapper).insert(Mockito.any(PlatformProductTemplateMap.class));
    }
    
    @Test
    public void testInsertSelective(){
        assertFalse(platformProductTemplateMapService.insertSelective(null));
        Mockito.when(mapper.insertSelective(Mockito.any(PlatformProductTemplateMap.class))).thenReturn(1);
        assertTrue(platformProductTemplateMapService.insertSelective(new PlatformProductTemplateMap()));
        Mockito.verify(mapper).insertSelective(Mockito.any(PlatformProductTemplateMap.class));
    }
    
    @Test
    public void testUpdateByPrimaryKeySelective(){
        assertFalse(platformProductTemplateMapService.updateByPrimaryKeySelective(null));
        Mockito.when(mapper.updateByPrimaryKeySelective(Mockito.any(PlatformProductTemplateMap.class))).thenReturn(1);
        assertTrue(platformProductTemplateMapService.updateByPrimaryKeySelective(new PlatformProductTemplateMap()));
        Mockito.verify(mapper).updateByPrimaryKeySelective(Mockito.any(PlatformProductTemplateMap.class));
    }
    
    @Test
    public void testUpdateByPrimaryKey(){
        assertFalse(platformProductTemplateMapService.updateByPrimaryKey(null));
        Mockito.when(mapper.updateByPrimaryKey(Mockito.any(PlatformProductTemplateMap.class))).thenReturn(1);
        assertTrue(platformProductTemplateMapService.updateByPrimaryKey(new PlatformProductTemplateMap()));
        Mockito.verify(mapper).updateByPrimaryKey(Mockito.any(PlatformProductTemplateMap.class));
    }
    
    @Test
    public void testSelectByPrimaryKey(){
        assertNull(platformProductTemplateMapService.selectByPrimaryKey(null));
        Mockito.when(mapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(new PlatformProductTemplateMap());
        assertNotNull(platformProductTemplateMapService.selectByPrimaryKey(1L));
        Mockito.verify(mapper).selectByPrimaryKey(Mockito.anyLong());
    }
    
    @Test
    public void testBatchDelete(){
        assertFalse(platformProductTemplateMapService.batchDelete(null));
        
        List<PlatformProductTemplateMap> records = new ArrayList<PlatformProductTemplateMap>();
        PlatformProductTemplateMap record = new PlatformProductTemplateMap();
        records.add(record);
        Mockito.when(mapper.batchDelete(Mockito.anyList())).thenReturn(1);
        assertTrue(platformProductTemplateMapService.batchDelete(records));
    }
    
    @Test
    public void testBatchInsert(){
        assertFalse(platformProductTemplateMapService.batchInsert(null));
        
        List<PlatformProductTemplateMap> records = new ArrayList<PlatformProductTemplateMap>();
        PlatformProductTemplateMap record = new PlatformProductTemplateMap();
        records.add(record);
        Mockito.when(mapper.batchInsert(Mockito.anyList())).thenReturn(1);
        assertTrue(platformProductTemplateMapService.batchInsert(records));
    }
    
    @Test
    public void testSelectByTemplateId(){
        assertNull(platformProductTemplateMapService.selectByTemplateId(null));
        
        Mockito.when(mapper.selectByTemplateId(Mockito.anyLong())).thenReturn(new ArrayList<PlatformProductTemplateMap>());
        assertNotNull(platformProductTemplateMapService.selectByTemplateId(1L));
    }
   

}
