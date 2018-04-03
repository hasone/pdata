/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.DiscountMapper;
import com.cmcc.vrp.province.model.Discount;
import com.cmcc.vrp.province.service.DiscountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>Title:DiscountServiceImpl </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年7月25日
 */
@Service("discountService")
public class DiscountServiceImpl implements DiscountService {

    private static final Logger logger = LoggerFactory.getLogger(DiscountService.class);

    @Autowired
    private DiscountMapper discountMapper;

    @Override
    public List<Discount> selectAllDiscount() {
        List<Discount> discounts = discountMapper.selectAllDiscount();
        for (Discount discount : discounts) {
            discount.setDiscount(discount.getDiscount() * 100);
        }
        return discounts;
    }

    @Override
    public Discount selectByPrimaryKey(Long id) {
        return discountMapper.selectByPrimaryKey(id);
    }
}
