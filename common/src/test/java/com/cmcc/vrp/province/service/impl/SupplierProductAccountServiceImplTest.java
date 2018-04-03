package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Matchers.any;

import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.dao.SupplierProductAccountMapper;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductAccount;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.SupplierProductAccountService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierProductService;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2016年11月30日 上午9:50:01
*/
@RunWith(MockitoJUnitRunner.class)
public class SupplierProductAccountServiceImplTest {
    @InjectMocks
    SupplierProductAccountService supplierProductAccountService = new SupplierProductAccountServiceImpl();
    
    @Mock
	SupplierProductService supplierProductService;
	
    @Mock
	SupplierProductAccountMapper supplierProductAccountMapper;
	
    @Mock
	AccountService accountService;
	
    @Mock
	SupplierProductMapService supplierProductMapService;
	
    @Mock
	EnterprisesService enterpriseService;
    
    /**
     * 
     */
    @Test
    public void testCreateSupplierProductAccount() {
    	assertFalse(supplierProductAccountService.createSupplierProductAccount(null, (double) 5, (long) 5));
    	assertFalse(supplierProductAccountService.createSupplierProductAccount((long) 5, null, (long) 5));
    	assertFalse(supplierProductAccountService.createSupplierProductAccount((long) 5, (double) 5, null));
    	when(supplierProductService.selectByPrimaryKey(any(Long.class)))
        	.thenReturn(null);
    	assertSame(false, supplierProductAccountService.createSupplierProductAccount((long)123, (double)5, (long)123));
    	when(supplierProductService.selectByPrimaryKey(any(Long.class)))
        	.thenReturn(new SupplierProduct());
    	when(supplierProductAccountMapper.createSupplierProductAccount(any(SupplierProductAccount.class)))
        	.thenReturn(1);
    	assertSame(true, supplierProductAccountService.createSupplierProductAccount((long)123, (double)5, (long)123));
    	when(supplierProductAccountMapper.createSupplierProductAccount(any(SupplierProductAccount.class)))
        	.thenReturn(1);
    	assertSame(true, supplierProductAccountService.createSupplierProductAccount((long)123, (double)5, (long)123));
    	when(supplierProductAccountMapper.createSupplierProductAccount(any(SupplierProductAccount.class)))
        	.thenReturn(0);
    	assertSame(false, supplierProductAccountService.createSupplierProductAccount((long)123, (double)5, (long)123));
    }
    
    /**
     * 
     */
    @Test
    public void testGetInfoBySupplierProductId() {
    	assertNull(supplierProductAccountService.getInfoBySupplierProductId(null));
    	when(supplierProductService.selectById(any(Long.class)))
        	.thenReturn(null);
    	assertNull(supplierProductAccountService.getInfoBySupplierProductId((long)5));
    	when(supplierProductService.selectById(any(Long.class)))
        	.thenReturn(new SupplierProduct());
    	SupplierProductAccount supplierProductAccount = new SupplierProductAccount();
    	supplierProductAccount.setId((long) 1);
    	when(supplierProductAccountMapper.getInfoBySupplierProductId(any(Long.class)))
        	.thenReturn(supplierProductAccount);
    	
    	assertNotNull(supplierProductAccountService.getInfoBySupplierProductId((long)5));
    	when(supplierProductAccountMapper.getInfoBySupplierProductId(any(Long.class)))
    		.thenReturn(null);
    	assertNull(supplierProductAccountService.getInfoBySupplierProductId((long)5));
    }
    
    /**
     * 
     */
    @Test
    public void testUpdateSupplierProductAccount() {
    	assertSame(false, supplierProductAccountService.updateSupplierProductAccount(null, (double) 5, (long) 5));
    	assertSame(false, supplierProductAccountService.updateSupplierProductAccount((long) 5, null, (long) 5));
    	assertSame(false, supplierProductAccountService.updateSupplierProductAccount((long) 5, (double) 5, null));   	
    	
    	when(supplierProductService.selectByPrimaryKey(any(Long.class)))
        	.thenReturn(null);
    	assertSame(false,supplierProductAccountService.updateSupplierProductAccount((long)123, (double)5, (long) 5));
    	
    	SupplierProduct supplierProduct = new SupplierProduct();
    	supplierProduct.setFeature("{enterCode:123}");
    	when(supplierProductService.selectByPrimaryKey(any(Long.class)))
        	.thenReturn(supplierProduct);
    	SupplierProductAccount supplierProductAccount = new SupplierProductAccount();
    	supplierProductAccount.setCount((double) 5);
    	when(supplierProductAccountMapper.getInfoBySupplierProductId(any(Long.class)))
        	.thenReturn(supplierProductAccount);
    	when(supplierProductAccountMapper.updateSupplierProductAccount(any(Long.class), any(Double.class)))
        	.thenReturn(0);
    	
    	try  {
    	    supplierProductAccountService.updateSupplierProductAccount((long)123, (double)5, (long) 5);
    	} catch(TransactionException e) {
    	    assertSame("更新失败", e.getMessage());
    	}
    	
    	when(supplierProductAccountMapper.updateSupplierProductAccount(any(Long.class), any(Double.class)))
        	.thenReturn(1);
    	Product product = new Product();
    	product.setId((long) 1234);
    	List<Product> products = new ArrayList<Product>();
    	products.add(product);
    	when(supplierProductMapService.getBySplPid(any(Long.class)))
        	.thenReturn(products);
    	Enterprise enterprise = new Enterprise();
    	enterprise.setId((long)123);
    	when(enterpriseService.selectByCode(any(String.class)))
        	.thenReturn(enterprise);
    	when(accountService.addCount(any(Long.class), any(Long.class), any(AccountType.class), any(Double.class), any(String.class), any(String.class)))
        	.thenReturn(true);
    	when(accountService.minusCount(any(Long.class), any(Long.class), any(AccountType.class), any(Double.class), any(String.class), any(String.class)))
        	.thenReturn(true);
    	assertTrue(supplierProductAccountService.updateSupplierProductAccount((long)123, (double)5, (long) 5));
    	when(accountService.minusCount(any(Long.class), any(Long.class), any(AccountType.class), any(Double.class), any(String.class), any(String.class)))
        	.thenReturn(false);
    	try  {
    	    supplierProductAccountService.updateSupplierProductAccount((long)123, (double)5, (long) 5);
    	} catch(TransactionException e) {
    	    assertSame("扣减平台侧账户余额失败", e.getMessage());
    	}
    	assertTrue(supplierProductAccountService.updateSupplierProductAccount((long)123, (double)6, (long) 5));
    	when(accountService.addCount(any(Long.class), any(Long.class), any(AccountType.class), any(Double.class), any(String.class), any(String.class)))
        	.thenReturn(false);
    	try  {
    	    supplierProductAccountService.updateSupplierProductAccount((long)123, (double)6, (long) 5);
    	} catch(TransactionException e) {
    	    assertSame("增加平台侧账户余额失败", e.getMessage());
    	}
    }
    
    /**
     * 
     */
    @Test
    public void testGetInfoByEntSyncListId() {
    	assertNull(supplierProductAccountService.getInfoByEntSyncListId(null));
    	when(supplierProductAccountMapper.getInfoByEntSyncListId(any(Long.class)))
    	    .thenReturn(new ArrayList<SupplierProductAccount>());
    	assertNotNull(supplierProductAccountService.getInfoByEntSyncListId((long) 1));
    }
    
    /**
     * 
     */
    @Test
    public void testGetById() {
        SupplierProductAccount supplierProductAccount = new SupplierProductAccount();
        Mockito.when(supplierProductAccountMapper.getById(Mockito.anyLong())).thenReturn(supplierProductAccount);
        Assert.assertSame(supplierProductAccount, supplierProductAccountService.getById(1l));
        
    }
    
    /**
     * 
     */
    @Test
    public void testUpdateById() {
        Mockito.when(supplierProductAccountMapper.updateById(Mockito.anyLong(), Mockito.anyDouble()))
            .thenReturn(false).thenReturn(true);
        Assert.assertFalse(supplierProductAccountService.updateById(1l, 10d));
        Assert.assertTrue(supplierProductAccountService.updateById(1l, 10d));
        
    }
}
