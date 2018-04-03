package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.EntManagerMapper;
import com.cmcc.vrp.province.model.EntManager;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.EntManagerService;

@RunWith(MockitoJUnitRunner.class)
public class EntManagerServiceImplTest {
    @InjectMocks
    EntManagerService entManagerService = new EntManagerServiceImpl();

    @Mock
    EntManagerMapper mapper;
    
    @Test
    public void testInsertEntManager(){
        when(mapper.insert(Mockito.any(EntManager.class))).thenReturn(1);
        assertTrue(entManagerService.insertEntManager(new EntManager()));
        
        when(mapper.insert(Mockito.any(EntManager.class))).thenReturn(0);
        assertFalse(entManagerService.insertEntManager(new EntManager()));
        
        verify(mapper,times(2)).insert(Mockito.any(EntManager.class));
    }
    
    @Test
    public void testGetManagerIdForEnter(){
        when(mapper.getManagerIdForEnter(1L)).thenReturn(1L);
        assertEquals(entManagerService.getManagerIdForEnter(1L).intValue(),1);
        verify(mapper,times(1)).getManagerIdForEnter(1L);
    }
    
    @Test
    public void testGetManagerForEnter(){
        when(mapper.getManagerForEnter(1L)).thenReturn(new Manager());
        assertNotNull(entManagerService.getManagerForEnter(1L));
        verify(mapper,times(1)).getManagerForEnter(1L);
    }
    
    @Test
    public void testGetManagerForEnterCode(){
        when(mapper.getManagerForEnterCode(Mockito.anyString())).thenReturn(new Manager());
        assertNotNull(entManagerService.getManagerForEnterCode("123"));
        verify(mapper,times(1)).getManagerForEnterCode("123");
    }
    @Test
    public void testSelectByManagerId() {
        Mockito.when(mapper.selectByManagerId(Mockito.anyLong())).thenReturn(new ArrayList<EntManager>());
        Assert.assertNotNull(entManagerService.selectByManagerId(1l));
    }
}
