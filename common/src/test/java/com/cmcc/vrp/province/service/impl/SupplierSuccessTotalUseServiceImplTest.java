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

import com.cmcc.vrp.province.dao.SupplierSuccessTotalUseMapper;
import com.cmcc.vrp.province.model.SupplierSuccessTotalUse;
import com.cmcc.vrp.province.service.SupplierSuccessTotalUseService;

/**
 * @author lgk8023
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SupplierSuccessTotalUseServiceImplTest {
    @InjectMocks
    SupplierSuccessTotalUseService supplierSuccessTotalUseService =
        new SupplierSuccessTotalUseServiceImpl();
    @Mock
    SupplierSuccessTotalUseMapper mapper;
    
    /**
     * 
     */
    @Test
    public void testSelectByMap(){
        Mockito.when(mapper.selectByMap(Mockito.anyMap())).thenReturn(new ArrayList<SupplierSuccessTotalUse>());
        assertNotNull(supplierSuccessTotalUseService.selectByMap(new HashMap<String, String>()));
    }
    
    /**
     * 
     */
    @Test
    public void testSelectBySupplierId(){
        Mockito.when(mapper.selectBySupplierId(Mockito.anyLong())).thenReturn(new ArrayList<SupplierSuccessTotalUse>());
        assertNotNull(supplierSuccessTotalUseService.selectBySupplierId(1L));
    }
    
    /**
     * 
     */
    @Test
    public void testGetAllSupplierSuccessTotalUseRecords() {
        Mockito.when(mapper.getAllSupplierSuccessTotalUseRecords()).thenReturn(new ArrayList<SupplierSuccessTotalUse>());
        assertNotNull(supplierSuccessTotalUseService.getAllSupplierSuccessTotalUseRecords());
    }

}
