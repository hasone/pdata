package com.cmcc.vrp.province.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.SdBossProductMapper;
import com.cmcc.vrp.province.model.SdBossProduct;
import com.cmcc.vrp.province.service.SdBossProductService;

/**
 * 
 * @ClassName: SdBossProductServiceImpl 
 * @Description: 山东产品服务类
 * @author: Rowe
 * @date: 2017年8月31日 上午11:20:10
 */
@Service("sdBossProductService")
public class SdBossProductServiceImpl implements SdBossProductService {

    @Autowired
    private SdBossProductMapper sdBossProductMapper;

    @Override
    public SdBossProduct selectByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return sdBossProductMapper.selectByCode(code);
    }

    @Override
    public List<SdBossProduct> getAllProducts() {
        return sdBossProductMapper.getAllProducts();
    }

}
