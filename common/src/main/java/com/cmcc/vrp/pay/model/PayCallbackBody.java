package com.cmcc.vrp.pay.model;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * PayCallbackBody
 *
 */
public class PayCallbackBody {
    @XStreamAlias("Status")
    private String status;
     
    @XStreamAlias("StatusInfo")
    private String statusInfo;
     
    @XStreamAlias("OrderId")
    private String orderId;
     
    @XStreamAlias("DoneCode")
    private String doneCode;
     
    @XStreamAlias("PayAmount")
    private String payAmount;
     
    @XStreamAlias("PayDate")
    private String payDate;
     
    @XStreamAlias("Type")
    private String type;
     
    @XStreamAlias("thirdAmount")
    private String thirdAmount;
     
    @XStreamAlias("VirtualCurrency")
    private String virtualCurrency;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(String statusInfo) {
        this.statusInfo = statusInfo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDoneCode() {
        return doneCode;
    }

    public void setDoneCode(String doneCode) {
        this.doneCode = doneCode;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThirdAmount() {
        return thirdAmount;
    }

    public void setThirdAmount(String thirdAmount) {
        this.thirdAmount = thirdAmount;
    }

    public String getVirtualCurrency() {
        return virtualCurrency;
    }

    public void setVirtualCurrency(String virtualCurrency) {
        this.virtualCurrency = virtualCurrency;
    }
     
    /**
     * putParamsToMap
     */
    public boolean putParamsToMap(Map<String, String> map){
        if(StringUtils.isNotBlank(status)){
            map.put("Status", status);
        }
        
        if(StringUtils.isNotBlank(statusInfo)){
            map.put("StatusInfo", statusInfo);
        }
        
        if(StringUtils.isNotBlank(orderId)){
            map.put("OrderId", orderId);
        }
        
        if(StringUtils.isNotBlank(payAmount)){
            map.put("PayAmount", payAmount);
        }
        
        if(StringUtils.isNotBlank(doneCode)){
            map.put("DoneCode", doneCode);
        }
        
        if(StringUtils.isNotBlank(payDate)){
            map.put("PayDate", payDate);
        }
        
        if(StringUtils.isNotBlank(type)){
            map.put("Type", type);
        }
        
        if(StringUtils.isNotBlank(thirdAmount)){
            map.put("thirdAmount", thirdAmount);
        }
        
        if(StringUtils.isNotBlank(virtualCurrency)){
            map.put("VirtualCurrency", virtualCurrency);
        }
        return true;
    } 

}
