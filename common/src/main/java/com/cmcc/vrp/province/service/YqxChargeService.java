package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.AsyncCallbackReq;


/**
 * YqxChargeService.java
 * @author wujiamin
 * @date 2017年5月10日
 */
public interface YqxChargeService {
    /** 
     * @Title: charge 
     */
    public boolean charge(String payTransactionId);

    /** 
     * 处理回调
     * @Title: processingCallback 
     */
    public boolean processingCallback(AsyncCallbackReq acr);
}
