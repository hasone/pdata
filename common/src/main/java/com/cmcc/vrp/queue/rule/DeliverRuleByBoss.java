package com.cmcc.vrp.queue.rule;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.SupplierLimitStatus;
import com.cmcc.vrp.enums.SupplierStatus;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.Supplier;
import com.cmcc.vrp.province.model.SupplierProdReqUsePerDay;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierReqUsePerDay;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProdReqUsePerDayService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierReqUsePerDayService;
import com.cmcc.vrp.province.service.SupplierService;
import com.cmcc.vrp.queue.QueueRegistryCenter;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.util.Constants.FLOW_ACCOUNT_FLAG;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by sunyiwei on 2016/11/10.
 * edit by qinqinyan 
 */
public class DeliverRuleByBoss implements DeliverRule {
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
    QueueRegistryCenter queueRegistryCenter;
    @Autowired
    SupplierProdReqUsePerDayService supplierProdReqUsePerDayService;
    @Autowired
    SupplierReqUsePerDayService supplierReqUsePerDayService;
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

        //2. 根据企业ID获取关联的平台产品
        Product product = productService.get(pojo.getPrdId());
        if (product == null) {
            logger.error("产品不存在");
            return null;
        }
        
        //产品校验：如果是虚拟产品则查询其父产品
        if(FLOW_ACCOUNT_FLAG.VIRTUAL_PRODUCT.getCode().intValue() == product.getFlowAccountFlag().intValue()){//虚拟产品则查询其父产品
            product = productService.get(product.getFlowAccountProductId());
        }
        
        if (entProductService.selectByProductIDAndEnterprizeID(product.getId(), pojo.getEntId()) == null) {
            logger.error("企业-平台产品关联不存在，企业ID:{},平台产品ID:{}", pojo.getEntId(), pojo.getPrdId());
            return null;
        }

        //3. 根据企业的平台产品获取供应商产品信息
        List<SupplierProduct> splProducts = querySplProducts(product);
        if (splProducts == null
                || splProducts.size() == 0) {
            logger.error("对应的供应商产品为空. 平台产品ID为{}.", pojo.getPrdId());
            return null;
        }
        
        //4. 根据归属地、价格等因素选择最合适的供应商产品
        SupplierProduct chosenSupplierPrd = splProducts.get(0);
        if (chosenSupplierPrd == null) {
            Gson gson = new Gson();
            logger.error("没有找到可充值的供应商产品. 可选择的供应商产品信息为{}.", gson.toJson(splProducts));
            return null;
        }
        
        //5.判断供应商上下架
        Supplier supplier = supplierService.get(chosenSupplierPrd.getSupplierId());
        if(supplier == null){
            logger.error("选择的供应商产品对应的供应商为空. 平台产品ID为{}, 供应商产品ID为{}.", 
                    pojo.getPrdId(), chosenSupplierPrd.getId());
            return null;
        }
        if(supplier.getStatus().intValue()==SupplierStatus.OFF.getCode()){
            logger.error("选择的供应商产品对应的供应商. 平台产品ID为{}, 供应商产品ID为{}.", 
                    pojo.getPrdId(), chosenSupplierPrd.getId());
            return null;
        }


        pojo.setSplPrdId(chosenSupplierPrd.getId());
        Supplier spl = supplierService.get(chosenSupplierPrd.getSupplierId());

        return queueRegistryCenter.getByFingerprint(spl.getFingerprint());
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

    
    /**
     * edit by qinqinyan 2017/5/17
     * 获取供应商产品规则：
     * 1、首先是按照价格升序排列（最初分发规则只有这一条）
     * 2、然后要判断供应商和供应商产品是否限额（plus平台引入供应商和供应商限额功能，省公司兼容）
     * */
    //根据平台获取供应商产品列表，按价格升序排列
    protected List<SupplierProduct> querySplProducts(Product product) {
        if (product == null) {
            return null;
        }

        //根据平台产品获取供应商产品
        List<SupplierProduct> sps = supplierProductMapService.getOnshelfByPltfPid(product.getId());
        
        Collections.sort(sps, new Comparator<SupplierProduct>() {
            @Override
            public int compare(SupplierProduct o1, SupplierProduct o2) {
                return o1.getPrice() - o2.getPrice();
            }
        });
        
        List<SupplierProduct> returnList = new ArrayList<SupplierProduct>();
        for(SupplierProduct supplierProduct : sps){
            //1、校验供应商产品限额
            if(supplierProduct.getLimitMoneyFlag().toString()
                    .equals(SupplierLimitStatus.ON.getCode().toString())){
                SupplierProdReqUsePerDay record = 
                        supplierProdReqUsePerDayService.getTodayRecord(supplierProduct.getId());
                if(record!=null && supplierProduct.getLimitMoney().doubleValue()<=(record.getUseMoney().doubleValue()
                        +supplierProduct.getPrice().doubleValue())){
                    continue;
                }
            }
            //2、校验供应商限额
            if(supplierProduct.getSupplierLimitMoneyFlag().toString()
                    .equals(SupplierLimitStatus.ON.getCode().toString())){
                
                SupplierReqUsePerDay record = supplierReqUsePerDayService
                        .getTodayRecord(supplierProduct.getSupplierId());
                if(record!=null && supplierProduct.getSupplierLimitMoney().doubleValue()<=(record.getUsedMoney().doubleValue()
                        +supplierProduct.getPrice().doubleValue())){
                    continue;
                }
            }
            returnList.add(supplierProduct);
            //只要找到一个供应商产品，即结束循环
            break;
        }
        return returnList;
    }

}
