package com.cmcc.vrp.boss.xinjiang;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.xinjiang.response.NewResourcePoolResp;
import com.cmcc.vrp.boss.xinjiang.response.SendResp;
import com.cmcc.vrp.boss.xinjiang.service.XinjiangBossService;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProductService;

/**
 * XjBossServiceImplTest
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class XjBossServiceImplTest {
    @InjectMocks
    XjBossServiceImpl xjBossServiceImpl = new XjBossServiceImpl();
    
    @Mock
    XinjiangBossService xinjiangBossService;
    
    @Mock
    EnterprisesService enterprisesService;
    
    @Mock
    ProductService productService;
    
    @Mock
    SupplierProductService supplierProductService;
    
    /**
     * testCharge
     */
    @Test
    public void testCharge(){
        Long entId = 1L;
        Long splPid = 1L;
        String mobile= "18867102087";
        String serialNum ="100001";
        Long prdSize = 1024L;
        
        Enterprise enterprise = initEnterprise();
        SupplierProduct oldSProduct = initSproductOld();
        SupplierProduct newSpProduct = initSproductNew();
        SendResp resp = new SendResp();
        resp.setResultCode("0");
        //测试新产品
        Mockito.when(enterprisesService.selectByPrimaryKey(entId)).thenReturn(enterprise);
        Mockito.when(supplierProductService.selectByPrimaryKey(splPid)).thenReturn(newSpProduct);
        Mockito.when(xinjiangBossService.getNewSendResp(Mockito.anyString(), Mockito.anyString(), 
                Mockito.anyString(), Mockito.anyString())).thenReturn(resp);
        
        Assert.assertNotNull(xjBossServiceImpl.charge(entId, splPid, mobile, serialNum, prdSize));

        //测试老产品
        Mockito.when(supplierProductService.selectByPrimaryKey(splPid)).thenReturn(oldSProduct);
        Mockito.when(xinjiangBossService.getSendResp(Mockito.anyString(),Mockito.anyString(),
                Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(resp);
        Assert.assertNotNull(xjBossServiceImpl.charge(entId, splPid, mobile, serialNum, prdSize));
        
        //测试老产品错误
        enterprise.setCode("1000001");
        Mockito.when(enterprisesService.selectByPrimaryKey(entId)).thenReturn(enterprise);
        Assert.assertNotNull(xjBossServiceImpl.charge(entId, splPid, mobile, serialNum, prdSize));
        
        //测试产品编码错误
        oldSProduct.setCode("XXXX");
        Mockito.when(supplierProductService.selectByPrimaryKey(splPid)).thenReturn(oldSProduct);
        Assert.assertNotNull(xjBossServiceImpl.charge(entId, splPid, mobile, serialNum, prdSize));
        
        //测试sp产品不存在
        Mockito.when(supplierProductService.selectByPrimaryKey(splPid)).thenReturn(null);
        Assert.assertNotNull(xjBossServiceImpl.charge(entId, splPid, mobile, serialNum, prdSize));
        
        //测试企业不存在
        Mockito.when(enterprisesService.selectByPrimaryKey(entId)).thenReturn(null);
        Assert.assertNotNull(xjBossServiceImpl.charge(entId, splPid, mobile, serialNum, prdSize));
    }
    
    /**
     * testQueryAccountByEntId
     */
    @Test
    public void testQueryAccountByEntId(){
        Long entId = 1L;
        Enterprise enterprise = initEnterprise();
        String enterCode = enterprise.getCode();
        String defaultResult = "网络繁忙，请稍后再试";
        
        Mockito.when(enterprisesService.selectByPrimaryKey(entId)).thenReturn(enterprise);
        
        NewResourcePoolResp resp = new NewResourcePoolResp();
        resp.setResultCode("0");
        resp.setAddValue("1024");
        Mockito.when(xinjiangBossService.getResourcePoolRespNew(enterCode)).thenReturn(resp);
        Assert.assertEquals("1024MB", xjBossServiceImpl.queryAccountByEntId(entId));
        
        //测试错误
        resp.setResultCode("-1");
        Mockito.when(xinjiangBossService.getResourcePoolRespNew(enterCode)).thenReturn(resp);
        Assert.assertEquals(defaultResult, xjBossServiceImpl.queryAccountByEntId(entId));
        
        Mockito.when(enterprisesService.selectByPrimaryKey(entId)).thenReturn(null);
        Assert.assertEquals(defaultResult, xjBossServiceImpl.queryAccountByEntId(entId));
    }
    
    /**
     * testGetFingerPrint
     */
    @Test
    public void testGetFingerPrint(){
        Assert.assertEquals("xinjiang", xjBossServiceImpl.getFingerPrint());
    }
    
    /**
     * testMdrcCharge
     */
    @Test
    public void testMdrcCharge(){
        Assert.assertNull(xjBossServiceImpl.mdrcCharge("1", "1"));
    }
    
    /**
     * testSyncFromBoss
     */
    @Test
    public void testSyncFromBoss(){
        Assert.assertFalse(xjBossServiceImpl.syncFromBoss(1L, 1L));
    }
    
    /**
     * initEnterprise
     */
    private Enterprise initEnterprise(){
        Enterprise enterprise = new Enterprise();
        enterprise.setId(1L);
        enterprise.setCode("9919122100");
        return enterprise;
    }
    
    /**
     * initSproductOld
     */
    private SupplierProduct initSproductOld(){
        SupplierProduct supplierProduct = new SupplierProduct();
        supplierProduct.setId(1L);
        supplierProduct.setCode("oldProductOne");
        return supplierProduct;
    }
    
    /**
     * initSproductNew
     */
    private SupplierProduct initSproductNew(){
        SupplierProduct supplierProduct = new SupplierProduct();
        supplierProduct.setId(1L);
        supplierProduct.setCode("flowProduct");
        return supplierProduct;
    }
}
