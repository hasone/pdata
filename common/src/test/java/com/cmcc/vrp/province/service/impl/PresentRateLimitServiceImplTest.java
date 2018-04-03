package com.cmcc.vrp.province.service.impl;

import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * 批量赠送的频率限制
 * <p>
 * Created by sunyiwei on 2016/12/1.
 */
public class PresentRateLimitServiceImplTest extends AbstractRateLimitServiceImplTest {
    @InjectMocks
    private PresentRateLimitServiceImpl presentRateLimitService = new PresentRateLimitServiceImpl();

    @Override
    protected AbstractRateLimitServiceImpl getRateLimitService() {
        return presentRateLimitService;
    }

    /**
     * 测试获取前缀
     *
     * @throws Exception
     */
    @Test
    public void testGetPrefix() throws Exception {
        presentRateLimitService.getPrefix();
    }
}