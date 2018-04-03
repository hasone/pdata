package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import com.cmcc.vrp.province.dao.IndividualAccountRecordMapper;
import com.cmcc.vrp.province.model.IndividualAccountRecord;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.service.IndividualAccountRecordService;
import com.cmcc.vrp.province.service.IndividualProductService;

@RunWith(MockitoJUnitRunner.class)
public class IndividualAccountRecordServiceImplTest {
    @InjectMocks
    IndividualAccountRecordService service = new IndividualAccountRecordServiceImpl();

    @Mock
    IndividualAccountRecordMapper mapper;
    @Mock
    IndividualProductService individualProductService;
    
    @Test
    public void testCreate() {
        assertFalse(service.create(null));
        
        IndividualAccountRecord record = new IndividualAccountRecord();
        assertFalse(service.create(record));
        record.setAdminId(1L);
        assertFalse(service.create(record));
        record.setOwnerId(1L);
        assertFalse(service.create(record));
        record.setAccountId(1L);
        assertFalse(service.create(record));
        record.setType(1);
        assertFalse(service.create(record));
        record.setActivityType(1);
        assertFalse(service.create(record));
        record.setSerialNum("1234");

        when(mapper.insert(record)).thenReturn(0);
        assertFalse(service.create(record));
        
        when(mapper.insert(record)).thenReturn(1);
        assertTrue(service.create(record));

        verify(mapper,times(2)).insert(record);
    }
    
    @Test
    public void testUpdateByPrimaryKeySelective() {
        IndividualAccountRecord record = new IndividualAccountRecord();
        when(mapper.updateByPrimaryKeySelective(Mockito.any(IndividualAccountRecord.class))).thenReturn(0,1);

        assertFalse(service.updateByPrimaryKeySelective(record));
        assertTrue(service.updateByPrimaryKeySelective(record));

        verify(mapper,times(2)).updateByPrimaryKeySelective(record);
    }

    @Test
    public void testSelectByMap() {
        when(mapper.selectByMap(Mockito.anyMap())).thenReturn(new ArrayList());

        assertNotNull(service.selectByMap(new HashMap()));
      
        verify(mapper,times(1)).selectByMap(Mockito.anyMap());
    }
    @Test
    public void testSelectAccumulateAccount() {
        Mockito.when(mapper.selectAccumulateAccount(Mockito.anyLong())).thenReturn(null);
        Assert.assertNull(service.selectAccumulateAccount(1l));
    }
    @Test
    public void testCountDetailRecordByMap() {
        Map map = new HashMap();
        map.put("mobile", "mobile");
        IndividualProduct individualProduct = new IndividualProduct();
        individualProduct.setId(1l);
        Mockito.when(individualProductService.getIndivialPointProduct()).thenReturn(individualProduct);
        Mockito.when(mapper.countDetailRecordByMap(Mockito.anyMap())).thenReturn(1);
        Assert.assertSame(0, service.countDetailRecordByMap(map)-1);
    }
    @Test
    public void testSelectDetailRecordByMap() {
        Map map = new HashMap();
        map.put("mobile", "mobile");
        IndividualProduct individualProduct = new IndividualProduct();
        individualProduct.setId(1l);
        Mockito.when(individualProductService.getIndivialPointProduct()).thenReturn(individualProduct);
        Mockito.when(mapper.selectDetailRecordByMap(Mockito.anyMap())).thenReturn(new ArrayList<IndividualAccountRecord>());
        Assert.assertNotNull(service.selectDetailRecordByMap(map));
    }
}
