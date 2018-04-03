package com.cmcc.vrp.queue.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.queue.WorkerInfo;

/**
 * 测试各种consumer
 * <p>
 * Created by sunyiwei on 2016/11/4.
 */
@RunWith(MockitoJUnitRunner.class)
public class ConsumerTest {
    
    @Mock 
    WorkerInfo info;
    
    @InjectMocks
    Consumer consumer = new Consumer(info);


    @Test
    public void test() {
//	
//	WorkInfo info = new DeadLetterQueue();
//	Consumer consumer = new Consumer();
    }


}

