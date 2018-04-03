package com.cmcc.vrp.province.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.MonthChargeStatisticMapper;
import com.cmcc.vrp.province.model.ChargeStatistic;
import com.cmcc.vrp.province.service.MonthChargeStatisticService;

@Service("monthChargeStatisticService")
public class MonthChargeStatisticServiceImpl implements MonthChargeStatisticService {
    @Autowired
    MonthChargeStatisticMapper mapper;

    @Override
    public boolean batchInsert(List<ChargeStatistic> list) {
        return mapper.batchInsert(list) == list.size();
    }

    @Override
    public List<ChargeStatistic> getMonthChargeStatistic(Date date) {
        return mapper.getMonthChargeStatistic(date);
    }
}
