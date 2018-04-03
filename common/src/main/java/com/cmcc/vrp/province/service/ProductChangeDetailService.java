package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.ProductChangeDetail;

import java.util.List;

/**
 * ProductChangeDetailService.java
 */
public interface ProductChangeDetailService {

    /** 
     * @Title: batchInsert 
     */
    boolean batchInsert(List<ProductChangeDetail> productChangeDetails);

    List<ProductChangeDetail> getProductChangeDetailsByRequestId(Long requestId);

}
