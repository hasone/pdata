package com.cmcc.vrp.province.service.impl;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.SmsTemplateMapper;
import com.cmcc.vrp.province.service.SmsTemplateService;

/**
 * @author lgk8023
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SmsTemplateServiceImplTest2 {
    @InjectMocks
    SmsTemplateService eService = new SmsTemplateServiceImpl();
    @Mock
    SmsTemplateMapper mapper;
    
    /**
     * 
     */
    @Test
    public void testCheckSms() {
        Mockito.when(mapper.checkSms(Mockito.anyString())).thenReturn(null);
        Assert.assertNull(eService.checkSms("124"));
    }
    /**
     * 
     */
    @Test
    public void testCountSmsTemplate() {
        Mockito.when(mapper.countSmsTemplate(Mockito.anyMap())).thenReturn(1);
        Assert.assertNotNull(eService.countSmsTemplate(new HashMap<String, Object>()));
    }
}
