package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.ProductChangeDetailMapper;
import com.cmcc.vrp.province.model.ProductChangeDetail;
import com.cmcc.vrp.province.service.ProductChangeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productChangeDetailService")
public class ProductChangeDetailServiceImpl implements ProductChangeDetailService {
    @Autowired
    ProductChangeDetailMapper mapper;

    @Override
    public boolean batchInsert(List<ProductChangeDetail> productChangeDetails) {
        // TODO Auto-generated method stub
        if (productChangeDetails != null && productChangeDetails.size() > 0) {
            return mapper.batchInsert(productChangeDetails) == productChangeDetails.size();
        }
        return false;
    }

    @Override
    public List<ProductChangeDetail> getProductChangeDetailsByRequestId(Long requestId) {
        if (requestId == null) {
            return null;
        }
        return mapper.getProductChangeDetailsByRequestId(requestId);
    }
}
