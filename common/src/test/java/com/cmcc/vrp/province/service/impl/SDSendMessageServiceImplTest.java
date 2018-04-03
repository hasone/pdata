package com.cmcc.vrp.province.service.impl;



import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.shangdong.boss.model.SmsIctParam;
import com.cmcc.vrp.boss.shangdong.boss.service.SdCloudWebserviceImpl;
import com.cmcc.vrp.province.model.SmsRecord;
import com.cmcc.vrp.province.service.SmsRecordService;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.sms.SendMessageService;
import com.cmcc.vrp.sms.shandong.SDSendMessageServiceImpl;
@RunWith(MockitoJUnitRunner.class)
public class SDSendMessageServiceImplTest {
    
    @InjectMocks
    SendMessageService sendMessageService = new SDSendMessageServiceImpl();
    
    @Mock
    SdCloudWebserviceImpl SdCloudWebservice;
    
    @Mock
    SmsRecordService smsRecordService;
    
    @Test
    public void testSend(){
        SmsPojo smsPojo = new SmsPojo();
        smsPojo.setContent("123456");
        SmsRecord smsRecord = new SmsRecord();
        SmsIctParam smsIctParam = new SmsIctParam();
        smsIctParam.setContent("123456");
        smsIctParam.setMobs("123456789");
        Assert.assertFalse(sendMessageService.send(null));
        Mockito.when(smsRecordService.insert(smsRecord)).thenReturn(false);
        Mockito.when(SdCloudWebservice.sendMessage(smsIctParam)).thenReturn(false);
        Assert.assertFalse(sendMessageService.send(smsPojo));
        
    }
}
