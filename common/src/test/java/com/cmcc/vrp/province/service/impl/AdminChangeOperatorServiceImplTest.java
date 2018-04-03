package com.cmcc.vrp.province.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.dao.AdminChangeOperatorMapper;
import com.cmcc.vrp.province.model.AdminChangeOperator;

/**
 * 
 * AdminChangeDetailServiceImplTest
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class AdminChangeOperatorServiceImplTest {
    @InjectMocks
    AdminChangeOperatorServiceImpl  adminChangeOperatorService = new AdminChangeOperatorServiceImpl();
    @Mock
    AdminChangeOperatorMapper adminChangeOperatorMapper;
    
    /**
     * testGetByAdminId
     */
    @Test
    public void testGetByAdminId(){
        Mockito.when(adminChangeOperatorMapper.getAdminChangeRecordByAdminId(Mockito.anyLong())).thenReturn(null);
        Assert.assertNull(adminChangeOperatorService.getByAdminId(1L));
    }
    
    /**
     * testGetByAdminId
     */
    @Test
    public void testDeleteByAdminId(){
        Mockito.when(adminChangeOperatorMapper.deleteAdminChangeRecordByAdminId(Mockito.anyLong())).thenReturn(1);
        Assert.assertTrue(adminChangeOperatorService.deleteByAdminId(1L));
        
        Mockito.when(adminChangeOperatorMapper.deleteAdminChangeRecordByAdminId(Mockito.anyLong())).thenReturn(0);
        Assert.assertFalse(adminChangeOperatorService.deleteByAdminId(1L));
    }
    
    /**
     * testInsertSelective
     */
    @Test
    public void testInsertSelective(){
        Mockito.when(adminChangeOperatorMapper.insertSelective(Mockito.any(AdminChangeOperator.class))).thenReturn(1);
        Assert.assertTrue(adminChangeOperatorService.insert(null));
        
        Mockito.when(adminChangeOperatorMapper.insertSelective(Mockito.any(AdminChangeOperator.class))).thenReturn(0);
        Assert.assertFalse(adminChangeOperatorService.insert(null));
    }

    /**
     * testInsertAndDelByAdminId
     */
    @Test
    public void testInsertAndDelByAdminId(){
        AdminChangeOperator adminChangeOperator = new AdminChangeOperator();
        adminChangeOperator.setId(1L);
        adminChangeOperator.setAdminId(1L);
        
        Mockito.when(adminChangeOperatorMapper.deleteAdminChangeRecordByAdminId(Mockito.anyLong())).thenReturn(1);
        Mockito.when(adminChangeOperatorMapper.insertSelective(Mockito.any(AdminChangeOperator.class))).thenReturn(1);
        Assert.assertTrue(adminChangeOperatorService.insertAndDelByAdminId(adminChangeOperator));
        
        
        Mockito.when(adminChangeOperatorMapper.insertSelective(Mockito.any(AdminChangeOperator.class))).thenReturn(0);
        try{
            Assert.assertFalse(adminChangeOperatorService.insertAndDelByAdminId(adminChangeOperator));
        }catch(TransactionException e){
            
        }
        
        adminChangeOperator.setAdminId(null);
        Assert.assertFalse(adminChangeOperatorService.insertAndDelByAdminId(adminChangeOperator));
    }
}
