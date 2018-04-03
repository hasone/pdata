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

import com.cmcc.vrp.province.dao.SupplierProdSuccessUsePerDayMapper;
import com.cmcc.vrp.province.model.SupplierProdSuccessUsePerDay;
import com.cmcc.vrp.province.service.SupplierProdSuccessUsePerDayService;
@RunWith(MockitoJUnitRunner.class)
public class SupplierProdSuccessUsePerDayServiceImplTest {
    @InjectMocks
    SupplierProdSuccessUsePerDayService supplierProdSuccessUsePerDayService =
    new SupplierProdSuccessUsePerDayServiceImpl();
    @Mock
    SupplierProdSuccessUsePerDayMapper mapper;
    
    @Test
    public void testSelectByMap(){
        Mockito.when(mapper.selectByMap(Mockito.anyMap())).thenReturn(new ArrayList<SupplierProdSuccessUsePerDay>());
        assertNotNull(supplierProdSuccessUsePerDayService.selectByMap(new HashMap<String, String>()));
    }

}
