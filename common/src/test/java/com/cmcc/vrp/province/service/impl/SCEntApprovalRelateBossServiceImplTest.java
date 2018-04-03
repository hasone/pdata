package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.SyncAccountResult;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.EnterpriseUserIdService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductService;

@RunWith(MockitoJUnitRunner.class)
public class SCEntApprovalRelateBossServiceImplTest {
    @InjectMocks
    SCEntApprovalRelatedBossServiceImpl service = new SCEntApprovalRelatedBossServiceImpl();
    
    @Mock
    EnterpriseUserIdService enterpriseUserIdService;
    
    @Mock
    EnterprisesService enterprisesService;

    @Mock
    ProductService productService;
    
    @Mock
    AccountService accountService;
    
    @Test
    public void synchronizeFromBoss0(){
        when(enterprisesService.selectByPrimaryKey(1L)).thenReturn(new Enterprise());
        when(enterpriseUserIdService.saveUserId(Mockito.any(Enterprise.class))).thenReturn(true);
        Product product = new Product();
        product.setId(1L);
        when(productService.getCurrencyProduct()).thenReturn(product);
        when(accountService.syncFromBoss(Mockito.anyLong(), Mockito.anyLong())).thenReturn(new SyncAccountResult(null,true));
        assertTrue(service.synchronizeFromBoss(1L));
        verify(enterprisesService,times(1)).selectByPrimaryKey(1L);
        verify(enterpriseUserIdService,times(1)).saveUserId(Mockito.any(Enterprise.class));
        
    }
    
    @Test
    public void synchronizeFromBoss1(){
        when(enterprisesService.selectByPrimaryKey(1L)).thenReturn(new Enterprise());
        when(enterpriseUserIdService.saveUserId(Mockito.any(Enterprise.class))).thenReturn(true);
        Product product = new Product();
        product.setId(1L);
        when(productService.getCurrencyProduct()).thenReturn(null);
        assertFalse(service.synchronizeFromBoss(1L));
        verify(enterprisesService,times(1)).selectByPrimaryKey(1L); 
    }
    
    @Test
    public void synchronizeFromBoss2(){
        when(enterprisesService.selectByPrimaryKey(1L)).thenReturn(new Enterprise());
        when(enterpriseUserIdService.saveUserId(Mockito.any(Enterprise.class))).thenReturn(true);
        Product product = new Product();
        when(productService.getCurrencyProduct()).thenReturn(product);
        assertFalse(service.synchronizeFromBoss(1L));
        verify(enterprisesService,times(1)).selectByPrimaryKey(1L);       
    }
}
