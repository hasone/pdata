package com.cmcc.vrp.queue.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.WxTemplateMsgType;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityPaymentInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityPaymentInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.wx.WetchatService;
import com.cmcc.vrp.wx.WxExchangeRecordService;
import com.cmcc.vrp.wx.beans.TemplateMsgPojo;
import com.cmcc.vrp.wx.beans.WxUserInfo;
import com.cmcc.vrp.wx.flowcoin.GdFlowCoinCharge;
import com.cmcc.vrp.wx.model.WxExchangeRecord;

/**
 * WxSendTemplateWorkerTest.java
 * @author wujiamin
 * @date 2017年3月29日
 */
@RunWith(MockitoJUnitRunner.class)
public class WxSendTemplateWorkerTest {
    @InjectMocks
    WxSendTemplateWorker worker = new WxSendTemplateWorker();
    @Mock
    GdFlowCoinCharge gdFlowCoinCharge;
    @Mock
    ActivitiesService activitiesService;
    @Mock
    ActivityWinRecordService activityWinRecordService;
    @Mock
    ActivityPrizeService activityPrizeService;
    @Mock
    ProductService productService;
    @Mock
    GlobalConfigService globalConfigService;
    @Mock
    ActivityPaymentInfoService paymentInfoService;
    @Mock
    WxExchangeRecordService wxExchangeRecordService;
    @Mock
    IndividualProductService individualProductService;
    @Mock
    WetchatService wetchatService; 

    @Test
    public void exec(){
        
        worker.exec();

        
        TemplateMsgPojo pojo = createPojo();
        pojo.setType(WxTemplateMsgType.CROWDFUNDING_SUCCESS);
        String taskString = JSONObject.toJSONString(pojo);
        worker.setTaskString(taskString);

        worker.exec();
        
        Mockito.when(wetchatService.getWxUserInfo("18867103685")).thenReturn(new WxUserInfo());
        Mockito.when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(new Activities());
        Mockito.when(activityWinRecordService.selectByRecordId(Mockito.anyString())).thenReturn(new ActivityWinRecord());
        ActivityPrize actPrize = new ActivityPrize();
        actPrize.setDiscount(70);
        Mockito.when(activityPrizeService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(actPrize);
        Product product = new Product();
        product.setPrice(100);
        product.setProductSize(1024L);
        Mockito.when(productService.selectProductById(Mockito.anyLong())).thenReturn(product);
        Mockito.when(wetchatService.sendTemplateMag(Mockito.anyString())).thenReturn("测试返回string");

        worker.exec();
        
        
        pojo.setType(WxTemplateMsgType.PAY_SUCCESS);
        taskString = JSONObject.toJSONString(pojo);
        worker.setTaskString(taskString);
        ActivityPaymentInfo paymentInfo = new ActivityPaymentInfo();
        paymentInfo.setPayAmount(100L);
        Mockito.when(paymentInfoService.selectBySysSerialNum(Mockito.anyString())).thenReturn(paymentInfo);
        worker.exec();
        
        pojo.setType(WxTemplateMsgType.PAY_FAIL);
        taskString = JSONObject.toJSONString(pojo);
        worker.setTaskString(taskString);
        worker.exec();

        pojo.setType(WxTemplateMsgType.REFUND);
        taskString = JSONObject.toJSONString(pojo);
        worker.setTaskString(taskString);
        worker.exec();
        
        pojo.setType(WxTemplateMsgType.EXCHANGE_SUCCESS);
        taskString = JSONObject.toJSONString(pojo);
        worker.setTaskString(taskString);
        IndividualProduct individualProduct = new IndividualProduct();
        individualProduct.setPrice(100);
        individualProduct.setProductSize(1024L);
        Mockito.when(individualProductService.selectByPrimaryId(Mockito.anyLong())).thenReturn(individualProduct);
        Mockito.when(wxExchangeRecordService.selectBySystemNum(Mockito.anyString())).thenReturn(new WxExchangeRecord());
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.WEIXIN_SCORE_EXCHANGE_RULE.getKey())).thenReturn("10");
        worker.exec();
        
        pojo.setType(WxTemplateMsgType.EXCHANGE_FAIL);
        taskString = JSONObject.toJSONString(pojo);
        worker.setTaskString(taskString);
        worker.exec();
    }
    
    private TemplateMsgPojo createPojo(){
        TemplateMsgPojo pojo = new TemplateMsgPojo();
        pojo.setActivityId("activityId");
        pojo.setActivityWinRecordId("activityWinRecordId");
        pojo.setExchangeSystemNum("exchangeSystemNum");
        pojo.setPaymentSerial("paymentSerial");
        pojo.setMobile("18867103685");
        return pojo;
    }
}
