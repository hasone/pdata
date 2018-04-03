package com.cmcc.vrp.enums;

/**
 * 产品转化的所有类型，当前有黑白名单
 * 
 * @author qihang
 *
 */
public enum ProductConverterType {
    
    BLACKLIST("black","黑名单"),
    WHITELIST("white","白名单"),
    NOUSE("nouse","不使用转化关系");
    
    private String type;
    
    private String msg;
    
    
    private ProductConverterType(String type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    
}
