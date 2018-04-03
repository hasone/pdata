package com.cmcc.vrp.province.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.YqxVpmnDiscountMapper;
import com.cmcc.vrp.province.model.YqxVpmnDiscount;
import com.cmcc.vrp.province.service.YqxVpmnDiscountService;

/**
 * YqxVpmnDiscountServiceImpl.java
 * @author wujiamin
 * @date 2017年5月9日
 */
@Service
public class YqxVpmnDiscountServiceImpl implements YqxVpmnDiscountService {
    @Autowired
    YqxVpmnDiscountMapper mapper;
    
    @Override
    public Integer getDiscountByDate(Integer date) {
        List<YqxVpmnDiscount> discounts = mapper.getAllDiscount();
        for(YqxVpmnDiscount discount : discounts){
            if(date >= discount.getStart() && date < discount.getEnd()){
                return discount.getDiscount();
            }
        }
        return null;
    }
}
