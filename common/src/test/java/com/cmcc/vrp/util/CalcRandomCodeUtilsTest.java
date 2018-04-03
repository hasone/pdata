package com.cmcc.vrp.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * 算术验证码UT
 * <p>
 * Created by sunyiwei on 2016/11/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class CalcRandomCodeUtilsTest {
    /**
     * 测试算术验证码
     *
     * @throws Exception
     */
    @Test
    public void testCaclRandomCode() throws Exception {
        final int COUNT = 1000;
        for (int i = 0; i < COUNT; i++) {
            CalcRandomCodeUtils crcu = CalcRandomCodeUtils.randomModel();
            crcu.getValue();
            crcu.getExpression();
        }
    }
}