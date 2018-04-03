package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.DayChargeStatisticMapper;
import com.cmcc.vrp.province.model.ChargeStatistic;
import com.cmcc.vrp.province.service.DayChargeStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * DayChargeStatisticServiceImpl
 * */
@Service("dayChargeStatisticService")
public class DayChargeStatisticServiceImpl implements DayChargeStatisticService {
    @Autowired
    DayChargeStatisticMapper dayChargeStatisticMapper;
    
    @Override
    public boolean batchInsert(List<ChargeStatistic> records) {
        return dayChargeStatisticMapper.batchInsert(records) == records.size();
    }

    @Override
    public List<ChargeStatistic> getDayChargeStatistic(Date date) {
        return dayChargeStatisticMapper.getDayChargeStatistic(date);
    }

}
