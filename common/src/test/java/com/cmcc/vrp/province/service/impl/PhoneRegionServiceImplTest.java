package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;

/**
 * Created by sunyiwei on 2016/6/16.
 */
@PrepareForTest({HttpUtils.class})
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.cmcc.vrp.util.HttpUtils")
public class PhoneRegionServiceImplTest {
    @InjectMocks
    PhoneRegionService phoneRegionService = new PhoneRegionServiceImpl();

    @Mock
    GlobalConfigService globalConfigService;


    @Test
    public void testQuery() throws Exception {
        assertNull(phoneRegionService.query(null));
        assertNull(phoneRegionService.query("188"));

        when(globalConfigService.get(GlobalConfigKeyEnum.PHONE_REGION_QUERY_URL.getKey())).thenReturn("localhost:8080");
        String response = "{\"mobile\":\"18867103571\",\"province\":\"浙江\",\"city\":\"杭州\",\"supplier\":\"M\"}";
        PowerMockito.mockStatic(HttpUtils.class);
        PowerMockito.when(HttpUtils.get("localhost:8080", build("mobile", "18867103571"))).thenReturn(response);
        assertNotNull(phoneRegionService.query("18867103571"));
    }

    private Map<String, String> build(String key, String value) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put(key, value);
        return map;
    }
}