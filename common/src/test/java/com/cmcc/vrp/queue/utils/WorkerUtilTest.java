package com.cmcc.vrp.queue.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Created by leelyn on 2016/12/1.
 */
@RunWith(PowerMockRunner.class)
public class WorkerUtilTest {

    @Test
    public void test() {
        Assert.assertNotNull(WorkerUtil.getPropertyKey(""));
    }
}
