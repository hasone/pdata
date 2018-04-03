package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.MonthChargeStatisticMapper;
import com.cmcc.vrp.province.model.ChargeStatistic;
import com.cmcc.vrp.province.service.MonthChargeStatisticService;
/**
 * @author wujiamin
 * @date 2016年10月31日
 */
@RunWith(MockitoJUnitRunner.class)
public class MonthChargeStatisticServiceImplTest {
    @InjectMocks
    MonthChargeStatisticService service = new MonthChargeStatisticServiceImpl();
    
    @Mock
    MonthChargeStatisticMapper mapper;
    
    @Test
    public void batchInsert(){
        List<ChargeStatistic> records = new ArrayList();
        records.add(new ChargeStatistic());
        when(mapper.batchInsert(Mockito.anyList())).thenReturn(0,records.size());
        assertFalse(service.batchInsert(records));
        assertTrue(service.batchInsert(records));
        verify(mapper,times(2)).batchInsert(records);        
    }

    @Test
    public void getMonthChargeStatistic(){
        when(mapper.getMonthChargeStatistic(Mockito.any(Date.class))).thenReturn(new ArrayList());
        assertNotNull(service.getMonthChargeStatistic(new Date()));
        //verify(mapper,times(1)).getMonthChargeStatistic(new Date());
    }
}
