/**
 * 
 */
package com.cmcc.vrp.queue.task.channel;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * <p>Title:SxFeeChannelWorkerTest </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年12月20日
 */
public class SxFeeChannelWorkerTest {
    private SxFeeChannelWorker sxFeeChannelWorker = new SxFeeChannelWorker();

    /**
     * 省渠道，不需要再继续分发
     *
     * @throws Exception
     */
    @Test
    public void testIsContinueDistribute() throws Exception {
        assertFalse(sxFeeChannelWorker.isContinueDistribute());
    }

    /**
     * 选择boss
     *
     * @throws Exception
     */
    @Test
    public void testGetBossService() throws Exception {
	sxFeeChannelWorker.getBossService();
    }
}
