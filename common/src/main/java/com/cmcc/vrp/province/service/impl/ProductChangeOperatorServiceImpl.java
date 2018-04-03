/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.ProductChangeOperatorMapper;
import com.cmcc.vrp.province.model.ProductChangeOperator;
import com.cmcc.vrp.province.service.ProductChangeOperatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Title:ProductChangeOperatorServiceImpl </p>
 * <p>Description: 企业产品变更记录service实现类</p>
 *
 * @author xujue
 * @date 2016年7月21日
 */
@Service("productChangeOperatorService")
public class ProductChangeOperatorServiceImpl implements ProductChangeOperatorService {

    private static final Logger logger = LoggerFactory.getLogger(ProductChangeOperatorService.class);

    @Autowired
    private ProductChangeOperatorMapper productChangeOperatorMapper;

    @Override
    public boolean batchInsert(List<ProductChangeOperator> pcoList) {
        if (pcoList == null || pcoList.size() <= 0) {
            return false;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", pcoList);
        return productChangeOperatorMapper.batchInsert(map) == pcoList.size();
    }

    @Override
    public List<ProductChangeOperator> getProductChangeRecordByEntId(Long entId) {
        if (entId == null) {
            return null;
        }
        return productChangeOperatorMapper.getProductChangeRecordByEntId(entId);
    }

    @Override
    public boolean deleteProductChangeRecordByEntId(Long entId) {
        if (entId == null) {
            return false;
        }

        return productChangeOperatorMapper.deleteProductChangeRecordByEntId(entId) > 0;
    }


}
