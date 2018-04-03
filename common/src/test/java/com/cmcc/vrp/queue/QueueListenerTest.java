package com.cmcc.vrp.queue;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * queueListener的UT
 * <p>
 * Created by sunyiwei on 2016/12/1.
 */
@RunWith(MockitoJUnitRunner.class)
public class QueueListenerTest {

    @InjectMocks
    private QueueListener queueListener = new QueueListener();

    /**
     * 测试timeout时间
     *
     * @throws Exception
     */
    @Test
    @Ignore
    public void testTimeout() throws Exception {
        int timeOut = new Random().nextInt();
        queueListener.setTimeout(timeOut);
        assertTrue(queueListener.getTimeout() == timeOut);
    }
}