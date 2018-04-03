package com.cmcc.vrp.pay.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 退款返回解析
 * 同步时只有returnCode和returnMsg
 * 异步时都存在
 *
 */
@XStreamAlias("BusiData")
public class RefundAsyncResponse {
    @XStreamAlias("OrderId")
    private String orderId;  //支付订单编号
    
    @XStreamAlias("TradeNO")
    private String tradeNo;  //支付订单编号
    
    @XStreamAlias("RefundOrderId")
    private String refundOrderId;  //业务平台退款流水
    
    @XStreamAlias("ReturnCode")
    private String returnCode;  //返回码
    
    @XStreamAlias("ReturnMsg")
    private String returnMsg;  //返回信息

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getRefundOrderId() {
        return refundOrderId;
    }

    public void setRefundOrderId(String refundOrderId) {
        this.refundOrderId = refundOrderId;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
    
    
}
