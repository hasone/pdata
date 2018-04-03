package com.cmcc.vrp.queue;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;

/**
 * Created by sunyiwei on 2016/12/20.
 */
public class ChannelObjectPoolTest {
    @Test
    public void testConstrctor() throws Exception {
        ChannelObjectPool cop = new ChannelObjectPool(new ChannelPooledObjectFactory(null), new GenericObjectPoolConfig());
    }
}