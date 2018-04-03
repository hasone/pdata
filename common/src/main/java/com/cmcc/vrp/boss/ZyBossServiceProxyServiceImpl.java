package com.cmcc.vrp.boss;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.ProductService;
import com.google.gson.Gson;

/**
 * 自营BOSSService选择
 *
 * @author wujiamin
 */
public class ZyBossServiceProxyServiceImpl extends AbstractBossServiceProxyServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZyBossServiceProxyServiceImpl.class);
    private final String prefix = "zy.boss.proxy.";
    @Autowired
    ProductService productService;

    //根据手机号，企业ID以及平台产品ID获取相应的供应商
    @Override
    public BossMatchResult chooseBossService(String mobile, Long entId, Long prdId) {
        SupplierProduct chosedSupplierPrd = null;

//        String key = getKey(mobile, entId, prdId);
        String value = null;
//        if (StringUtils.isNotBlank(value = cacheService.get(key))) {
//            chosedSupplierPrd = new Gson().fromJson(value, SupplierProduct.class);
//        } else {
        //1. 根据手机号码获取归属地信息
        PhoneRegion phoneRegion = phoneRegionService.query(mobile);
        if (phoneRegion == null) {
            LOGGER.error("无法获取手机号{}的归属地信息.", mobile);
            return null;
        }

        //2. 根据企业ID获取关联的平台产品
        if (entProductService.selectByProductIDAndEnterprizeID(prdId, entId) == null) {
            LOGGER.error("企业-平台产品关联不存在，企业ID:{},平台产品ID:{}", entId, prdId);
            return null;
        }
        Product pltProduct = productService.selectProductById(prdId);

        //3. 根据企业的平台产品获取供应商产品信息
        List<SupplierProduct> splProducts = querySplProducts(pltProduct);
        if (splProducts == null) {
            LOGGER.error("对应的供应商产品为空. 平台产品ID为{}.", prdId);
            return null;
        }

        //4. 根据归属地、价格等因素选择最合适的供应商产品
        chosedSupplierPrd = compare(phoneRegion, splProducts);
        if (chosedSupplierPrd == null) {
            Gson gson = new Gson();
            LOGGER.error("没有找到可充值的供应商产品. 手机号归属信息为 {}, 可选择的供应商产品信息为{}.", gson.toJson(phoneRegion), gson.toJson(splProducts));
            return null;
        }

        //设置缓存
//            cacheService.add(key, new Gson().toJson(chosedSupplierPrd));
//        }

        //5. 根据供应商产品信息获取供应商BOSS服务
        return buildBmr(chosedSupplierPrd);
    }

    @Override
    protected String getPrefix() {
        return prefix;
    }

    @Override
    public boolean needSyncFromBoss() {
        return false;  //自营平台不需要从BOSS侧同步余额
    }
}
