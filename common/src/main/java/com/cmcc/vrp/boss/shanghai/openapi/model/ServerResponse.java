package com.cmcc.vrp.boss.shanghai.openapi.model;

import java.util.Date;

import com.cmcc.vrp.boss.shanghai.openapi.util.JsonUtil;


/**
 * @author lgk8023
 *
 */
public class ServerResponse {
    private Date sendDate;
    private String status;
    private String result;
    private String exceptionCode;
    private String errorCode;

    public ServerResponse() {

    }

    public ServerResponse(String result) {
        sendDate = new Date();
        this.status = ResponseStatus.SUCCESS.toString();
        this.result = result;
    }

    public ServerResponse(String result, String exceptionCode) {
        sendDate = new Date();
        this.result = result;
        this.status = ResponseStatus.ERROR.toString();
        this.exceptionCode = exceptionCode;
        this.errorCode = this.exceptionCode;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     *  (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}
