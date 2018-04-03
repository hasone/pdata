package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.ShOrderProductMap;

/**
 * @author lgk8023
 *
 */
public interface ShOrderProductMapService {

    /**
     * @param shOrderProductMap
     * @return
     */
    public boolean insert(ShOrderProductMap shOrderProductMap);
    
    public List<ShOrderProductMap> getByOrderListId(Long oriderListId);
    
    /**
     * @param shOrderProductMaps
     * @return
     */
    public boolean batchInsert(List<ShOrderProductMap> shOrderProductMaps);
    
    /**
     * @param productId
     * @return
     */
    boolean deleteByPrdId(Long productId);
    
}
