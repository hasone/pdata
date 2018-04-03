package com.cmcc.vrp.province.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmcc.vrp.boss.xinjiang.response.NewResourcePoolResp;
import com.cmcc.vrp.boss.xinjiang.service.XinjiangBossService;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.EnterprisesService;

/**
 * 新疆审核最后一步相关功能 XjEntApprovalRelatedBossServiceImpl.java
 * 
 * @author qihang
 * @date 2016年12月5日
 */
public class XjEntApprovalRelatedBossServiceImpl extends
        EntApprovalRelatedBossServiceImpl {

    private static Logger LOGGER = LoggerFactory
            .getLogger(XjEntApprovalRelatedBossServiceImpl.class);

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    XinjiangBossService xinjiangBossService;

    /**
     * 新疆审核，从boss判断是否池的状态异常
     */
    @Override
    public boolean synchronizeFromBoss(Long entId) {
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);

        if (enterprise == null || enterprise.getCode() == null) {
            return false;
        }

        NewResourcePoolResp resp = xinjiangBossService
                .getResourcePoolRespNew(enterprise.getCode());
        if (!resp.isExist()) {
            LOGGER.error("企业编码{}在boss端非正常状态", enterprise.getCode());
            return false;
        }
        return true;
    }

}
