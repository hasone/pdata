package com.cmcc.vrp.province.service;

import java.util.Date;
import java.util.List;

import com.cmcc.vrp.province.model.ChargeStatistic;

/**
 * YearChargeStatisticService.java
 */
public interface YearChargeStatisticService {

    List<ChargeStatistic> getYearChargeStatistic(Date date);

    /** 
     * @Title: batchInsert 
    */
    boolean batchInsert(List<ChargeStatistic> list);

}
