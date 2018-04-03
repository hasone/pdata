package com.cmcc.vrp.province.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cmcc.vrp.province.dao.IndividualActivityOrderMapper;
import com.cmcc.vrp.province.model.IndividualActivityOrder;
import com.cmcc.vrp.province.service.IndividualActivityOrderService;

/**
 * IndividualActivityOrderServiceImpl.java
 * @author wujiamin
 * @date 2017年3月22日
 */
@Service("individualActivityOrder")
public class IndividualActivityOrderServiceImpl implements IndividualActivityOrderService {
    @Autowired
    IndividualActivityOrderMapper mapper;
    
    @Override
    public boolean insert(String activityId, Long orderId) {
        if(StringUtils.isEmpty(activityId) || orderId == null){
            return false;
        }
        IndividualActivityOrder record = new IndividualActivityOrder();
        record.setActivityId(activityId);
        record.setOrderId(orderId);
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setDeleteFlag(0);
        return mapper.insert(record) == 1;
    }

}
