package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.CallbackPojo;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.CallbackForIndividualService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ActivitiesWinPojo;

/**
 * Created by qinqinyan on 2016/11/7.
 */
@RunWith(MockitoJUnitRunner.class)
public class CallbackForIndividualServiceImplTest {
    @InjectMocks
    CallbackForIndividualService callbackForIndividualService =
            new CallbackForIndividualServiceImpl();
    @Mock
    ActivitiesService activitiesService;
    @Mock
    AdministerService administerService;
    @Mock
    ActivityWinRecordService activityWinRecordService;
    @Mock
    IndividualAccountService individualAccountService;
    @Mock
    ChargeRecordService chargeRecordService;
    @Mock
    ActivityInfoService activityInfoService;
    @Mock
    IndividualProductService individualProductService;
    @Mock
    ActivityPrizeService activityPrizeService;
    @Mock
    TaskProducer producer;

    @Test
    public void testCallback0(){
        String serialNum = "test";
        CallbackPojo pojo = new CallbackPojo();
        pojo.setMobile("18867101111");
        pojo.setActiveId("12345");
        pojo.setPrizeCount(1);

        Mockito.when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(null);
        assertFalse(callbackForIndividualService.callback(pojo, serialNum));

        Mockito.when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(validateActivity());
        Mockito.when(activityWinRecordService.countChargeMobileByActivityId(Mockito.anyString())).thenReturn(1);
        Mockito.when(activityInfoService.selectByActivityId(Mockito.anyString())).thenReturn(null);
        assertFalse(callbackForIndividualService.callback(pojo, serialNum));
        
        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setPrizeCount(0L);
        Mockito.when(activityInfoService.selectByActivityId(Mockito.anyString())).thenReturn(activityInfo);
        assertFalse(callbackForIndividualService.callback(pojo, serialNum));
        
        activityInfo.setPrizeCount(2L);
        Mockito.when(activityInfoService.selectByActivityId(Mockito.anyString())).thenReturn(activityInfo);
       
        List list = new ArrayList();
        list.add(new ActivityWinRecord());
        Mockito.when(activityWinRecordService.selectByMap(Mockito.anyMap())).thenReturn(list);
        assertFalse(callbackForIndividualService.callback(pojo, serialNum));
        
        Mockito.when(activityWinRecordService.selectByMap(Mockito.anyMap())).thenReturn(null);
        IndividualProduct product = new IndividualProduct();
        product.setId(1L);
        Mockito.when(individualProductService.getDefaultFlowProduct()).thenReturn(product);
        
        IndividualAccount account = new IndividualAccount();
        account.setCount(new BigDecimal(0));
        Mockito.when(individualAccountService.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(),
                Mockito.anyInt())).thenReturn(account);
        
        assertFalse(callbackForIndividualService.callback(pojo, serialNum));
  
    }
    
    @Test
    public void testCallback1(){
        String serialNum = "test";
        CallbackPojo pojo = new CallbackPojo();
        pojo.setMobile("18867101111");
        pojo.setActiveId("12345");
        pojo.setPrizeCount(1);
        pojo.setPrizeId("1");
       
        Mockito.when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(validateActivity());
        Mockito.when(activityWinRecordService.countChargeMobileByActivityId(Mockito.anyString())).thenReturn(1);
        
        ActivityInfo activityInfo = new ActivityInfo();       
        activityInfo.setPrizeCount(2L);
        Mockito.when(activityInfoService.selectByActivityId(Mockito.anyString())).thenReturn(activityInfo);
   
        Mockito.when(activityWinRecordService.selectByMap(Mockito.anyMap())).thenReturn(null);
       
        IndividualProduct product = new IndividualProduct();
        product.setId(1L);
        Mockito.when(individualProductService.getDefaultFlowProduct()).thenReturn(product);
       
        IndividualAccount account = new IndividualAccount();        
        account.setCount(new BigDecimal(10));
        Mockito.when(individualAccountService.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(),
                Mockito.anyInt())).thenReturn(account);


        assertFalse(callbackForIndividualService.callback(pojo, null));

        ActivityPrize activityPrize = new ActivityPrize();
        activityPrize.setProductId(1L);
        Mockito.when(activityPrizeService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(activityPrize);        
        
        IndividualProduct flowcoin = new IndividualProduct();
        flowcoin.setId(2L);
        Mockito.when(individualProductService.getFlowcoinProduct()).thenReturn(flowcoin);
       
       
        
        Mockito.when(administerService.selectByMobilePhone(anyString())).thenReturn(null);
        Mockito.when(administerService.insertForScJizhong(anyString())).thenReturn(true);
        Mockito.when(activityWinRecordService.insertForIndividualRedpacket(any(CallbackPojo.class),
                anyString())).thenReturn(true);
        Mockito.when(individualAccountService.chargeFlowcoinForIndividualActivity(anyString(), anyInt(),
                anyString(), anyString())).thenReturn(true);
        Mockito.when(activityWinRecordService.updateForIndividualRedpacket(any(CallbackPojo.class), anyString(),
                anyInt(), anyString())).thenReturn(true);

        
        Mockito.when(producer.produceIndividualActivityWinMsg(Mockito.any(ActivitiesWinPojo.class))).thenReturn(true);
        
        
        assertFalse(callbackForIndividualService.callback(pojo, serialNum));

        activityPrize.setProductId(2L);
        Mockito.when(activityPrizeService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(activityPrize);        
        
        assertTrue(callbackForIndividualService.callback(pojo, serialNum));

    }
    
    @Test
    public void testCallback2(){
        String serialNum = "test";
        CallbackPojo pojo = new CallbackPojo();
        pojo.setMobile("18867101111");
        pojo.setActiveId("12345");
        pojo.setPrizeCount(1);
        pojo.setPrizeId("1");
       
        Mockito.when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(validateActivity());
        Mockito.when(activityWinRecordService.countChargeMobileByActivityId(Mockito.anyString())).thenReturn(1);
        
        ActivityInfo activityInfo = new ActivityInfo();       
        activityInfo.setPrizeCount(2L);
        Mockito.when(activityInfoService.selectByActivityId(Mockito.anyString())).thenReturn(activityInfo);
   
        Mockito.when(activityWinRecordService.selectByMap(Mockito.anyMap())).thenReturn(null);
       
        IndividualProduct product = new IndividualProduct();
        product.setId(1L);
        Mockito.when(individualProductService.getDefaultFlowProduct()).thenReturn(product);
       
        IndividualAccount account = new IndividualAccount();        
        account.setCount(new BigDecimal(10));
        Mockito.when(individualAccountService.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(),
                Mockito.anyInt())).thenReturn(account);

        ActivityPrize activityPrize = new ActivityPrize();
        activityPrize.setProductId(1L);
        Mockito.when(activityPrizeService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(activityPrize);        
        
        IndividualProduct flowcoin = new IndividualProduct();
        flowcoin.setId(2L);
        Mockito.when(individualProductService.getFlowcoinProduct()).thenReturn(flowcoin);

        Mockito.when(administerService.selectByMobilePhone(anyString())).thenReturn(null);
        //插入用户失败
        Mockito.when(administerService.insertForScJizhong(anyString())).thenReturn(false);
        assertFalse(callbackForIndividualService.callback(pojo, serialNum));
        
        activityPrize.setProductId(2L);
        Mockito.when(activityPrizeService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(activityPrize);        
        assertFalse(callbackForIndividualService.callback(pojo, serialNum));
        
        //插入中奖记录扣除冻结账户失败
        Mockito.when(administerService.insertForScJizhong(anyString())).thenReturn(true);
        Mockito.when(activityWinRecordService.insertForIndividualRedpacket(any(CallbackPojo.class),
                anyString())).thenReturn(false);
        activityPrize.setProductId(1L);
        assertFalse(callbackForIndividualService.callback(pojo, serialNum));
        
        activityPrize.setProductId(2L);
        Mockito.when(activityPrizeService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(activityPrize);        
        assertFalse(callbackForIndividualService.callback(pojo, serialNum));
        

        Mockito.when(activityWinRecordService.insertForIndividualRedpacket(any(CallbackPojo.class),
                anyString())).thenReturn(true);
        Mockito.when(individualAccountService.chargeFlowcoinForIndividualActivity(anyString(), anyInt(),
                anyString(), anyString())).thenReturn(false);
        assertFalse(callbackForIndividualService.callback(pojo, serialNum));
        
        
        Mockito.when(individualAccountService.chargeFlowcoinForIndividualActivity(anyString(), anyInt(),
                anyString(), anyString())).thenReturn(true);
        
        Mockito.when(activityWinRecordService.updateForIndividualRedpacket(any(CallbackPojo.class), anyString(),
                anyInt(), anyString())).thenReturn(false);
        assertFalse(callbackForIndividualService.callback(pojo, serialNum));
        
        
        Mockito.when(activityWinRecordService.updateForIndividualRedpacket(any(CallbackPojo.class), anyString(),
                anyInt(), anyString())).thenReturn(true);
  
        Mockito.when(producer.produceIndividualActivityWinMsg(Mockito.any(ActivitiesWinPojo.class))).thenReturn(false);
        activityPrize.setProductId(1L);
        Mockito.when(activityPrizeService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(activityPrize);                
        try{
            callbackForIndividualService.callback(pojo, serialNum);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        activityPrize.setProductId(2L);
        Mockito.when(activityPrizeService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(activityPrize);        
        try{
            callbackForIndividualService.callback(pojo, serialNum);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        
        Mockito.when(producer.produceIndividualActivityWinMsg(Mockito.any(ActivitiesWinPojo.class))).thenReturn(true);
        activityPrize.setProductId(1L);
        Mockito.when(activityPrizeService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(activityPrize);        
        assertFalse(callbackForIndividualService.callback(pojo, serialNum));
        
        activityPrize.setProductId(2L);
        Mockito.when(activityPrizeService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(activityPrize);        
        assertTrue(callbackForIndividualService.callback(pojo, serialNum));

    }

    @Test
    public void testCallback3(){
        String serialNum = "test";
        CallbackPojo pojo = new CallbackPojo();
        pojo.setMobile("18867101111");
        pojo.setActiveId("12345");
        pojo.setPrizeCount(1);
        pojo.setPrizeId("1");
       
        Mockito.when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(validateActivity());
        Mockito.when(activityWinRecordService.countChargeMobileByActivityId(Mockito.anyString())).thenReturn(1);
        
        ActivityInfo activityInfo = new ActivityInfo();       
        activityInfo.setPrizeCount(2L);
        Mockito.when(activityInfoService.selectByActivityId(Mockito.anyString())).thenReturn(activityInfo);
   
        Mockito.when(activityWinRecordService.selectByMap(Mockito.anyMap())).thenReturn(null);
       
        IndividualProduct product = new IndividualProduct();
        product.setId(1L);
        Mockito.when(individualProductService.getDefaultFlowProduct()).thenReturn(product);
       
        IndividualAccount account = new IndividualAccount();        
        account.setCount(new BigDecimal(10));
        Mockito.when(individualAccountService.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(),
                Mockito.anyInt())).thenReturn(account);

        ActivityPrize activityPrize = new ActivityPrize();
        activityPrize.setProductId(1L);
        Mockito.when(activityPrizeService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(activityPrize);        
        
        IndividualProduct flowcoin = new IndividualProduct();
        flowcoin.setId(2L);
        Mockito.when(individualProductService.getFlowcoinProduct()).thenReturn(flowcoin);

        Mockito.when(administerService.selectByMobilePhone(anyString())).thenReturn(null);
        //插入用户失败
        Mockito.when(administerService.insertForScJizhong(anyString())).thenReturn(false);
        assertFalse(callbackForIndividualService.callback(pojo, serialNum));
        
        activityPrize.setProductId(2L);
        Mockito.when(activityPrizeService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(activityPrize);        
        assertFalse(callbackForIndividualService.callback(pojo, serialNum));
        
        //插入中奖记录扣除冻结账户失败
        Mockito.when(administerService.insertForScJizhong(anyString())).thenReturn(true);
        Mockito.when(activityWinRecordService.insertForIndividualFlowRedpacket(any(CallbackPojo.class),
                anyString())).thenReturn(false);
        activityPrize.setProductId(1L);
        assertFalse(callbackForIndividualService.callback(pojo, serialNum));
        
        activityPrize.setProductId(2L);
        Mockito.when(activityPrizeService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(activityPrize);        
        assertFalse(callbackForIndividualService.callback(pojo, serialNum));
        

        Mockito.when(activityWinRecordService.insertForIndividualFlowRedpacket(any(CallbackPojo.class),
                anyString())).thenReturn(true);
        Mockito.when(individualAccountService.chargeFlowcoinForIndividualActivity(anyString(), anyInt(),
                anyString(), anyString())).thenReturn(false);
        assertFalse(callbackForIndividualService.callback(pojo, serialNum));
        
        
        Mockito.when(individualAccountService.chargeFlowcoinForIndividualActivity(anyString(), anyInt(),
                anyString(), anyString())).thenReturn(true);
        
        Mockito.when(activityWinRecordService.updateForIndividualRedpacket(any(CallbackPojo.class), anyString(),
                anyInt(), anyString())).thenReturn(false);
        assertFalse(callbackForIndividualService.callback(pojo, serialNum));
        
        
        Mockito.when(activityWinRecordService.updateForIndividualRedpacket(any(CallbackPojo.class), anyString(),
                anyInt(), anyString())).thenReturn(true);
  
        Mockito.when(producer.produceIndividualActivityWinMsg(Mockito.any(ActivitiesWinPojo.class))).thenReturn(false);
        activityPrize.setProductId(1L);
        Mockito.when(activityPrizeService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(activityPrize);                
        try{
            callbackForIndividualService.callback(pojo, serialNum);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        activityPrize.setProductId(2L);
        Mockito.when(activityPrizeService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(activityPrize);        
        try{
            callbackForIndividualService.callback(pojo, serialNum);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        
        Mockito.when(producer.produceIndividualActivityWinMsg(Mockito.any(ActivitiesWinPojo.class))).thenReturn(true);
        activityPrize.setProductId(1L);
        Mockito.when(activityPrizeService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(activityPrize);        
        assertTrue(callbackForIndividualService.callback(pojo, serialNum));
        
        activityPrize.setProductId(2L);
        Mockito.when(activityPrizeService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(activityPrize);        
        assertFalse(callbackForIndividualService.callback(pojo, serialNum));

    }
    
    private Activities validateActivity(){
        Activities activity = new Activities();
        activity.setEndTime(DateUtils.addDays(new Date(), 1));
        activity.setDeleteFlag(0);
        activity.setStatus(ActivityStatus.PROCESSING.getCode());
        return activity;
    }
    

}
