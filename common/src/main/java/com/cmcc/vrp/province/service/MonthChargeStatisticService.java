package com.cmcc.vrp.province.service;

import java.util.Date;
import java.util.List;

import com.cmcc.vrp.province.model.ChargeStatistic;

/**
 * MonthChargeStatisticService.java
 */
public interface MonthChargeStatisticService {
    
    /** 
     * @Title: batchInsert 
     */
    boolean batchInsert(List<ChargeStatistic> list);

    /** 
     * @Title: getMonthChargeStatistic 
     */
    List<ChargeStatistic> getMonthChargeStatistic(Date date);

}
