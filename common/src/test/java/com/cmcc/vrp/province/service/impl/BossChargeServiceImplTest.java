package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.beijing.BjBossServiceImpl;
import com.cmcc.vrp.boss.guangdong.GdBossServiceImpl;
import com.cmcc.vrp.boss.henan.HaChannelBossServiceImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by leelyn on 2016/7/11.
 */
@Ignore
public class BossChargeServiceImplTest {

    @Autowired
    private GdBossServiceImpl gdBossService;

    @Autowired
    private HaChannelBossServiceImpl haBossService;

    @Ignore
    @Test
    public void gdChargeTest() {
        BossOperationResult result = gdBossService.charge(0L, 23L, "13512727532", "123456789", null);
        System.out.println(result.getResultCode());
    }

    @Ignore
    @Test
    public void haChargeTest() {
        BossOperationResult result = haBossService.charge(0l, 0l, "18837190160", "12345678910", null);
    }

    @Ignore
    @Test
    public void bjChargeTest() {
        BjBossServiceImpl bjBossService = new BjBossServiceImpl();
        BossOperationResult result = bjBossService.charge(0L, 23L, "18801187016", "123456789", null);
    }

}
