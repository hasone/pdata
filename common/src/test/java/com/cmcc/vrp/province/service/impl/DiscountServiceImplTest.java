package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.DiscountMapper;
import com.cmcc.vrp.province.model.Discount;
import com.cmcc.vrp.province.service.DiscountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyLong;

/**
 * Created by qinqinyan on 2016/11/4.
 */
@RunWith(MockitoJUnitRunner.class)
public class DiscountServiceImplTest {
    @InjectMocks
    DiscountService discountService = new DiscountServiceImpl();
    @Mock
    DiscountMapper discountMapper;

    @Test
    public void testSelectAllDiscount(){
        List<Discount> discounts = new ArrayList<Discount>();
        Discount discount = new Discount();
        discount.setDiscount(0.0);
        discounts.add(discount);

        Mockito.when(discountMapper.selectAllDiscount()).thenReturn(discounts);
        assertNotNull(discountService.selectAllDiscount());
        Mockito.verify(discountMapper).selectAllDiscount();
    }

    @Test
    public void testSelectByPrimaryKey(){
        Mockito.when(discountMapper.selectByPrimaryKey(anyLong())).thenReturn(new Discount());
        assertNotNull(discountService.selectByPrimaryKey(anyLong()));
        Mockito.verify(discountMapper).selectByPrimaryKey(anyLong());
    }

}
