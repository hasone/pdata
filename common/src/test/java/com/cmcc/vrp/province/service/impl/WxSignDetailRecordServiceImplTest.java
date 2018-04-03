package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.WxSignDetailRecordMapper;
import com.cmcc.vrp.province.model.WxSignDetailRecord;
import com.cmcc.vrp.province.service.WxSignDetailRecordService;

@RunWith(MockitoJUnitRunner.class)
public class WxSignDetailRecordServiceImplTest {
    
    @InjectMocks
    WxSignDetailRecordService wxSignDetailRecordService = new WxSignDetailRecordServiceImpl();
    @Mock
    WxSignDetailRecordMapper mapper;
    
    @Test
    public void testInsertSelective(){
        Mockito.when(mapper.insertSelective(Mockito.any(WxSignDetailRecord.class))).thenReturn(1);
        assertTrue(wxSignDetailRecordService.insertSelective(new WxSignDetailRecord()));
    }
    
    @Test
    public void testSelectByMap(){
        Mockito.when(mapper.selectByMap(Mockito.anyMap())).thenReturn(new ArrayList<WxSignDetailRecord>());
        assertNotNull(wxSignDetailRecordService.selectByMap(new HashMap<String, String>()));
    }

}
