package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.SdBossProductMapper;
import com.cmcc.vrp.province.model.SdBossProduct;
import com.cmcc.vrp.province.service.SdBossProductService;
/**
 * 
 * @ClassName: SdBossProductServiceImplTest 
 * @Description: TODO
 * @author: Rowe
 * @date: 2017年8月31日 下午4:06:33
 */
@RunWith(MockitoJUnitRunner.class)
public class SdBossProductServiceImplTest {

    @InjectMocks
    SdBossProductService sdBossProductService = new SdBossProductServiceImpl();
    
    @Mock
    SdBossProductMapper sdBossProductMapper;
    
    @Test
    public void testSelectByCode() {
        //invalid
        assertNull(sdBossProductService.selectByCode(null));     
        //valid
        when(sdBossProductMapper.selectByCode(Mockito.anyString())).thenReturn(new SdBossProduct());
        assertNotNull(sdBossProductService.selectByCode("123"));
    }
}
