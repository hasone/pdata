package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.boss.fujian.FjBossServiceImpl;
import com.cmcc.vrp.boss.henan.service.HaCacheService;
import com.cmcc.vrp.boss.zhejiang.ZjBossServiceImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by leelyn on 2016/8/4.
 */
@Ignore
public class CacheServiceImplTest {

    @Autowired
    private HaCacheService haCacheService;

    @Autowired
    private ZjBossServiceImpl bossService;

    @Autowired
    private FjBossServiceImpl fjBossService;

    @Ignore
    @Test
    public void HaCacheServiceImplTest() {
        haCacheService.getAccessToken();
    }

    @Ignore
    @Test
    public void chargeZjTest() {
        bossService.charge(0l, 0l, "18867101129", "123456789", null);
    }

    @Ignore
    @Test
    public void FJBossServiceImplTest() {
        fjBossService.charge(1l, 27l, "13600815102", "1233125642354", null);
    }
}
