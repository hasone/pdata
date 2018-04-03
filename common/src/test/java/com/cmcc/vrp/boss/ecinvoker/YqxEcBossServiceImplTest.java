package com.cmcc.vrp.boss.ecinvoker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.ecinvoker.ecservice.EcAuthService;
import com.cmcc.vrp.boss.ecinvoker.ecservice.EcCacheService;
import com.cmcc.vrp.boss.ecinvoker.ecservice.EcChargeService;
import com.cmcc.vrp.boss.ecinvoker.impl.YqxEcBossServiceImpl;
import com.cmcc.vrp.ec.bean.AuthResp;
import com.cmcc.vrp.ec.bean.AuthRespData;
import com.cmcc.vrp.ec.bean.ChargeReq;
import com.cmcc.vrp.ec.bean.ChargeResp;
import com.cmcc.vrp.ec.bean.ChargeRespData;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.YqxChargeInfo;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.YqxChargeInfoService;

/**
 * YqxEcBossServiceImplTest.java
 * @author wujiamin
 * @date 2017年5月22日
 */
@RunWith(MockitoJUnitRunner.class)
public class YqxEcBossServiceImplTest {
    @InjectMocks
    BossService service = new YqxEcBossServiceImpl();
    
    @Mock
    private ProductService productService;

    @Mock
    private EcCacheService cacheService;

    @Mock
    private EcAuthService authService;

    @Mock
    private EcChargeService chargeService;

    @Mock
    private YqxChargeInfoService yqxChargeInfoService;
    
    @Mock
    private GlobalConfigService globalConfigService;
    
    @Test
    public void testGetFingerPrint(){
        assertNull(service.getFingerPrint());
    }
    
    @Test
    public void testCharge(){        
        Mockito.when(globalConfigService.get(Mockito.anyString())).thenReturn("test");
        assertEquals(service.charge(null, null, "18867103685", "test123", null), EcBossOperationResultImpl.FAIL);
        assertEquals(service.charge(null, 1L, "", "test123", null), EcBossOperationResultImpl.FAIL);
        assertEquals(service.charge(null, 1L, "18867103685", "", null), EcBossOperationResultImpl.FAIL);
        Mockito.when(productService.selectProductById(Mockito.anyLong())).thenReturn(null);
        assertEquals(service.charge(null, 1L, "18867103685", "test123", null), EcBossOperationResultImpl.FAIL);
        
        Mockito.when(productService.selectProductById(Mockito.anyLong())).thenReturn(new Product());        
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
        Mockito.when(yqxChargeInfoService.selectBySerialNum(Mockito.anyString())).thenReturn(new YqxChargeInfo());
        Mockito.when(yqxChargeInfoService.updateReturnSystemNum(Mockito.any(YqxChargeInfo.class))).thenReturn(false);    
        assertEquals(service.charge(null, 1L, "18867103685", "test123", null), EcBossOperationResultImpl.SUCCESS);
        
        Mockito.when(chargeService.charge(Mockito.any(ChargeReq.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(new ChargeResp());
        assertEquals(service.charge(null, 1L, "18867103685", "test123", null), EcBossOperationResultImpl.FAIL);
        
    }
}
