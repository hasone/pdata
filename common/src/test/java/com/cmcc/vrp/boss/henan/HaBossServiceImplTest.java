package com.cmcc.vrp.boss.henan;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.cmcc.vrp.boss.BaseBossServiceTest;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.henan.model.HaChargeResp;
import com.cmcc.vrp.boss.henan.model.HaQueryResult;
import com.cmcc.vrp.boss.henan.model.MemberDeal;
import com.cmcc.vrp.boss.henan.service.HaAuthService;
import com.cmcc.vrp.boss.henan.service.HaCacheService;
import com.cmcc.vrp.boss.henan.service.HaQueryBossService;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.google.gson.Gson;

/**
 * Created by leelyn on 2016/10/10.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpUtils.class})
@SuppressStaticInitializationFor("com.cmcc.vrp.util.HttpUtils")
public class HaBossServiceImplTest extends BaseBossServiceTest {

    @InjectMocks
    HaChannelBossServiceImpl haChannelBossService = new HaChannelBossServiceImpl();

    @Mock
    GlobalConfigService globalConfigService;

    @Mock
    EnterprisesService enterprisesService;

    @Mock
    SupplierProductService productService;

    @Mock
    HaAuthService authService;

    @Mock
    HaCacheService cacheService;

    @Mock
    SerialNumService serialNumService;

    @Mock
    HaQueryBossService haQueryService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_CHARGE_DOMAIN.getKey())).thenReturn("http://211.138.30.208:20110/oppf");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_AUTH_DOMAIN.getKey())).thenReturn("http://211.138.30.208:20200/aopoauth/oauth/token");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_APPKEY.getKey())).thenReturn("62f92f11d2ba5fb46191b2a84c5b27dd");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_APPID.getKey())).thenReturn("505890");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_GBILL_ID.getKey())).thenReturn("93713620147");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_SMS_TEMPLATE.getKey())).thenReturn("0");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_VALID_TYPE.getKey())).thenReturn("1");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_VALID_MONTH.getKey())).thenReturn("1");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_ZYHY_ENTID.getKey())).thenReturn("1");
        when(enterprisesService.selectByPrimaryKey(1l)).thenReturn(buildEnt());
        when(productService.selectByPrimaryKey(1l)).thenReturn(buildSP());
        when(cacheService.getAccessToken()).thenReturn("xxxxxxxxxx");
        when(serialNumService.getByPltSerialNum(anyString())).thenReturn(new SerialNum());
        when(serialNumService.updateSerial(any(SerialNum.class))).thenReturn(true);
        when(haQueryService.queryMemberDeal("18867101129")).thenReturn(buildMD());
        ReflectionTestUtils.setField(haChannelBossService, "gson", new Gson());   
                
    }

    @Test
    public void testCharge() {
        PowerMockito.mockStatic(HttpUtils.class);
        PowerMockito.when(HttpUtils.post(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
            .thenReturn(new Gson().toJson(bulidHaChargeResp()));
        BossOperationResult result = haChannelBossService.charge(1l, 1l, "18867101129", "123456789",null);        
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testCharge1() {
        PowerMockito.mockStatic(HttpUtils.class);
        PowerMockito.when(HttpUtils.post(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
            .thenReturn(new Gson().toJson(bulidHaChargeRespFail()));
        BossOperationResult result = haChannelBossService.charge(1l, 1l, "18867101129", "123456789",null);       
        Assert.assertFalse(result.isSuccess());
    }

    @Test
    public void testIsSuperposition() {
        boolean flag1 = haChannelBossService.isSuperposition("中移杭研", "18867101129");
        boolean flag2 = haChannelBossService.isSuperposition("中移(杭州)信息技术有限公司", "18867101129");
        boolean flag3 = haChannelBossService.isSuperposition("中移(杭州)信息技术有限公司", "18867100000");
        Assert.assertFalse(flag1);
        Assert.assertTrue(flag2);
        Assert.assertFalse(flag3);
    }

    private MemberDeal buildMD() {
        MemberDeal memberDeal = new MemberDeal();
        List<HaQueryResult> list = new ArrayList<HaQueryResult>();
        HaQueryResult result = new HaQueryResult();
        result.setGROUP_NAME("中移(杭州)信息技术有限公司");
        list.add(result);
        memberDeal.setSO_MEMBER_DEAL(list);
        return memberDeal;
    }
    
    private HaChargeResp bulidHaChargeResp(){
        HaChargeResp resp = new HaChargeResp();
        resp.setRespCode("00000");
        resp.setRespDesc("test");
        return resp;
    }
    
    private HaChargeResp bulidHaChargeRespFail(){
        HaChargeResp resp = new HaChargeResp();
        resp.setRespCode("00001");
        resp.setRespDesc("test");
        return resp;
    }
}
