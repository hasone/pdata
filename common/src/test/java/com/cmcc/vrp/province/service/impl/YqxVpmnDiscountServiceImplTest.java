package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.YqxVpmnDiscountMapper;
import com.cmcc.vrp.province.model.YqxVpmnDiscount;
import com.cmcc.vrp.province.service.YqxVpmnDiscountService;

/**
 * YqxVpmnDiscountServiceImplTest.java
 * @author wujiamin
 * @date 2017年5月9日
 */
@RunWith(MockitoJUnitRunner.class)
public class YqxVpmnDiscountServiceImplTest {
    @InjectMocks
    YqxVpmnDiscountService service = new YqxVpmnDiscountServiceImpl();
    
    @Mock
    YqxVpmnDiscountMapper mapper;
    
    /** 
     * @Title: testGetDiscountByDate 
     */
    @Test
    public void testGetDiscountByDate() {
        List<YqxVpmnDiscount> discounts = new ArrayList<YqxVpmnDiscount>();
        YqxVpmnDiscount d = new YqxVpmnDiscount();
        d.setDiscount(100);
        d.setStart(1);
        d.setEnd(2);
        discounts.add(d);
        Mockito.when(mapper.getAllDiscount()).thenReturn(discounts);
        assertNull(service.getDiscountByDate(3));
        assertNotNull(service.getDiscountByDate(1));
    }
}
