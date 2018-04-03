package com.cmcc.vrp.province.service;

import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductMap;
import com.cmcc.vrp.util.SizeUnits;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lilin on 2016/9/11.
 */
public abstract class AbstractProductAutoService implements ProductAutoService {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AbstractProductAutoService.class);

    @Autowired
    private EnterprisesService enterprisesService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SupplierProductService supplierProductService;

    @Autowired
    private EntProductService entProductService;

    @Autowired
    private SupplierProductMapService supplierProductMapService;

    protected abstract String getPPNamePrefix(Long entId);

    protected abstract List<SupplierProduct> getSupplierProducts(String code);


    @Override
    @Transactional
    public boolean autoCreateRelation(Long entId) {
        Enterprise enterprise;
        String code = null;
        List<SupplierProduct> supplierProducts;
        List<Product> products;
        try {
            if (entId == null
                    || (enterprise = enterprisesService.selectByPrimaryKey(entId)) == null
                    || StringUtils.isBlank(code = enterprise.getCode())
                    || (supplierProducts = getSupplierProducts(code)) == null) {
                LOGGER.error("获取企业在供应商侧的产品为空，企业编码:{}", code);
                return false;
            }
            if (!supplierProductService.batchInsertSupplierProduct(supplierProducts)) {
                LOGGER.error("批量插入供应商产品失败");
                throw new TransactionException("批量插入供应商产品失败");
            }
            Date supProCreateTime = supplierProducts.get(0).getCreateTime();
            if ((products = initPlatformProduct(supProCreateTime, enterprise.getId())) == null
                    || !productService.batchInsertProduct(products)) {
                LOGGER.error("批量插入平台产品失败");
                throw new TransactionException("批量插入平台产品失败");
            }
            Date platProCreateTime = products.get(0).getCreateTime();
            if (!createPp2SpRelation(supProCreateTime, platProCreateTime)) {
                LOGGER.error("平台产品与供应商产品关联失败");
                throw new TransactionException("平台产品与供应商产品关联失败");
            }

            if (!createPp2EntRelation(entId, platProCreateTime)) {
                LOGGER.error("平台产品与企业关联失败");
                throw new TransactionException("平台产品与企业关联失败");
            }
        } catch (Exception e) {
            LOGGER.error("建立平台产品.供应商产品.企业三者之间关系失败,抛出异常:{}", e.getMessage());
            return false;
        }
        return true;
    }

    private List<Product> initPlatformProduct(Date supProCreateTime, Long entId) {
        List<SupplierProduct> supplierProducts;
        if ((supplierProducts = supplierProductService.selectSupplierByCreateTime(supProCreateTime)) == null) {
            LOGGER.error("关联查找供应商产品为空");
            return null;
        }
        String prefix = getPPNamePrefix(entId);
        Date time = new Date();
        List<Product> products = new ArrayList<Product>();
        int i = 1;
        for (SupplierProduct supplierProduct : supplierProducts) {
            Integer price = supplierProduct.getSupplierProductPrice();
            Long size = supplierProduct.getSupplierProductSize();
            Product product = new Product();
            product.setProductCode(prefix + "-" + i);
            product.setType(2);
            product.setName(supplierProduct.getName() + formatName(size));
            product.setStatus(1);
            product.setCreateTime(time);
            product.setUpdateTime(time);
            product.setDeleteFlag(0);
            product.setPrice(price);
            product.setDefaultvalue(0);
            product.setIsp("M");
            product.setOwnershipRegion("全国");
            product.setRoamingRegion("全国");
            product.setProductSize(size);
            product.setFlowAccountFlag(2);
            products.add(product);
            i++;
        }
        return products;
    }

    private boolean createPp2SpRelation(Date supProCreateTime, Date platProCreateTime) {
        int num = 0;
        List<SupplierProduct> supplierProducts;
        List<Product> products;
        if (supProCreateTime == null
                || platProCreateTime == null
                || (supplierProducts = supplierProductService.selectSupplierByCreateTime(supProCreateTime)) == null
                || (products = productService.selectProductByCreateTime(platProCreateTime)) == null) {
            LOGGER.error("平台产品或供应商产品为空");
            return false;
        }
        if (!((num = supplierProducts.size()) == products.size())) {
            LOGGER.error("平台产品与供应商产品数量不一致");
            return false;
        }
        List<SupplierProductMap> list = new ArrayList<SupplierProductMap>();
        Date time = new Date();
        for (int i = 0; i < num; i++) {
            SupplierProductMap map = new SupplierProductMap();
            map.setPlatformProductId(products.get(i).getId());
            map.setSupplierProductId(supplierProducts.get(i).getId());
            map.setCreateTime(time);
            map.setUpdateTime(time);
            map.setDeleteFlag(0);
            list.add(map);
        }
        return supplierProductMapService.batchInsertMap(list);
    }

    private boolean createPp2EntRelation(Long entId, Date platProCreateTime) {
        List<Product> products;
        if (entId == null || (products = productService.selectProductByCreateTime(platProCreateTime)) == null) {
            LOGGER.error("关联企业与平台产品失败,企业id:{}", entId);
            return false;
        }
        Date time = new Date();
        List<EntProduct> list = new ArrayList<EntProduct>();
        for (Product product : products) {
            EntProduct entProduct = new EntProduct();
            entProduct.setEnterprizeId(entId);
            entProduct.setDeleteFlag(0);
            entProduct.setCreateTime(time);
            entProduct.setUpdateTime(time);
            entProduct.setDiscount(100);
            entProduct.setProductId(product.getId());
            list.add(entProduct);
        }
        return entProductService.batchInsertEntProduct(list);
    }

    private String formatName(Long size) {
        return "(" + SizeUnits.KB.toMB(size) + "M)";
    }
}
