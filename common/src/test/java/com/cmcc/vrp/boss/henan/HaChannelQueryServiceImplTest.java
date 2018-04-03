package com.cmcc.vrp.boss.henan;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.cmcc.vrp.boss.BossQueryResult;
import com.cmcc.vrp.boss.henan.Util.HaAES256;
import com.cmcc.vrp.boss.henan.model.HaQueryStatusResp;
import com.cmcc.vrp.boss.henan.model.HaStatusResult;
import com.cmcc.vrp.boss.henan.service.HaQueryBossService;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;

/**
 * HaChannelQueryImplTest.java
 * @author wujiamin
 * @date 2017年5月26日
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({HaAES256.class})
public class HaChannelQueryServiceImplTest {
    @InjectMocks
    HaChannelQueryServiceImpl service = new HaChannelQueryServiceImpl();
    
    @Mock
    private ChargeRecordService recordService;

    @Mock
    private HaQueryBossService haQueryBossService;

    @Mock
    private SerialNumService serialNumService;
    
    @Mock
    GlobalConfigService globalConfigService;
    
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

        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setPhone("18867103685");
        when(recordService.getRecordBySN(Mockito.anyString())).thenReturn(chargeRecord);
        
        SerialNum serialNum = new SerialNum();
        serialNum.setBossReqSerialNum("test");
        when(serialNumService.getByPltSerialNum(Mockito.anyString())).thenReturn(serialNum); 
        
        HaQueryStatusResp resp = new HaQueryStatusResp();
        resp.setRespCode("00000");
        resp.setRespDesc("test");
        resp.setResult("test");
        when(haQueryBossService.queryMemberStatus(Mockito.anyString(), Mockito.anyString())).thenReturn(resp);
        
        
        ReflectionTestUtils.setField(service, "gson", new Gson());   
                
    }
    
    @Test
    public void testQueryStatus(){
        assertEquals(service.queryStatus(""), BossQueryResult.FAILD);
        
        PowerMockito.mockStatic(HaAES256.class);
        
        HaStatusResult result = new HaStatusResult();
        result.setBILL_ID("test");
        result.setMEM_SRVPKG_DESC("test");
        result.setSTATE("test");
        result.setDEAL_RESULT("正在受理");
        
        PowerMockito.when(HaAES256.decryption(Mockito.anyString(), Mockito.anyString())).thenReturn(new Gson().toJson(result));
        assertEquals(service.queryStatus("test"), BossQueryResult.PROCESSING);
        
        result.setDEAL_RESULT("受理成功");
        PowerMockito.when(HaAES256.decryption(Mockito.anyString(), Mockito.anyString())).thenReturn(new Gson().toJson(result)); 
        assertEquals(service.queryStatus("test"), BossQueryResult.SUCCESS);
        
        result.setDEAL_RESULT("");
        PowerMockito.when(HaAES256.decryption(Mockito.anyString(), Mockito.anyString())).thenReturn(new Gson().toJson(result)); 
        assertEquals(service.queryStatus("test"), BossQueryResult.FAILD);        
    }
    
    @Test
    public void testGetFingerPrint(){
        assertEquals(service.getFingerPrint(), "henan123456789");
    }

}
