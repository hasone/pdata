package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.boss.xinjiang.response.NewResourcePoolResp;
import com.cmcc.vrp.boss.xinjiang.service.XinjiangBossService;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.EnterprisesService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;

/**
 * Created by leelyn on 2016/12/15.
 */
@RunWith(PowerMockRunner.class)
public class XjEntApprovalRelatedBossServiceImplTest {

    @InjectMocks
    XjEntApprovalRelatedBossServiceImpl xjEntApprovalRelatedBossService = new XjEntApprovalRelatedBossServiceImpl();

    @Mock
    EnterprisesService enterprisesService;

    @Mock
    XinjiangBossService xinjiangBossService;

    @Test
    public void test() {
        PowerMockito.when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(null);
        Assert.assertFalse(xjEntApprovalRelatedBossService.synchronizeFromBoss(1l));

        Enterprise enterprise = new Enterprise();
        enterprise.setCode("xxxxx");
        PowerMockito.when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(enterprise);
        NewResourcePoolResp poolResp = new NewResourcePoolResp();
        poolResp.setResultCode("0");
        PowerMockito.when(xinjiangBossService.getResourcePoolRespNew(anyString())).thenReturn(poolResp);
        Assert.assertTrue(xjEntApprovalRelatedBossService.synchronizeFromBoss(1l));

        Enterprise enterprise1 = new Enterprise();
        enterprise.setCode("xxxxx");
        PowerMockito.when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(enterprise1);
        NewResourcePoolResp poolResp1 = new NewResourcePoolResp();
        poolResp.setResultCode("1");
        PowerMockito.when(xinjiangBossService.getResourcePoolRespNew(anyString())).thenReturn(poolResp1);
        Assert.assertFalse(xjEntApprovalRelatedBossService.synchronizeFromBoss(1l));
    }
}
