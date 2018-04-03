package com.cmcc.vrp.queue;

import static org.junit.Assert.assertFalse;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.queue.pojo.ActivitiesWinPojo;
import com.cmcc.vrp.queue.pojo.ActivitySendMessagePojo;
import com.cmcc.vrp.queue.pojo.BlockPresentPojo;
import com.cmcc.vrp.queue.pojo.CallbackPojo;
import com.cmcc.vrp.queue.pojo.ChargePojo;
import com.cmcc.vrp.queue.pojo.ChargeQueryPojo;
import com.cmcc.vrp.queue.pojo.FlowcoinExchangePojo;
import com.cmcc.vrp.queue.pojo.FlowcoinPresentPojo;
import com.cmcc.vrp.queue.pojo.MdrcChargePojo;
import com.cmcc.vrp.queue.pojo.MdrcSmsChargePojo;
import com.cmcc.vrp.queue.pojo.PresentPojo;
import com.cmcc.vrp.queue.pojo.ProductOnlinePojo;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.queue.pojo.YqxChargePojo;
import com.cmcc.vrp.queue.pojo.ZwBossPojo;
import com.cmcc.vrp.queue.pojo.ZwChargePojo;
import com.cmcc.vrp.wx.beans.ExchangeChargePojo;
import com.cmcc.vrp.wx.beans.TemplateMsgPojo;

@RunWith(MockitoJUnitRunner.class)
public class TaskProducerTest {
    @InjectMocks
    private TaskProducer taskProducer = new TaskProducer();
    
    @Mock
    ChannelObjectPool rmqChannelPool;
    
    @Before
    public void setUp() throws Exception {
        //设置私有方法可见
        setValue(taskProducer, "productOnlineQueueName", "productOnlineQueue");
        setValue(taskProducer, "batchPresentQueueName", "batchPresentQueue");
        setValue(taskProducer, "deliverVerifySmsQueueName", "deliverVerifySmsQueue");
        setValue(taskProducer, "flowCardQueueName", "flowCardQueue");
        setValue(taskProducer, "lotteryQueueName", "lotteryQueue");
        setValue(taskProducer, "monthlyPresentQueueName", "monthlyPresentQueue");
        setValue(taskProducer, "deliverChargeQueueName", "deliverChargeQueue");
        setValue(taskProducer, "mdrcAsyncChargeQueueName", "mdrcAsyncChargeQueue");
        setValue(taskProducer, "asyncChargeQueryQueueName", "asyncChargeQueryQueue");
        setValue(taskProducer, "platformCallbackQueueName", "platformCallbackQueue");
        setValue(taskProducer, "activitiesWinQueueName", "activitiesWinQueue");
        setValue(taskProducer, "platformCallbackQueueName", "platformCallbackQueue");
        setValue(taskProducer, "flowcoinExchangeQueueName", "flowcoinExchangeQueue");
        setValue(taskProducer, "flowcoinPresentQueueName", "flowcoinPresentQueue");
        setValue(taskProducer, "zwChargeQueueName", "zwChargeQueue");
        setValue(taskProducer, "individualActivitiesWinQueueName", "individualActivitiesWinQueue");
        setValue(taskProducer, "wxScoreExchangeQueueName", "wxScoreExchangeQueue");
        setValue(taskProducer, "wxSendTemplateQueueName", "wxSendTemplateQueue");
        setValue(taskProducer, "yqxAsyncChargeQueueName", "yqxAsyncChargeQueue");
        setValue(taskProducer, "activitiesSendMessageQueueName", "activitiesSendMessageQueue");        
    }
    
    public static Object getValue(Object instance, String fieldName)   
            throws IllegalAccessException, NoSuchFieldException {   
        Field field = getField(instance.getClass(), fieldName);   
        // 参数值为true，禁用访问控制检查  
        field.setAccessible(true);   
        return field.get(instance);   
    }  
    public static void setValue(Object instance, String fieldName, String value)   
            throws IllegalAccessException, NoSuchFieldException {   
        Field field = getField(instance.getClass(), fieldName);   
        // 参数值为true，禁用访问控制检查  
        field.setAccessible(true);  
        field.set(instance, value); 
    }  
    
    //该方法实现根据变量名获得该变量的值  
    public static Field getField(Class thisClass, String fieldName)   
            throws NoSuchFieldException {  
        if (thisClass == null) {   
            throw new NoSuchFieldException("Error field !");  
        }
        return thisClass.getDeclaredField(fieldName);  
    }  
    
    @Test
    public void test(){
        assertFalse(taskProducer.productProductOnlineMsg(null));        
        assertFalse(taskProducer.productProductOnlineMsg(new ProductOnlinePojo()));   
        
        assertFalse(taskProducer.productPlatformCallbackMsg(null));        
        assertFalse(taskProducer.productPlatformCallbackMsg(new CallbackPojo()));   
         
        assertFalse(taskProducer.produceBatchPresentMsg(new ArrayList<BlockPresentPojo>()));   
    
        assertFalse(taskProducer.produceDeliverVerifySmsMsg(new ArrayList<SmsPojo>()));        
        assertFalse(taskProducer.produceDeliverVerifySmsMsg(new SmsPojo()));   
    
        assertFalse(taskProducer.produceAsynChargeQueryMsg(null));        
        assertFalse(taskProducer.produceAsynChargeQueryMsg(new ChargeQueryPojo()));   
         
        assertFalse(taskProducer.produceDeliverNoticeSmsMsg(new ArrayList<SmsPojo>()));        
        assertFalse(taskProducer.produceDeliverNoticeSmsMsg(new SmsPojo()));   
    
        assertFalse(taskProducer.produceFlowcardMsg(new ArrayList<PresentPojo>()));        
        assertFalse(taskProducer.produceFlowcardMsg(new PresentPojo()));   
         

        assertFalse(taskProducer.produceRedpacketMsg(new ArrayList<PresentPojo>()));        
        assertFalse(taskProducer.produceRedpacketMsg(new PresentPojo())); 
        
        assertFalse(taskProducer.produceChargeMsg(null));        
        assertFalse(taskProducer.produceChargeMsg(new ChargePojo())); 
    
        assertFalse(taskProducer.produceMdrcChargeMsg(new MdrcChargePojo())); 
        
        assertFalse(taskProducer.produceActivityWinMsg(null));        
        assertFalse(taskProducer.produceActivityWinMsg(new ActivitiesWinPojo())); 
  
        assertFalse(taskProducer.produceBatchActivityWinMsg(new ArrayList<ActivitiesWinPojo>())); 
        
        assertFalse(taskProducer.produceZwChargeMsg(null));        
        assertFalse(taskProducer.produceZwChargeMsg(new ZwChargePojo())); 
    
        assertFalse(taskProducer.produceFlowcoinExchangeMsg(null));        
        assertFalse(taskProducer.produceFlowcoinExchangeMsg(new FlowcoinExchangePojo())); 
    
        assertFalse(taskProducer.produceZwPackage(null));        
        assertFalse(taskProducer.produceZwPackage(new ZwBossPojo())); 
    
        assertFalse(taskProducer.produceFlowcoinPresentMsg(null));        
        assertFalse(taskProducer.produceFlowcoinPresentMsg(new FlowcoinPresentPojo())); 
    
        assertFalse(taskProducer.produceMdrcChargeMsg(new MdrcSmsChargePojo())); 
        
        assertFalse(taskProducer.produceWxScoreExchangeMsg(new ExchangeChargePojo()));       
        assertFalse(taskProducer.produceWxScoreExchangeMsg(null));    
        
        assertFalse(taskProducer.produceWxSendTemplateMsg(new TemplateMsgPojo()));       
        assertFalse(taskProducer.produceWxSendTemplateMsg(null));    
        
        assertFalse(taskProducer.produceBatchWxSendTemplateMsg(new ArrayList<TemplateMsgPojo>()));         
        
        assertFalse(taskProducer.produceYqxChargeMsg(new YqxChargePojo()));         
        assertFalse(taskProducer.produceYqxChargeMsg(null));     
        
        assertFalse(taskProducer.produceActivitySendMessage(new ActivitySendMessagePojo()));         
        assertFalse(taskProducer.produceActivitySendMessage(null));     
    }
}
