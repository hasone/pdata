package com.cmcc.vrp.boss;

import com.cmcc.vrp.boss.sichuan.SCBossOperationResultImpl;
import com.cmcc.vrp.province.cache.AbstractCacheSupport;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.Supplier;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.province.service.SupplierService;
import com.cmcc.webservice.sichuan.pojo.ReturnCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * BOSS代理服务
 * <p>
 * Created by sunyiwei on 2016/6/16.
 */
public abstract class AbstractBossServiceProxyServiceImpl extends AbstractCacheSupport implements BossServiceProxyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBossServiceProxyServiceImpl.class);

    @Autowired
    SupplierProductService supplierProductService;

    @Autowired
    PhoneRegionService phoneRegionService;

    @Autowired
    EntProductService entProductService;

    @Autowired
    SupplierProductMapService supplierProductMapService;

    @Autowired
    SupplierService supplierService;

    List<BossService> bossServices;

    @Autowired
    ChargeRecordService chargeRecordService;

    @Autowired
    ProductService productService;

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("conf/applicationContext.xml");
        ZyBossServiceProxyServiceImpl service = ctx.getBean(ZyBossServiceProxyServiceImpl.class);

//        Product product = new Product();
//        product.setId(381L);

//        List<SupplierProduct> sps = service.querySplProducts(product);
//        System.out.println(new Gson().toJson(sps));

        service.chooseBossService("18867102100", 103L, 381L);
    }

    @PostConstruct
    @Override
    protected void postConstruct() {
        super.postConstruct();

        bossServices = new LinkedList<BossService>(applicationContext.getBeansOfType(BossService.class).values());
    }

    @Override
    public BossOperationResult charge(Long chargeRecordId, Long entId, Long prdId, String mobile, String serialNum) {
        //检查产品是否存在
        EntProduct entProduct = entProductService.selectByProductIDAndEnterprizeID(prdId, entId);
        Product product = productService.get(prdId);
        if (entProduct == null || product == null || product.getDeleteFlag() != 0 || product.getStatus() != 1) {
            LOGGER.error("企业id：{}，平台产品id:{},没有该产品", entId, prdId);
            return new SCBossOperationResultImpl(ReturnCode.no_product);
        }

        //获取代理的真实对象
        BossMatchResult bossMatchResult = chooseBossService(mobile, entId, prdId);
        if (bossMatchResult == null) {
            LOGGER.error("无法根据平台产品ID获取相应的充值服务, 充值失败. 平台产品Id为{}.", prdId);
            return new SCBossOperationResultImpl(ReturnCode.parameter_error);
        } else {
            LOGGER.info("选择的BOSS渠道为{},供应商产品ID为{}.", bossMatchResult.getBossService().getFingerPrint(), bossMatchResult.getSplPid());
        }

        //调用真实对象进行充值操作
        BossService bossService = bossMatchResult.getBossService();
        Long splPid = bossMatchResult.getSplPid();

        //营销卡不传递chargeRecordId，不插入chargeRecord记录
        if (chargeRecordId != null) {
            //计算价格
            Long price = product.getPrice().longValue() * entProduct.getDiscount().longValue() / 100;

            //将splPid及price插入数据库
            ChargeRecord record = new ChargeRecord();
            record.setId(chargeRecordId);
            record.setPrice(price);
            record.setSupplierProductId(splPid);

            if (!chargeRecordService.updateByPrimaryKeySelective(record)) {
                LOGGER.error("无法更新chargeRecord的price:{}和supplierProductId:{}", price, splPid);
                return new SCBossOperationResultImpl(ReturnCode.parameter_error);
            }
        }

        LOGGER.info("调用bossService充值,entId={},splPid={},mobile={},serialNum={}",entId,splPid,mobile,serialNum);
        return bossService.charge(entId, splPid, mobile, serialNum, null);
    }

    protected BossMatchResult buildBmr(SupplierProduct supplierProduct) {
        if (supplierProduct == null) {
            LOGGER.error("没有可用的供应商产品,无法进行充值.");
            return null;
        }
        Supplier supplier = supplierService.get(supplierProduct.getSupplierId());

        if (bossServices == null || bossServices.isEmpty()) {
            LOGGER.error("没有可用的BOSS服务,无法进行充值.");
            return null;
        }

        for (BossService bossService : bossServices) {
            if (supplier.getFingerprint().equals(bossService.getFingerPrint())) {
                return new BossMatchResult(bossService, supplierProduct.getId());
            }
        }

        return null;
    }

    //根据手机号归属地及供应商产品列表，获取最优的供应商产品（可充值的最便宜的供应商产品）
    protected SupplierProduct compare(PhoneRegion phoneRegion, List<SupplierProduct> supplierProducts) {
        if (supplierProducts == null || supplierProducts.isEmpty()) {
            return null;
        }

        final int COUNT = supplierProducts.size();

        //由于列表是按供应商价格升序排列，只需要从前往后遍历即可
        for (int i = 0; i < COUNT; i++) {
            SupplierProduct supplierProduct = supplierProducts.get(i);
            //只要供应商的归属地等于或者包括手机号归属地即可
            if (chargeable(phoneRegion, supplierProduct)) {
                return supplierProduct;
            }
        }

        //遍历完了
        return null;
    }

    //判断供应商产品是否可供某个手机号码充值
    private boolean chargeable(PhoneRegion phoneRegion, SupplierProduct supplierProduct) {
        String validRegion = supplierProduct.getOwnershipRegion();
        String requestRegion = phoneRegion.getProvince();

        //包含或者等于
        return "全国".equals(validRegion) || validRegion.equals(requestRegion);
    }

    //根据平台获取供应商产品列表，按价格升序排列
    protected List<SupplierProduct> querySplProducts(Product product) {
        if (product == null) {
            return null;
        }

        //根据平台产品获取供应商产品
        List<SupplierProduct> sps = supplierProductMapService.getByPltfPid(product.getId());
        Collections.sort(sps, new Comparator<SupplierProduct>() {
            @Override
            public int compare(SupplierProduct o1, SupplierProduct o2) {
                return o1.getPrice() - o2.getPrice();
            }
        });

        return sps;
    }

    protected String getKey(String mobile, Long entId, Long prdId) {
        return mobile + "." + String.valueOf(entId) + "." + String.valueOf(prdId);
    }
}
