package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.IndividualProductMapMapper;
import com.cmcc.vrp.province.model.IndividualProductMap;
import com.cmcc.vrp.province.service.IndividualProductMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("individualProductMapService")
public class IndividualProductMapServiceImpl implements IndividualProductMapService {

    @Autowired
    IndividualProductMapMapper mapper;

    @Override
    public boolean insert(IndividualProductMap map) {
        map.setCreateTime(new Date());
        map.setDeleteFlag(0);
        map.setUpdateTime(new Date());
        return mapper.insert(map) == 1;
    }

    @Override
    public boolean batchInsert(List<IndividualProductMap> records) {
        return mapper.batchInsert(records) == records.size();
    }

    @Override
    public IndividualProductMap getByAdminIdAndProductId(Long adminId,
                                                         Long productId) {
        if (adminId == null || productId == null) {
            return null;
        }
        return mapper.getByAdminIdAndProductId(adminId, productId);
    }

    @Override
    public IndividualProductMap getByAdminIdAndProductType(Long adminId, Integer type) {
        if (adminId == null || type == null) {
            return null;
        }
        return mapper.getByAdminIdAndProductType(adminId, type);
    }
}
