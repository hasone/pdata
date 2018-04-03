package com.cmcc.vrp.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * RMQ中channel连接池的工厂对象
 * <p>
 * Created by sunyiwei on 2016/7/12.
 */
public class ChannelPooledObjectFactory extends BasePooledObjectFactory<Channel> {
    Connection connection;

    public ChannelPooledObjectFactory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Channel create() throws Exception {
        Channel channel = connection.createChannel();
        channel.basicQos(1);

        return channel;
    }

    @Override
    public PooledObject<Channel> wrap(Channel obj) {
        return new DefaultPooledObject<Channel>(obj);
    }

    @Override
    public void destroyObject(PooledObject<Channel> p) throws Exception {
        p.getObject().close();
    }
}
