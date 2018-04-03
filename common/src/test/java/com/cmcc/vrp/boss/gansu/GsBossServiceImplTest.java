package com.cmcc.vrp.boss.gansu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;

import com.cmcc.vrp.boss.BaseBossServiceTest;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.ecinvoker.ecservice.EcCacheService;
import com.cmcc.vrp.boss.gansu.model.GSChargeResponse;
import com.cmcc.vrp.boss.gansu.model.GSDynamicTokenResponse;
import com.cmcc.vrp.boss.gansu.model.GSInterBossResponse;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.google.gson.Gson;

/**
 * Created by lilin on 2016/10/13.
 */
@PrepareForTest({HttpUtils.class, UUID.class})
@SuppressStaticInitializationFor("com.cmcc.vrp.util.HttpUtils")
public class GsBossServiceImplTest extends BaseBossServiceTest {

    @InjectMocks
    GSBossServiceImpl gsBossService = new GSBossServiceImpl();

    @Mock
    EntProductService entProductService;

    @Mock
    SupplierProductService supplierProductService;

    @Mock
    GlobalConfigService globalConfigService;
    
    @Mock
    EcCacheService cacheService;


    @Before
    public void initMocks() {
        when(supplierProductService.selectByPrimaryKey(anyLong())).thenReturn(buildSP());
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_PRODUCT_URL.getKey())).thenReturn("productUrl");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_DYNAMIC_TOKEN_URL.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_VERSION.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_TEST_FLAG.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_APPID.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_ACCESS_TOKEN.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_USER_AUTHORIZATION_CODE.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_VALID_TYPE.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_NEW_PLAN_ID.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_NEW_BRAND.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_PROD_TYPE.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_ACTION_TYPE.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_BUS_TYPE.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_SERVICE_ID.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_VETYPE.getKey())).thenReturn("xxxxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GS_PRE_PRODID.getKey())).thenReturn("xxxxxxxx");
        
    }

    @Test
    public void testCharge() throws Exception {
        BossOperationResult result = gsBossService.charge(1l, 1l, "", "test", null);
        Assert.assertEquals(result, null);

        String bossReqNum = SerialNumGenerator.buildSerialNum();
        PowerMockito.mockStatic(HttpUtils.class);
        PowerMockito.when(HttpUtils.post(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(new Gson().toJson(buildResp()));

        PowerMockito.mockStatic(UUID.class);
        UUID uuid = PowerMockito.mock(UUID.class);
        PowerMockito.when(UUID.randomUUID()).thenReturn(uuid);
        PowerMockito.when(uuid.toString()).thenReturn("xxxxxx");

        SimpleDateFormat dateFormat = PowerMockito.mock(SimpleDateFormat.class);
        PowerMockito.whenNew(SimpleDateFormat.class).withArguments("YYYYMMDDHHMMSS").thenReturn(dateFormat);
        Date date = PowerMockito.mock(Date.class);
        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(date);
        PowerMockito.when(dateFormat.format(date)).thenReturn("xxxxxx");

        PowerMockito.when(cacheService.getAccessToken(Mockito.anyString())).thenReturn("test");
//        gsBossService = PowerMockito.spy(gsBossService);
//        PowerMockito.doReturn(buildResp()).when(gsBossService, "getRecvMsg", "xxxxxx", "xxxxxx", "xxxxxx", "xxxxxx", "18867101129", "123456");

        result = gsBossService.charge(1l, 1l, "18867103685", bossReqNum, null);
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testCharge1() throws Exception {
        BossOperationResult result = gsBossService.charge(1l, 1l, "", "test", null);
        Assert.assertEquals(result, null);

        String bossReqNum = SerialNumGenerator.buildSerialNum();
        PowerMockito.mockStatic(HttpUtils.class);
        PowerMockito.when(HttpUtils.post(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(buildAuthReq()).thenReturn(new Gson().toJson(buildResp()));

        PowerMockito.mockStatic(UUID.class);
        UUID uuid = PowerMockito.mock(UUID.class);
        PowerMockito.when(UUID.randomUUID()).thenReturn(uuid);
        PowerMockito.when(uuid.toString()).thenReturn("xxxxxx");

        SimpleDateFormat dateFormat = PowerMockito.mock(SimpleDateFormat.class);
        PowerMockito.whenNew(SimpleDateFormat.class).withArguments("YYYYMMDDHHMMSS").thenReturn(dateFormat);
        Date date = PowerMockito.mock(Date.class);
        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(date);
        PowerMockito.when(dateFormat.format(date)).thenReturn("xxxxxx");

        PowerMockito.when(cacheService.getAccessToken(Mockito.anyString())).thenReturn("");

        result = gsBossService.charge(1l, 1l, "18867103685", bossReqNum, null);
        Assert.assertNotNull(result);
    }

    private String buildAuthReq() {
        GSDynamicTokenResponse response = new GSDynamicTokenResponse();
        response.setDynamicToken("xxxxxxx");
        return new Gson().toJson(response);
    }


    private GSInterBossResponse buildResp() {
        GSInterBossResponse response = new GSInterBossResponse();
        GSChargeResponse gsChargeResponse = new GSChargeResponse();
        gsChargeResponse.setRspCode("A0000");
        response.setInterBOSS(gsChargeResponse);
        return response;
    }

    private String getReqStr() {
        return "{\"accessToken\":\"xxxxxx\",\"appId\":\"xxxxxx\"}";
    }
    
    @Test
    public void testGetFingerPrint(){
        assertEquals(gsBossService.getFingerPrint(), "2052bec3713769532a0765430fd8e7ca");
    }

    @Test
    public void testMdrcCharge(){
        assertNull(gsBossService.mdrcCharge("test", "test"));
    }
    
    @Test
    public void testSyncFromBoss(){
        assertTrue(gsBossService.syncFromBoss(1L,1L));
    }

}
