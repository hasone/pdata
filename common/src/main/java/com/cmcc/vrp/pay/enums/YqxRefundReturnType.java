package com.cmcc.vrp.pay.enums;

/**
 * 
 * RefundType
 * 
 *
 */
public enum YqxRefundReturnType {
    SUCCESS("退款成功","200"),
    ACCEPTED("退款请求受理" , "202"),  
    REFUNDFAILED("退款失败" , "201"),
    ANALYSEFAILED("返回xml解析失败" , "203"),
    NOTEXIST("记录不存在","101"),
    GENEURLFAILED("生成url失败" , "102"),
    HTTPERROR("HTTP请求失败" , "103"),
    DBERROR("数据库操作失败" , "104"),
    UNKNOWN("未知错误" , "100");
    
    private String desc;
    
    private String code;

    private YqxRefundReturnType(String desc, String code) {
        this.desc = desc;
        this.code = code;
    }
    
    /**
     * getTypeFromCode
     */
    public static YqxRefundReturnType getTypeFromCode(String code){
        for(YqxRefundReturnType type : YqxRefundReturnType.values()){
            if(code.equals(type.getCode())){
                return type;
            }
        }
        return YqxRefundReturnType.UNKNOWN;
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
