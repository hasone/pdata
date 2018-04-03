package com.cmcc.vrp.boss.shanxi;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import org.apache.http.impl.client.BasicResponseHandler;
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
import org.springframework.test.util.ReflectionTestUtils;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.shanxi.model.SxChargeResp;
import com.cmcc.vrp.boss.shanxi.util.SxEncrypt;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.google.gson.Gson;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年5月26日 下午12:22:19
*/
@RunWith(PowerMockRunner.class)
@PrepareForTest({SxEncrypt.class, Gson.class, HttpUtils.class})
@SuppressStaticInitializationFor("com.cmcc.vrp.util.HttpUtils")
public class SxFeeBossServiceImplTest {
    @InjectMocks
    SxFeeBossServiceImpl sxBossService = new SxFeeBossServiceImpl();

    @Mock
    SupplierProductService supplierProductService;
    @Mock
    SerialNumService serialNumService;
    @Mock
    GlobalConfigService globalConfigService;
    
    /**
     * 
     */
    @Before
    public void initMocks() {
        when(supplierProductService.selectByPrimaryKey(anyLong())).thenReturn(buildSP());
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANXI_CHARGE_URL.getKey())).thenReturn("1");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANXI_ACTIVITY_TRADESTAFFID.getKey())).thenReturn("2");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANX_ACTIVITY_INMODECODE.getKey())).thenReturn("3");
       
    }
    /**
     * 
     */
    @Test
    public void testCharge() {
        Assert.assertFalse(sxBossService.charge(null, null, null, null, null).isSuccess());
        Assert.assertFalse(sxBossService.charge(1l, null, null, null, null).isSuccess());
        Assert.assertFalse(sxBossService.charge(1l, 1l, "123456789", null, null).isSuccess());
        Assert.assertFalse(sxBossService.charge(1l, 1l, "18867105766", null, null).isSuccess());
        String systemNum = SerialNumGenerator.buildSerialNum();
        PowerMockito.mockStatic(SxEncrypt.class);
        PowerMockito.mockStatic(HttpUtils.class);
        PowerMockito.when(HttpUtils.post(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), Mockito.anyString(), 
                Mockito.any(BasicResponseHandler.class))).thenReturn("xxx");
        PowerMockito.when(SxEncrypt.decrypt(Mockito.anyString())).thenReturn("xxxxxx");
        PowerMockito.when(SxEncrypt.encrypt(Mockito.anyString())).thenReturn("xxxxxx");


        Gson gson = PowerMockito.mock(Gson.class);
        ReflectionTestUtils.setField(sxBossService, "gson", gson);
        SxChargeResp resp = new SxChargeResp();
        resp.setX_RESULTCODE("0");
        resp.setX_RESULTINFO("success");
        PowerMockito.when(gson.fromJson("xxxxxx", SxChargeResp.class)).thenReturn(resp);
        BossOperationResult success = sxBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertTrue(success.isSuccess());
    }

    private SupplierProduct buildSP() {
        SupplierProduct product = new SupplierProduct();
        product.setCode("123456");
        return product;
    }
}
