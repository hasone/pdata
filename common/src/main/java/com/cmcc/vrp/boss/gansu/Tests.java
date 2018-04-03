/**
 *
 */
package com.cmcc.vrp.boss.gansu;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossServiceProxyService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>Description: </p>
 *
 * @author xj
 * @date 2016年5月23日
 */
@Ignore
public class Tests {

    @Autowired
    BossServiceProxyService bossServiceProxyService;

    /**
     * 测试
     */
    @Test
    public void test() {
        BossOperationResult chargeResponse = bossServiceProxyService.charge(1L, 180L, 367L, "13993100280", "0000");

        System.out.println(chargeResponse.isSuccess());

        System.out.println(chargeResponse.getResultCode());
        System.out.println(chargeResponse.getResultDesc());

        if (chargeResponse.isSuccess()) {
            chargeResponse.getOperationResult();
        }
    }
}
