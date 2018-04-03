package com.cmcc.vrp.queue.task.channel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.boss.ecinvoker.impl.CqEcBossServiceImpl;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeType;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterpriseSmsTemplate;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EnterpriseSmsTemplateService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.MonthlyPresentRuleService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.CallbackPojo;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;

@RunWith(PowerMockRunner.class)
public class AbstractChannelWorkerTest{
    @InjectMocks
    CqChannelWorker cqWorker = new CqChannelWorker();
    
    @Mock
    CqEcBossServiceImpl bossService;
    
    @Mock
    ProductService productService;
    
    @Mock
    AccountService accountService;
    
    @Mock
    ChargeRecordService chargeRecordService;
    
    @Mock
    GlobalConfigService globalConfigService;
    
    @Mock
    MonthlyPresentRuleService monthlyPresentRuleService;
    
    @Mock
    EnterprisesService enterprisesService;
    
    @Mock
    EnterpriseSmsTemplateService enterpriseSmsTemplateService;
    
    @Mock
    TaskProducer taskProducer;
    
    @Before
    public void init() {
        Gson gson = new Gson();
        ReflectionTestUtils.setField(cqWorker, "gson", gson);       
    }
    
    @Test
    public void exec(){
        
        cqWorker.exec();
        
        cqWorker.setTaskString(getTaskString());
        
        Product product = new Product();
        product.setProductSize(10240L);
        Mockito.when(productService.get(Mockito.anyLong())).thenReturn(product);
        
        Mockito.when(taskProducer.productPlatformCallbackMsg(Mockito.any(CallbackPojo.class))).thenReturn(true);
        
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.DYNAMIC_PROXY_BOSS_FLAG.getKey())).thenReturn("false");
        
        Mockito.when(accountService.returnFunds(Mockito.anyString())).thenReturn(false);
                
        cqWorker.exec();

        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.DYNAMIC_PROXY_BOSS_FLAG.getKey())).thenReturn("true");
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.TEST_CHARGE_RESULT.getKey())).thenReturn("true");

        Mockito.when(chargeRecordService.getRecordBySN(Mockito.anyString())).thenReturn(new ChargeRecord());
        
        Mockito.when( globalConfigService.get(GlobalConfigKeyEnum.CHARGE_SUCCESS_NOTICE.getKey())).thenReturn("true");
        
        Mockito.when(enterprisesService.selectById(Mockito.anyLong())).thenReturn(new Enterprise());
        EnterpriseSmsTemplate smsmTemplate = new EnterpriseSmsTemplate();
        smsmTemplate.setContent("尊敬的用户，{1}向您赠送了{0}MB国内流量，当月有效，感谢您的支持！");
        Mockito.when(enterpriseSmsTemplateService.getChoosedSmsTemplate(Mockito.anyLong())).thenReturn(smsmTemplate);
        Mockito.when(taskProducer.produceDeliverNoticeSmsMsg(Mockito.any(SmsPojo.class))).thenReturn(false);
        cqWorker.exec();
        
        
    }
    
    private String getTaskString(){
        ChargeDeliverPojo pojo = new ChargeDeliverPojo();
        pojo.setEntId(1L);
        pojo.setActivityName("活动名称");
        pojo.setActivityType(ActivityType.REDPACKET);
        pojo.setMobile("18867103685");
        PhoneRegion phoneRegtion = new PhoneRegion();
        phoneRegtion.setCity("杭州");
        phoneRegtion.setMobile("18867103685");
        phoneRegtion.setProvince("浙江");
        phoneRegtion.setSupplier("M");
        pojo.setPhoneRegion(phoneRegtion);
        pojo.setPrdId(1L);
        pojo.setRecordId(1L);
        pojo.setSerialNum("XXXX0001");
        pojo.setSplPrdId(1L);
        pojo.setType(ChargeType.EC_TASK.getCode());
        return JSON.toJSONString(pojo);
    }

}
