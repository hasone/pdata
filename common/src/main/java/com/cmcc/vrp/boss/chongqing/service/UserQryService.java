package com.cmcc.vrp.boss.chongqing.service;

/**
 * 套餐使用情况查询、流量使用情况查询、流量余额查询
 * @author lgk8023
 *
 */
public interface UserQryService {
    
    /**
     * 套餐使用情况查询：入参TYPE传0，表示查询所有业务使用情况。
     * 流量使用情况查询：入参TYPE传4a，表示查询GPRS流量。
     * 流量余额查询：入参type默认传4b，表示查询WLAN流量。
     * @param mobile
     * @param type 
     * @return
     */
    public String qryLeftTraffic(String mobile, String type, String cycle);

}
