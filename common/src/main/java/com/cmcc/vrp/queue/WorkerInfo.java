package com.cmcc.vrp.queue;

import com.cmcc.vrp.queue.task.Worker;

/**
 * worker相关的信息
 * Created by sunyiwei on 2016/12/13.
 */
public interface WorkerInfo {
    //得到worker的类对象， 对于反射生成相应的worker
    Class<? extends Worker> getWorkerClass();

    //worker的数量
    int getWorkerCount();

    //监听的队列名
    String getQueueName();

    //每次获取worker对象前都会被调用
    void beforeGetWorker();

    //每次获取worker对象后都会被调用
    void afterGetWorker(Worker worker, String value);
}
