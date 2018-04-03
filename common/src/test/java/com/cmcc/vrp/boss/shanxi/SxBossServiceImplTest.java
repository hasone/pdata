package com.cmcc.vrp.boss.shanxi;

import static org.mockito.Matchers.any;
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
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.google.gson.Gson;

/**
 * Created by lilin on 2016/10/12.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SxEncrypt.class, Gson.class, HttpUtils.class})
@SuppressStaticInitializationFor("com.cmcc.vrp.util.HttpUtils")
public class SxBossServiceImplTest {

    @InjectMocks
    SxBossServiceImpl sxBossService = new SxBossServiceImpl();

    @Mock
    SupplierProductService productService;

    @Mock
    SerialNumService serialNumService;
    @Mock
    EnterprisesService enterprisesService;
    @Mock
    GlobalConfigService globalConfigService;

    /**
     * 
     */
    @Before
    public void initMocks() {
        when(productService.selectByPrimaryKey(anyLong())).thenReturn(buildSP());
        when(enterprisesService.selectById(anyLong())).thenReturn(buildEnter());
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANXI_CHARGE_URL.getKey())).thenReturn("1");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANXI_TRADESTAFFID.getKey())).thenReturn("2");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANXI_INMODECODE.getKey())).thenReturn("3");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANXI_PASSWD.getKey())).thenReturn("4");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANXI_XQJ_TRADESTAFFID.getKey())).thenReturn("5");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANXI_XQJ_INMODECODE.getKey())).thenReturn("6");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANXI_XQJ_PASSWD.getKey())).thenReturn("7");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANXI_XQJ_CODE.getKey())).thenReturn("8");
    }

    
    /**
     * @throws Exception
     */
    @Test
    public void testCharge() throws Exception {
        Assert.assertFalse(sxBossService.charge(null, null, null, null, null).isSuccess());
        Assert.assertFalse(sxBossService.charge(1l, null, null, null, null).isSuccess());
        Assert.assertFalse(sxBossService.charge(1l, 1l, "123456789", null, null).isSuccess());
        Assert.assertFalse(sxBossService.charge(1l, 1l, "18867105766", null, null).isSuccess());
        String systemNum = SerialNumGenerator.buildSerialNum();
        when(serialNumService.getByPltSerialNum(systemNum)).thenReturn(new SerialNum());
        when(serialNumService.updateSerial(any(SerialNum.class))).thenReturn(true);
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
        when(enterprisesService.selectById(anyLong())).thenReturn(buildEnter1());
        success = sxBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertTrue(success.isSuccess());
        resp.setX_RESULTCODE("1");
        resp.setX_RESULTINFO("failed");
        PowerMockito.when(gson.fromJson("xxxxxx", SxChargeResp.class)).thenReturn(resp);
       
        BossOperationResult faild = sxBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertFalse(faild.isSuccess());
        


    }
    
    
    private SxChargeResp sxRespGenerator(String resultCode) {
        SxChargeResp sxChargeResp = new SxChargeResp();
        sxChargeResp.setX_RESULTINFO(resultCode);
        return sxChargeResp;
    }
    private Enterprise buildEnter() {
        Enterprise enterprise = new Enterprise();
        enterprise.setCode("ssssss");
        return enterprise;
    }
    private Enterprise buildEnter1() {
        Enterprise enterprise = new Enterprise();
        enterprise.setCode("8");
        return enterprise;
    }
    /**
     * @return
     */
    public SupplierProduct buildSP() {
        SupplierProduct product = new SupplierProduct();
        product.setCode("123456");
        return product;
    }
}
