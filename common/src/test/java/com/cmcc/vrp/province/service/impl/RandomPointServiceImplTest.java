package com.cmcc.vrp.province.service.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.RandomPointService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * 随机积分服务测试
 *
 * Created by sunyiwei on 2017/2/27.
 */
@RunWith(MockitoJUnitRunner.class)
public class RandomPointServiceImplTest {
    @Mock
    GlobalConfigService globalConfigService;

    @InjectMocks
    private RandomPointService randomPointService = new RandomPointServiceImpl();

    /**
     * 测试获取下一个随机积分
     *
     * 因为是简单实现,所以返回的是随机数字
     */
    @Test
    public void testNext() throws Exception {
        when(globalConfigService.get(GlobalConfigKeyEnum.MAX_POINT_PER_DAY.getKey()))
                .thenReturn("100");

        assert (randomPointService.next("18867102100") > 0);
       
        verify(globalConfigService, times(1))
                .get(GlobalConfigKeyEnum.MAX_POINT_PER_DAY.getKey());
    }
}