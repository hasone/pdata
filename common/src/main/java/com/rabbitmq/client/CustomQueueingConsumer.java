package com.rabbitmq.client;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 自己实现的queueingConsumer, 支持断线重连
 *
 * Created by sunyiwei on 2017/3/9.
 */
public class CustomQueueingConsumer extends DefaultConsumer {
    private final BlockingQueue<Delivery> _queue;

    public CustomQueueingConsumer(Channel ch) {
        this(ch, new LinkedBlockingQueue<Delivery>());
    }

    public CustomQueueingConsumer(Channel ch, BlockingQueue<Delivery> q) {
        super(ch);
        this._queue = q;
    }

    @Override
    public void handleShutdownSignal(String consumerTag,
                                     ShutdownSignalException sig) {
    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {
    }

    @Override
    public void handleDelivery(String consumerTag,
                               Envelope envelope,
                               AMQP.BasicProperties properties,
                               byte[] body)
            throws IOException {
        this._queue.add(new Delivery(envelope, properties, body));
    }

    /**
     * Main application-side API: wait for the next message delivery and return it.
     *
     * @return the next message
     * @throws InterruptedException       if an interrupt is received while waiting
     * @throws ShutdownSignalException    if the connection is shut down while waiting
     * @throws ConsumerCancelledException if this consumer is cancelled while waiting
     */
    public Delivery nextDelivery()
            throws InterruptedException, ShutdownSignalException, ConsumerCancelledException {
        return _queue.take();
    }

    /**
     * Main application-side API: wait for the next message delivery and return it.
     *
     * @param timeout timeout in millisecond
     * @return the next message or null if timed out
     * @throws InterruptedException       if an interrupt is received while waiting
     * @throws ShutdownSignalException    if the connection is shut down while waiting
     * @throws ConsumerCancelledException if this consumer is cancelled while waiting
     */
    public Delivery nextDelivery(long timeout)
            throws InterruptedException, ShutdownSignalException, ConsumerCancelledException {
        return _queue.poll(timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * Encapsulates an arbitrary message - simple "bean" holder structure.
     */
    public static class Delivery {
        private final Envelope _envelope;
        private final AMQP.BasicProperties _properties;
        private final byte[] _body;

        public Delivery(Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
            _envelope = envelope;
            _properties = properties;
            _body = body;
        }

        /**
         * Retrieve the message envelope.
         *
         * @return the message envelope
         */
        public Envelope getEnvelope() {
            return _envelope;
        }

        /**
         * Retrieve the message properties.
         *
         * @return the message properties
         */
        public AMQP.BasicProperties getProperties() {
            return _properties;
        }

        /**
         * Retrieve the message body.
         *
         * @return the message body
         */
        public byte[] getBody() {
            return _body;
        }
    }
}
