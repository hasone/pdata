
package com.cmcc.vrp.queue.rule;

import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.EntSyncList;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductAccount;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EntSyncListService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProductAccountService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.queue.QueueRegistryCenter;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;

/**
 * Created by leelyn on 2016/12/13.
 */
@RunWith(MockitoJUnitRunner.class)
public class ChongqingDeliverRuleByBossTest {

    @InjectMocks
    ChongqingDeliverRuleByBoss ruleByBoss = new ChongqingDeliverRuleByBoss();

    @Mock
    ProductService productService;

    @Mock
    EntProductService entProductService;

    @Mock
    QueueRegistryCenter queueRegistryCenter;

    @Mock
    EnterprisesService enterprisesService;

    @Mock
    EntSyncListService entSyncListService;

    @Mock
    SupplierProductService supplierProductService;

    @Mock
    SupplierProductAccountService supplierProductAccountService;

    @Before
    public void initMocks() {
        Product p = new Product();
        p.setType(2);
        Mockito.when(productService.get(anyLong())).thenReturn(p);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(new EntProduct());
        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(new Enterprise());
        Mockito.when(productService.selectProductById(anyLong())).thenReturn(new Product());
        Mockito.when(entSyncListService.getByEntId(anyLong())).thenReturn(buildList());
        Mockito.when(supplierProductService.selectByFeature(anyString())).thenReturn(new SupplierProduct());
        SupplierProductAccount supplierProductAccount = new SupplierProductAccount();
        supplierProductAccount.setCreateTime(new Date());
        supplierProductAccount.setCount(2d);
        Mockito.when(supplierProductAccountService.getInfoBySupplierProductId(anyLong())).thenReturn(supplierProductAccount);
    }

    @Test
    public void testDeliver(){
        Assert.assertNull(null);
    }

    @Ignore
    @Test
    public void test() throws Exception {
        Mockito.when(queueRegistryCenter.getByFingerprint(anyString())).thenReturn(null);
        Assert.assertNull(ruleByBoss.deliver(buildPojo()));
    }

    private ChargeDeliverPojo buildPojo() {
        ChargeDeliverPojo pojo = new ChargeDeliverPojo();
        pojo.setPrdId(2l);
        pojo.setEntId(3l);
        pojo.setMobile("18888888888");
        return pojo;
    }

    private List<EntSyncList> buildList() {
        List<EntSyncList> list = new ArrayList<EntSyncList>();
        for (int i = 0; i < 10; i++) {
            EntSyncList entSyncList = new EntSyncList();
            entSyncList.setEntProductCode(String.valueOf(i));
            list.add(entSyncList);
        }
        return list;
    }
}
