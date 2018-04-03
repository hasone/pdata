package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.CityFeeMapper;
import com.cmcc.vrp.province.model.CityFee;
import com.cmcc.vrp.province.service.CityFeeService;

@RunWith(MockitoJUnitRunner.class)
public class CityFeeServiceImplTest {

    @InjectMocks
    CityFeeService cityFeeService = new CityFeeServiceImpl();

    @Mock
    CityFeeMapper cityFeeMapper;

    @Test
    public void selectByCityCodeTest() {
        assertNull(cityFeeService.getByCityCodeAndFeecode(null, null));

        HashMap<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("cityCode", "123456");
        queryMap.put("feeCode", "456");

        CityFee cityFee = new CityFee();

        Mockito.when(cityFeeMapper.getByCityCodeAndFeecode(queryMap)).thenReturn(cityFee);

        assertEquals(cityFeeService.getByCityCodeAndFeecode("123456", "456"), cityFee);

    }

}
