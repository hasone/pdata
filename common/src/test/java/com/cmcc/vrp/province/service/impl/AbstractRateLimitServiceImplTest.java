package com.cmcc.vrp.province.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * 速率限制服务UT
 * <p>
 * Created by sunyiwei on 2016/12/1.
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractRateLimitServiceImplTest {
    protected abstract AbstractRateLimitServiceImpl getRateLimitService();

    /**
     * 获取开关UT
     *
     * @throws Exception
     */
    @Test
    public void testGetLimitSwitchKey() throws Exception {
        getRateLimitService().getLimitSwitchKey();
    }

    /**
     * 获取限制时间
     *
     * @throws Exception
     */
    @Test
    public void testGetLimitTimeRangeKey() throws Exception {
        getRateLimitService().getLimitTimeRangeKey();
    }

    /**
     * 获取限制次数
     *
     * @throws Exception
     */
    @Test
    public void testGetLimitCountKey() throws Exception {
        getRateLimitService().getLimitCountKey();
    }
}