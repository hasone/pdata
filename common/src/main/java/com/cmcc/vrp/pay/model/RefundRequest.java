package com.cmcc.vrp.pay.model;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 退款请求
 *
 */
@XStreamAlias("BusiData")
public class RefundRequest implements PayReqBasicBody{
    
    @XStreamAlias("OrderId")
    private String tradeNo;  //支付订单编号
    
    @XStreamAlias("RefundOrderId")
    private String refundOrderId;  //业务平台退款流水
    
    @XStreamAlias("refundNotifyURL")
    private String refundNotifyUrl;  //退款结果通知URL
    
    @XStreamAlias("refundReason")
    private String refundReason;  //退款原因
    
    @XStreamAlias("RefundAmount")
    private String refundAmount;  //退款金额
    
    @XStreamAlias("RefundType")
    private String refundType;  //退款类型
    
    
    public RefundRequest(String tradeNo, String refundOrderId,
            String refundNotifyUrl, String refundReason, String refundAmount,
            String refundType) {
        super();
        this.tradeNo = tradeNo;
        this.refundOrderId = refundOrderId;
        this.refundNotifyUrl = refundNotifyUrl;
        this.refundReason = refundReason;
        this.refundAmount = refundAmount;
        this.refundType = refundType;
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

    public String getRefundNotifyUrl() {
        return refundNotifyUrl;
    }

    public void setRefundNotifyUrl(String refundNotifyUrl) {
        this.refundNotifyUrl = refundNotifyUrl;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }

    /**
     * putParamsToMap
     */
    @Override
    public boolean putParamsToMap(Map<String, String> map) {
        if(StringUtils.isNotBlank(tradeNo)){
            map.put("OrderId", tradeNo);
        }
        if(StringUtils.isNotBlank(refundOrderId)){
            map.put("RefundOrderId", refundOrderId);
        }
        if(StringUtils.isNotBlank(refundNotifyUrl)){
            map.put("refundNotifyURL", refundNotifyUrl);
        }
        if(StringUtils.isNotBlank(refundReason)){
            map.put("refundReason", refundReason);
        }
        if(StringUtils.isNotBlank(refundAmount)){
            map.put("RefundAmount", refundAmount);
        }
        if(StringUtils.isNotBlank(refundType)){
            map.put("RefundType", refundType);
        }
        return true;
    }

    /**
     * encode
     */
    @Override
    public void encode() {
        /*try {
            refundReason = URLEncoder.encode(refundReason, "UTF-8");
            refundNotifyUrl = URLEncoder.encode(refundNotifyUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        
    }
    
    
}
