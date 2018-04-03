package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.CallbackPojo;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityTemplateService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.CallbackService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ActivitiesWinPojo;

/**
 * Created by qinqinyan on 2016/11/7.
 */
@RunWith(MockitoJUnitRunner.class)
public class CallbackServiceImplTest {
    @InjectMocks
    CallbackService callbackService = new CallbackServiceImpl();
    @Mock
    ProductService productService;
    @Mock
    TaskProducer producer;
    @Mock
    SerialNumService serialNumService;
    @Mock
    ActivitiesService activitiesService;
    @Mock
    ActivityWinRecordService activityWinRecordService;
    @Mock
    AccountService accountService;
    @Mock
    ChargeRecordService chargeRecordService;
    @Mock
    EnterprisesService enterprisesService;
    @Mock
    EntProductService entProductService;
    @Mock
    ActivityTemplateService activityTemplateService;
    @Mock
    ActivityInfoService activityInfoService;

    @Test
    public void testCallback12(){
        CallbackPojo pojo = createCallbackPojo();
        String serialNum = "test";
        Mockito.when(activityTemplateService.invalidMobile(Mockito.anyString())).thenReturn(true);
        assertFalse(callbackService.callback(pojo, serialNum));
    }

    @Test
    public void testCallback(){
        CallbackPojo pojo = createCallbackPojo();
        String serialNum = "test";
        Enterprise enterprise = createEnterprise();
        enterprise.setDeleteFlag(2);

        Mockito.when(activityTemplateService.invalidMobile(Mockito.anyString())).thenReturn(false);

        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        assertFalse(callbackService.callback(pojo, serialNum));
        Mockito.verify(enterprisesService).selectById(anyLong());
    }

    private Enterprise createEnterprise(){
        Enterprise enterprise = new Enterprise();
        enterprise.setId(1L);
        enterprise.setStatus((byte)3);
        enterprise.setDeleteFlag(0);
        return enterprise;
    }


    private EntProduct createEntProduct(){
        EntProduct entProduct = new EntProduct();
        entProduct.setId(1L);
        entProduct.setEnterprizeId(1L);
        entProduct.setProductId(1L);
        return entProduct;
    }

    @Test
    public void testCallback0(){
        CallbackPojo pojo = createCallbackPojo();
        String serialNum = "test";

        Enterprise enterprise = createEnterprise();

        Mockito.when(activityTemplateService.invalidMobile(Mockito.anyString())).thenReturn(false);

        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong()))
            .thenReturn(null);

        assertFalse(callbackService.callback(pojo, serialNum));

        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(productService.get(anyLong())).thenReturn(createProduct());
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong()))
        .thenReturn(null);

        assertFalse(callbackService.callback(pojo, serialNum));
//        Mockito.verify(enterprisesService).selectById(anyLong());
//        Mockito.verify(productService).get(anyLong());
//        Mockito.verify(entProductService).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
    }

    @Test
    public void testCallback11(){
        CallbackPojo pojo = createCallbackPojo();
        String serialNum = "test";
        EntProduct entProduct =createEntProduct();

        Enterprise enterprise = createEnterprise();

        Mockito.when(activityTemplateService.invalidMobile(Mockito.anyString())).thenReturn(false);

        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong()))
            .thenReturn(entProduct);
        Mockito.when(productService.get(anyLong())).thenReturn(null);

        assertFalse(callbackService.callback(pojo, serialNum));

        Mockito.verify(enterprisesService).selectById(anyLong());
        Mockito.verify(productService).get(anyLong());
//        Mockito.verify(entProductService).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
    }

    @Test
    public void testCallback1(){
        CallbackPojo pojo = createCallbackPojo();
        String serialNum = "test";
        Product product = createProduct();
        Enterprise enterprise = createEnterprise();
        EntProduct entProduct =createEntProduct();

        Mockito.when(activityTemplateService.invalidMobile(Mockito.anyString())).thenReturn(false);

        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(productService.get(anyLong())).thenReturn(product);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong()))
            .thenReturn(entProduct);
        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(createActivities());

        assertFalse(callbackService.callback(pojo, serialNum));

//        Mockito.verify(enterprisesService).selectById(anyLong());
//        Mockito.verify(productService).get(anyLong());
//        Mockito.verify(entProductService).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
    }

    @Test
    public void testCallback2(){
        CallbackPojo pojo = createCallbackPojo();
        String serialNum = "test";
        Product product = createProduct();
        Enterprise enterprise = createEnterprise();
        EntProduct entProduct = createEntProduct();

        Mockito.when(activityTemplateService.invalidMobile(Mockito.anyString())).thenReturn(false);

        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(productService.get(anyLong())).thenReturn(product);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong()))
            .thenReturn(entProduct);
        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(createActivities());

        Mockito.when(accountService.minusCount(Long.parseLong(pojo.getEnterId()), Long.parseLong(pojo.getPrizeId()),
            AccountType.ENTERPRISE, (double)1, serialNum, "充值")).thenReturn(false);

        assertFalse(callbackService.callback(pojo, serialNum));

//        Mockito.verify(enterprisesService).selectById(anyLong());
//        Mockito.verify(productService).get(anyLong());
//        Mockito.verify(entProductService).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
//
//        Mockito.verify(accountService).minusCount(Long.parseLong(pojo.getEnterId()), Long.parseLong(pojo.getPrizeId()),
//            AccountType.ENTERPRISE, (double)1, serialNum, "充值");
    }

    @Test
    public void testCallback3(){
        CallbackPojo pojo = createCallbackPojo();
        String serialNum = "test";
        Product product = createProduct();
        Enterprise enterprise = createEnterprise();
        EntProduct entProduct = createEntProduct();

        Mockito.when(activityTemplateService.invalidMobile(Mockito.anyString())).thenReturn(false);

        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(productService.get(anyLong())).thenReturn(product);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong()))
            .thenReturn(entProduct);

        Mockito.when(accountService.minusCount(Long.parseLong(pojo.getEnterId()), Long.parseLong(pojo.getPrizeId()),
            AccountType.ENTERPRISE, (double)1, serialNum, "充值")).thenReturn(true);

        Mockito.when(activityWinRecordService.insertSelective(any(ActivityWinRecord.class)))
            .thenReturn(true);

        Mockito.when(serialNumService.insert(any(SerialNum.class)))
            .thenReturn(false);

        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(createActivities());
        Mockito.when(accountService.returnFunds(serialNum, ActivityType.fromValue(createActivities().getType()),
            Long.parseLong(String.valueOf(pojo.getPrizeId())), 1)).thenReturn(false);

        assertFalse(callbackService.callback(pojo, serialNum));

//        Mockito.verify(enterprisesService).selectById(anyLong());
//        Mockito.verify(productService).get(anyLong());
//        Mockito.verify(entProductService).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
//
//        Mockito.verify(accountService).minusCount(Long.parseLong(pojo.getEnterId()), Long.parseLong(pojo.getPrizeId()),
//            AccountType.ENTERPRISE, (double)1, serialNum, "充值");
//
//        Mockito.verify(activityWinRecordService).insertSelective(any(ActivityWinRecord.class));
//        Mockito.verify(serialNumService).insert(any(SerialNum.class));
//        Mockito.verify(activitiesService).selectByActivityId(anyString());
//        Mockito.verify(accountService).returnFunds(serialNum, ActivityType.fromValue(createActivities().getType()),
//            Long.parseLong(String.valueOf(pojo.getPrizeId())), 1);
    }

    @Test
    public void testCallback4(){
        CallbackPojo pojo = createCallbackPojo();
        String serialNum = "test";
        Product product = createProduct();
        Enterprise enterprise = createEnterprise();
        EntProduct entProduct = createEntProduct();

        Mockito.when(activityTemplateService.invalidMobile(Mockito.anyString())).thenReturn(false);

        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(productService.get(anyLong())).thenReturn(product);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong()))
            .thenReturn(entProduct);

        Mockito.when(accountService.minusCount(Long.parseLong(pojo.getEnterId()), Long.parseLong(pojo.getPrizeId()),
            AccountType.ENTERPRISE, (double)1, serialNum, "充值")).thenReturn(true);

        Mockito.when(activityWinRecordService.insertSelective(any(ActivityWinRecord.class)))
            .thenReturn(true);

        Mockito.when(serialNumService.insert(any(SerialNum.class)))
            .thenReturn(false);

        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(createActivities());
        Mockito.when(accountService.returnFunds(serialNum, ActivityType.fromValue(createActivities().getType()),
            Long.parseLong(String.valueOf(pojo.getPrizeId())), 1)).thenReturn(true);

        assertFalse(callbackService.callback(pojo, serialNum));

//        Mockito.verify(enterprisesService).selectById(anyLong());
//        Mockito.verify(productService).get(anyLong());
//        Mockito.verify(entProductService).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
//
//        Mockito.verify(accountService).minusCount(Long.parseLong(pojo.getEnterId()), Long.parseLong(pojo.getPrizeId()),
//            AccountType.ENTERPRISE, (double)1, serialNum, "充值");
//
//        Mockito.verify(activityWinRecordService).insertSelective(any(ActivityWinRecord.class));
//        Mockito.verify(serialNumService).insert(any(SerialNum.class));
//        Mockito.verify(activitiesService).selectByActivityId(anyString());
//        Mockito.verify(accountService).returnFunds(serialNum, ActivityType.fromValue(createActivities().getType()),
//            Long.parseLong(String.valueOf(pojo.getPrizeId())), 1);
    }


    @Test
    public void testCallback5(){
        CallbackPojo pojo = createCallbackPojo();
        String serialNum = "test";
        Product product = createProduct();
        Enterprise enterprise = createEnterprise();
        EntProduct entProduct = createEntProduct();

        Mockito.when(activityTemplateService.invalidMobile(Mockito.anyString())).thenReturn(false);

        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(productService.get(anyLong())).thenReturn(product);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong()))
            .thenReturn(entProduct);

        Mockito.when(accountService.minusCount(Long.parseLong(pojo.getEnterId()), Long.parseLong(pojo.getPrizeId()),
            AccountType.ENTERPRISE, (double)1, serialNum, "充值")).thenReturn(true);

        Mockito.when(activityWinRecordService.insertSelective(any(ActivityWinRecord.class)))
            .thenReturn(true);

        Mockito.when(serialNumService.insert(any(SerialNum.class)))
            .thenReturn(true);

        Mockito.when(chargeRecordService.create(any(ChargeRecord.class)))
            .thenReturn(false);

        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(createActivities());
        Mockito.when(accountService.returnFunds(serialNum, ActivityType.fromValue(createActivities().getType()),
            Long.parseLong(String.valueOf(pojo.getPrizeId())), 1)).thenReturn(true);

        assertFalse(callbackService.callback(pojo, serialNum));

//        Mockito.verify(enterprisesService).selectById(anyLong());
//        Mockito.verify(productService).get(anyLong());
//        Mockito.verify(entProductService).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
//
//        Mockito.verify(accountService).minusCount(Long.parseLong(pojo.getEnterId()), Long.parseLong(pojo.getPrizeId()),
//            AccountType.ENTERPRISE, (double)1, serialNum, "充值");
//
//        Mockito.verify(activityWinRecordService).insertSelective(any(ActivityWinRecord.class));
//        Mockito.verify(serialNumService).insert(any(SerialNum.class));
//        Mockito.verify(chargeRecordService).create(any(ChargeRecord.class));
//
//        //Mockito.verify(activitiesService).selectByActivityId(anyString());
//        Mockito.verify(accountService).returnFunds(serialNum, ActivityType.fromValue(createActivities().getType()),
//            Long.parseLong(String.valueOf(pojo.getPrizeId())), 1);
    }

    @Test
    public void testCallback6(){
        CallbackPojo pojo = createCallbackPojo();
        String serialNum = "test";
        Product product = createProduct();
        Enterprise enterprise = createEnterprise();
        EntProduct entProduct = createEntProduct();

        Mockito.when(activityTemplateService.invalidMobile(Mockito.anyString())).thenReturn(false);

        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(productService.get(anyLong())).thenReturn(product);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong()))
            .thenReturn(entProduct);

        Mockito.when(accountService.minusCount(Long.parseLong(pojo.getEnterId()), Long.parseLong(pojo.getPrizeId()),
            AccountType.ENTERPRISE, (double)1, serialNum, "充值")).thenReturn(true);

        Mockito.when(activityWinRecordService.insertSelective(any(ActivityWinRecord.class)))
            .thenReturn(true);

        Mockito.when(serialNumService.insert(any(SerialNum.class)))
            .thenReturn(true);

        Mockito.when(activitiesService.selectByActivityId(anyString()))
            .thenReturn(createActivities());

        Mockito.when(chargeRecordService.create(any(ChargeRecord.class)))
            .thenReturn(true);

        Mockito.when(chargeRecordService.getRecordBySN(anyString()))
            .thenReturn(createChargeRecord());

        Mockito.when(producer.produceActivityWinMsg(any(ActivitiesWinPojo.class)))
            .thenReturn(true);

        assertFalse(callbackService.callback(pojo, serialNum));

//        Mockito.verify(enterprisesService).selectById(anyLong());
//        Mockito.verify(productService).get(anyLong());
//        Mockito.verify(entProductService).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
//
//        Mockito.verify(accountService).minusCount(Long.parseLong(pojo.getEnterId()), Long.parseLong(pojo.getPrizeId()),
//            AccountType.ENTERPRISE, (double)1, serialNum, "充值");
//
//        Mockito.verify(activityWinRecordService).insertSelective(any(ActivityWinRecord.class));
//        Mockito.verify(serialNumService).insert(any(SerialNum.class));
//
//        Mockito.verify(activitiesService, Mockito.atLeastOnce()).selectByActivityId(anyString());
//
//        Mockito.verify(chargeRecordService).create(any(ChargeRecord.class));
//
//        Mockito.verify(chargeRecordService).getRecordBySN(anyString());
//        Mockito.verify(producer).produceActivityWinMsg(any(ActivitiesWinPojo.class));
    }

    @Test
    public void testCallback7(){
        CallbackPojo pojo = createCallbackPojo();
        String serialNum = "test";
        Product product = createProduct();
        Enterprise enterprise = createEnterprise();
        EntProduct entProduct = createEntProduct();

        Mockito.when(activityTemplateService.invalidMobile(Mockito.anyString())).thenReturn(false);

        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(productService.get(anyLong())).thenReturn(product);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong()))
            .thenReturn(entProduct);

        Mockito.when(accountService.minusCount(Long.parseLong(pojo.getEnterId()), Long.parseLong(pojo.getPrizeId()),
            AccountType.ENTERPRISE, (double)1, serialNum, "充值")).thenReturn(true);

        Mockito.when(activityWinRecordService.insertSelective(any(ActivityWinRecord.class)))
            .thenReturn(true);

        Mockito.when(serialNumService.insert(any(SerialNum.class)))
            .thenReturn(true);

        Mockito.when(activitiesService.selectByActivityId(anyString()))
            .thenReturn(createActivities());

        Mockito.when(chargeRecordService.create(any(ChargeRecord.class)))
            .thenReturn(true);

        Mockito.when(chargeRecordService.getRecordBySN(anyString()))
            .thenReturn(createChargeRecord());

        Mockito.when(producer.produceActivityWinMsg(any(ActivitiesWinPojo.class)))
            .thenReturn(true);

        Mockito.when(activityWinRecordService.updateByPrimaryKeySelective(any(ActivityWinRecord.class)))
            .thenReturn(true);

        Mockito.when(chargeRecordService.updateStatusCode(anyLong(), anyString())).thenReturn(true);


        assertTrue(callbackService.callback(pojo, serialNum));

//        Mockito.verify(enterprisesService).selectById(anyLong());
//        Mockito.verify(productService).get(anyLong());
//        Mockito.verify(entProductService).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
//
//        Mockito.verify(accountService).minusCount(Long.parseLong(pojo.getEnterId()), Long.parseLong(pojo.getPrizeId()),
//            AccountType.ENTERPRISE, (double)1, serialNum, "充值");
//
//        Mockito.verify(activityWinRecordService).insertSelective(any(ActivityWinRecord.class));
//        Mockito.verify(serialNumService).insert(any(SerialNum.class));
//
//        Mockito.verify(activitiesService, Mockito.atLeastOnce()).selectByActivityId(anyString());
//
//        Mockito.verify(chargeRecordService).create(any(ChargeRecord.class));
//
//        Mockito.verify(chargeRecordService).getRecordBySN(anyString());
//        Mockito.verify(producer).produceActivityWinMsg(any(ActivitiesWinPojo.class));
//
//        Mockito.verify(activityWinRecordService).updateByPrimaryKeySelective(any(ActivityWinRecord.class));
//        Mockito.verify(chargeRecordService).updateStatusCode(anyLong(), anyString());
    }
    
    @Test
    public void testCallback8(){
        CallbackPojo pojo = createCallbackPojo();
        String serialNum = "test";
        Product product = createProduct();
        Enterprise enterprise = createEnterprise();
        EntProduct entProduct = createEntProduct();

        Mockito.when(activityTemplateService.invalidMobile(Mockito.anyString())).thenReturn(false);

        Mockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);
        Mockito.when(productService.get(anyLong())).thenReturn(product);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong()))
            .thenReturn(entProduct);

        Mockito.when(accountService.minusCount(Long.parseLong(pojo.getEnterId()), Long.parseLong(pojo.getPrizeId()),
            AccountType.ENTERPRISE, (double)1, serialNum, "充值")).thenReturn(true);

        Mockito.when(activityWinRecordService.insertSelective(any(ActivityWinRecord.class)))
            .thenReturn(true);

        Mockito.when(serialNumService.insert(any(SerialNum.class)))
            .thenReturn(true);

        Activities activities = createActivities();
        activities.setType(ActivityType.LUCKY_REDPACKET.getCode());
        Mockito.when(activitiesService.selectByActivityId(anyString()))
            .thenReturn(activities);

        Product realUseProd = new Product();
        realUseProd.setId(1L);
        Mockito.when(productService.getPrdBySizeAndId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(realUseProd);

        pojo.setPrizeCount(10240);
        product.setFlowAccountProductId(1L);
        
        Mockito.when(chargeRecordService.create(any(ChargeRecord.class)))
            .thenReturn(true);

        Mockito.when(chargeRecordService.getRecordBySN(anyString()))
            .thenReturn(createChargeRecord());

        Mockito.when(producer.produceActivityWinMsg(any(ActivitiesWinPojo.class)))
            .thenReturn(true);

        Mockito.when(activityWinRecordService.updateByPrimaryKeySelective(any(ActivityWinRecord.class)))
            .thenReturn(true);

        Mockito.when(chargeRecordService.updateStatusCode(anyLong(), anyString())).thenReturn(true);

        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setGivedUserCount(1L);
        Mockito.when(activityInfoService.selectByActivityId(Mockito.anyString())).thenReturn(activityInfo);

        Mockito.when(activityInfoService.updateForRendomPacket(Mockito.any(ActivityInfo.class))).thenReturn(false);
        
        Product p = new Product();
        p.setIsp("M");
        Mockito.when(productService.selectProductById(Mockito.anyLong())).thenReturn(p);
        
        assertTrue(callbackService.callback(pojo, serialNum));
        
        
        //不存在虚拟化产品
        Mockito.when(productService.getPrdBySizeAndId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);
        Product realProduct = new Product();
        realProduct.setPrice(100);
        realProduct.setFlowAccountProductId(1L);
        realProduct.setId(1L);
        realProduct.setType(1);
        Mockito.when(productService.get(Mockito.anyLong())).thenReturn(realProduct);
        try{
            callbackService.callback(pojo, serialNum);
        }catch(Exception e){
            
        }
}


    private ActivitiesWinPojo createActivitiesWinPojo(){
        ActivitiesWinPojo pojo = new ActivitiesWinPojo();
        pojo.setActivitiesWinRecordId("1");
        return pojo;
    }

    private ChargeRecord createChargeRecord(){
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setId(1L);
        return chargeRecord;
    }


    private CallbackPojo createCallbackPojo(){
        CallbackPojo pojo = new CallbackPojo();
        pojo.setMobile("18867101234");
        pojo.setActiveId("test");
        pojo.setCatName("M");
        pojo.setPrizeId("1");
        pojo.setPrizeCount(1);
        pojo.setPrizeType(0);
        pojo.setPrizeResponse(1);
        pojo.setEnterId("1");
        return pojo;
    }

    private Product createProduct(){
        Product product = new Product();
        product.setId(1L);
        product.setIsp("M");
        product.setType(2);
        return product;
    }

    private Activities createActivities(){
        Activities activities = new Activities();
        activities.setId(1L);
        activities.setEntId(1L);
        activities.setType(1);
        activities.setType(0);
        activities.setName("aaa");
        return activities;
    }
    
}
