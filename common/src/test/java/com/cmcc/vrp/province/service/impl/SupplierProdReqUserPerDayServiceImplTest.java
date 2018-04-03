package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.SupplierProdReqUsePerDayMapper;
import com.cmcc.vrp.province.model.SupplierProdReqUsePerDay;
import com.cmcc.vrp.province.service.SupplierProdReqUsePerDayService;

/**
 * 
 * @ClassName: SupplierProdReqUserPerDayServiceImplTest 
 * @Description: TODO
 * @author: Rowe
 * @date: 2017年9月21日 下午2:18:40
 */
@RunWith(MockitoJUnitRunner.class)
public class SupplierProdReqUserPerDayServiceImplTest {
    @InjectMocks
    SupplierProdReqUsePerDayService supplierProdReqUsePerDayService = new SupplierProdReqUsePerDayServiceImpl();
    @Mock
    SupplierProdReqUsePerDayMapper supplierProdReqUsePerDayMapper;

    @Test
    public void testInsertSelective() {
        Mockito.when(supplierProdReqUsePerDayMapper.insertSelective(any(SupplierProdReqUsePerDay.class))).thenReturn(1);
        assertTrue(supplierProdReqUsePerDayService.insertSelective(new SupplierProdReqUsePerDay()));
        verify(supplierProdReqUsePerDayMapper,times(1)).insertSelective(Mockito.any(SupplierProdReqUsePerDay.class));

    }

    @Test
    public void testSelectByPrimaryKey() {
        SupplierProdReqUsePerDay s = new SupplierProdReqUsePerDay();
        Mockito.when(supplierProdReqUsePerDayMapper.selectByPrimaryKey(any(Long.class))).thenReturn(s);
        assertEquals(s, supplierProdReqUsePerDayService.selectByPrimaryKey(1L));
        verify(supplierProdReqUsePerDayMapper,times(1)).selectByPrimaryKey(Mockito.any(Long.class));


    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        SupplierProdReqUsePerDay s = new SupplierProdReqUsePerDay();
        Mockito.when(supplierProdReqUsePerDayMapper.updateByPrimaryKeySelective(any(SupplierProdReqUsePerDay.class)))
                .thenReturn(1);
        assertTrue(supplierProdReqUsePerDayService.updateByPrimaryKeySelective(s));
        verify(supplierProdReqUsePerDayMapper,times(1)).updateByPrimaryKeySelective(Mockito.any(SupplierProdReqUsePerDay.class));
    }

    @Test
    public void testDeleteByPrimaryKey() {
        Mockito.when(supplierProdReqUsePerDayMapper.deleteByPrimaryKey(any(Long.class))).thenReturn(1);
        assertTrue(supplierProdReqUsePerDayService.deleteByPrimaryKey(1L));
        verify(supplierProdReqUsePerDayMapper,times(1)).deleteByPrimaryKey(Mockito.any(Long.class));

    }

    @Test
    public void testUpdateUsedMoney() {
        Mockito.when(supplierProdReqUsePerDayMapper.updateUsedMoney(anyLong(), any(Double.class))).thenReturn(1);
        assertTrue(supplierProdReqUsePerDayService.updateUsedMoney(1L, 2.0));
        verify(supplierProdReqUsePerDayMapper,times(1)).updateUsedMoney(anyLong(), any(Double.class));

    }

    @Test
    public void testGetTodayRecord() {
        List<SupplierProdReqUsePerDay> list = new ArrayList<SupplierProdReqUsePerDay>();
        SupplierProdReqUsePerDay s = new SupplierProdReqUsePerDay();
        list.add(s);

        Mockito.when(supplierProdReqUsePerDayMapper.selectByMap(any(Map.class))).thenReturn(list);
        assertEquals(s, supplierProdReqUsePerDayService.getTodayRecord(1L));
        verify(supplierProdReqUsePerDayMapper,times(1)).selectByMap(any(Map.class));

    }

    @Test
    public void testSelectByMap() {
        Map map = new HashMap<Object, Object>();
        List<SupplierProdReqUsePerDay> list = new ArrayList<SupplierProdReqUsePerDay>();
        Mockito.when(supplierProdReqUsePerDayMapper.selectByMap(any(Map.class))).thenReturn(list);
        assertEquals(list, supplierProdReqUsePerDayService.selectByMap(map));
        verify(supplierProdReqUsePerDayMapper,times(1)).selectByMap(any(Map.class));

    }

}
