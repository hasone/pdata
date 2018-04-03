package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.IndividualProductMapMapper;
import com.cmcc.vrp.province.model.IndividualProductMap;
import com.cmcc.vrp.province.service.IndividualProductMapService;

/**
 * @author wujiamin
 * @date 2016年10月27日
 */
@RunWith(MockitoJUnitRunner.class)
public class IndividualProductMapServiceImplTest {
    @InjectMocks
    IndividualProductMapService service = new IndividualProductMapServiceImpl();
    
    @Mock
    IndividualProductMapMapper mapper;
    
    @Test
    public void testInsert() {
        when(mapper.insert(Mockito.any(IndividualProductMap.class))).thenReturn(0,1);
        assertFalse(service.insert(new IndividualProductMap()));
        assertTrue(service.insert(new IndividualProductMap()));
        verify(mapper,times(2)).insert(Mockito.any(IndividualProductMap.class));        
    }
    
    @Test
    public void testBatchInsert() {
        when(mapper.batchInsert(Mockito.anyList())).thenReturn(0,1);
        List<IndividualProductMap> records = new ArrayList();
        records.add(new IndividualProductMap());
        assertFalse(service.batchInsert(records));
        assertTrue(service.batchInsert(records));
        verify(mapper,times(2)).batchInsert(Mockito.anyList());        
    }
    
    @Test
    public void testGetByAdminIdAndProductId() {
        assertNull(service.getByAdminIdAndProductId(null, 1L));
        assertNull(service.getByAdminIdAndProductId(1L, null));
       
        when(mapper.getByAdminIdAndProductId(1L, 1L)).thenReturn(new IndividualProductMap());
        
        assertNotNull(service.getByAdminIdAndProductId(1L, 1L));
        verify(mapper,times(1)).getByAdminIdAndProductId(1L, 1L);        
    }
    
    @Test
    public void testGetByAdminIdAndProductType() {
        assertNull(service.getByAdminIdAndProductType(null, 1));
        assertNull(service.getByAdminIdAndProductType(1L, null));
       
        when(mapper.getByAdminIdAndProductType(1L, 1)).thenReturn(new IndividualProductMap());
        
        assertNotNull(service.getByAdminIdAndProductType(1L, 1));
        verify(mapper,times(1)).getByAdminIdAndProductType(1L, 1);        
    }

    
}
