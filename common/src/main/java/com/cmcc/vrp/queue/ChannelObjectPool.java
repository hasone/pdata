package com.cmcc.vrp.queue;

import com.rabbitmq.client.Channel;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * RMQ中channel的连接池对象
 * <p>
 * Created by sunyiwei on 2016/7/12.
 */
public class ChannelObjectPool extends GenericObjectPool<Channel> {
    public ChannelObjectPool(PooledObjectFactory<Channel> factory, GenericObjectPoolConfig config) {
        super(factory, config);
    }
}
