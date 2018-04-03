package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.IndividualProductMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Title:IndividualFlowcoinPurchaseMapper
 * @Description:
 * */
public interface IndividualProductMapMapper {

    /**
     * @Title:deleteByPrimaryKey
     * @Description:
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * @Title:insert
     * @Description:
     * */
    int insert(IndividualProductMap record);

    /**
     * @Title:insertSelective
     * @Description:
     * */
    int insertSelective(IndividualProductMap record);

    /**
     * @Title:selectByPrimaryKey
     * @Description:
     * */
    IndividualProductMap selectByPrimaryKey(Long id);

    /**
     * @Title:updateByPrimaryKeySelective
     * @Description:
     * */
    int updateByPrimaryKeySelective(IndividualProductMap record);

    /**
     * @Title:updateByPrimaryKey
     * @Description:
     * */
    int updateByPrimaryKey(IndividualProductMap record);

    /**
     * @Title:batchInsert
     * @Description:
     * */
    int batchInsert(@Param("records") List<IndividualProductMap> records);

    /**
     * @Title:getByAdminIdAndProductId
     * @Description:
     * */
    IndividualProductMap getByAdminIdAndProductId(@Param("adminId") Long adminId, @Param("productId") Long productId);

    /**
     * @Title:getByAdminIdAndProductType
     * @Description:
     * */
    IndividualProductMap getByAdminIdAndProductType(@Param("adminId") Long adminId, @Param("type") Integer type);
}