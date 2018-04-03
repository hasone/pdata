package com.cmcc.vrp.queue.rule;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmcc.vrp.enums.SupplierStatus;
import com.cmcc.vrp.province.model.EntSyncList;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.Supplier;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductAccount;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EntSyncListService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProductAccountService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.province.service.SupplierService;
import com.cmcc.vrp.queue.QueueRegistryCenter;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.util.Constants.FLOW_ACCOUNT_FLAG;
import com.google.gson.Gson;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author lgk8023
 * @date 2016年12月5日 下午5:19:20
 */
public class ChongqingDeliverRuleByBoss implements DeliverRule {
    private static final Logger logger = LoggerFactory.getLogger(ChongqingDeliverRuleByBoss.class);

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
    EnterprisesService enterprisesService;
    @Autowired
    EntSyncListService entSyncListService;
    @Autowired
    SupplierProductService supplierProductService;
    @Autowired
    SupplierProductAccountService supplierProductAccountService;
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
            logger.error("企业-平台产品关联不存在，企业ID:{},平台产品ID:{}", pojo.getEntId(), product.getId());
            return null;
        }

        if ("FreeTimeFlow".equals(product.getIllustration())) {
            List<SupplierProduct> splProducts = supplierProductMapService.getOnshelfByPltfPid(product.getId());
            if (splProducts == null
                    || splProducts.size() == 0) {
                logger.error("对应的供应商产品为空. 平台产品ID为{}.", pojo.getPrdId());
                return null;
            }
            SupplierProduct chosenSupplierPrd = splProducts.get(0);
            if (chosenSupplierPrd == null) {
                Gson gson = new Gson();
                logger.error("没有找到可充值的供应商产品. 可选择的供应商产品信息为{}.", gson.toJson(splProducts));
                return null;
            }
            
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
        Long supplierProductId = chooseEntProCode(pojo.getEntId(), product.getId());
        if (supplierProductId == null) {
            logger.error("没有可供选择的供应商产品或供应商产品个数为0. 企业id{}, 产品id{}", pojo.getEntId(), product.getId());
            return null;
        }
        if (!minusCount(supplierProductId, 1.0)) {
            logger.error("扣减集团产品账户失败. 企业id{}, 产品id{}, 供应商产品id", pojo.getEntId(), product.getId(), supplierProductId);
            return null;
        }
        pojo.setSplPrdId(supplierProductId);

        return queueRegistryCenter.getByFingerprint("chongqing");
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
            logger.error("无效的充值请求参数，ChargeDeliverPojo = {}.", taskPojo);
            return false;
        }

        return true;
    }
    /**
     * chooseEntProCode
     * */
    private Long chooseEntProCode(Long ownerId, Long prdId) {
        String entCode = enterprisesService.selectById(ownerId).getCode();
        String productCode = productService.selectProductById(prdId).getProductCode();
        List<EntSyncList> entSyncLists = entSyncListService.getByEntId(ownerId);
        Date createTime = new Date();
        Long supProAccountId = null;
        for (EntSyncList entSyncList : entSyncLists) {
            String entProCode = entSyncList.getEntProductCode();
            JSONObject feature = new JSONObject();  //构建产品属性
            feature.put("productCode", productCode);
            feature.put("enterProCode", entProCode);
            feature.put("enterCode", entCode);
            SupplierProduct supplierProduct = null;
            if ((supplierProduct = supplierProductService.selectByFeature(feature.toString())) != null) {
                Long supplierProductId = supplierProduct.getId();
                SupplierProductAccount supplierProductAccount = supplierProductAccountService.getInfoBySupplierProductId(supplierProductId);
                if (createTime.after(supplierProductAccount.getCreateTime())
                        && supplierProductAccount.getCount() > 0) {
                    createTime = supplierProductAccount.getCreateTime();
                    supProAccountId = supplierProductId;
                }

            }
        }

        return supProAccountId;
    }
    /**
     * @param splPid
     * @param delta
     * @return
     */
    public boolean minusCount(Long splPid, Double delta) {
        SupplierProductAccount supplierProductAccount = null;
        if ((supplierProductAccount = supplierProductAccountService.getInfoBySupplierProductId(splPid)) == null) {
            logger.info("扣减子账户余额失败");
            return false;
        }
        Long supProAccountId = supplierProductAccount.getId();
        supplierProductAccountService.updateById(supProAccountId, -delta);              
        logger.info("扣减帐户余额信息成功.");
        return true;
    }
}
