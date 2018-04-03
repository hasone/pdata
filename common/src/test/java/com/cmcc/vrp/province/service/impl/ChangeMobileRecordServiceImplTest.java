package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.ChangeMobileRecordMapper;
import com.cmcc.vrp.province.model.ChangeMobileRecord;
import com.cmcc.vrp.province.service.ChangeMobileRecordService;

@RunWith(MockitoJUnitRunner.class)
public class ChangeMobileRecordServiceImplTest {
    
    @InjectMocks
    ChangeMobileRecordService changeMobileRecordService =
    new ChangeMobileRecordServiceImpl();
    
    @Mock
    ChangeMobileRecordMapper mapper;
    
    @Test
    public void testInsert(){
        Mockito.when(mapper.insert(Mockito.any(ChangeMobileRecord.class))).thenReturn(1);
        assertTrue(changeMobileRecordService.insert(new ChangeMobileRecord()));
    }
    
    @Test
    public void testInsertSelective(){
        Mockito.when(mapper.insertSelective(Mockito.any(ChangeMobileRecord.class))).thenReturn(1);
        assertTrue(changeMobileRecordService.insertSelective(new ChangeMobileRecord()));
    }
    
    @Test
    public void testUpdateByPrimaryKeySelective(){
        Mockito.when(mapper.updateByPrimaryKeySelective(Mockito.any(ChangeMobileRecord.class))).thenReturn(1);
        assertTrue(changeMobileRecordService.updateByPrimaryKeySelective(new ChangeMobileRecord()));
    }
    
    @Test
    public void testUpdateByPrimaryKey(){
        Mockito.when(mapper.updateByPrimaryKey(Mockito.any(ChangeMobileRecord.class))).thenReturn(1);
        assertTrue(changeMobileRecordService.updateByPrimaryKey(new ChangeMobileRecord()));
    }

}
