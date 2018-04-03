package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.sichuan.model.SCOpenRequest;
import com.cmcc.vrp.boss.sichuan.model.SCOpenResponse;
import com.cmcc.vrp.boss.sichuan.model.SCOpenResponseOutData;
import com.cmcc.vrp.boss.sichuan.service.SCOpenService;
import com.cmcc.vrp.province.dao.DiscountMapper;
import com.cmcc.vrp.province.dao.DistrictMapper;
import com.cmcc.vrp.province.dao.EnterpriseUserIdMapper;
import com.cmcc.vrp.province.model.Discount;
import com.cmcc.vrp.province.model.District;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterpriseUserId;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.EntManagerService;
import com.cmcc.vrp.province.service.EnterpriseUserIdService;
import com.cmcc.vrp.province.service.ManagerService;


@RunWith(MockitoJUnitRunner.class)
public class EnterpriseUserIdServiceImplTest {
    @InjectMocks
    EnterpriseUserIdService enterpriseUserIdService = new EnterpriseUserIdServiceImpl();

    @Mock
    EnterpriseUserIdMapper enterpriseUserIdMapper;

    
    @Mock
    DiscountMapper discountMapper;
    
    @Mock
    EntManagerService entManagerService;
    
    @Mock
    ManagerService managerService;
    
    @Mock
    DistrictMapper districtMapper;
    
    @Mock
    SCOpenService scOpenService;
    
    @Test
    public void getUserIdByEnterpriseCode() {
        when(enterpriseUserIdMapper.getUserIdByEnterpriseCode("123")).thenReturn("11");
        assertEquals(enterpriseUserIdService.getUserIdByEnterpriseCode("123"), "11");
        verify(enterpriseUserIdMapper, times(1)).getUserIdByEnterpriseCode("123");
    }

    @Test
    public void testSaveUserId(){
        when(discountMapper.selectByPrimaryKey(1L)).thenReturn(getDiscount());
        Manager manager = new Manager();
        manager.setParentId(1L);
        when(entManagerService.getManagerForEnter(1L)).thenReturn(manager);
        Manager fatherManager = new Manager();
        fatherManager.setId(1L);
        fatherManager.setName("测试");
        when(managerService.selectByPrimaryKey(1L)).thenReturn(fatherManager);
        List<District> districts = new ArrayList();
        District d = new District();
        d.setCode("11");
        districts.add(d);
        when(districtMapper.selectByName(fatherManager.getName())).thenReturn(districts);
        
        when(enterpriseUserIdMapper.getUserIdByEnterpriseCode("123")).thenReturn("11");
        
        when(scOpenService.sendOpenRequest(Mockito.any(SCOpenRequest.class))).thenReturn(null);
        assertFalse(enterpriseUserIdService.saveUserId(getEnter()));
        
        
        when(enterpriseUserIdMapper.getUserIdByEnterpriseCode("123")).thenReturn(null);        
        when(scOpenService.sendOpenRequest(Mockito.any(SCOpenRequest.class))).thenReturn(null);
        assertFalse(enterpriseUserIdService.saveUserId(getEnter()));
        
        SCOpenResponse response = new SCOpenResponse();       
        response.setResCode("0");
        when(scOpenService.sendOpenRequest(Mockito.any(SCOpenRequest.class))).thenReturn(response);
        assertFalse(enterpriseUserIdService.saveUserId(getEnter()));
        
        response.setResCode("0000000");
        SCOpenResponseOutData outData = new SCOpenResponseOutData();
        outData.setUSER_ID("111");
        response.setOutData(outData);
        when(scOpenService.sendOpenRequest(Mockito.any(SCOpenRequest.class))).thenReturn(response);
        
        when(enterpriseUserIdMapper.insert(Mockito.any(EnterpriseUserId.class))).thenReturn(0);
        assertFalse(enterpriseUserIdService.saveUserId(getEnter()));
        
        when(enterpriseUserIdMapper.insert(Mockito.any(EnterpriseUserId.class))).thenReturn(1);
        assertTrue(enterpriseUserIdService.saveUserId(getEnter()));
    }


    private Enterprise getEnter() {
        Enterprise enter = new Enterprise();
        enter.setDiscount(1L);
        enter.setId(1L);
        enter.setCode("123");
        return enter;
    }
    
    private Discount getDiscount(){
        Discount discount = new Discount();
        discount.setDiscount(95d);
        discount.setId(1L);
        discount.setName("95折");
        return discount;
    }
    
}
