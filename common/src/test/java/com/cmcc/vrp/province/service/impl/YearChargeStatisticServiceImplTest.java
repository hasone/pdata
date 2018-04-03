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

import com.cmcc.vrp.province.dao.YearChargeStatisticMapper;
import com.cmcc.vrp.province.model.ChargeStatistic;
import com.cmcc.vrp.province.service.YearChargeStatisticService;
/**
 * @author wujiamin
 * @date 2016年10月31日
 */
@RunWith(MockitoJUnitRunner.class)
public class YearChargeStatisticServiceImplTest {
    @InjectMocks
    YearChargeStatisticService service = new YearChargeStatisticServiceImpl();
    
    @Mock
    YearChargeStatisticMapper mapper;
    
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
    public void getYearChargeStatistic(){
        when(mapper.getYearChargeStatistic(Mockito.any(Date.class))).thenReturn(new ArrayList());
        assertNotNull(service.getYearChargeStatistic(new Date()));
        //verify(mapper,times(1)).getYearChargeStatistic(new Date());
    }
}
