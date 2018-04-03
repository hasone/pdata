package com.cmcc.vrp.ec.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 广东登录交互返回模型
 *
 */
public class RespModel {
    @SerializedName(value="error")
    int error;
    
    @SerializedName(value="src")
    String src;
    
    @SerializedName(value="msg")
    String msg;
    
    public RespModel() {

    }

    public RespModel(int error, String src, String msg) {

        this.error = error;
        this.src = src;
        this.msg = msg;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    
}
