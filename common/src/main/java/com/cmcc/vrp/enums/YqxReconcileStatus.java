package com.cmcc.vrp.enums;

/**
 * 云企信对账状态
 *
 */
public enum YqxReconcileStatus {
    
    NOSTART(0, "未对账"),
    SUCCESS(1, "成功"),
    FAILED(2, "失败");
    
    private Integer code;

    private String message;
    
    

    private YqxReconcileStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
}
