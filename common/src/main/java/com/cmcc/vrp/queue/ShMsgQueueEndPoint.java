package com.cmcc.vrp.queue;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by leelyn on 2016/11/16.
 */
public class ShMsgQueueEndPoint extends AbstractQueueEndPoint {

    @Autowired
    protected ChannelObjectPool shRmqChannelPool;

    @Override
    protected ChannelObjectPool getChannelPool() {
        return shRmqChannelPool;
    }
}
