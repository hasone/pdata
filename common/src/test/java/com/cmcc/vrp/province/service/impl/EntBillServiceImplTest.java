package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.ChargeRecordMapper;
import com.cmcc.vrp.province.service.EntBillService;
import com.cmcc.vrp.util.QueryObject;

@RunWith(MockitoJUnitRunner.class)
public class EntBillServiceImplTest {
    @InjectMocks
    EntBillService entBillService = new EntBillServiceImpl();
    
    @Mock
    ChargeRecordMapper chargeRecordMapper;
    
    @Test
    public void showPageList() {
        Mockito.when(chargeRecordMapper.selectEntBillRecordByMap(Mockito.anyMap())).thenReturn(new ArrayList());
        assertNotNull(entBillService.showPageList(new QueryObject()));
        Mockito.verify(chargeRecordMapper,times(1)).selectEntBillRecordByMap(Mockito.anyMap());
    }
    
    @Test
    public void showPageCount() {
        Mockito.when(chargeRecordMapper.countEntBillRecordByMap(Mockito.anyMap())).thenReturn(Mockito.anyInt());
        assertNotNull(entBillService.showPageCount(new QueryObject()));
        Mockito.verify(chargeRecordMapper,Mockito.times(1)).countEntBillRecordByMap(Mockito.anyMap());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void sumEntBillPriceByMap() {
        Mockito.when(chargeRecordMapper.sumEntBillPriceByMap(Mockito.anyMap())).thenReturn(null);
        assertEquals(entBillService.sumEntBillPriceByMap(new HashMap()).doubleValue(),0, 0);
        Mockito.when(chargeRecordMapper.sumEntBillPriceByMap(Mockito.anyMap())).thenReturn(100L);
        assertEquals(entBillService.sumEntBillPriceByMap(new HashMap()).doubleValue(),1, 0);
        Mockito.verify(chargeRecordMapper,Mockito.times(2)).sumEntBillPriceByMap(Mockito.anyMap());
    }
    
}
