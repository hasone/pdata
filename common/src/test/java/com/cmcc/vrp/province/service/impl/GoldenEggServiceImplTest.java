package com.cmcc.vrp.province.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.mockito.Matchers.any;

/**
 * Created by leelyn on 2016/12/15.
 */
@RunWith(PowerMockRunner.class)
public class GoldenEggServiceImplTest {

    @InjectMocks
    GoldenEggServiceImpl goldenEggService = new GoldenEggServiceImpl();

    @Test
    public void test() {
        Assert.assertNull(goldenEggService.sortActivityPrizes(any(List.class)));
    }
}
