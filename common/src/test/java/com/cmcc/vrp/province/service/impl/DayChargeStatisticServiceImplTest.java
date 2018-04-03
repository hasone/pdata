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

import com.cmcc.vrp.province.dao.DayChargeStatisticMapper;
import com.cmcc.vrp.province.model.ChargeStatistic;
import com.cmcc.vrp.province.service.DayChargeStatisticService;
/**
 * @author wujiamin
 * @date 2016年10月31日
 */
@RunWith(MockitoJUnitRunner.class)
public class DayChargeStatisticServiceImplTest {
    @InjectMocks
    DayChargeStatisticService service = new DayChargeStatisticServiceImpl();
    
    @Mock
    DayChargeStatisticMapper dayChargeStatisticMapper;
    
    @Test
    public void batchInsert(){
        List<ChargeStatistic> records = new ArrayList();
        records.add(new ChargeStatistic());
        when(dayChargeStatisticMapper.batchInsert(Mockito.anyList())).thenReturn(0,records.size());
        assertFalse(service.batchInsert(records));
        assertTrue(service.batchInsert(records));
        verify(dayChargeStatisticMapper,times(2)).batchInsert(records);        
    }

    @Test
    public void getDayChargeStatistic(){
        when(dayChargeStatisticMapper.getDayChargeStatistic(Mockito.any(Date.class))).thenReturn(new ArrayList());
        assertNotNull(service.getDayChargeStatistic(new Date()));
    }
}
