package com.cmcc.vrp.pay.enums;

/**
 * 
 * RefundType
 *
 */
public enum RefundType {
    BUSINESS("业务退款" , "0"),    //0714新版  1->0
    ACCOUNT("对账退款" , "1");     //0714新版  2->1
    
    private String desc;
    
    private String code;

    private RefundType(String desc, String code) {
        this.desc = desc;
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    
}
