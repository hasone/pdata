package com.cmcc.vrp.boss.liaoning.model;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年2月22日 下午5:30:24
*/
public class Response {
    private ErrorInfo ErrorInfo;

    private RetInfo RetInfo;

    public ErrorInfo getErrorInfo() {
        return ErrorInfo;
    }

    public void setErrorInfo(ErrorInfo errorInfo) {
        ErrorInfo = errorInfo;
    }

    public RetInfo getRetInfo() {
        return RetInfo;
    }

    public void setRetInfo(RetInfo retInfo) {
        RetInfo = retInfo;
    }
    
}
