package com.cmcc.vrp.boss;

import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.ProductService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 省公司平台的bossService选择，不将手机号码地区作为选择条件
 *
 * @author wujiamin
 */
public class ProvinceBossServiceProxyServiceImpl extends AbstractBossServiceProxyServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProvinceBossServiceProxyServiceImpl.class);
    private final String prefix = "boss.service.proxy.";

    @Autowired
    ProductService productService;

    @Autowired
    private Gson gson = new Gson();

    @Override
    public BossMatchResult chooseBossService(String mobile, Long entId, Long prdId) {
//        String key = getKey(mobile, entId, prdId);
        String value = null;
        SupplierProduct chosenSupplierPrd = null;

//        if (StringUtils.isNotBlank((value = cacheService.get(key)))) { //恭喜，缓存命中！
//            chosenSupplierPrd = gson.fromJson(value, SupplierProduct.class);
//        } 
//        else {
        //1. 根据企业ID获取关联的平台产品
        
        
        
        if (entProductService.selectByProductIDAndEnterprizeID(prdId, entId) == null) {
            LOGGER.error("企业-平台产品关联不存在，企业ID:{},平台产品ID:{}", entId, prdId);
            return null;
        }
        Product pltProduct = productService.selectProductById(prdId);

        //2. 根据企业的平台产品获取供应商产品信息
        List<SupplierProduct> splProducts = querySplProducts(pltProduct);
        if (splProducts == null || splProducts.isEmpty()) {
            LOGGER.error("对应的供应商产品为空. 平台产品ID为{}.", prdId);
            return null;
        }

        //3. 根据归属地、价格等因素选择最合适的供应商产品
        chosenSupplierPrd = splProducts.get(0);
        if (chosenSupplierPrd == null) {
            Gson gson = new Gson();
            LOGGER.error("没有找到可充值的供应商产品. 可选择的供应商产品信息为{}.", gson.toJson(splProducts));
            return null;
        }

        //4. 增加缓存
//            cacheService.add(key, gson.toJson(chosenSupplierPrd));
//        }

        //5. 根据供应商产品信息获取供应商BOSS服务
        return buildBmr(chosenSupplierPrd);
    }

    @Override
    protected String getPrefix() {
        return prefix;
    }

    @Override
    public boolean needSyncFromBoss() {
        return true;  //省公司默认是需要从BOSS同步余额，具体的实现由各省公司渠道自行决定
    }
}
