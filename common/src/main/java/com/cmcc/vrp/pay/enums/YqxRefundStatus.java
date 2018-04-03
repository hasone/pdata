package com.cmcc.vrp.pay.enums;

/**
 * 
 * YqxRefundStatus
 *
 */
public enum YqxRefundStatus {
    
    WAIT(0,"待发送"),
    ACCETED(1,"受理成功"),
    REFUSED(2,"受理失败"),
    SUCCESS(3,"退款成功"),
    FAILED(4,"退款失败");
    
    private int status;
    private String desc;

    private YqxRefundStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    
    
}
