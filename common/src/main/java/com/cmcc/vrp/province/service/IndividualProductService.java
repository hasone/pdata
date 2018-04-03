package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.IndividualProduct;

import java.util.List;

/**
 * IndividualProductService.java
 */
public interface IndividualProductService {
    /** 
     * @Title: selectByDefaultValue 
     */
    public List<IndividualProduct> selectByDefaultValue(Integer defaultValue);

    public IndividualProduct getFlowcoinProduct();

    public IndividualProduct getPhoneFareProduct();
    
    public IndividualProduct getDefaultFlowProduct();

    /**
     * 获取个人积分产品, 产品是根据产品的type属性确定的
     *
     * @return 个人积分产品
     */
    IndividualProduct getIndivialPointProduct();

    /**
     * 根据产品主键id查询
     *
     * @author qinqinyan
     */
    IndividualProduct selectByPrimaryId(Long productId);
    
    /** 
     * @Title: selectByProductCode 
     */
    IndividualProduct selectByProductCode(String productCode);

    public List<IndividualProduct> getProductsByAdminIdAndType(Long adminId, Integer type);
    
    /** 
     * @Title: selectByType 
     */
    public List<IndividualProduct> selectByType(Integer type);

    /** 
     * 获取云企信的产品
     * @param canOrder 
     * @Title: getYqxProduct 
     */
    List<IndividualProduct> getYqxProduct(String originId, Boolean canOrder);

}
