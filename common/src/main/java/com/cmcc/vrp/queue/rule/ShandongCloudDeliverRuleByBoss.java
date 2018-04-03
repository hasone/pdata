package com.cmcc.vrp.queue.rule;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierService;
import com.cmcc.vrp.queue.QueueRegistryCenter;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.queue.queue.AbstractQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 山东云平台的boss分发
 * ShandongCloudDeliverRuleByBoss.java
 *
 * @author qihang
 * @date 2016年12月8日
 * 
 * edit by qinqinyan 仅查找正在上架的供应商产品
 */
public class ShandongCloudDeliverRuleByBoss implements DeliverRule {
    private static final Logger logger = LoggerFactory.getLogger(DeliverRuleByBoss.class);
    private static final String FINGERPRINT = "shandongcloud";
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

        //检查产品是否存在
        Product product = productService.get(pojo.getPrdId());
        if (product == null || product.getDeleteFlag() != 0 || product.getStatus() != 1) {
            logger.error("平台产品id:{},没有该产品", pojo.getSplPrdId());
            return null;
        }

        //product是转化过来的产品
        List<SupplierProduct> listSpIds;
        if (product.getFlowAccountFlag().equals(1)) { //是流量池产品
            //找到衍生前的产品
            Product flowAccountProduct = productService.get(product.getFlowAccountProductId());
            if (flowAccountProduct == null) {
                logger.error("getFlowAccountProductId can not found." + product.getFlowAccountProductId());
                return null;
            }
            //找到衍生前的产品对应的supplier_product
            listSpIds = supplierProductMapService.getOnshelfByPltfPid(flowAccountProduct.getId());

        } else {//是流量包产品,无衍生
            listSpIds = supplierProductMapService.getOnshelfByPltfPid(pojo.getPrdId());
        }

        //找到supplier_product
        if (listSpIds.isEmpty()) {
            logger.error("Not found supplier product");
            return null;
        }
        SupplierProduct sPrdouct = listSpIds.get(0);

        pojo.setSplPrdId(sPrdouct.getId());

        return queueRegistryCenter.getByFingerprint(FINGERPRINT);
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
