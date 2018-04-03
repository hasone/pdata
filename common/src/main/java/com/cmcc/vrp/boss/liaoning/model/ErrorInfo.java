package com.cmcc.vrp.boss.liaoning.model;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年2月22日 下午5:30:36
*/
public class ErrorInfo {
    
    private String Code;
    
    private String DealTime;
    
    private String Message;
    
    private String Hint;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getDealTime() {
        return DealTime;
    }

    public void setDealTime(String dealTime) {
        DealTime = dealTime;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getHint() {
        return Hint;
    }

    public void setHint(String hint) {
        Hint = hint;
    }

}
