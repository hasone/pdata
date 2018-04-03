package com.cmcc.vrp.async.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.shanghai.model.Feature;
import com.cmcc.vrp.boss.shanghai.model.common.AsiaDTO;
import com.cmcc.vrp.boss.shanghai.model.queryproduct.ProductItem;
import com.cmcc.vrp.boss.shanghai.service.ShBossService;
import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.ec.bean.ShEnterpriseResponse;
import com.cmcc.vrp.ec.bean.ShOrderProduct;
import com.cmcc.vrp.ec.bean.ShProductReq;
import com.cmcc.vrp.ec.bean.ShProductResp;
import com.cmcc.vrp.enums.SupplierLimitStatus;
import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.ShBossProduct;
import com.cmcc.vrp.province.model.ShOrderList;
import com.cmcc.vrp.province.model.ShOrderProductMap;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductMap;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.ShOrderListService;
import com.cmcc.vrp.province.service.ShOrderProductMapService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.SizeUnits;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.thoughtworks.xstream.XStream;

/**
 * @author lgk8023
 *
 */
@Controller
@RequestMapping(value = "/shProduct")
public class ShProductInterfaceController {

    private static final Logger logger = LoggerFactory.getLogger(ShProductInterfaceController.class);
    
    @Autowired
    EnterprisesService enterprisesService;
    
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    @Qualifier("QueryUsableBalanceOfFlowServiceImpl")
    private ShBossService queryProductService;
    @Autowired
    private SupplierProductService supplierProductService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private EntProductService entProductService;

    @Autowired
    private SupplierProductMapService supplierProductMapService;
    
    @Autowired
    private ShOrderListService shOrderListService;
    
    @Autowired
    private ShOrderProductMapService shOrderProductMapService;
    
    private XStream xStream;

    @PostConstruct
    private void postConstruct() {
        xStream = new XStream();
        xStream.alias("Request", ShProductReq.class);
        xStream.alias("Response", ShEnterpriseResponse.class);
        xStream.autodetectAnnotations(true);
    }
    /**
     * @param request
     * @param response
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public void productOperation(final HttpServletRequest request, final HttpServletResponse response) {
        String appKey = null;
        Enterprise enterprise;
        if (StringUtils.isBlank(appKey = (String) request.getAttribute(Constants.APP_KEY_ATTR))
               || (enterprise = enterprisesService.selectByAppKey(appKey)) == null
               || !getEntCode().equals(enterprise.getCode())) {
            logger.error("认证未通过, AppKey = {}.", appKey);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        ShProductReq shProductReq = retrieveReq(request);
        logger.info("收到用户请求{}", JSONObject.toJSONString(shProductReq));
        ShOrderProduct shOrderProduct = null;
        if (shProductReq == null
                || (shOrderProduct = shProductReq.getOrderProduct()) == null
                || !validate(shOrderProduct)) {
            logger.error("无效的请求报文");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        enterprise = enterprisesService.selectByCode(shOrderProduct.getCustServiceId());
        if (enterprise == null) {
            logger.error("无效的请求报文,企业不存在");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        ShProductResp shProductResp = new ShProductResp();
        shProductResp.setResponseTime(new Date().toString());        
        
        ShOrderList shOrderList = buildShOrderList(shOrderProduct, enterprise);       

        AsiaDTO asiaDTO = new AsiaDTO();
        List<ProductItem> itemList;
        asiaDTO.setOfferId(shOrderProduct.getOfferId());
        asiaDTO.setRoleId(shOrderProduct.getRoleId());
        asiaDTO.setBbossInsOfferId(shOrderProduct.getBbossInsOfferId());
//        if ((itemList = queryProductService.queryProductByOfferIdAndRoleId(asiaDTO)) == null) {
//            logger.error("产品查询失败");
//            shProductResp.setCode("0001");
//            shProductResp.setMessage("本地产品查询失败！");
//            try {
//                StreamUtils.copy(xStream.toXML(shProductResp), Charsets.UTF_8, response.getOutputStream());
//            } catch (IOException e) {
//                logger.error("产品同步响应出错，错误信息为{}.", e.toString());
//            }
//            return;
//        }
        if ((itemList = getProducts(shOrderProduct.getOrderType())).isEmpty()) {
            logger.error("本地产品查询失败");
            shProductResp.setCode("0001");
            shProductResp.setMessage("本地产品查询失败！");
            try {
                StreamUtils.copy(xStream.toXML(shProductResp), Charsets.UTF_8, response.getOutputStream());
            } catch (IOException e) {
                logger.error("产品同步响应出错，错误信息为{}.", e.toString());
            }
            return;
        }
        try {
            if (!updatedb(shOrderProduct, shOrderList, asiaDTO, itemList, enterprise)) {
                logger.error("更新数据库失败");
                shProductResp.setCode("0002");
                shProductResp.setMessage("本地更新产品信息失败！");
                try {
                    StreamUtils.copy(xStream.toXML(shProductResp), Charsets.UTF_8, response.getOutputStream());
                } catch (IOException e) {
                    logger.error("产品同步响应出错，错误信息为{}.", e.toString());
                }
                return;
            }
        } catch (Exception e) {
            logger.error("建立平台产品.供应商产品.企业三者之间关系失败,抛出异常:{}", e);
            shProductResp.setCode("0003");
            shProductResp.setMessage("建立产品订购组关系失败！");
            try {
                StreamUtils.copy(xStream.toXML(shProductResp), Charsets.UTF_8, response.getOutputStream());
            } catch (IOException e1) {
                logger.error("产品同步响应出错，错误信息为{}.", e1.toString());
            }
            return;
        }
        shProductResp.setCode("0000");
        shProductResp.setMessage("success");
        try {
            StreamUtils.copy(xStream.toXML(shProductResp), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            logger.error("产品同步响应出错，错误信息为{}.", e.toString());
        }
        
    }
    @Transactional
    private boolean updatedb(ShOrderProduct shOrderProduct, ShOrderList shOrderList, AsiaDTO asiaDTO, List<ProductItem> itemList, Enterprise enterprise) {
        List<String> oldProducts = new ArrayList<String>();
        List<String> newProducts = new ArrayList<String>(); 
        List<String> middleProducts = new ArrayList<String>(); 
        List<ProductItem> newItemList = new ArrayList<ProductItem>();
        String mainBillId = shOrderProduct.getMainBillId();
        if (shOrderListService.getByMainBillId(mainBillId) != null) {
            logger.error("订购组已存在", JSONObject.toJSON(shOrderProduct));
            shOrderList = shOrderListService.getByMainBillId(mainBillId);
        } else {
            if (!shOrderListService.insert(shOrderList)) {
                logger.error("更新订购组失败", JSONObject.toJSON(shOrderProduct));
                throw new TransactionException("更新订购组失败");
            }
        }
        
        List<Product> productList = productService.getProductsByOrderListId(shOrderList.getId());
        for (Product product:productList) {
            oldProducts.add(product.getProductCode());
            middleProducts.add(product.getProductCode());
        }
        for(ProductItem productItem:itemList) {
            newProducts.add(mainBillId + "_" + productItem.getProdRate());
        }
        oldProducts.removeAll(newProducts);
        newProducts.removeAll(middleProducts);
        for(ProductItem productItem:itemList) {
            for(String newProduct:newProducts) {
                if (newProduct.equals(mainBillId + "_" + productItem.getProdRate())) {
                    newItemList.add(productItem);
                }
            }           
        }
    
        List<SupplierProduct> supplierProducts = getSupplierProducts(asiaDTO, newItemList, shOrderProduct);
        List<Product> products;
        if (!supplierProducts.isEmpty()) {
            if (!supplierProductService.batchInsertSupplierProduct(supplierProducts)) {
                logger.error("批量插入供应商产品失败");
                throw new TransactionException("批量插入供应商产品失败");
            }
            if ((products = initPlatformProduct(supplierProducts, enterprise.getId(), shOrderProduct)) == null
                    || !productService.batchInsertProduct(products)) {
                logger.error("批量插入平台产品失败");
                throw new TransactionException("批量插入平台产品失败");
            }
            if (!createPp2SpRelation(supplierProducts, products)) {
                logger.error("平台产品与供应商产品关联失败");
                throw new TransactionException("平台产品与供应商产品关联失败");
            }
            if (!createPp2OrdRelation(products, shOrderList)) {
                logger.error("平台产品与订购组关联失败");
                throw new TransactionException("平台产品与订购组关联失败");
            }

            if (!createPp2EntRelation(enterprise.getId(), products)) {
                logger.error("平台产品与企业关联失败");
                throw new TransactionException("平台产品与企业关联失败");
            }   
        }
        
        if (!oldProducts.isEmpty()
                && !deleteProduct(oldProducts)) {
            logger.error("删除平台产品失败");
            throw new TransactionException("删除平台产品失败");
        }
        return true;
    }
    
    private boolean deleteProduct(List<String> oldProducts) {
        for(String oldProductCode:oldProducts) {
            Product product = productService.selectByCode(oldProductCode);
            Long productId = product.getId();
            List<SupplierProduct> supplierProducts = supplierProductMapService.getByPltfPid(productId);
            if (!productService.delete(productId)) {
                logger.error("删除平台产品失败");
                return false;
            }
            if (!entProductService.deleteByProductID(productId)) {
                logger.error("删除企业管理产品失败");
                return false;
            }
            for(SupplierProduct supplierProduct:supplierProducts) {
                if (!supplierProductService.deleteSupplierProduct(supplierProduct.getId())) {
                    logger.error("删除指定平台产品与所有供应商产品的关联关系失败");
                    return false;
                }
            }
            
            if (!shOrderProductMapService.deleteByPrdId(productId)) {
                logger.error("删除指定平台产品与订购组的关联关系失败");
                return false;
            }
        }
        return true;
    }
    
    private boolean createPp2OrdRelation(List<Product> products, ShOrderList shOrderList) {
        List<ShOrderProductMap> shOrderProductMaps = new ArrayList<ShOrderProductMap>();
        for(Product product:products) {
            ShOrderProductMap shOrderProductMap = new ShOrderProductMap();
            shOrderProductMap.setProductId(product.getId());
            shOrderProductMap.setOrderListId(shOrderList.getId());
            shOrderProductMap.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
            shOrderProductMaps.add(shOrderProductMap);
        }
        return shOrderProductMapService.batchInsert(shOrderProductMaps);
    }
    private ShOrderList buildShOrderList(ShOrderProduct shOrderProduct, Enterprise enterprise) {
        ShOrderList shOrderList = new ShOrderList();
        shOrderList.setEnterId(enterprise.getId());
        shOrderList.setMainBillId(shOrderProduct.getMainBillId());
        shOrderList.setOrderName(shOrderProduct.getOrderName());
        shOrderList.setOfferId(shOrderProduct.getOfferId());
        shOrderList.setRoleId(shOrderProduct.getRoleId());
        shOrderList.setOrderType(shOrderProduct.getOrderType());
        shOrderList.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        shOrderList.setAccId(shOrderProduct.getAccId());
        shOrderList.setCount(NumberUtils.toDouble(shOrderProduct.getDepositedFee()));
        shOrderList.setAlertCount(NumberUtils.toDouble(getAlertCount()));
        shOrderList.setStopCount(0d);
        return shOrderList;
    }
    private List<SupplierProduct> getSupplierProducts(AsiaDTO asiaDTO, List<ProductItem> itemList, ShOrderProduct shOrderProduct) {
        List<ShBossProduct> shBossProducts = supplierProductService.getShBossProducts();
        List<SupplierProduct> supplierProducts = new ArrayList<SupplierProduct>();
        String orderType = shOrderProduct.getOrderType();
        Date time = new Date();
        logger.info("开始企业的订购组产品转化为平台的供应商产品");
        for (ProductItem item : itemList) {
            String productId;
            if (StringUtils.isNotBlank(productId = item.getProdRate())) {
                for (ShBossProduct shBossProduct:shBossProducts) {
                    if (productId.equals(String.valueOf(shBossProduct.getSupplierProductId()))) {
                        SupplierProduct supplierProduct = new SupplierProduct();
                        supplierProduct.setCode(productId);
                        supplierProduct.setCreateTime(time);
                        supplierProduct.setDeleteFlag(0);
                        
                        Feature feature = new Feature();                       
                        feature.setMainBillId(shOrderProduct.getMainBillId());
                        feature.setBbossInsOfferId(asiaDTO.getBbossInsOfferId());
                        feature.setOfferId(asiaDTO.getOfferId());
                        feature.setAcctId(shOrderProduct.getAccId());
                        supplierProduct.setFeature(JSON.toJSONString(feature));                        
                        supplierProduct.setIsp("M");
                        supplierProduct.setName(item.getProductName());
                        supplierProduct.setOwnershipRegion("上海");
                        if("01".equals(orderType)) {
                            supplierProduct.setRoamingRegion("全国");
                            supplierProduct.setSupplierId(NumberUtils.toLong(getOldSupplier()));
                        } else {
                            supplierProduct.setRoamingRegion("上海");
                            supplierProduct.setSupplierId(NumberUtils.toLong(getNewSupplier()));
                        }
                        
                        supplierProduct.setPrice(shBossProduct.getSupplierProductPrice());
                        supplierProduct.setSize(shBossProduct.getSupplierProductSize());
                        supplierProduct.setStatus(1);
                        supplierProduct.setUpdateTime(time);
                        supplierProduct.setLimitMoneyFlag(SupplierLimitStatus.OFF.getCode());
                        supplierProduct.setSupplierLimitMoneyFlag(SupplierLimitStatus.OFF.getCode());
                        supplierProducts.add(supplierProduct);
                        break;
                    }
                }
            }            
        }
        logger.info("完成企业的订购组产品转化为平台的供应商产品");
        return supplierProducts;
    }
    private List<Product> initPlatformProduct(List<SupplierProduct> supplierProducts, Long entId, ShOrderProduct shOrderProduct) {

        Date time = new Date();
        List<Product> products = new ArrayList<Product>();
        for (SupplierProduct supplierProduct : supplierProducts) {
            Integer price = supplierProduct.getPrice();
            Long size = supplierProduct.getSize();
            Product product = new Product();
            product.setProductCode(shOrderProduct.getMainBillId() + "_" + supplierProduct.getCode());
            product.setType(2);
            product.setName(supplierProduct.getName() + formatName(size));
            product.setStatus(1);
            product.setCreateTime(time);
            product.setUpdateTime(time);
            product.setDeleteFlag(0);
            product.setPrice(price);
            product.setDefaultvalue(0);
            product.setIsp("M");
            product.setOwnershipRegion(supplierProduct.getOwnershipRegion());
            product.setRoamingRegion(supplierProduct.getRoamingRegion());
            product.setProductSize(size);
            product.setFlowAccountFlag(2);
            products.add(product);
        }
        return products;
    }

    private boolean createPp2SpRelation(List<SupplierProduct> supplierProducts, List<Product> products) {
        int num = 0;
        if (supplierProducts == null
                || products == null) {
            logger.error("平台产品或供应商产品为空");
            return false;
        }
        if (!((num = supplierProducts.size()) == products.size())) {
            logger.error("平台产品与供应商产品数量不一致");
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

    private boolean createPp2EntRelation(Long entId, List<Product> products) {

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
    private ShProductReq retrieveReq(HttpServletRequest request) {
        try {
            String reqStr = (String) request.getAttribute(Constants.BODY_XML_ATTR);
            return (ShProductReq) xStream.fromXML(reqStr);
        } catch (Exception e) {
            logger.error("无效的请求参数,错误信息为{}.", e.toString());
            return null;
        }
    }
  //校验充值请求参数
    private boolean validate(ShOrderProduct shOrderProduct) {

        return StringUtils.isNotBlank(shOrderProduct.getRoleId())
                && StringUtils.isNotBlank(shOrderProduct.getOfferId())
                && StringUtils.isNotBlank(shOrderProduct.getMainBillId())
                && StringUtils.isNotBlank(shOrderProduct.getAccId())
                && StringUtils.isNotBlank(shOrderProduct.getOrderType())
                && ("01".equals(shOrderProduct.getOrderType())
                        || "02".equals(shOrderProduct.getOrderType()))
                && StringUtils.isNotBlank(shOrderProduct.getOperateType())
                && StringUtils.isNotBlank(shOrderProduct.getOrderName())
                && StringUtils.isNotBlank(shOrderProduct.getCustServiceId())
                && !(shOrderProduct.getCustServiceId().length() > 64)
                && !(shOrderProduct.getOfferId().length() > 64)
                && !(shOrderProduct.getRoleId().length() > 64)
                && !(shOrderProduct.getOrderName().length() > 64)
                && !(shOrderProduct.getMainBillId().length() > 64)
                && !(shOrderProduct.getAccId().length() > 64);
        
    }

    private List<ProductItem> getProducts(String orderType) {
        List<ShBossProduct> shBossProducts = supplierProductService.getShBossProductsByOrderType(orderType);
        List<ProductItem> productItems = new ArrayList<ProductItem>();
        for (ShBossProduct shBossProduct:shBossProducts) {
            ProductItem productItem = new ProductItem();
            productItem.setProdId(String.valueOf(shBossProduct.getSupplierProductId()));
            productItem.setProdRate(String.valueOf(shBossProduct.getSupplierProductId()));
            productItem.setProductName(shBossProduct.getSupplierProductName());
            productItems.add(productItem);
        }
        return productItems;
        
    } 
    
    public String getEntCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.SH_BOSS_ENTER_CODE.getKey());
    }
    
    private String getAlertCount() {
        return globalConfigService.get(GlobalConfigKeyEnum.SH_BOSS_ALERT_COUNT.getKey());
        //return "0";
    }
    
    private String getOldSupplier() {
        return globalConfigService.get(GlobalConfigKeyEnum.SH_OLD_SUPPLIER_ID.getKey());
    }
    private String getNewSupplier() {
        return globalConfigService.get(GlobalConfigKeyEnum.SH_NEW_SUPPLIER_ID.getKey());
    }
}
