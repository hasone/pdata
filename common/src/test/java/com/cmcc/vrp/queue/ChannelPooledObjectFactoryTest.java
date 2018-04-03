package com.cmcc.vrp.queue;

import com.rabbitmq.client.impl.recovery.AutorecoveringChannel;
import org.junit.Test;

/**
 * Created by sunyiwei on 2016/12/20.
 */
public class ChannelPooledObjectFactoryTest {

    @Test
    public void testWrap() throws Exception {
        ChannelPooledObjectFactory cpof = new ChannelPooledObjectFactory(null);

        cpof.wrap(new AutorecoveringChannel(null, null));
    }
}