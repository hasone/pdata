package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.IndividualActivitySerialNumMapper;
import com.cmcc.vrp.province.model.IndividualActivitySerialNum;
import com.cmcc.vrp.province.service.IndividualActivitySerialNumService;

/**
 * IndividualActivitiesServiceImplTest.java
 * @author wujiamin
 * @date 2017年1月22日
 */
@RunWith(MockitoJUnitRunner.class)
public class IndividualActivitySerialNumServiceImplTest {
    @InjectMocks
    IndividualActivitySerialNumService service = new IndividualActivitySerialNumServiceImpl();
    
    @Mock
    IndividualActivitySerialNumMapper mapper;
    
    @Test
    public void testInsert(){
        Mockito.when(mapper.insert(Mockito.any(IndividualActivitySerialNum.class))).thenReturn(1);
        assertTrue(service.insert(new IndividualActivitySerialNum()));
        Mockito.verify(mapper,Mockito.times(1)).insert(Mockito.any(IndividualActivitySerialNum.class));
    }
    
    @Test
    public void testInsertSelective(){
        Mockito.when(mapper.insertSelective(Mockito.any(IndividualActivitySerialNum.class))).thenReturn(1);
        assertTrue(service.insertSelective(new IndividualActivitySerialNum()));
        Mockito.verify(mapper,Mockito.times(1)).insertSelective(Mockito.any(IndividualActivitySerialNum.class));
    }
    
    @Test
    public void testSelectByPrimaryKey(){
        Mockito.when(mapper.selectByPrimaryKey(1)).thenReturn(new IndividualActivitySerialNum());
        assertNotNull(service.selectByPrimaryKey(1));
        Mockito.verify(mapper,Mockito.times(1)).selectByPrimaryKey(1);
    }
    
    @Test
    public void testUpdateByPrimaryKeySelective(){
        Mockito.when(mapper.updateByPrimaryKeySelective(Mockito.any(IndividualActivitySerialNum.class))).thenReturn(1);
        assertTrue(service.updateByPrimaryKeySelective(new IndividualActivitySerialNum()));
        Mockito.verify(mapper,Mockito.times(1)).updateByPrimaryKeySelective(Mockito.any(IndividualActivitySerialNum.class));
    }
    
    @Test
    public void testUpdateByPrimaryKey(){
        Mockito.when(mapper.updateByPrimaryKey(Mockito.any(IndividualActivitySerialNum.class))).thenReturn(1);
        assertTrue(service.updateByPrimaryKey(new IndividualActivitySerialNum()));
        Mockito.verify(mapper,Mockito.times(1)).updateByPrimaryKey(Mockito.any(IndividualActivitySerialNum.class));
    }

}
