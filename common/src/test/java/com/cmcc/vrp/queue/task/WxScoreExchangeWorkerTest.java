package com.cmcc.vrp.queue.task;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.guangdong.GdBossOperationResultImpl;
import com.cmcc.vrp.boss.guangdong.model.GdReturnCode;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.IndividualAccountRecordService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.wx.WxExchangeRecordService;
import com.cmcc.vrp.wx.beans.ExchangeChargePojo;
import com.cmcc.vrp.wx.beans.TemplateMsgPojo;
import com.cmcc.vrp.wx.flowcoin.GdFlowCoinCharge;
import com.cmcc.vrp.wx.model.WxExchangeRecord;

/**
 * WxScoreExchangeWorkerTest.java
 * @author wujiamin
 * @date 2017年3月20日
 */
@RunWith(MockitoJUnitRunner.class)
public class WxScoreExchangeWorkerTest {
    @InjectMocks
    WxScoreExchangeWorker worker = new WxScoreExchangeWorker();
    @Mock
    GdFlowCoinCharge gdFlowCoinCharge;
    @Mock
    TaskProducer producer;
    
    @Mock
    AdministerService administerService;
    
    @Mock
    WxExchangeRecordService wxExchangeRecordService;
    
    @Mock
    IndividualAccountRecordService individualAccountRecordService;
    
    @Mock
    IndividualAccountService individualAccountService;
    
    @Mock
    IndividualProductService individualProductService;

    @Test
    public void exec(){

        worker.exec();
        
        worker.setTaskString(JSONObject.toJSONString(createPojo()));
        worker.exec();
        
        WxExchangeRecord record = new WxExchangeRecord();
        record.setCount(100);
        Mockito.when(wxExchangeRecordService.selectBySystemNum(Mockito.anyString())).thenReturn(record);
        
        Mockito.when(gdFlowCoinCharge.chargeFlow(Mockito.anyString(), Mockito.anyString(), Mockito.any(WxExchangeRecord.class)))
            .thenReturn(new GdBossOperationResultImpl(GdReturnCode.SUCCESS));
        Mockito.when(producer.produceWxSendTemplateMsg(Mockito.any(TemplateMsgPojo.class))).thenReturn(false);
        Mockito.when(administerService.selectAdministerById(Mockito.anyLong())).thenReturn(new Administer());
        worker.setTaskString(JSONObject.toJSONString(createPojo()));
        worker.exec();
        
        Mockito.when(gdFlowCoinCharge.chargeFlow(Mockito.anyString(), Mockito.anyString(), Mockito.any(WxExchangeRecord.class)))
            .thenReturn(new GdBossOperationResultImpl(GdReturnCode.FAILD));
        Mockito.when(individualProductService.getIndivialPointProduct()).thenReturn(null);
        worker.setTaskString(JSONObject.toJSONString(createPojo()));
        worker.exec();
                
        Mockito.when(individualProductService.getIndivialPointProduct()).thenReturn(new IndividualProduct());
        Mockito.when(individualAccountService.changeAccount(Mockito.any(IndividualAccount.class), Mockito.any(BigDecimal.class), 
                Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
        IndividualProduct product = new IndividualProduct();
        product.setProductSize(1024L);
        Mockito.when(individualProductService.selectByPrimaryId(Mockito.anyLong())).thenReturn(product);
        worker.setTaskString(JSONObject.toJSONString(createPojo()));
        worker.exec();
        
        Mockito.when(individualAccountService.changeAccount(Mockito.any(IndividualAccount.class), Mockito.any(BigDecimal.class), 
                Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(false);
        worker.setTaskString(JSONObject.toJSONString(createPojo()));
        worker.exec();
    
    }
    
    private ExchangeChargePojo createPojo(){
        ExchangeChargePojo pojo = new ExchangeChargePojo();
        pojo.setMobile("18867103685");
        pojo.setPrdCode("test");
        pojo.setSystemNum("test");
        return pojo;
    }
}
