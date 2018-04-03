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

import com.cmcc.vrp.province.dao.SupplierProdSuccessTotalUseMapper;
import com.cmcc.vrp.province.model.SupplierProdSuccessTotalUse;
import com.cmcc.vrp.province.service.SupplierProdSuccessTotalUseService;

@RunWith(MockitoJUnitRunner.class)
public class SupplierProdSuccessTotalUseServiceImplTest {
    @InjectMocks
    SupplierProdSuccessTotalUseService supplierProdSuccessTotalUseService
    = new SupplierProdSuccessTotalUseServiceImpl();
    @Mock
    SupplierProdSuccessTotalUseMapper mapper;
    
    @Test
    public void testSelectByMap(){
        Mockito.when(mapper.selectByMap(Mockito.anyMap())).thenReturn(new ArrayList<SupplierProdSuccessTotalUse>());
        assertNotNull(supplierProdSuccessTotalUseService.selectByMap(new HashMap<String, String>()));
    }

}
