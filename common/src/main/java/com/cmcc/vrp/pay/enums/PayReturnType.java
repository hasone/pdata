package com.cmcc.vrp.pay.enums;

/**
 * 
 * PayReturnType
 *
 */
public enum PayReturnType {
    UNKNOWN("未知错误" , "-1"),
    SUCCESS("成功" , "0"),
    FAILD("失败" , "1"),
    WAIT("等待支付" , "2"),
    WAITRETURN("等待支付平台返回" , "3");
    
    private String desc;
    
    private String code;

    private PayReturnType(String desc, String code) {
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
