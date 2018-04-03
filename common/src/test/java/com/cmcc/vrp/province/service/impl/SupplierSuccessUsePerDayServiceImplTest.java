package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.SupplierSuccessUsePerDayMapper;
import com.cmcc.vrp.province.model.SupplierSuccessUsePerDay;
import com.cmcc.vrp.province.service.SupplierSuccessUsePerDayService;

@RunWith(MockitoJUnitRunner.class)
public class SupplierSuccessUsePerDayServiceImplTest {
    @InjectMocks
    SupplierSuccessUsePerDayService supplierSuccessUsePerDayService
    = new SupplierSuccessUsePerDayServiceImpl();
    
    @Mock
    SupplierSuccessUsePerDayMapper mapper;
    
    @Test
    public void testSelectByMap(){
        Mockito.when(mapper.selectByMap(Mockito.anyMap())).thenReturn(new ArrayList<SupplierSuccessUsePerDay>());
        assertNotNull(supplierSuccessUsePerDayService.selectByMap(new HashMap()));
    }
}
