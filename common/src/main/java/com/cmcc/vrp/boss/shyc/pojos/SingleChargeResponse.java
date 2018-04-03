package com.cmcc.vrp.boss.shyc.pojos;

/**
 * 上海月呈的单号码充值响应
 *
 * Created by sunyiwei on 2017/3/14.
 */
public class SingleChargeResponse {
    private String taskId;
    private String code;
    private String message;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

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
