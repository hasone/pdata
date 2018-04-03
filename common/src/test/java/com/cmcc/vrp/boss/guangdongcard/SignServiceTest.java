package com.cmcc.vrp.boss.guangdongcard;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;


/**
 * 广东流量卡接口签名算法
 * <p>
 * Created by sunyiwei on 2016/11/18.
 */
public class SignServiceTest {
    /**
     * 签名算法
     *
     * @throws Exception
     */
    @Test
    public void testSign() throws Exception {
        final String pub = "04fec828-081c-4e3a-bdd2-98078e1ac917";
        final String prv = "cd4cce4a-2088-4f7f-a1c1-38813e7b7ba3";
        final String tms = "2016-11-21 15:09:13 0108";

        final String expected = "2C69823FD753759561E04635554A84DB";
        String actual = SignService.sign(pub, prv, tms);
        assertTrue(actual.equals(expected));
    }
}