package com.cmcc.vrp.util;

import com.cmcc.vrp.province.service.impl.BaseTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.LinkedHashMap;
import java.util.Map;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * httpUtils的ut
 *
 * Created by sunyiwei on 2017/5/25.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpUtils.class)
@SuppressStaticInitializationFor("com.cmcc.vrp.util.HttpUtils")
public class HttpUtilsTest extends BaseTest {
    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(HttpUtils.class);
    }

    /**
     * 测试get方法
     */
    @Test
    public void testGet() throws Exception {
        when(HttpUtils.get(Mockito.anyString()))
                .thenReturn(null)
                .thenReturn(randStr());

        assertNull(HttpUtils.get(randStr()));
        assertNotNull(HttpUtils.get(randStr()));
    }

    /**
     * 测试get2方法
     */
    @Test
    public void testGet2() throws Exception {
        when(HttpUtils.get(Mockito.anyString(), Mockito.anyMapOf(String.class, String.class), Mockito.anyMapOf(String.class, String.class)))
                .thenReturn(null)
                .thenReturn(randStr());

        assertNull(HttpUtils.get(randStr(),
                new LinkedHashMap<String, String>(),
                new LinkedHashMap<String, String>()));

        assertNotNull(HttpUtils.get(randStr(),
                new LinkedHashMap<String, String>(),
                new LinkedHashMap<String, String>()));
    }

    /**
     * 测试get3法
     */
    @Test
    public void testGet3() throws Exception {
        when(HttpUtils.get(Mockito.anyString(), Mockito.anyMapOf(String.class, String.class), Mockito.anyMapOf(String.class, String.class), Mockito.anyString()))
                .thenReturn(null)
                .thenReturn(randStr());

        assertNull(HttpUtils.get(randStr(), build(), build(), "application/text"));
        assertNotNull(HttpUtils.get(randStr(), build(), build(), "application/text"));
    }

    /**
     * 测试post方法
     */
    @Test
    public void testPost() throws Exception {
        when(HttpUtils.post(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(null).thenReturn(randStr());

        assertNull(HttpUtils.post(randStr(), randStr()));
        assertNotNull(HttpUtils.post(randStr(), randStr()));
    }

    /**
     * 测试post方法
     */
    @Test
    public void testPost2() throws Exception {
        when(HttpUtils.post(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyMapOf(String.class, String.class)))
                .thenReturn(null).thenReturn(randStr());
        assertNull(HttpUtils.post(randStr(), randStr(), "text/plain", build()));
        assertNotNull(HttpUtils.post(randStr(), randStr(), "text/plain", build()));
    }

    @Test
    public void testPost3() throws Exception {
        when(HttpUtils.post(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(null).thenReturn(randStr());

        assertNull(HttpUtils.post(randStr(), randStr()));
        assertNotNull(HttpUtils.post(randStr(), randStr()));
    }

    private Map<String, String> build() {
        final int COUNT = randInt();
        Map<String, String> map = new LinkedHashMap<String, String>();

        for (int i = 0; i < COUNT; i++) {
            map.put(randStr(), randStr());
        }

        return map;
    }
}

