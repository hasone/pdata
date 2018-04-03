package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.IndividualActivityOrderMapper;
import com.cmcc.vrp.province.model.IndividualActivityOrder;
import com.cmcc.vrp.province.service.IndividualActivityOrderService;

@RunWith(MockitoJUnitRunner.class)
public class IndividualActivityOrderServiceImplTest {
    @InjectMocks
    IndividualActivityOrderService individualActivityOrderService
    = new IndividualActivityOrderServiceImpl();
    
    @Mock
    IndividualActivityOrderMapper mapper;
    
    @Test
    public void testInsert(){
        assertFalse(individualActivityOrderService.insert("test", null));
        
        Mockito.when(mapper.insert(Mockito.any(IndividualActivityOrder.class))).thenReturn(1);
        assertTrue(individualActivityOrderService.insert("test", 1L));
    }

}
