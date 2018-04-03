package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.boss.chongqing.CQBossServiceImpl;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * CQEntApprovalRelatedBossServiceImpl
 * */
public class CQEntApprovalRelatedBossServiceImpl extends EntApprovalRelatedBossServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(CQEntApprovalRelatedBossServiceImpl.class);

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    CQBossServiceImpl cqBossService;

    @Autowired
    SupplierProductMapService supplierProductMapService;

    @Override
    public boolean synchronizeFromBoss(Long entId) {
        LOGGER.info("start to synchronizeFromBoss");
        cqBossService.syncronizedPrdsByEnterCode(entId);
        return true;
    }
}
