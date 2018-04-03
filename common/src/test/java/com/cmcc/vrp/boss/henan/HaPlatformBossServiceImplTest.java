package com.cmcc.vrp.boss.henan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.henan.service.HaQueryBossService;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

@RunWith(MockitoJUnitRunner.class)
public class HaPlatformBossServiceImplTest {
    @InjectMocks
    HaPlatformBossServiceImpl service = new HaPlatformBossServiceImpl();
    
    @Mock
    private GlobalConfigService globalConfigService;

    @Mock
    private HaQueryBossService haQueryService;

    @Mock
    private EnterprisesService enterprisesService;
    
    @Test
    public void testGetEnterprise(){
        Mockito.when(enterprisesService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(new Enterprise());
        assertNotNull(service.getEnterprise(2L));
    }
    
    @Test
    public void testGetQueryService(){
        assertNotNull(service.getQueryService());
    }
    
    @Test
    public void test(){
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_PLATFORM_APPKEY.getKey())).thenReturn("62f92f11d2ba5fb46191b2a84c5b27dd");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_PLATFORM_APPID.getKey())).thenReturn("505890");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_PLATFORM_SMS_TEMPLATE.getKey())).thenReturn("0");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_PLATFORM_VALID_TYPE.getKey())).thenReturn("1");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_PLATFORM_VALID_MONTH.getKey())).thenReturn("1");
     
        assertNotNull(service.getAppId());
        assertNotNull(service.getAppkey());
        assertNotNull(service.getSMS_TEMPLATE());
        assertNotNull(service.getVALID_MONTH());
        assertNotNull(service.getVALID_TYPE());        
    }
    
    @Test
    public void testGetFingerPrint(){
        assertEquals(service.getFingerPrint(), "henanplatform123456789");
    }
}
