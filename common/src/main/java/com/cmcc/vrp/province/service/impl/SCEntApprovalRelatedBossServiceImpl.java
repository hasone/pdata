package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.EnterpriseUserIdService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 四川审核最后一步相关功能
 * SCEntApprovalRelatedBossServiceImpl.java
 * @author wujiamin
 * @date 2016年11月22日
 */
public class SCEntApprovalRelatedBossServiceImpl extends EntApprovalRelatedBossServiceImpl {
    private static Logger LOGGER = LoggerFactory.getLogger(SCEntApprovalRelatedBossServiceImpl.class);

    @Autowired
    EnterpriseUserIdService enterpriseUserIdService;

    @Autowired
    EnterprisesService enterprisesService;
    
    @Autowired
    AccountService accountService;
    
    @Autowired
    ProductService productService;

    //四川在审批过程中更新企业状态时，如果企业状态改为正常，需要向BOSS发出开户请求
    @Override
    public boolean synchronizeFromBoss(Long entId) {
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        Product product = productService.getCurrencyProduct();
        if(product == null || product.getId() == null){
            return false;
        }
        accountService.syncFromBoss(entId, product.getId());
        return enterpriseUserIdService.saveUserId(enterprise);
    }
}
