package com.cmcc.vrp.province.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.service.XssService;

@RunWith(MockitoJUnitRunner.class)
public class XssServiceImplTest {
    @InjectMocks
    XssService xssService = new XssServiceImpl();
    
    @Test
    public void testStripXss(){
        String value = "<\"'a&>";
        xssService.stripXss(value);
    }
    
    @Test
    public void testStripQuot(){
        String value = "<\"'a&>";
        xssService.stripXss(value);
    }

}
