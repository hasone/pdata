package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.SdBossProduct;

/**
 * 
 * @ClassName: SdBossProductListService 
 * @Description: 山东产品服务类
 * @author: Rowe
 * @date: 2017年8月31日 上午11:13:35
 */
public interface SdBossProductService {
    /**
     * 
     * @Title: selectByCode 
     * @Description: 根据产品编码查询
     * @param code
     * @return
     * @return: SdBossProduct
     */
    SdBossProduct selectByCode(String code);
    
    /**
     * 
     * @Title: getAllProducts 
     * @Description: 查询所有山东产品
     * @return
     * @return: List<SdBossProduct>
     */
    List<SdBossProduct> getAllProducts();
}
