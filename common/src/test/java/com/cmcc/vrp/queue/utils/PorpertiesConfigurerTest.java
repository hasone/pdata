package com.cmcc.vrp.queue.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leelyn on 2016/12/1.
 */
@RunWith(PowerMockRunner.class)
public class PorpertiesConfigurerTest {

    @InjectMocks
    PorpertiesConfigurer configurer = new PorpertiesConfigurer();

    @Before
    public void initMocks() {
        Map map = new HashMap();
        ReflectionTestUtils.setField(configurer, "ctxPropertiesMap", map);
    }

    @Test
    public void test() {
        Assert.assertNull(configurer.getPropertiesValue(""));
    }
}
