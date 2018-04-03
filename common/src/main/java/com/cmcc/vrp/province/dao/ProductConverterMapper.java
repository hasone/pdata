package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.ProductConverter;

/**
 * ProductConverterMapper
 * @author qihang
 *
 */
public interface ProductConverterMapper {   
    /**
     * deleteByPrimaryKey
     */
    int deleteByPrimaryKey(Long id);

    /**
     * insert
     */
    int insert(ProductConverter record);

    /**
     * insertSelective
     */
    int insertSelective(ProductConverter record);
    
    /**
     * batchInsert
     */
    int batchInsert(@Param("records") List<ProductConverter> records);

    /**
     * selectByPrimaryKey
     */
    ProductConverter selectByPrimaryKey(Long id);

    /**
     * selectByPrimaryKey
     */
    int updateByPrimaryKeySelective(ProductConverter record);

    /**
     * updateByPrimaryKey
     */
    int updateByPrimaryKey(ProductConverter record);
    
    /**
     * delete
     */
    int delete(Long id);
    
    /**
     * batchDelete
     */
    int batchDelete(@Param("records") List<ProductConverter> records);
    
    /**
     * getAll
     */
    List<ProductConverter> getAll();
    
    /**
     * getBySrcDestId
     */
    List<ProductConverter> getBySrcDestId(@Param("sourcePrdId") Long sourcePrdId , 
            @Param("destPrdId") Long destPrdId);
    
    /**
     * count
     */
    int count(Map<String, Object> map);
    
    /**
     * page
     */
    List<ProductConverter> page(Map<String, Object> map);
}