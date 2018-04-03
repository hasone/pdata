package com.cmcc.vrp.province.reconcile.service;

/**
 * 对账服务类，对外只提供doDailyJob的方法
 *
 */
public interface ReconcileService {
    /**
     * 每日的对账任务，结合了所有的逻辑
     * @return
     */
    public boolean doDailyJob();
}
