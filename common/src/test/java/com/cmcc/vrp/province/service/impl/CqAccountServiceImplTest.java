package com.cmcc.vrp.province.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.modules.junit4.PowerMockRunner;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年5月25日 下午5:17:24
*/
@RunWith(PowerMockRunner.class)
public class CqAccountServiceImplTest {
    @InjectMocks
    CqAccountServiceImpl aService = new CqAccountServiceImpl();
    /**
     * 
     */
    @Test
    public void testIsEmptyAccount() {
        Assert.assertTrue(aService.isEmptyAccount());
    }
}
