package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.enums.IspType;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.JudgeIspService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * Created by sunyiwei on 2016/6/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class JudgeIspServiceImplTest {
    @InjectMocks
    JudgeIspService judgeIspService = new JudgeIspServiceImpl();
    
    @Mock
    GlobalConfigService globalConfigService;


    @Test
    public void testJudgeIsp1(){
        when(globalConfigService.get(GlobalConfigKeyEnum.CMCC_MOBILE_FLAG.getKey())).thenReturn("134,135,136,137,138,139,147,150,151,152,157,158,159,187,188");
        when(globalConfigService.get(GlobalConfigKeyEnum.TELECOM_MOBILE_FLAG.getKey())).thenReturn("133,153,180,189");
        when(globalConfigService.get(GlobalConfigKeyEnum.UNICOM_MOBILE_FLAG.getKey())).thenReturn("130,131,132,155,156,185,186");

        assertNull(judgeIspService.judgeIsp("17767103571"));
        assertEquals(judgeIspService.judgeIsp("18867103571"),IspType.CMCC.getValue());
        assertEquals(judgeIspService.judgeIsp("13367103571"),IspType.TELECOM.getValue());
        assertEquals(judgeIspService.judgeIsp("13067103571"),IspType.UNICOM.getValue());
    }
    
    @Test
    public void testJudgeIsp2(){
        String[] cmMobile = {"134","135","136","137","138","139","147","150","151","152","157","158","159","187","188"};        
        String[] ctMobile = {"133","153","180","189"};
        String[] cuMobile = {"130","131","132","155","156","185","186"};

        assertNull(judgeIspService.judgeIsp("17767103571", cmMobile, cuMobile, ctMobile));
        assertEquals(judgeIspService.judgeIsp("18867103571", cmMobile, cuMobile, ctMobile),IspType.CMCC.getValue());
        assertEquals(judgeIspService.judgeIsp("13367103571", cmMobile, cuMobile, ctMobile),IspType.TELECOM.getValue());
        assertEquals(judgeIspService.judgeIsp("13067103571", cmMobile, cuMobile, ctMobile),IspType.UNICOM.getValue());
    }
}