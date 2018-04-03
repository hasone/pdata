package com.cmcc.vrp.province.service.impl;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.MdrcBatchConfigInfoMapper;
import com.cmcc.vrp.province.model.MdrcBatchConfigInfo;
import com.cmcc.vrp.province.service.MdrcBatchConfigInfoService;

@RunWith(MockitoJUnitRunner.class)
public class MdrcBatchConfigInfoServiceImplTest {
    @InjectMocks
    MdrcBatchConfigInfoService MdrcBatchConfigInfoService = new MdrcBatchConfigInfoServiceImpl();
    @Mock
    MdrcBatchConfigInfoMapper mapper;
    
    @Test
    public void testSelectByPrimaryKey(){
        Mockito.when(mapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(new MdrcBatchConfigInfo());
        assertNotNull(MdrcBatchConfigInfoService.selectByPrimaryKey(1L));
    }
    
    @Test
    public void testUpdateByPrimaryKeySelective(){
        Mockito.when(mapper.updateByPrimaryKeySelective(Mockito.any(MdrcBatchConfigInfo.class))).thenReturn(1);
        assertTrue(MdrcBatchConfigInfoService.updateByPrimaryKeySelective(new MdrcBatchConfigInfo()));
    }
    
    @Test
    public void testUpdateByPrimaryKey(){
        Mockito.when(mapper.updateByPrimaryKey(Mockito.any(MdrcBatchConfigInfo.class))).thenReturn(1);
        assertTrue(MdrcBatchConfigInfoService.updateByPrimaryKey(new MdrcBatchConfigInfo()));
    }
    
    @Test
    public void testDeleteByPrimaryKey(){
        Mockito.when(mapper.deleteByPrimaryKey(Mockito.anyLong())).thenReturn(1);
        assertTrue(MdrcBatchConfigInfoService.deleteByPrimaryKey(1L));
    }
    
    @Test
    public void testInsertSelective(){
        Mockito.when(mapper.insertSelective(Mockito.any(MdrcBatchConfigInfo.class))).thenReturn(1);
        assertTrue(MdrcBatchConfigInfoService.insertSelective(new MdrcBatchConfigInfo()));
    }
    
}
