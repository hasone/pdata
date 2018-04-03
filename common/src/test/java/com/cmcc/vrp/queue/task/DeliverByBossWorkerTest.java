package com.cmcc.vrp.queue.task;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import com.cmcc.vrp.queue.rule.DeliverRule;

/**
 * Created by leelyn on 2016/11/20.
 */
@RunWith(PowerMockRunner.class)
public class DeliverByBossWorkerTest {

    @InjectMocks
    DeliverByBossWorker deliverByBossWorker = new DeliverByBossWorker();

    @Mock
    DeliverRule deliverRule;

    @Test
    public void test() {
        Assert.assertNotNull(deliverByBossWorker.getDeliverRule());
    }
}
