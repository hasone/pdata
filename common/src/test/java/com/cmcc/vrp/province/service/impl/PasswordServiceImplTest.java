package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.PasswordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;

/**
 * Created by qinqinyan on 2016/11/4.
 */
@RunWith(MockitoJUnitRunner.class)
public class PasswordServiceImplTest {
    @InjectMocks
    PasswordService passwordService = new PasswordServiceImpl();
    @Mock
    GlobalConfigService globalConfigService;

    @Test
    public void testGenerate(){
        Mockito.when(globalConfigService.get("serialNumKey")).thenReturn("1111");
        Mockito.when(globalConfigService.updateValue(anyString(), anyString(), anyString())).thenReturn(true);
        assertNotNull(passwordService.generate());
        Mockito.verify(globalConfigService).get("serialNumKey");
        Mockito.verify(globalConfigService).updateValue(anyString(), anyString(), anyString());
    }

    @Test
    public void testGenerate1(){
        Mockito.when(globalConfigService.get("serialNumKey")).thenReturn("1111");
        Mockito.when(globalConfigService.updateValue(anyString(), anyString(), anyString())).thenReturn(false);
        assertNull(passwordService.generate());
        Mockito.verify(globalConfigService).get("serialNumKey");
        Mockito.verify(globalConfigService).updateValue(anyString(), anyString(), anyString());
    }

    @Test
    public void testIsValidate(){
        String password = "46456464";
        assertFalse(passwordService.isValidate(password));

        String password1 = "12345698741236";
        assertTrue(passwordService.isValidate(password1));
    }

    @Test
    public void testGetYear() throws Exception{
        String password1 = "12345698741236";
        assertNotSame(0, passwordService.getYear(password1));
    }

    @Test(expected = Exception.class)
    public void testGetYear1() throws Exception{
        String password1 = "123456987";
        assertNotSame(0, passwordService.getYear(password1));
    }

    @Test
    public void testBatchGenerate(){
        int count = 0;
        assertNull(passwordService.batchGenerate(count));

        int count1 = 1;
        Mockito.when(globalConfigService.get("serialNumKey")).thenReturn("11");
        Mockito.when(globalConfigService.updateValue(anyString(), anyString(), anyString())).thenReturn(false);
        assertNull(passwordService.batchGenerate(count1));
        Mockito.verify(globalConfigService).get(anyString());
        Mockito.verify(globalConfigService).updateValue(anyString(), anyString(), anyString());
    }

    @Test
    public void testBatchGenerate1(){
        int count2 = 1;
        Mockito.when(globalConfigService.get("serialNumKey")).thenReturn("11");
        Mockito.when(globalConfigService.updateValue(anyString(), anyString(), anyString())).thenReturn(true);
        assertNotNull(passwordService.batchGenerate(count2));
        Mockito.verify(globalConfigService).get(anyString());
        Mockito.verify(globalConfigService).updateValue(anyString(), anyString(), anyString());

    }
}
