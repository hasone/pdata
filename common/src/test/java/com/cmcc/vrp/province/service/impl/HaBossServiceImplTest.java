package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.henan.HaChannelBossServiceImpl;
import com.cmcc.vrp.boss.henan.service.HaQueryBossService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by leelyn on 2016/8/17.
 */
@Ignore
public class HaBossServiceImplTest {

    @Autowired
    @Qualifier("HaQueryServiceImpl")
    private HaQueryBossService haQueryService;

    @Autowired
    private HaChannelBossServiceImpl haChannelBossService;

    @Autowired
    private HaChannelBossServiceImpl bossService;

    @Ignore
    @Test
    public void queryMemberDealTest() {
        haQueryService.queryMemberDeal("18837190160");
    }

    @Ignore
    @Test
    public void queryMemberReturnTest() {
        haQueryService.queryMemberStatus("15236261926", "13bf0a9a7e1836227f1266f67");
    }

    @Ignore
    @Test
    public void queryGrpBalanceTest() {
        haQueryService.queryGrpBalance("93713620147");
    }

    @Ignore
    @Test
    public void chargeTest() {
        BossOperationResult result = bossService.charge(432l, 15l, "18837190160", "12345678900942312344", null);
        System.out.println(result);
    }

    @Ignore
    @Test
    public void chargeHananchannelTest() {
        haChannelBossService.charge(409l, 17l, "18837190160", "123456789009876542312344", null);

    }

}
