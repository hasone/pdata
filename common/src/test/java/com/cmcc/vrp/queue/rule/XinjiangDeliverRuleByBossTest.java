/**
 *
 */
package com.cmcc.vrp.queue.rule;

import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.queue.QueueRegistryCenter;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.queue.queue.busi.DeliverByBossQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * XinjiangDeliverRuleByBossTest.java
 *
 * @author wujiamin
 * @date 2016年12月1日
 */
@RunWith(MockitoJUnitRunner.class)
public class XinjiangDeliverRuleByBossTest {

    @InjectMocks
    XinjiangDeliverRuleByBoss deliverRuleByBoss = new XinjiangDeliverRuleByBoss();

    @Mock
    QueueRegistryCenter queueRegistryCenter;

    @Mock
    EntProductService entProductService;

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

//	SupplierProduct sp = new SupplierProduct();
//	sps.add(sp);

        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null).thenReturn(ep);

        assertNull(deliverRuleByBoss.deliver(pojo));
        assertNull(deliverRuleByBoss.deliver(pojo));
        assertNull(deliverRuleByBoss.deliver(pojo));

        DeliverByBossQueue aq = new DeliverByBossQueue();

        Mockito.when(queueRegistryCenter.getByFingerprint("xinjiang")).thenReturn(aq);
        assertEquals(aq, deliverRuleByBoss.deliver(pojo));


    }

    private ChargeDeliverPojo createChargeDeliverPojo() {
        ChargeDeliverPojo pojo = new ChargeDeliverPojo();
        pojo.setEntId(1L);
        pojo.setPrdId(1L);
        pojo.setMobile("18867101111");
        return pojo;
    }


}
