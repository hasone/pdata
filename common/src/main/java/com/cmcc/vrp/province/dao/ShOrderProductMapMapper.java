package com.cmcc.vrp.province.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.ShOrderProductMap;

/**
 * @author lgk8023
 *
 */
public interface ShOrderProductMapMapper {

    /**
     * @param shOrderProductMap
     * @return
     */
    public int insert(ShOrderProductMap shOrderProductMap);
    
    public List<ShOrderProductMap> getByOrderListId(@Param("oriderListId") Long oriderListId);
    
    /**
     * @param shOrderProductMaps
     * @return
     */
    public int batchInsert(@Param("list")List<ShOrderProductMap> shOrderProductMaps);

    /**
     * @param productId
     * @return
     */
    public int deleteByPrdId(@Param("productId") Long productId);

}
