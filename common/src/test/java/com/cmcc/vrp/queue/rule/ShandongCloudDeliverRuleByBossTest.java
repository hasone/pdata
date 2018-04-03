package com.cmcc.vrp.queue.rule;


import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierService;
import com.cmcc.vrp.queue.QueueRegistryCenter;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * ShandongCloudDeliverRuleByBossTest
 */
@RunWith(MockitoJUnitRunner.class)
public class ShandongCloudDeliverRuleByBossTest {
    private static final String FINGERPRINT = "shandongcloud";
    @InjectMocks
    ShandongCloudDeliverRuleByBoss shandongCloudDeliverRuleByBoss = new ShandongCloudDeliverRuleByBoss();
    @Mock
    ProductService productService;
    @Mock
    SupplierProductMapService supplierProductMapService;
    @Mock
    SupplierService supplierService;
    @Mock
    QueueRegistryCenter queueRegistryCenter;

    /**
     * testDeliver
     */
    @Test
    public void testDeliver() {
        ChargeDeliverPojo pojo = initPojo();
        Product prd1 = initProductOne();
        Product prdParent = initProductOne();
        prdParent.setId(2L);

        List<SupplierProduct> listSp = initSplist();

        Mockito.when(productService.get(1L)).thenReturn(prd1);
        Mockito.when(productService.get(2L)).thenReturn(prdParent);
        Mockito.when(supplierProductMapService.getByPltfPid(2L)).thenReturn(listSp);

        Mockito.when(queueRegistryCenter.getByFingerprint(FINGERPRINT)).thenReturn(null);
        //测试正确
        Assert.assertNull(shandongCloudDeliverRuleByBoss.deliver(pojo));

        prd1.setFlowAccountFlag(1);
        Assert.assertNull(shandongCloudDeliverRuleByBoss.deliver(pojo));

        //覆盖所有错误
        listSp = new ArrayList<SupplierProduct>();
        Mockito.when(supplierProductMapService.getByPltfPid(2L)).thenReturn(listSp);
        Assert.assertNull(shandongCloudDeliverRuleByBoss.deliver(pojo));

        Mockito.when(productService.get(2L)).thenReturn(null);
        Assert.assertNull(shandongCloudDeliverRuleByBoss.deliver(pojo));

        prd1.setStatus(0);
        Assert.assertNull(shandongCloudDeliverRuleByBoss.deliver(pojo));

        prd1.setDeleteFlag(1);
        Assert.assertNull(shandongCloudDeliverRuleByBoss.deliver(pojo));

        Mockito.when(productService.get(1L)).thenReturn(null);
        Assert.assertNull(shandongCloudDeliverRuleByBoss.deliver(pojo));

        pojo.setMobile(null);
        Assert.assertNull(shandongCloudDeliverRuleByBoss.deliver(pojo));

        pojo.setEntId(null);
        Assert.assertNull(shandongCloudDeliverRuleByBoss.deliver(pojo));

        pojo.setPrdId(null);
        Assert.assertNull(shandongCloudDeliverRuleByBoss.deliver(pojo));

        Assert.assertNull(shandongCloudDeliverRuleByBoss.deliver(null));
    }

    private ChargeDeliverPojo initPojo() {
        ChargeDeliverPojo pojo = new ChargeDeliverPojo();
        pojo.setEntId(1L);
        pojo.setPrdId(1L);
        pojo.setMobile("18867100000");

        return pojo;

    }

    private Product initProductOne() {
        Product prd = new Product();
        prd.setId(1L);
        prd.setDeleteFlag(0);
        prd.setStatus(1);
        prd.setFlowAccountFlag(0);
        prd.setFlowAccountProductId(2L);

        return prd;
    }

    private List<SupplierProduct> initSplist() {
        List<SupplierProduct> list = new ArrayList<SupplierProduct>();
        SupplierProduct sp = new SupplierProduct();
        sp.setId(1L);

        list.add(sp);
        return list;
    }

}
