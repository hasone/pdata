package com.cmcc.vrp.boss.sichuan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.sichuan.model.SCBalanceRequest;
import com.cmcc.vrp.boss.sichuan.model.SCBalanceResponse;
import com.cmcc.vrp.boss.sichuan.model.SCBalanceResponseOutData;
import com.cmcc.vrp.boss.sichuan.model.SCChargeRequest;
import com.cmcc.vrp.boss.sichuan.model.SCChargeResponse;
import com.cmcc.vrp.boss.sichuan.service.SCAddMemberService;
import com.cmcc.vrp.boss.sichuan.service.ScMemberInquiryService;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterpriseSmsTemplate;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterpriseSmsTemplateService;
import com.cmcc.vrp.province.service.EnterpriseUserIdService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.webservice.sichuan.pojo.ReturnCode;

@RunWith(MockitoJUnitRunner.class)
public class ScBossServiceImplTest {
    @InjectMocks
    BossService serivce = new SCBossServiceImpl();
    
    @Mock
    SCAddMemberService scAddMemberService;

    @Mock
    EnterpriseUserIdService enterpriseUserIdService;

    @Mock
    EnterprisesService enterprisesService;

    @Mock
    SupplierProductService supplierProductService;

    @Mock
    EnterpriseSmsTemplateService enterpriseSmsTemplateService;

    @Mock
    com.cmcc.vrp.boss.sichuan.service.SCBalanceService SCBalanceService;

    @Mock
    GlobalConfigService globalConfigService;

    @Mock
    EntProductService entProductService;

    @Mock
    AccountService accountService;
    
    @Mock
    ScMemberInquiryService scMemberInquiryService;
    
    @Mock
    TaskProducer taskProducer;
    
    @Test
    public void testCharge(){
        assertEquals(serivce.charge(null, 1L, "18867103685", "test", null).getResultCode(), 
                ReturnCode.parameter_error.getCode());
        
        Mockito.when(enterprisesService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(null);
        assertEquals(serivce.charge(1L, 1L, "18867103685", "test", null).getResultCode(), 
                ReturnCode.parameter_error.getCode());
    
        Enterprise e = new Enterprise();
        e.setCode("test");
        Mockito.when(enterprisesService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(e);
        assertEquals(serivce.charge(1L, null, "18867103685", "test", null).getResultCode(), 
                ReturnCode.parameter_error.getCode());
        
        Mockito.when(supplierProductService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(null);
        assertEquals(serivce.charge(1L, 1L, "18867103685", "test", null).getResultCode(), 
                ReturnCode.parameter_error.getCode());
        
        SupplierProduct p = new SupplierProduct();
        p.setFeature("v");
        Mockito.when(supplierProductService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(p);
        assertEquals(serivce.charge(1L, 1L, "", "test", null).getResultCode(), 
                ReturnCode.parameter_error.getCode());
        
        
        Mockito.when(scMemberInquiryService.getdayRange(Mockito.anyString())).thenReturn(null);
        assertEquals(serivce.charge(1L, 1L, "18867103685", "test", null).getResultCode(), 
                ReturnCode.no_date_range.getCode());
        
        Mockito.when(scMemberInquiryService.getdayRange(Mockito.anyString())).thenReturn(0);
        assertEquals(serivce.charge(1L, 1L, "18867103685", "test", null).getResultCode(), 
                ReturnCode.over_date_range.getCode());
        
        Mockito.when(scMemberInquiryService.getdayRange(Mockito.anyString())).thenReturn(2);        
        
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_DATE_TYPE.getKey())).thenReturn("1");
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.SC_ACCOUNT_BALANCE.getKey())).thenReturn("100");
        Mockito.when(globalConfigService.get("SC_BOSSBALANCE_CHECK")).thenReturn("OK");
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.SC_MIGU_CODE.getKey())).thenReturn("test1,test2");
        SCBalanceResponse balanceResponse = new SCBalanceResponse();
        balanceResponse.setResCode("0000000");
        SCBalanceResponseOutData outData = new SCBalanceResponseOutData();
        outData.setPREPAY_FEE("10");
        balanceResponse.setOutData(outData );
        Mockito.when(SCBalanceService.sendBalanceRequest(Mockito.any(SCBalanceRequest.class))).thenReturn(balanceResponse);
        Mockito.when(enterprisesService.updateByPrimaryKeySelective(Mockito.any(Enterprise.class))).thenReturn(true);
        
        //assertNotNull(serivce.charge(1L, 1L, "18867103685", "test", null));
                
        //余额够的情况下
        outData.setPREPAY_FEE("100000");
        balanceResponse.setOutData(outData );
        Mockito.when(SCBalanceService.sendBalanceRequest(Mockito.any(SCBalanceRequest.class))).thenReturn(balanceResponse);
        SCChargeResponse scChargeResponse = new SCChargeResponse();
        scChargeResponse.setResCode("0000000");
        Mockito.when(scAddMemberService.sendChargeRequest(Mockito.any(SCChargeRequest.class))).thenReturn(scChargeResponse);
        EnterpriseSmsTemplate t = new EnterpriseSmsTemplate();
        t.setContent("test");
        Mockito.when(enterpriseSmsTemplateService.getChoosedSmsTemplate(Mockito.anyLong())).thenReturn(t);
        Mockito.when(taskProducer.produceDeliverNoticeSmsMsg(Mockito.any(SmsPojo.class))).thenReturn(true);
        
        assertNotNull(serivce.charge(1L, 1L, "18867103685", "test", 1024L));
        
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_DATE_TYPE.getKey())).thenReturn("1");        
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_START_DAY.getKey())).thenReturn("5");
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_START_TIME.getKey())).thenReturn("9:00:00");
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_END_DAY.getKey())).thenReturn("5");
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_END_TIME.getKey())).thenReturn("9:00:00");
        assertNotNull(serivce.charge(1L, 1L, "18867103685", "test", 1024L));
        
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_DATE_TYPE.getKey())).thenReturn("2");        
        assertNotNull(serivce.charge(1L, 1L, "18867103685", "test", 1024L));
    
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_END_DAY.getKey())).thenReturn("2");
        assertNotNull(serivce.charge(1L, 1L, "18867103685", "test", 1024L));
    }
}
