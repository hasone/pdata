/**
 * 
 */
package com.cmcc.vrp.queue.task.channel;

import static junit.framework.TestCase.assertFalse;

import org.junit.Test;

/**
 * <p>Title:XjChannelWorkerTest </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年12月20日
 */
public class XjChannelWorkerTest {
    private XjChannelWorker xjChannelWorker = new XjChannelWorker();

    /**
     * 省渠道，不需要再继续分发
     *
     * @throws Exception
     */
    @Test
    public void testIsContinueDistribute() throws Exception {
        assertFalse(xjChannelWorker.isContinueDistribute());
    }

    /**
     * 选择boss
     *
     * @throws Exception
     */
    @Test
    public void testGetBossService() throws Exception {
	xjChannelWorker.getBossService();
    }
}
