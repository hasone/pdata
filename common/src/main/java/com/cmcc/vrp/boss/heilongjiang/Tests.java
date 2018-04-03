package com.cmcc.vrp.boss.heilongjiang;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossServiceProxyService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>Description: </p>
 *
 * @author qh
 * @date 2016年6月24日
 */
@Ignore
public class Tests {
    @Autowired
    BossServiceProxyService bossServiceProxyService;

    /**
     * 
     */
    @Test
    public void test() {
        //该测试是在萧山机房32.45sichuan 库进行的
        BossOperationResult chargeResponse = bossServiceProxyService.charge(1L, 226L, 346L, "13993100280", "0000");

        if (chargeResponse.isSuccess()) {
            chargeResponse.getOperationResult();
        }
    }
}
