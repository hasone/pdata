package com.cmcc.vrp.queue.pojo;

/**
 * Created by qihang on 2017/5/10.
 */
public class YqxChargePojo {
    private String payOrderId;
    
    private String payTransactionId;
    
    public YqxChargePojo(String payOrderId, String payTransactionId) {
        this.payOrderId = payOrderId;
        this.payTransactionId = payTransactionId;
    }
    
    public YqxChargePojo(){
        
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getPayTransactionId() {
        return payTransactionId;
    }

    public void setPayTransactionId(String payTransactionId) {
        this.payTransactionId = payTransactionId;
    }

    
    
    
}
