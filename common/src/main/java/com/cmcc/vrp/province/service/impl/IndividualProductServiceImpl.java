package com.cmcc.vrp.province.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.chongqing.service.ProductQueryService;
import com.cmcc.vrp.enums.IndividualProductType;
import com.cmcc.vrp.province.dao.IndividualProductMapper;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * IndividualProductServiceImpl.java
 * @author wujiamin
 */
@Service("individualProductService")
public class IndividualProductServiceImpl implements IndividualProductService {
    @Autowired
    IndividualProductMapper mapper;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    ProductQueryService productQueryService;

    @Override
    public List<IndividualProduct> selectByDefaultValue(Integer defaultValue) {
        return mapper.selectByDefaultValue(defaultValue);
    }

    @Override
    public IndividualProduct getFlowcoinProduct() {
        return getByType(IndividualProductType.FLOW_COIN.getValue());
    }

    @Override
    public IndividualProduct getPhoneFareProduct() {
        return getByType(IndividualProductType.PHONE_FARE.getValue());
    }

    @Override
    public IndividualProduct getIndivialPointProduct() {
        return getByType(IndividualProductType.INDIVIDUAL_POINT.getValue());
    }

    private IndividualProduct getByType(int type) {
        List<IndividualProduct> products = mapper.selectByType(type);
        if (products != null && products.size() == 1) {
            return products.get(0);
        } else {
            return null;
        }
    }

    @Override
    public IndividualProduct selectByPrimaryId(Long productId) {
        if (productId == null) {
            return null;
        }
        return mapper.selectByPrimaryKey(productId);
    }

    @Override
    public List<IndividualProduct> getProductsByAdminIdAndType(Long adminId, Integer type) {
        return mapper.getProductsByAdminIdAndType(adminId, type);
    }

    @Override
    public IndividualProduct selectByProductCode(String productCode) {
        return mapper.selectByProductCode(productCode);
    }

    @Override
    public IndividualProduct getDefaultFlowProduct() {
        return getByType(IndividualProductType.DEFAULT_FLOW_PACKAGE.getValue());       
    }

    @Override
    public List<IndividualProduct> selectByType(Integer type) {
        return mapper.selectByType(type);
    }
    
    @Override
    public List<IndividualProduct> getYqxProduct(String originId, Boolean canOrder) {
        //1、筛选出云企信类型的所有产品
        List<IndividualProduct> products = mapper.selectByType(IndividualProductType.YQX_PRODUCT.getValue());
        //2、对所有产品的是否可用标识进行赋值
        
        for(IndividualProduct product : products){
            if(canOrder){//如果可以订购，重庆云企信再进行产品个数的判断
                product.setAvailable(1);           
                //重庆云企信标识
                String cqOriginId = globalConfigService.get(GlobalConfigKeyEnum.YQX_CQ_ORIGIN_ID.getKey());
                if(originId.equals(cqOriginId)){
                    String numLimit = globalConfigService.get(GlobalConfigKeyEnum.YQX_CQ_PRODUCT_NUM_LIMIT.getKey());
                    Double num = productQueryService.getProductNum(product.getProductCode());
                    if(num == null || num < Double.parseDouble(numLimit)){
                        product.setAvailable(0);
                    }
                }
            }else{//不可以订购，所有产品标志位设置成不可选
                product.setAvailable(0);
            }
        }
        
        return products;
    }
}
