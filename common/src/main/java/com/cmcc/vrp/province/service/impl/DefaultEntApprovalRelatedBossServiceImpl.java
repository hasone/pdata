package com.cmcc.vrp.province.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * DefaultEntApprovalRelatedBossServiceImpl
 * */
public class DefaultEntApprovalRelatedBossServiceImpl extends EntApprovalRelatedBossServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntApprovalRelatedBossServiceImpl.class);

    @Override
    public boolean synchronizeFromBoss(Long entId) {
        return true;
    }
}
