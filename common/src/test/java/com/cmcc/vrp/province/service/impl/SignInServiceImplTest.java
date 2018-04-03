package com.cmcc.vrp.province.service.impl;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.province.cache.CacheService;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.WxAdminister;
import com.cmcc.vrp.province.model.WxSerialSignRecord;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.RandomPointService;
import com.cmcc.vrp.province.service.SignInService;
import com.cmcc.vrp.province.service.WxAdministerService;
import com.cmcc.vrp.province.service.WxSerialSignRecordService;
import com.cmcc.vrp.util.StringUtils;

/**
 * 签到服务测试
 *
 * Created by sunyiwei on 2017/2/27.
 */
@RunWith(MockitoJUnitRunner.class)
public class SignInServiceImplTest {
    private final String PREFIX = "sign.in.";

    @InjectMocks
    SignInService signInService = new SignInServiceImpl();

    @Mock
    private RandomPointService randomPointService;

    @Mock
    private IndividualAccountService individualAccountService;

    @Mock
    private WxAdministerService wxAdministerService;

    @Mock
    private IndividualProductService individualProductService;

    @Mock
    private CacheService cacheService;
    @Mock
    GlobalConfigService globalConfigService;
    @Mock
    WxSerialSignRecordService wxSerialSignRecordService;

    /**
     * 测试异常逻辑
     */
    @Test
    public void testSign() throws Exception {
        //测试异常状况
        assertFalse(signInService.sign(emptyMobile(), "43343", 1));
        assertFalse(signInService.sign(invalidMobile(), "43343", 1));
        assertFalse(signInService.sign("18867102100", null, 1));
        assertFalse(signInService.sign("18867102100", "", 1));
    }

    /**
     * 测试刷积分逻辑
     */
    @Test
    public void testSign2() throws Exception {
        when(cacheService.getIncrOrUpdate(anyString(), anyInt()))
                .thenReturn(101l); //超过每天的防刷上限了,用户将被加入黑名单

        assertFalse(signInService.sign(validMobile(), validSN(), 1));

        verify(cacheService, times(1)).getIncrOrUpdate(anyString(), anyInt());
    }

    /**
     * 测试超过每日次数上限逻辑
     */
    @Test
    public void testSign3() throws Exception {
        when(cacheService.getIncrOrUpdate(anyString(), anyInt()))
                .thenReturn(2l); //超过每天的上限了,用户将无法继续签到

        assertFalse(signInService.sign(validMobile(), validSN(), 1));

        verify(cacheService, times(1)).getIncrOrUpdate(anyString(), anyInt());
    }

    /**
     * 测试无法获取积分产品的情况
     */
    @Test
    public void testSign4() throws Exception {
        when(cacheService.getIncrOrUpdate(anyString(), anyInt()))
                .thenReturn(1l);

        when(individualProductService.getIndivialPointProduct())
                .thenReturn(null);

        assertFalse(signInService.sign(validMobile(), validSN(), 1));

        verify(cacheService, times(1)).getIncrOrUpdate(anyString(), anyInt());

        verify(individualProductService, times(1)).getIndivialPointProduct();
    }

    /**
     * 测试增加账户积分失败的情况
     */
    @Test
    public void testSign5() throws Exception {
        when(cacheService.getIncrOrUpdate(anyString(), anyInt()))
                .thenReturn(1l);

        when(individualProductService.getIndivialPointProduct())
                .thenReturn(new IndividualProduct());

        when(individualAccountService.addCountForcely(anyString(), anyLong(), anyString(), any(BigDecimal.class), any(ActivityType.class), anyString()))
                .thenReturn(false);

        assertFalse(signInService.sign(validMobile(), validSN(), 1));

        verify(cacheService, times(1)).getIncrOrUpdate(anyString(), anyInt());

        verify(individualProductService, times(1)).getIndivialPointProduct();

        verify(individualAccountService, times(1)).addCountForcely(anyString(), anyLong(), anyString(), 
                any(BigDecimal.class), any(ActivityType.class), anyString());
    }

    /**
     * 测试正常的业务逻辑
     */
    @Test
    public void testSign6() throws Exception {
        when(cacheService.getIncrOrUpdate(anyString(), anyInt()))
                .thenReturn(1l);

        when(individualProductService.getIndivialPointProduct())
                .thenReturn(new IndividualProduct());

        when(individualAccountService.addCountForcely(anyString(), anyLong(), anyString(), any(BigDecimal.class), any(ActivityType.class), anyString()))
                .thenReturn(true);

        assertTrue(signInService.sign(validMobile(), validSN(), 1));

        verify(cacheService, times(1)).getIncrOrUpdate(anyString(), anyInt());

        verify(individualProductService, times(1)).getIndivialPointProduct();

        verify(individualAccountService, times(1)).addCountForcely(anyString(), anyLong(), anyString(), any(BigDecimal.class), 
                any(ActivityType.class), anyString());
    }

    private String validSN() {
        return StringUtils.randomString(10);
    }

    private String validMobile() {
        return "18867102100";
    }

    private String invalidMobile() {
        return "1886710";
    }

    private String emptyMobile() {
        return "";
    }
    
    private WxAdminister createAdminister(){
        WxAdminister admin = new WxAdminister();
        admin.setId(1L);
        return admin;
    }
    
    @Test
    public void testNewSign(){
        String mobile = "18867101234";
        
        assertNotNull(signInService.newSign("", new Date()));
        
        Mockito.when(wxAdministerService.selectWxAdministerById(Mockito.anyLong())).thenReturn(createAdminister());
        Mockito.when(wxAdministerService.selectByMobilePhone(Mockito.anyString())).thenReturn(createAdminister());
        
        Mockito.when(cacheService.get(Mockito.anyString())).thenReturn("true").thenReturn("false");
        
        assertNotNull(signInService.newSign(mobile, new Date()));
        
        Mockito.when(cacheService.setNxAndExpireTime(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenReturn(true);
        Mockito.when(cacheService.lPush(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenReturn(1L);
        Mockito.when(cacheService.setNxAndExpireTime(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenReturn(true);
        
        Mockito.when(wxSerialSignRecordService.selectByMap(Mockito.anyMap())).thenReturn(new ArrayList<WxSerialSignRecord>());
        Mockito.when(wxSerialSignRecordService.insertSelective(Mockito.any(WxSerialSignRecord.class))).thenReturn(false);
        
        Mockito.when(cacheService.delete(Mockito.anyString())).thenReturn(false);
        Mockito.when(cacheService.deleteValueOnList(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString())).thenReturn(false);
        
        assertNotNull(signInService.newSign(mobile, new Date()));
    }
    
    private List<WxSerialSignRecord> createWxSerialSignRecordList(){
        List<WxSerialSignRecord> list = new ArrayList<WxSerialSignRecord>();
        WxSerialSignRecord record = new WxSerialSignRecord();
        record.setId(1L);
        record.setStartTime(new Date());
        list.add(record);
        return list;
    }
    
    @Test
    public void testNewSign1(){
        String mobile = "18867101234";
        
        
        Mockito.when(wxAdministerService.selectWxAdministerById(Mockito.anyLong())).thenReturn(createAdminister());
        Mockito.when(wxAdministerService.selectByMobilePhone(Mockito.anyString())).thenReturn(createAdminister());
        
        Mockito.when(cacheService.get(Mockito.anyString())).thenReturn("false");
        
        
        Mockito.when(cacheService.setNxAndExpireTime(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenReturn(true);
        Mockito.when(cacheService.lPush(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenReturn(1L);
        Mockito.when(cacheService.setNxAndExpireTime(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenReturn(true);
        
        Mockito.when(wxSerialSignRecordService.selectByMap(Mockito.anyMap())).thenReturn(createWxSerialSignRecordList());
        
        Mockito.when(cacheService.delete(Mockito.anyString())).thenReturn(false);
        Mockito.when(cacheService.deleteValueOnList(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString())).thenReturn(false);
        
        assertNotNull(signInService.newSign(mobile, new Date()));
    }  
}