package com.cmcc.vrp.queue.task;

import com.cmcc.vrp.province.model.DeadLetterInfo;
import com.cmcc.vrp.province.service.DeadLetterInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;

/**
 * Created by qinqinyan on 2016/11/9.
 */
@RunWith(MockitoJUnitRunner.class)
public class DeadLetterWorkerTest {
    @InjectMocks
    DeadLetterWorker deadLetterWorker = new DeadLetterWorker();
    @Mock
    DeadLetterInfoService deadLetterInfoService;

    @Test(expected = RuntimeException.class)
    public void testExec1() {
        String content = "test";
        deadLetterWorker.setTaskString(content);
        Mockito.when(deadLetterInfoService.create(any(DeadLetterInfo.class))).thenReturn(false);
        deadLetterWorker.exec();
    }

    @Test
    public void testExec2() {
        String content = "test";
        deadLetterWorker.setTaskString(content);
        Mockito.when(deadLetterInfoService.create(any(DeadLetterInfo.class))).thenReturn(true);
        deadLetterWorker.exec();
        Mockito.verify(deadLetterInfoService).create(any(DeadLetterInfo.class));
    }


}
