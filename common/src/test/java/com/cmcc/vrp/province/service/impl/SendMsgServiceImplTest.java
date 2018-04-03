package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.enums.SmsType;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SendMsgService;
import com.cmcc.vrp.province.sms.login.SmsRedisListener;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.SmsPojo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

/**
 * Created by qinqinyan on 2016/11/3.
 */
@RunWith(MockitoJUnitRunner.class)
public class SendMsgServiceImplTest {
    @InjectMocks
    SendMsgService sendMsgService = new SendMsgServiceImpl();
    @Mock
    SmsRedisListener smsRedisListener;
    @Mock
    GlobalConfigService globalConfigService;
    @Mock
    TaskProducer taskProducer;

    @Test
    public void testSendRandomCode(){
        String mobile = "18867103691";
        SmsType smsType = SmsType.FLOWCARD_SMS;

        Mockito.when(smsRedisListener.setNewPass(anyString(), anyString(), any(SmsType.class))).thenReturn(false);
        assertEquals("请勿在1分钟之内重复发送" ,sendMsgService.sendRandomCode(mobile, smsType));
        Mockito.verify(smsRedisListener).setNewPass(anyString(), anyString(), any(SmsType.class));
    }

    @Test
    public void testSendRandomCode1(){
        String mobile = "18867103691";
        SmsType smsType = SmsType.FLOWCARD_SMS;

        Mockito.when(smsRedisListener.setNewPass(anyString(), anyString(), any(SmsType.class))).thenReturn(true);
        Mockito.when(globalConfigService.get("SENDMESSAGE_CHECK")).thenReturn("OK");
        Mockito.when(taskProducer.produceDeliverVerifySmsMsg(any(SmsPojo.class))).thenReturn(true);

        assertNull(sendMsgService.sendRandomCode(mobile, smsType));

        Mockito.verify(smsRedisListener).setNewPass(anyString(), anyString(), any(SmsType.class));
        Mockito.verify(globalConfigService).get("SENDMESSAGE_CHECK");
        Mockito.verify(taskProducer).produceDeliverVerifySmsMsg(any(SmsPojo.class));
    }
    
    @Test
    public void testSendRandomCode2(){
        String mobile = "18867103691";
        SmsType smsType = SmsType.FLOWCARD_SMS;

        Mockito.when(smsRedisListener.setNewPass(anyString(), anyString(), any(SmsType.class))).thenReturn(true);
        Mockito.when(globalConfigService.get("SENDMESSAGE_CHECK")).thenReturn("OK").thenReturn("NO");
        Mockito.when(taskProducer.produceDeliverVerifySmsMsg(any(SmsPojo.class))).thenThrow(RuntimeException.class);
        assertEquals("连接短信网关失败" ,sendMsgService.sendRandomCode(mobile, smsType));
        assertNull(sendMsgService.sendRandomCode(mobile, smsType));
        Mockito.verify(smsRedisListener, Mockito.times(2)).setNewPass(anyString(), anyString(), any(SmsType.class));
    }


    @Test
    public void testSendVerifyCode(){
        String mobile = "18867103691";
        String content = null;
        assertFalse(sendMsgService.sendVerifyCode(null, content));
        assertFalse(sendMsgService.sendVerifyCode(mobile, content));

        String content1 = "test";
        Mockito.when(taskProducer.produceDeliverVerifySmsMsg(any(SmsPojo.class))).thenReturn(false).thenReturn(true);
        assertFalse(sendMsgService.sendVerifyCode(mobile, content1));
        assertTrue(sendMsgService.sendVerifyCode(mobile, content1));
        Mockito.verify(taskProducer, Mockito.times(2)).produceDeliverVerifySmsMsg(any(SmsPojo.class));
    }

    @Test
    public void testSendMessage(){
        String mobile = "18867103691";
        String content = null;
        assertFalse(sendMsgService.sendMessage(null, content, null));
        assertFalse(sendMsgService.sendMessage(mobile, content, null));

        String content1 = "test";
        String messageType = "test";
        Mockito.when(taskProducer.produceDeliverNoticeSmsMsg(any(SmsPojo.class))).thenReturn(false).thenReturn(true);
        assertFalse(sendMsgService.sendMessage(mobile, content1, messageType));
        assertTrue(sendMsgService.sendMessage(mobile, content1, messageType));
        Mockito.verify(taskProducer, Mockito.times(2)).produceDeliverNoticeSmsMsg(any(SmsPojo.class));
    }




}
