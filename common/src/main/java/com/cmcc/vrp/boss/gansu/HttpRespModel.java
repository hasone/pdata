package com.cmcc.vrp.boss.gansu;

/**
 * 
 * HttpRespModel
 *
 */
public class HttpRespModel {
    private int code = -1;
    
    private String msg = "";
    
    public HttpRespModel(){
        
    }

    public HttpRespModel(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    
    
}
