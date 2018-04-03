package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.WxSerialSignRecordMapper;
import com.cmcc.vrp.province.model.WxSerialSignRecord;
import com.cmcc.vrp.province.service.WxSerialSignRecordService;



@RunWith(MockitoJUnitRunner.class)
public class WxSerialSignRecordServiceImplTest {
    @InjectMocks
    WxSerialSignRecordService wxSerialSignRecordService = new WxSerialSignRecordServiceImpl();
    
    @Mock
    WxSerialSignRecordMapper mapper;
    
    @Test
    public void testInsertSelective(){
        Mockito.when(mapper.insertSelective(Mockito.any(WxSerialSignRecord.class))).thenReturn(1).thenReturn(-1);
        assertTrue(wxSerialSignRecordService.insertSelective(new WxSerialSignRecord()));
        assertFalse(wxSerialSignRecordService.insertSelective(new WxSerialSignRecord()));
    }
    
    @Test
    public void testUpdateByPrimaryKeySelective(){
        Mockito.when(mapper.updateByPrimaryKeySelective(Mockito.any(WxSerialSignRecord.class))).thenReturn(1).thenReturn(-1);
        assertTrue(wxSerialSignRecordService.updateByPrimaryKeySelective(new WxSerialSignRecord()));
        assertFalse(wxSerialSignRecordService.updateByPrimaryKeySelective(new WxSerialSignRecord()));
    }
    
    @Test
    public void testSelectByMap(){
        Mockito.when(mapper.selectByMap(Mockito.anyMap())).thenReturn(new ArrayList<WxSerialSignRecord>());
        assertNotNull(wxSerialSignRecordService.selectByMap(new HashMap<String, String>()));
    }
    @Test
    public void testGetTotalCountByAdminIdAndMonth() {
        List<WxSerialSignRecord> wxSerialSignRecords = new ArrayList<WxSerialSignRecord>();
        WxSerialSignRecord wxSerialSignRecord = new WxSerialSignRecord();
        wxSerialSignRecord.setCount(10);
        wxSerialSignRecords.add(wxSerialSignRecord);
        Mockito.when(mapper.selectByMap(Mockito.anyMap())).thenReturn(new ArrayList<WxSerialSignRecord>()).thenReturn(wxSerialSignRecords);
        Assert.assertEquals(0, wxSerialSignRecordService.getTotalCountByAdminIdAndMonth(1l, new java.util.Date()));
        Assert.assertEquals(10, wxSerialSignRecordService.getTotalCountByAdminIdAndMonth(1l, new java.util.Date()));
    }

}
