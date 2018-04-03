/**
 *
 */
package com.cmcc.vrp.queue.rule;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierService;
import com.cmcc.vrp.queue.QueueRegistryCenter;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.queue.queue.AbstractQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 新疆的boss分发
 * XinjiangDeliverRuleByBoss.java
 *
 * @author wujiamin
 * @date 2016年12月1日
 * 
 * edit by qiniqnyan 增加供应商判断
 */
public class XinjiangDeliverRuleByBoss implements DeliverRule {
    private static final Logger logger = LoggerFactory.getLogger(DeliverRuleByBoss.class);
    @Autowired
    PhoneRegionService phoneRegionService;
    @Autowired
    EntProductService entProductService;
    @Autowired
    ProductService productService;
    @Autowired
    SupplierProductMapService supplierProductMapService;
    @Autowired
    SupplierService supplierService;
    @Autowired
    private QueueRegistryCenter queueRegistryCenter;

    /**
     * deliver
     * */
    @Override
    public AbstractQueue deliver(ChargeDeliverPojo pojo) {

        //确定充值渠道，分发到相应的队列中
        //1. 字段校验
        if (!validate(pojo)) {
            logger.error("无效的充值请求参数，充值失败.");
            return null;
        }
        
        /**
         * 新疆这里很奇怪，他的平台产品id居然是它的供应商产品id，这是在kidding me？？？
         * */

        pojo.setSplPrdId(pojo.getPrdId());

        return queueRegistryCenter.getByFingerprint("xinjiang");
    }

    /**
     * 校验对象的有效性
     * */
    //校验对象的有效性
    private boolean validate(ChargeDeliverPojo taskPojo) {
        if (taskPojo == null
            || taskPojo.getPrdId() == null
            || taskPojo.getEntId() == null
            || taskPojo.getMobile() == null) {
            logger.error("无效的充值请求参数，ChargeDeliverPojo = {}.", JSONObject.toJSONString(taskPojo));
            return false;
        }

        return true;
    }
}
