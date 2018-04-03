package com.cmcc.vrp.ec.bean.individual;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("OrderResponse")
public class OrderResponse {
    @XStreamAlias("Code")
    String code;
    
    @XStreamAlias("Message")
    String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    } 
}
