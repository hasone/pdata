package com.cmcc.vrp.boss.ecinvoker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
import com.cmcc.vrp.boss.ecinvoker.impl.NmEcBossServiceImpl;
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

/**
 * NmEcBossOperationResultImplTest.java
 * @author wujiamin
 * @date 2017年5月27日
 */
@RunWith(MockitoJUnitRunner.class)
public class NmEcBossServiceImplTest {
    @InjectMocks
    BossService service = new NmEcBossServiceImpl();
    
    @Mock
    private SupplierProductService productService;

    @Mock
    private EcCacheService cacheService;

    @Mock
    private EcAuthService authService;

    @Mock
    private EcChargeService chargeService;

    @Mock
    private SerialNumService serialNumService;
    
    @Mock
    private GlobalConfigService globalConfigService;
    
    @Test
    public void testGetFingerPrint(){
        assertNotNull(service.getFingerPrint());
    }
    
    @Test
    public void testCharge(){        
        Mockito.when(globalConfigService.get(Mockito.anyString())).thenReturn("test");
        assertEquals(service.charge(null, 1L, "18867103685", "test123", null), NmEcBossOperationResultImpl.FAIL);
        assertEquals(service.charge(1L, null, "", "test123", null), NmEcBossOperationResultImpl.FAIL);
        assertEquals(service.charge(1l, 1L, "18867103685", "", null), NmEcBossOperationResultImpl.FAIL);
        Mockito.when(productService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(null);
        assertEquals(service.charge(1l, 1L, "18867103685", "test123", null), NmEcBossOperationResultImpl.FAIL);
        
        Mockito.when(productService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(new SupplierProduct());        
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
        assertEquals(service.charge(1L, 1L, "18867103685", "test123", null), NmEcBossOperationResultImpl.SUCCESS);
        
        Mockito.when(serialNumService.getByPltSerialNum(Mockito.anyString())).thenReturn(new SerialNum());
        Mockito.when(serialNumService.updateSerial(Mockito.any(SerialNum.class))).thenReturn(false);
        assertEquals(service.charge(1L, 1L, "18867103685", "test123", null), NmEcBossOperationResultImpl.SUCCESS);
        
        Mockito.when(chargeService.charge(Mockito.any(ChargeReq.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(new ChargeResp());
        assertEquals(service.charge(1L, 1L, "18867103685", "test123", null), NmEcBossOperationResultImpl.FAIL);        
    }
    
    @Test
    public void testMdrcCharge() {
        assertNull(service.mdrcCharge("test", "test"));
    }

    @Test
    public void testSyncFromBoss() {
        assertTrue(service.syncFromBoss(1L, 1L));
    }

}
