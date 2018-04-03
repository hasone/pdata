/**
 *
 */
package com.cmcc.vrp.queue.rule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.Supplier;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductMap;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierService;
import com.cmcc.vrp.queue.QueueRegistryCenter;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.queue.queue.busi.DeliverByBossQueue;

/**
 * <p>Title:ZiyingDeliverRuleByBossTest </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年12月1日
 */
@RunWith(MockitoJUnitRunner.class)
public class ZiyingDeliverRuleByBossTest {

    @InjectMocks
    ZiyingDeliverRuleByBoss deliverRuleByBoss = new ZiyingDeliverRuleByBoss();

    @Mock
    QueueRegistryCenter queueRegistryCenter;

    @Mock
    PhoneRegionService phoneRegionService;

    @Mock
    EntProductService entProductService;

    @Mock
    ProductService productService;

    @Mock
    SupplierProductMapService supplierProductMapService;

    @Mock
    SupplierService supplierService;

    @Test
    public void testDeliver1() {
        ChargeDeliverPojo pojo = new ChargeDeliverPojo();
        pojo.setPrdId(1L);
        pojo.setEntId(1L);
        assertNull(deliverRuleByBoss.deliver(pojo));
    }

    @Test
    public void testDeliver2() {
        ChargeDeliverPojo pojo = createChargeDeliverPojo();
        PhoneRegion phoneRegion = new PhoneRegion();
        EntProduct ep = new EntProduct();
        Product pltProduct = new Product();
        pltProduct.setId(1L);
        List<SupplierProduct> sps = new ArrayList();
//	SupplierProduct sp = new SupplierProduct();
//	sps.add(sp);

        Mockito.when(phoneRegionService.query(Mockito.anyString())).thenReturn(null).thenReturn(phoneRegion);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null).thenReturn(ep);
        Mockito.when(productService.selectProductById(Mockito.anyLong())).thenReturn(pltProduct);
        Mockito.when(supplierProductMapService.getByPltfPid(Mockito.anyLong())).thenReturn(sps);

        assertNull(deliverRuleByBoss.deliver(pojo));
        assertNull(deliverRuleByBoss.deliver(pojo));
        assertNull(deliverRuleByBoss.deliver(pojo));

        SupplierProduct sp = new SupplierProduct();
        sp.setId(1L);
        sp.setSupplierId(1L);
        sp.setOwnershipRegion("全国");
        sp.setPrice(1);
        sp.setIsp("U");
        sp.setLimitMoneyFlag(0);
        sp.setSupplierLimitMoneyFlag(0);
        sps.add(sp);
        SupplierProduct sp1 = new SupplierProduct();
        sp1.setId(2L);
        sp1.setSupplierId(2L);
        sp1.setOwnershipRegion("全国");
        sp1.setPrice(2);
        sp1.setIsp("A");
        sp1.setLimitMoneyFlag(0);
        sp1.setSupplierLimitMoneyFlag(0);
        sps.add(sp1);
        Supplier spl = new Supplier();
        spl.setFingerprint("test");
        DeliverByBossQueue aq = new DeliverByBossQueue();
        Product product = new Product();
        product.setType(1);
        product.setFlowAccountFlag(1);
        Mockito.when(productService.get(Mockito.anyLong())).thenReturn(product);
        Mockito.when(supplierService.get(Mockito.anyLong())).thenReturn(spl);
        Mockito.when(queueRegistryCenter.getByFingerprint(spl.getFingerprint())).thenReturn(aq);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(Mockito.anyLong(), Mockito.anyLong())).thenReturn(new EntProduct());
        
        Mockito.when(supplierProductMapService.getOnshelfByPltfPid(product.getId())).thenReturn(sps);
        SupplierProductMap supplierProductMap = new SupplierProductMap();
        supplierProductMap.setPriorFlag(1);
        Mockito.when(supplierProductMapService.getBypltfpidAndSplpid(Mockito.anyLong(), Mockito.anyLong())).thenReturn(supplierProductMap);
        
        assertEquals(aq, deliverRuleByBoss.deliver(pojo));


    }
    
    @Test
    public void testDeliver3() {
        ChargeDeliverPojo pojo = createChargeDeliverPojo();
        PhoneRegion phoneRegion = new PhoneRegion();
        EntProduct ep = new EntProduct();
        Product pltProduct = new Product();
        pltProduct.setId(1L);
        pltProduct.setFlowAccountFlag(1);
        List<SupplierProduct> sps = new ArrayList();
//  SupplierProduct sp = new SupplierProduct();
//  sps.add(sp);

        Mockito.when(phoneRegionService.query(Mockito.anyString())).thenReturn(phoneRegion);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);
        Mockito.when(productService.get(Mockito.anyLong())).thenReturn(pltProduct);

        assertNull(deliverRuleByBoss.deliver(pojo));        
        
    }

    private ChargeDeliverPojo createChargeDeliverPojo() {
        ChargeDeliverPojo pojo = new ChargeDeliverPojo();
        pojo.setEntId(1L);
        pojo.setPrdId(1L);
        pojo.setMobile("18867101111");
        return pojo;
    }


}
