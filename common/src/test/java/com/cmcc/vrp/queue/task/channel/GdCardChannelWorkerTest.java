package com.cmcc.vrp.queue.task.channel;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;

/**
 * 向上渠道Worker的UT
 * <p>
 * Created by sunyiwei on 2016/12/12.
 */
public class GdCardChannelWorkerTest {
    private GdCardChannelWorker gdCardChannelWorker = new GdCardChannelWorker();

    /**
     * 省渠道，不需要再继续分发
     *
     * @throws Exception
     */
    @Test
    public void testIsContinueDistribute() throws Exception {
        assertFalse(gdCardChannelWorker.isContinueDistribute());
    }

    /**
     * 选择boss
     *
     * @throws Exception
     */
    @Test
    public void testGetBossService() throws Exception {
        gdCardChannelWorker.getBossService();
    }
}