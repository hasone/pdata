package com.cmcc.vrp.province.service.impl;

import com.google.gson.Gson;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.ec.bean.Constants.ProductType;
import com.cmcc.vrp.enums.ProductMaxValue;
import com.cmcc.vrp.enums.ProductStatus;
import com.cmcc.vrp.exception.ProductInitException;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.json.post.PresentRecordJson;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.province.service.VirtualProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.SizeUnits;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by leelyn on 2016/12/7.
 */
@Service
public class VirtualProductServiceImpl implements VirtualProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VirtualProductServiceImpl.class);

    @Autowired
    ProductService productService;

    @Autowired
    SupplierProductService supplierProductService;

    @Autowired
    GlobalConfigService globalConfigService;

    @Autowired
    EntProductService entProductService;

    @Autowired
    SupplierProductMapService supplierProductMapService;

    @Autowired
    Gson gson;

    @Override
    @Transactional
    public Product initProcess(Long superiorProId, String size) throws ProductInitException {
        Product supProduct = null;
        if (superiorProId == null
                || (supProduct = productService.get(superiorProId)) == null) {
            LOGGER.error("参数缺失");
            throw new ProductInitException("参数缺失");
        }

        //确保传入的是父产品, 如果传递的是虚拟产品,那么要继续获取它的父产品
        if (com.cmcc.vrp.util.Constants.FLOW_ACCOUNT_FLAG.VIRTUAL_PRODUCT.getCode().equals(supProduct.getFlowAccountFlag())) {
            supProduct = productService.get(supProduct.getFlowAccountProductId());
        }

        Integer productType = supProduct.getType();
        Integer prdSize = NumberUtils.toInt(size, 1);
        if (productType != null) {
            if (Constants.ProductType.FLOW_ACCOUNT.getValue() == productType.intValue()) {//流量池产品
                Long productSize = Long.valueOf(size);//产品大小,单位为KB
                Long maxSizeFlowMB = getMaxSizeOfVirtualFlowProduct();//单位为MB
                Long maxSizeFlowKB = SizeUnits.MB.toKB(maxSizeFlowMB);//MB转KB
                if (productSize > maxSizeFlowKB) {
                    LOGGER.error("单笔额度超过{}M", String.valueOf(maxSizeFlowMB));
                    throw new ProductInitException("单笔额度超过" + String.valueOf(maxSizeFlowMB) + "M");
                }
                //生成产品
                return generatePlatPrd(supProduct, String.valueOf(productSize));
            } else if (Constants.ProductType.FLOW_PACKAGE.getValue() == productType.intValue() || 
                    Constants.ProductType.PRE_PAY_PRODUCT.getValue() == productType.intValue()) {//流量包产品或预付费现金产品
                return supProduct;
            }else if(Constants.ProductType.VIRTUAL_COIN.getValue() == productType.intValue()){
                //虚拟币
                //超过单笔额度
                int maxCount = getGiveVirtualCoinMax();
                if(prdSize.intValue() > maxCount){
                    LOGGER.error("单笔额度超过{}个虚拟流量币", maxCount);
                    throw new ProductInitException("单笔额度超过" + maxCount + "个虚拟流量币");
                }
                
                //生成产品
                return generatePlatPrd(supProduct, size);

            } else if (productType == Constants.ProductType.MOBILE_FEE.getValue()) {
                //超过单笔额度
                if (prdSize > ProductMaxValue.HLJ_MOBILE_FEE.getValue() || prdSize <= 0) {
                    LOGGER.error("单笔额度超过{}元", ProductMaxValue.HLJ_MOBILE_FEE.getValue() / 100.00);
                    throw new ProductInitException("单笔额度超过" + String.valueOf(ProductMaxValue.HLJ_MOBILE_FEE.getValue() / 100.00) + "元");
                }
                //生成产品
                return generatePlatPrd(supProduct, size);
            } else {
                LOGGER.error("不支持该类型产品!productType = " + productType);
                throw new ProductInitException("不支持该类型产品!");
            }
        }
        LOGGER.error("该类型产品不存在!");
        throw new ProductInitException("该类型产品不存在!");
    }
    
    
    private int getGiveVirtualCoinMax(){
        String count = globalConfigService.get(GlobalConfigKeyEnum.GIVE_VIRTUAL_COIN_MAX.getKey());
        if(StringUtils.isEmpty(count)){
            return 999999;
        }
        try{
            return Integer.parseInt(count);
        }catch(Exception e){
            return 999999;
        }
    }
    

    @Override
    @Transactional
    public List<PresentRecordJson> batchInitProcess(List<PresentRecordJson> presentRecordJsons) throws ProductInitException {
        if (presentRecordJsons == null
                || presentRecordJsons.isEmpty()) {
            throw new ProductInitException("参数缺失");
        }
        List<PresentRecordJson> list = new ArrayList<PresentRecordJson>();
        for (PresentRecordJson presentRecordJson : presentRecordJsons) {
            Product p = productService.get(presentRecordJson.getPrdId());
            //20170104添加，将前端的Mb转化为KB
            String prdSize = presentRecordJson.getSize();
            if (!StringUtils.isBlank(presentRecordJson.getSize())
                    && p.getType().byteValue() == ProductType.FLOW_ACCOUNT.getValue()) {//前端传过来的产品大小单位为MB
                Long productSize = Long.valueOf(presentRecordJson.getSize());
                //MB转KB
                productSize = SizeUnits.MB.toKB(productSize);
                prdSize = String.valueOf(productSize);
            }

            p = initProcess(presentRecordJson.getPrdId(), prdSize);
            presentRecordJson.setPrdId(p.getId());
            list.add(presentRecordJson);
        }
        return list;
    }

    @Override
    @Transactional
    public List<ActivityPrize> activityInitProcess(String prizesParams) throws ProductInitException {
        List<ActivityPrize> prizes;
        if (StringUtils.isBlank(prizesParams)
                || (prizes = JSON.parseArray(prizesParams, ActivityPrize.class)) == null) {
            throw new ProductInitException("参数缺失");
        }
        List<ActivityPrize> list = new ArrayList<ActivityPrize>();
        for (ActivityPrize activityPrize : prizes) {
            //转换概率为小数
            Double probability = Double.parseDouble(activityPrize.getProbability()) / 100.0;
            activityPrize.setProbability(probability.toString());
            Product p = initProcess(activityPrize.getProductId(), String.valueOf(activityPrize.getProductSize()));
            activityPrize.setProductId(p.getId());
            list.add(activityPrize);
        }
        return list;
    }

    private Product generatePlatPrd(Product supProduct, String size) throws ProductInitException {
        //.产品已经存在
        Product sonProduct;
        if ((sonProduct = productService.getPrdBySizeAndId(Long.parseLong(size), supProduct.getId())) != null) {
            return sonProduct;
        }
        //2.产品不存在,创建平台产品,并创建关联关系
        sonProduct = initProduct(supProduct, size);
        if (createPlatformProducts(sonProduct)) {
            return sonProduct;
        }
        LOGGER.error("转换产品失败,productName=" + sonProduct.getName());
        throw new ProductInitException("转换产品失败");
    }

    private boolean createPlatformProducts(Product product) {
        if (product == null) {
            LOGGER.error("参数错误");
            return false;
        }
        return productService.insertProduct(product);
    }

    /**
     * 初始化平台产品
     */
    private Product initProduct(Product supProduct, String size) {
//        Integer type = (int) ProductType.FLOW_PACKAGE.getValue();
        Integer type = supProduct.getType();
        Product product = new Product();
        product.setFlowAccountFlag(com.cmcc.vrp.util.Constants.FLOW_ACCOUNT_FLAG.VIRTUAL_PRODUCT.getCode());//虚拟产品
        product.setType(type);
        product.setPrice(supProduct.getPrice() * Integer.parseInt(size));
        product.setName(definePlatPrdName(supProduct, size));
        product.setIsp(supProduct.getIsp());
        product.setOwnershipRegion(supProduct.getOwnershipRegion());
        product.setRoamingRegion(supProduct.getRoamingRegion());
        product.setProductCode(definePlatPrdCode(supProduct, size));
        product.setStatus(ProductStatus.NORMAL.getCode());
        product.setProductSize(Long.parseLong(size));
        product.setCreateTime(new Date());
        product.setDeleteFlag(com.cmcc.vrp.util.Constants.DELETE_FLAG.UNDELETED.getValue());
        product.setFlowAccountProductId(supProduct.getId());
        product.setConfigurableNumFlag(com.cmcc.vrp.util.Constants.CONFIGURABLE_NUM.YES.getValue());

        return product;
    }

    protected String definePlatPrdName(Product p, String size) {
        Integer type = p.getType();
        if (type == Constants.ProductType.MOBILE_FEE.getValue()) {
            double price = (Integer.parseInt(size)) / 100.00;
            return p.getName() + "-" + String.valueOf(price) + "元";
        } else if (type == Constants.ProductType.FLOW_ACCOUNT.getValue()) {
            return p.getName() + "-" + SizeUnits.KB.toMB(Long.valueOf(size)) + "M";
        } else if(type == Constants.ProductType.VIRTUAL_COIN.getValue()){
            return p.getName() + "-" + size + "个";
        }
        return "undefine";
    }

    protected String definePlatPrdCode(Product p, String size) {
        return p.getProductCode() + "-" + size;
    }

    @Override
    public Long getMaxSizeOfVirtualMobileFee() {
        try {
            String maxSizeStr = globalConfigService.get(GlobalConfigKeyEnum.MAX_SIZE_VIRTRUAL_MOBILE_FEE.getKey());
            if (StringUtils.isBlank(maxSizeStr)) {
                LOGGER.info("数据库未设置'花费赠送单笔最大值(分)',使用默认值(50000)。");
                return 50000L;
            }
            return Long.valueOf(maxSizeStr);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());

        }
        LOGGER.info("数据库设置格式错误：MAX_SIZE_VIRTRUAL_MOBILE_FEE,使用默认值(50000)。");
        return 50000L;
    }

    @Override
    public Long getMaxSizeOfVirtualFlowProduct() {
        try {
            String maxSizeStr = globalConfigService.get(GlobalConfigKeyEnum.MAX_SIZE_VIRTRUAL_FLOW_PRODUCT.getKey());
            if (StringUtils.isBlank(maxSizeStr)) {
                LOGGER.info("数据库未设置'流量池赠送单笔最大值(M)',使用默认值(1024)。");
                return 1024L;
            }
            return Long.valueOf(maxSizeStr);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());

        }
        LOGGER.info("数据库设置格式错误：MAX_SIZE_VIRTRUAL_FLOW_PRODUCT,使用默认值(1024)。");
        return 1024L;
    }
}
