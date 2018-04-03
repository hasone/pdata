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

import com.cmcc.vrp.boss.jiangsu.util.JsHttpUtils;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
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
public class JsBossServiceImplTest {
    @InjectMocks
    JsBossServiceImpl bossService = new JsBossServiceImpl();
    @Mock
    SupplierProductService supplierProductService;  
    @Mock
    EnterprisesService enterprisesService;    
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
    public void testCharge() {
        String mString = "<?xml version=\"1.0\" encoding=\"GBK\" ?><operation_out><process_code>cc_cchgpkgforpro</process_code>" 
                + "<sysfunc_id></sysfunc_id><response_time>20170605102604</response_time>" 
                + "<request_source></request_source><request_seq></request_seq><request_type></request_type><response>" 
                + "<resp_result>0</resp_result><resp_code>0000</resp_code><resp_desc>Success.</resp_desc></response><content>" 
                + "<ret_code>0</ret_code><cc_operating_srl>200005970660346237</cc_operating_srl></content><emergency_status>0</emergency_status>" 
                + "</operation_out>";
        String mString2 = "<?xml version=\"1.0\" encoding=\"GBK\" ?><operation_out><request_type></request_type><request_seq></request_seq>" 
                + "<response_time>20170605094140</response_time><response_seq></response_seq><response><resp_type>-4</resp_type>" 
                + "<resp_code>-20</resp_code><resp_desc>HTTP调用超时[url=http://10.33.254.26:6631/fcgi-bin/AbilityPlatForNet]</resp_desc>" 
                + "</response><content></content></operation_out>";
        String mString3 = "<?xml version=\"1.0\" encoding=\"GBK\" ?><operation_out><process_code>cc_cchgpkgforpro</process_code>" 
                + "<sysfunc_id></sysfunc_id><response_time>20170601171518</response_time><request_source></request_source><request_seq>" 
                + "</request_seq><request_type></request_type><response><resp_type>1</resp_type><resp_code>14802102</resp_code>" 
                + "<resp_desc>下游返回出参不满足业务要求，例如返回报文为空、返回报文不完整等.</resp_desc></response><emergency_status>0</emergency_status></operation_out>";
        PowerMockito.mockStatic(HttpUtils.class);
        PowerMockito.mockStatic(JsHttpUtils.class);
        Assert.assertNull(bossService.charge(null, null, null, null, null));
        Assert.assertNull(bossService.charge(1l, null, null, null, null));
        Assert.assertNull(bossService.charge(1l, 1l, null, null, null));
        Assert.assertNull(bossService.charge(1l, 1l, "18867105766", null, null));
        Assert.assertNull(bossService.charge(1l, 1l, "188671057", "123456", null));
        PowerMockito.when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(null).thenReturn(buildEnt());
        Assert.assertNull(bossService.charge(1l, 1l, "18867105766", "123456", null));
        PowerMockito.when(supplierProductService.selectByPrimaryKey(anyLong())).thenReturn(null).thenReturn(buildSupplierProduct());
        Assert.assertNull(bossService.charge(1l, 1l, "18867105766", "123456", null));
        
        PowerMockito.when(JsHttpUtils.post(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), 
                Mockito.anyString(), (org.apache.http.client.ResponseHandler<String>) Mockito.any(ResponseHandler.class)))
            .thenReturn(mString);
        PowerMockito.when(serialNumService.getByPltSerialNum(Mockito.anyString())).thenReturn(new SerialNum());
        PowerMockito.when(serialNumService.updateSerial(Mockito.any(SerialNum.class))).thenReturn(true);
        Assert.assertTrue(bossService.charge(1l, 1l, "18867105766", "123456", null).isSuccess());
        PowerMockito.when(JsHttpUtils.post(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), 
                Mockito.anyString(), (org.apache.http.client.ResponseHandler<String>) Mockito.any(ResponseHandler.class)))
            .thenReturn(mString2);
        Assert.assertFalse(bossService.charge(1l, 1l, "18867105766", "123456", null).isSuccess());
        PowerMockito.when(JsHttpUtils.post(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), 
                Mockito.anyString(), (org.apache.http.client.ResponseHandler<String>) Mockito.any(ResponseHandler.class)))
            .thenReturn(mString3);
        Assert.assertFalse(bossService.charge(1l, 1l, "18867105766", "123456", null).isSuccess());
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
