package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.EnterprisesService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class HuNanProductServiceImpl extends ProductServiceImpl {
    @Autowired
    EnterprisesService enterprisesService;

    @Override
    public List<Product> selectAllProductsByEnterCode(String enterpriseCode) {
        Enterprise enterprise = null;
        if (StringUtils.isBlank(enterpriseCode)
            || (enterprise = enterprisesService.selectByCode(enterpriseCode)) == null) {
            return null;
        }

        return selectAllProductsByEnterId(enterprise.getId());
    }
}
