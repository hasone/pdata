package com.cmcc.vrp.boss.ecinvoker;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.BaseBossServiceTest;
import com.cmcc.vrp.boss.ecinvoker.ecservice.EcAuthService;
import com.cmcc.vrp.boss.ecinvoker.ecservice.EcCacheService;
import com.cmcc.vrp.boss.ecinvoker.ecservice.EcChargeService;
import com.cmcc.vrp.boss.ecinvoker.impl.SdEcBossServiceImpl;
import com.cmcc.vrp.ec.bean.AuthResp;
import com.cmcc.vrp.ec.bean.AuthRespData;
import com.cmcc.vrp.ec.bean.ChargeReq;
import com.cmcc.vrp.ec.bean.ChargeResp;
import com.cmcc.vrp.ec.bean.ChargeRespData;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * Created by lilin on 2016/10/13.
 */
@RunWith(MockitoJUnitRunner.class)
public class EcInvokerServiceImplTest extends BaseBossServiceTest {

    @InjectMocks
    SdEcBossServiceImpl service = new SdEcBossServiceImpl();

    @Mock
    GlobalConfigService globalConfigService;

    @Mock
    SupplierProductService productService;

    @Mock
    EcCacheService cacheService;

    @Mock
    EcAuthService authService;

    @Mock
    EcChargeService chargeService;
    
    @Mock
    SerialNumService serialNumService;

    @Test
    public void testCharge() throws Exception {
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_SD_EC_APPKEY.getKey())).thenReturn("xxxxxx");
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_SD_EC_APPSECRET.getKey())).thenReturn("xxxxxx");
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_SD_EC_CHARGE_URL.getKey())).thenReturn("xxxxxx");
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_SD_EC_AUTH_URL.getKey())).thenReturn("xxxxxx");
        Mockito.when(productService.selectByPrimaryKey(anyLong())).thenReturn(buildSP());
        Mockito.when(cacheService.getAccessToken(anyString())).thenReturn("xxxxxx");
        Mockito.doNothing().when(cacheService).saveAccessToken(anyString(), anyString(), anyString());
        
        assertEquals(service.charge(null, 1L, "18867103685", "test123", null), EcBossOperationResultImpl.FAIL);
        assertEquals(service.charge(1L, null, "18867103685", "test123", null), EcBossOperationResultImpl.FAIL);
        assertEquals(service.charge(1L, 1L, "", "test123", null), EcBossOperationResultImpl.FAIL);
        assertEquals(service.charge(1L, 1L, "18867103685", "", null), EcBossOperationResultImpl.FAIL);
        Mockito.when(productService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(null);
        assertEquals(service.charge(1L, 1L, "18867103685", "test123", null), EcBossOperationResultImpl.FAIL);

        SupplierProduct product = new SupplierProduct();
        product.setFeature("{\"code\":\"test\"}");
        Mockito.when(productService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(product);
        Mockito.when(cacheService.getAccessToken(Mockito.anyString())).thenReturn(null);
        AuthResp response = new AuthResp();
       
        AuthRespData data = new AuthRespData();
        data.setExpiredTime("2017-05-22T12:00:00");
        response.setAuthRespData(data);
        Mockito.when(authService.auth(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(response);
        ChargeResp resp = new ChargeResp();
        resp.setResponseTime("2017-05-22T13:00:00");
        ChargeRespData respData = new ChargeRespData();
        respData.setSerialNum("test");
        respData.setSystemNum("sss");
        resp.setRespData(respData);
        Mockito.when(chargeService.charge(Mockito.any(ChargeReq.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(resp);
        Mockito.when(serialNumService.getByPltSerialNum(Mockito.anyString())).thenReturn(null);        
        assertEquals(service.charge(1L, 1L, "18867103685", "test123", null), EcBossOperationResultImpl.SUCCESS);

        Mockito.when(chargeService.charge(Mockito.any(ChargeReq.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(null);        
        Mockito.when(serialNumService.getByPltSerialNum(Mockito.anyString())).thenReturn(new SerialNum());
        Mockito.when(serialNumService.updateSerial(Mockito.any(SerialNum.class))).thenReturn(false);
        assertEquals(service.charge(1L, 1L, "18867103685", "test123", null), EcBossOperationResultImpl.FAIL);

    }

    @Override
    public SupplierProduct buildSP() {
        SupplierProduct product = super.buildSP();
        String data = "{\"code\":\"123456\"}";
        product.setFeature(data);
        return product;
    }

    private ChargeResp buildResp(String systemNum) {
        ChargeResp resp = new ChargeResp();
        ChargeRespData data = new ChargeRespData();
        data.setSystemNum(systemNum);
        resp.setRespData(data);
        resp.setResponseTime("xxxxxx");
        return resp;
    }
}
