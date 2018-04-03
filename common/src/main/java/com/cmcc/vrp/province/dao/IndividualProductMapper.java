package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.IndividualProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Title:IndividualProductMapper
 * @Description:
 * */
public interface IndividualProductMapper {
    /**
     * @Title:deleteByPrimaryKey
     * @Description:
     * */
    int deleteByPrimaryKey(Long id);
    /**
     * @Title:insert
     * @Description:
     * */
    int insert(IndividualProduct record);
    /**
     * @Title:insertSelective
     * @Description:
     * */
    int insertSelective(IndividualProduct record);
    /**
     * @Title:selectByPrimaryKey
     * @Description:
     * */
    IndividualProduct selectByPrimaryKey(Long id);
    /**
     * @Title:updateByPrimaryKeySelective
     * @Description:
     * */
    int updateByPrimaryKeySelective(IndividualProduct record);
    /**
     * @Title:updateByPrimaryKey
     * @Description:
     * */
    int updateByPrimaryKey(IndividualProduct record);
    /**
     * @Title:selectByDefaultValue
     * @Description:
     * */
    List<IndividualProduct> selectByDefaultValue(@Param("defaultValue") Integer defaultValue);
    /**
     * @Title:selectByType
     * @Description:
     * */
    List<IndividualProduct> selectByType(Integer type);
    /**
     * @Title:getProductsByAdminIdAndType
     * @Description:
     * */
    List<IndividualProduct> getProductsByAdminIdAndType(@Param("adminId") Long adminId, @Param("type") Integer type);

    /** 
     * @Title: selectByProductCode 
     */
    IndividualProduct selectByProductCode(String productCode);
}