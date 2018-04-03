package com.cmcc.vrp.ec.bean.individual;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * RedpacketCallbackResp.java
 * @author wujiamin
 * @date 2017年1月13日
 */
@XStreamAlias("Response")
public class RedpacketCallbackResp {
    @XStreamAlias("Code")
    private String code;

    @XStreamAlias("Message")
    private String message;
    
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
