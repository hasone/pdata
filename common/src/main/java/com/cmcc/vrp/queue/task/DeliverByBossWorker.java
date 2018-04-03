/**
 *
 */
package com.cmcc.vrp.queue.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.queue.rule.DeliverRule;

/**
 * <p>Title:deliverByBossWorker </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月11日
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeliverByBossWorker extends AbstractDeliverWorker {

    private static final Logger logger = LoggerFactory.getLogger(DeliverByBossWorker.class);

    @Autowired
    PhoneRegionService phoneRegionService;

    @Autowired
    EntProductService entProductService;

    @Autowired
    ProductService productService;

    @Autowired
    SupplierProductMapService supplierProductMapService;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    DeliverRule deliverRule;

    @Override
    protected DeliverRule getDeliverRule() {
        return deliverRule;
    }

}
