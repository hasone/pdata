package com.cmcc.vrp.boss;

import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.SupplierProduct;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by leelyn on 2016/10/10.
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@ContextConfiguration("classpath:conf/applicationContext.xml")
public class BaseBossServiceTest {

    public Enterprise buildEnt() {
        Enterprise enterprise = new Enterprise();
        enterprise.setName("中移(杭州)信息技术有限公司");
        enterprise.setCode("test");
        return enterprise;
    }

    public SupplierProduct buildSP() {
        SupplierProduct product = new SupplierProduct();
        product.setCode("123456");
        return product;
    }

    @Ignore
    @Test
    public void test() {

    }
}
