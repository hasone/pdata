package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.service.EntApprovalRelatedBossService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title:EntApprovalRelatedBossServiceImpl
 * */
public class EntApprovalRelatedBossServiceImpl implements EntApprovalRelatedBossService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntApprovalRelatedBossServiceImpl.class);

    /**
     * @title:synchronizeFromBoss
     * */
    @Override
    public boolean synchronizeFromBoss(Long entId) {
        return false;
    }


}
