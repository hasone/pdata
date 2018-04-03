package com.cmcc.vrp.province.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.ShOrderProductMapMapper;
import com.cmcc.vrp.province.model.ShOrderProductMap;
import com.cmcc.vrp.province.service.ShOrderProductMapService;

/**
 * @author lgk8023
 *
 */
@Service
public class ShOrderProductMapServiceImpl implements ShOrderProductMapService {

    @Autowired
    ShOrderProductMapMapper shOrderProductMapMapper;
    @Override
    public boolean insert(ShOrderProductMap shOrderProductMap) {
        if (shOrderProductMap == null) {
            return false;
        }
        return shOrderProductMapMapper.insert(shOrderProductMap) == 1;
    }

    @Override
    public List<ShOrderProductMap> getByOrderListId(Long oriderListId) {
        if (oriderListId == null) {
            return null;
        }
        
        return shOrderProductMapMapper.getByOrderListId(oriderListId);
    }

    @Override
    public boolean batchInsert(List<ShOrderProductMap> shOrderProductMaps) {
        if (shOrderProductMaps == null) {
            return false;
        }
        return shOrderProductMapMapper.batchInsert(shOrderProductMaps) == shOrderProductMaps.size();
    }

    @Override
    public boolean deleteByPrdId(Long productId) {

        return shOrderProductMapMapper.deleteByPrdId(productId) > 0;
    }

}
