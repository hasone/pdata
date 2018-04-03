package com.cmcc.vrp.province.service.impl;

import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Created by leelyn on 2016/11/29.
 */
public class LoginFailRateLimitServiceImplTest extends AbstractRateLimitServiceImplTest {

    @InjectMocks
    LoginFailRateLimitServiceImpl loginFailRateLimitService = new LoginFailRateLimitServiceImpl();

    @Override
    protected AbstractRateLimitServiceImpl getRateLimitService() {
        return loginFailRateLimitService;
    }

    /**
     * 测试前缀
     *
     * @throws Exception
     */
    @Test
    public void testGetPrefix() throws Exception {
        loginFailRateLimitService.getPrefix();
    }
}
