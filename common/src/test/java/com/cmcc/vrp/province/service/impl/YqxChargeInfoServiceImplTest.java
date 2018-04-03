package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.YqxChargeInfoMapper;
import com.cmcc.vrp.province.model.YqxChargeInfo;
import com.cmcc.vrp.province.service.YqxChargeInfoService;

@RunWith(MockitoJUnitRunner.class)
public class YqxChargeInfoServiceImplTest {
    @InjectMocks
    YqxChargeInfoService service = new YqxChargeInfoServiceImpl();
    
    @Mock
    YqxChargeInfoMapper mapper;
    
    @Test
    public void testInsert() {
        Mockito.when(mapper.insert(Mockito.any(YqxChargeInfo.class))).thenReturn(1);
        assertTrue(service.insert(new YqxChargeInfo()));
    }
    
    @Test
    public void testUpdateReturnSystemNum() {
        Mockito.when(mapper.updateReturnSystemNum(Mockito.any(YqxChargeInfo.class))).thenReturn(1);
        assertTrue(service.updateReturnSystemNum(new YqxChargeInfo()));
    }
    
    @Test
    public void testSelectByReturnSystemNum() {
        Mockito.when(mapper.selectByReturnSystemNum(Mockito.anyString())).thenReturn(new ArrayList());
        assertNull(service.selectByReturnSystemNum("test"));
    }
    
    @Test
    public void testSelectBySerialNum() {
        Mockito.when(mapper.selectBySerialNum(Mockito.anyString())).thenReturn(new YqxChargeInfo());
        assertNotNull(service.selectBySerialNum("test"));
    }
}
