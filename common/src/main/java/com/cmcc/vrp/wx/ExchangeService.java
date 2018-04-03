package com.cmcc.vrp.wx;

import com.alibaba.fastjson.JSONObject;

/**
 * 积分兑换
 * ExchangeService.java
 * @author wujiamin
 * @date 2017年3月15日
 */
public interface ExchangeService {
    /** 
     * 积分兑换操作
     * @Title: operateExchange 
     */
    public boolean operateExchange(String mobile, Long adminId, String prdCode, JSONObject json);
}
