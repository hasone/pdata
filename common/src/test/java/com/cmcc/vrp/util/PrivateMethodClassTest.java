package com.cmcc.vrp.util;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;

/**
 * 使用反射测试覆盖率
 * <p>
 * Created by sunyiwei on 2016/10/15.
 */
public class PrivateMethodClassTest {
    @Test
    public void testPublicMethod() throws Exception {
        PrivateMethodClass pmc = new PrivateMethodClass();
        pmc.publicMethod();
    }

    @Test
    public void testPrivateMethodTrue() throws Exception {
        PrivateMethodClass pmc = new PrivateMethodClass();
        Method method = buildMethod(pmc);
        method.invoke(pmc, true);
    }

    /**
     * 使用reflectionTestUtils工具类测试私有方法
     *
     * @throws Exception
     */
    @Test
    public void testUsingReflectionUtils() throws Exception {
        PrivateMethodClass pmc = new PrivateMethodClass();
        ReflectionTestUtils.invokeMethod(pmc, "privateMethod", true);
    }

    @Test
    public void testPrivateMethodNull() throws Exception {
        PrivateMethodClass pmc = new PrivateMethodClass();
        Method method = buildMethod(pmc);

        method.invoke(pmc, (Boolean) null);
        method.invoke(pmc, false);
    }

    private Method buildMethod(Object obj) throws NoSuchMethodException {
        Method method =
            obj.getClass().getDeclaredMethod("privateMethod", Boolean.class);
        method.setAccessible(true);
        return method;
    }
}