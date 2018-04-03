package com.cmcc.vrp.province.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.YearChargeStatisticMapper;
import com.cmcc.vrp.province.model.ChargeStatistic;
import com.cmcc.vrp.province.service.YearChargeStatisticService;

@Service("yearChargeStatisticService")
public class YearChargeStatisticServiceImpl implements YearChargeStatisticService {
    @Autowired
    YearChargeStatisticMapper mapper;

    @Override
    public boolean batchInsert(List<ChargeStatistic> list) {
        return mapper.batchInsert(list) == list.size();
    }

    @Override
    public List<ChargeStatistic> getYearChargeStatistic(Date date) {
        return mapper.getYearChargeStatistic(date);
    }

}
