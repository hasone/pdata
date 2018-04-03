package com.cmcc.vrp.province.service;

import java.util.Date;
import java.util.List;

import com.cmcc.vrp.province.model.ChargeStatistic;

/**
 *
 *
 */
public interface DayChargeStatisticService {

    /**
     * @param records
     * @return
     */
    boolean batchInsert(List<ChargeStatistic> records);

    List<ChargeStatistic> getDayChargeStatistic(Date date);

}
