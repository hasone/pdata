package com.cmcc.vrp.boss.shanghai;

import com.cmcc.vrp.boss.shanghai.model.common.AsiaDTO;
import com.cmcc.vrp.boss.shanghai.model.queryproduct.ProductItem;
import com.cmcc.vrp.boss.shanghai.service.ShBossService;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.AbstractProductAutoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lilin on 2016/9/11.
 */
@Service
public class ShProductAutoServiceImpl extends AbstractProductAutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShProductAutoServiceImpl.class);

    @Autowired
    @Qualifier("QueryAllGroupServiceImpl")
    private ShBossService QueryAllGroupService;

    @Autowired
    @Qualifier("QueryMemberRoleImpl")
    private ShBossService QueryMemberRoleService;

    @Autowired
    @Qualifier("QueryProductServiceIml")
    private ShBossService QueryProductService;

    @Override
    protected String getPPNamePrefix(Long entId) {
        return String.valueOf(entId) + "-" + SerialNumGenerator.buildBossReqSerialNum(4);
    }

    @Override
    protected List<SupplierProduct> getSupplierProducts(String code) {
        AsiaDTO asiaDTO;
        List<ProductItem> itemList;
        if (StringUtils.isBlank(code)
            || (asiaDTO = QueryAllGroupService.queryAllGroupOrderInfo(code)) == null
            || (asiaDTO = QueryMemberRoleService.queryMemberRoleByOfferId(asiaDTO)) == null
            || (itemList = QueryProductService.queryProductByOfferIdAndRoleId(asiaDTO)) == null) {
            LOGGER.error("查询产品的前三步中出现问题了");
            return null;
        }
        List<SupplierProduct> supplierProducts = new ArrayList<SupplierProduct>();
        Date time = new Date();
        LOGGER.info("开始企业的订购组产品转化为平台的供应商产品");
        for (ProductItem item : itemList) {
            String productId;
            // 选择定额统付流量包
            if (StringUtils.isNotBlank(productId = item.getProdRate())) {
                SupplierProduct supplierProduct = new SupplierProduct();
                supplierProduct.setCode(productId);
                supplierProduct.setCreateTime(time);
                supplierProduct.setDeleteFlag(0);
                supplierProduct.setFeature(asiaDTO.getBbossInsOfferId());
                supplierProduct.setIsp("M");
                supplierProduct.setName(item.getProductName());
                supplierProduct.setOwnershipRegion("全国");
                supplierProduct.setRoamingRegion("全国");
                supplierProduct.setSupplierId(1l);
                supplierProduct.setPrice(1);// BOSS未返回价格，统一设置为1
                supplierProduct.setSize(1l);// BOSS未返回包大小，统一设置为1
                supplierProduct.setStatus(1);
                supplierProduct.setUpdateTime(time);
                supplierProducts.add(supplierProduct);
            }
        }
        LOGGER.info("完成企业的订购组产品转化为平台的供应商产品");
        return supplierProducts;
    }

    @Override
    public String getFingerPrint() {
        return "shanghainational123456789";
    }
}
