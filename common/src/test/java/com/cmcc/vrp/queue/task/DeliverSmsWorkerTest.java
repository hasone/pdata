package com.cmcc.vrp.queue.task;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.province.dao.SmsRecordMapper;
import com.cmcc.vrp.province.model.SmsRecord;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.sms.SendMessageService;
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
public class DeliverSmsWorkerTest {
    @InjectMocks
    DeliverSmsWorker deliverSmsWorker = new DeliverSmsWorker();
    @Mock
    SendMessageService sendMessageService;
    @Mock
    SmsRecordMapper smsRecordMapper;

    @Test
    public void testExec() {
        SmsPojo smsPojo = createSmsPojo();
        String jsonStr = JSON.toJSONString(smsPojo);
        deliverSmsWorker.setTaskString(jsonStr);

        Mockito.when(sendMessageService.send(any(SmsPojo.class))).thenReturn(true);
        Mockito.when(smsRecordMapper.insert(any(SmsRecord.class))).thenReturn(1);
        deliverSmsWorker.exec();
        Mockito.verify(sendMessageService).send(any(SmsPojo.class));
        Mockito.verify(smsRecordMapper).insert(any(SmsRecord.class));
    }

    private SmsPojo createSmsPojo() {
        SmsPojo smsPojo = new SmsPojo();
        smsPojo.setContent("test");
        smsPojo.setMobile("18867101111");
        return smsPojo;
    }


}
