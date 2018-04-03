package com.cmcc.vrp.province.activity.model;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:03:38
*/
public class AutoResponsePojo {
    public static final Integer SUCCESS_CODE = 200;
    public static final Integer INTERVAL_ERROR_CODE = 500;
    public static final Integer BAD_REQUEST_CODE = 400;
    private int code;
    private String url;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}