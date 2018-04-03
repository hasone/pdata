package com.cmcc.vrp.boss.jiangsu;

import static org.mockito.Matchers.anyLong;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import com.cmcc.vrp.boss.BossQueryResult;
import com.cmcc.vrp.boss.jiangsu.util.JsHttpUtils;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.sun.mail.iap.ResponseHandler;

/**
 * @author lgk8023
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpUtils.class,JsHttpUtils.class})
@SuppressStaticInitializationFor("com.cmcc.vrp.util.HttpUtils")
public class JsBossQueryServiceImplTest {
    @InjectMocks
    JsBossQueryServiceImpl bossService = new JsBossQueryServiceImpl();
    
    @Mock
    ChargeRecordService chargeRecordService;
    @Mock
    SupplierProductService supplierProductService;
    @Mock
    GlobalConfigService globalConfigService;  
    @Mock
    SerialNumService serialNumService;
    /**
     * 
     */
    @Before
    public void initMocks() {
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.JS_BOSS_APPID.getKey())).thenReturn("109000000067");
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.JS_BOSS_ACCESS_TOKEN.getKey())).thenReturn("ABCDE12345ABCDE12410");
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.JS_BOSS_OPERATOR_ID.getKey())).thenReturn("17777704");
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.JS_BOSS_CHARGE_URL.getKey())).thenReturn("http://test");  

    }
    
    /**
     * 
     */
    @Test
    public void testQueryStatus() {
        String mString = "<?xml version=\"1.0\" encoding=\"GBK\" ?><operation_out><process_code>cc_qryofferorders</process_code><sysfunc_id></sysfunc_id>" 
                + "<request_seq>1234567890</request_seq><request_source></request_source><request_type></request_type>" 
                + "<response_time>20171031163149</response_time><content><ret_code>0</ret_code><qrylist></qrylist><qrylist></qrylist>" 
                + "<qrylist><item><ordertime>20171030173515</ordertime>" 
                + "<orderid>200084995123359324</orderid><orderstatus>1</orderstatus><orderchannelid>707</orderchannelid></item></qrylist>" 
                + "<qrylist><item><ordertime>20171027000029</ordertime>" 
                + "<orderid>200084356859599324</orderid><orderstatus>1</orderstatus><orderchannelid>707</orderchannelid></item></qrylist>" 
                + "<qrylist><item><ordertime>20171027112908</ordertime>" 
                + "<orderid>200084505124219324</orderid><orderstatus>1</orderstatus><orderchannelid>707</orderchannelid></item></qrylist>" 
                + "<qrylist><item><ordertime>20171028123003</ordertime>" 
                + "<orderid>200084691414529324</orderid><orderstatus>1</orderstatus><orderchannelid>707</orderchannelid></item></qrylist>" 
                + "</content><response><resp_result>0</resp_result><resp_code>0000</resp_code><resp_desc>成功</resp_desc></response>" 
                + "<emergency_status>0</emergency_status></operation_out>";
        
        String mString2 = "<?xml version=\"1.0\" encoding=\"GBK\" ?><operation_out><process_code>cc_qryofferorders</process_code><sysfunc_id>" 
                + "</sysfunc_id><response_time>20170928133917</response_time><request_source></request_source><request_seq>1234567890</request_seq>" 
                + "<request_type></request_type><response><resp_type>1</resp_type><resp_code>-1</resp_code><resp_desc>" 
                + "<![CDATA[查找业务 cc_qryofferorders 对应的业务解析方式失败]]></resp_desc></response><emergency_status>0</emergency_status></operation_out>";
        PowerMockito.mockStatic(HttpUtils.class);
        PowerMockito.mockStatic(JsHttpUtils.class);
        Assert.assertNotNull(bossService.queryStatus(null));

        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setChargeTime(DateUtil.parse("yyyyMMddHHmmSS", "20171027000000"));
        chargeRecord.setSupplierProductId(1l);
        chargeRecord.setPhone("18206218277");
        SupplierProduct supplierProduct = new SupplierProduct();
        supplierProduct.setCode("2000007865");
        PowerMockito.when(chargeRecordService.getRecordBySN(Mockito.anyString())).thenReturn(null).thenReturn(chargeRecord);
        Assert.assertNotNull(bossService.queryStatus("1234"));
        PowerMockito.when(supplierProductService.selectByPrimaryKey(anyLong())).thenReturn(null).thenReturn(supplierProduct);
        Assert.assertNotNull(bossService.queryStatus("1234"));
        
        PowerMockito.when(serialNumService.getByPltSerialNum(Mockito.anyString())).thenReturn(null);
        
        PowerMockito.when(JsHttpUtils.post(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), 
                Mockito.anyString(), (org.apache.http.client.ResponseHandler<String>) Mockito.any(ResponseHandler.class)))
            .thenReturn(mString);

        Assert.assertEquals("100", bossService.queryStatus("1234567890").getCode());
        
        PowerMockito.when(JsHttpUtils.post(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), 
                Mockito.anyString(), (org.apache.http.client.ResponseHandler<String>) Mockito.any(ResponseHandler.class)))
            .thenReturn(mString2);
        BossQueryResult result = bossService.queryStatus("1234567890");
        System.out.println(result.getMsg());
        Assert.assertEquals("101", result.getCode());
    }
    /**
     * @return
     */
    public SupplierProduct buildSupplierProduct() {
        SupplierProduct supplierProduct = new SupplierProduct();
        supplierProduct.setCode("1234");
        supplierProduct.setFeature("{\"elementId\":\"20151029\", \"packageId\":\"20151028\", \"productId\":\"7176\"}");
        return supplierProduct;
    }
    /**
     * @return
     */
    public Enterprise buildEnt() {
        Enterprise enterprise = new Enterprise();
        enterprise.setCode("1234");
        return enterprise;
    }
}
