package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.ProductChangeDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Title:IndividualFlowcoinPurchaseMapper
 * @Description:
 * */
public interface ProductChangeDetailMapper {
    /**
     * @Title:deleteByPrimaryKey
     * @Description:
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * @Title:insert
     * @Description:
     * */
    int insert(ProductChangeDetail record);

    /**
     * @Title:insertSelective
     * @Description:
     * */
    int insertSelective(ProductChangeDetail record);
    /**
     * @Title:selectByPrimaryKey
     * @Description:
     * */
    ProductChangeDetail selectByPrimaryKey(Long id);
    /**
     * @Title:updateByPrimaryKeySelective
     * @Description:
     * */
    int updateByPrimaryKeySelective(ProductChangeDetail record);
    /**
     * @Title:updateByPrimaryKey
     * @Description:
     * */
    int updateByPrimaryKey(ProductChangeDetail record);
    /**
     * @Title:batchInsert
     * @Description:
     * */
    int batchInsert(@Param("productChangeDetails") List<ProductChangeDetail> productChangeDetails);
    /**
     * @Title:getProductChangeDetailsByRequestId
     * @Description:
     * */
    List<ProductChangeDetail> getProductChangeDetailsByRequestId(@Param("requestId") Long requestId);
}