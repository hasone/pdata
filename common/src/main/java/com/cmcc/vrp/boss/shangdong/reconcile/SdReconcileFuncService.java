package com.cmcc.vrp.boss.shangdong.reconcile;



/**
 * 山东对账使用到的服务类，方便汇总时直接使用mock写UT
 *
 */
public interface SdReconcileFuncService {
    /**
     * 每日的对账任务，结合了所有的逻辑
     * @return
     */
    public boolean doDailyJob();
    
    /**
     * 每月的对账任务，结合了所有的逻辑
     * @return
     */
    public boolean doMonthlyJob();
}
