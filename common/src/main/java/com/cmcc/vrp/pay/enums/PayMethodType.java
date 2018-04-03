package com.cmcc.vrp.pay.enums;

/**
 * 
 * PayMethodType
 *
 */
public enum PayMethodType {
    HEBAO("和包" , "1"),
    ALIPAY("支付宝" , "2"),
    WECHAT("微信" , "4"),
    UNIONPAY("银联" , "5"),
    GANSU("甘肃和包" , "8");
    
    private String desc;
    
    private String code;

    private PayMethodType(String desc, String code) {
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
