package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.cache.CacheService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 登录成功次数UT
 * <p>
 * Created by sunyiwei on 2016/12/1.
 */
public class LoginSuccessRateLimitServiceImplTest extends AbstractRateLimitServiceImplTest {
    @InjectMocks
    LoginSuccessRateLimitServiceImpl loginSuccessRateLimitService = new LoginSuccessRateLimitServiceImpl();

    @Mock
    GlobalConfigService globalConfigService;

    @Mock
    CacheService cacheService;

    @Override
    protected AbstractRateLimitServiceImpl getRateLimitService() {
        return loginSuccessRateLimitService;
    }

    /**
     * 获取前缀UT
     *
     * @throws Exception
     */
    @Test
    public void testGetPrefix() throws Exception {
        loginSuccessRateLimitService.getPrefix();
    }

    /**
     * 增加调用次数UT
     *
     * @throws Exception
     */
    @Test
    public void testAdd() throws Exception {
        when(globalConfigService.get(anyString()))
            .thenReturn("off").thenReturn("on").thenReturn("300").thenReturn("10");
        when(cacheService.incrOrUpdate(anyString(), anyInt()))
            .thenReturn(true);

        loginSuccessRateLimitService.add("fdfa");
        loginSuccessRateLimitService.add("fdfa");


        verify(globalConfigService, times(4)).get(anyString());
        verify(cacheService, times(1)).incrOrUpdate(anyString(), anyInt());
    }

    /**
     * 调用频率UT
     *
     * @throws Exception
     */
    @Test
    public void testAlloToContinue() throws Exception {
        final int limitCount = 10;

        when(globalConfigService.get(anyString()))
            .thenReturn("off")
            .thenAnswer(new Answer<String>() {
                int count = 0;

                @Override
                public String answer(InvocationOnMock invocation) throws Throwable {
                    int value = (count++) % 3;
                    switch (value) {
                        case 0:
                            return "on";
                        case 1:
                            return "300";
                        case 2:
                            return String.valueOf(limitCount);
                        default:
                            return null;
                    }
                }
            });


        when(cacheService.get(anyString()))
            .thenReturn(null)
            .thenReturn(String.valueOf(limitCount - 1))
            .thenReturn(String.valueOf(limitCount + 1));

        assertTrue(loginSuccessRateLimitService.allowToContinue("fdsafd"));
        assertTrue(loginSuccessRateLimitService.allowToContinue("fdsafd"));
        assertTrue(loginSuccessRateLimitService.allowToContinue("fdsafd"));

        try {
            loginSuccessRateLimitService.allowToContinue("fdsafd");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        verify(globalConfigService, times(1 + 3 * 3)).get(anyString());
        verify(cacheService, times(3)).get(anyString());
    }
}