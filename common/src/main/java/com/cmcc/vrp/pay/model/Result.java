package com.cmcc.vrp.pay.model;

import org.apache.commons.lang.StringUtils;

import com.cmcc.vrp.pay.enums.YqxRefundReturnType;

/**
 * 
 * Result
 *
 */
public class Result {
    private String code;
    
    private String msg;

    public Result(String code, String msg) {
        this.code = code;
        this.msg = msg;
        if(StringUtils.isBlank(msg)){
            this.msg = "";
        }
    }
    
    public Result(YqxRefundReturnType type){
        if(type == null){
            type = YqxRefundReturnType.UNKNOWN;
        }
        this.code = type.getCode();
        this.msg = type.getDesc();
    }
   
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    
}
