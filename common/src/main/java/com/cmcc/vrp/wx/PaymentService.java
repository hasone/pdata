package com.cmcc.vrp.wx;

import com.cmcc.vrp.wx.beans.PayParameter;

/**
 * Created by leelyn on 2017/1/6.
 */
public interface PaymentService {

    /** 
     * 封装支付参数
     * @Title: combinePayPara 
     */
    public String combinePayPara(PayParameter parameter);

    /** 
     * 发送支付请求
     * @Title: sendChargeRequest 
     */
    public boolean sendChargeRequest(String param);
}
